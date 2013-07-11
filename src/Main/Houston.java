package Main;

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

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import Logic.EnemyLogic;
import Logic.GameLogic;
import Logic.ItemLogic;
import Logic.MagicLogic;
import Logic.PlayerLogic;
import MapEditor.MapEditor;
import Network.MultiPlayer;

/** Hauptklasse */
public class Houston implements ActionListener, Runnable {


	/** Hoehe des Fensters */
	public int height;

	/** Breite des Fensters */
	public int width;

	// gameIsRunning und gameOver sind fuer die Menuefuehrung
	// und den Spielverlauf von Bedeutung
	public boolean gameIsRunning = false;
	public boolean gameOver = true;
	// Delta ermoeglicht die Berechnung von konstanten
	// Bewegungen im Spiel, unabhaengig von den FPS
	long delta = 0;
	// Letzte Systemzeit, hilft bei Berechnungen zum Gameloop
	private long last = 0;
	long fps = 0;
	// Speichert die gewuenschte Bildwiederholungsrate
	public int preferredFps;

	/** Das Fenster, der Rahmen */
	public JFrame frame;

	/** Gibt an, welche Card im Fenster gerade aktiv ist */
	public String currentCard;
	// Kontainer fuer die "Unterfenster" card1, card2, ...
	private JPanel cards;

	/** "Unterfenster", die das Startmenue, Einstellungen, etc. beinhalten */
	public JPanel card1, card2, card4, card5, card6, card7;

	/** Spielfenster */
	public GamePanel gamePanel;

	/** Karten- und Leveleditor */
	public MapEditor mapEditor;
	public MultiPlayer multiPlayer;
	// CardLayout ermoeglicht erst diese Darstellung der unterschiedlichen
	// Fensterinhalte auf unterschiedlichen "Karten"
	private CardLayout cl;

	/** Durch den Spieler gesteuerter Charakter */
	public Player player;

	/** Die Karte */
	public Map map;

	/** Das Inventar haelt Items, die z.B. eingesammelt werden */
	public Inventory inventory;

	/** Im Shop koennen Items gekauft werden */
	public Shop shop;

	/** Quests und Raetsel */
	public Quest quest;

	/** Die Geschichte des Spiels */
	public Story story;

	/** In der Logik werden Berechnungen zur Laufzeit getaetigt */
	public GameLogic gameLogic;

	/** Das unterstuetzende Gehirn des Player */
	public PlayerLogic playerLogic;

	/** Das Gehirn der Gegner */
	public EnemyLogic enemyLogic;

	/** Die Logik der Magie */
	public MagicLogic magicLogic;

	/** Die Logik der Items */
	public ItemLogic itemLogic;


	// Die im Menue vorhandenen Knoepfe
	JButton
	c1b1, // Neues Spiel
	c1b2, // Einstellungen
	c1b3, // Mitwirkende
	c1b4, // MapEditor
	c1b5, // Beenden
	c1b6, // Multiplayer
	c2b1, // zum Hauptmenue
	c4b1, // zurueck ins Spiel
	c4b2, // zum Hauptmenue
	c5b1, // zum Hauptmenue
	c6b1, // weiter
	c7b1, // Healthpack
	c7b2, // Manatrank
	c7b3; // zurueck ins Spiel

	//Spielerauswahl
	JRadioButton maenlich, weiblich;

	// Knoepfe zur Kartenauswahl fuer Testzwecke
	JButton c6map1, c6map2, c6map3, c6map4, c6map5, c6map6, c6map7, c6map8, c6map9;

	// Label fuer die Ausgabe im Shop
	JLabel c7l4, c7l5, c7l6, c7l7, c7l8, c7l9, c7l10, c7l11;

	// Diese Strings identifizieren jeweils eins der Card Panel
	// Sind hilfreich z.B. beim Wechsel von einer Karte auf eine Andere
	public final static String STARTMENU = "STARTMENU";
	public final static String SETTINGS = "SETTINGS";
	public final static String GAME = "GAME";
	public final static String INGAMEMENU = "INGAMEMENU";
	public final static String CREDITS = "CREDITS";
	public final static String INTRODUCTION = "INTRODUCTION";
	public final static String SHOP = "SHOP";
	public final static String MAPEDITOR = "MAPEDITOR";
	public final static String MULTIPLAYER = "MULTIPLAYER";


