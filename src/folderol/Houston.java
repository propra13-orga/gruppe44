package folderol;

/**
 * 
 * 8 = Spawn/Start
 * 9 = Finish/Ziel
 * 1 = Ground/Boden
 * 0 = Wall/Wand
 * e = Enemy/Gegner
 * 7 = Trap/Falle
 * p = Player/Spieler
 * 
 * Ä, ä \u00c4, \u00e4
 * Ö, ö \u00d6, \u00f6
 * Ü, ü \u00dc, \u00fc
 * ß \u00df
 * ® \u00ae 
*/




import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

public class Houston implements ActionListener, Runnable {

	// Speichert die Hoehe und Breite des Fensterinhalts
	final int height;
	final int width;

	// gameIsRunning und gameOver sind fuer die Menuefuehrung
	// und den Spielverlauf von Bedeutung
	private boolean gameIsRunning = false;
	private boolean gameOver = true;
	// Delta ermoeglicht die Berechnung von konstanten
	// Bewegungen im Spiel, unabhaengig von den FPS
	long delta = 0;
	// Letzte Systemzeit, hilft bei Berechnungen zum Gameloop
	private long last = 0;
	long fps = 0;
	// Speichert die gewuenschte Bildwiederholungsrate
	private int preferredFps;

	// Das Fenster, der Rahmen
	private JFrame frame;
	// Gibt an, welche Card im Fenster gerade aktiv ist
	private String currentCard;
	// Kontainer fuer die "Unterfenster" card1, card2, ...
	private JPanel cards;
	// "Unterfenster", die das Startmenue, Einstellungen, etc. beinhalten 
	private JPanel card1, card2, card3, card4, card5, card6, card7;
	// CardLayout ermoeglicht erst diese Darstellung der unterschiedlichen
	// Fensterinhalte auf unterschiedlichen "Karten"
	private CardLayout cl;
	
	// Spielfenster, Spielleinwand, hier wird drauf gezeichnet
	GamePanel gamePanel;
	// Durch den Spieler gesteuerter Charakter
	Player player;
	// Die Karte
	Map map;
	// In der Logik werden Berechnungen zur Laufzeit getaetigt
	Logic logic;
	// Das Inventar hält Items, die z.B. eingesammelt werden
	Inventory inventory;
	// Im Shop können Items gekauft werden
	Shop shop;
	
	//Item fuer Leben
	// Healthpack healthpack;
	//Item fuer Mana
	// Manatrank manatrank;
	
	// Die im Menue vorhandenen Knoepfe
	JButton 
	c1b1, //Neues Spiel
	c1b2, //Einstellungen
	c1b3, //Mitwirkende
	c1b4, //Beenden
	c2b1, //zum Hauptmenue
	c4b1, //zurueck ins Spiel
	c4b2, //zum Hauptmenue
	c5b1, //zum Hauptmenue
	c6b1, //weiter
	c7b1, //Healthpack
	c7b2, //Manatrank
	c7b3; //zurueck ins Spiel

	//Spielerauswahl
	private JRadioButton maenlich, weiblich;
	
	// Knoepfe zur Kartenauswahl fuer Testzwecke
	private JButton 
	c6level1,
	c6level2,
	c6level3,
	c6level4,
	c6level5,
	c6level6, 
	c6level7,
	c6level8,
	c6level9;
	
	private JLabel 
	c7l4, //fuer die Ausgabe im Shop
	c7l5, //fuer Icon Club Mate im Shop
	c7b6; //fuer Icon Killepitsch im Shop

	// Diese Strings identifizieren jeweils eins der Card Panel
	// Sind hilfreich z.B. beim Wechsel von einer Karte auf eine Andere
	final static String STARTMENU = "STARTMENU";
	final static String SETTINGS = "SETTINGS";
	final static String GAME = "GAME";
	final static String INGAMEMENU = "INGAMEMENU";
	final static String CREDITS = "CREDITS";
	final static String INTRODUCTION = "INTRODUCTION";
	final static String SHOP = "SHOP";
		
	
	// ------------------------------------------------------------
	public static void main(String[] args) {
		new Houston(768, 672);
	}
	// ------------------------------------------------------------

