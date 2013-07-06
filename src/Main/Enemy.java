package Main;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import Logic.MagicLogic;

public class Enemy extends Movable{
	public boolean remove;
	private int direction;
	private int enemyType;
	public int shoot;
	public String enemyField;

	public Enemy (BufferedImage textures, Point2D resetPoint, int direction, int enemyType){
		setSpeed(128);
		setBounds(new Rectangle2D.Double(resetPoint.getX(), resetPoint.getY(), 28, 28));

		this.direction = direction;
		setDirection();
		this.enemyType = enemyType;
		setEnemyType();

		tex_down = textures.getSubimage(0, 0, 32, 46);
		tex_left = textures.getSubimage(0, 49, 32, 45);
		tex_right = textures.getSubimage(0, 97, 32, 45);
		tex_up = textures.getSubimage(0, 145, 32, 45);
	}

	@Override
	public void drawObjects(Graphics2D g) {
		setDirection();
		super.drawObjects(g);
		g.drawImage(texture, (int) getX() - 2, (int) getY() - 18, null);
	}

	private void turn180() {
		direction = (direction + 6) % 12;
	}

	private void setDirection() {
		switch (direction) {
		case 0: setUp(100);			setDown(0);		texture = tex_up;		break;
		case 3: setRight(100);		setLeft(0);		texture = tex_right;	break;
		case 6: setDown(100);		setUp(0);		texture = tex_down;		break;
		case 9: setLeft(100);		setRight(0);	texture = tex_left;		break;
		default: break;
		}
	}

	private void setEnemyType() {
		switch (enemyType) {
		case 0: shoot = 0;		setHealth(10);	enemyField = MagicLogic.ANA; 		break; // Gegner Level 1, schiesst nicht
		case 1: shoot = 1;		setHealth(10);	enemyField = MagicLogic.ANA; 		break; // Gegner Level 1, schiesst
		case 2: shoot = 1;		setHealth(60);	enemyField = MagicLogic.ANA; 		break; // Bossgegner Level 1
		case 3: shoot = 0;		setHealth(25);	enemyField = MagicLogic.LA; 		break; // Gegner Level 2, schiesst nicht
		case 4: shoot = 1;		setHealth(25);	enemyField = MagicLogic.LA; 		break; // Gegner Level 2, schiesst
		case 5: shoot = 1;		setHealth(80);	enemyField = MagicLogic.LA; 		break; // Bossgegner Level 2
		case 6: shoot = 0;		setHealth(50);	enemyField = MagicLogic.INFO; 		break; // Gegner Level 3, schiesst nicht
		case 7: shoot = 1;		setHealth(50);	enemyField = MagicLogic.INFO; 		break; // Gegner Level 3, schiesst
		case 8: shoot = 1;		setHealth(100);	enemyField = MagicLogic.INFO; 		break; // Bossgegner Level 3
		default: break;
		}
	}

	@Override
	public void onWallHit() {
		turn180();
	}
	
	public int getExperience(int levelNumber, boolean isBoss){
		if(levelNumber == 1){
			if(isBoss){
				return 30;
			}else return 15;
			}
		else if(levelNumber == 2){
			if(isBoss){
				return 50;
			}else return 21;	
			} 
		else {
			if(isBoss){
				return 100;
			}else return 32;
		}
	}
}
