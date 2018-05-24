package fr.upem.captcha;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.upem.captcha.images.Category;
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
		}
		catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IllegalStateException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void fillCategories(int difficulty) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String dirPath = this.getClass().getPackage().getName();
		dirPath = dirPath.replace(".", "/");
		String basePath = ".\\src\\fr\\upem\\captcha\\images\\";
		
		ArrayList<String> categoriesName = new ArrayList<String>();
		List<String> filesPaths = new ArrayList<String>();
		List<Class> classList = new ArrayList<Class>();
		
		try {
			// On parcourt tous les fichiers du répertoire courant, mais aussi des sous-répertoires
			Stream<Path> paths = Files.walk(Paths.get("./src/" + dirPath), difficulty);
		    filesPaths = paths
		    		.map(Path::toString)
		    		.filter(elem -> elem.contains(basePath))
		    		.collect(Collectors.toList());
		    
		    for (String path: filesPaths) {
		    	File tmp = new File(path);
		    	if (tmp.isDirectory()) {
		    		path = path.replace("\\", ".").replace("..src.", "");
		    		String[] pathElems = path.split("\\.");
		    		
		    		String className = pathElems[pathElems.length-1];
		    		className = className.substring(0, 1).toUpperCase() + className.substring(1);
		    		path += "." + className;
		    		System.out.println(path);
		    		categoriesName.add(path);
		    	}
		    }
		    
		    for (String className: categoriesName) {
		    	try {
		    		Class newClass = Class.forName(className);
		    		classList.add(newClass);
		    	}
		    	catch(ClassNotFoundException e) {
		    		e.getMessage();
		    	}
		    }
		    
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
	 * Compare deux listes de même taille
	 * @param selectedImages
	 * @return boolean
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
	
	public void fillValidList() {
		Random randomno = new Random();
		int nbImagesOk = randomno.nextInt(3) + 1; // On sélectionne entre 1 et 3 images

		try {
			validList = category.getRandomPhotosURL(nbImagesOk);
		}
		catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void fillFullList() {
		for (int i = 0; i < validList.size(); i++) {
			fullList.add(validList.get(i));
		}
		int j = fullList.size();
		Random randomno = new Random();
		while (j < maxImg) {
			int tmpIndex = randomno.nextInt(allCategories.size()-1);
			Images tmpCategory = allCategories.get(tmpIndex);
		}
	}
	
	public Images getCategory() {
		return category;
	}

	public List<URL> getValidList() {
		return validList;
	}

	public List<URL> getFullList() {
		return fullList;
	}

	public List<Images> getAllCategories() {
		return allCategories;
	}
	
	
	
	
}
