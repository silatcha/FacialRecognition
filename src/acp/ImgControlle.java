package acp;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import acp.Album;
import acp.ImageRef;
import acp.Matrix;
import acp.Pair;
import acp.Photo;
import acp.Vector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;


public class ImgControlle implements Initializable{
	@FXML
    private AnchorPane anchorPane;
	@FXML
	private VBox vBox;
	@FXML
	private HBox hBox;
	@FXML
	private HBox hBox1;
	@FXML
	private HBox hBoxViewCourante;
	@FXML
	private HBox hBoxViewCourante1;


	
	@FXML
	private ListView eigenface;
	@FXML
	private Button database;
	@FXML
	private Button facial;
	@FXML
	private Button mean;
	@FXML
	ListView listView;
	@FXML
	Button charat;
	@FXML
	TextArea area;
	
	@FXML
	private ImageView imageView;
	@FXML
	private ImageView imageView1;

	

	
	private Album album;
	
	private String  pathTest;
	
	
	// Data base reference images directory
			String src = "res/dataBase/reference";
	
	// Output file for the mean image
	private	String outputImageMean = "res/dataBase/results/ImageMean.jpg";

	private	String outputEigenfaces = "res/dataBase/results/EigenFaces/";

	private	String outputComp = "res/dataBase/results/comp/";
	
	private ImageRef[] images;
	
	// Number of images
			int nb_images ;
			
			private Vector vectorMean;
			private Matrix A ;
			private Pair<Matrix, double[]> pair;
	
	
	
	
	
	
	@FXML
	public void comp(ActionEvent e) throws IOException{
		comp(pair, images, vectorMean, nb_images, outputComp);
		
	}
	@FXML
	public void load(ActionEvent e){
		FileChooser chooser = new FileChooser();
		File f = chooser.showOpenDialog(null);
		if (f != null) {
			
			pathTest=f.getAbsolutePath();
			
			
			Image img= new Image("file:"+f.getAbsolutePath());
			imageView1.setImage(img);
			
		}
		
	}
	@FXML
	public void bg(ActionEvent e) throws IOException{
		 
	
	}
	
	
	
	@FXML
	public void facial(ActionEvent e) throws IOException{
		
		int id=facialRecognition(pathTest,  vectorMean,  pair, A, nb_images);
		
	
				imageView.setImage(images[id].getImg());
				area.setVisible(true);
		
				
		
		
	}
	@FXML
	public void imgMean(ActionEvent e) throws IOException{
		// Calculate the mean image
					imageMean(vectorMean, outputImageMean, images[0].getHeight(), images[0].getWidth());
					Image img =new Image("file:"+outputImageMean);
					imageView.setImage(img);
	}
	
	
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		
		 try {
			
			album= new Album(src);
			area.setVisible(false);
			
			images=album.getImages();
			nb_images= images.length;
			
			// Calculate the mean vector
			 vectorMean = new Vector(images);
			
	        
	        // Calculate the matrix A
			 A = new Matrix(images, vectorMean);
	        
	        // Calculate Eigenfaces and EigenValues   
			pair = A.computeACP(nb_images);
			eigenfaces(pair, nb_images, images[0].getHeight(), images[0].getWidth(), outputEigenfaces);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		 
		 


String[] nom = new String[album.getImages().length];
for(int i=0;i<album.getImages().length;i++) {
	nom[i]=album.getImages()[i].getFname();
}

listView.getItems().addAll(nom);
String[] eigen = new String[pair.getY().length];
for(int i=0;i<pair.getY().length;i++)eigen[i]=""+pair.getY()[i];
eigenface.getItems().addAll(eigen);
		
		
		
		
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
	}

	

	/* Facial recognition of a image  */
	private  int facialRecognition(String pathToRec, Vector vectorMean, Pair<Matrix, double[]> pair, Matrix ensVect, int nb_imgs) throws IOException{
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
			area.setText("Match found : "+ images[id[0]].getFname() +" "+ images[id[0]].getName()+"\nConfidence : "+ id[1] + "\nThreshold : "+ distanceThreshold);
			System.out.println("Match found : "+ images[id[0]].getFname() +" "+ images[id[0]].getName());
			System.out.println("Confidence : "+ id[1] + "\nThreshold : "+ distanceThreshold);
		} else {
			area.setText("No Match found \n Confidence : "+ id[1] + "\nThreshold : "+ distanceThreshold);
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

