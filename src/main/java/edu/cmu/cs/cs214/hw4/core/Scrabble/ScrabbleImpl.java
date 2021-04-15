package edu.cmu.cs.cs214.hw4.core.Scrabble;

import edu.cmu.cs.cs214.hw4.core.LetterTile.LetterTile;
import edu.cmu.cs.cs214.hw4.core.Player.Player;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.BoardTile;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.Location;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.ScrabbleBoard;
import edu.cmu.cs.cs214.hw4.core.ScrabbleComponents.BagOfTiles;
import edu.cmu.cs.cs214.hw4.core.ScrabbleComponents.Dictionary;
import edu.cmu.cs.cs214.hw4.core.ScrabbleComponents.Move;
import edu.cmu.cs.cs214.hw4.core.ScrabbleComponents.Placement;
import edu.cmu.cs.cs214.hw4.core.SpecialTile.Boom;
import edu.cmu.cs.cs214.hw4.core.SpecialTile.SpecialTile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implements a Scrabble game.
 * There is no interface extension because I didn't make an interface when
 * creating the actual implementation.
 */
public class ScrabbleImpl {
  /** The maximum number of players in scrabble. */
  public static final int MAX_PLAYERS = 4;

  /** The number of special tile types. */
  public static final int NUM_SPECIAL_TILES = 5;

  /** The bag of tiles to draw from. */
  private BagOfTiles theBag;
  /** The dictionary of Scrabble words. */
  private Dictionary dictionary;
  /** The board to play Scrabble on. */
  private ScrabbleBoard board;

  /** The overall game listener. */
  private GameChangeListener gameGui;
  /** The listeners who will be notified of changes in the game state. */
  private final List<PlayerChangeListener> playerChangeListeners =
          new ArrayList<>();

  /** The players playing Scrabble. */
  private final List<Player> players;
  /** The number of players in the game. */
  private int numPlayers;
  /** The ordering of players. */
  private List<Integer> ordering;

  /** True if tiles are being placed currently, false otherwise. */
  private boolean moveType;
  /** Contains the most recent placement of tiles. */
  private Move prevMove;
  /** The index of whose turn it is based on ordering. */
  private int turn;
  /** Whether or not the first turn has been done yet since there are different
   *  rules of placement. */
  private boolean firstTurn;
  /** The placements being done by current player. */
  private List<Placement> placements;
  /** The special tiles being placed by current player. */
  private List<SpecialTile> specialTileBeingPlaced;
  /** To remve consonants. */
  private boolean setRemoval;

  /**
   * Creates a new scrabble game.
   */
  public ScrabbleImpl() {
    players = new ArrayList<>();
  }

  /**
   * Starts a new game.
   * @param gui The overall game listener
   */
  public void makeNewGame(final GameChangeListener gui) {
    numPlayers = players.size();
    ordering = new ArrayList<>();

    theBag = new BagOfTiles();
    board = new ScrabbleBoard();
    dictionary = new Dictionary();
    gameGui = gui;

    for (int i = 0; i < numPlayers; i++) {
      players.get(i).clearHand();
      players.get(i).replaceTiles(Player.MAX_NUM_TILES, theBag);
      ordering.add(i);
    }
    Collections.shuffle(ordering);

    placements = new ArrayList<>();
    specialTileBeingPlaced = new ArrayList<>();
    prevMove = new Move();
    moveType = false;
    firstTurn = true;
    turn = 0;
  }

  /**
   * Starts the game.
   */
  public void startGame() {
    gameGui.startGame();
    currentListener().startTurn();
  }

  /**
   * Adds a new game listener.
   * @param listener the new listener
   */
  public void addGameChangeListener(final PlayerChangeListener listener) {
    playerChangeListeners.add(listener);
  }

  /**
   * Simulates whenever a player challenges another one over placed words
   * If it is succesful, return the tiles placed by the challenged player.
   * If it is not succesful, the challenger loses his/her next turn.
   * @param name the name of player challenging
   * @return whether the challenge was successful or not
   */
  public boolean challenge(final String name) {
    Player challenger = null;
    for (Player player : players) {
      if (name.equals(player.getName())) {
        challenger = player;
      }
    }

    boolean result = false;
    for (String word : prevMove.getWords()) {
      if (!dictionary.isWord(word)) {
        System.out.println(word);
        result = true;
        break;
      }
    }

    if (result) {
      board.removeTiles(prevMove.getTiles(), prevMove.getLocations());
      //board.removeSpecialTiles(specialTileBeingPlaced);

      LetterTile[] listOfTile = prevMove.getTiles();
      Location[] listOfLocation = prevMove.getLocations();
      for (int i = 0; i < listOfTile.length; i++) {
        addBackTile(listOfTile[i], listOfLocation[i]);
      }
      prevMove = new Move();

      currentListener().winChallenge();
    } else {
      currentListener().loseChallenge(name);
      challenger.loseNextTurn();
    }
    finishMove();
    return true;
  }

