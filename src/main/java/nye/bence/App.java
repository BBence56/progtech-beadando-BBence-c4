package nye.bence;

import nye.bence.database.Database;
import nye.bence.ui.gui.GuiUserInterface;

import java.sql.SQLException;
import javax.swing.SwingUtilities;

/**
 * Main application class.
 */
public final class App {

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

        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                new GuiUserInterface(database);
            } catch (SQLException e) {
                System.err.println("Failed to initialize GUI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}