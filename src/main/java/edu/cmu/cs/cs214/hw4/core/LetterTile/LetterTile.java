package edu.cmu.cs.cs214.hw4.core.LetterTile;

/**
 * Represents a letter tile from Scrabble.
 */
public class LetterTile {
  /** The letter on the tile. */
  private final char letter;

  /** The score of the tile. */
  private final int score;

  /**
   * Represents a letter tile in scrabble with a letter and score value.
   * @param letterL of the tile
   * @param scoreL of the tile
   */
  public LetterTile(final char letterL, final int scoreL) {
    this.letter = letterL;
     this.score = scoreL;
  }

  /**
   * Gets the letter.
   * @return letter
   */
  public char getLetter() {
    return letter;
  }

  /**
   * Gets the score.
   * @return score
   */
  public int getScore() {
    return score;
  }

  /**
   * Returns whether or not another object is equal to the letter tile. This
   * is true when the letters on both are the same.
   * @param o comparing value
   * @return whether or not another object is equal to the letter tile
   */
  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof LetterTile)) {
      return false;
    }
    LetterTile tile = (LetterTile) o;
    return tile.letter == letter;
  }

  /**
   * Returns the hashcode of the letter tile, which is the hashcode of the
   * letter.
   * @return the hashcode of the letter tile
   */
  @Override
  public int hashCode() {
    return Character.hashCode(letter);
  }

  /**
   * Returns the string representation of the letter tile, which is the letter
   * followed by a whitespace.
   * @return string representation of the letter tile
   */
  @Override
  public String toString() {
    return "" + letter;
  }

  /**
   * Returns whether the letter tile is a consonant or not.
   * @return whether the letter tile is a consonant or not
   */
  public boolean isConsonant() {
    return (letter != 'a' && letter != 'e' && letter != 'i'
            && letter != 'o' && letter != 'u');
  }
}
