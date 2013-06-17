package folderol;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Item {
	
	public Rectangle2D bounds;
	public int itemType;
	private BufferedImage texture;
	boolean remove;

	public Item(ItemLogic itemLogic, Point2D singleItemPosition, int itemType) {
		this.texture = itemLogic.texture.get(itemType);
		
		this.bounds = new Rectangle2D.Double(singleItemPosition.getX(), singleItemPosition.getY() - texture.getHeight() + 32, 32, 32);
		this.itemType = itemType;
	}
	
	public void drawObjects(Graphics2D g) {
		g.drawImage(texture, (int) bounds.getX(), (int) bounds.getY(), null);
	}

}
