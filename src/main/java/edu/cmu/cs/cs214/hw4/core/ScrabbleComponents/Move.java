package edu.cmu.cs.cs214.hw4.core.ScrabbleComponents;

import edu.cmu.cs.cs214.hw4.core.LetterTile.LetterTile;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.BoardTile;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.Location;
import edu.cmu.cs.cs214.hw4.core.SpecialTile.NegativePoints;
import edu.cmu.cs.cs214.hw4.core.SpecialTile.SpecialTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Datatype representing a move in Scrabble of placing down tiles.
 */
public class Move {
  /** The letter tiles that are placed down on the board. */
  private LetterTile[] tiles;
  /** The locations of the letter tiles that are placed down on the board. */
  private Location[] locations;
  /** The list of words that are formed. */
  private List<BoardTile[]> words;
  /** The list of ACTUAL words that are formed. */
  private final List<String> wordS;
  /** The list of special tiles that the letter tiles were placed on. */
  private List<SpecialTile> specialTiles;

  /** Signifies if the things have been placed. */
  private boolean placed;

  /**
   * Returns the total number of points of a move.
   * @return total number of points of a move
   */
  public int computeScore() {
    int result = 0;
    for (BoardTile[] word : words) {
      int temp = 0;
      int multiplier = 1;
      boolean isNegative = false;
      for (BoardTile aWord : word) {
        temp += aWord.value();
        if (aWord.hasMultWord()) {
          multiplier *= aWord.getMultWord().getMultiplier();
        }
        if (aWord.hasSpecial()
                && aWord.getSpecial() instanceof NegativePoints) {
          isNegative = true;
        }
      }
      if (isNegative) {
        multiplier *= -1;
      }
      result += temp * multiplier;
    }
    return result;
  }

  /**
   * Data type that holds a move in scrabble for placing down tiles.
   */
  public Move() {
    this.tiles = null;
    this.locations = null;
    this.words = null;
    this.wordS = new ArrayList<>();

    specialTiles = new ArrayList<>();
    placed = false;
  }

  /**
   * Sets the data for the move type.
   * @param tileL letter tiles being placed
   * @param locationL locations of letter tile
   * @param wordL list of words formed by board tiles
   */
  public void setPlacements(final LetterTile[] tileL,
                            final Location[] locationL,
                            final List<BoardTile[]> wordL) {
    placed = true;
    this.tiles = tileL;
    this.locations = locationL;
    this.words = wordL;

    for (BoardTile[] word : wordL) {
      StringBuilder str = new StringBuilder();
      for (BoardTile aWord : word) {
        str.append(aWord.toString());
      }
      wordS.add(str.toString());
    }
  }

  /**
   * Adds a special tile to the total move represented by Move.
   * @param special tile to be added
   */
  public void addSpecialTile(final SpecialTile special) {
    specialTiles.add(special);
  }

  /**
   * Removes a special tile.
   * @param special tile to be added
   */
  public void remSpecialTile(final SpecialTile special) {
    specialTiles.remove(special);
  }

  /**
   * Returns whether tiles have been placed (formally).
   * @return whether tiles have been placed (formally)
   */
  public boolean isPlaced() {
    return placed;
  }

  /**
   * Returns the special tiles placed.
   * @return special tiles placed
   */
  public List<SpecialTile> getSpecialTiles() {
    return specialTiles;
  }

  /**
   * Returns letter tiles.
   * @return letter tiles
   */
  public LetterTile[] getTiles() {
    return tiles;
  }

  /**
   * Returns locations of letter tiles.
   * @return locations letter tiles
   */
  public Location[] getLocations() {
    return locations;
  }

  /**
   * Returns score of move.
   * @return score of move
   */
  public int getScore() {
    return computeScore();
  }

  /**
   * Returns words formed.
   * @return words formed
   */
  public List<String> getWords() {
    return wordS;
  }
}
