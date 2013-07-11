package Logic;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Main.Houston;
import Main.Item;
import Main.Map;

/** enthaelt die Logic der Items */
public class ItemLogic {

	private Houston houston;
	private Map map;

	/** Itemsarray, enthaelt alle Items der aktuellen Karte */
	public ArrayList<Item> items;
	private Item item;
	private int itemType;

	/** enthaelt die Texturen der unterschiedlichen Items */
	public HashMap<Integer, BufferedImage> texture;

	/** enthaelt die Namen der unterschiedlichen Items */
	public HashMap<Integer, String> itemName;

	/**
	 * initialisiert die Itemarrays
	 * @param houston
	 */
	public ItemLogic(Houston houston) {
		this.houston = houston;
		this.map = houston.map;

		items = new ArrayList<Item>();
		texture = new HashMap<>();
		itemName = new HashMap<>();
		initializeHashMaps();
	}

	private void initializeHashMaps() {
		File tex_file = new File("./res/img/tiles/texture_array.png");
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(tex_file));) {
			BufferedImage sprite = ImageIO.read(in);

			BufferedImage tex_delete = sprite.getSubimage(0, 288, 32, 32);
			BufferedImage tex_health = sprite.getSubimage(0, 192, 32, 32);
			BufferedImage tex_mana = sprite.getSubimage(0, 160, 32, 32);
			BufferedImage tex_armor = sprite.getSubimage(32, 166, 32, 58);

			texture.put(0, tex_delete);
			texture.put(90, tex_health);
			texture.put(91, tex_mana);
			texture.put(92, tex_armor);

			itemName.put(0, "Item entfernen");
			itemName.put(90, "Health Pack");
			itemName.put(91, "Mana Potion");
			itemName.put(92, "R\u00fcstung");
		} catch (IOException e) {e.printStackTrace();}
	}

	/** liest die Items aus der Karte und erstellt diese */
	public void onLevelChange() {
		items.clear();

		itemType = 90;
		for (Point2D singleItemPosition : map.multiSearch(map.itemArray, itemType)) {
			items.add(new Item(texture.get(itemType), singleItemPosition, itemType));
		}
		itemType = 91;
		for (Point2D singleItemPosition : map.multiSearch(map.itemArray, itemType)) {
			items.add(new Item(texture.get(itemType), singleItemPosition, itemType));
		}
		itemType = 92;
		for (Point2D singleItemPosition : map.multiSearch(map.itemArray, itemType)) {
			items.add(new Item(texture.get(itemType), singleItemPosition, itemType));
		}
	}

	/**
	 * entfernt aufgenommene Items von der Karte;
	 * prueft ob der Spieler ein Item aufgenommen hat
	 */
	public void doGameUpdates() {
		for (int i = items.size() - 1; i >= 0; i--) {
			item = items.get(i);

			// Filtert die zu loeschenden Magics raus
			if (item.remove) {
				items.remove(i);
				continue;
			}
			// Prueft, ob das Item sich mit dem Player Ueberschneidet
			if (item.bounds.intersects(houston.player.getBounds())) {
				item.remove = true;
				pickUpItem(item.itemType);
			}
		}
	}

	private void pickUpItem(int itemType) {
		switch (itemType) {
		case 90:
			houston.inventory.addHealthPack();
			break;
		case 91:
			houston.inventory.addManaPotion();
			break;
		case 92:
			houston.player.setArmor(50);
			break;
		}
	}

}
