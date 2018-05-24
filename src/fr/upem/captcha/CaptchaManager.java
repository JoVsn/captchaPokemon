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
	private int difficulty = 2;
	
	CaptchaManager() {
		this.validList = new ArrayList<URL>();
		this.fullList = new ArrayList<URL>();
		this.allCategories = new ArrayList<Images>();
		
		try {
			fillCategories(difficulty);
			chooseCategory();
			fillValidList();
			fillFullList();
		}
		catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IllegalStateException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Fills the categories by browsing through the folders
	 * @param int difficulty
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void fillCategories(int difficulty) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String dirPath = this.getClass().getPackage().getName();
		dirPath = dirPath.replace(".", "/");
		String basePath = ".\\src\\fr\\upem\\captcha\\images\\";
		
		ArrayList<String> categoriesName = new ArrayList<String>();
		List<String> filesPaths = new ArrayList<String>();
		List<Class<?>> classList = new ArrayList<Class<?>>();
		
		try {
			// On parcourt tous les fichiers du répertoire courant, mais aussi des sous-répertoires
			Stream<Path> paths = Files.walk(Paths.get("./src/" + dirPath), difficulty);
		    filesPaths = paths
		    		.map(Path::toString)
		    		.filter(elem -> elem.contains(basePath))
		    		.collect(Collectors.toList());
		    paths.close();
		    
		    for (String path: filesPaths) {
		    	File tmp = new File(path);
		    	if (tmp.isDirectory()) {
		    		// On enlève les partie qui servent à rien et formate l'URL de la bonne forme
		    		path = path.replace("\\", ".").replace("..src.", "");
		    		String[] pathElems = path.split("\\.");
		    		
		    		// On crée le nom de la classe complet
		    		String className = pathElems[pathElems.length-1];
		    		className = className.substring(0, 1).toUpperCase() + className.substring(1);
		    		path += "." + className;
		    		categoriesName.add(path);
		    	}
		    }
		    
		    // On crée une liste de classe
		    for (String className: categoriesName) {
		    	try {
		    		Class<?> newClass = Class.forName(className);
		    		classList.add(newClass);
		    	}
		    	catch(ClassNotFoundException e) {
		    		e.getMessage();
		    	}
		    }
		    
		    // Pour chaque classe, on crée une instance de celle-ci que l'on stocke dans notre liste de catégories
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
	 * @param ArrayList<URL> selectedImages
	 * @return boolean
	 */
	public boolean compareLists(ArrayList<URL> selectedImages) {
		boolean ctrl = false;
		for (int i = 0; i < this.validList.size(); ++i) {
			System.out.println(this.validList.get(i));
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
	 */
	public void chooseCategory() {
		int nbCategories = allCategories.size();
		if (nbCategories >= 2) {
			Random randomno = new Random();
			int index = randomno.nextInt(nbCategories);
			category = allCategories.get(index);
		}
		else {
			throw new IllegalStateException("Il n'y a pas assez de catégories pour créer le captcha");
		}
	}
	
	/**
	 * Fills the valid list of images randomly
	 */
	public void fillValidList() {
		Random randomno = new Random();
		int nbImagesOk = randomno.nextInt(4) + 1; // On sélectionne entre 1 et 3 images

		try {
			validList = category.getRandomPhotosURL(nbImagesOk);
		}
		catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Fills the reste of the list of images
	 */
	public void fillFullList() {
		// On ajoute la liste d'éléments valides à la liste complète
		for (int i = 0; i < validList.size(); i++) {
			fullList.add(validList.get(i));
		}
		
		// On récupère toutes les autres catégories, attention il ne faut pas récupérer les catégories filles de celle en cours, on a déjà ses images
		List<Images> otherCategories = new ArrayList<Images>();
		for(Images i: allCategories) {
			boolean contains = i.getClass().getPackage().getName().contains(category.getClass().getPackage().getName());
			boolean equals = i.getClass().getPackage().getName().equals(category.getClass().getPackage().getName());
			if (!equals && !contains) {
				otherCategories.add(i);
			}
		}
		
		// On remplit la liste complète avec des images d'autres catégories
		int j = fullList.size();
		Random randomno = new Random();
		while (j < maxImg) {
			int tmpIndex = randomno.nextInt(otherCategories.size());
			Images tmpCategory = otherCategories.get(tmpIndex);
			URL newUrl = tmpCategory.getRandomPhotoURL();
			// Précaution pour qu'une image ne soit pas ajoutée sous prétexte qu'elle fasse partie de la catégorie "mère"
			if (!fullList.contains(newUrl) && !category.isPhotoCorrect(newUrl)) {
				fullList.add(newUrl);
				j++;
			}
		}
		// Mélange de la liste d'images
		Collections.shuffle(fullList);
	}
	
	public void increaseDifficulty() {
		difficulty++;
	}
	
	/**
	 * Get the category
	 * @return Images
	 */
	public Images getCategory() {
		return category;
	}

	/**
	 * Get the valid list of images
	 * @return List<URL>
	 */
	public List<URL> getValidList() {
		return validList;
	}

	/**
	 * Get the full list of images
	 * @return List<URL>
	 */
	public List<URL> getFullList() {
		return fullList;
	}

	/**
	 * Get all categories
	 * @return List<Images>
	 */
	public List<Images> getAllCategories() {
		return allCategories;
	}	
}
