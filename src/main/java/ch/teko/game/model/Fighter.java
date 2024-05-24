package ch.teko.game.model;

import java.awt.Graphics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.teko.game.model.*;
import ch.teko.game.view.*;
import ch.teko.game.controllers.InputController;
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

    private AssetsManager assetsManager;
    private Animate animate;
    private Game game;

    boolean isJumping = false;
    AnimationLock animationLock;

    private Logger log = LogManager.getLogger(Main.class);

    public Fighter(Game game, int x, int y, String assetsDir) {
        super(x, y, 0, 0);

        this.game = game;
        this.assetsManager = new AssetsManager(assetsDir);
        this.animate = new Animate();
        this.animate.animate(this.assetsManager.getAsset(Asset.State.IDLE));
        this.animationLock = new AnimationLock(this.animate);
    }

    void jump() {
        if (!this.animationLock.locked) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.JUMP));
            this.animationLock.unlockAction = AnimationUnlockAction.FALL;
            this.animationLock.velocityY -= 5;
            this.animationLock.locked = true;       
        }
    }

    @Override
    public void onTick() {
        this.animationLock.update();

        this.animate.onTick();

        InputController input = this.game.getInputController();
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
    }

    @Override
    public void onRender(Graphics g) {
        g.drawImage(this.animate.get(), this.x, this.y, null);
    }
}
