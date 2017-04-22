/**
 * Created by Ken Kohl on 4/12/2017.
 */
public class ImageProcessor {
    private static String imageName;

    /**
     *
     * @param imageFile
     */
    public void ImageProcessor(String imageFile) {
        imageName = imageFile;
        Picture picture = new Picture(imageFile);
    }

    /**
     *
     * @param x
     * @return
     */
    public static Picture reduceWidth(double x) {
        return null;
    }

}
