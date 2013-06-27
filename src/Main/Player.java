package Main;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player extends Movable {

	public Rectangle2D attackBox;
	private int money;
	// Maximale Werte fuer Health und Mana
	public final int maxHealth = 100, maxMana = 100;
	private int armor;
	private int lives;

	public Player() {
		// Setzt Geschwindigkeit des Player
		setSpeed(128);

		// Setzt Ursprungsposition des Player
		resetPoint = new Point2D.Double();

		// Setzt Position und Groesse des Player
		setBounds(new Rectangle2D.Double(0, 0, 28, 28));
		
		attackBox = new Rectangle2D.Double(0, 0, 48, 48);

		// Setzt die Textur des Player
		changeTexture(0);
	}

	// Zeichnet den Spieler
	@Override
	public void drawObjects(Graphics2D g) {
		g.drawImage(texture, (int) getBounds().getX() - 2, (int) getBounds().getY() - 18, null);
	}
	
	@Override
	public void move(double dX, double dY) {
		getBounds().setRect(getBounds().getX() + dX, getBounds().getY() + dY, getBounds().getWidth(), getBounds().getHeight());
		attackBox.setRect(attackBox.getX() + dX, attackBox.getY() + dY, attackBox.getWidth(), attackBox.getHeight());
	}

	// Setzt die Bilder des Spielers je nach Auswahl in den Einstellungen
	public void changeTexture(int value) {
		try {
			if (value == 0) {
				texture = ImageIO.read(new File("./res/img/characters/german_m1.png"));
				texture = texture.getSubimage(0, 0, 32, 46);
			}
			if (value == 1) {
				texture = ImageIO.read(new File("./res/img/characters/german_f2.png"));
				texture = texture.getSubimage(0, 0, 32, 46);
			}
		} catch (IOException e) {e.printStackTrace();}
	}
	
	@Override
	public void decreaseHealth(int amountOfHealth) {
		super.decreaseHealth(amountOfHealth * armor/100);
	}

	// Setzt Health, Mana und Money zurueck
	public void resetHealthManaMoneyArmorLives(int health, int mana, int money, int armor, int lives) {
		setHealth(health);
		setMana(mana);
		setMoney(money);
		setArmor(armor);
		setLives(lives);
	}

	// Money
	public int getMoney() {
		return money;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}

	public void increaseMoney(int amountOfMoney) {
		money += amountOfMoney;
	}

	public void decreaseMoney(int amountOfMoney) {
		if (money > 0)
			money -= amountOfMoney;
	}
	
	// Armor
	public int getArmor() {
		return armor;
	}
	
	public void setArmor(int armor) {
		this.armor = armor;
	}
	
	// Lives
	public int getLives() {
		return lives;
	}
	
	public void setLives(int lives) {
		this.lives = lives;
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
