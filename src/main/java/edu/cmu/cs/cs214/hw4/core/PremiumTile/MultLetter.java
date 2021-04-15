package edu.cmu.cs.cs214.hw4.core.PremiumTile;

import java.awt.Color;

/**
 * Represents a letter multiplier on a board tile.
 */
public class MultLetter {
  /** Double multiplier. */
  public static final int TWO = 2;

  /** Triple multiplier. */
  public static final int THREE = 3;

  /** Multiplier of the letter. */
  private final int multiplier;

  /**
   * Creates a letter multiplier with specified multiplier amount.
   * @param multiplierL multiplier for the letter
   */
  public MultLetter(final int multiplierL) {
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
      return Color.BLUE;
    }
    return Color.CYAN;
  }

  /**
   * Returns the string representation of the letter multiplier.
   * Double multiplier is DL, triple multiplier is TL.
   * @return string representation of the letter multiplier
   */
  @Override
  public String toString() {
    if (multiplier == TWO) {
      return "DL";
    }
    if (multiplier == THREE) {
      return "TL";
    }
    return Integer.toString(multiplier);
  }
}
