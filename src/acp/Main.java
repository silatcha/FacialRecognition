package acp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	@Override
	public void start(Stage primaryStage) throws IOException{
		Parent root = FXMLLoader.load(getClass().getResource("../assets/projet.fxml"));
        Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("../assets/stylesheet.css").toExternalForm());
		primaryStage.setResizable(false);
		primaryStage.setTitle("Facial Recognition");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
    public static void main(String[] args) throws IOException {
		// // Data base reference images directory
		// String src = "res/dataBase/reference";
		// // Output file for the mean image
		// String outputImageMean = "res/dataBase/results/ImageMean.jpg";

		// String outputEigenfaces = "res/dataBase/results/EigenFaces/";

		// String outputComp = "res/dataBase/results/comp/";
		// // Images on src directory
		// ImageRef[] images = init(src);

        // // Number of images
		// int nb_images = images.length;

		// // Calculate the mean vector
		// Vector vectorMean = new Vector(images);
		// // Calculate the mean image
		// imageMean(vectorMean, outputImageMean, images[0].getHeight(), images[0].getWidth());
        
        // // Calculate the matrix A
		// Matrix A = new Matrix(images, vectorMean);
        
        // // Calculate Eigenfaces and EigenValues   
		// Pair<Matrix, double[]> pair = A.computeACP(nb_images);
		// eigenfaces(pair, nb_images, images[0].getHeight(), images[0].getWidth(), outputEigenfaces);
		// comp(pair, images, vectorMean, nb_images, outputComp);

    	// String srcToRec = "res/dataBase/test/s4.jpg";
		// int id = facialRecognition(srcToRec, vectorMean, pair, A, images);
		
		// System.out.println(images[id].getFname() + " " + images[id].getName());

		launch(args);
    }

    public static ImageRef[] init(String src) throws IOException{
		File file = new File(src);
		// long nb_images = file.list().length;
		long nb_images = 0;
		for (File f : file.listFiles()){
			if (f.isDirectory()) {
				for (String str : f.list()){
					nb_images++;
				}
			}
		}
		ImageRef[] images = new ImageRef[(int)nb_images];
        int cpt = 0;

		for (File f : file.listFiles()){
			if (f.isDirectory()){
				for (String str : f.list()){
					images[cpt] = new ImageRef(src+"/"+f.getName()+"/"+str, f.getName().split("_")[1], f.getName().split("_")[0], cpt);
					cpt++;
				}
			}
		}
		return images;
    }

	private static void imageMean(Vector vectorMean, String output, int height, int width) throws IOException {
		// Calcule de l'image moyenne et write
        ImageIO.write(vectorMean.convertToMatrix(height, width).convertToImage(), "jpg", new File(output));
	}

	private static void eigenfaces(Pair<Matrix, double[]> pair, int nb_images, int height, int width, String output) throws IOException {
		Matrix eigenFaces = pair.getX();
		
		for (int i=0; i<nb_images; i++) {
			Vector vect = new Vector(eigenFaces.getMatrix()[nb_images-i-1]);
			// deep copy to avoid modifying above matrix
			vect = vect.copie();
			vect.mapsatured(vect.min(), vect.max(), 0, 255);
			ImageIO.write(vect.convertToMatrix(height, width).convertToImage(), "jpg", new File(output+i+".jpg"));
		}
	}

	private static void comp(Pair<Matrix, double[]> pair, ImageRef[] images, Vector vectorMean, int nb_images, String output) throws IOException {
		Matrix eigenFaces = pair.getX();
		double[] eigenValues = pair.getY();
		int Ks[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50};
		double[][] xData = new double[nb_images*2][Ks.length];		
		double[] yData = new double[Ks.length];
		
		int l = 0;
		for (int K : Ks) {
			yData[l] = K;
			// caluclate reduced eigenspace + write eigenfaces
			Matrix F =  new Matrix(K, eigenFaces.getWidth());
			for (int i=0; i<K; i++) {
				F.getMatrix()[i] = eigenFaces.getMatrix()[nb_images-i-1];	
			}
			
			// reference Images
			Vector[] imagesVect = new Vector[nb_images*2*2];
			for (int i=0; i<nb_images; i++) {
				Vector v = images[i].projectImage(F, vectorMean);
				Vector un = new Vector (images[i].getImageVector());
				xData[i][l] = Vector.getError(v, un);
				v.map(v.min(), v.max(), 0, 255);
				un.map(un.min(), un.max(), 0, 255);
				imagesVect[i*2] = v;
				imagesVect[i*2+1] = un;

				ImageIO.write(v.convertToMatrix(images[0].getHeight(),images[0].getWidth()).convertToImage(), "jpg", new File(output + "ref_"+i+"_k_"+K+"_e_"+(Math.round(xData[i][l] * 100.0) / 100.0)+".jpg"));
				ImageIO.write(un.convertToMatrix(images[0].getHeight(),images[0].getWidth()).convertToImage(), "jpg", new File(output + "ref_"+i+"_J.jpg"));
			}
			l++;
		}
		// Create Chart
		XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("K").yAxisTitle("E(J)").build();
		for (int p=0; p<nb_images*2; p++) {
			chart.addSeries(String.valueOf(p), yData, xData[p]).setMarker(SeriesMarkers.NONE);
		}
		new SwingWrapper(chart).displayChart();
		// normalise eigenvalues
		Vector EV = new Vector(eigenValues);
		EV.normalise();
		double[] kx = new double[EV.getLength()];
		double[] ky = new double[EV.getLength()];
		double s=0;
		for (int i=0;i<EV.getLength();i++) {
			s += EV.getVector()[EV.getLength()-1-i];
			kx[i] = i;
			ky[i] = s;
		}

		XYChart chart2 = new XYChartBuilder().width(800).height(600).xAxisTitle("K").yAxisTitle("Sum of EigenValues").build();
		chart2.addSeries("sum", kx, ky).setMarker(SeriesMarkers.NONE);
	}

	/* Facial recognition of a image  */
	private static int facialRecognition(String pathToRec, Vector vectorMean, Pair<Matrix, double[]> pair, Matrix ensVect, ImageRef[] images) throws IOException{
		/* get the image */
		Photo imgToRec = new Photo(pathToRec);	
		/* get the clean face */
		Vector cleanVectToRec = (new Matrix(imgToRec, vectorMean)).convertToVector();
		//String outputImageMeanT = "res/dataBase/results/ImageMeanToTes.jpg";
        //ImageIO.write(id.convertToMatrix(100, 100).convertToImage(), "jpg", new File(outputImageMeanT));

		/* get the eigenfaces */
		Matrix eigenFaces = pair.getX();
		
		/* transform to a vector */
		Vector vectToRec = eigenFaces.projectVector(cleanVectToRec);
		
		//System.out.println("ensVect = " + ensVect.getHeigth());

		/* save the eigenVectors in a map*/
	    HashMap<Integer, Vector> map = new HashMap<>();
		for (int i = 0; i < ensVect.getHeigth(); i++) {			
			Vector vectorProj = eigenFaces.projectVector(ensVect.getCol(i));
			//System.out.println(vectorProj.getLength());
			map.put(i, vectorProj);
		}
		
		/* get the id of the closest image */
		int[] id = minDistance(vectToRec, map);

		/* The threshold separating people in and out of the database */
		int distanceThreshold = 51;
		if (id[1] > distanceThreshold) {
			System.out.println("Match found : "+ images[id[0]].getFname() +" "+ images[id[0]].getName());
			System.out.println("Confidence : "+ id[1] + "\nThreshold : "+ distanceThreshold);
		} else {
			System.out.println("No match found.");
			System.out.println("Confidence : "+ id[1] + "\nThreshold : "+ distanceThreshold);
		}	

		return id[0];

		
	}
	
	/* Minimal distance between the image and an eigenVector*/
	/* Returns tab[closestId, precision] */
	private static int[] minDistance(Vector vectToRec, HashMap<Integer, Vector> map) {
        double distance = -1;
        double total = 0;
        int closestId = -1;
        for (int id : map.keySet()) {
            double newDistance = vectToRec.compareTo(map.get(id));
            total += newDistance;
            //System.out.println(newDistance);
            if (distance < 0 || distance > newDistance) {
                distance = newDistance;
                closestId = id;
            }
        }
        // for the precision
		int[] res = {closestId, (int)(100-(100*distance*map.size()/total))};
        // return the closest id : maybe not the good one because return the id of the map and not the one of the closest image
		return res;
	}

}
