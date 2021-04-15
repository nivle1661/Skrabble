package edu.cmu.cs.cs214.hw4.core.SpecialTile;

import edu.cmu.cs.cs214.hw4.core.Player.Player;
import edu.cmu.cs.cs214.hw4.core.Scrabble.ScrabbleImpl;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.Location;

/**
 * Represents a Skip tile.
 */
public class Skip implements SpecialTile {
  /** Location of skip tile. */
  private Location loc;

  /** Whoever places the special tile. */
  private final Player player;

  /** Index of the special tile. */
  private final int index = 3;

  /**
   * Constructs a skip tile.
   * @param placer self-explanatory
   */
  public Skip(final Player placer) {
    player = placer;
  }

  /**
   * Causes the next player to have his turn skipped.
   * @param game where it is activated.
   */
  @Override
  public void activate(final ScrabbleImpl game) {
    game.skipNextPlayer();
  }

  /**
   * Specifices the location of the skip tile when placed.
   * @param locL location of placement
   */
  @Override
  public void setLocation(final Location locL) {
    this.loc = locL;
  }

  /**
   * Returns location of special tile for removal.
   * @return location
   */
  @Override
  public Location getLocation() {
    return loc;
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
    return "Skip";
  }

  /**
   * Returns if object is equal.
   * @return if object is equal
   */
  @Override
  public boolean equals(final Object o) {
    return o instanceof Skip;
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
