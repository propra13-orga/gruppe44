package folderol;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {

	// bounds = "Grenzen" des Player
	private Rectangle2D bounds;
	private Point2D resetPoint;
	private Color color;

	final double speed;
	boolean up, down, left, right;
	private BufferedImage texture = null;

	// Werte fuer Health, Mana und Money
	private int health, mana, money;
	// Maximale Werte für Health und Mana
	final int maxHealth = 100, maxMana = 100;

	public Player() {

		// Setzt Farbe des Player
		color = new Color(0, 255, 64, 50);

		// Setzt Geschwindigkeit des Player
		speed = 128;

		// Setzt Ursprungsposition des Player
		resetPoint = new Point2D.Double(64, 96);

		// Setzt Position und Groesse des Player
		bounds = new Rectangle2D.Double(resetPoint.getX(), resetPoint.getY(), 28, 28);

		// Setzt die Textur des Player
		changeTexture(0);

		// Setzt initial Health, Mana und Money zurueck
		resetHealthManaMoney(100, 100, 200);
	}

	// Zeichnet den spieler
	public void drawObjects(Graphics2D g) {
		g.setColor(color);
		// g.drawRect((int) bounds.getX(), (int) bounds.getY(), (int)
		// bounds.getWidth(), (int) bounds.getHeight());
		g.drawImage(texture, (int) bounds.getX() - 2, (int) bounds.getY() - 18,
				null);
	}

	// Versetzt den Player an gewuenschte Stelle
	public void setPosition(double x, double y) {
		bounds.setRect(x, y, bounds.getWidth(), bounds.getHeight());
	}

	// Setzt den Player an seine Ursprungsposition zurueck
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

	// Versetzt den Player um dX in der Horizontalen und dY in der Vertikalen
	public void move(double dX, double dY) {
		bounds.setRect(bounds.getX() + dX, bounds.getY() + dY, bounds.getWidth(), bounds.getHeight());
	}
	
	public void stop() {
		up = down = left = right = false;
	}

	// Gibt die aktuelle horizontale Position zurueck
	public double getX() {
		return bounds.getX();
	}

	// Gibt die aktuelle vertikale Position zurueck
	public double getY() {
		return bounds.getY();
	}

	// Gibt die Breite des Player zurueck
	public double getWidth() {
		return bounds.getWidth();
	}

	// Gibt die Hoehe des Player zurueck
	public double getHeight() {
		return bounds.getHeight();
	}

	public BufferedImage getTexture() {
		return texture;
	}

	// Setzt die Bilder des Spielers je nach Auswahl in den Einstellungen
	public void changeTexture(int value) {
		try {
			if (value == 0) {

				texture = ImageIO.read(new File("./res/img/german_m1.png"));
				texture = texture.getSubimage(0, 0, 32, 46);
			}
			if (value == 1) {
				texture = ImageIO.read(new File("./res/img/german_f2.png"));
				texture = texture.getSubimage(0, 0, 32, 46);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Setzt Health, Mana und Money zurueck
	public void resetHealthManaMoney(int health, int mana, int money) {
		this.health = health;
		this.mana = mana;
		this.money = money;
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

	// Money
	public int getMoney() {
		return money;
	}

	public void increaseMoney(int amountOfMoney) {
		money += amountOfMoney;
	}

	public void decreaseMoney(int amountOfMoney) {
		money -= amountOfMoney;
	}

}
