package edu.cmu.cs.cs214.hw4.core.PremiumTile;


import java.awt.Color;

/**
 * Represents a word multiplier on a board tile.
 */
public class MultWord {
  /** Double multiplier. */
  public static final int TWO = 2;

  /** Triple multiplier. */
  public static final int THREE = 3;

  /** Multiplier of the word. */
  private final int multiplier;

  /**
   * Creates a word multiplier with specified multiplier amount.
   * @param multiplierL for the word
   */
  public MultWord(final int multiplierL) {
    this.multiplier = multiplierL;
  }

  /**
   * Get the multiplier.
   * @return multiplier
   */
  public int getMultiplier() {
    return multiplier;
  }

  /**
   * Returns appropriate color of letter multiplier.
   * @return color of letter multiplier
   */
  public Color getColor() {
    if (multiplier == THREE) {
      return Color.ORANGE;
    }
    return Color.PINK;
  }

  /**
   * Returns the string representation of the word multiplier.
   * Double multiplier is DW, triple multiplier is TW.
   * @return string representation of the word multiplier
   */
  @Override
  public String toString() {
    if (multiplier == TWO) {
      return "DW";
    }
    if (multiplier == THREE) {
      return "TW";
    }
    return Integer.toString(multiplier) + "W";
  }
}
