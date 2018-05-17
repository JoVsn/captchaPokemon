package fr.upem.captcha.images;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

		catch(Exception e) {
			System.out.println(e.getMessage());
			return new URL("Test");
		}
	}
	
	public boolean isPhotoCorrect(URL url) {
		return false;
	}
}
