package Main;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class Quest {

	private Player player;

	private String questUrl;
	private String[] questUrls;
	private BufferedReader questBuffer;
	static ArrayList<String> questText;
	static String solution;
	static int complength;
	String tempcl;
	private int mapNumber;

	public Quest(int mapNumber, Houston houston) {
		this.mapNumber = mapNumber;
		this.player = houston.player;

		questUrls = new String[8];
		questUrls[0] = "./res/quest/quest1.txt";
		questUrls[1] = "./res/quest/quest2.txt";
		questUrls[2] = "./res/quest/quest3.txt";
		questUrls[3] = "./res/quest/quest4.txt";

		questText = new ArrayList<String>();

		try {
			initializeQuest();
		} catch (IOException e) {e.printStackTrace();}
	}

	private void initializeQuest() throws IOException {
		questUrl = questUrls[mapNumber];
		readQuestFile();
		assignFileContentToQuestText();
		solution = questText.get(1);
		tempcl = questText.get(2);
		complength = Integer.parseInt(tempcl);
	}

	public void renewQuest(int mapNumber) {
		this.mapNumber = mapNumber;
		try {
			initializeQuest();
		} catch (IOException e) {e.printStackTrace();}
	}

	private void readQuestFile() throws IOException {
		FileReader fr = new FileReader(questUrl);
		questBuffer = new BufferedReader(fr);
	}

	private void assignFileContentToQuestText() {
		String tempLine;

		try {
			for (int row = 0; ((tempLine = questBuffer.readLine()) != null); row++) {
				questText.add(row, tempLine);
			}
		} catch (NumberFormatException | IOException e) {e.printStackTrace();}
	}

	public void doQuest() {
		final JDialog questDialog = new JDialog();
		final JDialog bitchDialog = new JDialog();
		JButton button1 = new JButton("Klick Mich");
		final JTextField textField = new JTextField("Eingabe" , 1 );
		questDialog.setLayout(null);
		JLabel questLabel = new JLabel(questText.get(0));
		JLabel bitchPlease = new JLabel(new ImageIcon("./res/img/albert.jpg"));

		questLabel.setBounds(50, 0, 390, 100);
		textField.setBounds(150,70,200,30);
		button1.setBounds(190, 120, 120, 30);


		ActionListener textMsg = new ActionListener() {



			@Override
			public void actionPerformed(ActionEvent e) {
				if (questCheck(textField.getText()) == true) {
					player.increaseExperience((mapNumber+1) * 10);
					questDialog.dispose();
				}
				else {
					bitchDialog.setVisible(true);
					}
				}




		};

		questDialog.setTitle("R\u00e4tsel");
		questDialog.setSize(500, 250);
		questDialog.setModal(true);
		questDialog.add(button1);
		questDialog.add(textField);
		questDialog.add(questLabel);
		questDialog.setResizable(false);
		questDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		bitchDialog.setSize(501, 382);
		bitchDialog.setModal(true);
		bitchDialog.setResizable(false);
		bitchDialog.add(bitchPlease);

		button1.addActionListener(textMsg);

		textField.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent evt) {
				try {
					Thread.sleep(1200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				textField.setText("");

		        }
		});

		questDialog.setVisible(true);
	}

	 static boolean questCheck(String input){
		if ((input.equals("/quit")) == true ){
			return true;}
		else if((solution.contains(input) == true) && input.length() >= complength){
			return true;
		}
			return false;

	}

}
