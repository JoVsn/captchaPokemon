package fr.upem.captcha;

import java.net.URL;
import java.util.ArrayList;

import fr.upem.captcha.images.Category;

public class CaptchaManager {
	Category category;
	ArrayList<URL> validList;
	ArrayList<URL> fullList;
	
	CaptchaManager(Category category) {
		this.category = category;
		this.validList = new ArrayList<URL>();
		this.fullList = new ArrayList<URL>();
	}
	
	
}
