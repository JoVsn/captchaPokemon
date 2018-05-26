package fr.upem.captcha.images;

import java.net.URL;
import java.util.List;

/**
 * @author Alexane LE GUERN, Jordan VILSAINT
 *
 */
public interface Images {

	/**
	 * Returns all images
	 * @return A list of url
	 */
	List<URL> getPhotos();
	
	/**
	 * Returns a random image list
	 * @param n The number of images
	 * @return A list of url
	 * @throws IllegalStateException if there are no images in the category
	 * @throws IllegalArgumentException if the number of images isn't in the range of 1 to the size of image list
	 */
	List<URL> getRandomPhotosURL(int n);
	
	/**
	 * Returns a random image
	 * @return An url
	 */
	URL getRandomPhotoURL();
	
	/**
	 * Checks if an image belongs to the category
	 * @param url The url of the image
	 * @return A boolean
	 */
	boolean isPhotoCorrect(URL url);
	
}
