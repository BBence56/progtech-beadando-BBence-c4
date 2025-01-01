package nye.bence;

import nye.bence.database.Database;
import nye.bence.ui.UserInterface;
import nye.bence.user.Player;

public class App {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load SQLite JDBC driver.");
            e.printStackTrace();
            return;
        }

        Database database = new Database();
        UserInterface ui = new UserInterface(database);

        Player player = null;
        boolean exit = false;
        while (!exit) {
            System.out.println("-------------------------------------");
            System.out.println("Welcome! Please log in or register.");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Close Game");
            System.out.print("Choose an option: ");
            int choice = ui.scanner.nextInt();
            ui.scanner.nextLine();

            switch (choice) {
                case 1:
                    player = ui.login();
                    break;
                case 2:
                    player = ui.register();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

            if (player != null) {
                ui.showMenu(player);
                player = null;
            }
        }
        System.out.println("-------------------------------------");
        System.out.println("Game closed. Goodbye!");
    }
}