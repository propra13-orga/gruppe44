package folderol;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Movable {

	// bounds = "Grenzen" des Player
	Rectangle2D bounds;
	double speed;
	boolean up, down, left, right;
	int health, mana;

	Point2D resetPoint;
	BufferedImage texture = null;

	// Zeichnet den Charakter
	public void drawObjects(Graphics2D g) {
		g.draw(bounds);
	}

	// Fuehrt die Bewegung des Charakter aus 
	public void move(double dX, double dY) {
		bounds.setRect(bounds.getX() + dX, bounds.getY() + dY, bounds.getWidth(), bounds.getHeight());
	}
	
	// Haelt den Charakter an
	public void stop() {
		up = down = left = right = false;
	}

	// Versetzt den Charakter an gewuenschte Stelle
	public void setPosition(double x, double y) {
		bounds.setRect(x, y, bounds.getWidth(), bounds.getHeight());
	}

	// Setzt den Charakter an seine Ursprungsposition zurueck
	public void resetPosition() {
		setPosition(resetPoint.getX(), resetPoint.getY());
	}

	// Veraendert die Ursprungsposition
	public void setResetPosition(double x, double y) {
		resetPoint.setLocation(x, y);
	}

	// Veraendert die Ursprungsposition
	public void setResetPosition(Point2D point) {
		setResetPosition(point.getX(), point.getY());
	}

	// Gibt die aktuelle horizontale Position zurueck
	public double getX() {
		return bounds.getX();
	}

	// Gibt die aktuelle vertikale Position zurueck
	public double getY() {
		return bounds.getY();
	}

	// Gibt die Breite des Characters zurueck
	public double getWidth() {
		return bounds.getWidth();
	}

	// Gibt die Hoehe des Characters zurueck
	public double getHeight() {
		return bounds.getHeight();
	}
	
	public Point2D getCenterPosition() {
		return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
	}

	public void resetHealthManaMoney(int health, int mana, int money) {
		this.health = health;
		this.mana = mana;
	}

	// Health
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void increaseHealth(int amountOfHealth) {
		health += amountOfHealth;
	}

	public void decreaseHealth(int amountOfHealth) {
		health -= amountOfHealth;
	}

	// Mana
	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public void increaseMana(int amountOfMana) {
		mana += amountOfMana;
	}

	public void decreaseMana(int amountOfMana) {
		mana -= amountOfMana;
	}

	// Bestimmt, was passieren soll, wenn die Wand berührt wird
	abstract public void onWallHit();
}
