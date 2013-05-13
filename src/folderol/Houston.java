package folderol;

/**
 * 
 * s = Spawn/Start
 * f = Finish/Ziel
 * g = Ground/Boden
 * w = Wall/Wand
 * e = Enemy/Gegner
 * t = Trap/Falle
 * p = Player/Spieler
 * 
*/



import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Houston implements ActionListener, Runnable {
	
	private int height;
	private int width;

	private boolean gameIsRunning = false;
	private boolean gameOver = false;
	long delta = 0;
	long last = 0;
	public static long fps = 0;

	JFrame frame;
	JPanel cards;
	JPanel card1, card2, card3, card4, card5;
	CardLayout cl;
	
	private JButton 
	c1b1, //Neues Spiel
	c1b2, //Einstellungen
	c1b3, //Mitwirkende
	c1b4, //Beenden
	c2b1, //zum Hauptmenue
	c4b1, //zurueck ins Spiel
	c4b2, //zum Hauptmenue
	c5b1; //zum Hauptmenue

	// strings to identify each card (jpanel) in cards (jpanel:cardlayout)
	final static String STARTMENU = "Hauptmenue";
	final static String SETTINGS = "Einstellungen";
	final static String GAME = "Spielfenster";
	final static String INGAMEMENU = "Spielmenue";
	final static String CREDITS = "Mitwirkende";

	// ------------------------------------------------------------
	public static void main(String[] args) {
		new Houston(768, 640);
	}

	// ------------------------------------------------------------

	public Houston(int width, int height) {
		this.width = width;
		this.height = height;
		
		// Card 1 - STARTMENU
		card1 = card1();

		// Card 2 - SETTINGS
		card2 = card2();

		// Card 3 - GAME
		card3 = card3();

		// Card 4 - INGAMEMENU
		card4 = card4();

		// Card 5 - CREDITS
		card5 = card5();

		// Create new Panel and add all Sub-Panel
		cl = new CardLayout();
		cards = new JPanel(cl);
		cards.setPreferredSize(new Dimension(this.width, this.height));
		cards.add(card1, STARTMENU);
		cards.add(card2, SETTINGS);
		cards.add(card3, GAME);
		cards.add(card4, INGAMEMENU);
		cards.add(card5, CREDITS);

		// Create Main Window
		frame = new JFrame("Folderol");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setLocationRelativeTo(null);
		frame.setLocation(0, 0);
		frame.add(cards);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);

		// First Card displayed: STARTMENU; SETTINGS; GAME; INGAMEMENU
		cl.show(cards, STARTMENU);
		
		// initialize all necessary crap here
		initializeCrap();
		
		// jump into the game loop
		Thread th = new Thread(this);
		th.start();
		
	}
	
	
	
	private void initializeCrap() {
		// TODO Auto-generated method stub
		
		last = System.nanoTime();
		
	}
	
	
	
	@Override
	public void run() {
		// GameLoop - Work In Progress
		while(frame.isVisible()) {
		
			if (gameIsRunning) {
				computeDelta();
				card3.repaint();
			}
			
			
			try {
				Thread.sleep(1000 / 30);
			} catch (InterruptedException e) {}
		}
	}
	
	
	

	private void computeDelta() {
		delta = System.nanoTime() - last;
		last = System.nanoTime();
		fps = ((long) 1e9) / delta;
	}

	private JPanel card1() {
		card1 = new JPanel();
		card1.add(new JLabel(STARTMENU));

		c1b1 = new JButton("Neues Spiel");
		c1b1.addActionListener(this);
		card1.add(c1b1);

		c1b2 = new JButton("Einstellungen");
		c1b2.addActionListener(this);
		card1.add(c1b2);

		c1b3 = new JButton("Mitwirkende");
		c1b3.addActionListener(this);
		card1.add(c1b3);

		c1b4 = new JButton("Beenden");
		c1b4.addActionListener(this);
		card1.add(c1b4);
		return card1;
	}
	
	private JPanel card2() {
		card2 = new JPanel();
		card2.add(new JLabel(SETTINGS));

		c2b1 = new JButton("-> Hauptmenue");
		c2b1.addActionListener(this);
		card2.add(c2b1);
		return card2;
	}
	
	private JPanel card3() {
		// card3 also functions as the canvas for the game 
		card3 = new GamePanel();
		card3.add(new JLabel(GAME));

		// replace this button by Act. List. for VK_ESCAPE in GamePanel
		JButton c3b1 = new JButton("-> Spielmenue");
		c3b1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cl.show(cards, INGAMEMENU);
				gameIsRunning = false;
			}
		});
		card3.add(c3b1);
		
//		card3.getInputMap().put(KeyStroke.getKeyStroke("F2"), "doSomething");
//		card3.getActionMap().put("doSomething", anAction);
		
		return card3;
	}

	private JPanel card4() {
		card4 = new JPanel();
		card4.add(new JLabel(INGAMEMENU));

		c4b1 = new JButton("zurueck ins Spiel");
		c4b1.addActionListener(this);
		card4.add(c4b1);

		c4b2 = new JButton("-> Hauptmenue");
		c4b2.addActionListener(this);
		card4.add(c4b2);
		return card4;
	}

	private JPanel card5() {
		card5 = new JPanel();
		card5.add(new JLabel(CREDITS));

		c5b1 = new JButton("-> Hauptmenue");
		c5b1.addActionListener(this);
		card5.add(c5b1);
		return card5;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// handling the click events on menu buttons
		JButton buttonClicked = (JButton) e.getSource();
		if (buttonClicked == c1b1) {
			cl.show(cards, GAME);
			gameIsRunning = true;
		} else if (buttonClicked == c1b2) {
			cl.show(cards, SETTINGS);
		} else if (buttonClicked == c1b3) {
			cl.show(cards, CREDITS);
		} else if (buttonClicked == c1b4) {
			System.exit(0);
		} else if (buttonClicked == c2b1) {
			cl.show(cards, STARTMENU);
		} else if (buttonClicked == c4b1) {
			cl.show(cards, GAME);
			gameIsRunning = true;
		} else if (buttonClicked == c4b2) {
			cl.show(cards, STARTMENU);
			gameOver = true;
		} else if (buttonClicked == c5b1) {
			cl.show(cards, STARTMENU);
		}
		
	}

}
