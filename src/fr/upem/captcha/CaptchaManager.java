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
	private ArrayList<Images> allCategories;
	private ArrayList<URL> validList;
	private ArrayList<URL> fullList;
	
	
	CaptchaManager() {
		this.category = category;
		this.validList = new ArrayList<URL>();
		this.fullList = new ArrayList<URL>();
		this.allCategories = new ArrayList<Images>();
		
		try {
			fillCategories(2);
		}
		catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
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
	 * Utiliser le Forname
	 * @return
	 */

	public Images getCategory() {
		return category;
	}

	public ArrayList<URL> getValidList() {
		return validList;
	}

	public ArrayList<URL> getFullList() {
		return fullList;
	}

	public ArrayList<Images> getAllCategories() {
		return allCategories;
	}
	
	
	
	
}
