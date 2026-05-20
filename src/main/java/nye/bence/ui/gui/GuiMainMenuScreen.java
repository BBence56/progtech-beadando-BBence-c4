package nye.bence.ui.gui;

import nye.bence.database.Database;
import nye.bence.game.Board;
import nye.bence.user.Player;
import nye.bence.util.GameSaveManager;

import javax.swing.*;
import java.awt.*;

/**
 * Main menu screen for logged-in players.
 * 
 * Features:
 * - Start new game
 * - Continue saved game
 * - View scoreboard
 * - Player stats display
 * - Logout functionality
 */
public class GuiMainMenuScreen extends GuiScreen {

    private final Database database;
    private final GuiGameScreen gameScreen;
    
    private Player currentPlayer;
    private JLabel playerNameLabel;
    private JLabel winsLabel;
    private JButton startGameButton;
    private JButton continueGameButton;
    private JButton scoreboardButton;
    private JButton logoutButton;

    /**
     * Constructs the main menu screen.
     *
     * @param screenManager the screen manager for navigation
     * @param database the database for player operations
     * @param gameScreen reference to game screen for passing game state
     */
    public GuiMainMenuScreen(ScreenManager screenManager, Database database, GuiGameScreen gameScreen) {
        super(screenManager);
        this.database = database;
        this.gameScreen = gameScreen;
        initializeUI();
    }

    /**
     * Initializes all UI components.
     */
    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(50, 50, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Player info section
        playerNameLabel = new JLabel("Welcome, Player!");
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playerNameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(playerNameLabel, gbc);

        winsLabel = new JLabel("Wins: 0");
        winsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        winsLabel.setForeground(new Color(150, 255, 150));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(winsLabel, gbc);

        // Game buttons section
        startGameButton = createButton("Start New Game", e -> startNewGame());
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(startGameButton, gbc);

        continueGameButton = createButton("Continue Game", e -> continueGame());
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(continueGameButton, gbc);

        scoreboardButton = createButton("Show Scoreboard", e -> screenManager.showScreen("scoreboard"));
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(scoreboardButton, gbc);

        logoutButton = createButton("Logout", e -> logout());
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(logoutButton, gbc);
    }

    /**
     * Creates a button with consistent styling.
     *
     * @param text the button text
     * @param listener the action listener
     * @return the styled button
     */
    private JButton createButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(200, 40));
        button.addActionListener(listener);
        return button;
    }

    /**
     * Starts a new game.
     */
    private void startNewGame() {
        if (currentPlayer != null) {
            gameScreen.setCurrentPlayer(currentPlayer);
            gameScreen.setLoadedBoard(null);
            screenManager.showScreen("game");
        }
    }

    /**
     * Continues a previously saved game.
     */
    private void continueGame() {
        if (currentPlayer == null) {
            JOptionPane.showMessageDialog(this, "No player selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Board savedBoard = GameSaveManager.loadGame(currentPlayer);
        if (savedBoard != null) {
            gameScreen.setCurrentPlayer(currentPlayer);
            gameScreen.setLoadedBoard(savedBoard);
            screenManager.showScreen("game");
        } else {
            JOptionPane.showMessageDialog(this, "No saved game found.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Logs out the current player and returns to auth screen.
     */
    private void logout() {
        currentPlayer = null;
        screenManager.showScreen("auth");
    }

    /**
     * Sets the current player and updates display.
     *
     * @param player the logged-in player
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
        updatePlayerDisplay();
    }

    /**
     * Updates the player name and wins display.
     */
    private void updatePlayerDisplay() {
        if (currentPlayer != null) {
            playerNameLabel.setText("Welcome, " + currentPlayer.getName() + "!");
            winsLabel.setText("Wins: " + currentPlayer.getWins());
        }
    }

    /**
     * Called when screen is shown - refreshes player display.
     */
    @Override
    public void onShow() {
        updatePlayerDisplay();
    }
}
