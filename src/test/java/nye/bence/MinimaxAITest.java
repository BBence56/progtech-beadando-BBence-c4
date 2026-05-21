package nye.bence;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import nye.bence.game.Board;
import nye.bence.util.MinimaxAI;
import org.junit.jupiter.api.Test;

public class MinimaxAITest {

    @Test
    public void testComputerTakesImmediateWinningMove() {
        int[][] board = new int[Board.SIZE_Y][Board.SIZE_X];
        board[5][0] = 2;
        board[5][1] = 2;
        board[5][2] = 2;

        assertEquals(3, MinimaxAI.getBestMove(board));
    }

    @Test
    public void testComputerBlocksImmediatePlayerWin() {
        int[][] board = new int[Board.SIZE_Y][Board.SIZE_X];
        board[5][0] = 1;
        board[5][1] = 1;
        board[5][2] = 1;

        assertEquals(3, MinimaxAI.getBestMove(board));
    }

    @Test
    public void testSearchDoesNotMutateBoard() {
        int[][] board = new int[Board.SIZE_Y][Board.SIZE_X];
        board[5][0] = 1;
        board[5][1] = 2;
        board[4][0] = 1;
        int[][] original = copyBoard(board);

        MinimaxAI.getBestMove(board);

        for (int row = 0; row < Board.SIZE_Y; row++) {
            assertArrayEquals(original[row], board[row]);
        }
    }

    @Test
    public void testFullBoardReturnsNoMove() {
        int[][] board = new int[Board.SIZE_Y][Board.SIZE_X];
        for (int row = 0; row < Board.SIZE_Y; row++) {
            for (int col = 0; col < Board.SIZE_X; col++) {
                board[row][col] = row % 2 + 1;
            }
        }

        assertEquals(-1, MinimaxAI.getBestMove(board));
    }

    private int[][] copyBoard(final int[][] board) {
        int[][] copy = new int[Board.SIZE_Y][Board.SIZE_X];
        for (int row = 0; row < Board.SIZE_Y; row++) {
            System.arraycopy(board[row], 0, copy[row], 0, Board.SIZE_X);
        }
        return copy;
    }
}
