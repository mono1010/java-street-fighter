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

class GameView extends JPanel {

  Fighter f1, f2;

  final int startOffset = 80;

  public GameView(String assetsPath) {
    Map.getInstance().setWidth(600);
    Map.getInstance().setHeight(400);

    final int height = Map.getInstance().getHeight();
    final int width = Map.getInstance().getWidth();

    Floor.getInstance().setHeight(height - 50);

    f1 = new Fighter(startOffset, Floor.getInstance().getHeight(), true, assetsPath);
    f2 = new Fighter(width - startOffset, Floor.getInstance().getHeight(), false, assetsPath);
  }

  public void reset() {
    final int height = Map.getInstance().getHeight();
    final int width = Map.getInstance().getWidth();
    f1 = new Fighter(startOffset, Floor.getInstance().getHeight(), true, f1.getAssetsManager());
    f2 = new Fighter(width - startOffset, Floor.getInstance().getHeight(), false, f2.getAssetsManager());
  }

  public boolean player1Won() {
    return f2.isDead();
  }

  public boolean player2Won() {
    return f1.isDead();
  }

  void player1Attack() {
    Optional<Rectangle> hitBoxOptional = f1.getHitBox();
    if (hitBoxOptional.isPresent()) {
      Rectangle hitBox = hitBoxOptional.get();
      Rectangle aabb = f2.getAABB();
      if (hitBox.intersects(aabb)) {
        f2.damage(7);
      }
    }
  }

  void player2Attack() {
    Optional<Rectangle> hitBoxOptional = f2.getHitBox();
    if (hitBoxOptional.isPresent()) {
      Rectangle hitBox = hitBoxOptional.get();
      Rectangle aabb = f1.getAABB();
      if (hitBox.intersects(aabb)) {
        f1.damage(7);
      }
    }
  }

  void loop() {
    f1.onTick();
    f2.onTick();

    player1Attack();
    player2Attack();

    Health.getInstance().setHealth(f1.getHealth(), f1.getMaxHealth(), f2.getHealth(), f2.getMaxHealth());
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Floor.getInstance().onRender(g);
    Health.getInstance().onRender(g);

    f1.onRender(g);
    f2.onRender(g);
  }
}

public class Game extends JFrame {
  private final boolean instantStart = true;

  private final int WIDTH = 600;
  private final int HEIGHT = 400;

  private Logger log = LogManager.getLogger(Main.class);

  private GameView gameView;
  private Menu menuView;
  private Winner winnerView;

  void loadSettings() {
    Settings.getInstance().load();
  }

  void saveSettingsHook(JFrame frame) {
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent event) {
        Settings.getInstance().save();
      }
    });
  }

  public Game(String assetsPath) {
    loadSettings();
    saveSettingsHook(this);

    this.setTitle("Street Fighter");

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(WIDTH, HEIGHT);
    this.setAlwaysOnTop(true);
    this.setResizable(false);
    this.addKeyListener(InputController.getInstance());

    this.gameView = new GameView(assetsPath);
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

          // Soft freeze the game
          if (!this.menuView.isOpen())
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

      if (player1Wins == 2) {
          this.winnerView.player1Won();
          player1Wins = 0;
          player2Wins = 0;
          requestFocus();
      }

      if (player2Wins == 2) {
          this.winnerView.player2Won();
          player1Wins = 0;
          player2Wins = 0;
          requestFocus();
      }

      this.gameView.reset();
    }
  }
}
