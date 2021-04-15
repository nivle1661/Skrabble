package edu.cmu.cs.cs214.hw4.core.SpecialTile;

import edu.cmu.cs.cs214.hw4.core.Player.Player;
import edu.cmu.cs.cs214.hw4.core.Scrabble.ScrabbleImpl;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.Location;

/**
 * Represents the Negative Points tile.
 */
public class NegativePoints implements SpecialTile {
  /** Location of the tile. */
  private Location loc;

  /** Whoever places the special tile. */
  private final Player player;

  /** Index of the special tile. */
  private final int index = 1;

  /**
   * Constructs a negative points tile.
   * @param placer self-explanatory
   */
  public NegativePoints(final Player placer) {
    player = placer;
  }

  /**
   * Causes the player to have negative points for the turn.
   * @param game where it is activated.
   */
  @Override
  public void activate(final ScrabbleImpl game) {
    game.makePointsNegative();
  }

  /**
   * Specifices the location of the negative points tile.
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
    return "Negative ";
  }

  /**
   * Returns if object is equal.
   * @return if object is equal
   */
  @Override
  public boolean equals(final Object o) {
    return o instanceof NegativePoints;
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
