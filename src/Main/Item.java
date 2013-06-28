package Main;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Item {

	public Rectangle2D bounds;
	public int itemType;
	private BufferedImage texture;
	public boolean remove;

	public Item(BufferedImage texture, Point2D singleItemPosition, int itemType) {
		this.texture = texture;

		this.bounds = new Rectangle2D.Double(singleItemPosition.getX(), singleItemPosition.getY() - texture.getHeight() + 32, 32, 32);
		this.itemType = itemType;
	}

	public void drawObjects(Graphics2D g) {
		g.drawImage(texture, (int) bounds.getX(), (int) bounds.getY(), null);
	}

}
