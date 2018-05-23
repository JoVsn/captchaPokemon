package fr.upem.captcha.images;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Alexane LE GUERN, Jordan VILSAINT
 *
 */
public abstract class Categorie implements Images{

	public ArrayList<URL> getPhotos() {
		return null;
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
		String categorieName = this.getClass().getName().toLowerCase();
		return url.toString().contains(categorieName);
	}
}
