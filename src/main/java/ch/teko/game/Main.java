package ch.teko.game;

import ch.teko.game.view.MainFrame;

/**
 * The Main class containing the entry point of the application.
 */
public class Main {

  /**
   * Empty and unused constructor
   */
  public Main() {

  }

  /**
   * The entry point of the application.
   * This will only init the MainFrame class
   * @param args The path to the assets folder for the selected player.
   */
  public static void main(String[] args) {
      new MainFrame(args[0]);
  }
}
