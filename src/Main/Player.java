package Main;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import Logic.MagicLogic;

public class Player extends Movable {

	public Rectangle2D attackBox;
	private int money;
	// Maximale Werte fuer Health und Mana
	public final int maxHealth = 100, maxMana = 100;
	private int armor;
	private int lives;
	public String magicType;
	int experience;
	int playerLevel = 1;

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

	@Override
	public void decreaseHealth(int amountOfHealth) {
		super.decreaseHealth(amountOfHealth * armor/100);
	}

	// Setzt Health, Mana und Money zurueck
	public void resetPlayerStats(int health, int mana, int money, int armor, int lives, int playerLevel, int experience) {
		setHealth(health);
		setMana(mana);
		setMoney(money);
		setArmor(armor);
		setLives(lives);
		setPlayerLevel(playerLevel);
		setExperience(experience);
	}
	//PLayer Level
	public void setPlayerLevel(int playerLevel){
		this.playerLevel = playerLevel;
	}

	public int getplayerLevel(){
		return playerLevel;
	}
	//Player Experience
	public void increaseExperience(int experiencePoints){
		experience += experiencePoints;
	}
	public void setExperience(int experience){
		this.experience = experience;
	}

	public int getExperience(){
		return experience;
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
