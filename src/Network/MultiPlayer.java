package Network;

import java.awt.Font;
import java.awt.GridLayout;
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

public class MultiPlayer extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Houston houston;


	private JPanel
	chatPanel,
	serverPanel,
	clientPanel;

	private JTextArea chatHistory;

	public JTextField
	serverPort,
	serverIp,
	clientIp,
	clientPort,
	chatInput;

	private JButton
	startMenu,
	createServer,
	killServer,
	connectAsClient,
	disconnectAsClient;


	public Thread serverClientThread = null;
	public Runnable serverThread, clientThread;


	private final static String newline = "\n";
	public final static String ME = "Ich";
	public final static String OPPONENT = "Er/Sie/Es";
	public final static String CONSOLE = "Console";
	public String messageToSend = "";
	public Boolean serverIsConnectedToClient = false;
	public Data input, output;
	public Boolean isOver;


	public MultiPlayer(Houston houston) {
		this.houston = houston;


		this.setLayout(null);

		startMenu = new JButton("Hauptmenue");
		startMenu.addActionListener(this);
		startMenu.setBounds(houston.width - 220, 20, 200, 30);
		this.add(startMenu);

		// ChatPanel
		TitledBorder chatBorder = BorderFactory.createTitledBorder("Chat");
		chatPanel = new JPanel();
		chatPanel.setLayout(new GridLayout(0, 1, 5, 5));
		chatPanel.setBorder(chatBorder);
		chatPanel.setBounds(20, 80, 500, 240);

		chatHistory = new JTextArea("Chatverlauf ...\n", 0, 0);
		chatHistory.setEditable(false);
		chatHistory.setFont(new Font("Arial", Font.PLAIN, 13));
		chatHistory.setLineWrap(true);
		chatHistory.setWrapStyleWord(true);
		JScrollPane chatScrollPane = new JScrollPane(chatHistory);
		chatPanel.add(chatScrollPane);

		chatInput = new JTextField(14);
		chatInput.setEnabled(false);
		chatInput.addActionListener(this);
		chatPanel.add(chatInput);

		this.add(chatPanel);

		// ServerPanel
		TitledBorder serverBorder = BorderFactory.createTitledBorder("Server");
		serverPanel = new JPanel();
		serverPanel.setLayout(new GridLayout(0, 1, 5, 5));
		serverPanel.setBorder(serverBorder);
		serverPanel.setBounds(houston.width - 220, 80, 200, 240);

		JLabel serverIpLabel = new JLabel("IP");
		serverPanel.add(serverIpLabel);
		serverIp = new JTextField(14);
		serverIp.setEnabled(false);
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

	private void setButtonEnabled(Boolean createServer, Boolean killServer, Boolean connectAsClient, Boolean disconnectAsClient) {
		this.createServer.setEnabled(createServer);
		this.killServer.setEnabled(killServer);
		this.connectAsClient.setEnabled(connectAsClient);
		this.disconnectAsClient.setEnabled(disconnectAsClient);

		// Wenn eine Verbindung aufgebaut wird, schalte alle eingaben aus
		if ((createServer == false) || (connectAsClient == false)) {
			chatInput.setEnabled(false);
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
	}


	public void stop() {
		serverClientThread = null;
		setButtonEnabled(true, false, true, false);
	}


	private void createServer() {
		if ((serverClientThread == null) || !(serverClientThread.getState() == Thread.State.TERMINATED)) {
			messageToSend = "";
			setButtonEnabled(false, true, false, false);
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
			setButtonEnabled(false, false, false, true);
			assignThread("client");
			serverClientThread.start();
		} else {
			System.out.println("Client schon verbunden");
		}
	}

	private void disconnectClient() {
		sendChatMessage("/bye");
		appendChatMessage("/bye", MultiPlayer.ME);
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
		}
	}


}
