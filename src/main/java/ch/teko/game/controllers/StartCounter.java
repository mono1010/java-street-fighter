package ch.teko.game.controllers;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class StartCounter {
    private boolean isRunning = false;
    private int count = 0;
    private long lastTime, timer;

    public StartCounter() {

    }

    /**
     * Instance of StartCounter for singleton pattern.
     */
    private static StartCounter instance;

    public void start() {
        this.isRunning = true;
        this.count = 3;
        this.lastTime = System.currentTimeMillis();
    }

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

    public boolean isRunning() {
        return this.isRunning;
    }

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
