package fr.upem.captcha;

import java.net.URL;
import java.util.ArrayList;

import fr.upem.captcha.images.Categorie;

public class GridManager {
	Categorie categorie;
	ArrayList<URL> validList;
	ArrayList<URL> invalidList;
	
	GridManager(Categorie categorie) {
		this.categorie = categorie;
		this.validList = new ArrayList<URL>();
		this.invalidList = new ArrayList<URL>();
	}
	
	
}
