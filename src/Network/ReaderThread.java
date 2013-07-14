package Network;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * zustaendig fuer das Lesen des Inputs
 */
public class ReaderThread implements Runnable {

	private MultiPlayer mp;
	private ObjectInputStream in;

	/**
	 * @param mp
	 * @param in
	 */
	public ReaderThread(MultiPlayer mp, ObjectInputStream in) {
		this.mp = mp;
		this.in = in;
	}

	/**
	 * liest den Input, falls welche vorhanden ist
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (!mp.isOver) {
			try {
				if ((mp.input = (Data) in.readObject()) != null) {
					if (mp.input.closeConnection) {
						mp.isOver = true;
					}

					mp.handleChat();
					mp.handleGameStatus();
					mp.handleGameUpdatesByNetworkInput();
				}
			} catch (IOException | ClassNotFoundException e) {
				mp.isOver = true;
			}
		}
	}


}
