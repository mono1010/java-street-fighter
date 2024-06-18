package ch.teko.game.model;

/**
 * Represents the game map bounds.
 */
public class Map {
    /**
     * The width of the map.
     */
    int width;
    
    /**
     * The height of the map.
     */
    int height;

    /**
     * Instance of Map for singleton pattern.
     */
    private static Map instance;

    /**
     * Constructor for Map.
     */
    public Map() {

    }

    /**
     * Sets the width of the map.
     * @param width The width to set for the map.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets the height of the map.
     * @param height The height to set for the map.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the X coordinate.
     * @return The X coordinate.
     */
    public int getX() {
        return 0; // Placeholder implementation
    }

    /**
     * Gets the Y coordinate.
     * @return The Y coordinate.
     */
    public int getY() {
        return 0; // Placeholder implementation
    }

    /**
     * Gets the width of the map.
     * @return The current width of the map.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Gets the height of the map.
     * @return The current height of the map.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Provides access to the singleton instance of Map.
     * @return The single instance of Map.
     */
    public static Map getInstance() {
        if (instance == null) {
            instance = new Map();
        }
        return instance;
    }
}
