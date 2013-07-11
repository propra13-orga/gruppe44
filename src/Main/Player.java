package Main;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Logic.MagicLogic;

/** Klasse vom Spieler */
public class Player extends Movable {

	/** Rechteck um den Spieler, ist der Gegner innerhalb dieses Rechtecks wird er bei einer Attacke verletzt */
	public Rectangle2D attackBox;
	private int money;

	/** Maximale Werte fuer Health und Mana */
	public final int maxHealth = 100, maxMana = 100;
	private int armor;
	private int lives;

	/** Zaubertyp; ANALYSIS; LINEAREALGEBRA oder INFORMATIK */
	public String magicType;
	int experience;
	int playerLevel = 1;

	/** initialisiert den Spieler */
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

		magicType = MagicLogic.ANA;
	}

	/** Zeichnet den Spieler
	 * @see Main.Movable#drawObjects(java.awt.Graphics2D)
	 */
	@Override
	public void drawObjects(Graphics2D g) {
		g.drawImage(texture, (int) getBounds().getX() - 2, (int) getBounds().getY() - 18, null);
	}

	/**
	 * bewegt den Spieler
	 * @see Main.Movable#move(double, double)
	 */
	@Override
	public void move(double dX, double dY) {
		getBounds().setRect(getBounds().getX() + dX, getBounds().getY() + dY, getBounds().getWidth(), getBounds().getHeight());
		attackBox.setRect(attackBox.getX() + dX, attackBox.getY() + dY, attackBox.getWidth(), attackBox.getHeight());
	}

	/**
	 * Setzt die Bilder des Spielers je nach Auswahl in den Einstellungen
	 * @param value
	 */
	public void changeTexture(int value) {
		try {
			if (value == 0) {
				textures = ImageIO.read(new File(
						"./res/img/characters/german_m1.png"));
			}
			if (value == 1) {
				textures = ImageIO.read(new File(
						"./res/img/characters/german_f2.png"));
			}

			tex_down = textures.getSubimage(0, 0, 32, 46);
			tex_left = textures.getSubimage(0, 49, 32, 45);
			tex_right = textures.getSubimage(0, 97, 32, 45);
			tex_up = textures.getSubimage(0, 145, 32, 45);

			texture = tex_down;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * verringert das Leben des Spielers abhaengig von der Ruestung
	 * @see Main.Movable#decreaseHealth(int)
	 */
	@Override
	public void decreaseHealth(int amountOfHealth) {
		super.decreaseHealth(amountOfHealth * armor/100);
	}

	/**
	 * Setzt Health, Mana, Money, Armor, Lives, PlayerLever und Experience zurueck
	 * @param health
	 * @param mana
	 * @param money
	 * @param armor
	 * @param lives
	 * @param playerLevel
	 * @param experience
	 */
	public void resetPlayerStats(int health, int mana, int money, int armor, int lives, int playerLevel, int experience) {
		setHealth(health);
		setMana(mana);
		setMoney(money);
		setArmor(armor);
		setLives(lives);
		setPlayerLevel(playerLevel);
		setExperience(experience);
	}

	/**
	 * setzt die Lebenanzahl auf den angegebenen Wert
	 * @param playerLevel
	 */
	public void setPlayerLevel(int playerLevel){
		this.playerLevel = playerLevel;
	}

	/** @return playerLevel */
	public int getplayerLevel(){
		return playerLevel;
	}

	/**
	 * erhoeht die Erfahrungspunkte
	 * @param experiencePoints
	 */
	public void increaseExperience(int experiencePoints){
		experience += experiencePoints;
	}

	/**
	 * setzt die Erfahrungspunkte auf den angegebenen Wert
	 * @param experience
	 */
	public void setExperience(int experience){
		this.experience = experience;
	}

	/** @return experience */
	public int getExperience(){
		return experience;
	}

	/** @return money */
	public int getMoney() {
		return money;
	}

	/**
	 * setzt Money auf den angegebenen Wert
	 * @param money
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * erhoeht Money, um den angegebenen Wert
	 * @param amountOfMoney
	 */
	public void increaseMoney(int amountOfMoney) {
		money += amountOfMoney;
	}

	/**
	 * verringert Money, um den angegebenen Wert
	 * @param amountOfMoney
	 */
	public void decreaseMoney(int amountOfMoney) {
		if (money > 0)
			money -= amountOfMoney;
	}

	/** @return armor */
	public int getArmor() {
		return armor;
	}

	/**
	 * setzt armor auf den angegebenen Wert
	 * @param armor
	 */
	public void setArmor(int armor) {
		this.armor = armor;
	}

	/** @return lives */
	public int getLives() {
		return lives;
	}

	/**
	 * setzt Lebensanzahl auf den angebenen Wert
	 * @param lives
	 */
	public void setLives(int lives) {
		this.lives = lives;
	}

	/**
	 * Spieler bleibt vor der Wand stehen
	 * @see Main.Movable#onWallHit()
	 */
	@Override
	public void onWallHit() {
		// Eierschaukeln
	}

	/**
	 * setzt Spieler an die Position
	 * @see Main.Movable#setResetPosition(double, double)
	 */
	@Override
	public void setResetPosition(double x, double y) {
		super.setResetPosition(x, y);
		attackBox.setRect(x - 10, y - 10, attackBox.getWidth(), attackBox.getHeight());
	}
}
