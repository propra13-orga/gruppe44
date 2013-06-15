package folderol;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Magic extends Movable {
	
	public BufferedImage picture;
	Point2D currentPosition;
	Point2D endPosition;
	Houston houston;
	boolean shouldBeRemoved = false;
	int damage = 100;
	
	/**
	 * Konstruktor.
	 */
	public Magic(Houston houston, Point2D startPosition, Point2D endPosition) {
		this.houston = houston;
		currentPosition = startPosition;
		this.endPosition = endPosition;
		speed = 300;
		try {
			picture = ImageIO.read(new File("./res/img/ungleich.png"));
		} catch (IOException e){ e.printStackTrace(); }
		 
		bounds = new Rectangle2D.Double(currentPosition.getX() - (picture.getWidth() / 2), currentPosition.getY() - (picture.getHeight() / 2), picture.getWidth(), picture.getHeight());
		 
		calculateDirection();
	}
	
	public void move(double dX, double dY) {
		super.move(dX, dY);
		currentPosition = getCenterPosition();
	}
	
	public void drawObjects(Graphics2D g) {
		g.drawImage(picture, (int) bounds.getX(), (int) bounds.getY(), null);
	}
	
	public void onMoved() {
		calculateDirection();
		for (Enemy enemy : houston.enemyLogic.enemies) {
			if(bounds.intersects(enemy.bounds)) {
				enemy.decreaseHealth(damage);
				shouldBeRemoved = true;
			}
		}
	}
	
	public void onHitWall() {
		shouldBeRemoved = true;
	}
	
	public void calculateDirection() {
		
		// Resette Richtung
		left = right = up = down = false;
		
		double diffX = endPosition.getX() - currentPosition.getX();
		double diffY = endPosition.getY() - currentPosition.getY();
		double distanceX = Math.abs(diffX);
		double distanceY = Math.abs(diffY);
		
		boolean hasReachedTarget = (distanceX + distanceY < (speed / 35));
		if (hasReachedTarget) {
			shouldBeRemoved = true;
			return;
		}
		
		boolean horizontalDistanceIsBiggerThanVerticalDistance = (distanceX > distanceY);
		if (horizontalDistanceIsBiggerThanVerticalDistance) {
			if (diffX < 0) {
				left = true;
			} else {
				right = true;
			}
		} else {
			if (diffY < 0) {
				up = true;
			} else {
				down = true;
			}
		}
	}
	
	/**
	 * Gibt die Mana Kosten an.
	 */
	public static int getManaCost() {
		return 10;
	}


}
