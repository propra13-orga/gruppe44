package folderol;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Map {

	private int rows;
	private int cols;
	int[][] mapArray;

	 private int mapNumber;
	private String mapUrl;
	private String[] mapUrls;
	private BufferedReader fileContents;
	
	
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
		
		initializeMap();		
	}
	
	
	void renewMap(int mapNumber) {
		this.mapNumber = mapNumber;
		initializeMap();
	}
	
	void renewMap() {
		initializeMap();
	}

	private void initializeMap() {
		mapUrl = mapUrls[mapNumber];
		readMapFile();
		assignFileContentToMapArray();
	}

	public void drawObjects(Graphics2D g) {
		
		// Hier noch alles optimieren
		// TODO Hier noch alles optimieren
		int M = 32;
		int Off = 32;
		int wid = 31;
		int hei = 31;
		for (int i = 0; i < mapArray.length; i++) {
			for (int j = 0; j < mapArray[i].length; j++) {
				g.setColor(new Color(255 - 60*mapArray[i][j], 255 - 40*mapArray[i][j], 255));
				g.fillRect(j * M, i * M + Off, wid, hei);
				// g.drawRect(j*M+Off, i*M+Off, wid-1, hei-1);
				// g.setColor(Color.WHITE);
				// g.drawString("" + mapArray[i][j], j * M, i * M + Off + 12);
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
		;
	}
	
	private void assignFileContentToMapArray() {
		String tempLine = " ";
		String[] tempArray;

		try {

			for (int row = 0; ((tempLine = fileContents.readLine()) != null) && row <= rows; row++) {
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
