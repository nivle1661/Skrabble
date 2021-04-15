package edu.cmu.cs.cs214.hw4.core.ScrabbleComponents;

import edu.cmu.cs.cs214.hw4.core.LetterTile.LetterTile;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.Location;

import java.util.Objects;

/**
 * Contains a letter tile and the location to place it.
 */
public class Placement implements Comparable<Placement> {
  /** Tile to place. */
  private LetterTile tile;

  /** Location to place tile. */
  private Location loc;

  /**
   * Creates a placement object from a tile and a location.
   * @param tileL to place
   * @param locL location to place tileL
   */
  public Placement(final LetterTile tileL, final Location locL) {
    tile = tileL;
    loc = locL;
  }

  /**
   * Returns location.
   * @return location
   */
  public Location getLoc() {
    return loc;
  }

  /**
   * Returns letter tile.
   * @return letter tile
   */
  public LetterTile getTile() {
    return tile;
  }

  /**
   * Comparator for two placements which is based on location.
   * @param o other placement
   * @return the comparison between two placements.
   */
  @Override
  public int compareTo(final Placement o) {
    return loc.compareTo(o.loc);
  }

  /**
   * Returns whether o is the same placement, which is if they have same letter
   * and location.
   * @param o comparing object
   * @return whether o is the same placement.
   */
  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof Placement)) {
      return false;
    }
    Placement placement = (Placement) o;
    return loc.equals(placement.loc) && tile.equals(placement.tile);
  }

  /**
   * Returns hashcode.
   * @return hashcode
   */
  @Override
  public int hashCode() {
    return Objects.hash(tile.hashCode(), loc.hashCode());
  }
}
