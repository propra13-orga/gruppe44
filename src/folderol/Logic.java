package folderol;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Logic {
	
	private Houston houston;
	private Player player;
	private Map map;
	private Story story;

	Point2D topLeft, topRight, bottomLeft, bottomRight;
	double tlX, tlY; // Temporäre Character Ecke
	private long delta;
	private double dX, dY;
	int value;

	public Logic(Houston houston) {
		this.houston	= houston;
		this.player		= houston.player;
		this.map		= houston.map;
		this.story		= houston.story;
		
		// 4 Punkte fuer die 4 Ecken des Player
		topLeft			= new Point2D.Double();
		topRight		= new Point2D.Double();
		bottomLeft		= new Point2D.Double();
		bottomRight		= new Point2D.Double();
	}

	// Startet ein neues Spiel
	public void setupNewGame(int levelNumber, int mapNumber) {
		player.resetHealthManaMoney(100, 100, 200);
		houston.inventory.clear();
		
		jumpToLevel(levelNumber, mapNumber);
	}
	
	// Springt zum angegebenen Level.
	public void jumpToLevel(int levelNumber, int mapNumber) {
		map.renewMap(levelNumber, mapNumber);
		// story.renewStory(levelNumber, mapNumber);

		// Sucht und setzt die Ursprungsposition des Player
		Point2D spawn = new Point2D.Double();
		if ((spawn = map.singleSearch(8)) != null)
			player.setResetPosition(spawn);

		// Setzt den Player auf seine Ursprungsposition
		player.resetPosition();
		houston.enemyLogic.enemies.clear();
		houston.enemyLogic.setSpawnPosition();
		
	}

	// Berechnet z.B. alle Bewegungen und Kollisionen im Spiel
	void doGameUpdates(long delta) {
		this.delta = delta;
		
		ArrayList<Magic> magics = new ArrayList<Magic>(houston.gamePanel.magics);
		for (Magic magic : houston.gamePanel.magics) {
			if(magic.shouldBeRemoved) {
				magics.remove(magic);
			}
		}
		houston.gamePanel.magics = magics;
		
		ArrayList<Enemy> enemies = new ArrayList<Enemy>(houston.enemyLogic.enemies);
		for (Enemy enemy : houston.enemyLogic.enemies) {
			if(enemy.shouldBeRemoved) {
				enemies.remove(enemy);
			}
		}
		houston.enemyLogic.enemies = enemies;

		checkIfIsStillAlive();
		contrallCharacterMovement(player);
		playerEnemyCollisionDetection();
		detectSpecialTiles();
		for (Enemy enemy : houston.enemyLogic.enemies) {
			contrallCharacterMovement(enemy);
		}
		for (Magic magic : houston.gamePanel.magics) {
			contrallCharacterMovement(magic);
		}
	}

	// Regelt den Wechsel zur naechsten Karte
	private void nextMap() {
		// Haelt den Spieler an
		player.stop();

		if (map.getMapNumber() < (map.getCountOfMapsByLevel() - 1)) {
			// Naechste Karte
			jumpToLevel(map.getLevelNumber(), (map.getMapNumber() + 1));
		} else {
			if (map.getLevelNumber() < (map.getCountOfLevel() - 1)) {
				// Naechstes Level
				jumpToLevel((map.getLevelNumber() + 1), 0);
			} else {
				// Spiel gewonnen
				houston.changeAppearance(true, false, "STARTMENU");
			}
		}
	}

	// Setzt die Tastendruecke um in die Bewegung des Movable
	private void contrallCharacterMovement(Movable character) {
		dX = dY = 0;
		getCharacterCorners(character);
		
		// Bewegung nach Links
		if (character.left && !character.right) {
			dX = -character.speed * (delta / 1e9);
			if (isValidXMovement(topLeft, bottomLeft, dX) == 1) {
				dX = 0;
				character.wallHit();
			}

		// Bewegung nach Rechts
		} else if (character.right && !character.left) {
			dX = character.speed * (delta / 1e9);
			if (isValidXMovement(topRight, bottomRight, dX) == 1) {
				dX = 0;
				character.wallHit();
			}
		}

		// Bewegung nach Oben
		if (character.up && !character.down) {
			dY = -character.speed * (delta / 1e9);
			if (isValidYMovement(topLeft, topRight, dY) == 1) {
				dY = 0;
				character.wallHit();
			}

		// Bewegung nach Unten
		} else if (character.down && !character.up) {
			dY = character.speed * (delta / 1e9);
			if (isValidYMovement(bottomLeft, bottomRight, dY) == 1) {
				dY = 0;
				character.wallHit();
			}
		}

		// Bewegt den Spieler falls notwendig
		if (dX != 0 || dY != 0)
			character.move(dX, dY);
	}
	
	private void playerEnemyCollisionDetection () {
		for (Enemy enemy : houston.enemyLogic.enemies) {
			if(player.bounds.intersects(enemy.bounds)) {
				player.decreaseHealth(2);
			}
		}
	}

	// Ermittelt, ob sich der Player auf einer Speziellen Kachel befindet, und leitet entsprechende Massnahmen ein
	private void detectSpecialTiles() {
		if (player.left || player.right || player.up || player.down) {
			value = (map.mapArray[(int) Math.floor(player.getY() + (player.getHeight()/2))/32][(int) Math.floor((player.getX() + (player.getWidth()/2))/32)]);
			
			// entsprechende Massnahmen
			if (value == 7) { 
				backToLastCheckpoint();
				player.decreaseHealth(25);
			} else if (value == 9){
				nextMap();
			}
		}
	}
	
	// Ermittelt die Koordinaten der 4 Eckpunkte des Player
	private void getCharacterCorners(Movable character) {
		double tlX = character.getX();
		double tlY = character.getY();
		topLeft.setLocation(tlX, tlY);
		topRight.setLocation(tlX + character.getWidth(), tlY);
		bottomLeft.setLocation(tlX, tlY + character.getHeight());
		bottomRight.setLocation(tlX + character.getWidth(), tlY + character.getHeight());
	}

	// Ermittelt, ob die, durch horizontale Bewegung dX, errechnete neue Position des Spielers in einer Wand liegt oder nicht
	private int isValidXMovement(Point2D pointTop, Point2D pointBottom, double dX) {
		if ((map.mapArray[(int) Math.floor(pointTop.getY()/32)][(int) Math.floor((pointTop.getX()+dX)/32)] == 1)) return 1;
		else if ((map.mapArray[(int) Math.floor(pointBottom.getY()/32)][(int) Math.floor((pointBottom.getX()+dX)/32)] == 1)) return 1;
		return 0;
	}

	// Ermittelt, ob die, durch vertikale Bewegung dY, errechnete neue Position des Spielers in einer Wand liegt oder nicht
	private int isValidYMovement(Point2D pointLeft, Point2D pointRight, double dY2) {
		if ((map.mapArray[(int) Math.floor(pointLeft.getY()+dY)/32][(int) Math.floor(pointLeft.getX()/32)] == 1)) return 1;
		else if ((map.mapArray[(int) Math.floor(pointRight.getY()+dY)/32][(int) Math.floor(pointRight.getX()/32)] == 1)) return 1;
		return 0;
	}

	private void backToLastCheckpoint() {
		// Zurueck zur ersten Karte des aktuellen Level
		jumpToLevel(map.getLevelNumber(), 0);
	}

	private void checkIfIsStillAlive() {
		if (player.getHealth() <= 0) {
			houston.changeAppearance(true, false, "STARTMENU");
		}
	}

}
