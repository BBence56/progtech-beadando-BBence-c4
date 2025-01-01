package nye.bence.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import nye.bence.user.Player;

public class Database {
    private static final String URL = "jdbc:sqlite:src/main/resources/database.db";

    public boolean playerExists(String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM player WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    public void registerPlayer(String name) throws SQLException {
        String sql = "INSERT INTO player (name, wins) VALUES (?, 0)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
    }

    public Player loginPlayer(String name) throws SQLException {
        String sql = "SELECT * FROM player WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Player(rs.getString("name"), rs.getInt("wins"));
            } else {
                return null;
            }
        }
    }

    public void incrementPlayerWins(String name) throws SQLException {
        String sql = "UPDATE player SET wins = wins + 1 WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
    }

    public List<Player> getTopPlayers() throws SQLException {
        String sql = "SELECT name, wins FROM player ORDER BY wins DESC LIMIT 20";
        List<Player> topPlayers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                topPlayers.add(new Player(rs.getString("name"), rs.getInt("wins")));
            }
        }
        return topPlayers;
    }
}