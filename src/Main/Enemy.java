package Main;

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
		setSpeed(128);
		setBounds(new Rectangle2D.Double(resetPoint.getX(), resetPoint.getY(), 28, 28));

		this.direction = direction;
		setDirection();
		this.enemyType = enemyType;
		setEnemyType();
		
		try {
			texture = ImageIO.read(new File("./res/img/characters/french_m1.png"));
			texture = texture.getSubimage(0, 0, 32, 46);
		} catch (IOException e) {e.printStackTrace();}	
	}

	@Override
	public void drawObjects(Graphics2D g) {
		super.drawObjects(g);
		g.drawImage(texture, (int) getX() - 2, (int) getY() - 18, null);
	}
	
	private void turn180() {
		direction = (direction + 6) % 12;
		setDirection();
	}
	
	private void setDirection() {
		switch (direction) {
		case 0: setUp(100);			setDown(0);		break;
		case 3: setRight(100);		setLeft(0);		break;
		case 6: setDown(100);		setUp(0);		break;
		case 9: setLeft(100);		setRight(0);	break;
		default: break;
		}		
	}
	
	private void setEnemyType() {
		switch (enemyType) {
		case 0: shoot = 0;		setHealth(10);	break; // Gegner Level 1, schiesst nicht
		case 1: shoot = 1;		setHealth(10);	break; // Gegner Level 1, schiesst
		case 2: shoot = 1;		setHealth(60);	break; // Bossgegner Level 1
		case 3: shoot = 0;		setHealth(20);	break; // Gegner Level 2, schiesst nicht
		case 4: shoot = 1;		setHealth(20);	break; // Gegner Level 2, schiesst 
		case 5: shoot = 1;		setHealth(80);	break; // Bossgegner Level 2
		case 6: shoot = 0;		setHealth(30);	break; // Gegner Level 3, schiesst nicht
		case 7: shoot = 1;		setHealth(30);	break; // Gegner Level 3, schiesst
		case 8: shoot = 1;		setHealth(100);	break; // Bossgegner Level 3
		default: break;
		}
	}

	@Override
	public void onWallHit() {
		turn180();
	}
}
