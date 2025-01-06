package nye.bence;

import nye.bence.database.Database;
import nye.bence.ui.UserInterface;


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
        Controller controller = new Controller(database, userInterface);

        controller.run();
    }
}