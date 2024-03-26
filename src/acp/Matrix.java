package acp;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.EigenDecomposition;

public class Matrix{
    private double[][] matrix;
    private int height;
    private int width;

    // A constructor that takes a 2D array of doubles and sets the matrix to that array. It also sets
    // the height and width of the matrix.
    public Matrix(double[][] matrix) {
        this.matrix = matrix;
        this.height = matrix.length;
        this.width = matrix[0].length;
    }
    
    // This is a constructor that takes in the height and width of the matrix and creates a matrix of
    // that size.
    public Matrix(int height, int width) {
    	this.height = height;
    	this.width = width;
    	matrix = new double[height][width];
    }

    /**
	 * It takes an array of images and a vector mean, and returns a matrix of the images
	 *
	 * @param imgs an array of ImageRef objects
	 * @param vectorMean the mean vector of the training set
	 * @return A matrix of the images.
	 */
    public Matrix(ImageRef[] images, Vector vectorMean){
        this(images.length, images[0].getHeight()*images[0].getWidth());
		int x=0;
		for (int i=0; i<height; i++) {
			for (int j=0; j<images[0].getHeight(); j++) {
				for (int k=0; k<images[0].getWidth(); k++) {
					matrix[i][x] = images[i].getImageMatrix()[j][k] - vectorMean.getVector()[x];
					x++;
				}
			}
			x=0;
		}
    }

    public Matrix(Photo img, Vector vectorMean) {
    	this(1, img.getHeight()*img.getWidth());
    	int x=0;
    	for (int i = 0; i < img.getHeight(); i++) {
    		for (int k = 0; k<img.getWidth(); k++) {
    			matrix[0][x] = img.getImageMatrix()[i][k] - vectorMean.getVector()[x];
    			x++;
    		}
    	}
    	x=0;
    }

    /**
     * public double[][] getMatrix() {
     *         return matrix;
     *     }
     *
     * @return The matrix is being returned.
     */
    public double[][] getMatrix() {
        return matrix;
    }
    
    /**
     * > This function returns the height of the map
     *
     * @return The height of the map.
     */
    public int getHeigth() {
    	return this.height;
    }
    
    /**
     * This function returns the width of the current object.
     *
     * @return The width of the maze.
     */
    public int getWidth() {
    	return this.width;
    }

    /**
     * The toString() function returns a string representation of the object
     *
     * @return The matrix is being returned.
     */
    @Override
    public String toString() {
        return ("Matrix: " + matrix);
    }

