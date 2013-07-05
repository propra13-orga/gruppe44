package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class DemoClient {

	private static int port = 2345;
	private static String ip = "127.0.0.1";

	public static void main(String[] args) {

		try (Socket server = new Socket(ip, port)) {

			ObjectOutputStream objOut = new ObjectOutputStream(server.getOutputStream());
			ObjectInputStream objIn = new ObjectInputStream(server.getInputStream());


			Data input, output = new Data();
			Boolean running = true;

			while (running) {
				// Eingabe
				String message = JOptionPane.showInputDialog(null,
						"Zu verschickende Nachricht eingeben: ",
						"Eingabe der Nachricht", JOptionPane.PLAIN_MESSAGE);

				if (message != null) {
					output.name = message;
					objOut.reset();
					objOut.writeObject(output);
				}
				else {
					output.name = "/bye";
					objOut.reset();
					objOut.writeObject(output);
				}
				// Ausgabe
				if ((input = (Data) objIn.readObject()) != null) {
					System.out.println("Der Server sagt: " + input.name);
					if (input.name.toLowerCase().contains("/bye")) {
						System.out.println("ende ...");
						break;
					}
				}
			}


		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
