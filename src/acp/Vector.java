package acp;

public class Vector {

    private double[] vector;
    private int size;

	// A constructor that takes in a double array and sets the vector to the double array.
    public Vector(double[] vector) {
        this.vector = vector;
        size = this.vector.length;
    }

	// A copy constructor.
    public Vector(Vector vector) {
        this.vector = vector.getVector();
        size = this.vector.length;
    }

    
	// This is a constructor that takes in an integer and creates a vector of that size.
    public Vector(int size) {
    	this.vector = new double[size];
    	this.size = size;
    }

	/**
	 * It takes an array of images, and returns a vector that is the mean of all the images
	 *
	 * @param images an array of ImageRef objects
	 * @return A vector of the mean of the images.
	 */
	public Vector(ImageRef[] images){
		this(images[0].getHeight()*images[0].getWidth());
		int x=0;
		for (int i=0; i<images[0].getHeight(); i++) {
			for (int j=0; j<images[0].getWidth(); j++) {
				for (int k=0; k<images.length; k++) {
					vector[x] += images[k].getImageMatrix()[i][j];
				}
				vector[x] /= images.length;
				x++;
			}
		}
	}
    
	/**
	 * The toString() function returns a string representation of the object
	 *
	 * @return The string "Vector: " and the vector.
	 */
    @Override
    public String toString() {
        return ("Vector: " + vector);
    }

	/**
	 * If the object is not null, and is of the same class, and has the same values, then it is equal
	 *
	 * @param obj The object to compare this Vector against
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
        if (obj instanceof Vector) {
            Vector test = (Vector) obj;
            for (int i = 0; i < size; i++) {
                if (vector[i] != test.vector[i]) {
                    return false;
                }
            }
        }
        return true;
    }

	/**
	 * This function returns the vector.
	 *
	 * @return The vector of the instance.
	 */
    public double[] getVector() {
        return vector;
    }

	/**
	 * Returns the number of elements in this list.
	 *
	 * @return The size of the array.
	 */
    public int getLength() {
        return size;
    }
    
	/**
	 * Find the smallest value in the vector.
	 *
	 * @return The smallest value in the vector.
	 */
	public double min() {
		double s = vector[0];
		for (int i=1;i<size;i++) {
			if (vector[i] < s)
				s = vector[i];
		}
		return s;
	}
	
	/**
	 * Find the largest value in the vector.
	 *
	 * @return The maximum value in the vector.
	 */
	public double max() {
		double s = vector[0];
		for (int i=1;i<size;i++) {
			if (vector[i] > s)
				s = vector[i];
		}
		return s;
	}
	
	/**
	 * Map the values of the vector from the range [a,b] to the range [c,d].
	 *
	 * @param a the minimum value of the original vector
	 * @param b the upper bound of the input vector
	 * @param c the minimum value of the new range
	 * @param d the maximum value of the new range
	 */
	public void map(double a,double b,double c,double d) {
		for (int i=0;i<this.size;i++) {
			this.vector[i] = (this.vector[i] - a) / (b - a) * (d - c) + c;
		}
	}
	
	public void mapsatured(double a, double b, double c, double d) {
		for (int i=0;i<this.size;i++) {
			double val = (this.vector[i] - a) / (b - a) * (d - c) + c;
			if (val < c) {
				val = c;
			}
			if (val > d) {
				val = d;
			}
			this.vector[i] = val;
		}
	}
	
	/**
	 * > This function takes a vector and converts it into a matrix
	 *
	 * @param height the height of the matrix
	 * @param width the width of the image
	 * @return A matrix of the same size as the original matrix.
	 */
	public Matrix convertToMatrix(int height, int width) {
		double[][] mat = new double[height][width];
		int k=0;
		for (int i=0; i<height; i++) {
			for (int j=0; j< width; j++) {
				mat[i][j] = this.getVector()[k];
				k++;
			}
		}
		return (new Matrix(mat));
	}
	
	/**
	 * It normalises the vector
	 */
	public void normalise() {
		double s = 0;
		for (int i=0; i<size; i++) {
			s += vector[i];
		}
		for (int i=0; i<size; i++) {
			vector[i] = vector[i] / s;
		}
	}
	
	public Vector copie() {
		Vector copie = new Vector(this.size);
		for (int i=0; i<this.size; i++) {
			copie.vector[i] = this.vector[i];
		}
		return copie;
	}
	
	public static double getError(Vector v1, Vector v2) {
		double d=0;
		for (int i=0; i<v1.size;i++) {
			d += (v2.vector[i] - v1.vector[i]) * (v2.vector[i] - v1.vector[i]);
		}
		return Math.sqrt(d);
	}
	
	public double compareTo(Vector vector) {
        double distance = 0;
        for (int i = 0; i < this.getLength(); i++) {
                distance += Math.pow(this.getVector()[i]-vector.getVector()[i], 2);
        }
        return Math.sqrt(distance);
    }

}