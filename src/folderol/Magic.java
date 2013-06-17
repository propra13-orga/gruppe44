package folderol;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Magic extends Movable {
	
	private Houston houston;
	
	public BufferedImage picture;
	private Point2D centerPosition;
	private Point2D endPosition;
	boolean remove;
	
	public Magic(Houston houston, Point2D centerPosition, Point2D endPosition) {
		this.houston = houston;
		this.centerPosition = centerPosition;
		this.endPosition = endPosition;
		
		speed = 300;
		
		try {
			picture = ImageIO.read(new File("./res/img/ungleich.png"));
		} catch (IOException e){ e.printStackTrace(); }
		 
		bounds = new Rectangle2D.Double(centerPosition.getX() - (picture.getWidth() / 2), centerPosition.getY() - (picture.getHeight() / 2), picture.getWidth(), picture.getHeight());
		 
		calculateDirection();
	}
	
	@Override
	public void move(double dX, double dY) {
		super.move(dX, dY);
		centerPosition = getCenterPosition();
		onMoved();
	}
	
	public void drawObjects(Graphics2D g) {
		g.drawImage(picture, (int) bounds.getX(), (int) bounds.getY(), null);
	}
	
	private void onMoved() {
		for (Enemy enemy : houston.enemyLogic.enemies) {
			if(bounds.intersects(enemy.bounds)) {
				enemy.remove = true;
				remove = true;
				houston.player.increaseMoney(10);
			}
		}
	}
	
	private void calculateDirection() {
		left = right = up = down = false;
		
		double differenceX = endPosition.getX() - centerPosition.getX();
		double differenceY = endPosition.getY() - centerPosition.getY();

		if (Math.abs(differenceX) > Math.abs(differenceY)) {
			if (differenceX < 0)
				left = true;
			else
				right = true;
		} else {
			if (differenceY < 0)
				up = true;
			else
				down = true;
		}
		
	}

	@Override
	public void onWallHit() {
		remove = true;
	}

}
