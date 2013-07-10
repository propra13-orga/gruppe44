package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread implements Runnable {

	private MultiPlayer mp;

	private Data output;

	public ClientThread(MultiPlayer mp) {
		this.mp = mp;
	}

	@Override
	public void run() {
		String ip = mp.clientIp.getText();
		int port = Integer.parseInt(mp.clientPort.getText());

		try (Socket server = new Socket(ip, port)) {

			// Wenn die Verbindung steht, stelle einige Werte ein
			mp.appendChatMessage("Client verbunden mit " + server.getLocalSocketAddress());
			mp.chatInput.setEnabled(true);
			mp.setReadyToPlayButtonEnabled(true);
			mp.changeGameStatus(MultiPlayer.NOBODYREADY);

			// Hohle die eingehende und ausgehende Verbindung zum Client
			ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(server.getInputStream());

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

					if (mp.output.message.contains(MultiPlayer.BYE)) {
						mp.output.closeConnection = true;
						mp.isOver = true;
					}
					mp.messageToSend = "";
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
			mp.appendChatMessage("Clientfehler: " + e.getMessage());
		}

		mp.appendChatMessage("Client getrennt.");
		mp.stop();
	}

}
