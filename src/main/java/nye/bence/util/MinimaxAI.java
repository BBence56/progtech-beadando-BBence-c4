package nye.bence.util;

import java.util.ArrayList;
import java.util.List;
import nye.bence.game.Board;

/**
 * Utility class containing the Minimax algorithm with Alpha-Beta pruning.
 * Used to calculate the best possible move for the computer player.
 */
public final class MinimaxAI {

    /**
     * The search depth for the Minimax algorithm.
     */
    private static final int MAX_DEPTH = 7;

    /**
     * Human player identifier.
     */
    private static final int HUMAN = 1;

    /**
     * Computer player identifier.
     */
    private static final int COMPUTER = 2;

    /**
     * Score for terminal winning board states.
     */
    private static final int WIN_SCORE = 1_000_000;

    /**
     * Private constructor to prevent instantiation.
     */
    private MinimaxAI() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Determines the best move for the computer.
     *
     * @param board the current game board matrix
     * @return the best column index, or -1 when no move is available
     */
    public static int getBestMove(final int[][] board) {
        List<Integer> validColumns = getValidColumns(board);
        if (validColumns.isEmpty()) {
            return -1;
        }

        int bestColumn = validColumns.get(0);
        int bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        int[][] searchBoard = copyBoard(board);
        for (int column : validColumns) {
            Actions.place(column, searchBoard, Board.SIZE_Y, COMPUTER);

            int score;
            if (isWinningMove(searchBoard, COMPUTER)) {
                score = WIN_SCORE;
            } else {
                score = minimax(searchBoard, MAX_DEPTH - 1, alpha, beta, false);
            }

            undoMove(searchBoard, column);

            if (score > bestScore) {
                bestScore = score;
                bestColumn = column;
            }

            alpha = Math.max(alpha, bestScore);
        }

        return bestColumn;
    }

    /**
     * Recursive Minimax search with Alpha-Beta pruning.
     *
     * @param board the simulated board state
     * @param depth the remaining search depth
     * @param alpha the best score already found for the maximizing player
     * @param beta the best score already found for the minimizing player
     * @param maximizing true when searching the computer's turn
     * @return the evaluated score for the board state
     */
    private static int minimax(final int[][] board,
                               final int depth,
                               int alpha,
                               int beta,
                               final boolean maximizing) {
        if (isWinningMove(board, COMPUTER)) {
            return WIN_SCORE + depth;
        }
        if (isWinningMove(board, HUMAN)) {
            return -WIN_SCORE - depth;
        }
        if (depth == 0 || Actions.isBoardFull(board)) {
            return evaluateBoard(board);
        }

        List<Integer> validColumns = getValidColumns(board);
        if (maximizing) {
            int value = Integer.MIN_VALUE;
            for (int column : validColumns) {
                Actions.place(column, board, Board.SIZE_Y, COMPUTER);
                value = Math.max(
                    value,
                    minimax(board, depth - 1, alpha, beta, false)
                );
                undoMove(board, column);

                alpha = Math.max(alpha, value);
                if (alpha >= beta) {
                    break;
                }
            }
            return value;
        }

        int value = Integer.MAX_VALUE;
        for (int column : validColumns) {
            Actions.place(column, board, Board.SIZE_Y, HUMAN);
            value = Math.min(
                value,
                minimax(board, depth - 1, alpha, beta, true)
            );
            undoMove(board, column);

            beta = Math.min(beta, value);
            if (alpha >= beta) {
                break;
            }
        }
        return value;
    }

    /**
     * Scores the current board from the computer player's perspective.
     *
     * @param board the board to evaluate
     * @return a higher score for stronger computer positions
     */
    private static int evaluateBoard(final int[][] board) {
        int score = 0;

        int centerColumn = Board.SIZE_X / 2;
        for (int row = 0; row < Board.SIZE_Y; row++) {
            if (board[row][centerColumn] == COMPUTER) {
                score += 6;
            }
        }

        for (int row = 0; row < Board.SIZE_Y; row++) {
            for (int column = 0; column <= Board.SIZE_X - 4; column++) {
                score += evaluateWindow(
                    board[row][column],
                    board[row][column + 1],
                    board[row][column + 2],
                    board[row][column + 3]
                );
            }
        }

        for (int column = 0; column < Board.SIZE_X; column++) {
            for (int row = 0; row <= Board.SIZE_Y - 4; row++) {
                score += evaluateWindow(
                    board[row][column],
                    board[row + 1][column],
                    board[row + 2][column],
                    board[row + 3][column]
                );
            }
        }

        for (int row = 0; row <= Board.SIZE_Y - 4; row++) {
            for (int column = 0; column <= Board.SIZE_X - 4; column++) {
                score += evaluateWindow(
                    board[row][column],
                    board[row + 1][column + 1],
                    board[row + 2][column + 2],
                    board[row + 3][column + 3]
                );
            }
        }

        for (int row = 3; row < Board.SIZE_Y; row++) {
            for (int column = 0; column <= Board.SIZE_X - 4; column++) {
                score += evaluateWindow(
                    board[row][column],
                    board[row - 1][column + 1],
                    board[row - 2][column + 2],
                    board[row - 3][column + 3]
                );
            }
        }

        return score;
    }

