package nye.bence.ui.gui;

import nye.bence.database.Database;
import nye.bence.user.Player;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Scoreboard screen displaying top players.
 */
public class GuiScoreboardScreen extends GuiScreen {

    private final Database database;
    private JTable scoreboardTable;
    private ScoreboardTableModel scoreboardTableModel;
    private JLabel statusLabel;
    private JButton backButton;

    public GuiScoreboardScreen(ScreenManager screenManager, Database database) {
        super(screenManager);
        this.database = database;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(50, 50, 50));

        // Title and status
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Top 20 Players");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(180, 180, 180));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(statusLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Scoreboard table
        scoreboardTableModel = new ScoreboardTableModel();
        scoreboardTable = new JTable(scoreboardTableModel);
        configureScoreboardTable(scoreboardTable);

        JScrollPane scrollPane = new JScrollPane(scoreboardTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setBackground(new Color(40, 40, 40));
        scrollPane.getViewport().setBackground(new Color(40, 40, 40));
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        backButton = new JButton("Back");
        backButton.addActionListener(e -> screenManager.showScreen("mainMenu"));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Configures the scoreboard table UI and behavior.
     *
     * @param table the table instance
     */
    private void configureScoreboardTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setBackground(new Color(40, 40, 40));
        table.setForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setGridColor(new Color(80, 80, 80));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setFocusable(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(30, 30, 30));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
    }

    @Override
    public void onShow() {
        loadScoreboard();
    }

    private void loadScoreboard() {
        try {
            List<Player> topPlayers = database.getTopPlayers();
            scoreboardTableModel.setPlayers(topPlayers);
            if (topPlayers.isEmpty()) {
                statusLabel.setText("No results yet. Play a game to appear here.");
            } else {
                statusLabel.setText(" ");
            }
            statusLabel.setForeground(new Color(180, 180, 180));
        } catch (SQLException e) {
            scoreboardTableModel.clear();
            statusLabel.setText("Error loading scoreboard: " + e.getMessage());
            statusLabel.setForeground(new Color(255, 140, 140));
        }
    }

    /**
     * Table model for the scoreboard data.
     */
    private static class ScoreboardTableModel extends AbstractTableModel {

        private static final String[] COLUMNS = {"Rank", "Player", "Wins"};
        private final List<Player> players = new ArrayList<>();

        /**
         * Sets the players to display.
         *
         * @param newPlayers the players list
         */
        public void setPlayers(List<Player> newPlayers) {
            players.clear();
            if (newPlayers != null) {
                players.addAll(newPlayers);
            }
            fireTableDataChanged();
        }

        /**
         * Clears all rows from the table.
         */
        public void clear() {
            players.clear();
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return players.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0 || columnIndex == 2) {
                return Integer.class;
            }
            return String.class;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Player player = players.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return rowIndex + 1;
                case 1:
                    return player.getName();
                case 2:
                    return player.getWins();
                default:
                    return "";
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}
