package edu.cmu.cs.cs214.hw4.core.Scrabble;

/**
 * Overall game listener for Scrabble.
 */
public interface GameChangeListener {
  /**
   * Transitions to the next player's GUI.
   * @param succesful transition
   */
  void nextPlayerGui(boolean succesful);

  /**
   * Skips next player's turn.
   */
  void skipNextPlayerTurn();

  /**
   * Starts the game.
   */
  void startGame();

  /**
   * Ends the game.
   */
  void endGame();

  /**
   * Reverses the ordering.
   */
  void reverseOrdering();
}
