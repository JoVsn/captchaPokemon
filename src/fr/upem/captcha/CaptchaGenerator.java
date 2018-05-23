package fr.upem.captcha;

import java.net.URL;
import java.util.ArrayList;

import fr.upem.captcha.images.Category;

public class CaptchaGenerator {
	Category category;
	ArrayList<URL> validList;
	ArrayList<URL> fullList;
	
	CaptchaGenerator(Category categorie) {
		this.category = categorie;
		this.validList = new ArrayList<URL>();
		this.fullList = new ArrayList<URL>();
	}
	
	
}
