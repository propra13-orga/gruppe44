package Main;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Magic extends Movable {

	private BufferedImage texture;
	private Point2D centerPosition;
	private Point2D endPosition;
	public boolean remove;
	private boolean magicFromPlayer;

	public Magic(BufferedImage texture, Point2D centerPosition, Point2D endPosition, boolean playerMagic) {
		this.texture = texture;
		this.centerPosition = centerPosition;
		this.endPosition = endPosition;
		this.setMagicFromPlayer(playerMagic);
		
		setSpeed(300);
		
		setBounds(new Rectangle2D.Double(centerPosition.getX() - (texture.getWidth() / 2), centerPosition.getY() - (texture.getHeight() / 2), texture.getWidth(), texture.getHeight()));
		 
		calculateDirection();
	}
	
	public boolean isMagicFromPlayer() {
		return magicFromPlayer;
	}

	public void setMagicFromPlayer(boolean magicFromPlayer) {
		this.magicFromPlayer = magicFromPlayer;
	}

	@Override
	public void move(double dX, double dY) {
		super.move(dX, dY);
		centerPosition = getCenterPosition();
	}
	
	public void drawObjects(Graphics2D g) {
		g.drawImage(texture, (int) getBounds().getX(), (int) getBounds().getY(), null);
	}
	
	private void calculateDirection() {
		stop();
		
		double differenceX = endPosition.getX() - centerPosition.getX();
		double differenceY = endPosition.getY() - centerPosition.getY();

		if (Math.abs(differenceX) > Math.abs(differenceY)) {
			if (differenceX < 0)
				setLeft(100);
			else
				setRight(100);
		} else {
			if (differenceY < 0)
				setUp(100);
			else
				setDown(100);
		}
		
	}

	@Override
	public void onWallHit() {
		remove = true;
	}

}
