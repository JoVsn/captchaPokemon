package fr.upem.captcha;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.upem.captcha.images.Images;

/**
 * @author Alexane LE GUERN, Jordan VILSAINT
 *
 */
public class CaptchaManager {
	private Images category;
	private List<Images> allCategories;
	private List<URL> validList;
	private List<URL> fullList;
	private int maxImg = 9;
	private int difficulty = 1;
	
	CaptchaManager() {
		this.validList = new ArrayList<URL>();
		this.fullList = new ArrayList<URL>();
		this.allCategories = new ArrayList<Images>();

		initializeDatas();
	}
	
	/**
	 * Initializes datas of the lists depending on the chosen category
	 */
	public void initializeDatas() {
		try {
			fillCategories(difficulty);
			chooseCategory();
			fillValidList();
			fillFullList();
		}
		catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IllegalArgumentException | IllegalStateException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Restarts the captcha's datas, clears then initializes lists
	 */
	public void restart() {	
		validList.clear();
		fullList.clear();
		try {
			fillCategories(difficulty);
			chooseCategory();
			fillValidList();
			fillFullList();
		}
		catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IllegalArgumentException | IllegalStateException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Fills the categories by browsing through the folders
	 * @param difficulty The difficulty level
	 * @throws ClassNotFoundException if the class is not found
	 * @throws InstantiationException if the instance can not be created
	 * @throws IllegalAccessException if access problem
	 * @throws IllegalArgumentException if argument problem
	 */
	public void fillCategories(int difficulty) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException {
		if (difficulty < 1) {
			throw new IllegalArgumentException("La difficult� doit au moins �tre � 2");
		}
		
		String dirPath = this.getClass().getPackage().getName();
		dirPath = dirPath.replace(".", "/");
		String basePath = ".\\src\\fr\\upem\\captcha\\images\\";
		
		ArrayList<String> categoriesName = new ArrayList<String>();
		List<String> filesPaths = new ArrayList<String>();
		List<Class<?>> classList = new ArrayList<Class<?>>();
		
		try {
			// On parcourt tous les fichiers du r�pertoire courant, mais aussi des sous-r�pertoires
			Stream<Path> paths = Files.walk(Paths.get("./src/" + dirPath), difficulty+1);
		    filesPaths = paths
		    		.map(Path::toString)
		    		.filter(elem -> elem.contains(basePath))
		    		.collect(Collectors.toList());
		    paths.close();
		    
		    // On r�cup�re toutes les cat�gories existantes � partir des noms de packages
		    for (String path: filesPaths) {
		    	File tmp = new File(path);
		    	if (tmp.isDirectory()) {
		    		// On enl�ve les partie qui servent � rien et formate l'URL de la bonne forme
		    		path = path.replace("\\", ".").replace("..src.", "");
		    		String[] pathElems = path.split("\\.");
		    		
		    		// On cr�e le nom de la classe complet
		    		String className = pathElems[pathElems.length-1];
		    		className = className.substring(0, 1).toUpperCase() + className.substring(1);
		    		path += "." + className;
		    		categoriesName.add(path);
		    	}
		    }
		    
		    // On cr�e une liste de classe, une pour chaque cat�gorie
		    for (String className: categoriesName) {
		    	try {
		    		Class<?> newClass = Class.forName(className);
		    		classList.add(newClass);
		    	}
		    	catch(ClassNotFoundException e) {
		    		e.getMessage();
		    	}
		    }
		    
		    // Pour chaque classe, on cr�e une instance de celle-ci que l'on stocke dans notre liste de cat�gories
		    for (Class<?> cls: classList) {
		    	Class<?> tmp = Class.forName(cls.getTypeName());
		    	Object category = tmp.newInstance();
		    	allCategories.add((Images)category);
		    }
		} 
		catch(IOException e) {
			System.out.println("Erreur : " + e.getMessage());
		}
	}
	
	/**
	 * Compares two Array lists of the same size
	 * @param selectedImages The list of images
	 * @return A boolean, true if the lists are equal
	 */
	public boolean compareLists(ArrayList<URL> selectedImages) {
		boolean ctrl = false;
		for (int i = 0; i < this.validList.size(); ++i) {
			if (selectedImages.contains(this.validList.get(i))) {
				ctrl = true;
			} else {
				return false;
			}
		}
		return ctrl;
	}

	/**
	 * Chooses a category randomly
	 * @throws IllegalStateException if there are not enough categories
	 */
	public void chooseCategory() throws IllegalStateException {
		int nbCategories = allCategories.size();
		if (nbCategories >= 2) {
			Random randomno = new Random();
			int index = randomno.nextInt(nbCategories);
			category = allCategories.get(index);
		}
		else {
			throw new IllegalStateException("Il n'y a pas assez de cat�gories pour cr�er le captcha");
		}
	}
	
	/**
	 * Fills the valid list of images randomly
	 */
	public void fillValidList() {
		Random randomno = new Random();
		int nbImagesOk = randomno.nextInt(4) + 1; // On s�lectionne entre 1 et 3 images

		try {
			validList = category.getRandomPhotosURL(nbImagesOk);
		}
		catch(IllegalArgumentException | IllegalStateException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Fills the rest of the list of images
	 */
	public void fillFullList() {
		// On ajoute la liste d'�l�ments valides � la liste compl�te
		for (int i = 0; i < validList.size(); i++) {
			fullList.add(validList.get(i));
		}
		
		// On r�cup�re toutes les autres cat�gories, attention il ne faut pas r�cup�rer les cat�gories filles de celle en cours, on a d�j� ses images
		List<Images> otherCategories = new ArrayList<Images>();
		for(Images i: allCategories) {
			boolean contains = i.getClass().getPackage().getName().contains(category.getClass().getPackage().getName());
			boolean equals = i.getClass().getPackage().getName().equals(category.getClass().getPackage().getName());
			// Si la cat�gorie est la fille de la cat�gorie d�j� selectionn�e, on ne la rajoute pas dans la liste des autres cat�gories
			if (!equals && !contains) {
				otherCategories.add(i);
			}
		}
		
		// On remplit la liste compl�te avec des images d'autres cat�gories
		int j = fullList.size();
		Random randomno = new Random();
		while (j < maxImg) {
			int tmpIndex = randomno.nextInt(otherCategories.size());
			Images tmpCategory = otherCategories.get(tmpIndex);
			URL newUrl = tmpCategory.getRandomPhotoURL();
			// Pr�caution pour qu'une image ne soit pas ajout�e sous pr�texte qu'elle fasse partie de la cat�gorie "m�re"
			if (!fullList.contains(newUrl) && !category.isPhotoCorrect(newUrl)) {
				fullList.add(newUrl);
				j++;
			}
		}
		// M�lange de la liste d'images
		Collections.shuffle(fullList);
	}
	
	/**
	 * Increase the difficulty attribute by 1
	 */
	public void increaseDifficulty() {
		difficulty++;
	}
	
	/**
	 * Get the category
	 * @return An instance of Images
	 */
	public Images getCategory() {
		return category;
	}

	/**
	 * Get the valid list of images
	 * @return A list of url
	 */
	public List<URL> getValidList() {
		return validList;
	}

	/**
	 * Get the full list of images
	 * @return A list of url
	 */
	public List<URL> getFullList() {
		return fullList;
	}

	/**
	 * Get all categories
	 * @return A list of images
	 */
	public List<Images> getAllCategories() {
		return allCategories;
	}	
}
