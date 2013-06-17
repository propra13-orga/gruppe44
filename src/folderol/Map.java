package folderol;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Map {

	private int rows;
	private int cols;
	public int[][] mapArray;

	private int levelNumber, mapNumber;
	private String mapUrl;
	private String[][] mapUrls;
	private BufferedReader fileContents;
	private HashMap<Integer, BufferedImage> texture =  new HashMap<Integer, BufferedImage>();
	private HashMap<Integer, Boolean> walkable =  new HashMap<Integer, Boolean>();
	
	
	public Map(int levelNumber, int mapNumber, int rows, int cols) {
		// Setzt die Zeilen- und Spaltenanzahl
		this.rows = rows;
		this.cols = cols;
		// Setzt die initiale Levelnummer
		this.levelNumber = levelNumber;
		// Setzt die initiale Kartennummer
		this.mapNumber = mapNumber;
		
		// Setzt Pfade zu den jeweiligen Kartendateien ins Array mapUrls
		mapUrls = new String[3][4];
		mapUrls[0][0] = "./res/maps/map01_01.txt";
		mapUrls[0][1] = "./res/maps/map01_02.txt";
		mapUrls[0][2] = "./res/maps/map01_03.txt";
		mapUrls[0][3] = "./res/maps/map01_04.txt";
		mapUrls[1][0] = "./res/maps/map02_01.txt";
		mapUrls[1][1] = "./res/maps/map02_02.txt";
		mapUrls[1][2] = "./res/maps/map02_03.txt";
		mapUrls[1][3] = "./res/maps/map02_04.txt";
		mapUrls[2][0] = "./res/maps/map03_01.txt";
		mapUrls[2][1] = "./res/maps/map03_02.txt";
		mapUrls[2][2] = "./res/maps/map03_03.txt";
		mapUrls[2][3] = "./res/maps/map03_04.txt";
		// Erstellt das Array mapArray, in dem die eingelesen Karte stehen wird
		mapArray = new int[rows][cols];
		
		// initializeHashMaps();
		initializeMap();		
	}
	
	// Zeichnet die Karte
	public void drawObjects(Graphics2D g) {
		
		int value;
		
		
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				value = mapArray[row][col];
				g.drawImage(texture.get(value), col * 32, row * 32 - texture.get(value).getHeight() + 32 , null);
			}
		}
	}
	
	// Fuellt 2 HashMaps mit den Informationen "begehbar" und "textur"
	// zu jedem entsprechenden Karten-Kacheltyp
	private void ititializeHashMaps() {
		walkable.put(0, true);	// Boden
		walkable.put(1, false);	// Wand
		walkable.put(2, true);	// Begehbare-Wand
		walkable.put(4, true);	// GegnerVertikal
		walkable.put(3, false); // NPC
		walkable.put(5, true);	// Shop
		walkable.put(6, true);	// GegnerHorizontal
		walkable.put(7, true); 	// Falle
		walkable.put(8, true);	// Start
		walkable.put(9, true);	// Ziel
		try {
			texture.put(0, ImageIO.read(new File("./res/img/tiles/ground.png")));
			texture.put(1, ImageIO.read(new File("./res/img/tiles/wall.png")));
			texture.put(2, ImageIO.read(new File("./res/img/tiles/wall.png")));
			texture.put(3, ImageIO.read(new File("./res/img/tiles/npc.png")));
			texture.put(4, ImageIO.read(new File("./res/img/tiles/ground.png")));
			texture.put(5, ImageIO.read(new File("./res/img/tiles/grass.png")));
			texture.put(6, ImageIO.read(new File("./res/img/tiles/ground.png")));
			texture.put(7, ImageIO.read(new File("./res/img/tiles/trap.png")));
			texture.put(8, ImageIO.read(new File("./res/img/tiles/start.png")));
			texture.put(9, ImageIO.read(new File("./res/img/tiles/finish.png")));
			texture.put(30, ImageIO.read(new File("./res/img/tiles/ground.png")));
			texture.put(31, ImageIO.read(new File("./res/img/tiles/ground.png")));
			texture.put(32, ImageIO.read(new File("./res/img/tiles/ground.png")));
			texture.put(33, ImageIO.read(new File("./res/img/tiles/ground.png")));
			texture.put(34, ImageIO.read(new File("./res/img/tiles/ground.png")));
			texture.put(35, ImageIO.read(new File("./res/img/tiles/ground.png")));
			texture.put(90, ImageIO.read(new File("./res/img/tiles/ground.png")));
			texture.put(91, ImageIO.read(new File("./res/img/tiles/ground.png")));
			texture.put(92, ImageIO.read(new File("./res/img/tiles/ground.png")));
		} catch (IOException e) {e.printStackTrace();}
	}
	
	// Gibt die aktuelle Kartennummer zurueck
	public int getMapNumber() {
		return mapNumber;
	}

	// Gibt die aktuelle Levelnummer zurueck
	public int getLevelNumber() {
		return levelNumber;
	}

	// Gibt die Anzahl an Level zurueck
	public int getCountOfLevel() {
		return mapUrls.length;
	}

	// Gibt die Anzahl an Karten in bestimmtem Level zurueck
	public int getCountOfMapsByLevel(int level) {
		return mapUrls[level].length;
	}

	// Gibt die Anzahl an Karten im aktuellen Level zurueck
	public int getCountOfMapsByLevel() {
		return getCountOfMapsByLevel(levelNumber);
	}

	// Erneuert/ersetzt die aktuelle Karte durch die Karte,
	// die durch mapNumber referenzierte ist
	public void renewMap(int levelNumber, int mapNumber) {
		this.levelNumber = levelNumber;
		this.mapNumber = mapNumber;
		initializeMap();
	}

	// Laedt die aktuelle Karte neu
	public void renewMap() {
		renewMap(levelNumber, mapNumber);
	}

	// Baut eine aktuelle/neue Karte auf
	private void initializeMap() {
		ititializeHashMaps();
		mapUrl = mapUrls[levelNumber][mapNumber];
		readMapFile();
		assignFileContentToMapArray();
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

	// Sucht und uebergibt eine Position im mapArray mit dem Wert value
	public Point2D singleSearch(int value) {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (mapArray[row][col] == value) {
					return (new Point2D.Double(col * 32, row * 32));
				}
			}
		}
		return null;
	}

	public ArrayList<Point2D> multiSearch(int value) {
		ArrayList<Point2D> enemyPositions  = new ArrayList<Point2D>( );
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (mapArray[row][col] == value){
					enemyPositions.add(new Point2D.Double(col*32, row*32));
				}
			}
		}
		return enemyPositions;
	}

}
