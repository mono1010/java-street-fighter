package ch.teko.game.controllers;

import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.teko.game.Main;
import ch.teko.game.controllers.*;

/**
 * Represents an input controller for handling keyboard events.
 */
public class InputController implements KeyListener {
    /**
     * Represents if the escape key has been pressed.
     * Will not be unset in the InputController class
     */
    public boolean escape = false;

    /**
     * Array containing the controls of all players
     */
    private PlayerControlls[] players;

    /**
     * Player1 control structure
     */
    public PlayerControlls player1;

    /**
     * Player2 control structure
     */
    public PlayerControlls player2;

    /**
     * Array containing key pressed states
     */
    private boolean keys[];

    /**
     * Log4j instance
     */
    private Logger log = LogManager.getLogger(Main.class);

    /**
     * Instance of InputController for singleton pattern.
     */
    private static InputController instance;

    /**
     * Constructs a new InputController.
     */
    public InputController() {
        keys = new boolean[KeyEvent.VK_BEGIN];
        players = new PlayerControlls[2];
        players[0] = new PlayerControlls();
        players[1] = new PlayerControlls();
        player1 = players[0];
        player2 = players[1];
    }

    /**
     * Updates the input state on each game tick.
     */
    public void onTick() {
        FighterSettings settingsPlayer1 = Settings.getInstance().getFighter1();
        FighterSettings settingsPlayer2 = Settings.getInstance().getFighter2();

        player1.up = keys[settingsPlayer1.getUpKey()];
        player1.left = keys[settingsPlayer1.getLeftKey()];
        player1.right = keys[settingsPlayer1.getRightKey()];
        player1.down = keys[settingsPlayer1.getDownKey()];
        player1.attack1 = keys[settingsPlayer1.getAttack1Key()];
        player1.attack2 = keys[settingsPlayer1.getAttack2Key()];

        player2.up = keys[settingsPlayer2.getUpKey()];
        player2.left = keys[settingsPlayer2.getLeftKey()];
        player2.right = keys[settingsPlayer2.getRightKey()];
        player2.down = keys[settingsPlayer2.getDownKey()];
        player2.attack1 = keys[settingsPlayer2.getAttack1Key()];
        player2.attack2 = keys[settingsPlayer2.getAttack2Key()];
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Implementation for keyTyped event (if needed)
    }

    @Override
    public void keyPressed(KeyEvent e) {
        final int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_ESCAPE)
            this.escape = true;

        this.keys[e.getKeyCode()] = true;
        this.log.trace("Key pressed {}", KeyEvent.getKeyText(e.getKeyCode()));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        final int keyCode = e.getKeyCode();

        this.keys[e.getKeyCode()] = false;
        this.log.trace("Key released {}", KeyEvent.getKeyText(e.getKeyCode()));
    }

    /**
     * Gets the singleton instance of InputController.
     *
     * @return The InputController instance.
     */
    public static InputController getInstance() {
        if (instance == null) {
            instance = new InputController();
        }
        return instance;
    }
}
