package nye.bence.ui.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import nye.bence.database.Database;
import nye.bence.game.Board;
import nye.bence.game.Game;
import nye.bence.user.Player;
import nye.bence.util.Actions;
import nye.bence.util.GameSaveManager;

/**
 * Interactive Connect-4 game screen.
 *
 * Features:
 * - Mouse-click column selection
 * - Real-time board rendering
 * - AI opponent with 1-second delay
 * - Win/Loss/Tie detection
 * - Game save/exit functionality
 */
public class GuiGameScreen extends GuiScreen {

    private final Database database;
    private Game game;
    private Player currentPlayer;
    private boolean gameOver;
    private boolean isComputerTurn;

    // Board rendering
    private final int cellSize = 70;
    private final int padding = 15;
    private int hoveredColumn = -1; // -1 means no column hovered

    // UI Components
    private JLabel statusLabel;
    private JLabel playerLabel;
    private JButton saveExitButton;
    private JButton exitButton;
    private JTable boardTable;
    private BoardTableModel boardTableModel;

    /**
     * Constructs the game screen with database reference.
     *
     * @param screenManager the screen manager for navigation
     * @param database the database for updating player stats
     */
    public GuiGameScreen(ScreenManager screenManager, Database database) {
        super(screenManager);
        this.database = database;
        this.gameOver = false;
        this.isComputerTurn = false;
        initializeUI();
    }

