package acp;

import java.io.IOException;

public class ImageRef extends Photo{
    private String name;
    private String fname;
    private int id;

    // A constructor for the ImageRef class. It takes in a path, name, fname, and id and sets the
    // values of the ImageRef object to those values.
    public ImageRef(String path, String name, String fname, int id) throws IOException{
        super(path);
        this.name = name;
        this.fname = fname;
        this.id = id;
    }

    /**
     * This function returns the name of the person.
     *
     * @return The name of the person.
     */
    public String getName() {
        return name;
    }

    /**
     * This function returns the value of the variable fname
     *
     * @return The first name of the person.
     */
    public String getFname() {
        return fname;
    }

    /**
     * This function returns the id of the object.
     *
     * @return The id of the object.
     */
    public int getId(){
        return id;
    }

    /**
     * The toString() method returns a string representation of the object
     *
     * @return The super class toString() method is being called, and then the name and fname are being
     * added to the end of the string.
     */
    @Override
    public String toString(){
        return (super.toString() + ", name = "+ name + ", fname = " + fname);
    }

    /**
     * If the object is not null, and is of the same class, and the path, name, fname, and id are the
     * same, then the objects are equal
     *
     * @param obj The object to compare to
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
        if (obj instanceof ImageRef) {
            ImageRef test = (ImageRef) obj;
            return (super.getPath() == test.getPath() && this.name == test.getName() && this.fname == test.getFname() && this.id == test.getId());
        }
        return false;
    }

    public Vector projectImage(Matrix F, Vector vectorMean) {
		ImageRef[] ref = new ImageRef[1];
		ref[0] = this;
		Matrix j = new Matrix(ref, vectorMean);
		Matrix w = F.matrixMultiply(F.Transpose().matrixMultiply(j));
		Vector v = new Vector(w.getMatrix()[0]);
		return v;
	}
}
