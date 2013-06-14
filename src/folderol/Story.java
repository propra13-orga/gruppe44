package folderol;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Story {
	Houston houston;
	Map map;

	private String storyUrl;
	private String[] storyUrls;
	private BufferedReader storyText;
	private int mapNumber;
	private int levelNumber;
	private int storyCounter;
	private int storyNumber;

	public Story(int mapNumber, int levelNumber, int storyCounter) {

		storyNumber = (levelNumber * 100) + (mapNumber * 10) + storyCounter;

		/*
		 * zur Veranschaulichung wie es gemeint ist. storyUrls = new String[9];
		 * //Level 1 Raum 1 storyUrls[0] = "./res/story/story000.txt"; Raum 2
		 * storyUrls[1] = "./res/story/story010.txt"; storyUrls[2] =
		 * "./res/story/story020.txt"; //Level 2 storyUrls[3] =
		 * "./res/story/story100.txt"; storyUrls[4] =
		 * "./res/story/story110.txt"; Raum 1 mit storyCounter = 1 storyUrls[5]
		 * = "./res/story/story111.txt"; //Level 3 storyUrls[6] =
		 * "./res/story/story200.txt"; storyUrls[7] =
		 * "./res/story/story210.txt"; storyUrls[8] =
		 * "./res/story/story220.txt";
		 */

		try {
			initializeStory();
		} catch (IOException e) {e.printStackTrace();}
	}

	private void initializeStory() throws IOException {
		storyNumber = (levelNumber * 100) + (mapNumber * 10) + storyCounter;
		storyUrl = "./res/story/story" + storyNumber + ".txt";
		readStoryFile();

	}

	// Erhoeht den StoryCounter. Nuetzlich wenn sich der Text in einem Raum
	// aendern soll.
	int nextStory() {
		storyCounter++;
		return storyCounter;

	}

	void renewStory(int levelNumber, int mapNumber) {
		this.mapNumber = mapNumber;
		this.levelNumber = levelNumber;
		this.storyCounter = 0;
		try {
			initializeStory();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void readStoryFile() throws IOException {

		FileReader fr = new FileReader(storyUrl);
		storyText = new BufferedReader(fr);

	}
}
