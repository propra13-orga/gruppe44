package folderol;

import java.awt.Color;
import java.awt.Graphics2D;
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
		this.rows = rows;
		this.cols = cols;
		this.mapNumber = mapNumber;
		
		// setze die Pfade zu den Kartendateien in mapUrls
		mapUrls = new String[3];
		mapUrls[0] = "./src/etc/maps/map01.txt";
		mapUrls[1] = "./src/etc/maps/map02.txt";
		mapUrls[2] = "./src/etc/maps/map03.txt";
		mapArray = new int[rows][cols];
		
		// ititializeHashMaps();
		initializeMap();		
	}
	
	
	private void ititializeHashMaps() {
		walkable.put(8, true); // Start
		walkable.put(9, true); // Ziel
		walkable.put(1, true); // Wand
		walkable.put(0, false); // Booden
		try {
			texture.put(8, ImageIO.read(new File("./src/etc/img/start.png")));
			texture.put(9, ImageIO.read(new File("./src/etc/img/finish.png")));
			texture.put(1, ImageIO.read(new File("./src/etc/img/wall.png")));
			texture.put(0, ImageIO.read(new File("./src/etc/img/ground.png")));
		} catch (IOException e) {e.printStackTrace();}
	}


	void renewMap(int mapNumber) {
		this.mapNumber = mapNumber;
		initializeMap();
	}
	
	void renewMap() {
		initializeMap();
	}

	private void initializeMap() {
		ititializeHashMaps();
		mapUrl = mapUrls[mapNumber];
		readMapFile();
		assignFileContentToMapArray();
	}

	public void drawObjects(Graphics2D g) {
		
		// Hier noch alles optimieren
		// TODO Hier noch alles optimieren
		int value;
		
		for (int i = 0; i < mapArray.length; i++) {
			for (int j = 0; j < mapArray[i].length; j++) {
				value = mapArray[i][j];
				// g.fillRect(j * 32, i * 32 + Off, wid, hei);
				g.drawImage(texture.get(value), j * 32, i * 32 + 32, null);
				// g.drawRect(j * 32 + Off, i * 32 + Off, wid - 1, hei - 1);
				// g.setColor(Color.BLACK);
				// g.drawString("" + value, j * 32, i * 32 + 32 + 12);
			}
		}
	}

	private void readMapFile() {
		// Liest den Dateiinhalt von mapUrl und speichert ihn in fileContens
		try {
			FileReader fr = new FileReader(mapUrl);
			fileContents = new BufferedReader(fr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void assignFileContentToMapArray() {
		// Speichert den gelesenen Dateiinhalt in das mapArray
		String tempLine = " "; 
		String[] tempArray;
		

		try {

			for (int row = 0; ((tempLine = fileContents.readLine()) != null)
					&& row <= rows; row++) {
				
				tempArray = tempLine.split(" ");
				int tempLength = tempArray.length;
				
				for (int col = 0; col < tempLength; col++) {
					mapArray[row][col] = Integer.parseInt(tempArray[col]);
				} // Ende der Spaltenschleife

			} // Ende der Reihenschleife

		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}

	}
	
}
