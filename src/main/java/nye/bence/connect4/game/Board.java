package nye.bence.connect4.game;

import java.util.Arrays;

public class Board {

    //könnyebb tesztelés miatt konstansban tárolom a tábla méretét
    public final static int SIZE_X = 7;
    public final static int SIZE_Y = 6;
    private int[][] board;

    //konstruktor létrehozza a táblát és feltölti 0-val
    public Board() {
        board = new int[SIZE_Y][SIZE_X];
        for (int i = 0; i < SIZE_Y; i++) {
            for (int j = 0; j < SIZE_X; j++) {
                board[i][j] = 0;
            }
        }
    }

    //menti egy txt fájlba a táblát
    public void save(){
        //TODO
    }
    //betölti a táblát egy txt fájlból
    public void load(){
        //TODO
    }


    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public void printBoard(int[][] board){
        for (int i = 0; i < SIZE_Y; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }


}
