package folderol;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Map {

	private int rows;
	private int cols;
	int[][] mapArray;

	private int mapNumber;
	private String mapUrl;
	private String[] mapUrls;
	private BufferedReader fileContents;
	private HashMap<Integer, BufferedImage> texture =  new HashMap<Integer, BufferedImage>();
	private HashMap<Integer, Boolean> walkable =  new HashMap<Integer, Boolean>();
	
	
	public Map(int mapNumber, int rows, int cols) {
		// Setzt die Zeilen- und Spaltenanzahl
		this.rows = rows;
		this.cols = cols;
		// Setzt die initiale Kartennummer
		this.mapNumber = mapNumber;
		
		// Setzt Pfade zu den jeweiligen Kartendateien ins Array mapUrls
		mapUrls = new String[3];
		mapUrls[0] = "./src/etc/maps/map01.txt";
		mapUrls[1] = "./src/etc/maps/map02.txt";
		mapUrls[2] = "./src/etc/maps/map03.txt";
		// Erstellt das Array mapArray, in dem die eingelesen Karte stehen wird
		mapArray = new int[rows][cols];
		
		// ititializeHashMaps();
		initializeMap();		
	}
	
	// Fuellt 2 HashMaps mit den Informationen "begehbar" und "textur"
	// zu jedem entsprechenden Karten-Kacheltyp
	private void ititializeHashMaps() {
		walkable.put(8, true); // Start
		walkable.put(9, true); // Ziel
		walkable.put(1, true); // Wand
		walkable.put(7, true); // Falle
		walkable.put(0, false); // Booden
		try {
			texture.put(8, ImageIO.read(new File("./src/etc/img/start.png")));
			texture.put(9, ImageIO.read(new File("./src/etc/img/finish.png")));
			texture.put(1, ImageIO.read(new File("./src/etc/img/wall.png")));
			texture.put(7, ImageIO.read(new File("./src/etc/img/trap.png")));
			texture.put(0, ImageIO.read(new File("./src/etc/img/ground.png")));
		} catch (IOException e) {e.printStackTrace();}
	}
	
	// Gibt die aktuelle Kartennummer zurueck
	int getMapNumber() {
		return mapNumber;
	}
	
	// Gibt die Anzahl der Karten zurueck
	int getCountOfMaps() {
		return mapUrls.length;
	}

	// Erneuert/ersetzt die aktuelle Karte durch die Karte,
	// die durch mapNumber referenzierte ist
	void renewMap(int mapNumber) {
		this.mapNumber = mapNumber;
		initializeMap();
	}
	
	// Laedt die aktuelle Karte neu
	void renewMap() {
		renewMap(mapNumber);
	}

	// Baut eine aktuelle/neue Karte auf
	private void initializeMap() {
		ititializeHashMaps();
		mapUrl = mapUrls[mapNumber];
		readMapFile();
		assignFileContentToMapArray();
	}

	// Zeichnet die Karte
	public void drawObjects(Graphics2D g) {
		
		int value;
		
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				value = mapArray[row][col];
				// g.fillRect(col * 32, row * 32 + 32, 32, 32);
				g.drawImage(texture.get(value), col * 32, row * 32 + 32, null);
				// g.drawRect(col * 32 + 32, row * 32 + 32, 32 - 1, 32 - 1);
				// g.setColor(Color.BLACK);
				// g.drawString("" + value, col * 32, row * 32 + 32 + 12);
			}
		}
	}

	// Laedt die durch mapUrl spezifizierte Datei und speichert ihren
	// Inhalt in fileContents
	private void readMapFile() {
		try {
			FileReader fr = new FileReader(mapUrl);
			fileContents = new BufferedReader(fr);
		} catch (IOException e) {e.printStackTrace();}
	}
	
	// Speichert den in readMapFile() gelesenen Dateiinhalt in mapArray
	private void assignFileContentToMapArray() {
		// Speichert temporaer eine Zeile beim zeilenweisen Lesen der Kartendatei
		String tempLine = " "; 
		// Speichert temporaer alle Kartenelemente einer Zeile
		String[] tempArray;
		

		try {
			// Iteriert ueber jede Zeile "row", solange es noch Zeilen im Dateiinhalt gibt 
			// und noch nicht alle Zeilen des mapArray befuellt sind
			for (int row = 0; ((tempLine = fileContents.readLine()) != null) && (row < rows); row++) {
				
				// Splittet eine Zeile in einzelne Arrayelemente auf
				tempArray = tempLine.split(" ");
				
				// Iteriert ueber jede Spalte,
				// solange noch nicht alle Spalten "col" einer Zeile "row" befuellt sind
				for (int col = 0; col < cols; col++) {
					// Speichert schlussendlich jedes Element der Kartenmatrix in dem
					// entsprechenden mapArray Eintrag an der Stelle [Reihe][Spalte]
					mapArray[row][col] = Integer.parseInt(tempArray[col]);
				} // Ende der Spaltenschleife
			} // Ende der Reihenschleife

		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	public Point2D singleSearch(int value) {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (mapArray[row][col] == 8) {
					return (new Point2D.Double(col*32, (row+1)*32));
				}
			}
		}
		return null;
	}
	
}