	public Houston(int width, int height) {

		// Initialisiert allen moeglichen Kram, vorm ersten Spielstart
		initializeCrap();

		// Setzt Hoehe und Breite des Fensterinhaltes
		this.width = width;
		this.height = height;
		
		// Erstellt die einzelnen Card-Panel
		card1 = card1(); // STARTMENU
		card2 = card2(); // SETTINGS
		card3 = card3(); // GAME
		card4 = card4(); // INGAMEMENU
		card5 = card5(); // CREDITS
		card6 = card6(); // INTRODUCTION		
		card7 = card7(); // SHOP

		// Erstellt ein neues Cards-Panel ...
		cl = new CardLayout();
		cards = new JPanel(cl);
		cards.setPreferredSize(new Dimension(this.width, this.height));
		// ... und fuegt alle Card-Panel hinzu
		cards.add(card1, STARTMENU);
		cards.add(card2, SETTINGS);
		cards.add(card3, GAME);
		cards.add(card4, INGAMEMENU);
		cards.add(card5, CREDITS);
		cards.add(card6, INTRODUCTION);
		cards.add(card7, SHOP);

		// Erstellt das Hauptfenster und fuegt Cards hinzu
		frame = buildFrame("Folderol", cards);
		frame.pack();
		
		// Setzt Card, die als erstes angezeigt werden soll
		// STARTMENU; SETTINGS; GAME; INGAMEMENU
		cl.show(cards, STARTMENU);
		currentCard = STARTMENU;
		
		// Startet den Game-Loop
		Thread th = new Thread(this);
		th.start();
	}
	
	
	// Hier wird vor Spielbeginn alles moegliche initialisiert
	private void initializeCrap() {
		last = System.nanoTime();
		preferredFps = 35;
		map = new Map(0, 0, 20, 24);
		player = new Player();
		logic = new Logic(this);
		// healthpack = new Healthpack(this);
		// manatrank = new Manatrank(this);
		inventory = new Inventory(this);
		shop = new Shop(this);
	}
	

	@Override
	public void run() {
		// GameLoop
		
		// Okay, let's do the loop, yeah come on baby let's do the loop
		// and it goes like this ...
		
		while(frame.isVisible()) {
		
			if (gameIsRunning) {
				computeDelta();
				// Berechnet z.B. Bewegungen im Spiel
				logic.doGameUpdates(delta);
				// Zeichnet die "Leinwand" in card4 neu
				card3.repaint();
			}
			
			try {
				Thread.sleep(1000 / preferredFps);
			} catch (InterruptedException e) {}
		}
	}
	
	
	// Berechnet FPS und Delta
	private void computeDelta() {
		delta = System.nanoTime() - last;
		last = System.nanoTime();
		fps = ((long) 1e9) / delta;
	}
	
	// Baut das Fenster auf und bindet JPanel cards ein
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

