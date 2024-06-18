package ch.teko.game.controllers;

import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.teko.game.Main;
import ch.teko.game.controllers.*;

public class InputController implements KeyListener {
    public boolean escape = false;
    private PlayerControlls[] players;
    public PlayerControlls player1;
    public PlayerControlls player2;

    boolean keys[];

    private Logger log = LogManager.getLogger(Main.class);

    private static InputController instance;
    
    public InputController() {
        keys = new boolean[KeyEvent.VK_Z];
        players = new PlayerControlls[2];
        players[0] = new PlayerControlls();
        players[1] = new PlayerControlls();
        player1 = players[0];
        player2 = players[1];
    }

    public void onTick() {
        player1.up = keys[KeyEvent.VK_W];
        player1.left = keys[KeyEvent.VK_A];
        player1.right = keys[KeyEvent.VK_D];
        player1.down = keys[KeyEvent.VK_S];

        player2.up = keys[KeyEvent.VK_UP];
        player2.left = keys[KeyEvent.VK_LEFT];
        player2.right = keys[KeyEvent.VK_RIGHT];
        player2.down = keys[KeyEvent.VK_DOWN];
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        final int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_ESCAPE)
            this.escape = true;

        if (keyCode >= KeyEvent.VK_LEFT && keyCode <= KeyEvent.VK_Z) {
            this.keys[e.getKeyCode()] = true;
            this.log.trace("Key pressed {}", KeyEvent.getKeyText(e.getKeyCode()));
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        final int keyCode = e.getKeyCode();

        if (keyCode >= KeyEvent.VK_LEFT && keyCode <= KeyEvent.VK_Z) {
            this.keys[e.getKeyCode()] = false;
            this.log.trace("Key released {}", KeyEvent.getKeyText(e.getKeyCode()));
        }
    }

    public static InputController getInstance() {
        if (instance == null) {
            instance = new InputController();
        }
        return instance;
    }
}
