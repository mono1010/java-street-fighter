package ch.teko.game.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.css.Rect;

import ch.teko.game.model.*;
import ch.teko.game.utils.*;
import ch.teko.game.view.*;
import ch.teko.game.controllers.*;
import ch.teko.game.Main;

enum AnimationUnlockAction {
    NONE,
    FALL,
}

public class Fighter extends Entity {
    private float walkSpeed = 5;
    private float health = 100;
    private float maxHealth = 100;

    private boolean isPlayer1;

    private AssetsManager assetsManager;
    private Animate animate;

    boolean isJumping = false;

    AnimationState animationState;
    PlayerControlls input;

    private Logger log = LogManager.getLogger(Main.class);

    public Fighter(int x, int y, boolean isPlayer1, String assetsDir) {
        super(x, y, 0, 0);

        this.isPlayer1 = isPlayer1;
        this.assetsManager = new AssetsManager(assetsDir);
        this.animate = new Animate();
        this.animate.animate(this.assetsManager.getAsset(Asset.State.IDLE));
        this.animationState = new AnimationState(animate, assetsManager);
        this.input = this.getInputController();

        setPositionRelativeToAABB(x, y);
    }

    public Fighter(int x, int y, boolean isPlayer1, AssetsManager assetsManager) {
        super(x, y, 0, 0);

        this.isPlayer1 = isPlayer1;
        this.assetsManager = assetsManager;
        this.animate = new Animate();
        this.animate.animate(this.assetsManager.getAsset(Asset.State.IDLE));
        this.animationState = new AnimationState(animate, assetsManager);
        this.input = this.getInputController();

        setPositionRelativeToAABB(x, y);
    }

    public AssetsManager getAssetsManager() {
        return this.assetsManager;
    }

    public void flipAsset() {
        this.animate.flipAsset();
    }

    PlayerControlls getInputController() {
        if (isPlayer1)
            return InputController.getInstance().player1;
        else
            return InputController.getInstance().player2;
    }

    void setAnimation() {
        if (this.input.right) {
            if (this.animate.getFlipAsset() != false)
                this.animate.flipAsset();
        } else if (this.input.left) {
            if (this.animate.getFlipAsset() != true)
                this.animate.flipAsset();
        }

        if (this.input.right)
            animationState.triggerAnimation(Asset.State.RUN);

        if (this.input.left)
            animationState.triggerAnimation(Asset.State.RUN);

        if (this.input.attack1)
            animationState.triggerAnimation(Asset.State.ATTACK1);

        if (this.input.attack2)
            animationState.triggerAnimation(Asset.State.ATTACK2);

        if (this.input.up)
            animationState.triggerAnimation(Asset.State.JUMP);

        animationState.triggerAnimation(Asset.State.IDLE);
    }

    void setVelocity() {
        if (this.animationState.canRun) {
            if (this.input.right) {
                this.velocityX += this.walkSpeed;
            } else if (this.input.left) {
                this.velocityX -= this.walkSpeed;
            }
        }

        if (this.animate.getCurrentAsset().state == Asset.State.JUMP) {
            this.velocityY -= 6;
            return;
        }

        if (this.animate.getCurrentAsset().state == Asset.State.FALL) {
            this.velocityY += 6;
            return;
        }
    }

    public float getMaxHealth() {
        return this.maxHealth;
    }

    public float getHealth() {
        return this.health;
    }

    public boolean isDead() {
        return this.health == 0.f;
    }

    public void damage(float damage) {
        this.health = Math.max(0, this.health - damage);
    }