	// ------------------------------------------------------------
	public static void main(String[] args) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {e.printStackTrace();}
		new Houston(768, 672);
	}
	// ------------------------------------------------------------

	/**
	 * Spiel wird initialisiert:
	 * ruft initializeCrap auf;
	 * erstellt das Spielfenster;
	 * laedt die Karten
	 * @param width
	 * @param height
	 */
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
		mapEditor = new MapEditor(this);	// MAPEDITOR
		multiPlayer = new MultiPlayer(this);// MULTIPLAYER

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
		cards.add(mapEditor, MAPEDITOR);
		cards.add(multiPlayer, MULTIPLAYER);

		// Erstellt das Hauptfenster und fuegt Cards hinzu
		frame = buildFrame("DungeonCrawler", cards);
		frame.pack();

		// Setzt Card, die als erstes angezeigt werden soll
		currentCard = MULTIPLAYER;
		cl.show(cards, currentCard);

		// Startet den Game-Loop
		Thread th = new Thread(this);
		th.start();
	}


	// Hier wird vor Spielbeginn alles moegliche initialisiert
	private void initializeCrap() {
		preferredFps = 35;
		map = new Map(0, 0, 20, 24);

		player = new Player();
		inventory = new Inventory(this);
		shop = new Shop(this);

		story = new Story(0, this);
		quest = new Quest(0, this);
		gameLogic = new GameLogic(this);
	}

	/** GameLoop
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// GameLoop

		while (frame.isVisible()) {
			//Gebraucht damit das Spiel nach der Anzeige vom LevelupFrame anhaelt
			//und beim schließen dessen weiterlaeuft

			if (gameIsRunning) {
				computeDelta();
				// Berechnet z.B. Bewegungen im Spiel
				gameLogic.doGameUpdates(delta);
				// Zeichnet die "Leinwand" in card4 neu
				gamePanel.repaint();
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
		frame.add(cards);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		return frame;
	}

	/**
	 * Wechselt die Card und weist die entsprechenden Tastendruecke zu
	 * @param gameOver
	 * @param gameIsRunning
	 * @param name
	 */
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

	/**
	 * ruft changeAppearance(boolean gameOver, boolean gameIsRunning, String name) auf
	 * @param gameIsRunning
	 * @param name
	 */
	public void changeAppearance(boolean gameIsRunning, String name) {
		changeAppearance(gameOver, gameIsRunning, name);
	}

	/**
	 * ruft changeAppearance(boolean gameOver, boolean gameIsRunning, String name) auf
	 * @param name
	 */
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
		KeyStroke space = KeyStroke.getKeyStroke("SPACE");

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
		im.remove(space);

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
			im.put(space, "attack");
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
		am.put("attack", new Actions.attack(this));
	}

	/** kontrolliert die Bouttonklicks
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// Ermittelt die Quelle des Tastendrucks
		Object buttonClicked = e.getSource();

		// Kuemmert sich um die Events der einzelnen Menu Buttons
		if (buttonClicked == c1b1) {
			changeAppearance(INTRODUCTION);
		} else if (buttonClicked == c6b1) {
			gameLogic.setupNewGame(0, 0);
			changeAppearance(false, true, GAME);
		} else if (buttonClicked == c1b2) {
			changeAppearance(SETTINGS);
		} else if (buttonClicked == c1b3) {
			changeAppearance(CREDITS);
		} else if (buttonClicked == c1b4) {
			mapEditor.showEditorWindow();
			changeAppearance(MAPEDITOR);
		} else if (buttonClicked == c1b5) {
			System.exit(0);
		} else if (buttonClicked == c1b6) {
			changeAppearance(MULTIPLAYER);
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
			gameLogic.setupNewGame(0, 0);
			changeAppearance(false, true, GAME);
		} else if (buttonClicked == c6map2) {
			changeAppearance(false, true, GAME);
			gameLogic.setupNewGame(0, 1);
		} else if (buttonClicked == c6map3) {
			changeAppearance(false, true, GAME);
			gameLogic.setupNewGame(0, 2);
		} else if (buttonClicked == c6map4) {
			changeAppearance(false, true, GAME);
			gameLogic.setupNewGame(1, 0);
		} else if (buttonClicked == c6map5) {
			changeAppearance(false, true, GAME);
			gameLogic.setupNewGame(1, 1);
		} else if (buttonClicked == c6map6) {
			changeAppearance(false, true, GAME);
			gameLogic.setupNewGame(1, 2);
		} else if (buttonClicked == c6map7) {
			changeAppearance(false, true, GAME);
			gameLogic.setupNewGame(2, 0);
		} else if (buttonClicked == c6map8) {
			changeAppearance(false, true, GAME);
			gameLogic.setupNewGame(2, 1);
		} else if (buttonClicked == c6map9) {
			changeAppearance(false, true, GAME);
			gameLogic.setupNewGame(2, 2);

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
