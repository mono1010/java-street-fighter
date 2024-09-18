package ch.teko.game.model;

import java.awt.image.BufferedImage;

/**
 * Represents an asset in the game.
 */
public class Asset {

    /**
     * Enumeration of existing animations.
     */
    public enum State {

        /**
         * The player running.
         */
        RUN,

        /**
         * The player idle (standing still).
         */
        IDLE,

        /**
         * The player jumping.
         */
        JUMP,

        /**
         * The player falling.
         */
        FALL,

        /**
         * The player's first attack animation.
         */
        ATTACK1,

        /**
         * The player's second attack animation.
         */
        ATTACK2
    }

    /**
     * Metadata associated with the asset.
     */
    public AssetsMetadata metadata;

    /**
     * The asset in a cached BufferedImage.
     */
    public BufferedImage image;

    /**
     * The state the asset represents.
     */
    public State state;

    /**
     * Constructs an Asset object.
     *
     * @param metadata The metadata for the asset.
     * @param image    The image representing the asset.
     * @param state    The state the asset represents.
     */
    public Asset(AssetsMetadata metadata, BufferedImage image, State state) {
        this.metadata = metadata;
        this.image = image;
        this.state = state;
    }
}