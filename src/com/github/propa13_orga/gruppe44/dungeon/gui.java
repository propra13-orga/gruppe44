package src.com.github.propa13_orga.gruppe44.dungeon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


public class gui extends JPanel implements ActionListener {

	Timer time; //notwendig fuer das wiederholte Laden des Bildes
	Image img;
	Image img2;
	int key; //Variable fuer Taste
	double laufhorizontal; //Variable fuer Geschwindigkeit nach Links bzw Rechts
	double laufvertikal; //Variable fuer Geschwindigkeit nach Oben bzw Unten
	int x_Bild; //Variable von bewegen
	int y_Bild;
	int x_Bild2 = 50;
	int y_Bild2 = 50;
	
	public gui (){
		
		key=0;
		laufhorizontal=0;
		laufvertikal=0;
		
		setFocusable(true);
		// man kann malen und drauf zugreifen
		ImageIcon u = new ImageIcon ("pixelbild.jpg");
		ImageIcon u2 = new ImageIcon ("pixelbild.jpg"); 
		//einfach nur ein Beispielbild
		img = u.getImage();
		img2 = u2.getImage();
		
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
		
		f.drawImage(img,x_Bild,y_Bild,null);
		f2.drawImage(img2,x_Bild2,y_Bild2,null);
		//Name, Position
	}
	
	public void bewegen (){
		if (x_Bild == 20 && 20<= y_Bild && y_Bild <=80) {
			laufhorizontal = -1;
			laufvertikal = 0;
			x_Bild += laufhorizontal;
			y_Bild += laufvertikal;
		}
		if (x_Bild == 80 && 20<= y_Bild && y_Bild <=80) {
			laufhorizontal = 1;
			laufvertikal = 0;
			x_Bild += laufhorizontal;
			y_Bild += laufvertikal;
		}
		if (y_Bild == 20 && 20<= x_Bild && x_Bild <=80) {
			laufhorizontal = 0;
			laufvertikal = -1;
			x_Bild += laufhorizontal;
			y_Bild += laufvertikal;
		}
		if (y_Bild == 80 && 20<= x_Bild && x_Bild <=80) {
			laufhorizontal = 0;
			laufvertikal = 1;
			x_Bild += laufhorizontal;
			y_Bild += laufvertikal;
		}
		else{
			x_Bild += laufhorizontal;
			y_Bild += laufvertikal;
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
 