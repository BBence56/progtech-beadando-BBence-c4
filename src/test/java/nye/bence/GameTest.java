package nye.bence;

import nye.bence.game.Board;
import nye.bence.game.Game;
import nye.bence.user.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameTest {

    private Player mockPlayer;
    private Game game;

    @BeforeEach
    public void setUp() {
        mockPlayer = mock(Player.class);
        game = new Game(mockPlayer);
    }

    @Test
    public void testPlayerPlace() {
        int[][] board = game.getBoard().getMatrix();
        assertFalse(game.playerPlace(1, board));
        assertEquals(1, board[5][0]);
    }

    @Test
    public void testComputerPlace() {
        int[][] board = game.getBoard().getMatrix();
        assertFalse(game.computerPlace(board));
        boolean piecePlaced = false;
        for (int i = 0; i < Board.SIZE_Y; i++) {
            for (int j = 0; j < Board.SIZE_X; j++) {
                if (board[i][j] == 2) {
                    piecePlaced = true;
                    break;
                }
            }
        }
        assertTrue(piecePlaced);
    }

    @Test
    public void testGetBoard() {
        Board board = game.getBoard();
        assertNotNull(board);
    }

    @Test
    public void testGetPlayer() {
        Player player = game.getPlayer();
        assertEquals(mockPlayer, player);
    }
}