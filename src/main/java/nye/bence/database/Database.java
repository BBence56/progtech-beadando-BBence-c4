package nye.bence.database;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import nye.bence.user.Player;

/**
 * Database class for managing player data.
 */
public class Database {

    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    private static final String DATABASE_NAME = "connect4.db";

    /**
     * The connection to the database.
     */
    private final Connection connection;

    /**
     * Constructs a new Database with the default connection.
     * Creates database in user home directory if it doesn't exist.
     * Automatically runs migrations on initialization.
     */
    public Database() throws SQLException {
        String dbPath = getOrCreateDatabasePath();
        String url = "jdbc:sqlite:" + dbPath;

        LOGGER.info("Connecting to database at: " + dbPath);
        this.connection = DriverManager.getConnection(url);

        // Run migrations
        DatabaseMigration migration = new DatabaseMigration(this.connection);
        migration.migrate();

        // Validate schema
        migration.validateSchema();

        LOGGER.info("Database initialized successfully.");
    }

    /**
     * Constructs a new Database with the specified connection.
     *
     * @param connection the connection to use
     */
    public Database(Connection connection) {
        this.connection = connection;
    }

    /**
     * Gets or creates the database file path.
     * Database is stored in user's home directory under .connect4/ folder.
     *
     * @return the absolute path to the database file
     * @throws SQLException if directory creation fails
     */
    private String getOrCreateDatabasePath() throws SQLException {
        try {
            String homeDir = System.getProperty("user.home");
            String dataDir = Paths.get(homeDir, ".connect4").toString();
            File dataDirFile = new File(dataDir);

            if (!dataDirFile.exists()) {
                if (!dataDirFile.mkdirs()) {
                    throw new SQLException("Failed to create data directory: " + dataDir);
                }
                LOGGER.info("Created data directory: " + dataDir);
            }

            String dbPath = Paths.get(dataDir, DATABASE_NAME).toString();
            LOGGER.info("Database path: " + dbPath);

            return dbPath;
        } catch (Exception e) {
            throw new SQLException("Failed to initialize database path", e);
        }
    }

    /**
     * Checks if a player exists in the database.
     *
     * @param name the name of the player
     * @return true if the player exists, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean playerExists(final String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM player WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    /**
     * Registers a new player in the database.
     *
     * @param name the name of the player
     * @throws SQLException if a database access error occurs
     */
    public void registerPlayer(final String name) throws SQLException {
        String sql = "INSERT INTO player (name, wins) VALUES (?, 0)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
    }

    /**
     * Logs in a player by retrieving their data from the database.
     *
     * @param name the name of the player
     * @return the Player object if the player exists, null otherwise
     * @throws SQLException if a database access error occurs
     */
    public Player loginPlayer(final String name) throws SQLException {
        String sql = "SELECT * FROM player WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Player(rs.getString("name"), rs.getInt("wins"));
            } else {
                return null;
            }
        }
    }

    /**
     * Increments the win count of a player.
     *
     * @param name the name of the player
     * @throws SQLException if a database access error occurs
     */
    public void incrementPlayerWins(final String name) throws SQLException {
        String sql = "UPDATE player SET wins = wins + 1 WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves the top 20 players sorted by their win count.
     *
     * @return a list of the top 20 players
     * @throws SQLException if a database access error occurs
     */
    public List<Player> getTopPlayers() throws SQLException {
        String sql = "SELECT name, wins FROM player"
                + " ORDER BY wins DESC LIMIT 20";
        List<Player> topPlayers = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                topPlayers.add(new Player(rs.getString("name"),
                        rs.getInt("wins")));
            }
        }
        return topPlayers;
    }
}
