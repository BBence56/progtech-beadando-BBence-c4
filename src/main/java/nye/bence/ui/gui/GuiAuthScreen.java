package nye.bence.ui.gui;

import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import nye.bence.database.Database;

/**
 * Initial authentication screen (login or register choice).
 */
public class GuiAuthScreen extends GuiScreen {

    private final Database database;
    private final GuiUserInterface guiUserInterface;
    private JButton loginButton;
    private JButton registerButton;
    private JButton exitButton;

    public GuiAuthScreen(
        ScreenManager screenManager,
        Database database,
        GuiUserInterface guiUserInterface
    ) {
        super(screenManager);
        this.database = database;
        this.guiUserInterface = guiUserInterface;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(50, 50, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        // Title
        JLabel titleLabel = new JLabel("Connect 4");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(new Color(255, 215, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Welcome");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(subtitleLabel, gbc);

        // Buttons
        loginButton = createButton("Login/Register", e -> {
            screenManager.showScreen("login");
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(loginButton, gbc);

        exitButton = createButton("Exit", e -> {
            System.exit(0);
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(exitButton, gbc);
    }

    private JButton createButton(
        String text,
        java.awt.event.ActionListener listener
    ) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(200, 50));
        button.addActionListener(listener);
        return button;
    }
}
