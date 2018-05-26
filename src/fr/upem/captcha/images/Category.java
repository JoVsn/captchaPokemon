package fr.upem.captcha.images;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Alexane LE GUERN, Jordan VILSAINT
 *
 */
public abstract class Category implements Images{
	
	/**
	 * Returns all images
	 * @return A list of url
	 */
	public ArrayList<URL> getPhotos() {
		ArrayList<URL> newList = new ArrayList<URL>();
		
		// On r�cup�re le r�pertoire courant
		String dirPath = this.getClass().getPackage().getName();
		dirPath = dirPath.replace(".", "/");
				
		List<String> files = new ArrayList<String>();
		
		try {
			// On parcourt tous les fichiers du r�pertoire courant, mais aussi des sous-r�pertoires
			Stream<Path> paths = Files.walk(Paths.get("./src/" + dirPath));
		    files = paths
		    	.map(Path::toString)
		        .filter(elem -> (elem.contains(".jpg") || elem.contains(".png"))) // On ne r�cup�re que les images
		        .collect(Collectors.toList());
		    paths.close();
			
		    // Enfin, on ajoute les images � notre liste d'images
			for (String file: files) {
				//String[] tmpTab = file.split("fr\\\\upem\\\\captcha\\\\images\\\\" + this.getClass().getSimpleName().toLowerCase() + "\\\\");
				
				// On r�cup�re la partie interessante de l'URL
				String[] tmpTab = file.split("fr\\\\upem\\\\captcha\\\\images\\\\");
				// C'est � partir de l'indice 1 qu'on a vraiment nos cat�gories, d'apr�s notre split
				StringBuilder stb = new StringBuilder();
				for (int i = 1; i < tmpTab.length; i++) {
					stb.append(tmpTab[i]);
				}
				String tmp = stb.toString();
				// On ne r�cup�re que la partie du chemin qui se trouve apr�s le nom de la cat�gorie 
				tmpTab = tmp.split(this.getClass().getSimpleName().toLowerCase());
				tmp = tmpTab[tmpTab.length-1];
				tmp = tmp.substring(1, tmp.length()); // On retire le "/" qui se trouve en d�but de chemin
				tmp = tmp.replace("\\", "/"); // On remplace les "\" g�nants par des /
				
				newList.add(this.getClass().getResource(tmp)); // Ajout de l'URL
			}
		} 
		catch(IOException e) {
			System.out.println("Erreur : " + e.getMessage());
		}
		return newList;
	}
	
	/**
	 * Returns a random image list
	 * @param n The number of images
	 * @return A list of url
	 * @throws IllegalStateException if there are no images in the category
	 * @throws IllegalArgumentException if the number of images isn't in the range of 1 to the size of image list
	 */
	public List<URL> getRandomPhotosURL(int n) throws IllegalStateException, IllegalArgumentException {
		ArrayList<URL> photos = getPhotos();
		ArrayList<URL> newList = new ArrayList<URL>();
		Random randomno = new Random();
		
		if (photos.size() == 0) {
			throw new IllegalStateException("La cat�gorie ne contient aucune image");
		}
		if (n <= 0 || n >= photos.size()) {
			throw new IllegalArgumentException("Le nombre d'images doit �tre compris entre 1 et " + photos.size());
		}
		
		// On r�cup�re un nombre d'images prises au hasard dans notre liste d'images
		int i = 0;
		while (i < n) {
			int tmpIndex = randomno.nextInt(photos.size());
			URL tmp = photos.get(tmpIndex);
			if (!newList.contains(tmp)) {
				newList.add(tmp);
				i++;
			}
		}
		
		return newList;
	}
	
	/**
	 * Returns a random image
	 * @return The url of the chosen image
	 */
	public URL getRandomPhotoURL() {
		// On demande une seule image al�atoire
		try {
			URL newURL = getRandomPhotosURL(1).get(0);
			return newURL;
		}
		catch(IllegalStateException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	/**
	 * Checks if an image belongs to the category
	 * @param url The url of the image
	 * @return A boolean
	 */
	public boolean isPhotoCorrect(URL url) {
		String categorieName = this.getClass().getPackage().getName().toLowerCase();
		return url.toString().replace("/", ".").toLowerCase().contains(categorieName);
	}

	
	/**
	 * Gives the complete name of the Category, e.g : the mother category name + the child category name
	 * @return categoryName the full name of the category
	 */
	public String getCompleteName() {
		// On ne garde que la partie int�ressante
		String categoryName = this.getClass().getPackage().getName().replace("fr.upem.captcha.images.", "");
		String[] categoryNameTab = categoryName.split("\\.");
		categoryName = "";
		StringBuilder stb = new StringBuilder();
		// On recr�e le nom complet de la cat�gorie, proprement, avec des majuscules
		for (int i = 0; i < categoryNameTab.length; i++) {
			String tmp = categoryNameTab[i].substring(0, 1).toUpperCase() + categoryNameTab[i].substring(1, categoryNameTab[i].length());
			categoryName = stb.append(tmp).append(" ").toString();
		}
		return categoryName;
		
	}
}
