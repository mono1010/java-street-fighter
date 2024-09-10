package ch.teko.game.model;

import java.awt.Color;
import java.awt.Graphics;

/**
 * The {@code Health} class represents the health rendering.
 */
public class Health {

    // Singleton instance of the Health class
    private static Health instance;

    // The current health percentage for player 1, where 1 represents full health
    private float player1HealthPercent = 1;

    // The current health percentage for player 2, where 1 represents full health
    private float player2HealthPercent = 1;

    /**
     * Constructs a new {@code Health} object.
     * The constructor is private to enforce the singleton pattern.
     */
    public Health() {

    }

    /**
     * Sets the health percentages for both players based on their current health
     * and maximum health.
     *
     * @param p1Health    the current health of player 1
     * @param p1MaxHealth the maximum health of player 1
     * @param p2Health    the current health of player 2
     * @param p2MaxHealth the maximum health of player 2
     */
    public void setHealth(float p1Health, float p1MaxHealth, float p2Health, float p2MaxHealth) {
        this.player1HealthPercent = (p1Health / p1MaxHealth);
        this.player2HealthPercent = (p2Health / p2MaxHealth);
    }

    /**
     * Renders the health bars for both players on the screen.
     * The health bars are colored yellow to indicate remaining health and red to
     * indicate lost health.
     * Each player's health bar is rendered on either side of the screen.
     *
     * @param g the {@code Graphics} object used to render the health bars.
     */
    public void onRender(Graphics g) {
        final int middle = Map.getInstance().getWidth() / 2;
        final int offset = 150;
        final int separationOffset = 1;

        final int y = 20;
        final int height = 18;

        // Draw rectangle around the entire health bar
        g.setColor(Color.BLACK);
        g.drawRect((middle - separationOffset) - offset - 1, y - 1, ((offset + separationOffset) * 2) + 2, height + 2);

        int player1Offset = Math.max(0, offset - (int) (offset * this.player1HealthPercent));

        // Draw remaining health for player 1
        g.setColor(Color.YELLOW);
        g.fillRect((middle - separationOffset) - offset + player1Offset, y, offset - player1Offset, height);

        // Draw lost health for player 1
        g.setColor(Color.RED);
        g.fillRect(middle - separationOffset - offset, y, player1Offset, height);

        int player2Offset = Math.max(0, offset - (int) (offset * this.player2HealthPercent));

        // Draw remaining health for player 2
        g.setColor(Color.YELLOW);
        g.fillRect(middle + separationOffset, y, offset - player2Offset, height);

        // Draw lost health for player 2
        g.setColor(Color.RED);
        g.fillRect(middle + separationOffset + offset - player2Offset, y, player2Offset, height);

        // Draw the border between the two health bars
        g.setColor(Color.BLACK);
        g.fillRect(middle - separationOffset, y - 1, separationOffset * 2, height + 2);
    }

    /**
     * Retrieves the singleton instance of the {@code Health} class.
     * If the instance does not yet exist, it is created.
     *
     * @return the singleton instance of the {@code Health} class.
     */
    public static Health getInstance() {
        if (instance == null) {
            instance = new Health();
        }
        return instance;
    }
}
