package edu.cmu.cs.cs214.hw4.core.SpecialTile;

import edu.cmu.cs.cs214.hw4.core.Player.Player;
import edu.cmu.cs.cs214.hw4.core.Scrabble.ScrabbleImpl;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.Location;

/**
 * Represents a boom tile.
 */
public class Boom implements SpecialTile {
  /** Where the radius of the blast starts. */
  private Location center;

  /** Whoever places the special tile. */
  private final Player player;

  /** The blast radius of boom. */
  public static final int BLAST_RADIUS = 3;

  /** Index of the special tile. */
  private final int index = 0;

  /**
   * Constructs a boom tile.
   * @param placer self-explanatory
   */
  public Boom(final Player placer) {
    player = placer;
  }

  /**
   * Destroys all letter tiles in 3 radius of center.
   * @param game where it is activated.
   */
  @Override
  public void activate(final ScrabbleImpl game) {
    game.boom(center);
  }

  /**
   * Specifices the location of a special tile when placed.
   * @param loc location of placement
   */
  @Override
  public void setLocation(final Location loc) {
    center = loc;
  }

  /**
   * Returns location of special tile for removal.
   * @return location
   */
  @Override
  public Location getLocation() {
    return center;
  }

  /**
   * Returns player who owns tile.
   * @return player who owns tile
   */
  @Override
  public Player getPlayer() {
    return player;
  }

  /**
   * Returns string representation.
   * @return string representation
   */
  @Override
  public String toString() {
    return "Boom";
  }

  /**
   * Returns if object is equal.
   * @return if object is equal
   */
  @Override
  public boolean equals(final Object o) {
    return o instanceof Boom;
  }

  /**
   * Returns hashcode.
   * @return hashcode
   */
  @Override
  public int hashCode() {
    return index;
  }
}
