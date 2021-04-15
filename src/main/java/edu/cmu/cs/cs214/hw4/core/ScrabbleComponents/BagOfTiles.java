package edu.cmu.cs.cs214.hw4.core.ScrabbleComponents;

import edu.cmu.cs.cs214.hw4.core.LetterTile.LetterTile;
import edu.cmu.cs.cs214.hw4.core.Player.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Collections;

/**
 * Represents the bag of tiles in scrabble, with 98 letter tiles inside.
 */
public class BagOfTiles {
  /**  Contains all the letter tiles inside the bag. */
  private List<LetterTile> tiles;

  /** Maps ever character to its point value in scrabble. */
  private final Map<Character, Integer> points;

  /** Maps every character to its frequency in scrabble. */
  private final Map<Character, Integer> frequency;

  /** Number of tiles needed to perform swaps. */
  private static final int NUM_TILES_SWAP = 7;

  /**
   * Initializes the map from character to point value by going through the
   * resource file.
   * @return map from character to point value
   */
  private Map<Character, Integer> initializePoints() {
    Map<Character, Integer> result = new HashMap<>();
    String dicPath = "points.txt";
    ClassLoader classLoader = this.getClass().getClassLoader();
    InputStream in = classLoader.getResourceAsStream(dicPath);

    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = reader.readLine()) != null) {
        Scanner temp = new Scanner(line);
        char letter = temp.next().charAt(0);
        int point = temp.nextInt();
        result.put(letter, point);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * Initializes the map from character to frequency by going through the
   * resource file.
   * @return map from character to frequency
   */
  private Map<Character, Integer> initializeFrequency() {
    Map<Character, Integer> result = new HashMap<>();
    String dicPath = "frequency.txt";
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream in = classLoader.getResourceAsStream(dicPath);

    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = reader.readLine()) != null) {
        Scanner temp = new Scanner(line);
        char letter = temp.next().charAt(0);
        int freq = temp.nextInt();
        result.put(letter, freq);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * Creates a bag of tiles for a scrabble game.
   */
  public BagOfTiles() {
    tiles = new ArrayList<>();
    frequency = initializeFrequency();
    points = initializePoints();
    for (char letter = 'a'; letter <= 'z'; letter++) {
      for (int i = 0; i < frequency.get(letter); i++) {
         tiles.add(new LetterTile(letter, points.get(letter)));
      }
    }
  }

  /**
   * Returns some number of tiles.
   * @param numTiles to draw
   * @return some number of tiles
   */
  public LetterTile[] drawTiles(final int numTiles) {
    int numberOfTiles = Math.min(numTiles, tiles.size());

    LetterTile[] draw = new LetterTile[numberOfTiles];
    for (int i = 0; i < numberOfTiles; i++) {
      Collections.shuffle(tiles);
      draw[i] = tiles.remove(0);
    }
    return draw;
  }

  /**
   * Takes in letter tiles and returns the same amount.
   * @param giveBack letter tiles given back to the bag
   * @return same amount of letter tiles given
   */
  public LetterTile[] swapTiles(final LetterTile[] giveBack) {
    if (tiles.size() < Player.MAX_NUM_TILES) {
      return null;
    }

    tiles.addAll(Arrays.asList(giveBack));
    return drawTiles(giveBack.length);
  }

  /**
   * Returns whether or not a player can swap tiles with the bag.
   * @return whether or not a player can swap tiles with the bag
   */
  public boolean canSwap() {
    return tiles.size() >= NUM_TILES_SWAP;
  }

  /**
   * Returns whether or not the bag is empty.
   * @return whether or not the bag is empty.
   */
  public boolean isEmpty() {
        return tiles.isEmpty();
    }

  /**
   * Returns number of tiles left.
   * @return number of tiles left
   */
  public int numberOfTiles() {
    return tiles.size();
  }
}
