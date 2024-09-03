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
import ch.teko.game.view.*;
import ch.teko.game.controllers.*;
import ch.teko.game.Main;

enum AnimationUnlockAction {
    NONE,
    FALL,
}

class Timer {
    long timer, lastTime;

    public Timer() {
        reset();
    }

    boolean reached(long delay) {
        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        if (timer <= delay) {
            return false;
        }

        return true;
    }

    void reset() {
        timer = 0;
        lastTime = System.currentTimeMillis();
    }
}

class AnimationState {
    public float velocityX, velocityY;
    public boolean canRun = true;

    private int currentAnimationIterations = 0;
    private int animationIterations = 0;
    private int tickesUsedForAnimation = 0;
    private int maxTicksForAnimation = 0;
    private AssetsManager assetsManager;
    private Animate animate;
    private Optional<Asset.State> currentAnimation;
    private Optional<Asset.State> previousAnimation;
    private List<Asset.State> wantedAnimations = new ArrayList<>();

    private Logger log = LogManager.getLogger(Main.class);

    public AnimationState(Animate animate, AssetsManager assetsManager) {
        this.animate = animate;
        this.assetsManager = assetsManager;
        this.currentAnimation = Optional.empty();
        this.previousAnimation = Optional.empty();
    }

    void reset() {
        this.currentAnimationIterations = 0;
        this.animationIterations = 0;
        this.tickesUsedForAnimation = 0;
        this.maxTicksForAnimation = 0;
        this.canRun = true;
    }

    boolean reached(int iterations) {
        if (iterations >= this.animationIterations)
            return true;

        return false;
    }

    void onTick() {
        this.wantedAnimations.clear();
        boolean reachedEnd = this.animate.onTick();

        this.tickesUsedForAnimation += 1;
        if (this.maxTicksForAnimation == 0) {
            if (reachedEnd && this.animationIterations != 0)
                this.currentAnimationIterations += 1;

            if (!reached(this.currentAnimationIterations) && this.animationIterations != 0)
                return;
        } else {
            if (this.maxTicksForAnimation > this.tickesUsedForAnimation)
                return;
        }

        previousAnimation = currentAnimation;
        if (!this.triggerChainedAnimation()) {
            this.currentAnimation = Optional.empty();
            this.previousAnimation = Optional.empty();
            this.reset();
        }
    }

    boolean triggerChainedAnimation() {
        Asset.State currentAnimationState = this.currentAnimation.orElse(Asset.State.IDLE);
        if (currentAnimationState == Asset.State.JUMP) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.FALL));
            currentAnimation = Optional.of(Asset.State.FALL);
            int ticksUsed = this.tickesUsedForAnimation;
            this.reset();
            this.maxTicksForAnimation = ticksUsed;
            log.info("triggered chained fall animation");
            return true;
        }

        return false;
    }

    void triggerAnimation(Asset.State animation) {
        this.wantedAnimations.add(animation);

        Asset.State currentAnimationState = this.currentAnimation.orElse(Asset.State.IDLE);
        if (currentAnimationState == Asset.State.JUMP || currentAnimationState == Asset.State.FALL)
            return;

        if (animation == Asset.State.JUMP) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.JUMP));
            currentAnimation = Optional.of(animation);
            this.reset();
            this.animationIterations = 1;
            log.info("triggered JUMP animation");
            return;
        }

        if (wantedAnimations.contains(Asset.State.JUMP))
            return;

        if (currentAnimationState == Asset.State.ATTACK1 || currentAnimationState == Asset.State.ATTACK2)
            return;

        if (animation == Asset.State.ATTACK1) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.ATTACK1));
            currentAnimation = Optional.of(animation);
            this.reset();
            this.animationIterations = 1;
            this.canRun = false;
            return;
        }

        if (wantedAnimations.contains(Asset.State.ATTACK1))
            return;

        if (currentAnimationState == Asset.State.ATTACK2)
            return;

        if (animation == Asset.State.ATTACK2) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.ATTACK2));
            currentAnimation = Optional.of(animation);
            this.reset();
            this.animationIterations = 1;
            this.canRun = false;
            return;
        }

        if (wantedAnimations.contains(Asset.State.ATTACK2))
            return;

        if (animation == Asset.State.RUN) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.RUN));
            currentAnimation = Optional.of(animation);
            this.reset();
            return;
        }

        if (wantedAnimations.contains(Asset.State.RUN))
            return;

        if (animation == Asset.State.IDLE) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.IDLE));
            currentAnimation = Optional.of(animation);
            this.reset();
            return;
        }
    }
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
