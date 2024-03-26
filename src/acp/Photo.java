package acp;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javafx.scene.image.Image;

/**
 * > The function decomposeMatrix() takes a matrix A and returns the eigenvectors of the comatrix of A
 *
 * @param A the matrix to decompose
 * @return Eigenvectors of the comatrix
 */
public class Photo {
    private String path;
    private int height;
    private int width;
    private Matrix imageMatrix;
    private Vector imageVector;
    private BufferedImage img1;
    private Image img;

    // This is the constructor of the class Image. It takes a path as a parameter and creates a
    // BufferedImage object from the image at the given path. Then it creates a matrix and a vector
    // from the image.
    public Photo(String path) throws IOException {
        this.path = path;
       
        try {
            if (path == null) {
                throw new NullPointerException("Image path is null");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        this.img1 = ImageIO.read(new File(path));
        this.img=new Image("file:"+path);
        try {
            if (img == null) {
                throw new IOException("Something went wrong, image is not load.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        height = img1.getHeight();
        width = img1.getWidth();
        double[][] imageMatrix = new double[height][width];
        double[] imageVector = new double[height*width];
        int k = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                imageMatrix[i][j] = img1.getRGB(j, i) & 0xFF;
                imageVector[k] = img1.getRGB(j, i) & 0xFF;
                k++;
            }
        }
        this.imageMatrix = new Matrix(imageMatrix);
        this.imageVector = new Vector(imageVector);
    }

    /**
     * The toString() method returns the string representation of the object
     *
     * @return The path of the image.
     */
    @Override
    public String toString() {
        return "My img is : "+path;
    }

    /**
     * If the object is not an Image, or if the object is null, return false. If the object is an
     * Image, return true if the path is the same
     *
     * @param obj The object to compare this Image against.
     * @return The hashcode of the object.
     */
    @Override
    public boolean equals(Object obj){
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        if (this == obj){
            return true;
        }
        if (obj instanceof Photo) {
            Photo test = (Photo) obj;
            return (this.path == test.getPath());
        }
        return false;
    }

    /**
     * This function returns the path of the file
     *
     * @return The path of the file.
     */
    public String getPath() {
        return path;
    }

    /**
     * This function returns the height of the rectangle.
     *
     * @return The height of the rectangle.
     */
    public int getHeight() {
        return height;
    }

    /**
     * This function returns the width of the rectangle.
     *
     * @return The width of the rectangle.
     */
    public int getWidth() {
        return width;
    }

    
    
    public Image getImg() {
		return img;
	}

	/**
     * Returns the image matrix as a 2D array of doubles.
     *
     * @return The image matrix is being returned.
     */
    public double[][] getImageMatrix() {
        return imageMatrix.getMatrix();
    }

    /**
     * This function returns the image vector of the current image.
     *
     * @return The image vector is being returned.
     */
    public double[] getImageVector() {
        return imageVector.getVector();
    }
    
    
}
