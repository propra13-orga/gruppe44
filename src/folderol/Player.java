package folderol;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player extends Movable {

	Rectangle2D attackBox;
	private int money;
	// Maximale Werte für Health und Mana
	public final int maxHealth = 100, maxMana = 100;

	public Player() {
		// Setzt Geschwindigkeit des Player
		speed = 128;

		// Setzt Ursprungsposition des Player
		resetPoint = new Point2D.Double(96, 96);

		// Setzt Position und Groesse des Player
		bounds = new Rectangle2D.Double(resetPoint.getX(), resetPoint.getY(), 28, 28);
		
		attackBox = new Rectangle2D.Double(resetPoint.getX() - 15,resetPoint.getY() - 15, 48, 48);

		// Setzt die Textur des Player
		changeTexture(0);

		// Setzt initial Health, Mana und Money zurueck
		resetHealthManaMoney(100, 100, 200);
	}

	// Zeichnet den spieler
	@Override
	public void drawObjects(Graphics2D g) {
		g.drawImage(texture, (int) bounds.getX() - 2, (int) bounds.getY() - 18, null);
	}
	
	@Override
	public void move(double dX, double dY) {
		bounds.setRect(bounds.getX() + dX, bounds.getY() + dY, bounds.getWidth(), bounds.getHeight());
		attackBox.setRect(attackBox.getX() + dX, attackBox.getY() + dY, attackBox.getWidth(), attackBox.getHeight());
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
	
	@Override
	public void onWallHit() {
		// Eierschaukeln
	}

	@Override
	public void setResetPosition(double x, double y) {
		super.setResetPosition(x, y);
		attackBox.setRect(x - 10, y - 10, attackBox.getWidth(), attackBox.getHeight());
	}
}
