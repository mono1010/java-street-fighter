package ch.teko.game.controllers;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * The {@code StartCounter} class implements a countdown timer that
 * can be displayed graphically.
 */
public class StartCounter {
    // Indicates if the counter is currently running
    private boolean isRunning = false;

    // The current count value for the countdown
    private int count = 0;

    // Variables for tracking the time between ticks
    private long lastTime, timer;

    public StartCounter() {

    }

    /**
     * Instance of StartCounter for singleton pattern.
     */
    private static StartCounter instance;

    /**
     * Starts the countdown by setting the running state to {@code true},
     * initializing the count to 3, and setting the current system time.
     */
    public void start() {
        this.isRunning = true;
        this.count = 3;
        this.lastTime = System.currentTimeMillis();
    }

    /**
     * Handles the ticking mechanism. It updates the timer based on
     * the elapsed time since the last tick, and returns {@code true}
     * if a tick occurred, meaning one second has passed.
     *
     * @return {@code true} if a tick occurred, otherwise {@code false}.
     */
    public boolean onTick() {
        final int rate = 1000;

        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        if (timer > rate) {
            timer = 0;
            return true;
        }

        return false;
    }

    /**
     * Checks if the counter is currently running.
     *
     * @return {@code true} if the counter is running, otherwise {@code false}.
     */
    public boolean isRunning() {
        return this.isRunning;
    }

    /**
     * Draws the countdown number at the center of the screen using
     * a larger font size if isRunning is {@code true}. The countdown value is updated every second.
     * If the countdown reaches zero, the running state is set to {@code false}.
     *
     * @param g the {@code Graphics} object used for drawing the countdown.
     */
    public void draw(Graphics g) {
        if (onTick()) {
            if (count == 0) {
                this.isRunning = false;
                return;
            }
            count--;
        }

        if (this.isRunning) {
            String text = String.valueOf(this.count);
            Rectangle bounds = g.getClipBounds();

            // Change the font size for the text
            Font originalFont = g.getFont(); // Store the original font
            Font newFont = originalFont.deriveFont(40f); // Create a new font with size 24
            g.setFont(newFont); // Set the new font

            // Get the width and height of the drawing area
            int componentWidth = bounds.width;
            int componentHeight = bounds.height;

            // Get the FontMetrics to calculate the width and height of the string
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            int stringWidth = metrics.stringWidth(text);
            int stringHeight = metrics.getAscent();

            // Calculate the coordinates to center the string
            int x = (componentWidth - stringWidth) / 2;
            int y = (componentHeight + stringHeight) / 2;

            // Draw the string at the calculated coordinates
            g.drawString(text, x, y);

            // Reset to the original font
            g.setFont(originalFont);
        }
    }

    /**
     * Gets the singleton instance of InputController.
     *
     * @return The InputController instance.
     */
    public static StartCounter getInstance() {
        if (instance == null) {
            instance = new StartCounter();
        }
        return instance;
    }
}
