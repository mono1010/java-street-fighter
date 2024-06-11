package ch.teko.game.controllers;

import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.teko.game.Main;

public class InputController implements KeyListener {
    public boolean up, down, right, left, escape = false;
    boolean keys[];

    private Logger log = LogManager.getLogger(Main.class);

    private static InputController instance;
    
    public InputController() {
        keys = new boolean[KeyEvent.VK_Z];
    }

    public void onTick() {
        up = keys[KeyEvent.VK_W];
        left = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_D];
        down = keys[KeyEvent.VK_S];
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        final int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_ESCAPE)
            this.escape = true;

        if (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z) {
            this.keys[e.getKeyCode()] = true;
            this.log.trace("Key pressed {}", KeyEvent.getKeyText(e.getKeyCode()));
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        final int keyCode = e.getKeyCode();

        if (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z) {
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
