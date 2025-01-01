package nye.bence.util;

public class Actions {
    private static int lastX;
    private static int lastY;
    private static int lastValue;
    private static final int WINNING_COUNT = 4;

    private Actions() {}

    public static boolean canPlace(final int[][] b, final int x, final int y) {
        return b[y][x] == 0;
    }

    public static void place(final int x, final int[][] b, final int sizeY, final int val) {
        if (canPlace(b, x, 0)) {
            int i;
            for (i = 0; i < sizeY && b[i][x] == 0; i++) {
                // Loop to find the first non-empty slot
            }
            b[i - 1][x] = val;
            lastX = x;
            lastY = i - 1;
            lastValue = val;
        }
    }

    public static boolean isOverVertical(final int[][] b, final int sizeY) {
        return lastY + WINNING_COUNT - 1 < sizeY
                && b[lastY + (WINNING_COUNT - 3)][lastX] == lastValue
                && b[lastY + (WINNING_COUNT - 2)][lastX] == lastValue
                && b[lastY + (WINNING_COUNT - 1)][lastX] == lastValue;
    }

    public static boolean isOverHorizontal(final int[][] b, final int sizeX) {
        int count = 0;
        for (int i = lastX; i < sizeX && b[lastY][i] == lastValue; i++) {
            count++;
        }
        for (int i = lastX - 1; i >= 0 && b[lastY][i] == lastValue; i--) {
            count++;
        }
        return count >= WINNING_COUNT;
    }

    public static boolean isOverDiagonal(final int[][] b, final int sizeX,
                                         final int sizeY) {
        int count = 0;
        for (int i = lastX, j = lastY; i < sizeX && j < sizeY
                && b[j][i] == lastValue; i++, j++) {
            count++;
        }
        for (int i = lastX - 1, j = lastY - 1; i >= 0 && j >= 0
                && b[j][i] == lastValue; i--, j--) {
            count++;
        }
        if (count >= WINNING_COUNT) {
            return true;
        }
        count = 0;
        for (int i = lastX, j = lastY; i < sizeX && j >= 0
                && b[j][i] == lastValue; i++, j--) {
            count++;
        }
        for (int i = lastX - 1, j = lastY + 1; i >= 0 && j < sizeY
                && b[j][i] == lastValue; i--, j++) {
            count++;
        }
        return count >= WINNING_COUNT;
    }

    public static boolean isOver(final int[][] b, final int sizeX,
                                 final int sizeY) {
        if (isOverVertical(b, sizeY)) {
            return true;
        }
        if (isOverHorizontal(b, sizeX)) {
            return true;
        }
        return isOverDiagonal(b, sizeX, sizeY);
    }
}
