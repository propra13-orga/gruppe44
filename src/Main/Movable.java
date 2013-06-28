package Main;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Movable {

	// bounds = "Grenzen" des Player
	private Rectangle2D bounds;
	private double speed;
	private int up, down, left, right;
	private int health, mana;

	Point2D resetPoint;
	BufferedImage texture = null;
	BufferedImage textures = null;
	BufferedImage tex_down = null;
	BufferedImage tex_left = null;
	BufferedImage tex_right = null;
	BufferedImage tex_up = null;

	// Zeichnet den Charakter
	public void drawObjects(Graphics2D g) {
		g.draw(bounds);
	}

	// Fuehrt die Bewegung des Charakter aus
	public void move(double dX, double dY) {
		bounds.setRect(getX() + dX, getY() + dY, getWidth(), getHeight());
	}

	// Haelt den Charakter an
	public final void stop() {
		up = down = left = right = 0;
	}

	public final int getUp() {
		return up;
	}

	public final void setUp(int up) {
		this.up = up;
	}

	public final int getDown() {
		return down;
	}

	public final void setDown(int down) {
		this.down = down;
	}

	public final int getLeft() {
		return left;
	}

	public final void setLeft(int left) {
		this.left = left;
	}

	public final int getRight() {
		return right;
	}

	public final void setRight(int right) {
		this.right = right;
	}

	public final boolean isMoving() {
		if (up > 0 || down > 0 || left > 0 || right > 0)
			return true;
		return false;
	}

	public final double getSpeed() {
		return speed;
	}

	public final void setSpeed(double speed) {
		this.speed = speed;
	}

	public final double getDX(int i, long delta) {
		if (i == -1)
			return i * speed * (delta / 1e9) * (left / 100.0);
		else if (i == 1)
			return i * speed * (delta / 1e9) * (right / 100.0);
		return 0;
	}

	public final double getDY(int i, long delta) {
		if (i == -1)
			return i * speed * (delta / 1e9) * (up / 100.0);
		else if (i == 1)
			return i * speed * (delta / 1e9) * (down / 100.0);
		return 0;
	}

	// Versetzt den Charakter an gewuenschte Stelle
	public final void setPosition(double x, double y) {
		bounds.setRect(x, y, getWidth(), getHeight());
	}

	// Setzt den Charakter an seine Ursprungsposition zurueck
	public final void resetPosition() {
		setPosition(resetPoint.getX(), resetPoint.getY());
	}

	// Veraendert die Ursprungsposition
	public void setResetPosition(double x, double y) {
		resetPoint.setLocation(x, y);
	}

	// Veraendert die Ursprungsposition
	public final void setResetPosition(Point2D point) {
		setResetPosition(point.getX(), point.getY());
	}

	// Gibt die aktuelle horizontale Position zurueck
	public final double getX() {
		return bounds.getX();
	}

	// Gibt die aktuelle vertikale Position zurueck
	public final double getY() {
		return bounds.getY();
	}

	// Gibt die Breite des Characters zurueck
	public final double getWidth() {
		return bounds.getWidth();
	}

	// Gibt die Hoehe des Characters zurueck
	public final double getHeight() {
		return bounds.getHeight();
	}

	public final Point2D getCenterPosition() {
		return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
	}

	public final Rectangle2D getBounds() {
		return bounds;
	}

	public final void setBounds(Rectangle2D bounds) {
		this.bounds = bounds;
	}

	public final void resetHealthManaMoney(int health, int mana, int money) {
		this.health = health;
		this.mana = mana;
	}

	// Health
	public final int getHealth() {
		return health;
	}

	public final void setHealth(int health) {
		this.health = health;
	}

	public final void increaseHealth(int amountOfHealth) {
		health += amountOfHealth;
	}

	public void decreaseHealth(int amountOfHealth) {
		health -= amountOfHealth;
	}

	// Mana
	public final int getMana() {
		return mana;
	}

	public final void setMana(int mana) {
		this.mana = mana;
	}

	public final void increaseMana(int amountOfMana) {
		mana += amountOfMana;
	}

	public final void decreaseMana(int amountOfMana) {
		mana -= amountOfMana;
	}

	// Bestimmt, was passieren soll, wenn die Wand berührt wird
	abstract public void onWallHit();

}
