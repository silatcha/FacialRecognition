
package acp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Album {

    private int indexCourant; // index de l'image courante
    private ImageRef[] images;

    /**
     * Initalise l'album en rajoutant les photos correspondantes a tous les fichiers
     * du dossier repertoire
     * 
     * @param repertoire
     *                   le dossier contenant les photos de l'album
     * @throws IOException
     */
    public Album(String repertoire) throws IOException {

        this.images = init(repertoire);

    }

    public ImageRef[] getImages() {
        return images;
    }

    /**
     * redimensionne la photo courante de l'album avec zoom pour facteur de zoom.
     * 
     * @param zoom
     *             le facteur de zoom a appliquer a la photo courante
     */
    public void redimensionnerPhotoCourante(Float zoom) {
        // this.getPhotoCourante().redimensionner(zoom);
    }

    public ImageRef[] init(String src) throws IOException {
        File file = new File(src);
        // long nb_images = file.list().length;
        long nb_images = 0;
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                for (String str : f.list()) {
                    nb_images++;
                }
            }
        }

        ImageRef[] images = new ImageRef[(int) nb_images];
        int cpt = 0;

        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                for (String str : f.list()) {

                    images[cpt] = new ImageRef(src + "/" + f.getName() + "/" + str, f.getName().split("_")[1],
                            f.getName().split("_")[0], Integer.parseInt(str.split("\\.")[0]));
                    cpt++;
                }
            }
        }
        return images;
    }
}
