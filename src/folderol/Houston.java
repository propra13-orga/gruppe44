package folderol;

/**
 * 
 * 8 = Spawn/Start
 * 9 = Finish/Ziel
 * 1 = Ground/Boden
 * 0 = Wall/Wand
 * e = Enemy/Gegner
 * t = Trap/Falle
 * p = Player/Spieler
 * 
*/



import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Houston implements ActionListener, Runnable {
	
	
	// hier den code noch besser organisieren und evtl in eigene methode auslagern
	
	final int height;
	final int width;

	private boolean gameIsRunning = false;
	private boolean gameOver = true;
	long delta = 0;
	private long last = 0;
	long fps = 0;
	private int preferredFps;

	private JFrame frame;
	private String currentCard;
	private JPanel cards;
	private JPanel card1, card2, card3, card4, card5;
	private CardLayout cl;
	
	GamePanel gamePanel;
	Player player;
	Map map;
	
	private JButton 
	c1b1, //Neues Spiel
	c1b2, //Einstellungen
	c1b3, //Mitwirkende
	c1b4, //Beenden
	c2b1, //zum Hauptmenue
	c4b1, //zurueck ins Spiel
	c4b2, //zum Hauptmenue
	c5b1; //zum Hauptmenue
	

	// diese Strings identifizieren jeweils eins der Card Panel
	final static String STARTMENU = "STARTMENU";
	final static String SETTINGS = "SETTINGS";
	final static String GAME = "GAME";
	final static String INGAMEMENU = "INGAMEMENU";
	final static String CREDITS = "CREDITS";
	
	

	// ------------------------------------------------------------
	public static void main(String[] args) {
		new Houston(768, 672);
	}

	// ------------------------------------------------------------

	public Houston(int width, int height) {

		// initialisiere allen moeglichen Kram hier drin
		initializeCrap();

		// Hoehe und Breite des Fensterinhaltes
		this.width = width;
		this.height = height;
		
		// im folgenden Bereich wird alles rund ums Fenster aufgebaut und verknuepft
		
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

		// Erstelle ein neues Cards-Panel und fuege alle Card-Panel hinzu
		cl = new CardLayout();
		cards = new JPanel(cl);
		cards.setPreferredSize(new Dimension(this.width, this.height));
		cards.add(card1, STARTMENU);
		cards.add(card2, SETTINGS);
		cards.add(card3, GAME);
		cards.add(card4, INGAMEMENU);
		cards.add(card5, CREDITS);

		// Erstelle das Hauptfenster und fuege die Cards hinzu
		frame = buildFrame("Folderol", cards);
		
		// Card die als erstes angezeigt werden soll
		// STARTMENU; SETTINGS; GAME; INGAMEMENU
		cl.show(cards, STARTMENU);
		currentCard = STARTMENU;
		
		// springe in den Game-Loop
		Thread th = new Thread(this);
		th.start();
		
	}
	
	
	
	private void initializeCrap() {
		// hier wird alles moegliche initialisiert
		
		last = System.nanoTime();
		preferredFps = 30;
		player = new Player();
		map = new Map(0, 20, 24);
		
	}
	
	
	

	@Override
	public void run() {
		// GameLoop - noch in Arbeit
		while(frame.isVisible()) {
		
			if (gameIsRunning) {
				computeDelta();
				player.move(delta);
				card3.repaint();
			}
			
			
			
			try {
				Thread.sleep(1000 / preferredFps);
			} catch (InterruptedException e) {}
		}
	}
	
	
	

	/**
	 * berechnet fps und delta
	 */
	private void computeDelta() {
		delta = System.nanoTime() - last;
		last = System.nanoTime();
		fps = ((long) 1e9) / delta;
	}
	
	private JFrame buildFrame(String titel, JPanel cards) {
		frame = new JFrame(titel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setLocationRelativeTo(null);
		frame.setLocation(0, 0);
		frame.add(cards);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		return frame;
	}

	private JPanel card1() {
		card1 = new JPanel(null);
		
		// card1.add(new JLabel(STARTMENU));
		
		c1b1 = new JButton("Neues Spiel");
		c1b1.addActionListener(this);
		c1b1.setBounds(284, 300, 200, 40);
		card1.add(c1b1);

		c1b2 = new JButton("Einstellungen");
		c1b2.addActionListener(this);
		c1b2.setBounds(284, 360, 200, 40);
		card1.add(c1b2);

		c1b3 = new JButton("Mitwirkende");
		c1b3.addActionListener(this);
		c1b3.setBounds(284, 420, 200, 40);
		card1.add(c1b3);

		c1b4 = new JButton("Beenden");
		c1b4.addActionListener(this);
		c1b4.setBounds(284, 480, 200, 40);
		card1.add(c1b4);
		return card1;
	}
	
	private JPanel card2() {
		card2 = new JPanel(null);
		// card2.add(new JLabel(SETTINGS));

		c2b1 = new JButton("-> Hauptmenue");
		c2b1.setBounds(284, 360, 200, 40);
		c2b1.addActionListener(this);
		card2.add(c2b1);
		return card2;
	}
	
	private JPanel card3() {
		// gamePanel fungiert als die Leinwand fuer das eigentliche Spiel 
		gamePanel = new GamePanel(this);
		
		return gamePanel;
	}

	private JPanel card4() {
		card4 = new JPanel(null);
		// card4.add(new JLabel(INGAMEMENU));

		c4b1 = new JButton("zurueck ins Spiel");
		c4b1.addActionListener(this);
		c4b1.setBounds(284, 300, 200, 40);
		card4.add(c4b1);

		c4b2 = new JButton("-> Hauptmenue");
		c4b2.addActionListener(this);
		c4b2.setBounds(284, 360, 200, 40);
		card4.add(c4b2);
		return card4;
	}

	private JPanel card5() {
		card5 = new JPanel(null);
//		card5.add(new JLabel(CREDITS));

		c5b1 = new JButton("-> Hauptmenue");
		c5b1.addActionListener(this);
		c5b1.setBounds(284, 420, 200, 40);
		card5.add(c5b1);
		return card5;
	}
	
	/**
	 * Wechselt die Ansicht (Card) im Fenster und weist
	 * die gerade benoetigten Tastendruecke zu
	 * 
	 * @param gameOver
	 * @param gameIsRunning
	 * @param name
	 */
	void changeAppearance(boolean gameOver, boolean gameIsRunning, String name) {
		currentCard = name;
		this.gameIsRunning = gameIsRunning;
		this.gameOver = gameOver;
		mapActions();
		cl.show(cards, name);
	}
	
	void changeAppearance(boolean gameIsRunning, String name) {
		changeAppearance(gameOver, gameIsRunning, name);
	}
	
	void changeAppearance(String name) {
		changeAppearance(gameOver, gameIsRunning, name);
	}
	
	
	/**
	 * Weist, je nach angezeigtem Panel (Card), die entsprechenden Tastendruecke zu
	 */
	private void mapActions() {
		InputMap im = cards.getInputMap();
		ActionMap am = cards.getActionMap();
		
		
		// Definiere die benoetigten Tastendruecke
		KeyStroke esc = KeyStroke.getKeyStroke("ESCAPE");
		KeyStroke r = KeyStroke.getKeyStroke("R");
		KeyStroke w = KeyStroke.getKeyStroke("W");
		KeyStroke s = KeyStroke.getKeyStroke("S");
		KeyStroke a = KeyStroke.getKeyStroke("A");
		KeyStroke d = KeyStroke.getKeyStroke("D");
		KeyStroke rw = KeyStroke.getKeyStroke("released W");
		KeyStroke rs = KeyStroke.getKeyStroke("released S");
		KeyStroke ra = KeyStroke.getKeyStroke("released A");
		KeyStroke rd = KeyStroke.getKeyStroke("released D");
		
		// Entferne alle Tastenzuweisungen
		im.remove(KeyStroke.getKeyStroke("ESCAPE"));
		im.remove(r);
		im.remove(w);
		im.remove(s);
		im.remove(a);
		im.remove(d);
		im.remove(rw);
		im.remove(rs);
		im.remove(ra);
		im.remove(rd);
		
		
		// aktiviert das Wechseln zwischen GAME und INGAMEMENU mit "Escape"
		if (gameOver == false) {
			if (currentCard == GAME) {
				im.put(esc, "jumpToIngamemenu");
			} else {
				im.put(esc, "jumpToGame");
			}
		}

		// aktiviert die Bewegungstasten
		if (gameIsRunning) {
			im.put(r, "resetPlayer");
			im.put(w, "moveUp");
			im.put(s, "moveDown");
			im.put(a, "moveLeft");
			im.put(d, "moveRight");
			im.put(rw, "releasedUp");
			im.put(rs, "releasedDown");
			im.put(ra, "releasedLeft");
			im.put(rd, "releasedRight");
		}
		
		// "verbindet" den jeweiligen Tastendruck mit einer Action
		am.put("jumpToIngamemenu", new Actions.jumpToIngamemenu(this));
		am.put("jumpToGame", new Actions.jumpToGame(this));
		am.put("resetPlayer", new Actions.resetPlayer(this));
		am.put("moveUp", new Actions.moveUp(this));
		am.put("moveDown", new Actions.moveDown(this));
		am.put("moveLeft", new Actions.moveLeft(this));
		am.put("moveRight", new Actions.moveRight(this));
		am.put("releasedUp", new Actions.releasedUp(this));
		am.put("releasedDown", new Actions.releasedDown(this));
		am.put("releasedLeft", new Actions.releasedLeft(this));
		am.put("releasedRight", new Actions.releasedRight(this));

	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		JButton buttonClicked = (JButton) e.getSource();

		
		// kuemmert sich um die Events der Menu Buttons
		if (buttonClicked == c1b1) {
			changeAppearance(false, true, GAME);
			player.resetPosition();
			map.renewMap(0);
		} else if (buttonClicked == c1b2) {
			changeAppearance(SETTINGS);
		} else if (buttonClicked == c1b3) {
			changeAppearance(CREDITS);
		} else if (buttonClicked == c1b4) {
			System.exit(0);
		} else if (buttonClicked == c2b1) {
			changeAppearance(STARTMENU);
		} else if (buttonClicked == c4b1) {
			changeAppearance(true, GAME);
		} else if (buttonClicked == c4b2) {
			changeAppearance(true, false, STARTMENU);
		} else if (buttonClicked == c5b1) {
			changeAppearance(STARTMENU);
		}

	}


}
