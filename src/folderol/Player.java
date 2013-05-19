package folderol;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {

	// bounds = "Grenzen" des Player
	Rectangle2D bounds;
	private Point2D resetPoint;
	private Color color;

	final double speed;
	boolean up, down, left, right;
	private BufferedImage texture = null;

	public Player() {
		
		// Setzt Farbe des Player
		color = new Color(0, 255, 64, 50);
		
		// Setzt Geschwindigkeit des Player
		speed = 128;
		
		// Setzt Ursprungsposition des Player
		resetPoint = new Point2D.Double(64, 96);
		
		// Setzt Position und Groeße des Player
		bounds = new Rectangle2D.Double(resetPoint.getX(), resetPoint.getY(), 28, 28);
		
		// Liest und setzt Textur des Player
		try {
			texture = ImageIO.read(getClass().getResourceAsStream("../etc/img/german_m1.png"));
			texture = texture.getSubimage(0, 0, 32, 46);
		} catch (IOException e) {e.printStackTrace();}
	}
	
	
	
	// Zeichnet den spieler 
	public void drawObjects(Graphics2D g) {
		g.setColor(color);
		g.drawRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
		g.drawImage(texture, (int) bounds.getX()-2, (int) bounds.getY()-18, null);
	}

	// Versetzt den Player an gewuenschte Stelle
	void setPosition(double x, double y) {
		bounds.setRect(x, y, bounds.getWidth(), bounds.getHeight());
	}

	// Setzt den Player an seine Ursprungsposition zurueck
	void resetPosition() {
		setPosition(resetPoint.getX(), resetPoint.getY());
	}

	// Veraendert die Ursprungsposition
	void setResetPosition(double x, double y) {
		resetPoint.setLocation(x, y);
	}
	
	// Veraendert die Ursprungsposition
	public void setResetPosition(Point2D point) {
		setResetPosition(point.getX(), point.getY());
	}

	// Versetzt den Player um dX in der Horizontalen und dY in der Vertikalen
	void move(double dX, double dY) {
		bounds.setRect(bounds.getX() + dX, bounds.getY() + dY, bounds.getWidth(), bounds.getHeight());
	}

	// Gibt die aktuelle horizontale Position zurueck
	double getX() {
		return bounds.getX();
	}

	// Gibt die aktuelle vertikale Position zurueck
	double getY() {
		return bounds.getY();
	}

	// Gibt die Breite des Player zurueck
	double getWidth() {
		return bounds.getWidth();
	}

	// Gibt die Hoehe des Player zurueck
	double getHeight() {
		return bounds.getHeight();
	}


	
}
