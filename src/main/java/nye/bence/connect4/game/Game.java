package nye.bence.connect4.game;

import nye.bence.connect4.utils.UtilActions;

import java.util.Random;

public class Game {
    //későbbi feladathoz eltároljuk az id-t
    private int id;
    Board board = new Board();


    public Game(int id) {
        this.id = id;
        this.board = new Board();

    }

    public Board getBoard() {
        return board;
    }

    //bot lépése egy random kordinátát választ az x tengelyen majd meghívja a place függvényt a UtilActions-ből
    //majd megnézi hogy vége van e a játéknak
    public boolean botMove(int[][] b, int SIZE_Y, int SIZE_X) {
        {
            Random rand = new Random();
            int random = rand.nextInt(4);
            UtilActions.place(random, b, SIZE_Y, 2);
            if (UtilActions.isOver(b, SIZE_X, SIZE_Y)) {
                return true;
            }
        }
        return false;
    }
}
