package nye.bence.database;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import nye.bence.user.Player;

/**
 * Database class for managing player data.
 */
public class Database {

    /**
     * The connection to the database.
     */
    private final Connection connection;

    /**
     * Constructs a new Database with the default connection.
     */
    public Database() throws SQLException {
        URL resource = getClass().getClassLoader().getResource("database.db");
        if (resource == null) {
            throw new SQLException("Database file not found in resources.");
        }
        String url = "jdbc:sqlite:" + new File(resource.getFile()).getAbsolutePath();
        this.connection = DriverManager.getConnection(url);
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
