package nye.bence.ui.gui;

import nye.bence.database.Database;
import nye.bence.game.Board;
import nye.bence.user.Player;
import nye.bence.util.GameSaveManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Main GUI application window.
 * 
 * Manages:
 * - Screen navigation using CardLayout
 * - Player state during gameplay
 * - Transitions between screens
 * - Coordinate all GUI components
 */
public class GuiUserInterface extends JFrame {

    private final Database database;
    private final ScreenManager screenManager;
    
    private final GuiAuthScreen authScreen;
    private final GuiLoginScreen loginScreen;
    private final GuiMainMenuScreen mainMenuScreen;
    private final GuiGameScreen gameScreen;
    private final GuiScoreboardScreen scoreboardScreen;

    private Player currentPlayer;

    /**
     * Constructs and initializes the main GUI window.
     *
     * @param database the database instance
     * @throws SQLException if database operations fail
     */
    public GuiUserInterface(Database database) throws SQLException {
        this.database = database;
        this.currentPlayer = null;

        // Setup main frame properties
        setTitle("Connect 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        // Setup card layout for screen management
        JPanel mainPanel = new JPanel();
        CardLayout cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        screenManager = new ScreenManager(mainPanel, cardLayout);

        // Create screens (order matters - dependencies)
        gameScreen = new GuiGameScreen(screenManager, database);
        authScreen = new GuiAuthScreen(screenManager, database, this);
        mainMenuScreen = new GuiMainMenuScreen(screenManager, database, gameScreen);
        loginScreen = new GuiLoginScreen(screenManager, database, this, mainMenuScreen);
        scoreboardScreen = new GuiScoreboardScreen(screenManager, database);

        // Register screens
        screenManager.addScreen("auth", authScreen);
        screenManager.addScreen("login", loginScreen);
        screenManager.addScreen("mainMenu", mainMenuScreen);
        screenManager.addScreen("game", gameScreen);
        screenManager.addScreen("scoreboard", scoreboardScreen);

        add(mainPanel);
        setVisible(true);

        // Show initial screen
        screenManager.showScreen("auth");
    }

    /**
     * Sets the current player for the session.
     *
     * @param player the logged-in player
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
        mainMenuScreen.setCurrentPlayer(player);
        gameScreen.setCurrentPlayer(player);
    }

    /**
     * Gets the current player.
     *
     * @return the current player, or null if no player is logged in
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Navigates to the game screen to start a new game.
     */
    public void startNewGame() {
        if (currentPlayer != null) {
            gameScreen.setCurrentPlayer(currentPlayer);
            screenManager.showScreen("game");
        }
    }

    /**
     * Loads and continues a saved game.
     */
    public void continueGame() {
        if (currentPlayer != null) {
            Board savedBoard = GameSaveManager.loadGame(currentPlayer);
            if (savedBoard != null) {
                gameScreen.setCurrentPlayer(currentPlayer);
                gameScreen.setLoadedBoard(savedBoard);
                screenManager.showScreen("game");
            } else {
                JOptionPane.showMessageDialog(this, "No saved game found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
