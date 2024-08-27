package ch.teko.game.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.teko.game.Main;

enum Fighter {
    FIGHTER1,
    FIGHTER2,
}

public class Settings {
    private final String PROPERTIES_FILE = "config.properties";

    private FighterSettings fighter1;
    private FighterSettings fighter2;

    private Logger log = LogManager.getLogger(Main.class);

    /**
     * Instance of Settings for singleton pattern.
     */
    private static Settings instance;

    public Settings() {
        this.fighter1 = new FighterSettings(Fighter.FIGHTER1);
        this.fighter2 = new FighterSettings(Fighter.FIGHTER2);
    }

    /**
     * Loads all settings
     */
    public void load() {
        log.info("settings");
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(fis);
            this.fighter1.load(properties);
            this.fighter2.load(properties);
        } catch (IOException e) {
            this.log.warn("Failed to load settings");
            e.printStackTrace();
        }

    }

    /**
     * Saves all settings
     */
    public void save() {
        log.info("Saving settings");
        try (FileOutputStream fos = new FileOutputStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            this.fighter1.save(properties);
            this.fighter2.save(properties);
            properties.store(fos, "Street-Fighter Settings");
        } catch (IOException e) {
            this.log.warn("Failed to save settings");
            e.printStackTrace();
        }
    }

    /**
     * 
     * @return Fighter 1 settings
     */
    public FighterSettings getFighter1() {
        return this.fighter1;
    }

    /**
     * 
     * @return Fighter 2 settings
     */
    public FighterSettings getFighter2() {
        return this.fighter2;
    }

    /**
     * Gets the singleton instance of Settings.
     *
     * @return The Settings instance.
     */
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }
}

/**
 * Settings class for the fighter holding all fighter key binds
 */
class FighterSettings {
    private int leftKey = KeyEvent.VK_LEFT;
    private int rightKey = KeyEvent.VK_RIGHT;
    private int upKey = KeyEvent.VK_UP;
    private int downKey = KeyEvent.VK_DOWN;
    private int attack1Key = KeyEvent.VK_Z;
    private int attack2Key = KeyEvent.VK_X;

    Fighter fighter;

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
     * Sets the key binding for the "left" action.
     *
     * @param keyCode the key code for the new "left" key binding
     */
    public void setLeftKey(int keyCode) {
        this.leftKey = keyCode;
    }

    /**
     * Sets the key binding for the "right" action.
     *
     * @param keyCode the key code for the new "right" key binding
     */
    public void setRightKey(int keyCode) {
        this.rightKey = keyCode;
    }

    /**
     * Sets the key binding for the "up" action.
     *
     * @param keyCode the key code for the new "up" key binding
     */
    public void setUpKey(int keyCode) {
        this.upKey = keyCode;
    }

    /**
     * Sets the key binding for the "down" action.
     *
     * @param keyCode the key code for the new "down" key binding
     */
    public void setDownKey(int keyCode) {
        this.downKey = keyCode;
    }

    /**
     * Sets the key binding for the "attack1" action.
     *
     * @param keyCode the key code for the new "attack1" key binding
     */
    public void setAttack1Key(int keyCode) {
        this.attack1Key = keyCode;
    }

    /**
     * Sets the key binding for the "attack2" action.
     *
     * @param keyCode the key code for the new "attack2" key binding
     */
    public void setAttack2Key(int keyCode) {
        this.attack2Key = keyCode;
    }

    /**
     * Returns the key code for the "left" action.
     *
     * @return the key code associated with the "left" action
     */
    public int getLeftKey() {
        return this.leftKey;
    }

    /**
     * Returns the key code for the "right" action.
     *
     * @return the key code associated with the "right" action
     */
    public int getRightKey() {
        return this.rightKey;
    }

    /**
     * Returns the key code for the "up" action.
     *
     * @return the key code associated with the "up" action
     */
    public int getUpKey() {
        return this.upKey;
    }

    /**
     * Returns the key code for the "down" action.
     *
     * @return the key code associated with the "down" action
     */
    public int getDownKey() {
        return this.downKey;
    }

    /**
     * Returns the key code for the "attack1" action.
     *
     * @return the key code associated with the "attack1" action
     */
    public int getAttack1Key() {
        return this.attack1Key;
    }

    /**
     * Returns the key code for the "attack2" action.
     *
     * @return the key code associated with the "attack2" action
     */
    public int getAttack2Key() {
        return this.attack2Key;
    }

}
