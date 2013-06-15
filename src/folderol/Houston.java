package folderol;

/**
 * 
 * 8 = Spawn/Start
 * 9 = Finish/Ziel
 * 1 = Ground/Boden
 * 0 = Wall/Wand
 * 5 = Shop
 * 6 = Enemy/GegnerRight
 * 4 = Enemy/GegnerOben
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
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
	public JPanel card1, card2, card4, card5, card6, card7;
	public GamePanel gamePanel;
	// CardLayout ermoeglicht erst diese Darstellung der unterschiedlichen
	// Fensterinhalte auf unterschiedlichen "Karten"
	private CardLayout cl;
	
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
	
	Story story;
	
	EnemyLogic enemyLogic;

	
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
	JRadioButton maenlich, weiblich;
	
	// Knoepfe zur Kartenauswahl fuer Testzwecke
	JButton c6map1, c6map2, c6map3, c6map4, c6map5, c6map6, c6map7, c6map8, c6map9;

	// Label fuer die Ausgabe im Shop
	JLabel c7l4, c7l5, c7l6, c7l7, c7l8, c7l9, c7l10, c7l11;

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
		card1 = new MenuCards.card1(this);	// STARTMENU
		card2 = new MenuCards.card2(this);	// SETTINGS
		gamePanel = new GamePanel(this);	// GAME
		card4 = new MenuCards.card4(this);	// INGAMEMENU
		card5 = new MenuCards.card5(this);	// CREDITS
		card6 = new MenuCards.card6(this);	// INTRODUCTION
		card7 = new MenuCards.card7(this);	// SHOP

		// Erstellt ein neues Cards-Panel ...
		cl = new CardLayout();
		cards = new JPanel(cl);
		cards.setPreferredSize(new Dimension(this.width, this.height));
		// ... und fuegt alle Card-Panel hinzu
		cards.add(card1, STARTMENU);
		cards.add(card2, SETTINGS);
		cards.add(gamePanel, GAME);
		cards.add(card4, INGAMEMENU);
		cards.add(card5, CREDITS);
		cards.add(card6, INTRODUCTION);
		cards.add(card7, SHOP);

		// Erstellt das Hauptfenster und fuegt Cards hinzu
		frame = buildFrame("Folderol", cards);
		frame.pack();
		
		// Setzt Card, die als erstes angezeigt werden soll
		cl.show(cards, STARTMENU);
		currentCard = STARTMENU;
		
		// Startet den Game-Loop
		Thread th = new Thread(this);
		th.start();
	}
	
	
	// Hier wird vor Spielbeginn alles moegliche initialisiert
	private void initializeCrap() {
		preferredFps = 35;
		map = new Map(0, 0, 20, 24);
		player = new Player();
		enemyLogic = new EnemyLogic(this);
		logic = new Logic(this);
		// story = new Story(0, 0, 0);
		inventory = new Inventory(this);
		shop = new Shop(this);
		
	}
	

	@Override
	public void run() {
		// GameLoop
		
		// Okay, let's do the loop, yeah come on baby let's do the loop
		// and it goes like this ...
		
		while(frame.isVisible()) {
			synchronized (this) {
				if (gameIsRunning) {
					computeDelta();
					// Berechnet z.B. Bewegungen im Spiel
					logic.doGameUpdates(delta);
					// Zeichnet die "Leinwand" in card4 neu
					gamePanel.repaint();
			
				}
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
		
		player.stop();
		
		last = System.nanoTime();
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
		KeyStroke e = KeyStroke.getKeyStroke("E");
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
		im.remove(e);
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
			im.put(h, "useHealthPack");
			im.put(e, "interact");
			im.put(m, "useManaPotion");
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
		am.put("useHealthPack", new Actions.useHealthPack(this));
		am.put("useManaPotion", new Actions.useManaPotion(this));
		am.put("interact", new Actions.interact(this));
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
		synchronized (this) {
			
			
			
			// Ermittelt die Quelle des Tastendrucks
			Object buttonClicked = e.getSource();
			
			// Kuemmert sich um die Events der einzelnen Menu Buttons
			if (buttonClicked == c1b1) {
				changeAppearance(INTRODUCTION);
			} else if (buttonClicked == c6b1) {
				logic.setupNewGame(0, 0);
				changeAppearance(false, true, GAME);
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
			} else if (buttonClicked == c6map1) {
				logic.setupNewGame(0, 0);
				changeAppearance(false, true, GAME);
			} else if (buttonClicked == c6map2) {
				changeAppearance(false, true, GAME);
				logic.setupNewGame(0, 1);
			} else if (buttonClicked == c6map3) {
				changeAppearance(false, true, GAME);
				logic.setupNewGame(0, 2);
			} else if (buttonClicked == c6map4) {
				changeAppearance(false, true, GAME);
				logic.setupNewGame(1, 0);
			} else if (buttonClicked == c6map5) {
				changeAppearance(false, true, GAME);
				logic.setupNewGame(1, 1);
			} else if (buttonClicked == c6map6) {
				changeAppearance(false, true, GAME);
				logic.setupNewGame(1, 2);
			} else if (buttonClicked == c6map7) {
				changeAppearance(false, true, GAME);
				logic.setupNewGame(2, 0);
			} else if (buttonClicked == c6map8) {
				changeAppearance(false, true, GAME);
				logic.setupNewGame(2, 1);
			} else if (buttonClicked == c6map9) {
				changeAppearance(false, true, GAME);
				logic.setupNewGame(2, 2);
			
			// Kuemmert sich um die Kaufbutton im Shop
			} else if (buttonClicked == c7b1) {
				shop.buyHealthPack();
			} else if (buttonClicked == c7b2) {
				shop.buyManaPotion();
			} else if (buttonClicked == c7b3) {
				changeAppearance(true, GAME);
			}
		}	
	}

}
