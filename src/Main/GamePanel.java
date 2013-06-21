package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;
	private Houston houston;
	private Player player;
	private Map map;
	private Infobar infobar;
	
	private Font plainFont;
	private Color bgColor;
	private final int heightOfInfobar = 32;
	private Point2D mouseClickPosition;

	public GamePanel(Houston houston) {
		this.houston	= houston;
		this.player		= houston.player;
		this.map		= houston.map;
		this.infobar	= new Infobar(houston, 0, -heightOfInfobar);
		
		// Setzt die Schrift fuer die Konsolenausgaben
		plainFont = new Font("Arial", Font.PLAIN, 12);
		// Setzt die Hintergrundfarbe
		bgColor = new Color(240, 240, 240);
		
		addMouseListener(this);
		mouseClickPosition = new Point2D.Double();
	}

	@Override
	protected void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		// Verschiebt den Ursprungspunkt
		g.translate(0, heightOfInfobar);
		g.setFont(plainFont);

		// Ab hier wird gezeichnet

		// Zeichnet den Hintergrund
		g.setColor(bgColor);
		g.fillRect(0, -heightOfInfobar, houston.width, houston.height);

		// Zeichnet Informationen in der Informationsleiste
		infobar.drawObjects(g);
		
		// Zeichnet die Karte
		map.drawObjects(g);
		
		// Zeichnet Items
		for (Item item : houston.itemLogic.items) {
			item.drawObjects(g);
		}

		// Zeichnet die Gegner
		for (Enemy enemy : houston.enemyLogic.enemies) {
			enemy.drawObjects(g);
		}

		// Zeichnet den Player
		player.drawObjects(g);

		for (Magic magic : houston.magicLogic.magics) {
			magic.drawObjects(g);
		}

		g.dispose();
	} // Ab hier ist Schluss mit Zeichnen

	public void mouseClicked(MouseEvent e) {
		mouseClickPosition.setLocation(e.getX(), e.getY() - heightOfInfobar);

		if (mouseClickPosition.getY() >= 0) {
			houston.magicLogic.doMagic(mouseClickPosition);
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
