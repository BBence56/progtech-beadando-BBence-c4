package nye.bence;

import nye.bence.database.Database;
import nye.bence.ui.UserInterface;
import nye.bence.user.Player;

import java.sql.SQLException;

/**
 * Controller class to manage the application flow.
 */
public class Controller {

    private final Database database;
    private final UserInterface userInterface;

    public Controller(Database database, UserInterface userInterface) {
        this.database = database;
        this.userInterface = userInterface;
    }

    public void run() throws SQLException {
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