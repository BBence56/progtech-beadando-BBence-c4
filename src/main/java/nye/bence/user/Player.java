package nye.bence.user;

public class Player {

    private String name;
    private int wins;

    public Player(String name, int wins) {

        this.name = name;
        this.wins = wins;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void addWin() {
        this.wins++;
    }
}