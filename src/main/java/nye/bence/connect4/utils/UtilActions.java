package nye.bence.connect4.utils;
public class UtilActions {
    static int lastX = 0;
    static int lastY = 0;
    static int lastValue;

    //kiírja a táblát


    //megnézi hogy az első sor üres-e ha igen le lehet tenni a korongot
    private static boolean canPlace(int x, int[][] b){
        return b[0][x]==0;
    }

    //Egyszerű place függvény

    public static void place(int x, int[][] b, int SIZE_Y, int val){
        //utils canPlace függvényel megnézi hogy le tudja e tenni a korongot
        if (canPlace(x,b)){
            int i;
            for (i = 0; i < SIZE_Y && b[i][x] == 0; i++){
                //nem kell test mivel csak az i értéke miatt megy végig a cikluson
            };

            lastX = x;
            lastY = i-1;
            lastValue = val;
            b[i-1][x]=val;
        }
    }


    //Hajnal fél 4 re végeztem vele vér izzadással de működnie kéne...
    //nem működik

    //megnézi hogy vége van e a játéknak
    public static boolean isOver(int[][] b, int SIZE_X, int SIZE_Y) {
        int count = 0;

        // függőleges y-
        if (lastY + 3 < SIZE_Y && b[lastY + 1][lastX] == lastValue &&
                b[lastY + 2][lastX] == lastValue && b[lastY + 3][lastX] == lastValue) {
            return true;
        }

        // vízszintes x+-
        count = 1;
        for (int i = 1; i <= 3; i++) {
            if (lastX + i < SIZE_X && b[lastY][lastX + i] == lastValue) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i <= 3; i++) {
            if (lastX - i >= 0 && b[lastY][lastX - i] == lastValue) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 4) {
            return true;
        }

        // átlósan +y+x irányba
        count = 0;
        for (int i = lastY +1, j = lastX +1; i < SIZE_Y && j < SIZE_X && b[i][j] == lastValue; i++, j++) {
            count++;
        }
        //átlósan -y-x irányba
        for (int i = lastY - 1, j = lastX - 1; i >= 0 && j >= 0 && b[i][j] == lastValue; i--, j--) {
            count++;
        }
        if (count >= 3) {
            return true;
        }

        // átlósan +y-x irányba
        count = 0;
        for (int i = lastY+1, j = lastX-1; i < SIZE_Y && j >= 0 && b[i][j] == lastValue; i++, j--) {
            count++;
        }
        //átlósan -y+x irányba
        for (int i = lastY - 1, j = lastX + 1; i >= 0 && j < SIZE_X && b[i][j] == lastValue; i--, j++) {
            count++;
        }
        if (count >= 3) {
            return true;
        }

        return false;
    }

}
