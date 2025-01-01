package nye.bence;

import nye.bence.util.Actions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ActionsTest {

    @Test
    public void testPlace() {
        int[][] board = new int[6][7];
        Actions.place(0, board, 6, 1);
        assertEquals(1, board[5][0]);
    }

    @Test
    public void testIsOverNoWin() {
        int[][] board = new int[6][7];
        assertFalse(Actions.isOver(board, 7, 6));
    }

    @Test
    public void testIsOverHorizontalWin() {
        int[][] board = new int[6][7];
        for (int i = 0; i < 4; i++) {
            board[0][i] = 1;
        }
        assertTrue(Actions.isOver(board, 7, 6));
    }

    @Test
    public void testIsOverVerticalWin() {
        int[][] board = new int[6][7];
        for (int i = 0; i < 4; i++) {
            board[i][0] = 1;
        }
        assertTrue(Actions.isOver(board, 7, 6));
    }

    @Test
    public void testIsOverDiagonalWinPositiveSlope() {
        int[][] board = new int[6][7];
        for (int i = 0; i < 4; i++) {
            board[i][i] = 1;
        }
        assertTrue(Actions.isOver(board, 7, 6));
    }

    @Test
    public void testIsOverDiagonalWinNegativeSlope() {
        int[][] board = new int[6][7];
        for (int i = 0; i < 4; i++) {
            board[3 - i][i] = 1;
        }
        assertTrue(Actions.isOver(board, 7, 6));
    }
}