package fr.upem.captcha.images;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexane LE GUERN, Jordan VILSAINT
 *
 */
public abstract class Categorie implements Images{

	public List<URL> getPhotos() {
		ArrayList<URL> newList = new ArrayList<URL>();
		return newList;
	}
	
	public List<URL> getRandomPhotosURL(int n) {
		ArrayList<URL> newList = new ArrayList<URL>();
		
		return newList;
	}
	
	public URL getRandomPhotoURL() {
		try {
			URL newUrl = new URL("Test");
			return newUrl;
		}

		catch(MalformedURLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public boolean isPhotoCorrect(URL url) {
		return false;
	}
}
