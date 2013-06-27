package Main;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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
	public int[][] enemyArray;
	public int[][] itemArray;

	private int levelNumber, mapNumber;
	private String mapUrl;
	private String mapUrlsPath = "./res/maps/map_urls.txt";
	public ArrayList<Level> mapUrls;
	private HashMap<Integer, Boolean> walkable =  new HashMap<>();
	public HashMap<Integer, BufferedImage> texture =  new HashMap<>();
	public HashMap<Integer, String> textureName = new HashMap<>();
	
	
	public Map(int levelNumber, int mapNumber, int rows, int cols) {
		// Setzt die Zeilen- und Spaltenanzahl
		this.rows = rows;
		this.cols = cols;
		// Setzt die initiale Levelnummer
		this.levelNumber = levelNumber;
		// Setzt die initiale Kartennummer
		this.mapNumber = mapNumber;
		
		// Setzt Pfade zu den jeweiligen Kartendateien ins Array mapUrls
		mapUrls= new ArrayList<>();
		readMapPathsFromFile();
		
		// Erstellt die Arrays mapArray, enemyArray, itemArray,
		// in denen die eingelesen Elemente stehen werden
		mapArray = new int[rows][cols];
		enemyArray = new int[rows][cols];
		itemArray = new int[rows][cols];
		
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
	
	private void readMapPathsFromFile() {
		try (BufferedReader br = new BufferedReader(new FileReader(mapUrlsPath));) {
			String tempString;
			String[] tempArray;
			
			for(int i = 0; ((tempString = br.readLine()) != null) && (tempArray = tempString.split("\\|")).length == 3; i++) {
				mapUrls.add(i, new Level(Integer.parseInt(tempArray[0]), Integer.parseInt(tempArray[1]), tempArray[2]));
			}
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void writeBackMapUrls() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(mapUrlsPath))) {
			Level l;
			
			for (int i = 0; i < mapUrls.size(); i++) {
				l = mapUrls.get(i);
				bw.write(String.format("%02d|%02d|%s\n", l.level, l.map, l.path));
			}
		} catch (IOException e) {e.printStackTrace();}
	}
	
	// Fuellt 2 HashMaps mit den Informationen "begehbar" und "textur"
	// zu jedem entsprechenden Karten-Kacheltyp
	private void initializeHashMaps() {
		walkable.put(0, true);	// Boden
		walkable.put(1, false);	// Wand
		walkable.put(2, true);	// Begehbare-Wand
		walkable.put(3, false); // NPC
		walkable.put(5, true);	// Shop
		walkable.put(7, true); 	// Falle
		walkable.put(8, true);	// Start
		walkable.put(9, true);	// Ziel
		
		File tex_file = new File("./res/img/tiles/texture_array.png");
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(tex_file));) {
			BufferedImage sprite = ImageIO.read(in);
			
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
			texture.put(5, tex_grass);
			texture.put(7, tex_trap);
			texture.put(8, tex_start);
			texture.put(9, tex_finish);
			
			textureName.put(0, "Boden");
			textureName.put(1, "Wand");
			textureName.put(2, "Begehbare Wand");
			textureName.put(3, "NPC");
			textureName.put(5, "Shop");
			textureName.put(7, "Falle");
			textureName.put(8, "Start");
			textureName.put(9, "Ziel");
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void clearMap(int value) {
		for (int row = 0; row < rows; row++) {
			Arrays.fill(mapArray[row], value);
			Arrays.fill(enemyArray[row], value);
			Arrays.fill(itemArray[row], value);
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
		int count = 0;
		for (int i = 0, lastValue = -1; i < mapUrls.size(); i++) {
			if (lastValue != mapUrls.get(i).level) {
				count++;
				lastValue = mapUrls.get(i).level;
			}
		}
		return count;
	}

	// Gibt die Anzahl an Karten in bestimmtem Level zurueck
	public int getCountOfMapsByLevel(int level) {
		int count = 0;
		for (int i = 0; i < mapUrls.size(); i++) {
			if (mapUrls.get(i).level == level)
				count++;
		}
		return count;
	}
	
	private String getMapUrlByLevelAndMap(int levelNumber, int mapNumber) {
		for (int i = 0; i < mapUrls.size(); i++) {
			Level l = mapUrls.get(i);
			if ((l.level == levelNumber) && (l.map == mapNumber)) {
				return l.path;
			}
		}
		return "";
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
		mapUrl = getMapUrlByLevelAndMap(levelNumber, mapNumber);
		readMapByFile(mapUrl);
	}
	
	public void readMapByFile(String path) {
		try (BufferedReader br = new BufferedReader(new FileReader(path));) {
			if (br.readLine().contains("map")) {
				bufferedReaderToArray(br, mapArray);
			}
			if (br.readLine().contains("enemies")) {
				bufferedReaderToArray(br, enemyArray);
			}
			if (br.readLine().contains("items")) {
				bufferedReaderToArray(br, itemArray);
			}
		} catch (NumberFormatException | IOException e) {e.printStackTrace();}
	}
	
	private void bufferedReaderToArray(BufferedReader br, int[][] destinationArray) throws NumberFormatException, IOException {
		String tempLine = new String();
		String[] tempArray;
		
		for (int row = 0; row < rows; row++) {
			if (((tempLine = br.readLine()) != null) && (tempArray = tempLine.split(" ")).length == cols) {
				for (int col = 0; col < cols; col++) {
					destinationArray[row][col] = Integer.parseInt(tempArray[col]);
				}
			} else {
				Arrays.fill(destinationArray[row], 0);
			}
		}
	}
	
	public void saveMapToFile(String path) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path));) {
			arrayToBufferedWriter(bw, mapArray, "map");
			arrayToBufferedWriter(bw, enemyArray, "enemies");
			arrayToBufferedWriter(bw, itemArray, "items");
		} catch (IOException e) {e.printStackTrace();}
	}
	
	private void arrayToBufferedWriter(BufferedWriter bw, int[][] sourceArray, String arrayTitle) throws IOException {
		bw.write(arrayTitle+"\n");
		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < rows; row++) {
			// Manuelle Version von Array.implode(sourceArray, " ")
			for (int col = 0; col < cols-1; col++) {
				sb.append(String.format("%02d ", sourceArray[row][col]));
			}
			sb.append(String.format("%02d", sourceArray[row][cols-1]));
			
			sb.append("\n");
			bw.write(sb.toString());
			sb.setLength(0);
		}
	}

	// Sucht und uebergibt eine Position im array mit dem Wert value
	public Point2D singleSearch(int[][] array, int value) {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (array[row][col] == value) {
					return (new Point2D.Double(col * 32, row * 32));
				}
			}
		}
		return null;
	}

	public ArrayList<Point2D> multiSearch(int[][] array, int value) {
		ArrayList<Point2D> entityPositions = new ArrayList<Point2D>();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (array[row][col] == value) {
					entityPositions.add(new Point2D.Double(col * 32, row * 32));
				}
			}
		}
		return entityPositions;
	}

}