    /**
     * If the object is not null, is the same class, is not the same object, and is a Matrix, then
     * check if the matrices are equal
     *
     * @param obj The object to compare this Matrix against
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
        if (obj instanceof Matrix) {
            Matrix test = (Matrix) obj;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (matrix[i][j] != test.matrix[i][j]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * > This function converts a matrix into a vector by concatenating the rows of the matrix
     *
     * @return A vector of the matrix
     */
    public Vector convertToVector() {
        double[] vect = new double[height * width];
        int k = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                vect[k] = matrix[i][j];
                k++;
            }
        }
        return new Vector(vect);
    }

    /**
     * It converts the matrix into a BufferedImage
     *
     * @return A BufferedImage object.
     */
    public BufferedImage convertToImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int g = (int)matrix[i][j];
                if (g< 0) {
                	g= 0;
                }
                if (g > 255) {
                	g = 255;
                }
                image.setRGB(j, i, new Color(g,g,g).getRGB());
            }
        }
        return image;
    }

    /**
     * > Transpose a matrix by swapping the rows and columns
     *
     * @return A new matrix that is the transpose of the original matrix.
     */
	public Matrix Transpose() {
		Matrix T = new Matrix(this.width, this.height);
		for (int i=0;i<this.width;i++) {
			for (int j=0;j<this.height;j++) {
				T.matrix[i][j] = this.matrix[j][i];
			}
		}
		return T;
	}

    /**
     * For each row in the first matrix, multiply each column in the second matrix by the corresponding
     * element in the row, and add the results
     *
     * @param m1 The first matrix to multiply.
     * @param m2 The matrix that will be multiplied by the current matrix.
     * @return A matrix
     */
    public static Matrix multiplyByMatrix(Matrix m1, Matrix m2) {
        int m1ColLength = m1.height; // m1 columns length
        int m2RowLength = m2.width;    // m2 rows length
        if(m1ColLength != m2RowLength) return null; // matrix multiplication is not possible
        int mRRowLength = m1.width;    // m result rows length
        int mRColLength = m2.height; // m result columns length
        double[][] mResult = new double[mRRowLength][mRColLength];
        for(int i = 0; i < mRRowLength; i++) {         // rows from m1
            for(int j = 0; j < mRColLength; j++) {     // columns from m2
                for(int k = 0; k < m1ColLength; k++) { // columns from m1         	
                    mResult[j][i] += m1.matrix[k][i] * m2.matrix[j][k];
                }
            }
        }
        Matrix result = new Matrix(mResult);
        return result;
    }
    
    /**
     * For each element in the output matrix, multiply the corresponding row of the first matrix by the
     * corresponding column of the second matrix and add the results
     *
     * @param B The matrix to multiply by
     * @return A matrix
     */
	public Matrix matrixMultiply(Matrix B) {
		Matrix C = null;
		if (this.height == B.getWidth()) {
			C = new Matrix(B.getHeigth(), this.width);
			int i,j,k;
			for (i=0; i<C.height; i++) {
				for (j=0; j<C.width; j++) {
					for (k=0; k<this.height; k++) {
						C.matrix[i][j]+=this.matrix[k][j]*B.matrix[i][k];
					}
				}
			}
		} else {
			System.out.println("Invalid sizes for matrix multiplication: \n" + this.toString() + "\n" + B.toString());
		}
		return C;
	}
	
    /**
     * Swap the rows of the matrix at indices i and j.
     *
     * @param i the row to swap with j
     * @param j the row to be swapped with the current row
     */
	public void swap(int i, int j) {
		double[] tmp = matrix[i].clone();
		matrix[i] = matrix[j];
		matrix[j] = tmp;
	}
	
    /**
     * For each row, find the largest value in the row, then divide each value in the row by that largest
     * value
     */
	public void normalise() {
		for (int i=0; i<height;i++) {
			double d = matrix[i][0];
			for (int j=1;j<width;j++) {
				if (d < matrix[i][j]) {
					d = matrix[i][j];
				}
			}
			for (int j=0;j<width;j++) {
				matrix[i][j] = matrix[i][j] / d;
			}
		}
	}

    /**
     * It computes the eigenvectors of the covariance matrix of the training set, sorts them by
     * eigenvalue, and then computes the eigenfaces by multiplying the eigenvectors by the training set
     * 
     * @param A The matrix of the images.
     * @return The EigenFaces matrix and Eigenvalues 
     */
	public Pair<Matrix, double[]> computeACP(int nb_images) {
		
		EigenDecomposition<DenseMatrix64F> evd = this.decomposeMatrix();
		double[] EigenValues = new double[nb_images];
		Matrix EigenVectors = new Matrix(nb_images, nb_images);
		
		for (int i=0; i<nb_images; i++) {
			EigenValues[i] = evd.getEigenvalue(i).real;
			EigenVectors.getMatrix()[i] = evd.getEigenVector(i).data;
		}
		
		// Sort Eigenfaces based on eigenvalues
		int swaps = 0;
		do {
			swaps = 0;
			for (int i=0;i<EigenVectors.getHeigth()-1;i++) {
				if (EigenValues[i] > EigenValues[i+1]) {
					double t = EigenValues[i];
					EigenValues[i] = EigenValues[i+1];
					EigenValues[i+1] = t;
					EigenVectors.swap(i, i+1);
					swaps += 1;
				}
			}
			
		}while (swaps != 0);
		
		// calculate Eigenfaces
		Matrix EigenFaces = this.matrixMultiply(EigenVectors);
		EigenFaces.normalise();
		
		Pair<Matrix, double[]> pair = new Pair<Matrix, double[]>(EigenFaces, EigenValues);
		return pair;
	}

    /**
     * > The function decomposeMatrix() takes a matrix A and returns the eigenvectors of the comatrix of A
     * 
     * @return Eigenvectors of the comatrix
     */
	public EigenDecomposition<DenseMatrix64F> decomposeMatrix() {
		// calculate comatrix 
		Matrix C = this.Transpose().matrixMultiply(this);

		DenseMatrix64F DMc = new DenseMatrix64F(C.getMatrix());
		EigenDecomposition<DenseMatrix64F> evd = DecompositionFactory.eig(C.getHeigth(), true, false);
		evd.decompose(DMc);
		return evd;
	}

    public Vector projectVector(Vector vector) {
        Matrix vectorMatrix = vector.convertToMatrix(1, vector.getLength());
        Matrix projection = vectorMatrix.Transpose().matrixMultiply(this);
        return projection.convertToVector();
    }

    public Vector getCol(int j) {
    	Vector vect = new Vector(this.width);
    	//System.out.println(this.getMatrix()[j].length);
    	
    	for (int i = 0; i < this.width; i++) {
    		vect.getVector()[i] = this.getMatrix()[j][i];
    	}
    	return vect;
    }

}