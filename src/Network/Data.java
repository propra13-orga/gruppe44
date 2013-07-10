package Network;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Data implements Serializable {

	private transient static final long serialVersionUID = 1L;

//	private transient BufferedImage texture;

	public Boolean closeConnection = false;
	public String name = "";
	public String message = "";
//	public String gameStatus = MultiPlayer.NOBODYREADY;
	public Boolean ready = false;
	public Rectangle2D playerBounds = new Rectangle2D.Double();


	public Data() {}

	public boolean isEqualTo(Data data) {
		return (
			(closeConnection == data.closeConnection)
			&& (name.equals(data.name))
			&& (message.equals(data.message))
//			&& (gameStatus.equals(data.gameStatus))
			&& (ready == data.ready)
			&& (playerBounds.equals(data.playerBounds))
			);
	}

	public void copyValuesFrom(Data data) {
		closeConnection = data.closeConnection;
		name = data.name;
		message = data.message;
//		gameStatus = data.gameStatus;
		ready = data.ready;
		playerBounds.setRect(data.playerBounds);
	}


}
