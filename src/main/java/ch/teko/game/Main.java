package ch.teko.game;

import ch.teko.game.view.Game;

/**
 * The Main class containing the entry point of the application.
 */
public class Main {

  /**
   * The entry point of the application.
   * This will only init the Game class
   * @param args The path to the assets folder for the selected player.
   */
  public static void main(String[] args) {
      new Game(args[0]);
  }
}
