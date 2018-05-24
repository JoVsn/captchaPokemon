package fr.upem.captcha.images;

import java.io.File;
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

	public ArrayList<URL> getPhotos() {
		ArrayList<URL> newList = new ArrayList<URL>();
		
		// On récupère le répertoire courant
		String dirPath = this.getClass().getPackage().getName();
		dirPath = dirPath.replace(".", "/");
				
		List<String> files = new ArrayList<String>();
		
		try {
			// On parcourt tous les fichiers du répertoire courant, mais aussi des sous-répertoires
			Stream<Path> paths = Files.walk(Paths.get("./src/" + dirPath));
		    files = paths
		    	.map(Path::toString)
		        .filter(elem -> (elem.contains(".jpg") || elem.contains(".png"))) // On ne récupère que les images
		        .collect(Collectors.toList());
			
		    // Enfin, on ajoute les images à notre liste d'images
			for (String file: files) {
				String[] tmpTab = file.split("\\\\");
				String tmp = tmpTab[tmpTab.length-1];
				newList.add(this.getClass().getResource(tmp));
			}
		} 
		catch(IOException e) {
			System.out.println("Erreur : " + e.getMessage());
		}
		return newList;
	}
	
	/**
	 * Retourne une liste de photo aléatoire
	 */
	public List<URL> getRandomPhotosURL(int n) {
		ArrayList<URL> photos = getPhotos();
		ArrayList<URL> newList = new ArrayList<URL>();
		Random randomno = new Random();
		
		if (n <= 0 || n >= photos.size()) {
			throw new IllegalArgumentException("Le nombre d'images doit être compris entre 1 et " + photos.size());
		}
		
		// On récupère un nombre d'images prises au hasard dans notre liste d'images
		for (int i = 0; i < n; i++) {
			int tmpIndex = randomno.nextInt(photos.size());
			URL tmp = photos.get(tmpIndex);
			if (!newList.contains(tmp))
				newList.add(tmp);
			else
				i--;
		}
		
		return newList;
	}
	
	/**
	 * Retourne une photo aléatoire
	 */
	public URL getRandomPhotoURL() {
		URL newURL = getRandomPhotosURL(1).get(0);
		return newURL;
	}
	
	/**
	 * Précise si une photo appartient bien à notre catégorie
	 */
	public boolean isPhotoCorrect(URL url) {
		String categorieName = this.getClass().getPackage().getName().toLowerCase();
		return url.toString().toLowerCase().contains(categorieName);
	}
}
