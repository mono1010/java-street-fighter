package ch.teko.game.controllers;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.teko.game.Main;

/**
 * Settings class for the fighter holding all fighter key binds
 */
public class FighterSettings {
    private int leftKey = KeyEvent.VK_LEFT;
    private int rightKey = KeyEvent.VK_RIGHT;
    private int upKey = KeyEvent.VK_UP;
    private int downKey = KeyEvent.VK_DOWN;
    private int attack1Key = KeyEvent.VK_Z;
    private int attack2Key = KeyEvent.VK_X;

    Fighter fighter;

    /**
     * Log4j instance
     */
    private Logger log = LogManager.getLogger(Main.class);

    /**
     * 
     * @param fighter Fighter 1 or 2
     */
    public FighterSettings(Fighter fighter) {
        this.fighter = fighter;
    }

    /**
     * Loads all key binds
     * 
     * @param properties Properties object holding the settings
     */
    public void load(Properties properties) {
        String player = fighter == Fighter.FIGHTER1 ? "player1" : "player2";
       this.leftKey = Integer.parseInt(properties.getProperty(player + ".left", String.valueOf(fighter == Fighter.FIGHTER1 ? KeyEvent.VK_A : KeyEvent.VK_LEFT)));
       this.rightKey = Integer.parseInt(properties.getProperty(player + ".right", String.valueOf(fighter == Fighter.FIGHTER1 ? KeyEvent.VK_D : KeyEvent.VK_RIGHT)));
       this.upKey = Integer.parseInt(properties.getProperty(player + ".up", String.valueOf(fighter == Fighter.FIGHTER1 ? KeyEvent.VK_W : KeyEvent.VK_UP)));
       this.downKey = Integer.parseInt(properties.getProperty(player + ".down", String.valueOf(fighter == Fighter.FIGHTER1 ? KeyEvent.VK_S : KeyEvent.VK_DOWN)));
       this.attack1Key = Integer.parseInt(properties.getProperty(player + ".attack1", String.valueOf(fighter == Fighter.FIGHTER1 ? KeyEvent.VK_C : KeyEvent.VK_K)));
       this.attack2Key = Integer.parseInt(properties.getProperty(player + ".attack2", String.valueOf(fighter == Fighter.FIGHTER1 ? KeyEvent.VK_V : KeyEvent.VK_L)));
    }

    /**
     * Saves all key binds
     * 
     * @param properties Properties object that will hold the saved settings
     */
    void save(Properties properties) {
        String player = fighter == Fighter.FIGHTER1 ? "player1" : "player2";
        properties.setProperty(player + ".left", String.valueOf(this.leftKey));
        properties.setProperty(player + ".right", String.valueOf(this.rightKey));
        properties.setProperty(player + ".up", String.valueOf(this.upKey));
        properties.setProperty(player + ".down", String.valueOf(this.downKey));
        properties.setProperty(player + ".attack1", String.valueOf(this.attack1Key));
        properties.setProperty(player + ".attack2", String.valueOf(this.attack2Key));
    }

    /**
     * Sets the key binding for a key.
     *
     * @param keyCode the key code for the new key bind
     * @param key the key that will have the new value stored
     */
    public void setKey(int keyCode, Keys key) {
        switch (key) {
            case RIGHT:
                this.rightKey = keyCode; 
                break;
            case LEFT:
                this.leftKey = keyCode; 
                break;
            case UP:
                this.upKey = keyCode; 
                break;
            case DOWN:
                this.downKey = keyCode; 
                break;
            case ATTACK1:
                this.attack1Key = keyCode; 
                break;
            case ATTACK2:
                this.attack2Key = keyCode; 
                break;
            default:
                log.error("key not found");
                break;
        }
    }

    /**
     * Sets the key binding for a key.
     *
     * @param key the key to get the value from
     * 
     * @return the key code
     */
    public int getKey(Keys key) {
        switch (key) {
            case RIGHT:
                return this.rightKey; 
            case LEFT:
                return this.leftKey; 
            case UP:
                return this.upKey; 
            case DOWN:
               return this.downKey; 
            case ATTACK1:
                return this.attack1Key; 
            case ATTACK2:
                return this.attack2Key; 
            default:
                log.error("key not found");
                return 0; 
        }
    }
}