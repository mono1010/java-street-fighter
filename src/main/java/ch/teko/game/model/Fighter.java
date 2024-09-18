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

/**
 * The {@code Fighter} class represents a player-controlled fighter in a game.
 * It extends the {@code Entity} class
 */
public class Fighter extends Entity {

    // Movement speed of the fighter
    private float walkSpeed = 5;

    // Health properties of the fighter
    private float health = 100;
    private float maxHealth = 100;

    // Flag to indicate whether this fighter is player 1
    private boolean isPlayer1;

    // Manages assets and animations for the fighter
    private AssetsManager assetsManager;
    private Animate animate;

    // Flag to track if the fighter is jumping
    boolean isJumping = false;

    // Manages animation state transitions
    AnimationState animationState;

    // Handles input controls for the fighter
    PlayerControlls input;

    // Logger for debugging purposes
    private Logger log = LogManager.getLogger(Main.class);

    /**
     * Constructs a new {@code Fighter} instance, initializing it with the given
     * position,
     * player identifier, and asset directory.
     *
     * @param x         the initial X-coordinate of the fighter.
     * @param y         the initial Y-coordinate of the fighter.
     * @param isPlayer1 flag to indicate if this fighter is player 1.
     * @param assetsDir the directory containing the assets for this fighter.
     */
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

    /**
     * Constructs a new {@code Fighter} instance, initializing it with the given
     * position,
     * player identifier, and an existing {@code AssetsManager}.
     *
     * @param x             the initial X-coordinate of the fighter.
     * @param y             the initial Y-coordinate of the fighter.
     * @param isPlayer1     flag to indicate if this fighter is player 1.
     * @param assetsManager the {@code AssetsManager} managing the fighter's assets.
     */
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

    /**
     * Retrieves the assets manager associated with the fighter.
     *
     * @return the {@code AssetsManager} for the fighter.
     */
    public AssetsManager getAssetsManager() {
        return this.assetsManager;
    }

    /**
     * Toggles the flipping of the fighter's assets.
     * This is useful for rendering the fighter facing in the opposite direction.
     */
    public void flipAsset() {
        this.animate.flipAsset();
    }

    /**
     * Retrieves the input controller for the fighter.
     * Player 1 uses {@code player1} controls, and Player 2 uses {@code player2}
     * controls.
     *
     * @return the {@code PlayerControlls} object associated with the fighter.
     */
    PlayerControlls getInputController() {
        if (isPlayer1)
            return InputController.getInstance().player1;
        else
            return InputController.getInstance().player2;
    }

    /**
     * Sets the animation state based on the fighter's input, such as moving or
     * attacking.
     * It also handles flipping the asset if necessary.
     */
    void setAnimation() {
        if (this.input.right) {
            if (!this.animate.getFlipAsset())
                this.animate.flipAsset();
        } else if (this.input.left) {
            if (this.animate.getFlipAsset())
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

    /**
     * Updates the fighter's velocity based on the current input and animation
     * state.
     */
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

    /**
     * Retrieves the maximum health of the fighter.
     *
     * @return the maximum health value.
     */
    public float getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * Retrieves the current health of the fighter.
     *
     * @return the current health value.
     */
    public float getHealth() {
        return this.health;
    }

    /**
     * Checks if the fighter is dead.
     *
     * @return {@code true} if the fighter's health is 0, otherwise {@code false}.
     */
    public boolean isDead() {
        return this.health == 0.f;
    }

    /**
     * Reduces the fighter's health by the specified amount of damage.
     *
     * @param damage the amount of damage to inflict on the fighter.
     */
    public void damage(float damage) {
        this.health = Math.max(0, this.health - damage);
    }

    /**
     * Updates the fighter's state and position on each game tick, adjusting
     * animations,
     * velocity, and ensuring the fighter stays within the map boundaries.
     */
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

    /**
     * Renders the fighter on the screen, including its current animation and
     * hitbox.
     *
     * @param g the {@code Graphics} object used for rendering.
     */
    @Override
    public void onRender(Graphics g) {
        BufferedImage image = this.animate.get();
        g.drawImage(image, this.x, this.y, null);

        if (!Main.PRODUCTION) {
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
    }

    /**
     * Returns the fighter's hitbox if it is in an attack state.
     *
     * @return an {@code Optional} containing the hitbox {@code Rectangle} if the
     *         fighter is attacking, otherwise empty.
     */
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
                    if (this.animate.getFlipAsset()) {
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
                    if (this.animate.getFlipAsset()) {
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

    /**
     * Retrieves the axis-aligned bounding box (AABB) of the fighter.
     *
     * @return the AABB {@code Rectangle} representing the fighter's bounding box.
     */
    public Rectangle getAABB() {
        BufferedImage image = this.animate.get();
        int width = image.getWidth() - 1;
        int height = image.getHeight() - 1;
        int sizeX = width - image.getMinX();
        int sizeY = height - image.getMinY();

        Rectangle rect = new Rectangle(this.x + (sizeX / 2) - 15, this.y + (sizeY / 2) - 25, 25, 55);

        int offsetWidth = 0;
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

        if (this.animate.getFlipAsset()) {
            rect.x -= offsetWidth;
            rect.width += offsetWidth;
        } else {
            rect.width += offsetWidth;
        }

        return rect;
    }

    /**
     * Adjusts the fighter's position relative to its axis-aligned bounding box
     * (AABB).
     *
     * @param width  the new X-coordinate of the fighter.
     * @param height the new Y-coordinate of the fighter.
     */
    public void setPositionRelativeToAABB(int width, int height) {
        Rectangle aabb = this.getAABB();
        this.y -= aabb.getY() + aabb.getHeight() - height;
        this.x -= aabb.getX() + aabb.getWidth() - width;
    }
}
