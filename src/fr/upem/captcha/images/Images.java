package fr.upem.captcha.images;

import java.net.URL;
import java.util.List;

/**
 * @author Alexane LE GUERN, Jordan VILSAINT
 *
 */
public interface Images {

	List<URL> getPhotos();
	
	List<URL> getRandomPhotosURL(int n);
	
	URL getRandomPhotoURL();
	
	boolean isPhotoCorrect(URL url);
	
}
