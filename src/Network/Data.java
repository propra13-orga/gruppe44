package Network;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * Daten des Mehrspielermodus
 */
public class Data implements Serializable {

	private transient static final long serialVersionUID = 1L;


	/**
	 * gibt an, ob die Verbindung beendet werden soll
	 */
	public Boolean closeConnection = false;

	/**
	 * gibt an, von wem de Nachricht ist
	 */
	public String name = "";

	/**
	 * enthaelt die Nachricht
	 */
	public String message = "";

	/**
	 * gibt an, ob der Client bzw der Server bereit zum Spielen ist
	 */
	public Boolean ready = false;

	/**
	 * Rechteck des Spielers
	 */
	public Rectangle2D playerBounds = new Rectangle2D.Double();

	/**
	 * gibt die Levelnummer an
	 */
	public int levelNumber = 0;

	/**
	 * gibt die Kartennummer an
	 */
	public int mapNumber = 0;

	/**
	 * gibt an, ob der Spieler noch lebt
	 */
	public Boolean playerIsAlive = true;


	/**
	 * hier geschieht nix
	 */
	public Data() {}


	/**
	 * @param data
	 * @return ob die Verbindung beendet werden soll;
	 * ob der uebergebene Name gleich dem data Namen ist;
	 * ob die uebergebene Nachricht gleich dem data Nachricht ist
	 * ob beide bereit sind oder nicht;
	 * ob das uebergebene Spieler Rechteck gleich dem date Spieler Rechteck ist;
	 * ob die uebergebene Levelnummer ungleich der data Levelnummer ist;
	 * ob die uebergebene Kartennummer ungleich der data Kartennummer ist
	 */
	public boolean isEqualTo(Data data) {
		return (
				(closeConnection == data.closeConnection)
				&& (name.equals(data.name))
				&& (message.equals(data.message))
				&& (ready == data.ready)
				&& (playerBounds.equals(data.playerBounds))
				&& (levelNumber != data.levelNumber)
				&& (mapNumber != data.mapNumber)
				);
	}


	/**
	 * kopiert folgende Informationen:
	 * ob die Verbingund beendet werden soll;
	 * Name; Nachricht;
	 * ob der Benutzer bereit ist;
	 * Spielerrechteck;
	 * Levelnummer; Kartennummer
	 * @param data
	 */
	public void copyValuesFrom(Data data) {
		closeConnection = data.closeConnection;
		name = data.name;
		message = data.message;
		ready = data.ready;
		playerBounds.setRect(data.playerBounds);
		levelNumber = data.levelNumber;
		mapNumber = data.mapNumber;
	}


}
