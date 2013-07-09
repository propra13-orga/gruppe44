package Main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Infobar {

	private final Houston houston;
	private final Player player;
	private final Map map;
	private final Inventory inventory;

	// X Offset, Y Offset
	private final int xo, yo;
	private int playerHealth, playerMana;

	public Infobar(Houston houston, int xOffset, int yOffset) {
		this.houston = houston;
		this.player = houston.player;
		this.map = houston.map;
		this.inventory = houston.inventory;
		this.xo = xOffset;
		this.yo = yOffset;
	}

	public void drawObjects(Graphics2D g) {
		playerHealth = player.getHealth();
		playerMana = player.getMana();

		// Zeigt Leben an
		g.setColor(Color.BLACK);
		g.drawString("Leben :", 10 + xo, 13 + yo);
		g.drawString("(" + playerHealth + ")", 58 + xo, 13 + yo);
		g.drawRect(89 + xo, 5 + yo, player.maxHealth + 1, 7);
		g.setColor(Color.GREEN);
		g.fillRect(90 + xo, 6 + yo, playerHealth, 6);
		g.setColor(Color.BLACK);
		g.drawLine(90 + playerHealth + xo, 6 + yo, 90 + playerHealth + xo, 12 + yo);

		// Zeigt Mana an
		g.setColor(Color.BLACK);
		g.drawString("Mana :", 10 + xo, 26 + yo);
		g.drawString("(" + playerMana + ")", 58 + xo, 26 + yo);
		g.drawRect(89 + xo, 17 + yo, player.maxMana + 1, 7);
		g.setColor(Color.RED);
		g.fillRect(90 + xo, 18 + yo, playerMana, 6);
		g.setColor(Color.BLACK);
		g.drawLine(90 + playerMana + xo, 18 + yo, 90 + playerMana + xo, 24 + yo);

		//Zeichnet die Leben als Herzen
		BufferedImage heartPicture = null;
		try {
			heartPicture = ImageIO.read(new File("./res/img/infobar/heart.png"));
		} catch (IOException e) { e.printStackTrace();}
		if(player.getLives() > 1)
			g.drawImage(heartPicture , 205 + xo, 6 + yo, null);
		if(player.getLives() > 2)
			g.drawImage(heartPicture , 225 + xo, 6 + yo, null);

		// Zeigt Geld an
		g.setColor(Color.BLACK);
		g.drawString("Geld :", 255 + xo, 13 + yo);
		g.drawString(player.getMoney() + " CP", 255 + xo, 26 + yo);

		// Zeigt Anzahl an HealthPacks und ManaPotions an
		g.drawString("Health Packs :", 315 + xo, 13 + yo);
		g.drawString(inventory.getCountOfHealthPack() + "/3", 405 + xo, 13 + yo);
		g.drawString("Mana Potions :", 315 + xo, 26 + yo);
		g.drawString(inventory.getCountOfManaPotion() + "/3", 405 + xo, 26 + yo);

		//Zeichnet Schaden des Angriffes
		g.drawString("Attacke " + houston.playerLogic.getAttackDamage(player.getplayerLevel()), 500 + xo, 13 + yo );

		//Zeichnet Schaden des Zaubers
		g.drawString("Zauber " + houston.magicLogic.calculateMagicDamage(player.getplayerLevel()) +"/" + (houston.magicLogic.calculateMagicDamage(player.getplayerLevel()) + 5), 500 + xo, 26 + yo );

		// Zeichnet das Level vom Spieler
		g.drawString("Level " +player.getplayerLevel(), 435 + xo, 13 + yo);

		// Zeichnet Erfahrung
		g.drawString("Exp " +player.getExperience()+ "/" + houston.playerLogic.getNeededExperience(), 435 + xo, 26 + yo );

		// "Moechtest du zum Shop?" anzeigen
		if (houston.gameLogic.value == 5) {
			g.drawString("M\u00f6chtest du zum Shop?", 575 + xo, 13 + yo);
		}

		// Zeigt Armor an, falls es angelegt ist
		if (player.getArmor() != 100)
			g.drawString("R\u00fcstung", 610 + xo, 26 + yo);

		// Zeichnet die gerundete aktuelle Position des Player
		g.setColor(Color.GRAY);
		//		g.drawString("Pos " + "x:"+(int)player.getX()+" y:"+(int)player.getY(), 608+xo, 13+yo);

		// Zeichnet Karteninformationen
		g.drawString("Map  "+(map.getLevelNumber()+1)+" - "+(map.getMapNumber()+1), 707+xo, 26+yo);

		// Zeichnet die aktuellen FPS (Frames Per Second)
		g.drawString("FPS: " + houston.fps, 717 + xo, 13 + yo);
	}

}
