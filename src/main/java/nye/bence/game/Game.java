package nye.bence.game;

import nye.bence.user.Player;
import nye.bence.util.Actions;

/**
 * Represents a game session.
 */
public class Game {

    /**
     * The game board.
     */
    private Board board;

    /**
     * The player.
     */
    private final Player player;

    /**
     * Constructs a new Game with the specified player.
     *
     * @param playerParam the player
     */
    public Game(final Player playerParam) {
        this.board = new Board();
        this.player = playerParam;
    }

    /**
     * Sets the game board.
     *
     * @param boardParam the board to set
     */
    public void setBoard(final Board boardParam) {
        this.board = boardParam;
    }

    /**
     * Places the player's piece on the board.
     *
     * @param x the column to place the piece
     * @param b the board
     * @return true if the game is over, false otherwise
     */
    public boolean playerPlace(final int x, final int[][] b) {
        Actions.place(x-1, b, Board.SIZE_Y, 1);
        return Actions.isOver(b, Board.SIZE_X, Board.SIZE_Y);
    }

    /**
     * Places the computer's piece on the board.
     *
     * @param b the board
     * @return true if the game is over, false otherwise
     */
    public boolean computerPlace(final int[][] b) {
        int x = (int) (Math.random() * Board.SIZE_X);
        Actions.place(x, b, Board.SIZE_Y, 2);
        return Actions.isOver(b, Board.SIZE_X, Board.SIZE_Y);
    }

    /**
     * Returns the game board.
     *
     * @return the game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }
}
