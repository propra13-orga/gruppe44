package folderol;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Logic {
	
	private Houston houston;
	private Player player;
	private Map map;
	private EnemyLogic enemyLogic;
	private MagicLogic magicLogic;
	private Story story;

	private Point2D topLeft, topRight, bottomLeft, bottomRight;
	double tlX, tlY; // Temporäre Character Ecke
	private long delta;
	private double dX, dY;
	public int value;
	public int npcv;

	public Logic(Houston houston) {
		this.houston	= houston;
		this.player		= houston.player;
		this.map		= houston.map;
		this.story		= houston.story;
		this.enemyLogic = houston.enemyLogic;
		this.magicLogic = houston.magicLogic;
		
		// 4 Punkte fuer die 4 Ecken des Character
		topLeft			= new Point2D.Double();
		topRight		= new Point2D.Double();
		bottomLeft		= new Point2D.Double();
		bottomRight		= new Point2D.Double();
	}

	// Startet ein neues Spiel
	public void setupNewGame(int levelNumber, int mapNumber) {
		player.resetHealthManaMoney(100, 100, 200);
		houston.inventory.clear();
		
		changeLevel(levelNumber, mapNumber);
	}
	
	// Springt zum angegebenen Level.
	private void changeLevel(int levelNumber, int mapNumber) {
		map.renewMap(levelNumber, mapNumber);
		// story.renewStory(levelNumber, mapNumber);

		// Sucht und setzt die Ursprungsposition des Player
		Point2D spawn = new Point2D.Double();
		if ((spawn = map.singleSearch(8)) != null)
			player.setResetPosition(spawn);

		// Setzt den Player auf seine Ursprungsposition
		player.resetPosition();
		enemyLogic.removeEnemies();
		enemyLogic.setSpawnPositions();
		
	}

	// Berechnet z.B. alle Bewegungen und Kollisionen im Spiel
	public void doGameUpdates(long delta) {
		this.delta = delta;
		
		ArrayList<Magic> magics = new ArrayList<Magic>(magicLogic.magics);
		for (Magic magic : houston.magicLogic.magics) {
			if(magic.shouldBeRemoved) {
				magics.remove(magic);
			}
		}
		magicLogic.magics = magics;
		
		 ArrayList<Enemy> enemies = new ArrayList<Enemy>(enemyLogic.enemies);
		 for (Enemy enemy : enemyLogic.enemies) {
			 if(enemy.shouldBeRemoved) {
				 enemies.remove(enemy);
			 }
		 }
		 enemyLogic.enemies = enemies;

		checkIfIsStillAlive();
		contrallCharacterMovement(player);
		playerEnemyCollisionDetection();
		detectSpecialTiles();
		for (Enemy enemy : enemyLogic.enemies) {
			contrallCharacterMovement(enemy);
		}
		for (Magic magic : magicLogic.magics) {
			contrallCharacterMovement(magic);
		}
	}

	// Regelt den Wechsel zur naechsten Karte
	private void nextMap() {
		player.stop();

		if (map.getMapNumber() < (map.getCountOfMapsByLevel() - 1)) {
			// Naechste Karte
			changeLevel(map.getLevelNumber(), (map.getMapNumber() + 1));
		} else {
			if (map.getLevelNumber() < (map.getCountOfLevel() - 1)) {
				// Naechstes Level
				changeLevel((map.getLevelNumber() + 1), 0);
			} else {
				// Spiel gewonnen
				houston.changeAppearance(true, false, "STARTMENU");
			}
		}
	}

	// Setzt die Tastendruecke um in die Bewegung des Charakter
	private void contrallCharacterMovement(Movable character) {
		dX = dY = 0;
		getCharacterCorners(character);
		
		// Bewegung nach Links
		if (character.left && !character.right) {
			dX = -character.speed * (delta / 1e9);
			if (isValidXMovement(topLeft, bottomLeft, dX, 1) == 1) {
				dX = 0;
				character.onWallHit();
			}
			else if (isValidXMovement(topLeft, bottomLeft, dX, 3) == 3){
					dX = 0;
					System.out.println("Da ist ein NPC");
					npcv = 1;
				
			}

		// Bewegung nach Rechts
		} else if (character.right && !character.left) {
			dX = character.speed * (delta / 1e9);
			if (isValidXMovement(topRight, bottomRight, dX, 1) == 1) {
				dX = 0;
				character.onWallHit();
			}
			else if (isValidXMovement(topLeft, bottomLeft, dX, 3) == 3){
				dX = 0;
				System.out.println("Da ist ein NPC");
				npcv = 1;
			
		}
		}

		// Bewegung nach Oben
		if (character.up && !character.down) {
			dY = -character.speed * (delta / 1e9);
			if (isValidYMovement(topLeft, topRight, dY, 1) == 1) {
				dY = 0;
				character.onWallHit();
			}
			else if (isValidYMovement(topLeft, bottomLeft, dX, 3) == 3){
				dY = 0;
				System.out.println("Da ist ein NPC");
				npcv = 1;
			
		}

		// Bewegung nach Unten
		} else if (character.down && !character.up) {
			dY = character.speed * (delta / 1e9);
			if (isValidYMovement(bottomLeft, bottomRight, dY, 1) == 1) {
				dY = 0;
				character.onWallHit();
			}
			else if (isValidYMovement(topLeft, bottomLeft, dX, 3) == 3){
				dY = 0;
				System.out.println("Da ist ein NPC");
				npcv = 1;
			
		}
		}

		// Bewegt den Charakter falls notwendig
		if (dX != 0 || dY != 0)
			character.move(dX, dY);
	}
	
	private void playerEnemyCollisionDetection () {
		for (Enemy enemy : enemyLogic.enemies) {
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
				player.decreaseHealth(25);
				backToLastCheckpoint();
			} else if (value == 9){
				nextMap();
			}
		}
	}
	
	// Ermittelt die Koordinaten der 4 Eckpunkte des Charakter
	private void getCharacterCorners(Movable character) {
		double tlX = character.getX();
		double tlY = character.getY();
		topLeft.setLocation(tlX, tlY);
		topRight.setLocation(tlX + character.getWidth(), tlY);
		bottomLeft.setLocation(tlX, tlY + character.getHeight());
		bottomRight.setLocation(tlX + character.getWidth(), tlY + character.getHeight());
	}

	// Ermittelt, ob die, durch horizontale Bewegung dX, errechnete neue Position (des Spielers) in einer Wand liegt oder nicht
	private int isValidXMovement(Point2D pointTop, Point2D pointBottom, double dX, int checkValue) {
		if ((map.mapArray[(int) Math.floor(pointTop.getY()/32)][(int) Math.floor((pointTop.getX()+dX)/32)] == checkValue)) return checkValue;
		else if ((map.mapArray[(int) Math.floor(pointBottom.getY()/32)][(int) Math.floor((pointBottom.getX()+dX)/32)] == checkValue)) return checkValue;
		return 0;
	}

	// Ermittelt, ob die, durch vertikale Bewegung dY, errechnete neue Position (des Spielers) in einer Wand liegt oder nicht
	private int isValidYMovement(Point2D pointLeft, Point2D pointRight, double dY2, int checkValue) {
		if ((map.mapArray[(int) Math.floor(pointLeft.getY()+dY)/32][(int) Math.floor(pointLeft.getX()/32)] == checkValue)) return checkValue;
		else if ((map.mapArray[(int) Math.floor(pointRight.getY()+dY)/32][(int) Math.floor(pointRight.getX()/32)] == checkValue)) return checkValue;
		return 0;
	}

	private void backToLastCheckpoint() {
		// Zurueck zur ersten Karte des aktuellen Level
		changeLevel(map.getLevelNumber(), 0);
	}

	private void checkIfIsStillAlive() {
		if (player.getHealth() <= 0) {
			houston.changeAppearance(true, false, "STARTMENU");
		}
	}

	public void attack () {
		for (Enemy enemy : houston.enemyLogic.enemies) {
			if(player.attackBox.intersects(enemy.bounds)) {
				enemy.decreaseHealth(100);
			}
		}
	}
}