    @Override
    public void onTick() {
        this.animationState.onTick();

        setAnimation();
        setVelocity();

        this.x += this.velocityX;
        this.y += this.velocityY;
        this.velocityX = 0;
        this.velocityY = 0;

        Rectangle aabb = this.getAABB();
        if (aabb.getY() + aabb.getHeight() > Floor.getInstance().getHeight()) {
            this.y -= aabb.getY() + aabb.getHeight() - Floor.getInstance().getHeight();
        }

        if (aabb.getX() < Map.getInstance().getX()) {
            this.x += Map.getInstance().getX() - aabb.getX();
        }

        if (aabb.getX() + aabb.getWidth() > Map.getInstance().getWidth()) {
            this.x -= aabb.getX() + aabb.getWidth() - Map.getInstance().getWidth();
        }
    }

    @Override
    public void onRender(Graphics g) {
        BufferedImage image = this.animate.get();
        g.drawImage(image, this.x, this.y, null);

        Rectangle boundingBox = this.getAABB();
        g.setColor(Color.RED);
        g.drawRect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);

        Optional<Rectangle> hitBoxOptional = this.getHitBox();
        if (hitBoxOptional.isPresent()) {
            Rectangle hitBox = hitBoxOptional.get();
            g.setColor(Color.ORANGE);
            g.drawRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
        }

        g.setColor(Color.BLACK);
        g.drawString("Frame: " + this.animate.getAnimationIndex(), boundingBox.x, boundingBox.y - 10);
    }

    public Optional<Rectangle> getHitBox() {
        BufferedImage image = this.animate.get();
        int width = image.getWidth() - 1;
        int height = image.getHeight() - 1;
        int sizeX = width - image.getMinX();
        int sizeY = height - image.getMinY();

        Optional<Rectangle> rect = Optional.empty();
        switch (this.animate.getCurrentAsset().state) {
            case ATTACK1:
                if (this.animate.getAnimationIndex() == this.animate.getAnimationMaxIndex() - 2) {
                    int offsetWidth = 85;
                    if (this.animate.getFlipAsset() == true) {
                        rect = Optional.of(new Rectangle(this.x + (sizeX / 2) - offsetWidth, this.y + (sizeY / 2) - 30,
                                offsetWidth, 60));
                    } else {
                        rect = Optional
                                .of(new Rectangle(this.x + (sizeX / 2), this.y + (sizeY / 2) - 30, offsetWidth, 60));
                    }
                }
                break;
            case ATTACK2:
                if (this.animate.getAnimationIndex() == this.animate.getAnimationMaxIndex() - 2) {
                    int offsetWidth = 87;
                    if (this.animate.getFlipAsset() == true) {
                        rect = Optional.of(new Rectangle(this.x + (sizeX / 2) - offsetWidth, this.y + (sizeY / 2) - 55,
                                offsetWidth, 85));
                    } else {
                        rect = Optional
                                .of(new Rectangle(this.x + (sizeX / 2), this.y + (sizeY / 2) - 55, offsetWidth, 85));
                    }
                }
                break;
            default:
                break;
        }

        return rect;
    }

    public Rectangle getAABB() {
        BufferedImage image = this.animate.get();
        int width = image.getWidth() - 1;
        int height = image.getHeight() - 1;
        int sizeX = width - image.getMinX();
        int sizeY = height - image.getMinY();

        Rectangle rect = new Rectangle(this.x + (sizeX / 2) - 15, this.y + (sizeY / 2) - 25, 25, 55);

        int offsetWidth = 0;
        int offsetHeight = 0;
        switch (this.animate.getCurrentAsset().state) {
            case ATTACK1:
                offsetWidth += 10;
                break;
            case ATTACK2:
                offsetWidth += 20;
                break;
            default:
                break;
        }

        if (this.animate.getFlipAsset() == true) {
            rect.x -= offsetWidth;
            rect.width += offsetWidth;
        } else {
            rect.width += offsetWidth;
        }

        return rect;
    }

    public void setPositionRelativeToAABB(int width, int height) {
        Rectangle aabb = this.getAABB();
        this.y -= aabb.getY() + aabb.getHeight() - height;
        this.x -= aabb.getX() + aabb.getWidth() - width;
    }
}