  /**
   * Sumulates a player buying a special tile.
   */
  public void buySpecialTile() {
    SpecialTile bought = currentPlayer().buySpecialTile();
    currentListener().boughtTile(bought);
  }

  /**
   * Simulates a player placing a letter tile at a certain location.
   * Can only happen when we haven't formally placed the tiles down.
   * @param tile to place
   * @param location of placement
   */
  public void placeLetterTile(final LetterTile tile, final Location location) {
    moveType = true;
    if (!prevMove.isPlaced()) {
      placements.add(new Placement(tile, location));

      BoardTile boardTile = board.getSquare(location.getX(), location.getY());
      if (boardTile.hasSpecial()) {
        prevMove.addSpecialTile(boardTile.getSpecial());
      }
      currentPlayer().loseTile(tile);

      PlayerChangeListener currentListener = currentListener();
      currentListener.placeLetterTileOnBoard(tile, location);
    }
  }

  /**
   * Returns a tile from the current queue of placements to players hand.
   * @param tile to return to the rack.
   * @param location where it was
   */
  public void addBackTile(final LetterTile tile, final Location location) {
    currentPlayer().addTile(tile);

    placements.remove(new Placement(tile, location));
    if (placements.size() == 0) {
      moveType = false;
    }

    BoardTile boardTile = board.getSquare(location.getX(), location.getY());
    if (boardTile.hasSpecial()) {
      prevMove.remSpecialTile(boardTile.getSpecial());
    }

    PlayerChangeListener currentListener = currentListener();
    currentListener.returnTileToRack(tile, location);
  }

