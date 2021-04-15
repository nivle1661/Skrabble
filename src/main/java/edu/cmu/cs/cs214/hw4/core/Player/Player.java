package edu.cmu.cs.cs214.hw4.core.Player;

import edu.cmu.cs.cs214.hw4.core.ScrabbleComponents.BagOfTiles;
import edu.cmu.cs.cs214.hw4.core.LetterTile.LetterTile;
import edu.cmu.cs.cs214.hw4.core.SpecialTile.SpecialTile;
import edu.cmu.cs.cs214.hw4.core.SpecialTile.Boom;
import edu.cmu.cs.cs214.hw4.core.SpecialTile.Reverse;
import edu.cmu.cs.cs214.hw4.core.SpecialTile.Skip;
import edu.cmu.cs.cs214.hw4.core.SpecialTile.NegativePoints;
import edu.cmu.cs.cs214.hw4.core.SpecialTile.RemoveConsonants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a player in Scrabble.
 */
public class Player {
  /** The number of letter tiles a player starts with and mantains. */
  public static final int MAX_NUM_TILES = 7;

  /** The letter tiles the player has currently. */
  private List<LetterTile> letters;

  /** The special tiles to draw from. */
  private final List<SpecialTile> specialties;

  /** The special tiles the player has currently. */
  private Map<SpecialTile, Integer> specialTileIntegerMap;
  /** The special tiles the player has currently. */
  private List<SpecialTile> specialTiles;

  /** The total number of points the player has. */
  private int score;

  /** The name of the player. */
  private String name;

  /** Whether or not the player's next turn is skipped. */
  private boolean nextTurnSkipped;

  /**
   * Creates a player with a given name.
   * @param nameL of player
   */
  public Player(final String nameL) {
    letters = new ArrayList<>();
    specialties = new ArrayList<>();
    specialties.add(new Boom(this));
    specialties.add(new Reverse(this));
    specialties.add(new Skip(this));
    specialties.add(new NegativePoints(this));
    specialties.add(new RemoveConsonants(this));

    specialTileIntegerMap = new HashMap<>();
    specialTileIntegerMap.put(new Boom(this), 0);
    specialTileIntegerMap.put(new Reverse(this), 0);
    specialTileIntegerMap.put(new Skip(this), 0);
    specialTileIntegerMap.put(new NegativePoints(this), 0);
    specialTileIntegerMap.put(new RemoveConsonants(this), 0);

    specialTiles = new ArrayList<>();
    score = 0;
    this.name = nameL;
    nextTurnSkipped = false;
  }

  /**
   * Add points to the player.
   * @param numPoints to add
   */
  public void addPoints(final int numPoints) {
    score += numPoints;
  }

  /**
   * Loses a letter tile from the player's hand momentarily.
   * @param tile to remove
   */
  public void loseTile(final LetterTile tile) {
    for (LetterTile letter : letters) {
      if (tile.equals(letter)) {
        letters.remove(letter);
        return;
      }
    }
  }

  /**
   * Adds a letter tile back into the player's hand.
   * @param tile to add
   */
  public void addTile(final LetterTile tile) {
    letters.add(tile);
  }

  /**
   * Draws some number of tiles from the bag of tiles into the player's hand.
   * @param numTiles number of tiles to replace
   * @param bag of tiles
   * @return new tiles
   */
  public LetterTile[] replaceTiles(final int numTiles, final BagOfTiles bag) {
    LetterTile[] newTiles = bag.drawTiles(numTiles);
    letters.addAll(Arrays.asList(newTiles));
    return newTiles;
  }

  /**
   * Swap some list of tiles from the player's hand with the scrabble bag.
   * @param tiles to replace
   * @param bag of tiles
   * @return new tiles
   */
  public LetterTile[] swapTiles(final LetterTile[] tiles,
                                final BagOfTiles bag) {
    for (LetterTile tile : tiles) {
      if (letters.contains(tile)) {
        letters.remove(tile);
      }
    }
    LetterTile[] newTiles = bag.swapTiles(tiles);
    letters.addAll(Arrays.asList(newTiles));
    return newTiles;
  }

  /**
   * Makes the player lose his next turn.
   */
  public void loseNextTurn() {
        nextTurnSkipped = true;
    }

  /**
   * Returns whether or not this player's next turn is skipped.
   * @return whether or not this player's next turn is skipped.
   */
  public boolean isTurnSkipped() {
        return nextTurnSkipped;
    }

  /**
   * Skips the players turn.
   */
  public void skipTurn() {
    nextTurnSkipped = false;
  }

  /**
   * Gets the player's score.
   * @return score of the player
   */
  public int getScore() {
    return score;
  }

  /**
   * returns letter tiles of the player.
   * @return letter tiles of the player
   */
  public LetterTile[] getLetters() {
    return letters.toArray(new LetterTile[letters.size()]);
  }

  /**
   * Simulates the player buying a special tile.
   * @return special tile bought
   */
  public SpecialTile buySpecialTile() {
    if (score >= SpecialTile.COST_OF_SPECIAL_TILE) {
      Collections.shuffle(specialties);
      SpecialTile toAdd = specialties.get(0);

      int count = specialTileIntegerMap.get(toAdd);
      specialTileIntegerMap.put(toAdd, count + 1);
      specialTiles.add(toAdd);

      score -= SpecialTile.COST_OF_SPECIAL_TILE;
      return toAdd;
    }
    return null;
  }

  /**
   * Returns the name of player.
   * @return the name of player
   */
  public String getName() {
    return name;
  }

  /**
   * Restarts a player's hand and score.
   */
  public void clearHand() {
    letters = new ArrayList<>();

    specialTileIntegerMap.put(new Boom(this), 0);
    specialTileIntegerMap.put(new Reverse(this), 0);
    specialTileIntegerMap.put(new Skip(this), 0);
    specialTileIntegerMap.put(new NegativePoints(this), 0);
    specialTileIntegerMap.put(new RemoveConsonants(this), 0);
    score = 0;
  }

  /**
   * Returns map from special tiles to count.
   * @return map from special tiles to count
   */
  public Map<SpecialTile, Integer> getSpecialTileIntegerMap() {
    return specialTileIntegerMap;
  }

  /**
   * Returns list of special tiles.
   * @return list of special tiles
   */
  public List<SpecialTile> getSpecialTiles() {
    return specialTiles;
  }

  /**
   * Returns string representation of the player, which contains name, letters,
   * score.
   * @return string representation of player.
   */
  @Override
  public String toString() {
    String result = name + " (score = " + score + "): <";
    for (LetterTile letter : letters) {
      result += letter.toString();
    }
    result += ">";
    return result;
  }

  /**
   * Lose special tile.
   * @param tile to lose
   */
  public void loseSpecialTile(final SpecialTile tile) {
    specialTiles.remove(tile);

    int count = specialTileIntegerMap.get(tile);
    specialTileIntegerMap.put(tile, count - 1);
  }
}
