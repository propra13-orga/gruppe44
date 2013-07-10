package Network;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import Main.Houston;

public class MultiPlayer extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	public Houston houston;


	private JPanel
	chatPanel,
	gamePanel,
	serverPanel,
	clientPanel;

	private JTextArea chatHistory;

	public JTextField
	gameStatus,
	serverPort,
	serverIp,
	clientIp,
	clientPort,
	chatInput;

	public JButton
	readyToPlay,
	notReadyToPlay,
	startMenu,
	backToGame,
	createServer,
	killServer,
	connectAsClient,
	disconnectAsClient;


	public Thread serverClientThread = null;
	public Runnable serverThread, clientThread;
	private String type;


	public final static String ME = "Ich";
	public final static String OPPONENT = "Er/Sie/Es";
	public final static String CONSOLE = "Console";
	private final static String newline = "\n";
	public final static String BYE = "/bye";

	public final static String NOBODYREADY = "Du bist noch nicht bereit.";
	public final static String OPPONENTREADY = "Mitspieler ist bereit. Du auch?";
	public final static String YOUREADY = "Du bist bereit. Dein Mitspieler noch nicht";
	public final static String GAMESTARTS = "Spiel startet ...";


	public String messageToSend = "";
	public Boolean serverIsConnectedToClient = false;
	public Data input, output;
	public Boolean isOver;
	public boolean ready;


	public MultiPlayer(Houston houston) {
		this.houston = houston;


		this.setLayout(null);

		startMenu = new JButton("Hauptmenue");
		startMenu.addActionListener(this);
		startMenu.setBounds(houston.width - 220, 20, 200, 30);
		this.add(startMenu);

		backToGame = new JButton("Zur\u00fcck ins Spiel");
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
		this.type = type;
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
	}

	public void setReadyToPlayButtonEnabled(Boolean enableButtons) {
		if (enableButtons == true) {
			readyToPlay.setEnabled(!ready);
			notReadyToPlay.setEnabled(ready);
		} else {
			readyToPlay.setEnabled(false);
			notReadyToPlay.setEnabled(false);
		}
	}


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

	public void appendChatMessage(String message, String author) {
		message = message.trim();
		if (message.length() > 0) {
			chatHistory.append(author + ": " + message + newline);
			chatHistory.setCaretPosition(chatHistory.getDocument().getLength());
		}
	}

	public void appendChatMessage(String message) {
		appendChatMessage(message, MultiPlayer.CONSOLE);
	}

	private void sendChatMessage(String message) {
		messageToSend = message.trim();
	}

	private void sendReady(Boolean ready) {
		output.ready = ready;
	}

	public void changeGameStatus(String status) {
		gameStatus.setText(status);
	}

	public void handleGameStatus() {
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

	private void startMultiplayerGame() {

		 houston.changeAppearance(false, true, Houston.GAME);
	}


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
			ready = true;
			setReadyToPlayButtonEnabled(true);
			handleGameStatus();
			sendReady(true);
		} else if (buttonClicked == notReadyToPlay) {
			ready = false;
			setReadyToPlayButtonEnabled(true);
			handleGameStatus();
			sendReady(false);
		} else if (buttonClicked == backToGame) {
			System.out.println(backToGame.getText());
		}
	}


	public void doGameUpdates() {
		output.playerBounds.setRect(houston.player.getBounds());
	}

	public void drawObjects(Graphics2D g) {
		g.fillRect((int) input.playerBounds.getX(), (int) input.playerBounds.getY(), (int) input.playerBounds.getWidth(), (int) input.playerBounds.getHeight());
	}



}
