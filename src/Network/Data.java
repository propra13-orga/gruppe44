package Network;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class Data implements Serializable {

	private transient static final long serialVersionUID = 1L;


	public Boolean closeConnection = false;
	public String name = "";
	public String message = "";
	public Boolean ready = false;
	public Rectangle2D playerBounds = new Rectangle2D.Double();
	public int levelNumber = 0;
	public int mapNumber = 0;
	public Boolean playerIsAllive = true;


	public Data() {}


	public boolean isEqualTo(Data data) {
		return (
			(closeConnection == data.closeConnection)
			&& (name.equals(data.name))
			&& (message.equals(data.message))
			&& (ready == data.ready)
			&& (playerBounds.equals(data.playerBounds))
			&& (levelNumber != data.levelNumber)
			&& (mapNumber != data.mapNumber)
			);
	}


	public void copyValuesFrom(Data data) {
		closeConnection = data.closeConnection;
		name = data.name;
		message = data.message;
		ready = data.ready;
		playerBounds.setRect(data.playerBounds);
		levelNumber = data.levelNumber;
		mapNumber = data.mapNumber;
	}


}
