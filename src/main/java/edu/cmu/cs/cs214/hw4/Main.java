package edu.cmu.cs.cs214.hw4;

import edu.cmu.cs.cs214.hw4.gui.ScrabbleGameGui;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The class that runs the GUI with the core.
 */
public final class Main {
  /**
   * Default constructor, never called.
   */
  private Main() {
  }

  /**
   * The main function which runs the scrabble game.
   * @param args command arguments.
   */
  public static void main(final String[] args) {
    SwingUtilities.invokeLater(() -> {
      createAndShowGameBoard();
    });
  }

  /**
   * Creates the game GUI and setup.
   */
  private static void createAndShowGameBoard() {
    JFrame frame = new JFrame("Start Scrabble");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    frame.add(new ScrabbleGameGui());
  }
}