	// Baut das Hauptmenue
	private JPanel card1() {
		card1 = new JPanel(null);
		
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
	
	// Baut das Einstellungsfenster
	private JPanel card2() {
		card2 = new JPanel(null);

		c2b1 = new JButton("-> Hauptmen\u00fc");
		c2b1.setBounds(284, 360, 200, 40);
		c2b1.addActionListener(this);
		card2.add(c2b1);

		maenlich = new JRadioButton("Spieler");
		maenlich.setBounds(284, 250, 210, 40);
		maenlich.addActionListener(this);
		card2.add(maenlich);
		maenlich.setSelected(true);

		weiblich = new JRadioButton("Spielerin");
		weiblich.setBounds(284, 300, 210, 40);
		weiblich.addActionListener(this);
		card2.add(weiblich);

		ButtonGroup g = new ButtonGroup();
		g.add(maenlich);
		g.add(weiblich);

		return card2;
	}
	
	// Baut das Spielfenster
	private JPanel card3() {
		// gamePanel fungiert als die Leinwand fuer das eigentliche Spiel 
		gamePanel = new GamePanel(this);
		return gamePanel;
	}

	// Baut das Spielmenue
	private JPanel card4() {
		card4 = new JPanel(null);
		// card4.add(new JLabel(INGAMEMENU));

		c4b1 = new JButton("zur\u00fcck ins Spiel");
		c4b1.addActionListener(this);
		c4b1.setBounds(284, 300, 200, 40);
		card4.add(c4b1);

		c4b2 = new JButton("-> Hauptmen\u00fc");
		c4b2.addActionListener(this);
		c4b2.setBounds(284, 360, 200, 40);
		card4.add(c4b2);
		return card4;
	}

	// Baut das Mitwirkendenfenster
	private JPanel card5() {
		card5 = new JPanel(null);
		JLabel picsandy = new JLabel(new ImageIcon("./res/img/sandy.png"));
		picsandy.setBounds(29, 200, 130, 130);
		card5.add(picsandy);

		JLabel picjana = new JLabel(new ImageIcon("./res/img/jana.png"));
		picjana.setBounds(174, 200, 130, 130);
		card5.add(picjana);

		JLabel picphil = new JLabel(new ImageIcon("./res/img/phil.png"));
		picphil.setBounds(319, 200, 130, 130);
		card5.add(picphil);

		JLabel picphilipp = new JLabel(new ImageIcon("./res/img/philipp.png"));
		picphilipp.setBounds(464, 200, 130, 130);
		card5.add(picphilipp);

		JLabel picdavid = new JLabel(new ImageIcon("./res/img/david.png"));
		picdavid.setBounds(609, 200, 130, 130);
		card5.add(picdavid);

		c5b1 = new JButton("-> Hauptmen\u00fc");
		c5b1.addActionListener(this);
		c5b1.setBounds(284, 420, 200, 40);
		card5.add(c5b1);
		return card5;
	}
	
	//Baut das Introductionfenster
	private JPanel card6(){
		card6 = new JPanel(null);
		c6b1 = new JButton ("-> Weiter");
		c6b1.addActionListener(this);
		c6b1.setBounds(284, 300, 200, 40);
		card6.add(c6b1);
		
		// Button fuer Testzwecke		
		c6level1 = new JButton("Map 1");
		c6level1.addActionListener(this);
		c6level1.setBounds(100, 400, 80, 20);
		card6.add(c6level1);
		c6level2 = new JButton("Map 2");
		c6level2.addActionListener(this);
		c6level2.setBounds(100, 430, 80, 20);
		card6.add(c6level2);
		c6level3 = new JButton("Map 3");
		c6level3.addActionListener(this);
		c6level3.setBounds(100, 460, 80, 20);
		card6.add(c6level3);
		c6level4 = new JButton("Map 4");
		c6level4.addActionListener(this);
		c6level4.setBounds(200, 400, 80, 20);
		card6.add(c6level4);
		c6level5 = new JButton("Map 5");
		c6level5.addActionListener(this);
		c6level5.setBounds(200, 430, 80, 20);
		card6.add(c6level5);
		c6level6 = new JButton("Map 6");
		c6level6.addActionListener(this);
		c6level6.setBounds(200, 460, 80, 20);
		card6.add(c6level6);
		c6level7 = new JButton("Map 7");
		c6level7.addActionListener(this);
		c6level7.setBounds(300, 400, 80, 20);
		card6.add(c6level7);
		c6level8 = new JButton("Map 8");
		c6level8.addActionListener(this);
		c6level8.setBounds(300, 430, 80, 20);
		card6.add(c6level8);
		c6level9 = new JButton("Map 9");
		c6level9.addActionListener(this);
		c6level9.setBounds(300, 460, 80, 20);
		card6.add(c6level9);
		
		JLabel label = new JLabel("Eine Story f\u00fcr das Spiel wird hier sp\u00e4ter noch eingef\u00fcgt. ");
		label.setBounds(184, 200, 400, 100);
		card6.add(label);
		return card6;
	}
	
	//Baut denShop
	private JPanel card7(){
		card7 = new JPanel();
		//card7.repaint();
		c7b1 = new JButton("Healthpack - 40 CP");
		c7b1.addActionListener(this);
		c7b1.setBounds(284, 300, 200, 40);
		ImageIcon healthpack = new ImageIcon ("./res/img/health.png");
		c7b1.setIcon(healthpack);

		c7b2 = new JButton("Manatrank - 100 CP");
		c7b2.addActionListener(this);
		c7b2.setBounds(284, 500, 200, 40);
		ImageIcon manatrank = new ImageIcon ("./res/img/mana.png");
		c7b2.setIcon(manatrank);
			
		c7b3 = new JButton("zur\u00fcck ins Spiel");
		c7b3.addActionListener(this);
		c7b3.setBounds(520, 600, 200, 40);
		
		c7l4 = new JLabel ("");
		c7l4.setBounds (287, 590 ,200,50);
		c7l4.setVisible(true);
		
		c7l5 = new JLabel(new ImageIcon("./res/img/Club_mate.png"));
		c7l5.setBounds(70, 190, 200, 149);
		c7l4.setVisible(true);
		c7b6 = new JLabel(new ImageIcon("./res/img/Killepitsch.png"));
		c7b6.setBounds(70, 391, 200, 149);
		c7l4.setVisible(true);
		
		JLabel Background = new JLabel(new ImageIcon("./res/img/regal.png"));
		Background.setLayout(null);
		Background.setBounds(0, 0, 768, 672);
		Background.add(c7b1);
		Background.add(c7b2);
		Background.add(c7b3);
		Background.add(c7l4);
		Background.add(c7l5);
		Background.add(c7b6);
		  
		card7.add(Background);
		return card7;
	}

	// Wechselt die Card und weist die entsprechenden Tastendruecke zu
	public void changeAppearance(boolean gameOver, boolean gameIsRunning, String name) {
		// Setzt die aktuelle Card auf die neue Card
		currentCard = name;
		// Aendert moeglicherweise, ob das Spiel noch laeuft
		this.gameIsRunning = gameIsRunning;
		// Aendert moeglicherweise, ob das Spiel beendet wurde
		this.gameOver = gameOver;
		// Weist die Tastendruecke zu
		mapActions();
		// Waechselt von der aktuellen Card auf die neue Card
		cl.show(cards, name);
	}
	
	public void changeAppearance(boolean gameIsRunning, String name) {
		changeAppearance(gameOver, gameIsRunning, name);
	}
	
	public void changeAppearance(String name) {
		changeAppearance(gameOver, gameIsRunning, name);
	}
	
	
	// Weist, je nach angezeigtem Panel (Card), die entsprechenden Tastendruecke zu
	private void mapActions() {
		// Holt die InputMap, um neue Tastendruecke zu registrieren
		InputMap im = cards.getInputMap();
		// Holt die ActionMap, um neue Aktionen zu registrieren
		ActionMap am = cards.getActionMap();

		// Definiert die benoetigten Tastendruecke
		KeyStroke esc = KeyStroke.getKeyStroke("ESCAPE");
		KeyStroke h = KeyStroke.getKeyStroke("H");
		KeyStroke j = KeyStroke.getKeyStroke("J");
		KeyStroke m = KeyStroke.getKeyStroke("M");
		KeyStroke r = KeyStroke.getKeyStroke("R");
		KeyStroke w = KeyStroke.getKeyStroke("W");
		KeyStroke s = KeyStroke.getKeyStroke("S");
		KeyStroke a = KeyStroke.getKeyStroke("A");
		KeyStroke d = KeyStroke.getKeyStroke("D");
		KeyStroke rw = KeyStroke.getKeyStroke("released W");
		KeyStroke rs = KeyStroke.getKeyStroke("released S");
		KeyStroke ra = KeyStroke.getKeyStroke("released A");
		KeyStroke rd = KeyStroke.getKeyStroke("released D");

		// Entfernt alle Tastenzuweisungen in der InputMap
		im.remove(KeyStroke.getKeyStroke("ESCAPE"));
		im.remove(h);
		im.remove(j);
		im.remove(m);
		im.remove(r);
		im.remove(w);
		im.remove(s);
		im.remove(a);
		im.remove(d);
		im.remove(rw);
		im.remove(rs);
		im.remove(ra);
		im.remove(rd);

		// Aktiviert das Wechseln zwischen GAME und INGAMEMENU mit "Escape"
		if (gameOver == false) {
			if (currentCard == GAME) {
				im.put(esc, "jumpToIngamemenu");
			} else {
				im.put(esc, "jumpToGame");
			}
		}

		// Aktiviert/registriert die Bewegungstasten und die Resettaste
		if (gameIsRunning) {
			im.put(h, "useHealthpack");
			im.put(j, "enterShop");
			im.put(m, "useManatrank");
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

		// "Verbindet" die Tastendruecke mit einer jeweiligen Action
		am.put("jumpToIngamemenu", new Actions.jumpToIngamemenu(this));
		am.put("jumpToGame", new Actions.jumpToGame(this));
		am.put("useHealthpack", new Actions.useHealthpack(this));
		am.put("useManatrank", new Actions.useManatrank(this));
		am.put("enterShop", new Actions.enterShop(this));
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
		
		// Ermittelt die Quelle des Tastendrucks
		Object buttonClicked = e.getSource();
		
		// Kuemmert sich um die Events der einzelnen Menu Buttons
		if (buttonClicked == c1b1) {
			changeAppearance(INTRODUCTION);
			player.resetHealthManaMoney(100, 100, 200);
		} else if (buttonClicked == c6b1) {
			changeAppearance(false, true, GAME);
			logic.setupNewGame(0, 0);
			inventory.clear();
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
		
		// Kuemmert sich um die Auswahl des Spielers
		} else if (buttonClicked == maenlich) {
			player.changeTexture(0);
		} else if (buttonClicked == weiblich) {
			 player.changeTexture(1);

		// Auswahl der einzelnen Level zu Testzwecken
		} else if (buttonClicked == c6level1) {
			changeAppearance(false, true, GAME);
			logic.setupNewGame(0, 0);
		} else if (buttonClicked == c6level2) {
			changeAppearance(false, true, GAME);
			logic.setupNewGame(0, 1);
		} else if (buttonClicked == c6level3) {
			changeAppearance(false, true, GAME);
			logic.setupNewGame(0, 2);
		} else if (buttonClicked == c6level4) {
			changeAppearance(false, true, GAME);
			logic.setupNewGame(1, 0);
		} else if (buttonClicked == c6level5) {
			changeAppearance(false, true, GAME);
			logic.setupNewGame(1, 1);
		} else if (buttonClicked == c6level6) {
			changeAppearance(false, true, GAME);
			logic.setupNewGame(1, 2);
		} else if (buttonClicked == c6level7) {
			changeAppearance(false, true, GAME);
			logic.setupNewGame(2, 0);
		} else if (buttonClicked == c6level8) {
			changeAppearance(false, true, GAME);
			logic.setupNewGame(2, 1);
		} else if (buttonClicked == c6level9) {
			changeAppearance(false, true, GAME);
			logic.setupNewGame(2, 2);
		
		// Kuemmert sich um die Kaufbutton im Shop
		} else if (buttonClicked == c7b1) {
			shop.buyHealthPack();
		} else if (buttonClicked == c7b2) {
			shop.buyManaPotion();
		} else if (buttonClicked == c7b3) {
			// c7l4.setText("");
			changeAppearance(true, GAME);
		}
		
	}
}
