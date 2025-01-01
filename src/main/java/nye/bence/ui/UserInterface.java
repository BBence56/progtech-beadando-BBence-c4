package nye.bence.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import nye.bence.database.Database;
import nye.bence.game.Board;
import nye.bence.game.Game;
import nye.bence.user.Player;
import nye.bence.util.GameSaveManager;

public class UserInterface {
    private Database database;
    public Scanner scanner;

    public UserInterface(Database database) {
        this.database = database;
        this.scanner = new Scanner(System.in);
    }

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

    public void showMenu(Player player) {
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

    public void showScoreboard() {
        System.out.println("-------------------------------------");
        try {
            List<Player> topPlayers = database.getTopPlayers();
            System.out.println("Top 20 Players:");
            for (Player player : topPlayers) {
                System.out.println(player.getName() + " - " + player.getWins() + " wins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void startGame(Player player, Board savedBoard) {
        Game game;
        if (savedBoard != null) {
            game = new Game(player);
            game.board = savedBoard;
        } else {
            game = new Game(player);
        }

        boolean gameOver = false;

        while (!gameOver) {
            game.board.printBoard(game.board.getBoard());
            System.out.println("-------------------------------------");
            System.out.print("Enter column to place your piece (or type 'exit' to save and quit): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                GameSaveManager.saveGame(game.board, player);
                return;
            }

            int column = Integer.parseInt(input);

            if (game.playerPlace(column, game.board.getBoard())) {
                game.board.printBoard(game.board.getBoard());
                System.out.println("-------------------------------------");
                try {
                    database.incrementPlayerWins(player.getName());
                    System.out.println("You won! Your win count has been updated.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                gameOver = true;
            } else {
                if (game.computerPlace(game.board.getBoard())) {
                    game.board.printBoard(game.board.getBoard());
                    System.out.println("-------------------------------------");
                    System.out.println("The computer won. Better luck next time!");
                    gameOver = true;
                }
            }
        }
    }
}