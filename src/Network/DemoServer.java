package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DemoServer {

	private static int port = 2345;

	public static void main(String[] args) {

		try (ServerSocket server = new ServerSocket(port)) {
			Socket client = server.accept();
			System.out.println(client.getInetAddress().getHostAddress()+":"+client.getPort());

			ObjectOutputStream objOut = new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream objIn = new ObjectInputStream(client.getInputStream());



			Data input;
			Boolean running = true;

			while (running) {
				if (((input = (Data) objIn.readObject()) != null) && !(input.name.toLowerCase().contains("/bye"))) {
					objOut.writeObject(input);
				} else {
					objOut.writeObject(input);
					running = false;
				}
			}



		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
