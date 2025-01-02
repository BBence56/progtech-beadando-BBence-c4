package nye.bence;

import nye.bence.database.Database;
import nye.bence.ui.UserInterface;
import nye.bence.user.Player;

import java.sql.SQLException;

/**
 * Main application class.
 */
public final class App {

    // Private constructor to prevent instantiation
    private App() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Main method to run the application.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load SQLite JDBC driver.");
            e.printStackTrace();
            return;
        }

        Database database = new Database();
        UserInterface userInterface = new UserInterface(database);

        Player player = null;
        boolean exit = false;
        while (!exit) {
            System.out.println("-------------------------------------");
            System.out.println("Welcome! Please log in or register.");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Close Game");
            System.out.print("Choose an option: ");
            int choice = userInterface.getScanner().nextInt();
            userInterface.getScanner().nextLine();

            switch (choice) {
                case 1:
                    player = userInterface.login();
                    break;
                case 2:
                    player = userInterface.register();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

            if (player != null) {
                userInterface.showMenu(player);
                player = null;
            }
        }
        System.out.println("-------------------------------------");
        System.out.println("Game closed. Goodbye!");
    }
}
