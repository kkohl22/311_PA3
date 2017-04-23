import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ken Kohl and Jeff Murray on 4/12/2017.
 */
public class DynamicProgramming {
    private static int lastUsed;
    private static int[][] m;
    private static int col;
    private static int dif;
    
    /**
     * Method that given a 2D array of integers, finds the smallest costing path from top to bottom.
     * @param M	the matrix to create the vertical cut from.
     * @return a min-cost vertical cut.
     */
    public static ArrayList<Integer> minCostVC(int[][] M) {
    	int rows = M.length;
    	int columns = M[0].length;
    	
    	// Determine smallest element(s) in the last row
    	int minEndPoint = Integer.MAX_VALUE;
    	for(int i = 0; i < columns; i++) {
    		if(M[rows - 1][i] < minEndPoint) {
    			minEndPoint = M[rows - 1][i];
    		}
    	}
    	
    	// Collect indices of that element
    	ArrayList<Integer> minEndPoints = new ArrayList<Integer>();
    	for(int i = 0; i < columns; i++) {
    		if(M[rows - 1][i] == minEndPoint) {
    			minEndPoints.add(i);
    		}
    	}
    	
    	// Iterate through smallest end points and pick one to use.
    	int minCost = Integer.MAX_VALUE;
    	CostPair minPair = new CostPair();
    	for(int i = 0; i < minEndPoints.size(); i++) {
    		CostPair pair = findCheapestPath(M, minEndPoints.get(i));
    		
    		// Defaults to the rightmost value that has a minimum cost path if there are multiple of the same cost
    		if(pair.getCost() <= minCost) {
    			minCost = pair.getCost();
    			minPair = pair;
    		}
    	}
    	
    	// Create the path to return
    	ArrayList<Integer> minPath = minPair.getPath();
    	Collections.reverse(minPath);
    	ArrayList<Integer> retval = new ArrayList<Integer>();
    	for(int i = 0; i < minPath.size(); i++) {
    		retval.add(i);
    		retval.add(minPath.get(i));
    	}
    	
    	return retval;
    }

    /**
     * @param x string of length n.
     * @param y string of length m
     * @return d returns a string z (obtained by inserting $ at n − m indices in y) such that AlignCost(x, z) ≤ AlignCost(x, y) over all possible y
     */
    public static String stringAlignment(String x, String y) {
        String result = "";
        col = x.length();
        dif = Math.abs(x.length() - y.length());
        lastUsed = x.length() * 2;
        m = new int[col][col];
        init_all();
        constructGrid(x, y);
        char[] sol = indexGrid(x, y, m);
        char[] resultArray = fillEmptyCells(sol);
        for(char c : resultArray) {
            result += c;
        }
        return result;
    }
    
    /**
     * Method that starts from a specific index on the bottom row of the matrix and finds the cheapest path to the top.
     * @param M	the array to find a path through.
     * @param startIndex the index on the bottom row to start the path from.
     * @return	the resulting CostPair representing the path and the cost of that path.	
     */
    private static CostPair findCheapestPath(int[][] M, int startIndex) {
    	int rows = M.length;
    	int columns = M[0].length;
    	int currentIndex = startIndex;
    	CostPair pair = new CostPair();
    	
    	// Iterate through the grid in reverse, as the bottom values have the most weight
    	for(int i = rows - 1; i > 0; i--) {
    		pair.addToPath(currentIndex);
    		pair.addToCost(M[i][currentIndex] * i);
    		
    		int upLeft, upCenter, upRight;
    		upLeft = upCenter = upRight = Integer.MAX_VALUE;
    		
    		// Try to obtain elements diagonally above and left, straight above, and diagonally above and right
			if(currentIndex - 1 >= 0) upLeft = M[i - 1][currentIndex - 1];
			if(currentIndex + 1 < M[i].length) upRight = M[i - 1][currentIndex + 1];
			upCenter = M[i - 1][currentIndex];
			
			
			// If there are duplicate values, determine which of the options is best
			if(((upLeft == upCenter && upLeft != Integer.MAX_VALUE) 
					|| (upCenter == upRight && upCenter != Integer.MAX_VALUE) 
					|| (upRight == upLeft && upRight != Integer.MAX_VALUE))) {
				int min = getMinThree(upLeft, upCenter, upRight);
				
				if(i - 2 >= 0) {
					ArrayList<Integer> nextValues = new ArrayList<Integer>();
					
					// Add value the left can reach if upLeft == min
					if(currentIndex - 2 >= 0 && upLeft == min) {
						nextValues.add(M[i - 2][currentIndex - 2]);
					}
					
					// Add values any combination can reach
					if(currentIndex - 1 >= 0) nextValues.add(M[i - 2][currentIndex - 1]);
					nextValues.add(M[i - 2][currentIndex]);
					if(currentIndex + 1 < columns) nextValues.add(M[i - 2][currentIndex + 1]);
					
					// Add values the right can reach if upRight == min
					if(currentIndex + 2 < columns && upRight == min) {
						nextValues.add(M[i - 2][currentIndex + 2]);
					}
					
					int minIndex = getMinIndex(nextValues);
					
					// all three + left and right
					if((upLeft == upCenter && upCenter == upRight) || upLeft == upRight) {
						if(minIndex == 0) currentIndex--;
						else if(minIndex == 4) currentIndex++;
						// Otherwise keep currentIndex
					}
					// left and center
					else if(minIndex == 0 && upLeft == upCenter) currentIndex--;
					// right and center
					else if(minIndex == 3 && upRight == upCenter) currentIndex++;
				}
			}
			// Otherwise determine the minimum value and choose it
			else {
				int min = getMinThree(upLeft, upCenter, upRight);
				
				if(min == upLeft) currentIndex--;
				else if(min == upRight) currentIndex++;
				// Otherwise currentIndex is the same
			}
    	}
    	pair.addToPath(currentIndex);
    	
    	return pair;
    	
    }
    
    /**
     * Method that determines the smallest element of three inputs.
     * @param a	the first element.
     * @param b	the second element.
     * @param c	the third element.
     * @return	the smallest element present.
     */
    private static int getMinThree(int a, int b, int c) {
    	if(a < 0) a = Integer.MAX_VALUE;
    	if(b < 0) b = Integer.MAX_VALUE;
    	if(c < 0) c = Integer.MAX_VALUE;
    	
    	if(a <= b && a <= c) return a;
		else if(b <= a && b <= c) return b;
    	return c;
    }
    
    /**
     * Method that determines the index of the smallest element in an ArrayList of Integers.
     * @param list	the list to iterate through.
     * @return	the index of the smallest element present in this ArrayList.
     */
    private static int getMinIndex(ArrayList<Integer> list) {
    	int min = Integer.MAX_VALUE;
    	int minIndex = -1;
    	
    	for(int i = 0; i < list.size(); i++) {
    		if(list.get(i) <= min) {
    			min = list.get(i);
    			minIndex = i;
    		}
    	}
    	
    	return minIndex;
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
}
