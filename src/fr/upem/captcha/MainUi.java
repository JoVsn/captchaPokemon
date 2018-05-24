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
		
		JFrame frame = new JFrame("Capcha"); // Cr�ation de la fen�tre principale
		
		GridLayout layout = createLayout();  // Cr�ation d'un layout de type Grille avec 4 lignes et 3 colonnes
		
		frame.setLayout(layout);  // affection du layout dans la fen�tre.
		frame.setSize(1024, 768); // d�finition de la taille
		frame.setResizable(false);  // On d�finit la fen�tre comme non redimentionnable
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Lorsque l'on ferme la fen�tre on quitte le programme.
		 
		
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
		
		//frame.add(new JTextArea("Cliquez n'importe o� ... juste pour tester l'interface !"));
		frame.add(new JTextArea("Veuillez s�lectionner les images qui contiennent : " + captchaManager.getCategory().getClass().getSimpleName()));
		
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
		return new JButton(new AbstractAction("V�rifier") { //ajouter l'action du bouton
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() { // faire des choses dans l'interface donc appeler cela dans la queue des �v�nements
					
					@Override
					public void run() { // c'est un runnable
						System.out.println("J'ai cliqu� sur Ok");
						System.out.println("Nb d'images valides: " + captchaManager.getValidList().size());
						System.out.println("Nb d'images cliqu�es: " + selectedImages.size());
						System.out.println("Images valides: " + captchaManager.getValidList());
						System.out.println("Images cliqu�es: " + selectedImages);
						if (selectedImages.size() == captchaManager.getValidList().size()) {
							if (captchaManager.compareLists(selectedImages)) {
								System.out.println("S�lection correcte\n omedeto gozaimasu~");
								ShowOptionPane("S�lection correcte\n Omedeto gozaimasu~", "Information");
							} else {
								System.out.println("S�lection incorrecte\n pr�t � relancer un captcha\n difficult�++");
								ShowOptionPane("S�lection incorrecte\n pr�t � relancer un captcha\n difficult�++", "Error");
							}
						} else {
							System.out.println("Vous avez s�lectionn� trop, ou pas assez d'images\n pr�t � relancer un captcha\n difficult�++");
							ShowOptionPane("Vous avez s�lectionn� trop, ou pas assez d'images\n pr�t � relancer un captcha\n difficult�++", "Warning");
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
		
		final JLabel label = new JLabel(new ImageIcon(sImage)); // cr�er le composant pour ajouter l'image dans la fen�tre
		
		label.addMouseListener(new MouseListener() { //Ajouter le listener d'�venement de souris
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
			public void mouseClicked(MouseEvent arg0) { //ce qui nous int�resse c'est lorsqu'on clique sur une image, il y a donc des choses � faire ici
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