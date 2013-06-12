package folderol;

public class Shop {

	Houston houston;
	private Player player;
	private Inventory inventory;

	// Preise fuer die kaufbaren Items
	final static int 
	priceForHealthPack = 40, 
	priceForManaPotion = 100;

	public Shop(Houston houston) {
		this.houston = houston;
		this.player = houston.player;
		this.inventory = houston.inventory;
	}

	// Aktiviert/deaktiviert die erlaubten Kaufbutton
	public void aktivateValidBuyButtons() {
		houston.c7b1.setEnabled(false);
		houston.c7b2.setEnabled(false);

		if ((inventory.getCountOfHealthPack() < 3)
				&& (player.getMoney() >= priceForHealthPack)) {
			houston.c7b1.setEnabled(true);
		}
		if ((inventory.getCountOfManaPotion() < 3)
				&& (player.getMoney() >= priceForManaPotion)) {
			houston.c7b2.setEnabled(true);
		}
	}

	// Kauft ein HealthPack
	public void buyHealthPack() {
		player.decreaseMoney(priceForHealthPack);
		inventory.addHealthPack();
		aktivateValidBuyButtons();
	}

	// Kauft ein ManaPotion
	public void buyManaPotion() {
		player.decreaseMoney(priceForManaPotion);
		inventory.addManaPotion();
		aktivateValidBuyButtons();
	}
	
}
