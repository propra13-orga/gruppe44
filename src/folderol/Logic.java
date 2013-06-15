package folderol;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Logic {
	
	Houston houston;
	Player player;
	Map map;
	Story story;

	private long delta;
	int value;

	public Logic(Houston houston) {
		this.houston	= houston;
		this.player		= houston.player;
		this.map		= houston.map;
		this.story		= houston.story;
	}

	/**
	 * Startet ein neues Spiel.
	 */
	public void setupNewGame(int levelNumber, int mapNumber) {
		player.resetHealthManaMoney(100, 100, 200);
		houston.inventory.clear();
		
		jumpToLevel(levelNumber, mapNumber);
	}
	
	/**
	 * Springt zum angegebenen Level.
	 */
	public void jumpToLevel(int levelNumber, int mapNumber) {
		map.renewMap(levelNumber, mapNumber);
//		story.renewStory(levelNumber, mapNumber);

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
		move(player);
		playerEnemyCollisionDetection();
		detectSpecialTiles();
		for (Enemy enemy : houston.enemyLogic.enemies) {
			move(enemy);
		}
		for (Magic magic : houston.gamePanel.magics) {
			move(magic);
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
	private void move(Movable movable) {
		
		// Die Entfernungen in x und y, die das Movable für die Bewegung zurücklegen würde
		double dX = 0;
		double dY = 0;
		
		// Bewegung nach Links
		if (movable.left && !movable.right) {
			dX = -movable.speed * (delta / 1e9);
			
		// Bewegung nach Rechts
		} else if (movable.right && !movable.left) {
			dX = movable.speed * (delta / 1e9);
		}
		
		// Bewegung nach Oben
		if (movable.up && !movable.down) {
			dY = -movable.speed * (delta / 1e9);
			
		// Bewegung nach Unten
		} else if (movable.down && !movable.up) {
			dY = movable.speed * (delta / 1e9);
		}
		
		// Die 4 Eckpunkte, die das Movable nach der Bewegung hätte
		double topLeftX = movable.getX() + dX;
		double topLeftY = movable.getY() + dY;
		Point2D topLeft = new Point2D.Double(topLeftX, topLeftY);
		Point2D topRight = new Point2D.Double(topLeftX + movable.getWidth(), topLeftY);
		Point2D bottomLeft = new Point2D.Double(topLeftX, topLeftY + movable.getHeight());
		Point2D bottomRight = new Point2D.Double(topLeftX + movable.getWidth(), topLeftY + movable.getHeight());
		
		boolean movableHasHitWall = (map.isWall(Map.screenPositionToMapPosition(topLeft)) ||
				map.isWall(Map.screenPositionToMapPosition(topRight)) ||
				map.isWall(Map.screenPositionToMapPosition(bottomLeft)) ||
				map.isWall(Map.screenPositionToMapPosition(bottomRight)));
		
		if (movableHasHitWall) {
			movable.onHitWall();
			return;
		}
		
		// Bewegt den Spieler falls notwendig
		if (dX != 0 || dY != 0) {
			movable.move(dX, dY);
			movable.onMoved();
		}
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
