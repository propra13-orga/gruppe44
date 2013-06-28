package Main;


public class Shop {

	private Houston houston;
	private Player player;
	private Inventory inventory;

	// Preise fuer die kaufbaren Items
	final static int
	priceForHealthPack = 40,
	priceForManaPotion = 80;

	public Shop(Houston houston) {
		this.houston = houston;
		this.player = houston.player;
		this.inventory = houston.inventory;
	}

	// Aktiviert/deaktiviert die erlaubten Kaufbutton und
	// passt die Texte der Infolabel ensprechend aktueller Werte
	public void resetShopView() {
		// Aktualisiert den Text der Informationslabel
		houston.c7l11.setText(player.getMoney() + " CP");
		houston.c7l8.setText(inventory.getCountOfHealthPack() + "");
		houston.c7l9.setText(inventory.getCountOfManaPotion() + "");

		// Schaltet alle Kaufbutton aus ...
		houston.c7b1.setEnabled(false);
		houston.c7b2.setEnabled(false);

		// ... und die Erlaubten ein
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
		resetShopView();
	}

	// Kauft ein ManaPotion
	public void buyManaPotion() {
		player.decreaseMoney(priceForManaPotion);
		inventory.addManaPotion();
		resetShopView();
	}

}
