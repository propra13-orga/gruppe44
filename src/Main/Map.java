package Main;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Map {

	private int rows;
	private int cols;
	public int[][] mapArray;

	private int levelNumber, mapNumber;
	private String mapUrl;
	private String[][] mapUrls;
	private BufferedImage sprite;
	public HashMap<Integer, BufferedImage> texture =  new HashMap<Integer, BufferedImage>();
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
		
		initializeHashMaps();
		clearMap(0);
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
	private void initializeHashMaps() {
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
			File tex_file = new File("./res/img/tiles/texture_array.png");
			sprite = ImageIO.read(tex_file);
			
			BufferedImage tex_ground = sprite.getSubimage(0, 96, 32, 32);
			BufferedImage tex_wall = sprite.getSubimage(0, 0, 32, 32);
			BufferedImage tex_npc = sprite.getSubimage(32, 50, 32, 46);
			BufferedImage tex_grass = sprite.getSubimage(0, 128, 32, 32);
			BufferedImage tex_trap = sprite.getSubimage(0, 32, 32, 32);
			BufferedImage tex_start = sprite.getSubimage(0, 64, 32, 32);
			BufferedImage tex_finish = sprite.getSubimage(32, 110, 32, 50);
			
			texture.put(0, tex_ground);
			texture.put(1, tex_wall);
			texture.put(2, tex_wall);
			texture.put(3, tex_npc);
			texture.put(4, tex_ground);
			texture.put(5, tex_grass);
			texture.put(6, tex_ground);
			texture.put(7, tex_trap);
			texture.put(8, tex_start);
			texture.put(9, tex_finish);
			texture.put(30, tex_ground);
			texture.put(31, tex_ground);
			texture.put(32, tex_ground);
			texture.put(33, tex_ground);
			texture.put(34, tex_ground);
			texture.put(35, tex_ground);
			texture.put(90, tex_ground);
			texture.put(91, tex_ground);
			texture.put(92, tex_ground);
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void clearMap(int value) {
		for (int row = 0; row < rows; row++) {
			Arrays.fill(mapArray[row], value);
		}
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
	
	// Laedt die aktuelle Karte neu
	public void renewMap() {
		renewMap(levelNumber, mapNumber);
	}

	// Erneuert/ersetzt die aktuelle Karte durch die Karte,
	// die durch mapNumber referenzierte ist
	public void renewMap(int levelNumber, int mapNumber) {
		this.levelNumber = levelNumber;
		this.mapNumber = mapNumber;
		initializeMap();
	}

	// Baut eine aktuelle/neue Karte auf
	private void initializeMap() {
		initializeHashMaps();
		clearMap(0);
		mapUrl = mapUrls[levelNumber][mapNumber];
		readMapByFile(mapUrl);
	}
	
	// Laedt die durch path spezifizierte Datei und 
	// speichert den in gelesenen Dateiinhalt im mapArray
	public void readMapByFile(String path) {
		// Speichert temporaer eine Zeile beim zeilenweisen Lesen der Kartendatei
		String tempLine = " "; 
		// Speichert temporaer alle Kartenelemente einer Zeile
		String[] tempArray;
		

		try (BufferedReader br = new BufferedReader(new FileReader(path));) {
			// Iteriert ueber jede Zeile "row", solange es noch Zeilen im Dateiinhalt gibt 
			// und noch nicht alle Zeilen des mapArray befuellt sind
			for (int row = 0; ((tempLine = br.readLine()) != null) && (row < rows); row++) {
				
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

		} catch (NumberFormatException | IOException e) {e.printStackTrace();}
	}
	
	public void saveMapToFile(String path) {
		try (BufferedWriter wr = new BufferedWriter(new FileWriter(path));) {
			StringBuilder sb = new StringBuilder();
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					sb.append(String.format("%02d ", mapArray[row][col]));
				}
				
				sb.append("\n");
				wr.write(sb.toString());
				sb.setLength(0);
			}
		} catch (Exception e) {e.printStackTrace();}
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
		ArrayList<Point2D> entityPositions  = new ArrayList<Point2D>( );
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (mapArray[row][col] == value){
					entityPositions.add(new Point2D.Double(col*32, row*32));
				}
			}
		}
		return entityPositions;
	}

}
