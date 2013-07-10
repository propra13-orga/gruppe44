package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable {

	private MultiPlayer mp;
	private Data output;


	public ServerThread(MultiPlayer mp) {
		this.mp = mp;
	}

	@Override
	public void run() {
		mp.appendChatMessage("Server gestartet. Auf Client warten ...");
		int port = Integer.parseInt(mp.serverPort.getText());

		while (mp.serverClientThread == Thread.currentThread()) {

			try (ServerSocket server = new ServerSocket(port)) {

				// Auf Verbindung vom Client warten
				Socket client = server.accept();

				// Wenn die Verbindung steht, stelle einige Werte ein
				mp.serverIsConnectedToClient = true;
				mp.chatInput.setEnabled(true);
				mp.setReadyToPlayButtonEnabled(true);
				mp.changeGameStatus(MultiPlayer.NOBODYREADY);
				mp.appendChatMessage("Verbunden mit Client " + client.getLocalSocketAddress());

				// Hohle die eingehende und ausgehende Verbindung zum Client
				ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(client.getInputStream());

				// Erstelle einen Thread, der einkommende Daten separat liest
				ReaderThread reader = new ReaderThread(mp, in);
				Thread readerThread = new Thread(reader);
				readerThread.start();


				mp.isOver = false;
				mp.input = new Data();
				mp.output = new Data();
				this.output = new Data();

				while (!mp.isOver) {
					// Nachricht setzen/aendern
					if (mp.messageToSend.length() > 0) {
						mp.output.message = mp.messageToSend;
						mp.messageToSend = "";

						if (mp.output.message.contains(MultiPlayer.BYE)) {
							mp.output.closeConnection = true;
							mp.isOver = true;
						}
					}

					// Daten schreiben, wenn sie veraendert wurden
					if (!this.output.isEqualTo(mp.output)) {
						out.reset();
						out.writeObject(mp.output);

						mp.output.message = "";
						this.output.copyValuesFrom(mp.output);
					}

					// Schlaaaaaaaafen ... nur kurz
					try {
						Thread.sleep(1000 / mp.houston.preferredFps);
					} catch (InterruptedException e) {}
				} // isOver

			} catch (IOException e) {
				mp.appendChatMessage("Serverfehler: " + e.getMessage());
				break;
			}

			mp.appendChatMessage("Client getrennt.");
			mp.serverIsConnectedToClient = false;
			mp.chatInput.setEnabled(false);
			mp.ready = false;
			mp.setReadyToPlayButtonEnabled(false);
			mp.changeGameStatus("");

		} // Ende von While

		mp.appendChatMessage("Server beendet.");
		mp.stop();
	}

}
