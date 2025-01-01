package nye.bence.game;

import nye.bence.user.Player;
import nye.bence.util.Actions;


public class Game {

    public Board board;
    Player player;

    public Game(Player player) {
        this.board = new Board();
        this.player = player;
    }



    public boolean playerPlace(final int x, final int[][] b){
        Actions.place(x, b, Board.SIZE_Y,1);
        return Actions.isOver(b, Board.SIZE_X , Board.SIZE_Y);
    }

    public boolean computerPlace(final int[][] b){
        int x = (int) (Math.random() * Board.SIZE_X);
        Actions.place(x, b, Board.SIZE_Y,2);
        return Actions.isOver(b, Board.SIZE_X , Board.SIZE_Y);
    }

}
