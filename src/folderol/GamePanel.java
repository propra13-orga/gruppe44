package folderol;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	Timer timer;
	Houston houston;
	Player player;
	Map map;
	
	public GamePanel(Houston houston) {
		this.houston = houston;
		this.player = houston.player;
		this.map = houston.map;

	}

	@Override
	protected void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		Graphics2D g = (Graphics2D) gr;
		// Hier wird gezeichnet

		g.setColor(new Color(230, 230, 230));
		g.fillRect(0, 0, houston.width, houston.height); // Hintergrund

		// Zeichne die Karte
		map.drawObjects(g);

		// Zeichne den Player
		player.drawObjects(g);
		
		// Hier werden Konsolenausgaben gezeichnet
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 12));
		g.drawString("Konsolenausgaben hier", 16, 20);
		g.drawString("Pos " + "x:"+(int)player.getX()+" y:"+(int)player.getY(), 208, 20);
		// g.drawString("Delta " + houston.delta, 368, 20);

		// Hier werden die aktuellen fps gezeichnet
		g.setColor(Color.BLACK);
		g.drawString("FPS: " + houston.fps, 712, 20);

	} // Ab hier ist Schluss mit Zeichnen

}