  /**
   * Returns if a location is in the current queue of placements.
   * @param location to check
   * @return if a location is in the current queue of placements
   */
  public boolean isInQueue(final Location location) {
    for (Placement placement: placements) {
      if (location.equals(placement.getLoc())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Simulates a player placing down tiles at certain locations.
   * REQUIRES: |placements| > 0
   * @return whether the placement is valid or not
   */
  public boolean placeTiles() {
    int n = placements.size();
    Collections.sort(placements);

    LetterTile[] listOfTiles = new LetterTile[n];
    Location[] listOfLocation = new Location[n];
    for (int i = 0; i < n; i++) {
      listOfTiles[i] = placements.get(i).getTile();
      listOfLocation[i] = placements.get(i).getLoc();
    }

    if (firstTurn && listOfTiles.length == 1) {
      return false;
    }

    List<BoardTile[]> words;
    words = board.placeTiles(listOfTiles, listOfLocation, firstTurn);
    if (words != null) {
      prevMove.setPlacements(listOfTiles, listOfLocation, words);
      currentListener().placeWords(prevMove.getWords());

      return true;
    }
    return false;
  }

  /**
   * Simulates a player swapping of tiles for new ones out of the bag.
   * REQUIRES: !moveType
   * @param letters to switch
   */
  public void swapTiles(final LetterTile[] letters) {
    PlayerChangeListener currentListener = currentListener();
    if (moveType) {
      currentListener.swapTiles(null, null, true);
      return;
    }

    Player player = players.get(ordering.get(turn));
    LetterTile[] newTiles = player.swapTiles(letters, theBag);
    currentListener.swapTiles(letters, newTiles, true);

    getToNextPlayer();
    gameGui.nextPlayerGui(true);
  }

  /**
   * Simulates a player attempting to place a special tile on the board.
   * Special tiles cannot be placed on other special tiles or on a double word/
   * triple word/etc.
   * @param tile special tile to be placed
   * @param loc location of where the player wants to place it
   */
  public void placeSpecialTile(final SpecialTile tile, final Location loc) {
    boolean successful = board.placeSpecialTile(tile, loc);
    System.out.println(successful);
    if (successful) {
      tile.setLocation(loc);
      specialTileBeingPlaced.add(tile);

      board.placeSpecialTile(tile, loc);
      currentPlayer().loseSpecialTile(tile);
    }
    currentListener().placeSpecialTile(tile, successful);
  }

  /**
   * Passes the player's turn.
   */
  public void passMove() {
    if (moveType) {
      gameGui.nextPlayerGui(false);
      return;
    }

    //Turn goes to the next player whose turn isn't skipped.
    getToNextPlayer();
    gameGui.nextPlayerGui(true);
  }

  /**
   * Finishes a move a player has done after a challenge is unsuccesful or
   * there are no challenges.
   * All special tiles are activated and the player is awarded appropriate
   * number of points. The turn counter updates to the next player.
   */
  public void finishMove() {
    PlayerChangeListener playerListener = currentListener();
    Player player = currentPlayer();

    if (moveType) {
      setRemoval = false;
      List<SpecialTile> specialTiles = prevMove.getSpecialTiles();
      for (SpecialTile t : specialTiles) {
        t.activate(this);
      }

      LetterTile[] oldTiles = prevMove.getTiles();
      LetterTile[] newTiles =
              player.replaceTiles(oldTiles.length, theBag);
      player.addPoints(prevMove.getScore());

      if (setRemoval) {
        removeConsonants();
      }
      for (SpecialTile t : specialTiles) {
        t.activate(this);

        Location loc = t.getLocation();
        board.getSquare(loc.getX(), loc.getY()).removeSpecialTile();
      }

      //This means that we've run out of scrabble tiles -> game ends
      if (newTiles.length < oldTiles.length) {
        gameGui.endGame();
        return;
      }

      playerListener.swapTiles(prevMove.getTiles(), newTiles, false);
      board.deactivateTiles(prevMove.getLocations());
      firstTurn = false;
      moveType = false;

      currentListener().finishTurn();
    }
    prevMove = new Move();

    placements = new ArrayList<>();
    specialTileBeingPlaced = new ArrayList<>();
    getToNextPlayer();

    gameGui.nextPlayerGui(true);
    currentListener().startTurn();
  }

  /**
   * Updates turn to the next unskipped player.
   */
  private void getToNextPlayer() {
    turn = (turn + 1) % numPlayers;
    Player nextPlayer = players.get(ordering.get(turn));
    while (nextPlayer.isTurnSkipped()) {
      currentListener().skipTurn();
      nextPlayer.skipTurn();

      turn = (turn + 1) % numPlayers;
      nextPlayer = players.get(ordering.get(turn));
    }
  }

  /**
   * Returns the index of a player based on the random ordering.
   * @param player to find index
   * @return index of the player
   */
  public int indexOf(final Player player) {
    int index = players.indexOf(player);
    return ordering.get(index);
  }

  /**
   * Reverses the order of the players.
   */
  public void reverseOrder() {
    Collections.reverse(ordering);
    turn = numPlayers - 1 - turn;

    gameGui.reverseOrdering();
  }

  /**
   * Skips the move of the next player whose move is not skipped.
   * WARNING: Could be yourself!!!
   */
  public void skipNextPlayer() {
    int counter = 1;
    int currentIndex = ordering.get((turn + counter) % numPlayers);
    while (players.get(currentIndex).isTurnSkipped()) {
      counter++;
      currentIndex = ordering.get((turn + counter) % numPlayers);
    }
    players.get(currentIndex).loseNextTurn();
  }

  /**
   * Removes all letter tiles in 3 radius of the center.
   * @param center of the explosion
   */
  public void boom(final Location center) {
    int rad = Boom.BLAST_RADIUS;
    for (int i = -rad; i <= rad; i++) {
      for (int j = -(rad - Math.abs(i)); j <= rad - Math.abs(i); j++) {
        int x = i + center.getX();
        int y = j + center.getY();
        if (x >= 0 && x < board.BOARD_SIZE && y >= 0 && y < board.BOARD_SIZE) {
          System.out.println(x + " " + y);
          board.destroyTile(new Location(x, y));
        }
      }
    }
  }

  /**
   * Makes the player lose points.
   */
  public void makePointsNegative() {
    //Already handled by Move score calculation.
  }

  /**
   * Makes the player lose points.
   */
  public void setConsonantRemoval() {
    setRemoval = true;
  }

  /**
   * Removes all consonants tiles on the board.
   */
  public void removeConsonants() {
    for (int row = 0; row < board.BOARD_SIZE; row++) {
      for (int column = 0; column < board.BOARD_SIZE; column++) {
        if (board.getSquare(row, column).hasConsonant()) {
          board.getSquare(row, column).removeLetterTile();
        }
      }
    }
  }

  /**
   * Gets the winner.
   * @return winning player
   */
  public Player getWinner() {
    int maxScore = Integer.MIN_VALUE;
    Player winner = null;
    for (Player player : players) {
      if (player.getScore() > maxScore) {
        winner = player;
        maxScore = player.getScore();
      }
    }
    return winner;
  }

  /**
   * Returns the Scrabble board.
   * @return Scrabble board
   */
  public ScrabbleBoard getBoard() {
    return board;
  }

  /**
   * Returns current player.
   * @return current player
   */
  public Player currentPlayer() {
    return players.get(ordering.get(turn));
  }

  /**
   * Returns current player.
   * @return current player
   */
  public PlayerChangeListener currentListener() {
    return playerChangeListeners.get(ordering.get(turn));
  }

  /**
   * Adds a new player to the game.
   * @param player to add
   */
  public void addPlayer(final Player player) {
    players.add(player);
  }

  /**
   * returns string representation of game (just the board).
   * @return string representation
   */
  @Override
  public String toString() {
    return board.toString();
  }

  /**
   * Returns number of tiles left.
   * @return number of tiles left
   */
  public int numberOfTilesLeft() {
    return theBag.numberOfTiles();
  }
}
