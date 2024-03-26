// package acp;

// import java.io.File;
// import java.io.IOException;
// import java.net.URL;
// import java.util.HashMap;
// import java.util.ResourceBundle;

// import javax.imageio.ImageIO;

// import acp.ImageRef;
// import acp.Matrix;
// import acp.Pair;
// import acp.Vector;
// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.fxml.Initializable;
// import javafx.geometry.Insets;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.control.ListView;
// import javafx.scene.control.Slider;
// import javafx.scene.control.TextArea;
// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
// import javafx.scene.layout.AnchorPane;
// import javafx.scene.layout.HBox;
// import javafx.scene.layout.VBox;
// import javafx.stage.FileChooser;


// public class JfxController implements Initializable{
// 	@FXML
//     private AnchorPane anchorPane;
// 	@FXML
// 	private VBox vBox;
// 	@FXML
// 	private HBox hBox;
// 	@FXML
// 	private HBox hBox1;
// 	@FXML
// 	private HBox hBoxViewCourante;
// 	@FXML
// 	private HBox hBoxViewCourante1;
// 	@FXML
// 	private HBox hBoxView;
// 	@FXML
// 	private Button preview;
// 	@FXML
// 	private Button next;
// 	@FXML
// 	private ListView eigenface;
// 	@FXML
// 	private Button database;
// 	@FXML
// 	private Button facial;
// 	@FXML
// 	private Button mean;
// 	@FXML
// 	private ListView listView;
// 	@FXML
// 	private Button charat;
// 	@FXML
// 	private Slider slider;
// 	@FXML
// 	private ImageView imageView;
// 	@FXML
// 	private ImageView imageView1;
// 	@FXML
// 	private ImageView img;
// 	@FXML
// 	private ImageView img1;
// 	@FXML
// 	private ImageView img2;
// 	@FXML
// 	private ImageView img3;
// 	@FXML
// 	private ImageView img4;

// 	private String  pathTest;
	
	
// 	// Data base reference images directory
// 			String src = "res/dataBase/reference";
	
// 	// Output file for the mean image
// 	private	String outputImageMean = "res/dataBase/results/ImageMean.jpg";

// 	private	String outputEigenfaces = "res/dataBase/results/EigenFaces/";

// 	private	String outputComp = "res/dataBase/results/comp/";
	
// 	private ImageRef[] images;
	
// 	// Number of images
// 	int nb_images ;
	
// 	private Vector vectorMean;
// 	private Matrix A ;
// 	private Pair<Matrix, double[]> pair;
	
	
// 	@FXML
// 	public void comp(ActionEvent e) throws IOException{
// 		comp(pair, images, vectorMean, nb_images, outputComp);
		
// 	}
// 	@FXML
// 	public void load(ActionEvent e){
// 		FileChooser chooser = new FileChooser();
// 		File f = chooser.showOpenDialog(null);
// 		if (f != null) {
			
// 			pathTest = f.getAbsolutePath();
			
// 			Image img= new Image("file:"+f.getAbsolutePath());
// 			imageView1.setImage(img);
// 		}
		
// 	}
	
// 	@FXML
// 	public void facial(ActionEvent e) throws IOException{
		
// 		int id=facialRecognition(pathTest,  vectorMean,  pair, A, nb_images);
// 		System.out.println(id+" rir");
// 		for(int i=0;i<nb_images;i++) {
// 			System.out.println(images[i].getId());
// 			if(images[i].getId()==id) {
// 				imageView.setImage(images[i].getImg());
// 			}
// 		}
				
		
		
// 	}
// 	@FXML
// 	public void imgMean(ActionEvent e) throws IOException{
// 		// Calculate the mean image
// 					imageMean(vectorMean, outputImageMean, images[0].getHeight(), images[0].getWidth());
// 					Image img =new Image("file:"+outputImageMean);
// 					imageView.setImage(img);
// 	}
	
	
	
	
	
// 	@Override
// 	public void initialize(URL arg0, ResourceBundle arg1) {
// 		// TODO Auto-generated method stub
		
		
// 		 try {
			
