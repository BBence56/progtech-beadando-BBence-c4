package nye.bence.game;

/**
 * Represents the game board.
 */
public class Board {

    /**
     * The width of the board.
     */
    public static final int SIZE_X = 7;

    /**
     * The height of the board.
     */
    public static final int SIZE_Y = 6;

    /**
     * The 2D array representing the board.
     */
    private final int[][] board;

    /**
     * Constructs a new Board and initializes it with zeros.
     */
    public Board() {
        board = new int[SIZE_Y][SIZE_X];
        for (int i = 0; i < SIZE_Y; i++) {
            for (int j = 0; j < SIZE_X; j++) {
                board[i][j] = 0;
            }
        }
    }

    /**
     * Returns the board.
     *
     * @return the 2D array representing the board
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Prints the board to the console.
     *
     * @param boardToPrint the board to print
     */
    public void printBoard(final int[][] boardToPrint) {
        for (int i = 0; i < SIZE_Y; i++) {
            for (int j = 0; j < SIZE_X; j++) {
                switch (boardToPrint[i][j]) {
                    case 0:
                        System.out.print("[ ]");
                        break;
                    case 1:
                        System.out.print("[X]");
                        break;
                    case 2:
                        System.out.print("[O]");
                        break;
                    default:
                        break;
                }
            }
            System.out.println();
        }
    }
}
