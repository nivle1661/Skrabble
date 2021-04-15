package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.LetterTile.LetterTile;
import edu.cmu.cs.cs214.hw4.core.Player.Player;
import edu.cmu.cs.cs214.hw4.core.Scrabble.PlayerChangeListener;
import edu.cmu.cs.cs214.hw4.core.Scrabble.ScrabbleImpl;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.BoardTile;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.Location;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.ScrabbleBoard;
import edu.cmu.cs.cs214.hw4.core.SpecialTile.SpecialTile;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.BoardTile.SPECIAL_COLOR;
import static edu.cmu.cs.cs214.hw4.gui.ScrabbleGameGui.BOARD_SIZE;
import static edu.cmu.cs.cs214.hw4.gui.ScrabbleGameGui.TEXT_HEIGHT;
import static edu.cmu.cs.cs214.hw4.gui.ScrabbleGameGui.TEXT_WIDTH;
import static edu.cmu.cs.cs214.hw4.gui.ScrabbleGameGui.TURN_HEADER;

/**
 * This will be the GUI which a player interacts with when playing Scrabble.
 */
public class ScrabbleGamePlayerGui extends JPanel implements
        PlayerChangeListener {
  /** Text size of board tiles. */
  public static final int LETTER_TEXT_SIZE = 20;

  /** Letter tile dimension. */
  public static final int LETTER_SIZE = 60;

  /** Letter tile dimension. */
  public static final int MOVE_HEIGHT = 70;

  /** Letter tile dimension. */
  public static final int MOVE_WIDTH = 140;

  /** Number of move types. */
  public static final int NUM_MOVES = 3;

  /** Score panel that goes on top of the board. */
  private JPanel header;

  /** The panel containing the Scrabble board. */
  private JPanel gridPanel;

  /** The panel containing the letter tiles of the player. */
  private final JPanel rack;

  /** The panel containing special tile count. */
  private final JPanel count;

  /** The panel containing the game info. */
  private JPanel gameInfo;

  /** The panel containing the move options. */
  private final JPanel moves;

  /** The challenge button. */
  private JButton challenge;

  /** The JButtons that contain the move options. */
  private JButton[] moveButtons = new JButton[NUM_MOVES];

  /** The grid representing the Scrabble board. */
  private JButton[][] theGrid = new JButton[ScrabbleBoard.BOARD_SIZE]
          [ScrabbleBoard.BOARD_SIZE];
  /** The JButtons that contain the letter tiles. */
  private JButton[] theRack = new JButton[Player.MAX_NUM_TILES];
  /** The JPanels wrapping the JButtons. */
  private JPanel[] theRackP = new JPanel[Player.MAX_NUM_TILES];
  /** The JLabels that contain special tile count. */
  private JLabel[] theCount = new JLabel[ScrabbleImpl.NUM_SPECIAL_TILES];

  /** The label containing the current player's score. */
  private JLabel score;
  /** The label containing the current player's turn. */
  private JLabel turn;

  /** The current game implementation being played. */
  private ScrabbleImpl scrabbleGame;
  /** The scrabble board implementation. */
  private ScrabbleBoard scrabbleBoard;

  /** The current player this GUI is for. */
  private Player player;
  /** Other players. */
  private String[] otherPlayers;

  /**
   * Constructs the GUI for a specific player based on scrabble board game.
   * @param game of scrabble
   * @param playerL player
   * @param allPlayers all player names in the game
   */
  public ScrabbleGamePlayerGui(final ScrabbleImpl game,
                               final Player playerL,
                               final List<String> allPlayers) {
    scrabbleGame = game;
    scrabbleBoard = game.getBoard();
    player = playerL;

    allPlayers.remove(playerL.getName());
    otherPlayers = allPlayers.toArray(new String[allPlayers.size()]);

    setLayout(new BorderLayout());
    header = new JPanel();
    header.setPreferredSize(new Dimension(BOARD_SIZE, TURN_HEADER));
    add(header, BorderLayout.NORTH);

    gridPanel = new JPanel();
    gridPanel.setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
    add(gridPanel, BorderLayout.CENTER);

    rack = initRack();
    count = initCount();
    add(rack, BorderLayout.SOUTH);

    moves = initMoves();
    add(moves, BorderLayout.WEST);

    gameInfo = new JPanel();
    gameInfo.add(count);
    gameInfo.setPreferredSize(new Dimension(TEXT_WIDTH, BOARD_SIZE));
    JButton buySpecialTile = new JButton();
    buySpecialTile.addActionListener(e -> {
      game.buySpecialTile();
    });
    buySpecialTile.setText("Buy special tile");
    gameInfo.add(buySpecialTile);

    JButton howManyTilesLeft = new JButton();
    JPanel container = this;
    howManyTilesLeft.addActionListener(e -> {
      JOptionPane.showMessageDialog(container,
              "There are " + game.numberOfTilesLeft() + " tiles remaining",
              "Tiles left",
              JOptionPane.INFORMATION_MESSAGE);
    });
    howManyTilesLeft.setText("How many tiles left?");
    gameInfo.add(howManyTilesLeft);
    gameInfo.add(Box.createRigidArea(new Dimension(TEXT_WIDTH,
            TEXT_HEIGHT)));
    add(gameInfo, BorderLayout.EAST);

    setVisible(true);
  }

  /**
   * Sets the grid parameter containing the scrabble board.
   * @param center JPanel wrapper
   * @param grid The grid containing the scrabble board
   */
  public void setGrid(final JPanel center, final JButton[][] grid) {
    gridPanel.add(center);
    theGrid = grid;
  }

  /**
   * Sets the score for the current player.
   * @param right JPanel wrapper
   * @param scoreL score label for the current player
   */
  public void setScore(final JPanel right, final JLabel scoreL) {

    gameInfo.add(right);
    score = scoreL;

    //Special case!
    updateScore();
  }

  /**
   * Sets the turn label for the current player.
   * @param top JPanel wrapper
   * @param turnL turn label for the current player
   */
  public void setTurn(final JPanel top, final JLabel turnL) {
    header.add(top);
    turn = turnL;
  }

  /**
   * Initializes the current player rack.
   * @return player rack
   */
  private JPanel initRack() {
    JPanel rackL = new JPanel();
    rackL.setLayout(new FlowLayout());
    rackL.setPreferredSize(new Dimension(LETTER_SIZE * Player.MAX_NUM_TILES,
            LETTER_SIZE));

    LetterTile[] hand = player.getLetters();
    for (int column = 0; column < hand.length; column++) {
      LetterTile temp = hand[column];
      JPanel panel = new JPanel(new BorderLayout());
      JButton button = new JButton(temp.toString());
      button.setBackground(BoardTile.LETTER_COLOR);
      button.setFont(new java.awt.Font("Century Schoolbook L",
              Font.BOLD, LETTER_TEXT_SIZE));
      button.setPreferredSize(new Dimension(LETTER_SIZE, LETTER_SIZE));

      panel.setPreferredSize(new Dimension(LETTER_SIZE, LETTER_SIZE));
      panel.add(button, BorderLayout.CENTER);

      rackL.add(panel);
      theRack[column] = button;
      theRackP[column] = panel;
    }
    return rackL;
  }

  /**
   * Initiates the special tile counter.
   * @return panel containing counter
   */
  private JPanel initCount() {
    JPanel result = new JPanel();
    GridLayout layout = new GridLayout(theCount.length, 2);
    layout.setHgap(LETTER_TEXT_SIZE / 2);
    result.setLayout(layout);

    for (SpecialTile key : player.getSpecialTileIntegerMap().keySet()) {
      JLabel type = new JLabel(key.toString());
      JLabel counter = new JLabel("0");
      theCount[key.hashCode()] = counter;
      result.add(type);
      result.add(counter);
    }
    return result;
  }

  /**
   * Initializes the player move input panel.
   * @return move options.
   */
  private JPanel initMoves() {
    JPanel moveL = new JPanel();
    moveL.setLayout(new FlowLayout());
    moveL.setPreferredSize(new Dimension(MOVE_WIDTH, MOVE_HEIGHT * NUM_MOVES));

    JButton placeTiles = new JButton("Place tiles");
    JButton finishMove = new JButton("Finish move");
    finishMove.setPreferredSize(new Dimension(MOVE_WIDTH, MOVE_HEIGHT));
    finishMove.addActionListener(e -> {
      finishMove.setVisible(false);
      challenge.setVisible(false);
      placeTiles.setVisible(true);
      scrabbleGame.finishMove();
    });

    placeTiles.setPreferredSize(new Dimension(MOVE_WIDTH, MOVE_HEIGHT));
    placeTiles.addActionListener(e -> {
      boolean legal = scrabbleGame.placeTiles();
      if (!legal) {
        showErrorDialog(this, "Not valid", "Invalid placement of tiles");
        return;
      }
      placeTiles.setVisible(false);
      finishMove.setVisible(true);
    });
    finishMove.setVisible(false);
    placeTiles.setVisible(true);

    JButton swapTiles = new JButton("Swap tiles");
    swapTiles.setPreferredSize(new Dimension(MOVE_WIDTH, MOVE_HEIGHT));
    swapTiles.addActionListener(e -> {
      LetterTile[] hand = player.getLetters();
      String[] selectionValues = new String[hand.length];
      for (int i = 0; i < hand.length; i++) {
        selectionValues[i] = hand[i].toString();
      }

      JPanel al = new JPanel();
      JCheckBox[] checkBoxes = new JCheckBox[hand.length];
      for (int i = 0; i < hand.length; i++) {
        String tile = hand[i].toString();
        JCheckBox box = new JCheckBox(tile);
        checkBoxes[i] = box;
        al.add(box);
      }
      int answer = JOptionPane.showConfirmDialog(null, al,
                  "Select tiles to swap",
                   JOptionPane.OK_CANCEL_OPTION);
      if (answer == JOptionPane.OK_OPTION) {
        List<LetterTile> toSwap = new ArrayList<>();
        for (int i = 0; i < hand.length; i++) {
          if (checkBoxes[i].isSelected()) {
            toSwap.add(hand[i]);
          }
        }
        scrabbleGame.swapTiles(toSwap.toArray(new LetterTile[toSwap.size()]));
      }
    });

    JButton passMove = new JButton("Pass move");
    passMove.setPreferredSize(new Dimension(MOVE_WIDTH, MOVE_HEIGHT));
    passMove.addActionListener(e -> {
      scrabbleGame.passMove();
    });

    challenge = new JButton("Challenge!");
    challenge.addActionListener(e -> {
      String name = (String) JOptionPane.showInputDialog(this,
              "Who is challenging " + player.getName() + "?",
              "Challenge in progress...",
              JOptionPane.QUESTION_MESSAGE, null, otherPlayers,
              otherPlayers[0]);

      if (name != null) {
        finishMove.setVisible(false);
        placeTiles.setVisible(true);
        scrabbleGame.challenge(name);
      }
    });
    challenge.setVisible(false);

    moveL.add(placeTiles);
    moveL.add(finishMove);
    moveL.add(swapTiles);
    moveL.add(passMove);
    moveL.add(challenge);

    return moveL;
  }

  /**
   * Update turn header.
   */
  private void updateHeader() {
    turn.setText(player.getName() + "'s turn");
  }

  /**
   * Updates the score.
   */
  private void updateScore() {
    String skipped = "";
    if (player.isTurnSkipped()) {
      skipped = " (SKIPPED)";
    }
    score.setText(player.getName() + skipped + ": " + player.getScore());
  }

  /**
   * Update button action listeners of the Scrabble board.
   */
  private void updateGrid() {
    LetterTile[] hand = player.getLetters();
    String[] letterValues = new String[hand.length];
    for (int i = 0; i < hand.length; i++) {
      letterValues[i] = hand[i].toString();
    }

    Map<SpecialTile, Integer> mapping = player.getSpecialTileIntegerMap();
    List<String> specialValuesL = new ArrayList<>();
    for (Map.Entry<SpecialTile, Integer> entry : mapping.entrySet()) {
      if (entry.getValue() > 0) {
        specialValuesL.add(entry.getKey().toString());
      }
    }
    String[] specialValues =
            specialValuesL.toArray(new String[specialValuesL.size()]);

    for (int row = 0; row < ScrabbleBoard.BOARD_SIZE; row++) {
      for (int column = 0; column < ScrabbleBoard.BOARD_SIZE; column++) {
        JButton button = theGrid[row][column];
        int finalColumn = column;
        int finalRow = row;

        BoardTile boardTile = scrabbleBoard.getSquare(row, column);
        //If there is a letter tile on the button, don't change the listener.
        if (scrabbleGame.isInQueue(new Location(row, column))
                || boardTile.isOccupied()) {
          continue;
        }

        //Edit the color/text if needed.
        if (boardTile.hasSpecial()
                && player == boardTile.getSpecial().getPlayer()) {
            button.setText(boardTile.getSpecial().toString());
            button.setBackground(SPECIAL_COLOR);
        } else {
            //Just in case if things get blown up and we have to remove tiles.
            button.setText(boardTile.toString());
            button.setBackground(boardTile.getColor());
        }

        if (button.getActionListeners().length > 0) {
          button.removeActionListener(button.getActionListeners()[0]);
        }
        button.addActionListener(e -> {
          String[] typeValues = {"Letter tile", "Special tile"};
          String name = (String) JOptionPane.showInputDialog(this,
                  "Select type of tile to place", "Placing tile...",
                  JOptionPane.QUESTION_MESSAGE, null, typeValues,
                  typeValues[0]);
          if (name != null) {
            //Player chooses to place letter tile.
            if (name.equals("Letter tile")) {
              if (letterValues.length == 0) {
                showErrorDialog(this, "No tiles!", "You have no more letter "
                        + "tiles to place");
                return;
              }

              // Show an input dialog. This method blocks until the dialog is
              // dismissed.
              name = (String) JOptionPane.showInputDialog(this,
                      "Select a letter tile to place", "Placing tile...",
                      JOptionPane.QUESTION_MESSAGE, null, letterValues,
                      letterValues[0]);
              if (name != null) {
                for (LetterTile tile : hand) {
                  if (name.equals(tile.toString())) {
                    scrabbleGame.placeLetterTile(tile, new Location(finalRow,
                            finalColumn));
                    return;
                  }
                }
              }
              return;
            }

            //Player chooses to place special tile.
            if (specialValues.length == 0) {
              showErrorDialog(this, "No tiles!", "You have no special tiles "
                      + "to place");
              return;
            }

            // Show an input dialog. This method blocks until the dialog is
            // dismissed.
            name = (String) JOptionPane.showInputDialog(this,
                    "Select a special tile to place", "Placing tile...",
                    JOptionPane.QUESTION_MESSAGE, null, specialValues,
                    specialValues[0]);
            if (name != null) {
              for (SpecialTile tile : player.getSpecialTiles()) {
                if (name.equals(tile.toString())) {
                  scrabbleGame.placeSpecialTile(tile, new Location(finalRow,
                          finalColumn));
                  return;
                }
              }
            }
          }
        });
      }
    }
  }

  /**
   * Returns the index of the JButton containing the letter tile, given that
   * it should be visible or not.
   * @param tile to search for
   * @param isVisible if it is visible
   * @return index of JButton containing the letter tile
   */
  private int getRackIndex(final LetterTile tile, final boolean isVisible) {
    for (int i = 0; i < Player.MAX_NUM_TILES; i++) {
      JButton button = theRack[i];
      JPanel panel = theRackP[i];
      if (tile.toString().equals(button.getText())
              && (isVisible == panel.isVisible())) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Starts a new turn for the current player.
   */
  @Override
  public void startTurn() {
    updateGrid();
    updateHeader();
  }

  /**
   * Finishes a turn for the current player.
   */
  @Override
  public void finishTurn() {
    updateScore();
  }

  /**
   * Places a letter tile on the GUI by the current player.
   * @param tile to be placed
   * @param loc location
   */
  @Override
  public void placeLetterTileOnBoard(final LetterTile tile,
                                     final Location loc) {
    updateGrid();

    JButton button = theGrid[loc.getX()][loc.getY()];
    button.setText(tile.toString());
    button.removeActionListener(button.getActionListeners()[0]);
    button.addActionListener(e -> {
      scrabbleGame.addBackTile(tile, loc);
    });
    button.setBackground(BoardTile.LETTER_COLOR);

    JPanel panel = theRackP[getRackIndex(tile, true)];
    panel.setVisible(false);
    System.out.println(player);
  }

  /**
   * Adds a tile back into the rack.
   * @param tile to be removed
   * @param loc location
   */
  @Override
  public void returnTileToRack(final LetterTile tile, final Location loc) {
    updateGrid();

    JButton button = theGrid[loc.getX()][loc.getY()];
    button.setText(scrabbleBoard.getSquare(loc.getX(), loc.getY()).toString());
    button.setBackground(scrabbleBoard.getSquare(loc.getX(),
            loc.getY()).getColor());

    JPanel panel = theRackP[getRackIndex(tile, false)];
    panel.setVisible(true);
    System.out.println(player);
  }

  /**
   * Swaps tiles from the current player.
   * @param oldTiles to swap out
   * @param newTiles to put in
   * @param isVisible if the old tiles should be visible or not
   */
  @Override
  public void swapTiles(final LetterTile[] oldTiles,
                        final LetterTile[] newTiles,
                        final boolean isVisible) {
    if (oldTiles == null) {
      showErrorDialog(this, "Error!", "You can't swap out tiles "
              + "while placing down tiles on the board.");
      return;
    }
    updateGrid();

    StringBuilder topMessage = new StringBuilder("Old tiles:");
    StringBuilder bottomMessage = new StringBuilder("New tiles:");
    for (int i = 0; i < oldTiles.length; i++) {
      int index = getRackIndex(oldTiles[i], isVisible);
      JButton button = theRack[index];
      button.setText(newTiles[i].toString());

      JPanel panel = theRackP[index];
      panel.setVisible(true);

      topMessage.append(" ").append(oldTiles[i].toString());
      bottomMessage.append(" ").append(newTiles[i].toString());
    }

    JOptionPane.showMessageDialog(this,
            topMessage.toString() + "\n" + bottomMessage.toString(),
            "Swapped tiles",
            JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Places multiple tiles on the GUI.
   * @param words to be placed
   */
  @Override
  public void placeWords(final List<String> words) {
    challenge.setVisible(true);

    //Take no more input.
    for (int row = 0; row < ScrabbleBoard.BOARD_SIZE; row++) {
      for (int column = 0; column < ScrabbleBoard.BOARD_SIZE; column++) {
        JButton button = theGrid[row][column];
        if (button.getActionListeners().length > 0) {
          button.removeActionListener(button.getActionListeners()[0]);
        }
      }
    }
  }

  /**
   * Skips the player's turn.
   */
  @Override
  public void skipTurn() {
    JOptionPane.showMessageDialog(this, "Your turn is skipped",
            "Skipped", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Lose challenge.
   * @name of challenger
   */
  @Override
  public void loseChallenge(final String name) {
    JOptionPane.showMessageDialog(this, name + "'s turn is skipped",
            "Challenge failed", JOptionPane.INFORMATION_MESSAGE);
    challenge.setVisible(false);
  }

  /**
   * Win challenge.
   */
  @Override
  public void winChallenge() {
    JOptionPane.showMessageDialog(this,
            "Tiles have been moved off the board",
            "Challenge succesful", JOptionPane.INFORMATION_MESSAGE);
    challenge.setVisible(false);
  }

  /**
   * Buys a special tile (either succesfully or unsuccesfully).
   * @param bought tile
   */
  public void boughtTile(final SpecialTile bought) {
    if (bought == null) {
      showErrorDialog(this, "Error!", "Not enough points.");
      return;
    }
    JOptionPane.showMessageDialog(this, bought.toString()
            + " tile has been purchased",
            "Purchase done", JOptionPane.INFORMATION_MESSAGE);
    int newNumber = player.getSpecialTileIntegerMap().get(bought);
    theCount[bought.hashCode()].setText(newNumber + "");
    updateScore();
    updateGrid();
  }

  /**
   * Places a special tile from placement (either succesfully or unsuccesfully).
   * @param lost special tile
   * @param successful placement
   */
  @Override
  public void placeSpecialTile(final SpecialTile lost,
                              final boolean successful) {
    if (!successful) {
      showErrorDialog(this, "Error!", "Can't place special tile here.");
      return;
    }

    updateGrid();
    int newNumber = player.getSpecialTileIntegerMap().get(lost);
    theCount[lost.hashCode()].setText(newNumber + "");
  }

  /**
   * Pops up an error message.
   * @param c component
   * @param title of error message
   * @param msg actual content
   */
  private static void showErrorDialog(final Component c, final String title,
                                      final String msg) {
    JOptionPane.showMessageDialog(c, msg, title, JOptionPane.ERROR_MESSAGE);
  }
}
