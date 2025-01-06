package nye.bence.util;

/**
 * Utility class for game actions.
 */
public final class Actions {

    /**
     * Private constructor to prevent instantiation.
     */
    private Actions() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean canPlace(final int x, final int[][] board) {
        return board[0][x] == 0;
    }

    /**
     * Places a piece on the board.
     *
     * @param x the column to place the piece
     * @param board the game board
     * @param sizeY the height of the board
     * @param player the player number (1 or 2)
     */
    public static void place(final int x,
                             final int[][] board,
                             final int sizeY,
                             final int player) {
        for (int y = sizeY - 1; y >= 0; y--) {
            if (board[y][x] == 0) {
                board[y][x] = player;
                break;
            }
        }
    }

    public static boolean isBoardFull(final int[][] board) {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) {
                    return false;
                }
            }
        }
        return true;

    }

    /**
     * Checks if the game is over.
     *
     * @param board the game board
     * @param sizeX the width of the board
     * @param sizeY the height of the board
     * @return true if the game is over, false otherwise
     */
    public static boolean isOver(final int[][] board,
                                 final int sizeX,
                                 final int sizeY) {
        // Check horizontal, vertical, and diagonal lines for a win
        return checkHorizontal(board, sizeX, sizeY)
                || checkVertical(board, sizeX, sizeY)
                || checkDiagonal(board, sizeX, sizeY);
    }

    /**
     * Checks horizontal lines for a win.
     *
     * @param board the game board
     * @param sizeX the width of the board
     * @param sizeY the height of the board
     * @return true if there is a horizontal win, false otherwise
     */
    private static boolean checkHorizontal(final int[][] board,
                                           final int sizeX,
                                           final int sizeY) {
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX - 3; x++) {
                if (board[y][x] != 0 && board[y][x] == board[y][x + 1]
                        && board[y][x] == board[y][x + 2]
                        && board[y][x] == board[y][x + 3]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks vertical lines for a win.
     *
     * @param board the game board
     * @param sizeX the width of the board
     * @param sizeY the height of the board
     * @return true if there is a vertical win, false otherwise
     */
    private static boolean checkVertical(final int[][] board,
                                         final int sizeX,
                                         final int sizeY) {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY - 3; y++) {
                if (board[y][x] != 0 && board[y][x] == board[y + 1][x]
                        && board[y][x] == board[y + 2][x]
                        && board[y][x] == board[y + 3][x]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks diagonal lines for a win.
     *
     * @param board the game board
     * @param sizeX the width of the board
     * @param sizeY the height of the board
     * @return true if there is a diagonal win, false otherwise
     */
    private static boolean checkDiagonal(final int[][] board,
                                         final int sizeX,
                                         final int sizeY) {
        // Check for diagonals with positive slope
        for (int y = 0; y < sizeY - 3; y++) {
            for (int x = 0; x < sizeX - 3; x++) {
                if (board[y][x] != 0 && board[y][x] == board[y + 1][x + 1]
                        && board[y][x] == board[y + 2][x + 2]
                        && board[y][x] == board[y + 3][x + 3]) {
                    return true;
                }
            }
        }
        // Check for diagonals with negative slope
        for (int y = 3; y < sizeY; y++) {
            for (int x = 0; x < sizeX - 3; x++) {
                if (board[y][x] != 0 && board[y][x] == board[y - 1][x + 1]
                        && board[y][x] == board[y - 2][x + 2]
                        && board[y][x] == board[y - 3][x + 3]) {
                    return true;
                }
            }
        }
        return false;
    }
}
