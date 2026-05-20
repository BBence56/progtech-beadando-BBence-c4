package nye.bence.ui.gui;

import nye.bence.database.Database;
import nye.bence.user.Player;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Login and registration screen.
 * 
 * Features:
 * - Player login with validation
 * - New player registration
 * - Error handling and feedback
 * - Back navigation to auth screen
 */
public class GuiLoginScreen extends GuiScreen {

    private final Database database;
    private final GuiUserInterface guiUserInterface;
    private final GuiMainMenuScreen mainMenuScreen;
    
    private JTextField nameField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton backButton;
    private JLabel messageLabel;

    /**
     * Constructs the login screen.
     *
     * @param screenManager the screen manager for navigation
     * @param database the database for player operations
     * @param guiUserInterface the main window for setting current player
     * @param mainMenuScreen the menu screen to update with player
     */
    public GuiLoginScreen(ScreenManager screenManager, Database database, 
                          GuiUserInterface guiUserInterface, GuiMainMenuScreen mainMenuScreen) {
        super(screenManager);
        this.database = database;
        this.guiUserInterface = guiUserInterface;
        this.mainMenuScreen = mainMenuScreen;
        initializeUI();
    }

    /**
     * Initializes all UI components.
     */
    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(50, 50, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("Connect 4");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Player name input
        JLabel nameLabel = new JLabel("Player Name:");
        nameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(nameLabel, gbc);

        nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(nameField, gbc);

        // Message feedback
        messageLabel = new JLabel("");
        messageLabel.setForeground(new Color(255, 100, 100));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(messageLabel, gbc);

        // Action buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        buttonPanel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegister());
        buttonPanel.add(registerButton);

        backButton = new JButton("Back");
        backButton.addActionListener(e -> screenManager.showScreen("auth"));
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);
    }

    /**
     * Handles login action.
     * Validates player exists and transitions to main menu.
     */
    private void handleLogin() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            messageLabel.setText("Please enter a name.");
            return;
        }
        try {
            Player player = database.loginPlayer(name);
            if (player != null) {
                // Set player in GUI and menu
                guiUserInterface.setCurrentPlayer(player);
                mainMenuScreen.setCurrentPlayer(player);
                
                // Clear and navigate
                nameField.setText("");
                messageLabel.setText("");
                screenManager.showScreen("mainMenu");
            } else {
                messageLabel.setText("Player not found.");
            }
        } catch (SQLException e) {
            messageLabel.setText("Database error: " + e.getMessage());
        }
    }

    /**
     * Handles registration action.
     * Creates new player and shows success message.
     */
    private void handleRegister() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            messageLabel.setText("Please enter a name.");
            return;
        }
        try {
            if (database.playerExists(name)) {
                messageLabel.setText("Name already taken.");
            } else {
                database.registerPlayer(name);
                messageLabel.setText("Registration successful! Please login.");
                nameField.setText("");
            }
        } catch (SQLException e) {
            messageLabel.setText("Database error: " + e.getMessage());
        }
    }

    /**
     * Called when screen is shown - clears input and focuses name field.
     */
    @Override
    public void onShow() {
        nameField.setText("");
        messageLabel.setText("");
        nameField.requestFocus();
    }
}
