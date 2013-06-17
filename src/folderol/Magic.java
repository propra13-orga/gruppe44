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
	
	private BufferedImage picture;
	private Point2D centerPosition;
	private Point2D endPosition;
	public boolean remove;
	private boolean magicFromPlayer;
	
	public Magic(Houston houston, Point2D centerPosition, Point2D endPosition, boolean playerMagic) {
		this.houston = houston;
		this.centerPosition = centerPosition;
		this.endPosition = endPosition;
		this.magicFromPlayer = playerMagic;
		
		speed = 300;
		
		try {
			picture = ImageIO.read(new File("./res/img/tiles/ungleich.png"));
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
			if((bounds.intersects(enemy.bounds)) && (magicFromPlayer)) {
				enemy.decreaseHealth(10);
				if(enemy.health <= 0){
					if(houston.enemyLogic.bossIsAlive)
						houston.enemyLogic.bossIsAlive = false;
					enemy.remove= true;
				}
				remove = true;
				houston.player.increaseMoney(10);
			}
		}
		if((bounds.intersects(houston.player.bounds)) && (magicFromPlayer == false)){
			houston.player.decreaseHealth(10);
			remove = true;
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
