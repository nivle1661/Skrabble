package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.Player.Player;
import edu.cmu.cs.cs214.hw4.core.Scrabble.GameChangeListener;
import edu.cmu.cs.cs214.hw4.core.Scrabble.ScrabbleImpl;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.BoardTile;
import edu.cmu.cs.cs214.hw4.core.ScrabbleBoard.ScrabbleBoard;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Contains implementation for the Scrabble Game GUI.
 */
public class ScrabbleGameGui extends JPanel implements GameChangeListener {
  /** The players in the Scrabble game. */
  private static List<Player> players = new ArrayList<>();

  /** The listener for each player GUI. */
  private static List<ScrabbleGamePlayerGui> playerGuis = new ArrayList<>();

  /** Mapping from player to corresponding listener. */
  private static Map<Player, ScrabbleGamePlayerGui> playerToGui =
          new HashMap<>();

  /** The game implementation. */
  private ScrabbleImpl game;

  /** Menu title. */
  private static final String MENU_TITLE = "Game Menu";
  /** New game title. */
  private static final String MENU_NEW_GAME = "New Game";
  /** END game title. */
  private static final String MENU_END_GAME = "End Game";
  /** Add Player title. */
  private static final String MENU_ADD_PLAYER = "Add Player...";
  /** Remove Player title. */
  private static final String MENU_REMOVE_PLAYER = "Remove Player...";
  /** Exit title. */
  private static final String MENU_EXIT = "Exit";

  /** Add player title. */
  private static final String ADD_PLAYER_TITLE = "Add New Player";
  /** Add player message. */
  private static final String ADD_PLAYER_MSG = "Enter player name:";
  /** Remove player title. */
  private static final String REMOVE_PLAYER_TITLE = "Remove Player";
  /** Remove player message. */
  private static final String REMOVE_PLAYER_MSG = "Select a player to remove.";
  /** No players error title. */
  private static final String ERROR_NO_PLAYERS_TITLE = "Error!";
  /** No players error message. */
  private static final String ERROR_NO_PLAYERS_MSG = "No players to remove from"
          + "the game!";
  /** No players error title. */
  private static final String ERROR_NO_MORE_PLAYERS_TITLE = "Error!";
  /** No players error message. */
  private static final String ERROR_NO_MORE_PLAYERS_MSG = "No more players "
          + "can be added.";

  /** Size of a turn header. */
  public static final int TURN_HEADER = 40;
  /** Size of a turn header. */
  public static final int TEXT_WIDTH = 300;
  /** Size of a turn header. */
  public static final int TEXT_HEIGHT = 40;
  /** Size of a board tile on the board. */
  public static final int SQUARE_SIZE = 50;
  /** Size of board. */
  public static final int BOARD_SIZE = 750;
  /** Text size of board tiles. */
  public static final int BOARD_TEXT_SIZE = 12;
  /** Width of the Frame. */
  private final int width = 1250;
  /** Height of the Frame. */
  private final int height = 950;

  /** New game menu. */
  private static JMenuItem newGameMenu;
  /** End game menu. */
  private static JMenuItem endGameMenu;
  /** Add player menu. */
  private static JMenuItem addPlayerMenuItem;
  /** Remove player menu. */
  private static JMenuItem removePlayerMenuItem;

  /** The parent JFrame window. */
  private final JFrame frame;

  /** The actual body of the game. */
  private JPanel body;

  /** The grid representing the Scrabble board. */
  private JButton[][] theGrid = new JButton[ScrabbleBoard.BOARD_SIZE]
          [ScrabbleBoard.BOARD_SIZE];
  /** The labels that make up the scoreline. */
  private List<JLabel> scoreline = new ArrayList<>();
  /** The label that signifies whose turn it is. */
  private JLabel turn;

  /** Up arrow label. */
  private JLabel upArrow;
  /** Down arrow label. */
  private JLabel downArrow;

  /** The top panel. */
  private JPanel top;
  /** The center panel. */
  private JPanel center;
  /** The right panel. */
  private JPanel playerInfo;

  /**
   * Creates the Scrabble Game gui from a Scrabble implementation.
   */
  public ScrabbleGameGui() {
    body = new JPanel(new CardLayout());
    initArrows();

    frame = new JFrame("Scrabble Game");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(width, height));

    // Set-up the menu bar.
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu(MENU_TITLE);
    fileMenu.setMnemonic(KeyEvent.VK_F);

