package nye.bence.connect4.utils;
public class UtilActions {
    static int lastX = 0;
    static int lastY = 0;
    static int lastValue;

    //kiírja a táblát
    public static void printBoard(char[][] board) {
        for (char[] chars : board) {
            for (char aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
    }

    //megnézi hogy az első sor üres-e ha igen le lehet tenni a korongot
    private static boolean canPlace(int x, int[][] b){
        return b[x][0]==0;
    }

    //Egyszerű place függvény

    public static int[][] place(int x, int[][] b, int SIZE_Y,int val){
        //utils canPlace függvényel megnézi hogy le tudja e tenni a korongot
        if (canPlace(x,b)){
            int i=0;
            while(i<SIZE_Y & b[x][i]==0){
                i++;
            }
            lastX = x;
            lastY = i-1;
            lastValue = val;
            b[x][i-1]=val;
            return b;
        }
        return b;
    }


    //Hajnal fél 4 re végeztem vele vér izzadással de működnie kéne...
    public static boolean isOver(int[][] b,int SIZE_X,int SIZE_Y){

        //count a megtalált azonos korongok száma egy sorban/átlóban (connect4 lényege)
        int count = 0;
        //először a relatív korong allatt keresi 3 al ha nem talál 3at tovább halad mivel fölötte nem lehet korong
        if (lastY < 4 & b[lastX][lastY+1]==lastValue&
                        b[lastX][lastY+2]==lastValue&
                        b[lastX][lastY+3]==lastValue)
        {
            return true;
        }
        //utánna végig megy a vízszintes tengelyen itt egyszerűbbnek tartottam az egészen végig iterálni
        for (int i = 0; i < SIZE_X; i++) {
            if (b[i][lastY]==lastValue){
                count++;
            }else{
                count = 0;
            }
            if (count == 4){
                return true;
            }
        }
        //kezdődik a buli

        //először végig iterál a x+1,y+1 sorozaton és ha egyezés van az értékek közt a countot növeli
        count = 0;
        int i = lastX;
        int j = lastY;
        while(i < SIZE_X & j < SIZE_Y & b[i][j] == lastValue){
            i++;
            j++;
            count ++;
        }
        if (count == 3) {
            return true;
        }
        //ha nem sikerült megtalálni a 3at végig iterál a x-1,y-1 sorozaton
        i = lastX;
        j = lastY;
        while (i >= 0 & j >=0 &b[i][j]== lastValue ){
            i--;
            j--;
            count++;
        }
        //ha a count elérte a 3 at megvan a connect4
        if (count == 3){
            return true;
        }


        //a következő 2 return is ezen az elven működik csak tükrözve
        count = 0;
        i = lastX;
        j = lastY;

        while(i < SIZE_X & j >= 0 & b[i][j] == lastValue){
            i++;
            j--;
            count ++;
        }

        if (count == 3) {
            return true;
        }
        i = lastX;
        j = lastY;
        while (i >= 0 & j < SIZE_Y &b[i][j]== lastValue ){
            i--;
            j++;
            count++;
        }
        if (count == 3){
            return true;
        }

        return false;
    }

}
