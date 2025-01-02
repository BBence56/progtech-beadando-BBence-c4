package nye.bence;

import nye.bence.database.Database;
import nye.bence.user.Player;
import nye.bence.ui.UserInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserInterfaceTest {

    private Database mockDatabase;
    private Scanner mockScanner;
    private UserInterface userInterface;

    @BeforeEach
    public void setUp() {
        mockDatabase = mock(Database.class);
        mockScanner = mock(Scanner.class);
        userInterface = new UserInterface(mockDatabase);
        userInterface.setScanner(mockScanner);
    }

    @Test
    public void testRegisterSuccess() throws SQLException {
        when(mockScanner.nextLine()).thenReturn("newPlayer");
        when(mockDatabase.playerExists("newPlayer")).thenReturn(false);

        Player player = userInterface.register();

        assertNotNull(player);
        assertEquals("newPlayer", player.getName());
        assertEquals(0, player.getWins());
        verify(mockDatabase).registerPlayer("newPlayer");
    }

    @Test
    public void testRegisterFailure() throws SQLException {
        when(mockScanner.nextLine()).thenReturn("existingPlayer");
        when(mockDatabase.playerExists("existingPlayer")).thenReturn(true);

        Player player = userInterface.register();

        assertNull(player);
        verify(mockDatabase, never()).registerPlayer(anyString());
    }

    @Test
    public void testLoginSuccess() throws SQLException {
        when(mockScanner.nextLine()).thenReturn("existingPlayer");
        Player mockPlayer = new Player("existingPlayer", 5);
        when(mockDatabase.loginPlayer("existingPlayer")).thenReturn(mockPlayer);

        Player player = userInterface.login();

        assertNotNull(player);
        assertEquals("existingPlayer", player.getName());
        assertEquals(5, player.getWins());
    }

    @Test
    public void testLoginFailure() throws SQLException {
        when(mockScanner.nextLine()).thenReturn("nonExistentPlayer");
        when(mockDatabase.loginPlayer("nonExistentPlayer")).thenReturn(null);

        Player player = userInterface.login();

        assertNull(player);
    }
}