    /**
     * Scores one four-cell window.
     *
     * @param first first cell
     * @param second second cell
     * @param third third cell
     * @param fourth fourth cell
     * @return the window score
     */
    private static int evaluateWindow(final int first,
                                      final int second,
                                      final int third,
                                      final int fourth) {
        int computerCount = 0;
        int humanCount = 0;
        int emptyCount = 0;

        int[] cells = {first, second, third, fourth};
        for (int cell : cells) {
            if (cell == COMPUTER) {
                computerCount++;
            } else if (cell == HUMAN) {
                humanCount++;
            } else {
                emptyCount++;
            }
        }

        if (computerCount == 4) {
            return 10_000;
        }
        if (humanCount == 4) {
            return -10_000;
        }
        if (computerCount == 3 && emptyCount == 1) {
            return 120;
        }
        if (humanCount == 3 && emptyCount == 1) {
            return -150;
        }
        if (computerCount == 2 && emptyCount == 2) {
            return 20;
        }
        if (humanCount == 2 && emptyCount == 2) {
            return -20;
        }

        return 0;
    }

    /**
     * Checks whether a player has a four-in-a-row.
     *
     * @param board the board to check
     * @param player the player identifier
     * @return true when the player has won
     */
    private static boolean isWinningMove(final int[][] board,
                                         final int player) {
        for (int row = 0; row < Board.SIZE_Y; row++) {
            for (int column = 0; column <= Board.SIZE_X - 4; column++) {
                if (board[row][column] == player
                        && board[row][column + 1] == player
                        && board[row][column + 2] == player
                        && board[row][column + 3] == player) {
                    return true;
                }
            }
        }

        for (int column = 0; column < Board.SIZE_X; column++) {
            for (int row = 0; row <= Board.SIZE_Y - 4; row++) {
                if (board[row][column] == player
                        && board[row + 1][column] == player
                        && board[row + 2][column] == player
                        && board[row + 3][column] == player) {
                    return true;
                }
            }
        }

        for (int row = 0; row <= Board.SIZE_Y - 4; row++) {
            for (int column = 0; column <= Board.SIZE_X - 4; column++) {
                if (board[row][column] == player
                        && board[row + 1][column + 1] == player
                        && board[row + 2][column + 2] == player
                        && board[row + 3][column + 3] == player) {
                    return true;
                }
            }
        }

        for (int row = 3; row < Board.SIZE_Y; row++) {
            for (int column = 0; column <= Board.SIZE_X - 4; column++) {
                if (board[row][column] == player
                        && board[row - 1][column + 1] == player
                        && board[row - 2][column + 2] == player
                        && board[row - 3][column + 3] == player) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns the columns that can accept a new piece.
     *
     * @param board the board to inspect
     * @return available columns ordered from the center outward
     */
    private static List<Integer> getValidColumns(final int[][] board) {
        List<Integer> columns = new ArrayList<>();
        for (int column = 0; column < Board.SIZE_X; column++) {
            if (Actions.canPlace(column, board)) {
                columns.add(column);
            }
        }

        final int centerColumn = Board.SIZE_X / 2;
        columns.sort((first, second) ->
            Integer.compare(
                Math.abs(first - centerColumn),
                Math.abs(second - centerColumn)
            )
        );
        return columns;
    }

    /**
     * Removes the top-most piece from a column.
     *
     * @param board the board to update
     * @param column the column to undo
     */
    private static void undoMove(final int[][] board, final int column) {
        for (int row = 0; row < Board.SIZE_Y; row++) {
            if (board[row][column] != 0) {
                board[row][column] = 0;
                return;
            }
        }
    }

    /**
     * Creates a deep copy of the board.
     *
     * @param board the board to copy
     * @return copied board matrix
     */
    private static int[][] copyBoard(final int[][] board) {
        int[][] copy = new int[Board.SIZE_Y][Board.SIZE_X];
        for (int row = 0; row < Board.SIZE_Y; row++) {
            System.arraycopy(board[row], 0, copy[row], 0, Board.SIZE_X);
        }
        return copy;
    }
}
