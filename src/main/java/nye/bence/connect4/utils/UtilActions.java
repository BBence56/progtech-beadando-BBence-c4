package nye.bence.connect4.utils;

public class UtilActions {
    static int lastX = 0;
    static int lastY = 0;
    static int lastValue;

    public static void printBoard(char[][] board) {
        for (char[] chars : board) {
            for (char aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
    }
    private static boolean canPlace(int x, int[][] b){
        return b[x][0]==0;
    }

    public static int[][] place(int x, int[][] b, int SIZE_Y,int val){
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

    public static boolean isOver(int[][] b,int SIZE_X,int SIZE_Y){
        int count = 0;
        if (lastY < 4 & b[lastX][lastY+1]==lastValue&
                        b[lastX][lastY+2]==lastValue&
                        b[lastX][lastY+3]==lastValue)
        {
            return true;
        }
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
        count = 0;

        if(lastY == lastX){
            for (int i = 0; i < SIZE_X; i++) {
                if (b[i][i]==lastValue){
                    count++;
                }else{
                    count = 0;
                }
                if (count == 4){
                    return true;
                }
            }
            count = 0;

            for (int i = 0; i < ; i++) {
                
            }
        }
        

    }

}
