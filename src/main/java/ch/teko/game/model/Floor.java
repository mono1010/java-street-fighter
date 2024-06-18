package ch.teko.game.model;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents the game Floor bounds and graphical rendering.
 */
public class Floor {
    /**
     * The position of the floor.
     */
    private int height;

    /**
     * Instance of Floor for singleton pattern.
     */
    private static Floor instance;

    /**
     * Constructor for Floor.
     */
    public Floor() {
        
    }

    /**
     * Sets the position of the floor.
     * @param height The position to set for the floor.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the position of the floor.
     * @return The current position of the floor.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Renders the floor on a given graphics context.
     * @param g The graphics context to render on.
     */
    public void onRender(Graphics g) {
        g.setColor(Color.RED);
        g.drawLine(0, this.height, 100000, this.height);
    }

    /**
     * Provides access to the singleton instance of Floor.
     * @return The single instance of Floor.
     */
    public static Floor getInstance() {
        if (instance == null) {
            instance = new Floor();
        }
        return instance;
    }
}
