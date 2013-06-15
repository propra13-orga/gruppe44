package folderol;

import java.awt.Color;
import java.awt.Graphics2D;

public class Infobar {

	private Houston houston;
	private Player player;
	private Map map;
	private Inventory inventory;

	// X Offset, Y Offset
	private int xo, yo;
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
		g.drawString("Leben :", 16 + xo, 13 + yo);
		g.drawString("(" + playerHealth + ")", 64 + xo, 13 + yo);
		g.drawRect(95 + xo, 5 + yo, 101, 7);
		g.setColor(Color.GREEN);
		g.fillRect(96 + xo, 6 + yo, playerHealth, 6);
		g.setColor(Color.BLACK);
		g.drawLine(96 + playerHealth + xo, 6 + yo, 96 + playerHealth + xo, 12 + yo);

		// Zeigt Mana an
		g.setColor(Color.BLACK);
		g.drawString("Mana :", 16 + xo, 26 + yo);
		g.drawString("(" + playerMana + ")", 64 + xo, 26 + yo);
		g.drawRect(95 + xo, 17 + yo, 101, 7);
		g.setColor(Color.RED);
		g.fillRect(96 + xo, 18 + yo, playerMana, 6);
		g.setColor(Color.BLACK);
		g.drawLine(96 + playerMana + xo, 18 + yo, 96 + playerMana + xo, 24 + yo);

		// Zeigt Geld an
		g.setColor(Color.BLACK);
		g.drawString("Geld :", 240 + xo, 13 + yo);
		g.drawString(player.getMoney() + " CP", 240 + xo, 26 + yo);

		// Zeigt Anzahl an HealthPacks und ManaPotions an
		g.drawString("Health Packs :", 350 + xo, 13 + yo);
		g.drawString(inventory.getCountOfHealthPack() + "/3", 440 + xo, 13 + yo);
		g.drawString("Mana Potions :", 350 + xo, 26 + yo);
		g.drawString(inventory.getCountOfManaPotion() + "/3", 440 + xo, 26 + yo);

		// "Moechtest du zum Shop?" anzeigen
		if (houston.logic.value == 5) {
			g.drawString("M\u00f6chtest du zum Shop?", 512 + xo, 26 + yo);
		}

		// Zeichnet die gerundete aktuelle Position des Player
		g.setColor(Color.GRAY);
		g.drawString("Pos " + "x:"+(int)player.getX()+" y:"+(int)player.getY(), 608+xo, 13+yo);
		
		// Zeichnet Karteninformationen
		g.drawString("Map  "+map.getLevelNumber()+" - "+map.getMapNumber(), 700+xo, 26+yo);
		
		// Zeichnet die aktuellen FPS (Frames Per Second)
		g.drawString("FPS: " + houston.fps, 712 + xo, 13 + yo);
	}

}
