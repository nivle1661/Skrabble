package edu.cmu.cs.cs214.hw4.core.Scrabble;

import edu.cmu.cs.cs214.hw4.core.LetterTile.LetterTile;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.Location;
import edu.cmu.cs.cs214.hw4.core.SpecialTile.SpecialTile;

import java.util.List;

/**
 * Takes in responses from the GUI and changes the current game state
 * accordingly.
 */
public interface PlayerChangeListener {
  /**
   * Starts a new turn for the listener.
   */
  void startTurn();

  /**
   * Places a tile on the GUI.
   * @param tile to be placed
   * @param loc location
   */
  void placeLetterTileOnBoard(LetterTile tile, Location loc);

  /**
   * Finalizes tile placements with words formed.
   * @param words formed
   */
  void placeWords(List<String> words);

  /**
   * Adds a tile back into the rack.
   * @param tile to be removed
   * @param loc location of tile to be removed from board
   */
  void returnTileToRack(LetterTile tile, Location loc);

  /**
   * Swaps tiles from the current player.
   * @param oldTiles to swap out
   * @param newTiles to put in
   * @param isVisible if the old tiles should be visible or not
   */
  void swapTiles(LetterTile[] oldTiles,
                 LetterTile[] newTiles,
                 boolean isVisible);

  /**
   * Skips a turn.
   */
  void skipTurn();

  /**
   * Finishes a turn (updates the score).
   */
  void finishTurn();

  /**
   * Skips a turn.
   */
  void winChallenge();

  /**
   * Skips a turn.
   * @param name of challenger
   */
  void loseChallenge(String name);

  /**
   * Buys a tile.
   * @param bought tile
   */
  void boughtTile(SpecialTile bought);

  /**
   * Places a special tile from placement (either succesfully or unsuccesfully).
   * @param lost special tile
   * @param successful placement
   */
  void placeSpecialTile(SpecialTile lost, boolean successful);
}
