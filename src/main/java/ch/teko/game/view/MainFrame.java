package ch.teko.game.view;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import ch.teko.game.*;
import ch.teko.game.model.*;
import ch.teko.game.controllers.*;

/**
 * The {@code MainFrame} class extends {@code JFrame} and serves as the main
 * window for the game application.
 */
public class MainFrame extends JFrame {

  // Flag to skip the startup menu for debugging purposes
  private final boolean instantStart = false;

  // Dimensions of the main game window
  private final int WIDTH = 600;
  private final int HEIGHT = 400;

  // Logger for logging purposes
  private Logger log = LogManager.getLogger(Main.class);

  // Game view for displaying the game
  private Game gameView;

  // Menu view for displaying the main menu
  private Menu menuView;

  // Winner view for displaying the winner screen
  private Winner winnerView;

  /**
   * Loads the game settings from the settings file using the {@code Settings}
   * singleton.
   */
  void loadSettings() {
    Settings.getInstance().load();
  }

  /**
   * Adds a hook to save settings when the main window is closing.
   * This method ensures that the game settings are saved when the user
   * closes the application window.
   *
   * @param frame the {@code JFrame} to which the window listener is added.
   */
  void saveSettingsHook(JFrame frame) {
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent event) {
        Settings.getInstance().save();
      }
    });
  }

  /**
   * Initializes the main frame, all views and starts the game loop.
   * 
   * @param assetsPath the {@code assetsPath} for the fighter assets
   */
  public MainFrame(String assetsPath) {
    loadSettings();
    saveSettingsHook(this);

    this.setTitle("Street Fighter");

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(WIDTH, HEIGHT);
    this.setAlwaysOnTop(true);
    this.setResizable(false);
    this.addKeyListener(InputController.getInstance());

    this.gameView = new Game(assetsPath);
    this.menuView = new Menu((JFrame) this);
    this.winnerView = new Winner();

    JLayeredPane layeredPane = new JLayeredPane();
    this.setContentPane(layeredPane);

    gameView.setBounds(0, 0, this.getWidth(), this.getHeight());
    menuView.setBounds(0, 0, this.getWidth(), this.getHeight());
    winnerView.setBounds(0, 0, this.getWidth(), this.getBounds().height);

    layeredPane.add(gameView, JLayeredPane.DEFAULT_LAYER);
    layeredPane.add(menuView, JLayeredPane.PALETTE_LAYER);
    layeredPane.add(winnerView, JLayeredPane.PALETTE_LAYER);

    if (!this.instantStart) {
      menuView.openMenu(true);
    }

    this.setVisible(true);

    while (this.menuView.isOpen()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    StartCounter.getInstance().start();

    loop();
  }

  /**
   * The game main loop
   */
  private void loop() {
    final int TICKS = 60;
    final double nsPerTick = 1000000000 / TICKS;

    int player1Wins = 0;
    int player2Wins = 0;
    while (true) {
      int ticks = 0;
      int frames = 0;
      long lastTime = System.nanoTime();
      long lastTimer = System.currentTimeMillis();
      double delta = 0;
      boolean roundEnd = false;

      while (true) {
        if (InputController.getInstance().escape) {
          if (!this.menuView.isOpen())
            menuView.openMenu(false);

          InputController.getInstance().escape = false;
        }

        long now = System.nanoTime();
        delta += (now - lastTime) / nsPerTick;
        lastTime = now;

        while (delta >= 1) {
          ticks++;

          InputController.getInstance().onTick();

          boolean softFreeze = this.menuView.isOpen() || StartCounter.getInstance().isRunning();

          // Soft freeze the game
          if (!softFreeze)
            this.gameView.loop();

          if (this.gameView.player1Won()) {
            player1Wins++;
            roundEnd = true;
            break;
          }

          if (this.gameView.player2Won()) {
            player2Wins++;
            roundEnd = true;
            break;
          }

          delta--;
        }

        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        frames++;

        this.gameView.repaint();

        if (System.currentTimeMillis() - lastTimer > 1000) {
          log.info("ticks {} frames {}", ticks, frames);
          lastTimer += 1000;
          frames = 0;
          ticks = 0;
        }

        if (roundEnd)
          break;
      }

      boolean gameEnded = false;
      if (player1Wins == 2) {
        this.winnerView.player1Won();
        player1Wins = 0;
        player2Wins = 0;
        gameEnded = true;
        requestFocus();
      }

      if (player2Wins == 2) {
        this.winnerView.player2Won();
        player1Wins = 0;
        player2Wins = 0;
        gameEnded = true;
        requestFocus();
      }

      this.gameView.reset();

      if (gameEnded) {
        StartCounter.getInstance().start();
      }
    }
  }
}
