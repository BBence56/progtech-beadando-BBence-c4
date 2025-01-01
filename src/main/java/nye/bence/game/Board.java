package nye.bence.game;



public class Board {

    public static final int SIZE_X = 7;
    public static final int SIZE_Y = 6;

    private final int[][] board;


    public Board() {
        board = new int[SIZE_Y][SIZE_X];
        for (int i = 0; i < SIZE_Y; i++) {
            for (int j = 0; j < SIZE_X; j++) {
                board[i][j] = 0;
            }
        }
    }

    public int[][] getBoard() {
        return board;
    }

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
