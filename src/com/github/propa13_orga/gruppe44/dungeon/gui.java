package src.com.github.propa13_orga.gruppe44.dungeon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


public class gui extends JPanel implements ActionListener {

	Timer time; //notwendig fuer das wiederholte Laden des Bildes
	Image spielerBild;
	Image wandBild;
	int key; //Variable fuer Taste
	double laufhorizontal; //Variable fuer Geschwindigkeit nach Links bzw Rechts
	double laufvertikal; //Variable fuer Geschwindigkeit nach Oben bzw Unten
	int x_spieler; //Variable von bewegen
	int y_spieler;
	int x_wand = 300;
	int y_wand = 300;
	
	public gui (){
		
		key=0;
		laufhorizontal=0;
		laufvertikal=0;
		
		setFocusable(true);
		// man kann malen und drauf zugreifen
		ImageIcon u = new ImageIcon ("pixelbild.jpg");
		ImageIcon u2 = new ImageIcon ("pixelbild.jpg");
		//einfach nur ein Beispielbild
		spielerBild = u.getImage();
		wandBild = u2.getImage();
		
		addKeyListener(new AL());
		
		time = new Timer(5,this); //Methode wird alle 5 Millisekunden wiederaufgerufen
		time.start(); 
	}

	public void actionPerformed(ActionEvent e){ //Methode vom Timer
		bewegen();
		repaint();
	}
	
	public void paint (Graphics g){
		
		super.paint(g);
		Graphics2D f = (Graphics2D) g;
		Graphics2D f2 = (Graphics2D) g;
		//Methode zeichnet sonst nur Linien, notwendig um Bilder darstellen zu können
		
		f.drawImage(spielerBild,x_spieler,y_spieler,null);
		f2.drawImage(wandBild,x_wand,y_wand,null);
		//Name, Position
	}
	
	public void bewegen (){
		
		//Variablen für die Positionen von Spieler und Wand
		int neue_position_spieler_x1 = x_spieler + (int) laufhorizontal;			//Koordinate des Spielers bei horizontaler Bewegung
		int neue_position_spieler_y1 = y_spieler + (int) laufvertikal;				//Koordinate des Spielers bei vertikaler Bewegung
		int neue_position_spieler_x2 = neue_position_spieler_x1 + spielerBild.getWidth(null); 		// Korrdinate der Breite des Spielers bei Bewegung
		int neue_position_spieler_y2 = neue_position_spieler_y1 + spielerBild.getHeight(null);		//Korrdinate der Höhe des Spielers bei Bewegung
		int position_wand_x1 = x_wand;																//Koordinaten der Wand, mit Höhe und Breite
		int position_wand_x2 = x_wand + wandBild.getWidth(null);
		int position_wand_y1 = y_wand;
		int position_wand_y2 = y_wand + wandBild.getHeight(null);
		
		
				//Abfrage, ob eine Kollision stadtfindet
		boolean hatKollision = (neue_position_spieler_x2 >= position_wand_x1 && neue_position_spieler_x1 <= position_wand_x2 &&
				neue_position_spieler_y2 >= position_wand_y1 && neue_position_spieler_y1 <= position_wand_y2);
		if (!hatKollision) {
			x_spieler = neue_position_spieler_x1; //Aktualisierung der neuen Position
			y_spieler = neue_position_spieler_y1;
		}
		
	
		
	}
	
	private class AL extends KeyAdapter{
		public AL(){
			
		}//hier passiert nix
		
		public void keyPressed(KeyEvent e){ // wird beim Druecken irgeneiner Taste aufgerufen
			key=e.getKeyCode();
			
			if (key == KeyEvent.VK_LEFT){ //Linkstaste wird gedrückt
				laufhorizontal = -1; // legt Laufgeschwindigkeit fest
			}
			
			if (key == KeyEvent.VK_RIGHT){//Rechtstaste wird gedrückt
				laufhorizontal = 1;
			}
			
			if (key == KeyEvent.VK_UP){//Obentaste wird gedrückt
				laufvertikal = -1;
			}
			
			if (key == KeyEvent.VK_DOWN){//Untentaste wird gedrückt
				laufvertikal = 1;
			}
		}
		
		public void keyReleased (KeyEvent e){ //wird beim Loslassen der Taste aufgerufen
			key = e.getKeyCode ();
			
			if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN){
				laufhorizontal = 0;
				laufvertikal = 0;
			}
		}
		
	}
}
 