package Main;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/** Items des Spielers */
public class Item {

	/** Rechteck um die Items */
	public Rectangle2D bounds;

	/** legt den Itemtyp fest */
	public int itemType;
	private BufferedImage texture;

	/** gibt an, ob das Item aufgenommen wurde */
	public boolean remove;

	/**
	 * legt das Item abhaengig von der uebergebenen Textur, Position, Itemtyp
	 * @param texture
	 * @param singleItemPosition
	 * @param itemType
	 */
	public Item(BufferedImage texture, Point2D singleItemPosition, int itemType) {
		this.texture = texture;

		this.bounds = new Rectangle2D.Double(singleItemPosition.getX(), singleItemPosition.getY() - texture.getHeight() + 32, 32, 32);
		this.itemType = itemType;
	}

	/**
	 * zeichnet die Items
	 * @param g
	 */
	public void drawObjects(Graphics2D g) {
		g.drawImage(texture, (int) bounds.getX(), (int) bounds.getY(), null);
	}

}
