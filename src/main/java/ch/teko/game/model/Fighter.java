package ch.teko.game.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

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

class AnimationLock {
    Animate animate;
    int velocityX, velocityY;
    boolean locked;

    AnimationUnlockAction unlockAction = AnimationUnlockAction.NONE;

    public AnimationLock(Animate animate) {
        this.animate = animate;
        this.locked = false;
        this.velocityX = 0;
        this.velocityY = 0;
    }

    void update() {
        if (!locked)
            return;

        if (this.animate.getAnimationIndex() + 1 == this.animate.getAnimationMaxIndex()) {
            this.velocityX = 0;
            this.velocityY = 0;
            if (unlockAction == AnimationUnlockAction.NONE)
                this.locked = false;
            else if (unlockAction == AnimationUnlockAction.FALL) {
                this.velocityY -= 5;
                unlockAction = AnimationUnlockAction.NONE;
            }
        } 
    }
}

public class Fighter extends Entity {
    private float walkSpeed = 5;
    private boolean isPlayer1;

    private AssetsManager assetsManager;
    private Animate animate;

    boolean isJumping = false;
    AnimationLock animationLock;

    private Logger log = LogManager.getLogger(Main.class);

    public Fighter(int x, int y, boolean isPlayer1, String assetsDir) {
        super(x, y, 0, 0);

        this.isPlayer1 = isPlayer1;
        this.assetsManager = new AssetsManager(assetsDir);
        this.animate = new Animate();
        this.animate.animate(this.assetsManager.getAsset(Asset.State.IDLE));
        this.animationLock = new AnimationLock(this.animate);

        setPositionRelativeToAABB(x, y);
    }

    void jump() {
        if (!this.animationLock.locked) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.JUMP));
            this.animationLock.unlockAction = AnimationUnlockAction.FALL;
            this.animationLock.velocityY -= 5;
            this.animationLock.locked = true;       
        }
    }

    PlayerControlls getInputController() {
        if (isPlayer1)
            return InputController.getInstance().player1;
        else 
            return InputController.getInstance().player2;
    }

    @Override
    public void onTick() {
        this.animationLock.update();

        this.animate.onTick();

        PlayerControlls input = getInputController();

        if (input.right)
            if (this.animate.getFlipAsset() != false)
                this.animate.flipAsset();
        
        if (input.left)
            if (this.animate.getFlipAsset() != true)
                this.animate.flipAsset();

        if (input.right) {
            this.velocityX += this.walkSpeed;
        } 
        
        if (input.left) {
            this.velocityX -= this.walkSpeed;
        }

        if (input.up) {
            this.jump();
        }

        if (input.down) {
            this.velocityY += 10;
        }

        this.velocityX += this.animationLock.velocityX;
        this.velocityY += this.animationLock.velocityY;

        if (this.velocityX == 0 && this.velocityY == 0 && !this.animationLock.locked) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.IDLE));   
        } else if (!this.animationLock.locked) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.RUN));
        }

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
            this.x -=  aabb.getX() + aabb.getWidth() - Map.getInstance().getWidth();
        }
    }

    @Override
    public void onRender(Graphics g) {
        BufferedImage image = this.animate.get();
        g.drawImage(image, this.x, this.y, null);

        Rectangle boundingBox = this.getAABB();
        g.setColor(Color.RED);
        g.drawRect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    public Rectangle getAABB() {
        BufferedImage image = this.animate.get();
        int width = image.getWidth() - 1;
        int height = image.getHeight() - 1;
        int sizeX = width - image.getMinX();
        int sizeY = height - image.getMinY();
        return new Rectangle(this.x + (sizeX / 2) - 10, this.y + (sizeY / 2) - 25, 25, 55);
    }

    public void setPositionRelativeToAABB(int width, int height) {
        Rectangle aabb = this.getAABB();
        this.y -= aabb.getY() + aabb.getHeight() - height;
        this.x -= aabb.getX() + aabb.getWidth() - width;
    }
}
