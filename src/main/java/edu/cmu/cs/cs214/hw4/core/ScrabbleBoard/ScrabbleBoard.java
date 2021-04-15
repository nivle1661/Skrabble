package edu.cmu.cs.cs214.hw4.core.ScrabbleBoard;

import edu.cmu.cs.cs214.hw4.core.LetterTile.LetterTile;
import edu.cmu.cs.cs214.hw4.core.PremiumTile.MultLetter;
import edu.cmu.cs.cs214.hw4.core.PremiumTile.MultWord;
import edu.cmu.cs.cs214.hw4.core.SpecialTile.SpecialTile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents the board of a Scrabble game.
 */
public class ScrabbleBoard {
  /**
   * Represents one of the two directions that tiles can be placed, namely
   * vertical or horizontal.
   */
  private enum Direction {
    /**
     * UPDOWN is vertical, LEFTRIGHT is horizontal.
     */
    UPDOWN, LEFTRIGHT
  }

  /** The orientation of the tiles of the most recent tile placement of
   * the board. */
  private Direction dir;

  /** The index of the center tile. */
  private static final int CENTER_INDEX = 7;

  /** The size of the Scrabble board. */
  public static final int BOARD_SIZE = 15;

  /** The 2D grid of board tiles that make up the Scrabble board. */
  private BoardTile[][] board;

