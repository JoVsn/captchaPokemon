package fr.upem.captcha;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import  fr.upem.captcha.images.dresseur.Dresseur;
import fr.upem.captcha.images.pokemon.Pokemon;

/**
 * @author Alexane LE GUERN, Jordan VILSAINT
 *
 */
public class MainUi {
	
	private static ArrayList<URL> selectedImages = new ArrayList<URL>();
	private static CaptchaManager captchaManager = new CaptchaManager();
	
	public static void main(String[] args) throws IOException {
		
		JFrame frame = new JFrame("Capcha"); // Création de la fenêtre principale
		
		GridLayout layout = createLayout();  // Création d'un layout de type Grille avec 4 lignes et 3 colonnes
		
		frame.setLayout(layout);  // affection du layout dans la fenêtre.
		frame.setSize(1024, 768); // définition de la taille
		frame.setResizable(false);  // On définit la fenêtre comme non redimentionnable
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Lorsque l'on ferme la fenêtre on quitte le programme.
		 
		
		JButton okButton = createOkButton();

		/* NE PAS SUPPRIMER CES LIGNES */
		
		Pokemon pok = new Pokemon();
		pok.getPhotos();

		
		//CaptchaManager cm = new CaptchaManager();
				
		List<URL> list = captchaManager.getFullList();
		//List<URL> list = captchaManager.getValidList();
		for (URL url: list) {
			frame.add(createLabelImage(url.toString().replaceAll("(.*)/fr/upem/captcha/", "./")));
		}	
		
		/* NE PAS SUPPRIMER CES LIGNES */
		
		//frame.add(new JTextArea("Cliquez n'importe où ... juste pour tester l'interface !"));
		frame.add(new JTextArea("Veuillez sélectionner les images qui contiennent : " + captchaManager.getCategory().getClass().getSimpleName()));
		
		frame.add(okButton);
		
		frame.setVisible(true);
	}
	
	/**
	 * Creates the grid layout
	 * @return grid layout
	 */
	private static GridLayout createLayout(){
		return new GridLayout(4,3);
	}
	
	/**
	 * Creates ok button and checks results
	 * @return
	 */
	private static JButton createOkButton(){
		return new JButton(new AbstractAction("Vérifier") { //ajouter l'action du bouton
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() { // faire des choses dans l'interface donc appeler cela dans la queue des évènements
					
					@Override
					public void run() { // c'est un runnable
						System.out.println("J'ai cliqué sur Ok");
						System.out.println("Nb d'images valides: " + captchaManager.getValidList().size());
						System.out.println("Nb d'images cliquées: " + selectedImages.size());
						System.out.println("Images valides: " + captchaManager.getValidList());
						System.out.println("Images cliquées: " + selectedImages);
						if (selectedImages.size() == captchaManager.getValidList().size()) {
							if (captchaManager.compareLists(selectedImages)) {
								System.out.println("Sélection correcte\n omedeto gozaimasu~");
								ShowOptionPane("Sélection correcte\n Omedeto gozaimasu~", "Information");
							} else {
								System.out.println("Sélection incorrecte\n prêt à relancer un captcha\n difficulté++");
								ShowOptionPane("Sélection incorrecte\n prêt à relancer un captcha\n difficulté++", "Error");
							}
						} else {
							System.out.println("Vous avez sélectionné trop, ou pas assez d'images\n prêt à relancer un captcha\n difficulté++");
							ShowOptionPane("Vous avez sélectionné trop, ou pas assez d'images\n prêt à relancer un captcha\n difficulté++", "Warning");
						}
					}
				});
			}
		});
	}
	
	/**
	 * Display an option pane
	 * @param String message
	 * @param String type
	 */
	private static void ShowOptionPane(String message, String type) {
		JOptionPane jop = new JOptionPane();
		switch(type) {
		case "Information":
			jop.showMessageDialog(null, message, type, JOptionPane.INFORMATION_MESSAGE);
			break;
		case "Warning":
			jop.showMessageDialog(null, message, type, JOptionPane.WARNING_MESSAGE);
			break;
		case "Error":
			jop.showMessageDialog(null, message, type, JOptionPane.ERROR_MESSAGE);
			break;
		default:
			jop.showMessageDialog(null, message, type, JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/**
	 * Image processing
	 * @param imageLocation
	 * @return JLabel
	 * @throws IOException
	 */
	private static JLabel createLabelImage(String imageLocation) throws IOException{
		
		final URL url = MainUi.class.getResource(imageLocation); //Aller chercher les images !! IMPORTANT 
		
		System.out.println(url); 
		
		BufferedImage img = ImageIO.read(url); //lire l'image
		Image sImage = img.getScaledInstance(1024/3,768/4, Image.SCALE_SMOOTH); //redimentionner l'image
		
		final JLabel label = new JLabel(new ImageIcon(sImage)); // créer le composant pour ajouter l'image dans la fenêtre
		
		label.addMouseListener(new MouseListener() { //Ajouter le listener d'évenement de souris
			private boolean isSelected = false;
			
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
		
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}
			
			
			/**
			 * Creates interactions on click
			 */
			@Override
			public void mouseClicked(MouseEvent arg0) { //ce qui nous intéresse c'est lorsqu'on clique sur une image, il y a donc des choses à faire ici
				EventQueue.invokeLater(new Runnable() { 
					
					@Override
					public void run() {
						if(!isSelected){
							label.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
							isSelected = true;
							selectedImages.add(url);
						}
						else {
							label.setBorder(BorderFactory.createEmptyBorder());
							isSelected = false;
							selectedImages.remove(url);
						}
						
					}
				});
				
			}
		});
		
		return label;
	}
}