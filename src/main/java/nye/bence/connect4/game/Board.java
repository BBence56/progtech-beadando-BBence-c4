package nye.bence.connect4.game;
import nye.bence.connect4.utils.*;

public class Board {

    //könnyebb tesztelés miatt konstansban tárolom a táblát
    final static int SIZE_X = 7;
    final static int SIZE_Y = 7;

    /*
    Az adatokat egy N*M mátrixban tárolom
    egyenlőre a játékos "1" értékkel a gépi játékos "2" értékkel fog rendelkezik
    az üres helyeket "0" val jelölöm
    */
    private int[][] board;
    public Board() {
        int[][] board = new int[SIZE_X][SIZE_Y];
    };

    public void save(){
        //TODO
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
