package fr.upem.captcha.images;

import java.net.URL;
import java.util.List;

public abstract class Categorie implements Images{

	public List<URL> getPhotos() {
		
	}
	
	public List<URL> getRandomPhotosURL(int n);
	
	public URL getRandomPhotoURL();
	
	public boolean isPhotoCorrect(URL url);
}
