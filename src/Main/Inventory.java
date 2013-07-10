package Main;

/** Inventar des Spielers */
public class Inventory {

	Houston houston;
	Player player;

	// Anzal der Items im Inventar
	private int healthPack, manaPotion;
	// Werte, wieviel jedes Item bringt
	final static int
	healthPerPortion	= 30,
	manaPerPortion		= 60;

	/**
	 * Initialisiert das Inventar
	 * @param houston
	 */
	public Inventory(Houston houston) {
		this.houston = houston;
		this.player = houston.player;
	}

	/** Setzt healthPack und ManaPotion auf 0 zurueck */
	public void clear() {
		healthPack = manaPotion = 0;
	}

	/** @return Anzahl healthPack */
	public int getCountOfHealthPack() {
		return healthPack;
	}

	/** Erhoeht die Anzahl an HealthPack um 1, solang es weniger als 3 sind */
	public void addHealthPack() {
		if (healthPack < 3)
			healthPack++;
	}

	/** Verringert die Anzahl an HealthPack um 1, solang der Spieler mindestens 1 besitzt */
	public void subHealthPack() {
		if (healthPack > 0)
			healthPack--;
	}


	/** @return Anzahl manaPotion */
	public int getCountOfManaPotion() {
		return manaPotion;
	}

	/** Erhoeht die Anzahl an ManaPotion um 1, solang es weniger als 3 sind */
	public void addManaPotion() {
		if (manaPotion < 3)
			manaPotion++;
	}

	/** Veringert die Anzahl an ManaPotion um 1, solang der Spieler mindestens 1 besitzt */
	public void subManaPotion() {
		if (manaPotion > 0)
			manaPotion--;
	}

	/** Benutzt ein Healthpack und veraendert entsprechend das Leben des Spielers */
	public void useHealthPack() {
		if (healthPack > 0) {
			subHealthPack();
			if (player.getHealth() <= (player.maxHealth - healthPerPortion)) {
				player.increaseHealth(healthPerPortion);
			} else {
				if (player.getLives() < 3) {
					player.setLives(player.getLives() + 1);
					player.setHealth((player.getHealth() + healthPerPortion) % player.maxHealth);
				} else {
					player.setHealth(player.maxHealth);
				}
			}
		}
	}

	/** Benutzt ein ManaPotion und veraendert entsprechend das Mana des Spielers */
	public void useManaPotion() {
		if (manaPotion > 0) {
			subManaPotion();
			if (player.getMana() <= (player.maxHealth - manaPerPortion)) {
				player.increaseMana(manaPerPortion);
			} else {
				player.setMana(player.maxHealth);
			}
		}
	}

}
