package nye.bence.user;

/**
 * Represents a player in the game.
 */
public class Player {

    /**
     * The name of the player.
     */
    private final String name;

    /**
     * The number of wins the player has.
     */
    private int wins;

    /**
     * Constructs a new Player with the specified name and wins.
     *
     * @param nameParam the name of the player
     * @param winsParam the number of wins the player has
     */
    public Player(final String nameParam, final int winsParam) {
        this.name = nameParam;
        this.wins = winsParam;
    }

    /**
     * Returns the name of the player.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the number of wins the player has.
     *
     * @return the number of wins
     */
    public int getWins() {
        return wins;
    }

    /**
     * Sets the number of wins the player has.
     *
     * @param winsParam the number of wins to set
     */
    public void setWins(final int winsParam) {
        this.wins = winsParam;
    }
}
