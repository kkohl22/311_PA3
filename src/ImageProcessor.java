import java.awt.Color;
import java.util.ArrayList;

/**
 * Created by Ken Kohl and Jeff Murray on 4/12/2017.
 */
public class ImageProcessor {

	private static Picture picture;
	private static Color[][] reducedPicture;

    /**
     *
     * @param imageFile
     */

    public ImageProcessor(String imageFile) {
    	picture = new Picture(imageFile);
    }

    /**
     * Method that takes a percentage as input and returns an image that is reduced to that percentage of the previous width.
     * @param x	the percentage to reduce the image width to.
     * @return	the newly reduced image.
     */
    public static Picture reduceWidth(double x) {
    	int[][] I = createImportanceArray();
    	int removedColumns = picture.width() - ((int) Math.ceil(picture.width() * x));
    	
    	for(int i = 0; i < removedColumns; i++) {
    		ArrayList<Integer> vc = DynamicProgramming.minCostVC(I);
    		
    		I = cutImage(I, vc);
    	}
    	
    	Picture retval = new Picture((int) Math.ceil(picture.width() * x), picture.height());
    	
    	for(int i = 0; i < reducedPicture.length; i++) {
    		for(int j = 0; j < reducedPicture[0].length; j++) {
    			retval.set(j, i, reducedPicture[i][j]);
    		}
    	}
    	
        return retval;
    }
    
    /**
     * Method that creates an Importance matrix for the image.
     * @return	the created Importance array.
     */
    private static int[][] createImportanceArray() {
    	int width = picture.width();
    	int height = picture.height();
    	int[][] retval = new int[height][width];
    	reducedPicture = new Color[height][width];
    	
    	int importance = 0;
    	for(int i = 0; i < height; i++) {
    		for(int j = 0; j < width; j++) {
    			reducedPicture[i][j] = picture.get(j, i);
    			Color upPixel;
    			Color downPixel;
    			Color leftPixel;
    			Color rightPixel;
    			
    			// Determine up and down pixel locations
    			if(i > 0 && i < height - 1) {
    				upPixel = picture.get(j, i - 1);
    				downPixel = picture.get(j, i + 1);
    			}
    			else if(i == 0) {
    				upPixel = picture.get(j, height - 1);
    				downPixel = picture.get(j, i + 1);
    			}
    			else {
    				upPixel = picture.get(j, i - 1);
    				downPixel = picture.get(j, 0);
    			}
    			
    			// Determine left and right pixel locations
    			if(j > 0 && j < width - 1) {
    				leftPixel = picture.get(j - 1, i);
    				rightPixel = picture.get(j + 1, i);
    			}
    			else if(j == 0) {
    				leftPixel = picture.get(width - 1, i);
    				rightPixel = picture.get(j + 1, i);
    			}
    			else {
    				leftPixel = picture.get(j - 1, i);
    				rightPixel = picture.get(0, i);
    			}
    			
    			importance = difference(leftPixel, rightPixel) + difference(upPixel, downPixel);
    			retval[i][j] = importance;
    		}
    	}
    	
    	return retval;
    }

    /**
     * Method that determines the level of difference between two colors.
     * @param c1	the first color.
     * @param c2	the second color.
     * @return	the difference between the two inputted color.
     */
    private static int difference(Color c1, Color c2) {
    	int rval = 0;
    	int gval = 0;
    	int bval = 0;
    	
    	rval = c1.getRed() - c2.getRed();
    	rval *= rval;
    	gval = c1.getGreen() - c2.getGreen();
    	gval *= gval;
    	bval = c1.getBlue() - c2.getBlue();
    	bval *= bval;
    	
    	return rval + gval + bval;
    }
    
    /**
     * Method that takes the Importance array generated and the generated vertical cut and creates a new Importance array
     * and image created by reducing by that cut.
     * @param I	the Importance array to be reduced.
     * @param cut	the cut to use to reduce the Importance array and image.
     * @return	the newly cut Importance array.
     */
    private static int[][] cutImage(int[][] I, ArrayList<Integer> cut) {
    	int height = I.length;
    	int width = I[0].length;
    	Color[][] currentPicture = reducedPicture;
    	
    	// Create a new Color array and integer array to represent the cut image and importance arrays
    	reducedPicture = new Color[height][width - 1];
    	int[][] retval = new int[height][width - 1];
    	
    	for(int i = 0; i < height; i++) {
    		// Index of the item to be cut from this row
    		int colIndex = cut.get((2 * i) + 1);
    		
    		// Copy values from I and currentPicture to retval and reducedPicture, skipping over the item in the cut
    		for(int j = 0; j < width; j++) {
    			if(j != colIndex) {
    				if(j < colIndex) {
    					retval[i][j] = I[i][j];
    					reducedPicture[i][j] = currentPicture[i][j];
    				}
    				else {
    					retval[i][j - 1] = I[i][j];
    					reducedPicture[i][j - 1] = currentPicture[i][j];
    				}
    			}
    		}
    	}
    	
    	return retval;
    }
}
