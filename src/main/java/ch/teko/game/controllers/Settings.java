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

/**
 * Settings class initializing variables by reading a provided config file.
 */
public class Settings {
    /**
     * Hardcoded config filename
     */
    private final String PROPERTIES_FILE = "config.properties";

    /**
     * Fighter one settings
     */
    private FighterSettings fighter1;
    
    /**
     * Fighter two settings
     */
    private FighterSettings fighter2;

    /**
     * Log4J logger instance
     */
    private Logger log = LogManager.getLogger(Main.class);

    /**
     * Instance of Settings for singleton pattern.
     */
    private static Settings instance;

    /**
     * Settings constructor initializing the fighter settings
     */
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
     * Gets the Fighter1
     * @return Fighter 1 settings
     */
    public FighterSettings getFighter1() {
        return this.fighter1;
    }

    /**
     * Gets the Fighter2
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
