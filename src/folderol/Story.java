package folderol;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

public class Story {
	private Houston houston;
	private Map map;
	String storyUrl;
	private String[] storyUrls;
	BufferedReader storyBuffer;
	String[] storyText;
	private int mapNumber;
	private int levelNumber;

	public Story(int mapNumber, int levelNumber, int storyCounter) {
		try {
			initializeStory();
		} catch (IOException e) {e.printStackTrace();}
	}

	private void initializeStory() throws IOException {
		storyUrl = "./res/story/story.txt";
		readStoryFile();
		assignFileContentToStoryText();

	}
	
	void renewStory(int levelNumber, int mapNumber) {
		this.mapNumber = mapNumber;
		this.levelNumber = levelNumber;
		try {
			initializeStory();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void readStoryFile() throws IOException {

		FileReader fr = new FileReader(storyUrl);
		storyBuffer = new BufferedReader(fr);

	}
	
	private void assignFileContentToStoryText() {
		String tempLine;
		

		try {
			for (int row = 0; ((tempLine = storyBuffer.readLine()) != null); row++) {
				storyText[row]= tempLine;
				} 

		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}
