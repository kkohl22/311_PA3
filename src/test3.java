import java.util.Arrays;

/**
 * Created by Ken Kohl on 4/19/2017.
 */
public class test3 {
    private static int lastUsed;
    //  private static cell[][] m;
    private static int[][] m;
    private static int col;
    private static int cost = 0;

    public static void main(String[] args) {
//        String d = "paa";
//        String b = "tea";
//        String d = "beat";
//        String b = "bit";
//        String d = "dmzt";
//        String b = "mmz";
        String d = "republican";
        String b = "democrat";

        col = d.length();
        int dif = Math.abs(d.length() - b.length());
        lastUsed = d.length() * 2;
        m = new int[col][col];
        init_all();
        constructGrid(d, b, dif);
        char[] sol = indexGrid(d, b, m, dif);
        //init_all();


        for (int i = 0; i < col; i++) {
            System.out.print(i + "  ");
            if (i < 10) {
                System.out.print(" ");
            }
            for (int j = 0; j < col; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println(Arrays.toString(sol));
        System.out.println(Arrays.toString(fillEmptyCells(sol)));
        System.out.print(cost);
    }

    private static void constructGrid(String d, String b, int dif) {
        //int[][] grid = new int[d.length()][d.length()];
        for (int i = 0; i < b.length(); i++) {
            for (int j = i; j <= dif + i; j++) {
                m[i][j] = cost(d.charAt(j), b.charAt(i));
            }
        }
    }

    private static char[] indexGrid(String d, String b, int[][] grid, int dif) {
        char[] result = new char[d.length()];
        for (int i = b.length() - 1; i >= 0; i--) {
                int min = smallestInRow(i, (dif + i), lastUsed, dif);
                result[min] = b.charAt(i);
        }
        return result;
    }

    private static int cost(char foo, char bar) {
        if (foo == bar) {
            return 0;
        } else return 2;
    }

    private static int smallestInRow(int i, int j, int smllthn, int dif) {
        int curSmall = Integer.MAX_VALUE;
        int curSmallIndex = -1;
        for (int n = j; n >= i; n--) {
            if (m[i][n] < curSmall && (n < smllthn)) {
                curSmall = m[i][n];
                curSmallIndex = n;
            }
        }
        lastUsed = curSmallIndex;
        return curSmallIndex;
    }

    private static char[] fillEmptyCells(char[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '\u0000') {
                arr[i] = '$';
            }
        }
        return arr;
    }

    private static void init_all() {
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < col; j++) {
                m[i][j] = 8;
            }
        }
    }
}
