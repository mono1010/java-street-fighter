package ch.teko.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.teko.game.view.MainFrame;

/**
 * The Main class containing the entry point of the application.
 */
public class Main {

  /**
   * Boolean representing if the application is starting as in production
   */
  public static boolean PRODUCTION = false;
  public static boolean USE_RESOURCE_ASSETS = false;

  /**
   * Logger for logging purposes
   */
  private static Logger log = LogManager.getLogger(Main.class);

  /**
   * Empty and unused constructor
   */
  public Main() {

  }

  /**
   * The entry point of the application.
   * This will only init the MainFrame class
   * 
   * @param args The path to the assets folder for the selected player.
   */
  public static void main(String[] args) {
    Main.PRODUCTION = Thread.currentThread().getContextClassLoader().getResource("prod.properties") != null;
    Main.USE_RESOURCE_ASSETS = args.length != 1;

    if (Main.USE_RESOURCE_ASSETS) {
      log.info("Using assets from resources folder");
      new MainFrame("assets/fighter/");
    } else {
      new MainFrame(args[0]);
    }
  }
}