    // Add an 'Add Player' menu item.
    addPlayerMenuItem = new JMenuItem(MENU_ADD_PLAYER);
    addPlayerMenuItem.setMnemonic(KeyEvent.VK_N);
    addPlayerMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent event) {
        if (players.size() == ScrabbleImpl.MAX_PLAYERS) {
          showErrorDialog(frame, ERROR_NO_MORE_PLAYERS_TITLE,
                  ERROR_NO_MORE_PLAYERS_MSG);
          return;
        }
        String name = (String) JOptionPane.showInputDialog(frame,
                ADD_PLAYER_MSG, ADD_PLAYER_TITLE, JOptionPane.PLAIN_MESSAGE,
                null, null, "");

        if (name != null) {
          players.add(new Player(name));
        }
      }
    });
    fileMenu.add(addPlayerMenuItem);

    // Add a 'Remove Player' menu item.
    removePlayerMenuItem = new JMenuItem(MENU_REMOVE_PLAYER);
    removePlayerMenuItem.setMnemonic(KeyEvent.VK_R);
    removePlayerMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent event) {
        if (players.isEmpty()) {
          // No players to remove.
          showErrorDialog(frame, ERROR_NO_PLAYERS_TITLE, ERROR_NO_PLAYERS_MSG);
          return;
        }

        String[] selectionValues = new String[players.size()];
        for (int i = 0; i < players.size(); i++) {
          selectionValues[i] = players.get(i).getName();
        }
        String name = (String) JOptionPane.showInputDialog(frame,
                REMOVE_PLAYER_MSG, REMOVE_PLAYER_TITLE,
                JOptionPane.QUESTION_MESSAGE, null, selectionValues,
                selectionValues[0]);

        if (name != null) {
          for (int i = players.size() - 1; i >= 0; i--) {
            if (name.equals(players.get(i).getName())) {
              players.remove(i);
              break;
            }
          }
        }
      }
    });
    fileMenu.add(removePlayerMenuItem);
    fileMenu.addSeparator();

    // Add a 'New Game' menu item.
    ScrabbleGameGui currentGame = this;
    newGameMenu = new JMenuItem(MENU_NEW_GAME);
    newGameMenu.setMnemonic(KeyEvent.VK_N);
    newGameMenu.addActionListener(event -> {
      if (players.size() < 2) {
        showErrorDialog(frame, "ERROR",
                "Not enough players to start a game!");
        return;
      }
      addPlayerMenuItem.setVisible(false);
      removePlayerMenuItem.setVisible(false);

      game = new ScrabbleImpl();
      List<String> playerNames = new ArrayList<>();
      for (Player player : players) {
        game.addPlayer(player);
        playerNames.add(player.getName());
      }
      game.makeNewGame(currentGame);

      top = initTurn();
      center = initGrid();
      playerInfo = initScores();

      for (Player player : players) {
        ScrabbleGamePlayerGui playerGui =
          new ScrabbleGamePlayerGui(game,
                  player,
                  new ArrayList<>(playerNames));
        game.addGameChangeListener(playerGui);

        playerGui.setGrid(center, theGrid);
        playerGui.setScore(playerInfo,
                scoreline.get(game.indexOf(game.currentPlayer())));
        playerGui.setTurn(top, turn);

        playerToGui.put(player, playerGui);
        body.add(playerGui, player.getName());
      }
      frame.add(body);

      game.startGame();
      System.out.println(game.currentPlayer());

      CardLayout cl = (CardLayout) (body.getLayout());
      cl.show(body, game.currentPlayer().getName());
    });
    fileMenu.add(newGameMenu);

    // Add a 'End Game' menu item.
    JPanel container = this;
    endGameMenu = new JMenuItem(MENU_END_GAME);
    endGameMenu.setMnemonic(KeyEvent.VK_N);
    endGameMenu.addActionListener(event -> {
      JOptionPane.showMessageDialog(container,
              game.getWinner().getName() + " has won!",
              "Game ended",
              JOptionPane.INFORMATION_MESSAGE);
      addPlayerMenuItem.setVisible(true);
      removePlayerMenuItem.setVisible(true);
      game = null;

      body.setVisible(false);
      body = new JPanel(new CardLayout());
    });
    fileMenu.add(endGameMenu);

    fileMenu.addSeparator();
    JMenuItem exitMenuItem = new JMenuItem(MENU_EXIT);
    exitMenuItem.setMnemonic(KeyEvent.VK_X);
    exitMenuItem.addActionListener(event -> System.exit(0));
    fileMenu.add(exitMenuItem);

    menuBar.add(fileMenu);
    frame.setJMenuBar(menuBar);

    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Initiates arrows determining order.
   */
  private void initArrows() {
    BufferedImage myPicture = null;
    ClassLoader classLoader = getClass().getClassLoader();
    try {
      InputStream in = classLoader.getResourceAsStream("uparrow.png");
      myPicture = ImageIO.read(in);
    } catch (IOException e) {
      e.printStackTrace();
    }
    upArrow = new JLabel(new ImageIcon(myPicture));

    try {
      InputStream in = classLoader.getResourceAsStream("downarrow.png");
      myPicture = ImageIO.read(in);
    } catch (IOException e) {
      e.printStackTrace();
    }
    downArrow  = new JLabel(new ImageIcon(myPicture));
  }

  /**
   * Initiates the grid containing the scrabble board.
   * @return JPanel containing the grid
   */
  private JPanel initGrid() {
    JPanel grid = new JPanel();
    grid.setLayout(new GridLayout(ScrabbleBoard.BOARD_SIZE,
            ScrabbleBoard.BOARD_SIZE));
    grid.setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
    ScrabbleBoard board = game.getBoard();

    for (int row = 0; row < ScrabbleBoard.BOARD_SIZE; row++) {
      for (int column = 0; column < ScrabbleBoard.BOARD_SIZE; column++) {
        BoardTile square = board.getSquare(row, column);
        JPanel panel = new JPanel(new BorderLayout());
        JButton button = new JButton(square.toString());
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setFont(new java.awt.Font("Century Schoolbook L",
                Font.BOLD, BOARD_TEXT_SIZE));
        button.setBackground(square.getColor());
        button.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));

        panel.setBackground(square.getColor());
        panel.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        panel.add(button, BorderLayout.CENTER);
        theGrid[row][column] = button;

        grid.add(panel);
      }
    }
    grid.setVisible(true);
    return grid;
  }

  /**
   * Initiates the panel containing the turn header.
   * @return JPanel containing the turn header
   */
  private JPanel initTurn() {
    JPanel headline = new JPanel();
    turn = new JLabel();

    headline.add(turn);
    headline.setVisible(true);
    return headline;
  }

  /**
   * Initiates the panels containing the individual score line.
   * @return panels containing the score lines
   */
  private JPanel initScores() {
    JPanel result = new JPanel();
    upArrow.setVisible(false);
    result.add(upArrow);
    downArrow.setVisible(true);
    result.add(downArrow);

    JPanel actualScore = new JPanel();
    actualScore.setLayout(new GridLayout(players.size(), 1));
    actualScore.setPreferredSize(new Dimension(TEXT_WIDTH,
            TEXT_HEIGHT));

    scoreline = new ArrayList<>();
    for (int i = 0; i < players.size(); i++) {
      JPanel temp = new JPanel();
      JLabel score = new JLabel("");

      scoreline.add(score);
      actualScore.add(temp.add(score));
    }
    result.add(actualScore);
    result.setPreferredSize(new Dimension(2*TEXT_WIDTH, BOARD_SIZE));

    result.add(Box.createRigidArea(new Dimension(TEXT_WIDTH, TEXT_HEIGHT)));
    result.setVisible(true);
    return result;
  }

  /**
   * Starts the game.
   */
  @Override
  public void startGame() {
    ScrabbleGamePlayerGui playerGui = playerToGui.get(game.currentPlayer());
    playerGui.setGrid(center, theGrid);
    playerGui.setScore(playerInfo,
            scoreline.get(game.indexOf(game.currentPlayer())));
    playerGui.setTurn(top, turn);
  }

  /**
   * Ends the game.
   */
  @Override
  public void endGame() {
    JOptionPane.showMessageDialog(this,
            game.getWinner().getName() + " has won!",
            "Game ended",
            JOptionPane.INFORMATION_MESSAGE);

    addPlayerMenuItem.setVisible(true);
    removePlayerMenuItem.setVisible(true);
    game = null;

    body.setVisible(false);
    body = new JPanel(new CardLayout());
  }

  /**
   * Reverses the ordering.
   */
  @Override
  public void reverseOrdering() {
    if (upArrow.isVisible()) {
      upArrow.setVisible(false);
      downArrow.setVisible(true);
    } else {
      downArrow.setVisible(false);
      upArrow.setVisible(true);
    }
  }

  /**
   * Transitions to the next player gui.
   * @param successful transition
   */
  @Override
  public void nextPlayerGui(final boolean successful) {
    if (!successful) {
      showErrorDialog(this, "Error!", "Can't pass move while placing tiles");
    }
    ScrabbleGamePlayerGui playerGui = playerToGui.get(game.currentPlayer());
    playerGui.setGrid(center, theGrid);
    playerGui.setScore(playerInfo,
            scoreline.get(game.indexOf(game.currentPlayer())));
    playerGui.setTurn(top, turn);
    playerGui.startTurn();

    CardLayout cl = (CardLayout) (body.getLayout());
    cl.show(body, game.currentPlayer().getName());
  }

  /**
   * Skips next player's turn.
   */
  @Override
  public void skipNextPlayerTurn() {
    CardLayout cl = (CardLayout) (body.getLayout());

    Player player = game.currentPlayer();
    cl.show(body, player.getName());
    playerToGui.get(player).skipTurn();
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
