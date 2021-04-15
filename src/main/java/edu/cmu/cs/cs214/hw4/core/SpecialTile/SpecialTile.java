package edu.cmu.cs.cs214.hw4.core.SpecialTile;

import edu.cmu.cs.cs214.hw4.core.Player.Player;
import edu.cmu.cs.cs214.hw4.core.Scrabble.ScrabbleImpl;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.Location;


/**
 * Interface for any special tile.
 */
public interface SpecialTile {
  /** Number of points to buy a tile. */
  int COST_OF_SPECIAL_TILE = 15;

  /**
   * Activates the special tile.
   * @param game where it is activated.
   */
  void activate(ScrabbleImpl game);

  /**
   * Specifices the location of a special tile when placed.
   * @param loc location of placement
   */
  void setLocation(Location loc);

  /**
   * Returns location of special tile for removal.
   * @return location
   */
  Location getLocation();


  /**
   * Returns the player who placed the tile.
   * @return player who placed the tile
   */
  Player getPlayer();
}
