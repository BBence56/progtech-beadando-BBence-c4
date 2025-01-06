package nye.bence.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import nye.bence.database.Database;
import nye.bence.game.Board;
import nye.bence.game.Game;
import nye.bence.user.Player;
import nye.bence.util.Actions;
import nye.bence.util.GameSaveManager;

/**
 * User interface class for interacting with the player.
 */
public class UserInterface {

    /**
     * The database instance.
     */
    private final Database database;

    /**
     * The scanner for user input.
     */
    private Scanner scanner;

    /**
     * Constructs a new UserInterface with the specified database.
     *
     * @param databaseParam the database
     */
    public UserInterface(final Database databaseParam) {
        this.database = databaseParam;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Sets the scanner.
     *
     * @param scanner the scanner to set
     */
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Returns the scanner.
     *
     * @return the scanner
     */
    public Scanner getScanner() {
        return scanner;
    }



    /**
     * Registers a new player.
     *
     * @return the registered player, or null if registration failed
     */
    public Player register() {
        System.out.println("-------------------------------------");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        try {
            if (database.playerExists(name)) {
                System.out.println("This name is already used.");
                return null;
            } else {
                database.registerPlayer(name);
                System.out.println("Registration successful.");
                return new Player(name, 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Logs in an existing player.
     *
     * @return the logged-in player, or null if login failed
     */
    public Player login() {
        System.out.println("-------------------------------------");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        try {
            Player player = database.loginPlayer(name);
            if (player == null) {
                System.out.println("There's no player with this name.");
                return null;
            } else {
                System.out.println("Login successful.");
                return player;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Shows the main menu to the player.
     *
     * @param player the player
     */
    public void showMenu(final Player player) {
        boolean exit = false;
        while (!exit) {
            System.out.println("-------------------------------------");
            System.out.println("1. Start Game");
            System.out.println("2. Continue Game");
            System.out.println("3. Show Scoreboard");
            System.out.println("4. Log Out");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    startGame(player, null);
                    break;
                case 2:
                    Board savedBoard = GameSaveManager.loadGame(player);
                    if (savedBoard != null) {
                        startGame(player, savedBoard);
                    }
                    break;
                case 3:
                    showScoreboard();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Shows the scoreboard.
     */
    public void showScoreboard() {
        System.out.println("-------------------------------------");
        try {
            List<Player> topPlayers = database.getTopPlayers();
            System.out.println("Top 20 Players:");
            for (Player player : topPlayers) {
                System.out.println(player.getName() + " - "
                                 + player.getWins() + " wins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts a new game or continues a saved game.
     *
     * @param player the player
     * @param savedBoard the saved board, or null to start a new game
     */
    public void startGame(final Player player, final Board savedBoard) {
        Game game;
        if (savedBoard != null) {
            game = new Game(player);
            game.setBoard(savedBoard);
        } else {
            game = new Game(player);
        }

        boolean gameOver = false;

        while (!gameOver) {
            game.getBoard().printBoard(game.getBoard().getBoard());
            System.out.println("-------------------------------------");
            System.out.print("Enter column to place your piece (1-7) (or type 'exit' to save and quit): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                GameSaveManager.saveGame(game.getBoard(), player);
                return;
            }

            try {
                int column = Integer.parseInt(input);
                if (column < 1 || column > 7) {
                    System.out.println("Invalid input. Please enter a number between 1 and 7.");
                    System.out.println("-------------------------------------");
                    continue;
                }

                if (!Actions.canPlace(column - 1, game.getBoard().getBoard())) {
                    System.out.println("Column is full. Please choose a different column.");
                    System.out.println("-------------------------------------");
                    continue;
                }

                if (game.playerPlace(column, game.getBoard().getBoard())) {
                    game.getBoard().printBoard(game.getBoard().getBoard());
                    System.out.println("-------------------------------------");
                    try {
                        database.incrementPlayerWins(player.getName());
                        System.out.println("You won! Your win count has been updated.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    gameOver = true;
                } else {
                    if (game.computerPlace(game.getBoard().getBoard())) {
                        game.getBoard().printBoard(game.getBoard().getBoard());
                        System.out.println("-------------------------------------");
                        System.out.println("The computer won. Better luck next time!");
                        gameOver = true;
                    } else if (Actions.isBoardFull(game.getBoard().getBoard())) {
                        game.getBoard().printBoard(game.getBoard().getBoard());
                        System.out.println("-------------------------------------");
                        System.out.println("It's a tie!");
                        gameOver = true;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
