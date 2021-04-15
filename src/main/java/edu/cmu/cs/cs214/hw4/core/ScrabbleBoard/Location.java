package edu.cmu.cs.cs214.hw4.core.ScrabbleBoard;

import java.util.Objects;

/**
 * Represents a location on the scrabble board.
 */
public class Location implements Comparable<Location> {
  /** Row of location. */
  private final int x;

  /** Column of location. */
  private final int y;

  /**
   * Represents a location on a board.
   * @param xL column
   * @param yL row
   */
  public Location(final int xL, final int yL) {
    this.x = xL;
    this.y = yL;
  }

  /**
   * Returns x value.
   * @return x
   */
  public int getX() {
    return x;
  }

  /**
   * Returns y value.
   * @return y
   */
  public int getY() {
    return y;
  }

  /**
   * Compares location based on x value, then y value.
   * Assumes that o is not the same location.
   * @param o other location
   * @return comparison value
   */
  @Override
  public int compareTo(final Location o) {
    if (o == null) {
      throw new NullPointerException();
    }

    if (x < o.x || (x == o.x && y < o.y)) {
      return -1;
    }
    return 1;
  }

  /**
   * Checks if location is equal to another object.
   * @param o other object
   * @return comparison value
   */
  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof Location)) {
      return false;
    }

    Location loc = (Location) o;
    return loc.x == x && loc.y == y;
  }

  /**
   * Returns hashcode of the location.
   * @return hashcode
   */
  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }



}
