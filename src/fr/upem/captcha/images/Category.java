package fr.upem.captcha.images;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * @author Alexane LE GUERN, Jordan VILSAINT
 *
 */
public abstract class Category implements Images{

	public ArrayList<URL> getPhotos() {
		ArrayList<URL> newList = new ArrayList<URL>();
		
		String dirPath = this.getClass().getPackageName();
		dirPath = dirPath.replace(".", "/");
		
		try (Stream<Path> paths = Files.walk(Paths.get("/" + dirPath))) {
		    paths
		        //.filter(Files::isRegularFile);
		        .forEach(System.out::println);
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
		
		for (int i = 0; i < n; i++) {
			int tmpIndex = randomno.nextInt(photos.size());
			URL tmp = photos.get(tmpIndex);
			if (!newList.contains(tmp))
				newList.add(tmp);
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
	
	public boolean isPhotoCorrect(URL url) {
		String categorieName = this.getClass().getPackageName().toLowerCase();
		return url.toString().toLowerCase().contains(categorieName);
	}
}
