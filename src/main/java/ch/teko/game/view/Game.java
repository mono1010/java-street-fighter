package ch.teko.game.view;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Optional;

import javax.swing.JPanel;

import ch.teko.game.model.*;
import ch.teko.game.controllers.*;

/**
 * The {@code Game} class extends {@code JPanel} and represents the actual game,
 */
public class Game extends JPanel {

    /** 
     * Two fighter objects representing the players in the game
     */
    Fighter f1, f2;

    /**
     *  Offset value to set the starting position of the fighters
     */
    final int startOffset = 80;

    /**
     * Constructs a new {@code Game} instance, initializes the map dimensions,
     * floor height, and creates two fighters with starting positions and asset paths.
     *
     * @param assetsPath the path to the assets directory for loading fighter assets.
     */
    public Game(String assetsPath) {
        Map.getInstance().setWidth(600);
        Map.getInstance().setHeight(400);

        final int height = Map.getInstance().getHeight();
        final int width = Map.getInstance().getWidth();

        Floor.getInstance().setHeight(height - 50);

        f1 = new Fighter(startOffset, Floor.getInstance().getHeight(), true, assetsPath);
        f2 = new Fighter(width - startOffset, Floor.getInstance().getHeight(), false, assetsPath);
        f2.flipAsset();

        Health.getInstance().setHealth(f1.getHealth(), f1.getMaxHealth(), f2.getHealth(), f2.getMaxHealth());
    }

    /**
     * Resets the game by reinitializing both fighters to their starting positions,
     * using their existing asset managers.
     */
    public void reset() {
        final int height = Map.getInstance().getHeight();
        final int width = Map.getInstance().getWidth();
        f1 = new Fighter(startOffset, Floor.getInstance().getHeight(), true, f1.getAssetsManager());
        f2 = new Fighter(width - startOffset, Floor.getInstance().getHeight(), false, f2.getAssetsManager());
        f2.flipAsset();

        Health.getInstance().setHealth(f1.getHealth(), f1.getMaxHealth(), f2.getHealth(), f2.getMaxHealth());
    }

    /**
     * Checks if player 1 has won the game by determining if player 2 is dead.
     *
     * @return {@code true} if player 2 is dead, indicating player 1 has won.
     */
    public boolean player1Won() {
        return f2.isDead();
    }

    /**
     * Checks if player 2 has won the game by determining if player 1 is dead.
     *
     * @return {@code true} if player 1 is dead, indicating player 2 has won.
     */
    public boolean player2Won() {
        return f1.isDead();
    }

    /**
     * Handles player 1's attack action. If player 1's hitbox intersects with player 2's
     * axis-aligned bounding box (AABB), player 2 takes damage.
     */
    void player1Attack() {
        Optional<Rectangle> hitBoxOptional = f1.getHitBox();
        if (hitBoxOptional.isPresent()) {
            Rectangle hitBox = hitBoxOptional.get();
            Rectangle aabb = f2.getAABB();
            if (hitBox.intersects(aabb)) {
                f2.damage(7); // Inflicts 7 damage to player 2
            }
        }
    }

    /**
     * Handles player 2's attack action. If player 2's hitbox intersects with player 1's
     * axis-aligned bounding box (AABB), player 1 takes damage.
     */
    void player2Attack() {
        Optional<Rectangle> hitBoxOptional = f2.getHitBox();
        if (hitBoxOptional.isPresent()) {
            Rectangle hitBox = hitBoxOptional.get();
            Rectangle aabb = f1.getAABB();
            if (hitBox.intersects(aabb)) {
                f1.damage(7); // Inflicts 7 damage to player 1
            }
        }
    }

    /**
     * The main game loop, which handles updating the fighters' animation,
     * managing attacks, and updating the health of the players.
     */
    void loop() {
        f1.onTick();
        f2.onTick();

        player1Attack();
        player2Attack();

        Health.getInstance().setHealth(f1.getHealth(), f1.getMaxHealth(), f2.getHealth(), f2.getMaxHealth());
    }

    /**
     * Paints the game components on the screen, including the start counter, floor, health,
     * and both fighters.
     *
     * @param g the {@code Graphics} object used for drawing game components.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        StartCounter.getInstance().draw(g);

        Floor.getInstance().onRender(g);
        Health.getInstance().onRender(g);

        f1.onRender(g);
        f2.onRender(g);
    }
}
