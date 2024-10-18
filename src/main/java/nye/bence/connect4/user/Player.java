package nye.bence.connect4.user;
import nye.bence.connect4.utils.*;


public class Player {

    private int val;
    private String name;
    private int score;


    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.val = 1;
    }

    @Override
    public String toString() {
        return name;
    }

    public void place(int x, int[][] b, int SIZE_Y){
        UtilActions.place(x,b,SIZE_Y,val);
    }
}
