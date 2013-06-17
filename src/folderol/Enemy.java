package folderol;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy extends Movable{
	public boolean remove;
	private int direction;
	private int enemyType;
	public int shoot;
	
	public Enemy (Point2D resetPoint, int direction, int enemyType){
		speed = 128;
		bounds = new Rectangle2D.Double(resetPoint.getX(), resetPoint.getY(), 28, 28);

		this.direction = direction;
		setDirection();
		this.enemyType = enemyType;
		setEnemyType();
		
		try {
			texture = ImageIO.read(new File("./res/img/german_f2.png"));
			texture = texture.getSubimage(0, 0, 32, 46);
		} catch (IOException e) {e.printStackTrace();}	
	}

	@Override
	public void drawObjects(Graphics2D g) {
		super.drawObjects(g);
		g.drawImage(texture, (int) bounds.getX() - 2, (int) bounds.getY() - 18, null);
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
	
	private void setEnemyType() {
		switch (enemyType) {
		case 0: shoot = 0;		health = 10;	break; //Gegner Level 1, schieﬂt nicht
		case 1: shoot = 1;		health = 10;	break; //Gegner Level 1, schieﬂt
		case 2: shoot = 1;		health = 60;	break; //Bossgegner Level 1
		case 3: shoot = 0;		health = 20;	break; //Gegner Level 2, schieﬂt nicht
		case 4: shoot = 1;		health = 20;	break; //Gegner Level 2, schieﬂt 
		case 5: shoot = 1;		health = 80;	break; //Bossgegner Level 2
		case 6: shoot = 0;		health = 30;	break; //Gegner Level 3, schieﬂt nicht
		case 7: shoot = 1;		health = 30;	break; //Gegner Level 3, schieﬂt
		case 8: shoot = 1;		health = 100;	break; //Bossgegner Level 3
		default: break;
		}
	}

	@Override
	public void onWallHit() {
		turn180();
	}
}
