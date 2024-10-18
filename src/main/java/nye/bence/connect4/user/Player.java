package nye.bence.connect4.user;
import nye.bence.connect4.utils.*;


public class Player {

    //későbbi feladatokhoz eltároljuk a nevét és a pontszámát
    private int val;
    private String name;
    private int score; //subject to change


    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.val = 1;
    }

    @Override
    public String toString() {
        return name;
    }

    //util package UtilActions place függvényét használva le rak egy korongot
    public void place(int x, int[][] b, int SIZE_Y){
        UtilActions.place(x,b,SIZE_Y,val);
    }
}
