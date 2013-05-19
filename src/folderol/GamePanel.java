package folderol;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Houston houston;
	private Player player;
	private Map map;
	
	private Font font;
	private Color bgColor;
	
	public GamePanel(Houston houston) {
		this.houston = houston;
		this.player = houston.player;
		this.map = houston.map;
		
		// Setzt die Schrift fuer die Konsolenausgaben
		font = new Font("Arial", Font.BOLD, 12);
		// Setzt die Hintergrundfarbe
		bgColor = new Color(240, 240, 240);
	}

	@Override
	protected void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		// g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Hier wird gezeichnet
		
		// Zeichnet den Hintergrund
		g.setColor(bgColor);
		g.fillRect(0, 0, houston.width, houston.height);

		// Zeichne die Karte
		map.drawObjects(g);

		// Zeichne den Player
		player.drawObjects(g);
		
		// Zeichnet Helfer zum Verstaendnis der Kollisionserkennung
		// houston.logic.drawObjects(g);
		
		// Zeichnet Konsolenausgaben
		g.setColor(Color.BLACK);
		g.setFont(font);
		g.drawString("Konsolenausgaben hier", 16, 20);
		// Zeichnet die gerundete aktuelle Position des Player
		g.drawString("Pos " + "x:"+(int)player.getX()+" y:"+(int)player.getY(), 208, 20);
		
		// Zeichnet die aktuellen FPS (Frames Per Second)
		g.drawString("FPS: " + houston.fps, 712, 20);
		
		
		g.dispose();
	} // Ab hier ist Schluss mit Zeichnen

}
