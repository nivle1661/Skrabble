package edu.cmu.cs.cs214.hw4.core.SpecialTile;

import edu.cmu.cs.cs214.hw4.core.Player.Player;
import edu.cmu.cs.cs214.hw4.core.Scrabble.ScrabbleImpl;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.Location;

/**
 * Represents a Remove consonants tile.
 */
public class RemoveConsonants implements SpecialTile {
  /** Location of the reverse tile. */
  private Location loc;

  /** Whoever places the special tile. */
  private final Player player;

  /** Index of the special tile. */
  private final int index = 4;

  /**
   * Constructs a remove consonants tile.
   * @param placer self-explanatory
   */
  public RemoveConsonants(final Player placer) {
    player = placer;
  }

  /**
   * Reverses the order of players in the game.
   * @param game where it is activated.
   */
  @Override
  public void activate(final ScrabbleImpl game) {
    game.removeConsonants();
  }

  /**
   * Specifices the location of the reverse tile.
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
    return "Remove Consonants";
  }

  /**
   * Returns if object is equal.
   * @return if object is equal
   */
  @Override
  public boolean equals(final Object o) {
    return o instanceof RemoveConsonants;
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