    /**
     * Initializes the UI components.
     */
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(50, 50, 50));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel with status and player info
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Game board panel (clickable)
        JPanel boardPanel = createBoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        // Bottom panel with action buttons
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    /**
     * Creates the header panel showing game status and player info.
     *
     * @return the header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        // Status label
        statusLabel = new JLabel("Your turn");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setForeground(Color.WHITE);
        headerPanel.add(statusLabel, BorderLayout.WEST);

        // Player label
        playerLabel = new JLabel("Player: -");
        playerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        playerLabel.setForeground(new Color(150, 255, 150));
        headerPanel.add(playerLabel, BorderLayout.EAST);

        return headerPanel;
    }

    /**
     * Creates the interactive game board panel.
     * Supports:
     * - Table-based grid rendering
     * - Column highlighting on hover
     * - Click detection for piece placement
     *
     * @return the board panel
     */
    private JPanel createBoardPanel() {
        boardTableModel = new BoardTableModel();
        boardTable = new JTable(boardTableModel);
        configureBoardTable(boardTable);

        JScrollPane scrollPane = new JScrollPane(boardTable);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER
        );
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);

        JPanel boardContainer = new JPanel(new BorderLayout());
        boardContainer.setOpaque(false);
        boardContainer.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 3),
                BorderFactory.createEmptyBorder(
                    padding,
                    padding,
                    padding,
                    padding
                )
            )
        );
        boardContainer.add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.add(boardContainer);

        return panel;
    }

    /**
     * Configures the JTable used to render the game board.
     *
     * @param table the board table
     */
    private void configureBoardTable(JTable table) {
        table.setRowHeight(cellSize);
        table.setPreferredScrollableViewportSize(
            new Dimension(Board.SIZE_X * cellSize, Board.SIZE_Y * cellSize)
        );
        table.setTableHeader(null);
        table.setShowGrid(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setGridColor(new Color(100, 150, 200));
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setFocusable(false);
        table.setIntercellSpacing(new Dimension(2, 2));
        table.setBackground(new Color(50, 80, 150));
        table.setDefaultRenderer(Object.class, new BoardCellRenderer());

        for (int i = 0; i < Board.SIZE_X; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setMinWidth(cellSize);
            column.setMaxWidth(cellSize);
            column.setPreferredWidth(cellSize);
            column.setResizable(false);
        }

        table.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!gameOver && !isComputerTurn && game != null) {
                        handleBoardClick(e);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hoveredColumn = -1;
                    table.repaint();
                }
            }
        );

        table.addMouseMotionListener(
            new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    updateHoveredColumn(e);
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    updateHoveredColumn(e);
                }
            }
        );
    }

    /**
     * Updates the hovered column based on mouse position.
     *
     * @param e the mouse event
     */
    private void updateHoveredColumn(MouseEvent e) {
        if (!gameOver && !isComputerTurn && game != null) {
            int newHoveredColumn = getColumnFromEvent(e);
            if (newHoveredColumn != hoveredColumn) {
                hoveredColumn = newHoveredColumn;
                boardTable.repaint();
            }
        }
    }

    /**
     * Resolves the board column based on a mouse event.
     *
     * @param e the mouse event
     * @return the column index, or -1 if outside the board
     */
    private int getColumnFromEvent(MouseEvent e) {
        int col = boardTable.columnAtPoint(e.getPoint());
        if (col < 0 || col >= Board.SIZE_X) {
            return -1;
        }
        return col;
    }

    /**
     * Creates the button panel with exit options.
     *
     * @return the button panel
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        // Exit button (without saving)
        exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> handleExit(false));
        buttonPanel.add(exitButton);

        // Save & Exit button
        saveExitButton = new JButton("Save & Exit");
        saveExitButton.addActionListener(e -> handleExit(true));
        buttonPanel.add(saveExitButton);

        return buttonPanel;
    }

    /**
     * Notifies the board table that the underlying board has changed.
     */
    private void refreshBoard() {
        if (boardTableModel != null) {
            boardTableModel.fireTableDataChanged();
        }
    }

    /**
     * Handles player clicking on a column to place a piece.
     *
     * @param e the mouse event
     */
    private void handleBoardClick(MouseEvent e) {
        int col = getColumnFromEvent(e);

        // Validate column
        if (col < 0) {
            return;
        }

        // Check if column can accept a piece
        if (!Actions.canPlace(col, game.getBoard().getMatrix())) {
            JOptionPane.showMessageDialog(
                this,
                "Column is full! Choose another column.",
                "Invalid Move",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Place player piece
        Actions.place(col, game.getBoard().getMatrix(), Board.SIZE_Y, 1);

        refreshBoard();

        // Check if player won
        if (
            Actions.isOver(
                game.getBoard().getMatrix(),
                Board.SIZE_X,
                Board.SIZE_Y
            )
        ) {
            endGame("You won!", true);
            return;
        }

        // Check if board is full (tie)
        if (Actions.isBoardFull(game.getBoard().getMatrix())) {
            endGame("It's a tie!", false);
            return;
        }

        // Computer's turn
        playComputerMove();
    }

    /**
     * Executes the computer's move with a delay.
     *
     */
    private void playComputerMove() {
        isComputerTurn = true;
        statusLabel.setText("Computer's turn...");
        refreshBoard();

        // 1-second delay before computer move
        Timer timer = new Timer(1000, e -> {
            executeComputerMove();
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Executes the actual computer move and checks game state.
     *
     */
    private void executeComputerMove() {
        int[][] board = game.getBoard().getMatrix();
        boolean computerWon = game.computerPlace(board);

        refreshBoard();

        // Check if computer won
        if (computerWon) {
            endGame("Computer won!", false);
            return;
        }

        // Check if board is full (tie)
        if (Actions.isBoardFull(board)) {
            endGame("It's a tie!", false);
            return;
        }

        // Back to player's turn
        isComputerTurn = false;
        statusLabel.setText("Your turn");
        refreshBoard();
    }

    /**
     * Handles the end of a game.
     *
     * @param message the game result message
     * @param playerWon true if player won, false otherwise
     */
    private void endGame(String message, boolean playerWon) {
        gameOver = true;
        statusLabel.setText(message);

        // Update win count if player won
        if (playerWon) {
            try {
                database.incrementPlayerWins(currentPlayer.getName());
                currentPlayer.incrementWins();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error updating wins: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }

        // Show end game dialog
        int option = JOptionPane.showOptionDialog(
            this,
            message + "\n\nPlay again?",
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[] {"New Game", "Main Menu"},
            "Main Menu"
        );

        if (option == 0) {
            startNewGame();
        } else {
            screenManager.showScreen("mainMenu");
        }
    }

    /**
     * Handles exit button - with optional save.
     *
     * @param shouldSave true to save game before exiting, false otherwise
     */
    private void handleExit(boolean shouldSave) {
        if (shouldSave && game != null && currentPlayer != null) {
            GameSaveManager.saveGame(game.getBoard(), currentPlayer);
            JOptionPane.showMessageDialog(
                this,
                "Game saved!",
                "Info",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
        screenManager.showScreen("mainMenu");
    }

    /**
     * Table model backed by the current {@link Board} instance.
     */
    private class BoardTableModel extends AbstractTableModel {

        private Board board;

        /**
         * Sets the board to render.
         *
         * @param board the board instance
         */
        public void setBoard(Board board) {
            this.board = board;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return Board.SIZE_Y;
        }

        @Override
        public int getColumnCount() {
            return Board.SIZE_X;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (board == null) {
                return 0;
            }
            return board.getMatrix()[rowIndex][columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }

    /**
     * Cell renderer that draws the board background, hover highlight,
     * and the red/blue player pieces.
     */
    private class BoardCellRenderer
        extends JPanel
        implements TableCellRenderer
    {

        private int piece;
        private boolean hovered;

        BoardCellRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column
        ) {
            piece = value instanceof Integer ? (Integer) value : 0;
            hovered = column == hoveredColumn && !gameOver && !isComputerTurn;
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Color baseColor = hovered
                ? new Color(255, 255, 100)
                : new Color(50, 80, 150);
            setBackground(baseColor);
            super.paintComponent(g);

            if (piece == 0) {
                return;
            }

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int diameter = Math.min(getWidth(), getHeight()) - 10;
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;

            if (piece == 1) {
                // Player piece (red)
                g2d.setColor(new Color(220, 60, 60));
                g2d.fillOval(x, y, diameter, diameter);
                g2d.setColor(new Color(180, 20, 20));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(x, y, diameter, diameter);
            } else if (piece == 2) {
                // Computer piece (blue)
                g2d.setColor(new Color(80, 120, 255));
                g2d.fillOval(x, y, diameter, diameter);
                g2d.setColor(new Color(40, 70, 200));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(x, y, diameter, diameter);
            }

            g2d.dispose();
        }
    }

    /**
     * Sets the current player.
     *
     * @param player the player
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
        if (playerLabel != null) {
            playerLabel.setText("Player: " + player.getName());
        }
    }

    /**
     * Sets a previously saved board state.
     *
     * @param loadedBoard the board to load
     */
    public void setLoadedBoard(Board loadedBoard) {
        if (currentPlayer == null) {
            return;
        }

        if (loadedBoard == null) {
            startNewGame();
            return;
        }

        game = new Game(currentPlayer);
        game.setBoard(loadedBoard);
        gameOver = false;
        isComputerTurn = false;
        statusLabel.setText("Your turn");
        if (boardTableModel != null) {
            boardTableModel.setBoard(loadedBoard);
        }
        refreshBoard();
    }

    /**
     * Starts a new game.
     */
    private void startNewGame() {
        if (currentPlayer != null) {
            game = new Game(currentPlayer);
            gameOver = false;
            isComputerTurn = false;
            statusLabel.setText("Your turn");
            if (boardTableModel != null) {
                boardTableModel.setBoard(game.getBoard());
            }
            refreshBoard();
        }
    }

    /**
     * Called when screen is shown - initializes game if needed.
     */
    @Override
    public void onShow() {
        hoveredColumn = -1; // Reset hover state
        if (game == null && currentPlayer != null) {
            startNewGame();
        }
        refreshBoard();
    }
}
