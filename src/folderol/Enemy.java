package folderol;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy extends Movable{
	boolean shouldBeRemoved = false;
	private int direction;
	
	public Enemy (Point2D resetPoint, int direction){
		this.direction = direction;
		setDirection();
		
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
	
	public void onHealthDecreased() {
		if (health <= 0) {
			shouldBeRemoved = true;
		}
	}
	
	private void turn180() {
		direction = (direction + 6) % 12;
		setDirection();
	}
	
	private void setDirection() {
		switch (direction) {
		case 0: up = true;		down = false;	break;
		case 3: right = true;	left = false;	break;
		case 6: down= true;		up = false;		break;
		case 9: left = true;	right = false;	break;
		default: break;
		}		
	}

	@Override
	void wallHit() {
		turn180();
	}
}
