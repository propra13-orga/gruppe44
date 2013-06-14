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
	private Infobar infobar;
	private EnemyLogic enemyLogic;
	
	private Font plainFont;
	private Color bgColor;
	
	public GamePanel(Houston houston) {
		this.houston	= houston;
		this.player		= houston.player;
		this.map		= houston.map;
		this.infobar	= new Infobar(houston, 0, -32);
		this.enemyLogic = houston.enemyLogic;
		
		// Setzt die Schrift fuer die Konsolenausgaben
		plainFont = new Font("Arial", Font.PLAIN, 12);
		// Setzt die Hintergrundfarbe
		bgColor = new Color(240, 240, 240);
	}

	@Override
	protected void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		// Verschiebt den Ursprungspunkt
		g.translate(0, 32);
		g.setFont(plainFont);

		// Ab hier wird gezeichnet
		
		// Zeichnet den Hintergrund
		g.setColor(bgColor);
		g.fillRect(0, -32, houston.width, houston.height);

		// Zeichnet die Karte
		map.drawObjects(g);
		
		// Zeichnet die Gegner
		for (int i=0; i<enemyLogic.allEnemysRight.size(); i++){
			enemyLogic.enemy = enemyLogic.allEnemysRight.get(i);
			enemyLogic.enemy.drawObjects(g);
		}
		for (int i=0; i<enemyLogic.allEnemysUp.size(); i++){
			enemyLogic.enemy = enemyLogic.allEnemysUp.get(i);
			enemyLogic.enemy.drawObjects(g);
		}

		// Zeichnet den Player
		player.drawObjects(g);
		
		// Zeichnet Informationen in der Informationsleiste
		infobar.drawObjects(g);
		
		g.dispose();
	} // Ab hier ist Schluss mit Zeichnen
	
}
