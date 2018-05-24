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
		    paths.close();
			
		    // Enfin, on ajoute les images à notre liste d'images
			for (String file: files) {
				//String[] tmpTab = file.split("fr\\\\upem\\\\captcha\\\\images\\\\" + this.getClass().getSimpleName().toLowerCase() + "\\\\");
				
				// On récupère la partie interessante de l'URL
				String[] tmpTab = file.split("fr\\\\upem\\\\captcha\\\\images\\\\");
				// C'est à partir de l'indice 1 qu'on a vraiment nos catégories, d'après notre split
				StringBuilder stb = new StringBuilder();
				for (int i = 1; i < tmpTab.length; i++) {
					stb.append(tmpTab[i]);
				}
				String tmp = stb.toString();
				// On ne récupère que la partie du chemin qui se trouve après le nom de la catégorie 
				tmpTab = tmp.split(this.getClass().getSimpleName().toLowerCase());
				tmp = tmpTab[tmpTab.length-1];
				tmp = tmp.substring(1, tmp.length()); // On retire le "/" qui se trouve en début de chemin
				tmp = tmp.replace("\\", "/"); // On remplace les "\" gênants par des /
				
				newList.add(this.getClass().getResource(tmp)); // Ajout de l'URL
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
		
		if (photos.size() == 0) {
			throw new IllegalStateException("La catégorie ne contient aucune image");
		}
		if (n <= 0 || n >= photos.size()) {
			throw new IllegalArgumentException("Le nombre d'images doit être compris entre 1 et " + photos.size());
		}
		
		// On récupère un nombre d'images prises au hasard dans notre liste d'images
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
	 * Retourne une photo aléatoire
	 */
	public URL getRandomPhotoURL() {
		// On demande une seule image aléatoire
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
	 * Précise si une photo appartient bien à notre catégorie
	 */
	public boolean isPhotoCorrect(URL url) {
		String categorieName = this.getClass().getPackage().getName().toLowerCase();
		return url.toString().replace("/", ".").toLowerCase().contains(categorieName);
	}
}
