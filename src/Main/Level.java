package Main;

/** Levelklasse */
public class Level {

	/** Levelnummer */
	public int level;

	/** Kartennummer */
	public int map;

	/** Pfad der Karte */
	public String path;

	/**
	 * Level abhaengig von Levelnummer, Kartennummer und Pfad der Karte
	 * @param level
	 * @param map
	 * @param path
	 */
	public Level(int level, int map, String path) {
		this.level = level;
		this.map = map;
		this.path = path;
	}

	/**
	 * gibt einen String bestehend aus Level, Karte und Pfad zureuck
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "level:"+level + " map:"+map + " path:"+path;
	}
}