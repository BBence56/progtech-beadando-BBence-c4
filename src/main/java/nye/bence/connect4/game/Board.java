package nye.bence.connect4.game;
import nye.bence.connect4.utils.*;

public class Board {
    final static int SIZE_X = 7;
    final static int SIZE_Y = 7;

    private int[][] board;
    public Board() {
        int[][] board = new int[SIZE_X][SIZE_Y];
    };

    public void save(){

    };
    public void load(){
        //TODO
    };


    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }
}
