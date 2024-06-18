package ch.teko.game.model;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * The {@code Entity} class represents a generic entity with position and velocity.
 * This class provides the basic framework for entity objects.
 */
public abstract class Entity {
    /**
     * The x-coordinate of the entity.
     */
    protected int x;

    /**
     * The y-coordinate of the entity.
     */
    protected int y;

    /**
     * The velocity of the entity along the x-axis.
     */
    protected int velocityX;

    /**
     * The velocity of the entity along the y-axis.
     */
    protected int velocityY;

    /**
     * Constructs a new {@code Entity} with specified position and velocity.
     *
     * @param x         the initial x-coordinate of the entity
     * @param y         the initial y-coordinate of the entity
     * @param velocityX the initial velocity of the entity along the x-axis
     * @param velocityY the initial velocity of the entity along the y-axis
     */
    public Entity(int x, int y, int velocityX, int velocityY) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /**
     * Updates the state of the entity every game tick.
     */
    public abstract void onTick();

    /**
     * Renders the entity on the given graphics context.
     *
     * @param g the graphics context to render on
     */
    public abstract void onRender(Graphics g);

    /**
     * Returns an axis-aligned bounding box representing the physical bounds of the entity.
     *
     * @return a {@code Rectangle} object that encloses the entity's bounds
     */
    public abstract Rectangle getAABB();
}
