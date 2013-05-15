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

	
	
	public GamePanel(Houston houston) {
		this.houston = houston;
		this.player = houston.player;
	}
	
	
	
	@Override
	protected void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		Graphics2D g = (Graphics2D) gr;
		// Hier wird gezeichnet
		
		g.setColor(new Color(230, 230, 230));
		g.fillRect(0, 0, 768, 640); // Hintergrund
	
		
		int[][] karte = { 	{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3}, 
							{ 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},	
							{ 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},	
							{ 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},	
							{ 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},	
							{ 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},	
							{ 3, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},	
							{ 3, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},	
							{ 3, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},	
							{ 3, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
							{ 3, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
							{ 3, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
							{ 3, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
							{ 3, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
							{ 3, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
							{ 3, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
							{ 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
							{ 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
							{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3} };
		
		int M = 32;
		int Off = 32;
		int wid = 31;
		int hei = 31;
		for (int i = 0; i < karte.length; i++) {
			for (int j = 0; j < karte[i].length; j++) {
				g.setColor(new Color(60*karte[i][j]+20, 70*karte[i][j], 40));
				g.fillRect(j*M, i*M+Off, wid, hei);
				// g.drawRect(j*M+Off, i*M+Off, wid-1, hei-1);
				// g.setColor(Color.GREEN);
				// g.drawString(""+karte[i][j], j*M+Off, i*M+Off+12);
			}
		}
		
		
		// Zeichne den Player
		g.setColor(player.getColor());
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
