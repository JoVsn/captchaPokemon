package fr.upem.captcha;

import java.net.URL;
import java.util.ArrayList;

import fr.upem.captcha.images.Category;

public class GridManager {
	Category categorie;
	ArrayList<URL> validList;
	ArrayList<URL> invalidList;
	
	GridManager(Category categorie) {
		this.categorie = categorie;
		this.validList = new ArrayList<URL>();
		this.invalidList = new ArrayList<URL>();
	}
	
	
}