  /** Initializes the board tiles containing triple word. */
  private void initializeTripleWord() {
    String dicPath = "tripleword.txt";
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream in = classLoader.getResourceAsStream(dicPath);
    List<Location> locations = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = reader.readLine()) != null) {
        Scanner temp = new Scanner(line);
        int x = temp.nextInt();
        int y = temp.nextInt();
        locations.add(new Location(x, y));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (Location loc : locations) {
      board[loc.getX()][loc.getY()] =
              new BoardTile(new MultWord(MultWord.THREE));
    }
  }

  /** Initializes the board tiles containing double word. */
  private void initializeDoubleWord() {
    String dicPath = "doubleword.txt";
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream in = classLoader.getResourceAsStream(dicPath);
    List<Location> locations = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = reader.readLine()) != null) {
        Scanner temp = new Scanner(line);
        int x = temp.nextInt();
        int y = temp.nextInt();
        locations.add(new Location(x, y));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (Location loc : locations) {
      board[loc.getX()][loc.getY()] = new BoardTile(new MultWord(MultWord.TWO));
    }
  }

  /** Initializes all board tiles containing triple letter. */
  private void initializeTripleLetter() {
    String dicPath = "tripleletter.txt";
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream in = classLoader.getResourceAsStream(dicPath);
    List<Location> locations = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = reader.readLine()) != null) {
        Scanner temp = new Scanner(line);
        int x = temp.nextInt();
        int y = temp.nextInt();
        locations.add(new Location(x, y));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (Location loc : locations) {
      board[loc.getX()][loc.getY()] =
              new BoardTile(new MultLetter(MultLetter.THREE));
    }
  }

  /** Initializes all board tiles containing double letter. */
  private void initializeDoubleLetter() {
    String dicPath = "doubleletter.txt";
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream in = classLoader.getResourceAsStream(dicPath);
    List<Location> locations = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = reader.readLine()) != null) {
        Scanner temp = new Scanner(line);
        int x = temp.nextInt();
        int y = temp.nextInt();
        locations.add(new Location(x, y));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (Location loc : locations) {
      board[loc.getX()][loc.getY()] =
              new BoardTile(new MultLetter(MultLetter.TWO));
    }
  }

  /** Initializes the board with multiplier effects. */
  public ScrabbleBoard() {
    board = new BoardTile[BOARD_SIZE][BOARD_SIZE];
    initializeDoubleLetter();
    initializeDoubleWord();
    initializeTripleLetter();
    initializeTripleWord();

    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        if (board[i][j] == null) {
          board[i][j] = new BoardTile();
        }
      }
    }
  }

  /**
   * Returns whether the location has an adjacent letter tile on the board.
   * @param loc location to check
   * @return whether the location has an adjacent letter tile on the board
   */
  private boolean hasAdj(final Location loc) {
    return ((loc.getX() != 0 && board[loc.getX() - 1][loc.getY()].isOccupied())
            || (loc.getY() != 0
                      && board[loc.getX()][loc.getY() - 1].isOccupied())
            || (loc.getX() != BOARD_SIZE - 1
                      && board[loc.getX() + 1][loc.getY()].isOccupied())
            || (loc.getY() != BOARD_SIZE - 1
                      && board[loc.getX()][loc.getY() + 1].isOccupied()));
  }

  /**
   * Checks if the locations are all valid locations.
   * @param listOfLocation locations of letter tiles
   * @param firstMove if this is first move
   * @return whether the locations are valid
   */
  private boolean validLocations(final Location[] listOfLocation,
                                 final boolean firstMove) {
    dir = Direction.UPDOWN;
    boolean isValid = false;
    int coordCheck = 0;

    //Check if all valid locations and at least one is adjacent to a letter
    for (int i = 0; i < listOfLocation.length; i++) {
      if (board[listOfLocation[i].getX()][listOfLocation[i].getY()].
              isOccupied()) {
        return false;
      }
      if ((!firstMove && hasAdj(listOfLocation[i]))
          || (firstMove && listOfLocation[i].getX() == CENTER_INDEX
                        && listOfLocation[i].getY() == CENTER_INDEX)) {
        isValid = true;
      }
    }
    if (!isValid) {
      return false;
    }

    //Determine the direction of the tiles
    if (listOfLocation.length > 1) {
      if (listOfLocation[0].getX() == listOfLocation[1].getX()) {
        dir = Direction.LEFTRIGHT;
        coordCheck = listOfLocation[0].getX();
      } else if (listOfLocation[0].getY() == listOfLocation[1].getY()) {
        dir = Direction.UPDOWN;
        coordCheck = listOfLocation[0].getY();
      } else {
        return false;
      }
    }

    for (int i = 1; i < listOfLocation.length; i++) {
    //Check if the tiles don't lie on the same line
      if ((dir == Direction.UPDOWN && listOfLocation[i].getY() != coordCheck)
          || (dir == Direction.LEFTRIGHT
              && listOfLocation[i].getX() != coordCheck)) {
        return false;
      } else {
        //Check if any gaps between tiles are occupied
        if (dir == Direction.UPDOWN) {
          for (int j = listOfLocation[i - 1].getX() + 1;
               j < listOfLocation[i].getX(); j++) {
            if (!board[j][coordCheck].isOccupied()) {
              return false;
            }
          }
        } else {
          for (int j = listOfLocation[i - 1].getY() + 1;
               j < listOfLocation[i].getY(); j++) {
            if (!board[coordCheck][j].isOccupied()) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  /**
   * Returns the list of board tiles that form a continuous string along a
   * direction with the tile at a given location.
   * @param loc location containing required tile
   * @param dirL direction of returned board tiles
   * @return contingent string containing loc along direction dir
   */
  public BoardTile[] findString(final Location loc, final Direction dirL) {
    BoardTile[] result = null;
    if (dirL == Direction.LEFTRIGHT) {
      int x = loc.getX();
      int leftBound = loc.getY();
      int rightBound = loc.getY();
      while (leftBound > 0 && board[x][leftBound - 1].isOccupied()) {
        leftBound--;
      }
      while (rightBound < BOARD_SIZE - 1
             && board[x][rightBound + 1].isOccupied()) {
        rightBound++;
      }
      result = new BoardTile[rightBound - leftBound + 1];
      for (int i = leftBound; i <= rightBound; i++) {
        result[i - leftBound] = board[x][i];
      }
    } else {
      int y = loc.getY();
      int upBound = loc.getX();
      int downBound = loc.getX();
      while (upBound > 0 && board[upBound - 1][y].isOccupied()) {
        upBound--;
      }
      while (downBound < BOARD_SIZE - 1
             && board[downBound + 1][y].isOccupied()) {
        downBound++;
      }
      result = new BoardTile[downBound - upBound + 1];
      for (int i = upBound; i <= downBound; i++) {
        result[i - upBound] = board[i][y];
      }
    }
    return result;
  }

  /**
   * Returns the list of words formed by the tiles being placed and the
   * letter tiles already placed on the board. If not a valid configuration,
   * returns null.
   *
   * @param listOfTiles letter tiles being placed
   * @param listOfLocation locations of letter tiles, must be sorted
   * @param firstMove if it's the first move
   * @return list of words formed
   */
  public List<BoardTile[]> placeTiles(final LetterTile[] listOfTiles,
                                      final Location[] listOfLocation,
                                      final boolean firstMove) {
    if (!validLocations(listOfLocation, firstMove)) {
      return null;
    }
    List<BoardTile[]> result = new ArrayList<>();
    int numTiles = listOfLocation.length;

    //Place the tiles on the board
    for (int i = 0; i < numTiles; i++) {
      board[listOfLocation[i].getX()][listOfLocation[i].getY()].
              placeLetterTile(listOfTiles[i]);
    }

    // Now find what new words are formed and insert them in result.
    // We know that only one word can be formed along original direction,
    // and the rest must be formed from new letters along other direction.
    BoardTile[] initWord = findString(listOfLocation[0], dir);
    result.add(initWord);
    if (dir == Direction.LEFTRIGHT) {
      for (int i = 0; i < numTiles; i++) {
        BoardTile[] temp = findString(listOfLocation[i], Direction.UPDOWN);
        if (temp.length > 1) {
          result.add(temp);
        }
      }
    } else {
      for (int i = 0; i < numTiles; i++) {
        BoardTile[] temp = findString(listOfLocation[i], Direction.LEFTRIGHT);
        if (temp.length > 1) {
          result.add(temp);
        }
      }
    }
    return result;
  }

  /**
   * Removes the list of letters at the locations specified from the board.
   * @param listOfTiles letter tiles being removed
   * @param listOfLocation locations of letter tiles
   */
  public void removeTiles(final LetterTile[] listOfTiles,
                          final Location[] listOfLocation) {
    for (int i = 0; i < listOfTiles.length; i++) {
      board[listOfLocation[i].getX()][listOfLocation[i].getY()].
              removeLetterTile();
    }
  }

  /**
   * Turns off the double word/triple word/double letter/triple letter effects
   * in the locations specified.
   * @param listOfLocation locations to deactivate
   */
  public void deactivateTiles(final Location[] listOfLocation) {
    for (int i = 0; i < listOfLocation.length; i++) {
      BoardTile temp = board[listOfLocation[i].getX()]
                            [listOfLocation[i].getY()];
      if (temp.isOccupied()) {
        temp.turnOffEffect();
      }
    }
  }

  /**
   * Removes the letter tile in a location if there is one.
   * @param loc of destruction
   */
  public void destroyTile(final Location loc) {
    board[loc.getX()][loc.getY()].removeLetterTile();
  }

  /**
   * Attempts to place a special tile on the board. If the location is already
   * occupied, has a special tile, or already as an effect, the special tile
   * cannot be placed.
   * @param tile special tile to be placed
   * @param loc location of special tile
   * @return whether or not the placement was succesful
   */
  public boolean placeSpecialTile(final SpecialTile tile, final Location loc) {
    if (board[loc.getX()][loc.getY()].isOccupied()
        || board[loc.getX()][loc.getY()].hasSpecial()
        || board[loc.getX()][loc.getY()].hasMultWord()
        || board[loc.getX()][loc.getY()].hasMultLetter()) {
      return false;
    }
    board[loc.getX()][loc.getY()].placeSpecialTile(tile, loc);
    return true;
  }

  /**
   * Returns the board tile at a certain location.
   * @param row of location
   * @param col of location
   * @return board tile at certain location
   */
  public BoardTile getSquare(final int row, final int col) {
    return board[row][col];
  }

  /**
   * Returns the string representation of the board.
   * @return string representation of the board
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        result.append(board[i][j]).append(" ");
      }
      result.append("\n");
    }
    return result.toString();
  }
}
