package ch.teko.game.view;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ch.teko.game.*;
import ch.teko.game.model.*;
import ch.teko.game.controllers.*;

public class Game extends JPanel {
  private final int width = 1200;
  private final int height = 600;

  private Logger log = LogManager.getLogger(Main.class);

  Fighter f1;
  Menu menu;
  public Game(String assetsPath) {
    f1 = new Fighter(0,0, assetsPath);

    JFrame frame = new JFrame("Street Fighter");

    this.menu = new Menu(frame);
    frame.getContentPane().add(this);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(width, height);
    frame.setVisible(true);
    frame.setAlwaysOnTop(true);
    frame.setResizable(false);
    
    frame.addKeyListener(InputController.getInstance());


    double interpolation = 0;
    final int TICKS = 60;
    final double nsPerTick = 1000000000 / TICKS;

    int ticks = 0;
    int frames = 0;
    long lastTime = System.nanoTime();
    long lastTimer = System.currentTimeMillis();
    double delta = 0;

    menu.openMenu(true);
    while (true) {
      while (menu.isOpen()) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      
      if (InputController.getInstance().escape) {
        menu.openMenu(false);
        repaint();
        InputController.getInstance().escape = false;
        continue;
      }
      
      long now = System.nanoTime();
      delta += (now - lastTime) / nsPerTick;
      lastTime = now;

      while (delta >= 1) {
        ticks++;
        InputController.getInstance().onTick();
        f1.onTick();
        delta--;
      }

      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      frames++;
      repaint();

      if (System.currentTimeMillis() - lastTimer > 1000) {
        log.info("ticks {} frames {}", ticks, frames);
        lastTimer += 1000;
        frames = 0;
        ticks = 0;
      }
    }
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (!menu.onRender(g))
      f1.onRender(g);
  }
}
