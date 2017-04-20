import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Ken Kohl on 4/12/2017.
 */
public class DynamicProgramming {
    private static int lastUsed;
    private static int[][] m;
    private static int col;
    private static int cost = 0;
    private static int dif;

    /**
     * @param M
     * @return a min-cost vertical cut.
     */
    public static ArrayList<Integer> minCostVC(int[][] M) {
        return null;
    }

    /**
     * @param x string of length n.
     * @param y string of length m
     * @return d returns a string z (obtained by inserting $ at n − m indices in y) such that AlignCost(x, z) ≤ AlignCost(x, y) over all possible y
     */
    public static String stringAlignment(String x, String y) {
        col = x.length();
        dif = Math.abs(x.length() - y.length());
        lastUsed = x.length() * 2;
        m = new int[col][col];
        init_all();
        constructGrid(x, y);
        char[] sol = indexGrid(x, y, m);
        return Arrays.toString(fillEmptyCells(sol));
    }

    private static void constructGrid(String d, String b) {
        //int[][] grid = new int[d.length()][d.length()];
        for (int i = 0; i < b.length(); i++) {
            for (int j = i; j <= dif + i; j++) {
                m[i][j] = cost(d.charAt(j), b.charAt(i));
            }
        }
    }

    private static char[] indexGrid(String d, String b, int[][] grid) {
        char[] result = new char[d.length()];
        for (int i = b.length() - 1; i >= 0; i--) {
            int min = smallestInRow(i, (dif + i), lastUsed);
            result[min] = b.charAt(i);
        }
        return result;
    }

    private static int cost(char foo, char bar) {
        if (foo == bar) {
            return 0;
        } else return 2;
    }

    private static int smallestInRow(int i, int j, int smllthn) {
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

    private static int getCost(String a, String b) {
        int totalCost = 0;
        if (a.length() == b.length()) {
            char s1[] = a.toCharArray();
            char s2[] = b.toCharArray();
            for (int i = 0; i < a.length(); i++) {
                if (s2[i] == '$') {
                    totalCost += 4;
                } else if (s1[i] != s2[i]) {
                    totalCost += 2;
                }
            }
            return totalCost;
        }
        return -1;
    }


}
