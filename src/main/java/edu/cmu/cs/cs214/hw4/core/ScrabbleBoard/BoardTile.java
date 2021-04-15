package edu.cmu.cs.cs214.hw4.core.ScrabbleBoard;

import edu.cmu.cs.cs214.hw4.core.LetterTile.LetterTile;
import edu.cmu.cs.cs214.hw4.core.PremiumTile.MultLetter;
import edu.cmu.cs.cs214.hw4.core.PremiumTile.MultWord;
import edu.cmu.cs.cs214.hw4.core.SpecialTile.SpecialTile;

import java.awt.Color;


/**
 * Represents a tile on the board.
 */
public class BoardTile {
  /** Specifies if the tile on the board has a letter multiplier. */
  private boolean hasLetterMult;

  /** Specifies if the tile on the board has a word multiplier. */
  private boolean hasWordMult;

  /** The letter multiplier on the board tile, if any. */
  private final MultLetter multLetter;

  /** The word multiplier on the board tile, if any. */
  private final MultWord multWord;

  /** The special tile on the board tile, if any. */
  private SpecialTile specialTile;

  /** Specifies if the tile on the board has a special tile. */
  private boolean hasSpecialTile = false;

  /** Specifies if the tile on the board has a letter tile. */
  private boolean hasLetterTile = false;

  /** The letter tile on the board tile, if any. */
  private LetterTile letterTile = null;

  /** Hexadecimal conversion int. */
  private static final int HEX = 16;
  /** Color for a scrabble piece. */
  public static final Color LETTER_COLOR =
          new Color(Integer.parseInt("AB9584", HEX));
  /** Color for a special tile. */
  public static final Color SPECIAL_COLOR = Color.RED;

  /** Initializes a board tile with no multiplier effect. */
  public BoardTile() {
    hasLetterMult = false;
    hasWordMult = false;

    multLetter = null;
    multWord = null;
  }

  /**
   * Initializes a board tile with a letter multiplier.
   * @param mult letter multiplier
   */
  public BoardTile(final MultLetter mult) {
    hasLetterMult = true;
    hasWordMult = false;

    multLetter = mult;
    multWord = null;
  }

  /**
   * Initializes a board tile with a word multiplier.
   * @param mult word multiplier
   */
  public BoardTile(final MultWord mult) {
    hasLetterMult = false;
    hasWordMult = true;

    multLetter = null;
    multWord = mult;
  }

  /**
   * Removes a letter tile on the board tile. Reactivates multiplier effect
   * if it had one previously.
   */
  public void removeLetterTile() {
    letterTile = null;
    hasLetterTile = false;

    if (multLetter != null) {
      hasLetterMult = true;
    }
    if (multWord != null) {
      hasWordMult = true;
    }
  }

  /**
   * Places a letter tile on the board tile.
   * @param letter tile to be placed.
   */
  public void placeLetterTile(final LetterTile letter) {
    letterTile = letter;
    hasLetterTile = true;
  }

  /**
   * Places a special tile on the board.
   * @param special tile to be placed
   * @param loc location of the tile
   */
  public void placeSpecialTile(final SpecialTile special,
                               final Location loc) {
    special.setLocation(loc);
    specialTile = special;
    hasSpecialTile = true;
  }

  /**
   * Removes special tile on the board.
   */
  public void removeSpecialTile() {
    specialTile = null;
    hasSpecialTile = false;
  }

  /**
   * Returns whether there is a letter tile on the board tile.
   * @return whether there is a letter tile on the board tile
   */
  public boolean isOccupied() {
    return hasLetterTile;
  }

  /**
   * Effectively removes the word/letter multiplier effect from the boar tile.
   */
  public void turnOffEffect() {
    hasLetterMult = false;
    hasWordMult = false;
  }

  /**
   * returns whether there is a special tile on the board tile.
   * @return whether there is a special tile on the board tile
   */
  public boolean hasSpecial() {
    return hasSpecialTile;
  }

  /**
   * returns whether there is a letter multiplier effect on the board tile.
   * @return whether there is a letter multiplier effect on the board tile
   */
  public boolean hasMultLetter() {
    return hasLetterMult;
  }

  /**
   * returns whether there is a word multiplier effect on the board tile.
   * @return whether there is a word multiplier effect on the board tile
   */
  public boolean hasMultWord() {
    return hasWordMult;
  }

  /**
   * Gets special tile.
   * @return special tile
   */
  public SpecialTile getSpecial() {
    return specialTile;
  }

  /**
   * Get word multiplier.
   * @return word multiplier
   */
  public MultWord getMultWord() {
    return multWord;
  }

  /**
   * returns the value of the letter on the tile.
   * @return the value of the letter on the tile
   */
  public int value() {
    if (!hasLetterTile) {
      return 0;
    }
    if (hasLetterMult) {
      return letterTile.getScore() * multLetter.getMultiplier();
    }
    return letterTile.getScore();
  }

  /**
   * Returns the appropriate color for the board tile.
   * @return appropriate color
   */
  public Color getColor() {
    if (hasLetterTile) {
      return LETTER_COLOR;
    }
    if (hasLetterMult) {
      return multLetter.getColor();
    }
    if (hasWordMult) {
      return multWord.getColor();
    }
    return Color.lightGray;
  }

  /**
   * Returns the string representation of the board tile.
   * @return the string representation of the board tile
   */
  @Override
  public String toString() {
    if (!hasLetterTile) {
      if (hasLetterMult) {
        return multLetter.toString();
      }
      if (hasWordMult) {
        return multWord.toString();
      }
      return "  ";
    }
    return letterTile.toString();
  }

  /**
   * Returns if the board tile has a consonant.
   * @return if the board tile has a consonant
   */
  public boolean hasConsonant() {
    return hasLetterTile && letterTile.isConsonant();
  }
}
