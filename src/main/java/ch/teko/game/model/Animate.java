package ch.teko.game.model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import ch.teko.game.model.*;

/**
 * The {@code Animate} class handles the animation of game assets. It keeps track
 * of the current animation frame, and it can flip and rotate assets for rendering.
 */
public class Animate {

    // Index of the current animation frame
    private int animationIndex;

    // Height and width of the sprite being animated
    private int spriteHeight;
    private int spriteWidth;

    // Number of frames (columns) in the sprite sheet
    private int spriteColumn;

    // The asset currently being animated
    private Asset currentAsset;

    // Boolean flag to determine if the asset should be flipped
    private boolean flipAsset;

    // Timer variables to manage animation speed
    private long lastTime, timer;

    // The rate at which the animation frames should change (in milliseconds)
    final int rate = 60;

    /**
     * Constructs a new {@code Animate} object with default values.
     */
    public Animate() {
    }

    /**
     * Updates the animation by progressing to the next frame if the timer has passed
     * the frame rate threshold. If the animation reaches the last frame, it resets.
     *
     * @return {@code true} if the animation has reached the end of the sequence, {@code false} otherwise.
     */
    public boolean onTick() {
        boolean reachedEnd = false;
        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        if (timer > rate) {
            this.animationIndex++;
            if (this.animationIndex == this.spriteColumn) {
                reachedEnd = true;
                this.animationIndex = 0;
            }
            timer = 0;
        }
        return reachedEnd;
    }

    /**
     * Gets the current animation frame index.
     *
     * @return the current animation frame index.
     */
    public int getAnimationIndex() {
        return this.animationIndex;
    }

    /**
     * Gets the maximum index (number of frames) in the animation.
     *
     * @return the maximum index of the animation (number of frames).
     */
    public int getAnimationMaxIndex() {
        return this.spriteColumn;
    }

    /**
     * Gets the current asset being animated.
     *
     * @return the current {@code Asset} being animated.
     */
    public Asset getCurrentAsset() {
        return this.currentAsset;
    }

    /**
     * Checks if the animation for the given asset is already running.
     *
     * @param asset the asset to check.
     * @return {@code true} if the animation for the given asset is running, {@code false} otherwise.
     */
    public boolean isAnimationRunning(Asset asset) {
        if (this.currentAsset == null)
            return false;

        return this.currentAsset.metadata.name.equals(asset.metadata.name);
    }

    /**
     * Starts the animation for the given asset. If the animation is already running,
     * it will not reset.
     *
     * @param asset the asset to animate.
     */
    public void animate(Asset asset) {
        if (isAnimationRunning(asset))
            return;

        this.currentAsset = asset;
        this.animationIndex = 0;
        this.spriteColumn = asset.metadata.column;
        this.spriteHeight = this.currentAsset.image.getHeight() / 1;
        this.spriteWidth = this.currentAsset.image.getWidth() / this.spriteColumn;
        this.timer = 0;
    }

    /**
     * Retrieves the current frame of the animation as a subimage from the sprite sheet,
     * and applies flipping if necessary.
     *
     * @return the current frame of the animation as a {@code BufferedImage}.
     */
    public BufferedImage get() {
        return rotate(this.currentAsset.image.getSubimage(this.animationIndex * this.spriteWidth, 0, this.spriteWidth, this.spriteHeight));
    }

    /**
     * Creates a copy of the given {@code BufferedImage}.
     *
     * @param source the source image to copy.
     * @return a new {@code BufferedImage} that is a copy of the source image.
     */
    public static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    /**
     * Rotates or flips the given {@code BufferedImage} if the {@code flipAsset} flag is set.
     *
     * @param image the image to rotate or flip.
     * @return the rotated or flipped image.
     */
    public BufferedImage rotate(BufferedImage image) {
        if (!this.flipAsset)
            return image;

        BufferedImage copy = copyImage(image);
        flipAsset(copy);
        return copy;
    }

    /**
     * Toggles the flipping state of the current asset.
     */
    void flipAsset() {
        this.flipAsset = !this.flipAsset;
    }

    /**
     * Gets the current flipping state of the asset.
     *
     * @return {@code true} if the asset is flipped, {@code false} otherwise.
     */
    boolean getFlipAsset() {
        return this.flipAsset;
    }

    /**
     * Flips the given {@code BufferedImage} horizontally.
     *
     * @param image the image to flip horizontally.
     */
    public static void flipAsset(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int i = 0; i < width / 2; i++) {
            for (int j = 0; j < height; j++) {
                int tmp = image.getRGB(i, j);
                image.setRGB(i, j, image.getRGB(width - i - 1, j));
                image.setRGB(width - i - 1, j, tmp);
            }
        }
    }
}
