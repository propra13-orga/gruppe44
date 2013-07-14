package Network;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import Main.Houston;

/**
 * Klasse fuer Mehrspielermodus
 */
public class MultiPlayer extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	public Houston houston;


	private JPanel
	chatPanel,
	gamePanel,
	serverPanel,
	clientPanel;

	private JTextArea chatHistory;

	/**
	 * alle Textfelder des Chatfensters
	 */
	public JTextField
	gameStatus,
	serverPort,
	serverIp,
	clientIp,
	clientPort,
	chatInput;

	/**
	 * alle Buttons des Chatfensters
	 */
	public JButton
	readyToPlay,
	notReadyToPlay,
	startMenu,
	backToGame,
	createServer,
	killServer,
	connectAsClient,
	disconnectAsClient;


	/**
	 * Server und Client Thread
	 */
	public Thread serverClientThread = null;

	/**
	 * Serverthread und Clientthread
	 */
	public Runnable serverThread, clientThread;


	/**
	 * String Ich
	 */
	public final static String ME = "Ich";

	/**
	 * String Er/Sie/Es
	 */
	public final static String OPPONENT = "Er/Sie/Es";

	/**
	 * String Console
	 */
	public final static String CONSOLE = "Console";
	private final static String newline = "\n";

	/**
	 *String /bye
	 */
	public final static String BYE = "/bye";

	/**
	 * String Du bist noch nicht bereit;
	 * gibt an, dass noch niemand bereit ist
	 */
	public final static String NOBODYREADY = "Du bist noch nicht bereit.";

	/**
	 * String Mitspieler ist bereit. Du auch?
	 */
	public final static String OPPONENTREADY = "Mitspieler ist bereit. Du auch?";

	/**
	 * String Du bist bereit. Dein Mitspieler noch nicht
	 */
	public final static String YOUREADY = "Du bist bereit. Dein Mitspieler noch nicht";

	/**
	 * String Spiel startet ...
	 */
	public final static String GAMESTARTS = "Spiel startet ...";


	/**
	 * String enthaelt die zu sendende Nachricht
	 */
	public String messageToSend = "";

	/**
	 * gibt an, ob der Client mit dem Server verbunden ist
	 */
	public Boolean serverIsConnectedToClient = false;

	/**
	 * enthalten die Input bzw Output Daten
	 */
	public Data input, output;

	/**
	 * gibt an, ob das Spiel vorbei ist
	 */
	public Boolean isOver = true;

	/**
	 * gibt an, ob der Benutzer bereit zum Spielen ist
	 */
	public boolean ready = false;


	/**
	 * initialisiert das Chatfenster
	 * @param houston
	 */
	public MultiPlayer(Houston houston) {
		this.houston = houston;


		this.setLayout(null);

		startMenu = new JButton("Hauptmenue");
		startMenu.addActionListener(this);
		startMenu.setBounds(houston.width - 220, 20, 200, 30);
		this.add(startMenu);

		backToGame = new JButton("Zur\u00fcck ins Spiel");
		backToGame.setEnabled(false);
		backToGame.addActionListener(this);
		backToGame.setBounds(houston.width - 450, 20, 200, 30);
		this.add(backToGame);

		// ChatPanel
		TitledBorder chatBorder = BorderFactory.createTitledBorder("Chat");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 0, 0, 5);

		chatPanel = new JPanel();
		chatPanel.setLayout(new GridBagLayout());
		chatPanel.setBorder(chatBorder);
		chatPanel.setBounds(20, 80, 500, 240);

		chatHistory = new JTextArea("Chatverlauf ...\n", 0, 0);
		chatHistory.setEditable(false);
		chatHistory.setFont(new Font("Arial", Font.PLAIN, 13));
		chatHistory.setLineWrap(true);
		chatHistory.setWrapStyleWord(true);
		JScrollPane chatScrollPane = new JScrollPane(chatHistory);
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 4;
		chatPanel.add(chatScrollPane, gbc);

		chatInput = new JTextField(30);
		chatInput.setEnabled(false);
		chatInput.addActionListener(this);
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridheight = 1;
		chatPanel.add(chatInput, gbc);

		this.add(chatPanel);

		// GamePanel
		TitledBorder gameBorder = BorderFactory.createTitledBorder("Game");
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridBagLayout());
		gamePanel.setBorder(gameBorder);
		gamePanel.setBounds(20, 340, 500, 240);

		gameStatus = new JTextField();
		gameStatus.setEditable(false);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gamePanel.add(gameStatus, gbc);

		notReadyToPlay = new JButton("Nicht Bereit");
		notReadyToPlay.addActionListener(this);
		notReadyToPlay.setEnabled(false);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gamePanel.add(notReadyToPlay, gbc);

		readyToPlay = new JButton("Bereit");
		readyToPlay.addActionListener(this);
		readyToPlay.setEnabled(false);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gamePanel.add(readyToPlay, gbc);

		this.add(gamePanel);

		// ServerPanel
		TitledBorder serverBorder = BorderFactory.createTitledBorder("Server");
		serverPanel = new JPanel();
		serverPanel.setLayout(new GridLayout(0, 1, 5, 5));
		serverPanel.setBorder(serverBorder);
		serverPanel.setBounds(houston.width - 220, 80, 200, 240);

		JLabel serverIpLabel = new JLabel("IP");
		serverPanel.add(serverIpLabel);
		serverIp = new JTextField(14);
		serverIp.setEditable(false);
		serverPanel.add(serverIp);

		JLabel serverPortLabel = new JLabel("Port");
		serverPanel.add(serverPortLabel);
		serverPort = new JTextField(6);
		serverPanel.add(serverPort);

		createServer = new JButton("Server erstellen");
		createServer.addActionListener(this);
		serverPanel.add(createServer);

		killServer = new JButton("Server schliessen");
		killServer.addActionListener(this);
		killServer.setEnabled(false);
		serverPanel.add(killServer);

		this.add(serverPanel);

		// ClientPanel
		TitledBorder clientboBorder = BorderFactory.createTitledBorder("Client");
		clientPanel = new JPanel();
		clientPanel.setLayout(new GridLayout(0, 1, 5, 5));
		clientPanel.setBorder(clientboBorder);
		clientPanel.setBounds(houston.width - 220, 340, 200, 240);

		JLabel clientIpPort = new JLabel("IP");
		clientPanel.add(clientIpPort);
		clientIp = new JTextField(14);
		clientPanel.add(clientIp);

		JLabel clientPortLabel = new JLabel("Port");
		clientPanel.add(clientPortLabel);
		clientPort = new JTextField(6);
		clientPanel.add(clientPort);

		connectAsClient = new JButton("Als Client verbinden");
		connectAsClient.addActionListener(this);
		clientPanel.add(connectAsClient);

		disconnectAsClient = new JButton("Verbindung schliessen");
		disconnectAsClient.addActionListener(this);
		disconnectAsClient.setEnabled(false);
		clientPanel.add(disconnectAsClient);

		this.add(clientPanel);

		// Diverses
		fillTextFieldsWithContent();
		createThreads();
	}


	private void fillTextFieldsWithContent() {
		try {
			serverIp.setText(InetAddress.getLocalHost().getHostAddress());
			clientIp.setText(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {e.printStackTrace();}
		serverPort.setText("2345");
		clientPort.setText("2345");
	}


	private void createThreads() {
		ServerThread server = new ServerThread(this);
		serverThread = new Thread(server);

		ClientThread client = new ClientThread(this);
		clientThread = new Thread(client);
	}


	private void assignThread(String type) {
		if ((serverClientThread == null) && type.contains("server")) {
			serverClientThread = new Thread(serverThread);
		} else if ((serverClientThread == null) && type.contains("client")) {
			serverClientThread = new Thread(clientThread);
		}
	}

	private void setClientServerButtonEnabled(Boolean createServer, Boolean killServer, Boolean connectAsClient, Boolean disconnectAsClient) {
		this.createServer.setEnabled(createServer);
		this.killServer.setEnabled(killServer);
		this.connectAsClient.setEnabled(connectAsClient);
		this.disconnectAsClient.setEnabled(disconnectAsClient);

		// Wenn eine Verbindung aufgebaut wird, schalte alle eingaben aus
		if ((createServer == false) || (connectAsClient == false)) {
			startMenu.setEnabled(false);
			serverPort.setEnabled(false);
			clientIp.setEnabled(false);
			clientPort.setEnabled(false);
		} else {
			startMenu.setEnabled(true);
			serverPort.setEnabled(true);
			clientIp.setEnabled(true);
			clientPort.setEnabled(true);
		}
		chatInput.setEnabled(false);
		backToGame.setEnabled(false);
	}

	/**
	 * veraendert den Zustand des Bereit und des  Nicht bereit Buttons
	 * @param enableButtons
	 */
	public void setReadyToPlayButtonEnabled(Boolean enableButtons) {
		if (enableButtons == true) {
			readyToPlay.setEnabled(!ready);
			notReadyToPlay.setEnabled(ready);
		} else {
			readyToPlay.setEnabled(false);
			notReadyToPlay.setEnabled(false);
		}
	}


	/**
	 * trennt die Verbindung zwischen Client und Server
	 */
	public void stop() {
		serverClientThread = null;

		setClientServerButtonEnabled(true, false, true, false);
		ready = false;
		setReadyToPlayButtonEnabled(false);
		gameStatus.setText("");
	}


	private void createServer() {
		if ((serverClientThread == null) || !(serverClientThread.getState() == Thread.State.TERMINATED)) {
			messageToSend = "";
			setClientServerButtonEnabled(false, true, false, false);
			assignThread("server");
			serverClientThread.start();
		} else {
			System.out.println("Server laeuft schon");
		}
	}

	private void killServer() {
		if (serverIsConnectedToClient) {
			disconnectClient();
		} else {
			try (Socket s = new Socket(serverIp.getText(), Integer.parseInt(serverPort.getText()))) {
				ObjectOutputStream o = new ObjectOutputStream(s.getOutputStream());
				Data data = new Data();
				data.closeConnection = true;
				o.writeObject(data);
			} catch (NumberFormatException | IOException e) {}
		}
		stop();
	}

	private void connectClient() {
		if ((serverClientThread == null) || !(serverClientThread.getState() == Thread.State.TERMINATED)) {
			messageToSend = "";
			setClientServerButtonEnabled(false, false, false, true);
			assignThread("client");
			serverClientThread.start();
		} else {
			System.out.println("Client schon verbunden");
		}
	}

	private void disconnectClient() {
		sendChatMessage(MultiPlayer.BYE);
		appendChatMessage(MultiPlayer.BYE, MultiPlayer.ME);
	}

	/**
	 * zeigt Nachrichten und deren Autor im Chat an
	 * @param message
	 * @param author
	 */
	public void appendChatMessage(String message, String author) {
		message = message.trim();
		if (message.length() > 0) {
			chatHistory.append(author + ": " + message + newline);
			chatHistory.setCaretPosition(chatHistory.getDocument().getLength());
		}
	}

	/**
	 * zeigt Nachrichten von der Konsole an
	 * @param message
	 */
	public void appendChatMessage(String message) {
		appendChatMessage(message, MultiPlayer.CONSOLE);
	}

	private void sendChatMessage(String message) {
		messageToSend = message.trim();
	}

	/**
	 * uebermittelt dem Mitspieler, dass der Benutzer bereit zum Spielen ist
	 * @param ready
	 */
	public void sendReady(Boolean ready) {
		this.ready = ready;
		output.ready = ready;
	}

	/**
	 * veraendert die Information darueber ob die Spieler bereit sind
	 * @param status
	 */
	public void changeGameStatus(String status) {
		gameStatus.setText(status);
	}

	/**
	 * zeigt Nachrichten vom Mitspieler an
	 */
	public void handleChat() {
		appendChatMessage(input.message, MultiPlayer.OPPONENT);
	}

	/**
	 * reagiert abhaengig davon, ob die Spieler bereit sind oder nicht
	 */
	public void handleGameStatus() {
		if (!houston.gameIsRunning && houston.gameOver) {
			if (input.ready && ready) {
				changeGameStatus(MultiPlayer.GAMESTARTS);
				startMultiplayerGame();
			} else if (input.ready && !ready) {
				changeGameStatus(MultiPlayer.OPPONENTREADY);
			} else if (!input.ready && ready) {
				changeGameStatus(MultiPlayer.YOUREADY);
			} else {
				changeGameStatus(MultiPlayer.NOBODYREADY);
			}
		}
	}

	/**
	 * veraendert im Spiel die Karte und beendet das Spiel
	 */
	public void handleGameUpdatesByNetworkInput() {
		if (houston.gameIsRunning && !input.ready) {
			exitGame();
		}
		if (!houston.gameIsRunning && !houston.gameOver && !input.ready) {
			exitGame();
		}
		if ((input.mapNumber > houston.map.getMapNumber() && input.levelNumber == houston.map.getLevelNumber()) ||
				(input.levelNumber > houston.map.getLevelNumber())) {
			houston.gameLogic.changeLevel(input.levelNumber, input.mapNumber);
		}
	}

	private void startMultiplayerGame() {
		houston.gameLogic.setupNewGame(0, 0);
		houston.changeAppearance(false, true, Houston.GAME);
	}


	/**
	 * kontrolliert die Buttons
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object buttonClicked = e.getSource();

		if (buttonClicked == startMenu) {
			houston.changeAppearance(Houston.STARTMENU);
		} else if (buttonClicked == createServer) {
			createServer();
		} else if (buttonClicked == killServer) {
			killServer();
		} else if (buttonClicked == connectAsClient) {
			connectClient();
		} else if (buttonClicked == disconnectAsClient) {
			disconnectClient();
		} else if (buttonClicked == chatInput) {
			sendChatMessage(chatInput.getText());
			appendChatMessage(chatInput.getText(), MultiPlayer.ME);
			chatInput.setText("");
		} else if (buttonClicked == readyToPlay) {
			sendReady(true);
			handleGameStatus();
			setReadyToPlayButtonEnabled(true);
		} else if (buttonClicked == notReadyToPlay) {
			exitGame();
		} else if (buttonClicked == backToGame) {
			if (!houston.gameOver) {
				houston.changeAppearance(false, true, Houston.GAME);
			}
		}
	}


	/**
	 * uebergibt:
	 * Rechteck vom Spieler;
	 * Kartennummer;
	 * ob der Spieler noch lebt;
	 * Levelnummer;
	 */
	public void doGameUpdates() {
		output.playerBounds.setRect(houston.player.getBounds());
		output.mapNumber = houston.map.getMapNumber();
		output.playerIsAlive = (houston.player.getLives() > 0);
		output.levelNumber = houston.map.getLevelNumber();
	}

	/**
	 * zeichnet den Mitspieler
	 * @param g
	 */
	public void drawObjects(Graphics2D g) {
		g.drawImage(houston.player.texture, (int) input.playerBounds.getX()-2, (int) input.playerBounds.getY()-18, null);
	}

	public void onLevelChange() {
	}


	/**
	 * beendet das Spiel und wechselt zu der uebergebene Karte
	 * @param cardName
	 */
	public void exitGame(String cardName) {
		sendReady(false);
		houston.changeAppearance(true, false, cardName);
		handleGameStatus();
		setReadyToPlayButtonEnabled(true);
	}

	/**
	 * beendet das Spiel und wechselt zurueck zum Chatfenster
	 */
	public void exitGame() {
		exitGame(Houston.MULTIPLAYER);
	}




}
