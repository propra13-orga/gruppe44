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
*/



import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ActionMap;
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
	private JPanel card1, card2, card3, card4, card5, card6;
	// CardLayout ermoeglicht erst diese Darstellung der unterschiedlichen
	// Fensterinhalte auf unterschiedlichen "Karten"
	private CardLayout cl;
	JRadioButton weiblich;
	JRadioButton maenlich;
	
	// Spielfenster, Spielleinwand, hier wird drauf gezeichnet
	GamePanel gamePanel;
	// Durch den Spieler gesteuerter Charakter
	Player player;
	// Die Karte
	Map map;
	// In der Logik werden Berechnungen zur Laufzeit getaetigt
	Logic logic;
	
	// Die im Menue vorhandenen Knoepfe
	private JButton 
	c1b1, //Neues Spiel
	c1b2, //Einstellungen
	c1b3, //Mitwirkende
	c1b4, //Beenden
	c2b1, //zum Hauptmenue
	c4b1, //zurueck ins Spiel
	c4b2, //zum Hauptmenue
	c5b1, //zum Hauptmenue
	c6b1; //weiter
	
	
	String text;

	// Diese Strings identifizieren jeweils eins der Card Panel
	// Sind hilfreich z.B. beim Wechsel von einer Karte auf eine Andere
	final static String STARTMENU = "STARTMENU";
	final static String SETTINGS = "SETTINGS";
	final static String GAME = "GAME";
	final static String INGAMEMENU = "INGAMEMENU";
	final static String CREDITS = "CREDITS";
	final static String INTRODUCTION = "INTRODUCTION";
	
	

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
		
		// Im Folgenden wird alles rund ums Fenster aufgebaut und verknuepft
		
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
		
		// Card 6 - INTRODUCTION
		card6 = card6();

		// Erstellt ein neues Cards-Panel und fuege alle Card-Panel hinzu
		cl = new CardLayout();
		cards = new JPanel(cl);
		cards.setPreferredSize(new Dimension(this.width, this.height));
		cards.add(card1, STARTMENU);
		cards.add(card2, SETTINGS);
		cards.add(card3, GAME);
		cards.add(card4, INGAMEMENU);
		cards.add(card5, CREDITS);
		cards.add(card6, INTRODUCTION);

		// Erstellt das Hauptfenster und fuegt die Cards hinzu
		frame = buildFrame("Folderol", cards);
		
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
		map = new Map(0, 20, 24);
		player = new Player();
		logic = new Logic(this);
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
	
	// Baut das Einstellungsfenster
	private JPanel card2() {
		card2 = new JPanel(null);
		// card2.add(new JLabel(SETTINGS));

		c2b1 = new JButton("-> Hauptmenue");
		c2b1.setBounds(284, 360, 200, 40);
		c2b1.addActionListener(this);
		weiblich = new JRadioButton("Spielerin");
		card2.add(weiblich);
		weiblich.addActionListener(this);
		maenlich = new JRadioButton ("Spieler");
		card2.add(maenlich);
		maenlich.addActionListener(this);
		ButtonGroup g = new ButtonGroup();
		g.add(weiblich);
		g.add(maenlich);
		weiblich.setBounds(284, 300, 210, 40);
		maenlich.setBounds(284, 250, 210, 40);
		maenlich.setSelected(true);
		
		card2.add(c2b1);
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

	// Baut das Mitwirkendenfenster
	private JPanel card5() {
		card5 = new JPanel(null);
		// card5.add(new JLabel(CREDITS));

		c5b1 = new JButton("-> Hauptmenue");
		c5b1.addActionListener(this);
		c5b1.setBounds(284, 420, 200, 40);
		card5.add(c5b1);
		return card5;
	}
	
	//Baut das Introductionfenster
	private JPanel card6(){
		card6 = new JPanel();
		c6b1 = new JButton ("-> Weiter");
		c6b1.addActionListener(this);
		
		c6b1.setBounds(284, 300, 200, 40);
		JLabel label = new JLabel("Eine Story für das Spiel wird hier später noch eingefügt. ");
		card6.add(label);
		label.setVisible(true);
	    card6.add(c6b1);
		return card6;
	}

	
	// Wechselt die Card und weist die entsprechenden Tastendruecke zu
	void changeAppearance(boolean gameOver, boolean gameIsRunning, String name) {
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
	
	void changeAppearance(boolean gameIsRunning, String name) {
		changeAppearance(gameOver, gameIsRunning, name);
	}
	
	void changeAppearance(String name) {
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
//		JButton buttonClicked = (JButton) e.getSource();
		
		// Kuemmert sich um die Events der einzelnen Menu Buttons
		if (buttonClicked == c1b1) {
			changeAppearance(INTRODUCTION);
		}else if(buttonClicked == c6b1){
			changeAppearance(false, true, GAME);
			logic.setupNewMap(0);
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
		} else if (buttonClicked == maenlich) {
			player.changeTexture(0);
			
		} else if (buttonClicked == weiblich) {
			 player.changeTexture(1);
		}
	}
	
}
