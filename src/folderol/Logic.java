package folderol;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class Logic {
	
	Houston houston;
	Player player;
	Map map;
	
	Point2D topLeft, topRight, bottomLeft, bottomRight;
	boolean r, y, gr, b;
	
	private long delta;
	private double dX, dY;
	

	public Logic(Houston houston) {
		this.houston = houston;
		this.player = houston.player;
		this.map = houston.map;
		
		// Die 4 Punkte stehen fuer die 4 Ecken des Player
		topLeft = new Point2D.Double();
		topRight = new Point2D.Double();
		bottomLeft = new Point2D.Double();
		bottomRight = new Point2D.Double();
		
	}

	
	void setupNewMap(int mapNumber) {
		map.renewMap(mapNumber);
		// Sucht und setzt die Ursprungsposition des Player
		Point2D spawn = new Point2D.Double();
		if ((spawn = map.singleSearch(8)) != null)
			player.setResetPosition(spawn);
		// Setzt den Player auf seine Ursprungsposition
		player.resetPosition();
	}


	// Berechnet z.B. alle Bewegungen und Kollisionen im Spiel
	void doGameUpdates(long delta) {
		this.delta = delta;
		
		controlPlayerMovement();
		detectSpecialTiles();
	}
	
	// Regelt den Wechsel zur naechsten Karte
	private void nextMap() {
		int mapNumber;
		if ((mapNumber = map.getMapNumber())+1 < map.getCountOfMaps())
			setupNewMap(mapNumber+1);
		else {
			houston.changeAppearance(true, false, "STARTMENU");
		}
	}


	// Setzt die Tastendruecke um in die Bewegung des Player
	void controlPlayerMovement() {
		dX = dY = 0;
		getPlayerCorners();
		// detectIntersection(); // entfernen, zusammen mit der Methode
		
		// Bewegung nach Links
		if (player.left && !player.right) {
			dX = -player.speed * (delta / 1e9);
			if (isValidXMovement(topLeft, bottomLeft, dX) == 1) {
				dX = 0;
			}
			
		// Bewegung nach Rechts
		} else if (player.right && !player.left) {
			dX = player.speed * (delta / 1e9);
			if (isValidXMovement(topRight, bottomRight, dX) == 1) {
				dX = 0;
			}
		}
		
		// Bewegung nach Oben
		if (player.up && !player.down) {
			dY = -player.speed * (delta / 1e9);
			if (isValidYMovement(topLeft, topRight, dY) == 1) {
				dY = 0;
			}
			
		// Bewegung nach Unten
		} else if (player.down && !player.up) {
			dY = player.speed * (delta / 1e9);
			if (isValidYMovement(bottomLeft, bottomRight, dY) == 1) {
				dY = 0;
			}
		}
		
		// Bewegt den Spieler falls notwendig
		if (dX != 0 || dY != 0)
			player.move(dX, dY);
	}
	


	// Ermittelt die Koordinaten der 4 Eckpunkte des Player
	private void getPlayerCorners() {
		topLeft.setLocation(player.getX(), player.getY());
		topRight.setLocation(player.getX() + player.getWidth(), player.getY());
		bottomLeft.setLocation(player.getX(), player.getY() + player.getHeight());
		bottomRight.setLocation(player.getX() + player.getWidth(), player.getY() + player.getHeight());
	}
	
	// Ermittelt, ob die, durch horizontale Bewegung dX, erechnete neue Position des Spielers in einer Wand liegt oder nicht
	private int isValidXMovement(Point2D pointTop, Point2D pointBottom, double dX) {
		if ((map.mapArray[(int) Math.floor(pointTop.getY()/32)-1][(int) Math.floor((pointTop.getX()+dX)/32)] == 1)) return 1;
		else if ((map.mapArray[(int) Math.floor(pointBottom.getY()/32)-1][(int) Math.floor((pointBottom.getX()+dX)/32)] == 1)) return 1;
		return 0;
	}

	// Ermittelt, ob die, durch vertikale Bewegung dY, erechnete neue Position des Spielers in einer Wand liegt oder nicht
	private int isValidYMovement(Point2D pointLeft, Point2D pointRight, double dY2) {
		if ((map.mapArray[(int) Math.floor((pointLeft.getY()+dY)/32)-1][(int) Math.floor(pointLeft.getX()/32)] == 1)) return 1;
		else if ((map.mapArray[(int) Math.floor((pointRight.getY()+dY)/32)-1][(int) Math.floor(pointRight.getX()/32)] == 1)) return 1;
		return 0;
	}

	// Ermittelt, ob sich der Player auf einer Speziellen Kachel befindet, und leitet entsprechende Massnahmen ein
	private void detectSpecialTiles() {
		if (player.left || player.right || player.up || player.down) {
			if ((map.mapArray[(int) Math.floor((player.getY() + (player.getHeight()/2))/32)-1][(int) Math.floor((player.getX() + (player.getWidth()/2))/32)]) == 9) { 
				nextMap();
			}
		}
	}
	
	


//	private void detectIntersection() {
//		r = y = gr = b = false;
//		
//		if((map.mapArray[(int) Math.floor(topLeft.getY()/32)-1][(int) Math.floor(topLeft.getX()/32)] == 1)) {
//			r = true;
//		}
//		if((map.mapArray[(int) Math.floor(topRight.getY()/32)-1][(int) Math.floor(topRight.getX()/32)] == 1)) {
//			y = true;
//		}
//		if((map.mapArray[(int) Math.floor(bottomLeft.getY()/32)-1][(int) Math.floor(bottomLeft.getX()/32)] == 1)) {
//			gr = true;
//		}
//		if((map.mapArray[(int) Math.floor(bottomRight.getY()/32)-1][(int) Math.floor(bottomRight.getX()/32)] == 1)) {
//			b = true;
//		}
//	}

	public void drawObjects(Graphics2D g) {
		
		g.setColor(Color.RED);
		g.drawRect( (int) (Math.floor(topLeft.getX()/32))*32, (int) (Math.floor(topLeft.getY()/32))*32, 32, 32);
		if(r) g.drawString("r", 600, 20);
		
		g.setColor(Color.YELLOW);
		g.drawRect( (int) (Math.floor(topRight.getX()/32))*32, (int) (Math.floor(topRight.getY()/32))*32, 32, 32);
		if(y) g.drawString("y", 620, 20);
		
		g.setColor(Color.GREEN);
		g.drawRect( (int) (Math.floor(bottomLeft.getX()/32))*32, (int) (Math.floor(bottomLeft.getY()/32))*32, 32, 32);
		if(gr) g.drawString("gr", 640, 20);
		
		g.setColor(Color.BLUE);
		g.drawRect( (int) (Math.floor(bottomRight.getX()/32))*32, (int) (Math.floor(bottomRight.getY()/32))*32, 32, 32);
		if(b) g.drawString("b", 660, 20);
	}

}
