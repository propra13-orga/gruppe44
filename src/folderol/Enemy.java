package folderol;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy extends Movable{
	boolean shouldBeRemoved = false;
	
	public Enemy (Point2D resetPoint, int direction){
		
		if(direction == 0) {
			up = true;
		}if(direction == 3) {
			right = true;
		}if(direction == 6) {
			down = true;
		}else if(direction == 9) {
			left = true;
		}
		speed = 128;
		health = 100;
		
		bounds = new Rectangle2D.Double(resetPoint.getX(), resetPoint.getY(), 28, 28);

		try {
			texture = ImageIO.read(new File("./res/img/german_f2.png"));
			texture = texture.getSubimage(0, 0, 32, 46);
		} catch (IOException e) {e.printStackTrace();}	
		
	}

	@Override
	public void drawObjects(Graphics2D g) {
			g.draw(bounds);
			g.drawImage(texture, (int) bounds.getX()-2, (int) bounds.getY()-18, null);
	}
	
	public void onHitWall() {
		// Wechsle die Richtung
		if (left) {
			left = false;
			right = true;
		} else if (right) {
			right = false;
			left = true;
		} else if (up) {
			up = false;
			down = true;
		} else if (down) {
			down = false;
			up = true;
		}
	}
	
	public void onHealthDecreased() {
		if (health <= 0) {
			shouldBeRemoved = true;
		}
	}
}
