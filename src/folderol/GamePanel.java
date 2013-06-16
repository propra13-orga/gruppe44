package folderol;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;
	private Houston houston;
	private Player player;
	private Map map;
	private Infobar infobar;
	private EnemyLogic enemyLogic;
	
	private Font plainFont;
	private Color bgColor;
	ArrayList<Magic> magics = new ArrayList<Magic> ();
	private final int infobarSize = 32;
	
	public GamePanel(Houston houston) {
		this.houston	= houston;
		this.player		= houston.player;
		this.map		= houston.map;
		this.infobar	= new Infobar(houston, 0, -infobarSize);
		this.enemyLogic = houston.enemyLogic;
		
		// Setzt die Schrift fuer die Konsolenausgaben
		plainFont = new Font("Arial", Font.PLAIN, 12);
		// Setzt die Hintergrundfarbe
		bgColor = new Color(240, 240, 240);
		
		addMouseListener(this);
	}

	@Override
	protected void paintComponent(Graphics gr) {
		synchronized (houston) {
			Graphics2D g = (Graphics2D) gr;
			// Verschiebt den Ursprungspunkt
			g.translate(0, infobarSize);
			g.setFont(plainFont);

			// Ab hier wird gezeichnet
			
			// Zeichnet den Hintergrund
			g.setColor(bgColor);
			g.fillRect(0, -infobarSize, houston.width, houston.height);

			// Zeichnet die Karte
			map.drawObjects(g);
			
			// Zeichnet die Gegner
			for (Enemy enemy : enemyLogic.enemies) {
				enemy.drawObjects(g);
			}

			// Zeichnet den Player
			player.drawObjects(g);
			
			// Zeichnet Informationen in der Informationsleiste
			infobar.drawObjects(g);
			
			for (Magic magic : magics) {
				magic.drawObjects(g);
			}
			
			g.dispose();
		}
	} // Ab hier ist Schluss mit Zeichnen
	
	public void mouseClicked(MouseEvent e){
		boolean isClickInGamepanel = (e.getY() > infobarSize);
		Point2D clickPosition = new Point2D.Double(e.getX(), e.getY() - infobarSize);

		if (isClickInGamepanel) {
			if (player.useMana(Magic.getManaCost())) {
				Magic magic = new Magic(houston, houston.player.getCenterPosition(), clickPosition);
				magics.add(magic);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
}