// 			album= new Album(src);
			
			
// 			images=album.getImages();
// 			nb_images= images.length;
			
// 			// Calculate the mean vector
// 			 vectorMean = new Vector(images);
			
	        
// 	        // Calculate the matrix A
// 			 A = new Matrix(images, vectorMean);
	        
// 	        // Calculate Eigenfaces and EigenValues   
// 			pair = A.computeACP(nb_images);
// 			eigenfaces(pair, nb_images, images[0].getHeight(), images[0].getWidth(), outputEigenfaces);
			
// 		} catch (IOException e) {
// 			// TODO Auto-generated catch block
// 			e.printStackTrace();
// 		}
		 
		
		 
		 
// img.setImage(album.getImages()[0].getImg());
// img1.setImage(album.getImages()[1].getImg());
// img2.setImage(album.getImages()[2].getImg());
// img3.setImage(album.getImages()[3].getImg());
// img4.setImage(album.getImages()[4].getImg());

// String[] nom = new String[album.getImages().length];
// for(int i=0;i<album.getImages().length;i++) {
// 	nom[i]=album.getImages()[i].getFname();
// }

// listView.getItems().addAll(nom);
// String[] eigen = new String[pair.getY().length];
// for(int i=0;i<pair.getY().length;i++)eigen[i]=""+pair.getY()[i];
// eigenface.getItems().addAll(eigen);
		
		
		
		
// 	}
	
// 	private static void imageMean(Vector vectorMean, String output, int height, int width) throws IOException {
// 		// Calcule de l'image moyenne et write
//         ImageIO.write(vectorMean.convertToMatrix(height, width).convertToImage(), "jpg", new File(output));
// 	}

// 	private static void eigenfaces(Pair<Matrix, double[]> pair, int nb_images, int height, int width, String output) throws IOException {
// 		Matrix eigenFaces = pair.getX();
		
// 		for (int i=0; i<nb_images; i++) {
// 			Vector vect = new Vector(eigenFaces.getMatrix()[nb_images-i-1]);
// 			// deep copy to avoid modifying above matrix
// 			vect = vect.copie();
// 			vect.mapsatured(vect.min(), vect.max(), 0, 255);
// 			ImageIO.write(vect.convertToMatrix(height, width).convertToImage(), "jpg", new File(output+i+".jpg"));
// 		}
// 	}

// 	private static void comp(Pair<Matrix, double[]> pair, ImageRef[] images, Vector vectorMean, int nb_images, String output) throws IOException {
// 		Matrix eigenFaces = pair.getX();
// 		double[] eigenValues = pair.getY();
// 		int Ks[] = {1,2,3,4,5,6,7,8,9,10};
// 		double[][] xData = new double[nb_images*2][Ks.length];		
// 		double[] yData = new double[Ks.length];
		
// 		int l = 0;
// 		for (int K : Ks) {
// 			yData[l] = K;
// 			// caluclate reduced eigenspace + write eigenfaces
// 			Matrix F =  new Matrix(K, eigenFaces.getWidth());
// 			for (int i=0; i<K; i++) {
// 				F.getMatrix()[i] = eigenFaces.getMatrix()[nb_images-i-1];	
// 			}
			
// 			// reference Images
// 			Vector[] imagesVect = new Vector[nb_images*2*2];
// 			for (int i=0; i<nb_images; i++) {
// 				Vector v = images[i].projectImage(F, vectorMean);
// 				Vector un = new Vector (images[i].getImageVector());
// 				xData[i][l] = Vector.getError(v, un);
// 				v.map(v.min(), v.max(), 0, 255);
// 				un.map(un.min(), un.max(), 0, 255);
// 				imagesVect[i*2] = v;
// 				imagesVect[i*2+1] = un;

// 				ImageIO.write(v.convertToMatrix(images[0].getHeight(),images[0].getWidth()).convertToImage(), "jpg", new File(output + "ref_"+i+"_k_"+K+"_e_"+(Math.round(xData[i][l] * 100.0) / 100.0)+".jpg"));
// 				ImageIO.write(un.convertToMatrix(images[0].getHeight(),images[0].getWidth()).convertToImage(), "jpg", new File(output + "ref_"+i+"_J.jpg"));
// 			}
// 			l++;
// 		}
// 	}

	

