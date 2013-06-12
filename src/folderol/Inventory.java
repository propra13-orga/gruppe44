package folderol;

public class Inventory {

	Houston houston;
	Player player;
	
	// Anzal der Items im Inventar
	int healthPack, manaPotion;
	// Werte, wieviel jedes Item bringt
	final static int 
	healthPerPortion	= 20,
	manaPerPortion		= 45;
	

	public Inventory(Houston houston) {
		this.houston = houston;
		this.player = houston.player;
	}

	// Setzt healthPack und ManaPotion auf 0 zurueck
	public void clear() {
		healthPack = manaPotion = 0;
	}

	// Gibt die Anzahl an HealthPack im Inventar zurueck
	public int getCountOfHealthPack() {
		return healthPack;
	}

	// Erhoeht die Anzahl an HealthPack um 1
	public void addHealthPack() {
		healthPack++;
	}

	// Veringert die Anzahl an HealthPack um 1
	public void subHealthPack() {
		healthPack--;
	}

	// Gibt die Anzahl an ManaPotion im Inventar zurueck
	public int getCountOfManaPotion() {
		return manaPotion;
	}

	// Erhoeht die Anzahl an ManaPotion um 1
	public void addManaPotion() {
		manaPotion++;
	}

	// Veringert die Anzahl an ManaPotion um 1
	public void subManaPotion() {
		manaPotion--;
	}

	// Benutzt ein Healthpack und veraendert entsprechend das Leben des Spielers
	public void useHealthPack() {
		if (healthPack > 0) {
			subHealthPack();
			if (player.getHealth() <= (player.maxHealth-healthPerPortion)) {
				player.increaseHealth(healthPerPortion);
			} else {
				player.setHealth(player.maxHealth);
			}
		}
	}

	// Benutzt ein ManaPotion und veraendert entsprechend das Mana des Spielers
	public void useManaPotion() {
		if (manaPotion > 0) {
			subManaPotion();
			if (player.getMana() <= (player.maxHealth-manaPerPortion)) {
				player.increaseMana(manaPerPortion);
			} else {
				player.setMana(player.maxHealth);
			}
		}
	}
	
}
