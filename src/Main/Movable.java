package Main;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * enthaelt Methoden fuer bewegliche Objecte
 */
public abstract class Movable {

	// bounds = "Grenzen" des Player
	private Rectangle2D bounds;
	private double speed;
	private int up, down, left, right;
	private int health, mana;

	Point2D resetPoint;

	/**
	 * Textur des beweglichen Objects
	 */
	public BufferedImage texture = null;

	/**
	 * Textursprite des beweglichen Objects
	 */
	public BufferedImage textures = null;

	/**
	 * Textur bei Bewegung nach unten
	 */
	public BufferedImage tex_down = null;

	/**
	 * Textur bei Bewegung nach left
	 */
	public BufferedImage tex_left = null;

	/**
	 * Textur bei Bewegung nach rechts
	 */
	public BufferedImage tex_right = null;

	/**
	 * Textur bei Bewegung nach oben
	 */
	public BufferedImage tex_up = null;

	/**
	 * Zeichnet den Charakter
	 * @param g
	 */
	public void drawObjects(Graphics2D g) {
		g.draw(bounds);
	}

	/**
	 * Fuehrt die Bewegung des Charakter aus
	 * @param dX
	 * @param dY
	 */
	public void move(double dX, double dY) {
		bounds.setRect(getX() + dX, getY() + dY, getWidth(), getHeight());
	}

	/**
	 * Haelt den Charakter an
	 */
	public final void stop() {
		up = down = left = right = 0;
	}

	/**
	 * gibt an, wieviel der Charakter nach oben laeuft
	 * @return up
	 */
	public final int getUp() {
		return up;
	}

	/**
	 * legt fest, wieviel der Charakter nach oben laeuft
	 * @param up
	 */
	public final void setUp(int up) {
		this.up = up;
	}

	/**
	 * gibt an, wieviel der Charakter nach unten laeuft
	 * @return down
	 */
	public final int getDown() {
		return down;
	}

	/**
	 * legt fest, wieviel der Charakter nach unten laeuft
	 * @param down
	 */
	public final void setDown(int down) {
		this.down = down;
	}

	/**
	 * gibt an, wieviel der Charakter nach links laeuft
	 * @return left
	 */
	public final int getLeft() {
		return left;
	}

	/**
	 * legt fest, wieviel der Charakter nach links laeuft
	 * @param left
	 */
	public final void setLeft(int left) {
		this.left = left;
	}

	/**
	 * gibt an, wieviel der Charakter nach rechts laeuft
	 * @return right
	 */
	public final int getRight() {
		return right;
	}

	/**
	 * legt fest, wieviel der Charakter nach rechts laeuft
	 * @param right
	 */
	public final void setRight(int right) {
		this.right = right;
	}

	/**
	 * prueft, ob sich der Charakter bewegt
	 * @return is moving
	 */
	public final boolean isMoving() {
		if (up > 0 || down > 0 || left > 0 || right > 0)
			return true;
		return false;
	}


	/**
	 * gibt Geschwindigkeit vom Charakter zurueck
	 * @return speed
	 */
	public final double getSpeed() {
		return speed;
	}

	/**
	 * legt die Geschwindigkeit vom Charakter fest
	 * @param speed
	 */
	public final void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * berechnet wieviel Pixel sich der Charakter in horizontaler Richtung bewegt
	 * @param i
	 * @param delta
	 * @return dX
	 */
	public final double getDX(int i, long delta) {
		if (i == -1)
			return i * speed * (delta / 1e9) * (left / 100.0);
		else if (i == 1)
			return i * speed * (delta / 1e9) * (right / 100.0);
		return 0;
	}


	/**
	 * berechnet wieviel Pixel sich der Charakter in vertikale Richtung bewegt
	 * @param i
	 * @param delta
	 * @return dY
	 */
	public final double getDY(int i, long delta) {
		if (i == -1)
			return i * speed * (delta / 1e9) * (up / 100.0);
		else if (i == 1)
			return i * speed * (delta / 1e9) * (down / 100.0);
		return 0;
	}

	/**
	 * Versetzt den Charakter an gewuenschte Stelle
	 * @param x
	 * @param y
	 */
	public final void setPosition(double x, double y) {
		bounds.setRect(x, y, getWidth(), getHeight());
	}

	/**
	 * Setzt den Charakter an seine Ursprungsposition zurueck
	 */
	public final void resetPosition() {
		setPosition(resetPoint.getX(), resetPoint.getY());
	}

	/**
	 * Veraendert die Ursprungsposition
	 * @param x
	 * @param y
	 */
	public void setResetPosition(double x, double y) {
		resetPoint.setLocation(x, y);
	}

	/**
	 * Veraendert die Ursprungsposition
	 * @param point
	 */
	public final void setResetPosition(Point2D point) {
		setResetPosition(point.getX(), point.getY());
	}

	/**
	 * Gibt die aktuelle horizontale Position zurueck
	 * @return X
	 */
	public final double getX() {
		return bounds.getX();
	}

	/**
	 * Gibt die aktuelle vertikale Position zurueck
	 * @return Y
	 */
	public final double getY() {
		return bounds.getY();
	}

	/**
	 * Gibt die Breite des Characters zurueck
	 * @return Width
	 */
	public final double getWidth() {
		return bounds.getWidth();
	}

	/**
	 * Gibt die Hoehe des Characters zurueck
	 * @return Height
	 */
	public final double getHeight() {
		return bounds.getHeight();
	}

	/**
	 * Gibt den Mittelpunkt des Charakters zurueck
	 * @return CenterPosition
	 */
	public final Point2D getCenterPosition() {
		return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
	}

	/**
	 * gibt das Rechteck zurueck
	 * @return bounds
	 */
	public final Rectangle2D getBounds() {
		return bounds;
	}

	/**
	 * setzt das Rechteck
	 * @param bounds
	 */
	public final void setBounds(Rectangle2D bounds) {
		this.bounds = bounds;
	}

	/**
	 * @return Health
	 */
	public final int getHealth() {
		return health;
	}

	/**
	 * setzt Health vom Charakter auf angegebenen Wert
	 * @param health
	 */
	public final void setHealth(int health) {
		this.health = health;
	}

	/**
	 * erhoeht die Lebenspunkte des Charakters
	 * @param amountOfHealth
	 */
	public final void increaseHealth(int amountOfHealth) {
		health += amountOfHealth;
	}

	/**
	 * verringert die Lebenspunkte des Charakters
	 * @param amountOfHealth
	 */
	public void decreaseHealth(int amountOfHealth) {
		health -= amountOfHealth;
	}

	/**
	 * @return Mana
	 */
	public final int getMana() {
		return mana;
	}

	/**
	 * setzt die Manapunkte auf den angegebenen Wert
	 * @param mana
	 */
	public final void setMana(int mana) {
		this.mana = mana;
	}

	/**
	 * erhoeht die Manapunkte des Charakters
	 * @param amountOfMana
	 */
	public final void increaseMana(int amountOfMana) {
		mana += amountOfMana;
	}

	/**
	 * verringert die Manapunkte des Charakters
	 * @param amountOfMana
	 */
	public final void decreaseMana(int amountOfMana) {
		mana -= amountOfMana;
	}

	/**
	 * Bestimmt, was passieren soll, wenn die Wand beruehrt wird
	 */
	abstract public void onWallHit();

}
