package nye.bence;

import nye.bence.database.Database;
import nye.bence.user.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DatabaseTest {

    private Database database;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        database = new Database(mockConnection);
    }

    @Test
    public void testPlayerExists() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        boolean exists = database.playerExists("existingPlayer");

        assertTrue(exists);
        verify(mockPreparedStatement).setString(1, "existingPlayer");
    }

    @Test
    public void testRegisterPlayer() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        database.registerPlayer("newPlayer");

        verify(mockPreparedStatement).setString(1, "newPlayer");
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testLoginPlayer() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("name")).thenReturn("existingPlayer");
        when(mockResultSet.getInt("wins")).thenReturn(5);

        Player player = database.loginPlayer("existingPlayer");

        assertNotNull(player);
        assertEquals("existingPlayer", player.getName());
        assertEquals(5, player.getWins());
        verify(mockPreparedStatement).setString(1, "existingPlayer");
    }

    @Test
    public void testIncrementPlayerWins() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        database.incrementPlayerWins("existingPlayer");

        verify(mockPreparedStatement).setString(1, "existingPlayer");
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testGetTopPlayers() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("name")).thenReturn("player1", "player2");
        when(mockResultSet.getInt("wins")).thenReturn(10, 8);

        List<Player> topPlayers = database.getTopPlayers();

        assertNotNull(topPlayers);
        assertEquals(2, topPlayers.size());
        assertEquals("player1", topPlayers.get(0).getName());
        assertEquals(10, topPlayers.get(0).getWins());
        assertEquals("player2", topPlayers.get(1).getName());
        assertEquals(8, topPlayers.get(1).getWins());
    }
}