// 	/* Facial recognition of a image  */
// 	private static int facialRecognition(String pathToRec, Vector vectorMean, Pair<Matrix, double[]> pair, Matrix ensVect, int nb_imgs) throws IOException{
// 		/* get the image */
// 		Photo imgToRec = new Photo(pathToRec);	
// 		/* get the clean face */
// 		Vector cleanVectToRec = (new Matrix(imgToRec, vectorMean)).convertToVector();
// 		//String outputImageMeanT = "res/dataBase/results/ImageMeanToTes.jpg";
//         //ImageIO.write(id.convertToMatrix(100, 100).convertToImage(), "jpg", new File(outputImageMeanT));

// 		/* get the eigenfaces */
// 		Matrix /*eigenFaces*/ F = pair.getX();
		



// 		int K = (int)(nb_imgs*20/100);
		
// 		Matrix eigenFaces =  new Matrix(K, F.getWidth());
// 		for (int i=0; i<K; i++) {
// 			eigenFaces.getMatrix()[i] = F.getMatrix()[ nb_imgs - i - 1];	
// 		}

		
// 		/* transform to a vector */
// 		Vector vectToRec = eigenFaces.projectVector(cleanVectToRec);
		
// 		//System.out.println("ensVect = " + ensVect.getHeigth());

// 		/* save the eigenVectors in a map*/
// 	    HashMap<Integer, Vector> map = new HashMap<>();
// 		for (int i = 0; i < ensVect.getHeigth(); i++) {			
// 			Vector vectorProj = eigenFaces.projectVector(ensVect.getCol(i));
// 			//System.out.println(vectorProj.getLength());
// 			map.put(i, vectorProj);
// 		}
		
// 		/* get the id of the closest image */
// 		int id = minDistance(vectToRec, map);
// 		return id;

		
// 	}
	
// 	/* Minimal distance between the image and an eigenVector*/ 
// 	private static int minDistance(Vector vectToRec, HashMap<Integer, Vector> map) {
//         double distance = -1;
//         double total =0;
//         int closestId = -1;
//         for (int id : map.keySet()) {
//             double newDistance = vectToRec.compareTo(map.get(id));
//             total += newDistance;
//             //System.out.println(newDistance);
//             if (distance < 0 || distance > newDistance) {
//                 distance = newDistance;
//                 closestId = id;
//             }
//         }
//         // for the precision
//         System.out.println("precision : " + (int)(100-(100*distance*map.size()/total)) + " %");
//         // return the closest id : maybe not the good one because return the id of the map and not the one of the closest image
// 		return closestId;
// 	}
// }


package acp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class JfxController {

    @FXML
    private Text distanceTxt;

    @FXML
    private Button facialRecognition;

    @FXML
    private Text imageFoundTxt;

    @FXML
    private Button imageMean;

    @FXML
    private Text imageToFound;

    @FXML
    private ImageView img1Preview;

    @FXML
    private ImageView img2Preview;

    @FXML
    private ImageView img3Preview;

    @FXML
    private ListView<?> listEigenfaces;

    @FXML
    private ListView<?> listImgView;

    @FXML
    private Button loadTest;

    @FXML
    private Text matchTxt;

    @FXML
    private Button next;

    @FXML
    private Button previous;

    @FXML
    private ImageView viewRefImage;

    @FXML
    private ImageView viewTestImage;

    @FXML
    private Button xchart;

    @FXML
    void facialRecognition(ActionEvent event) {
		
    }

    @FXML
    void imgMean(ActionEvent event) {

    }

    @FXML
    void loadRefImages(ActionEvent event) {

    }

    @FXML
    void nextImage(ActionEvent event) {

    }

    @FXML
    void previousImage(ActionEvent event) {

    }

    @FXML
    void xchart(ActionEvent event) {

    }

}
