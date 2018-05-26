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
	public List<URL> getPhotos();
	
	/**
	 * Returns a random image list
	 * @param n The number of images
	 * @return A list of url
	 * @throws IllegalStateException if there are no images in the category
	 * @throws IllegalArgumentException if the number of images isn't in the range of 1 to the size of image list
	 */
	public List<URL> getRandomPhotosURL(int n);
	
	/**
	 * Returns a random image
	 * @return An url
	 */
	public URL getRandomPhotoURL();
	
	/**
	 * Checks if an image belongs to the category
	 * @param url The url of the image
	 * @return A boolean
	 */
	public boolean isPhotoCorrect(URL url);
	
	/**
	 * Gives the complete name of the Category, e.g : the mother category name + the child category name
	 * @return categoryName the full name of the category
	 */
	public String getCompleteName();
	
}
