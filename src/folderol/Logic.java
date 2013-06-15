package folderol;

import java.awt.geom.Point2D;

public class Logic {
	
	Houston houston;
	Player player;
	Map map;
	Story story;

	Point2D topLeft, topRight, bottomLeft, bottomRight;
	private long delta;
	private double dX, dY;
	int value;

	public Logic(Houston houston) {
		this.houston	= houston;
		this.player		= houston.player;
		this.map		= houston.map;
		this.story		= houston.story;
		
		// Die 4 Punkte stehen fuer die 4 Ecken des Player
		topLeft			= new Point2D.Double();
		topRight		= new Point2D.Double();
		bottomLeft		= new Point2D.Double();
		bottomRight		= new Point2D.Double();
	}

	void setupNewGame(int levelNumber, int mapNumber) {
		if (mapNumber == 0 && levelNumber == 0) {
			player.resetHealthManaMoney(100, 100, 200);
			houston.inventory.clear();
		}
		map.renewMap(levelNumber, mapNumber);
//		story.renewStory(levelNumber, mapNumber);

		// Sucht und setzt die Ursprungsposition des Player
		Point2D spawn = new Point2D.Double();
		if ((spawn = map.singleSearch(8)) != null)
			player.setResetPosition(spawn);

		// Setzt den Player auf seine Ursprungsposition
		player.resetPosition();
		houston.enemyLogic.allEnemysRight.clear();
		houston.enemyLogic.allEnemysUp.clear();
		houston.enemyLogic.setSpawnPosition();
	}

	// Berechnet z.B. alle Bewegungen und Kollisionen im Spiel
	void doGameUpdates(long delta) {
		this.delta = delta;

		checkIfIsStillAlive();
		controlCharacterMovement(player);
		playerEnemyCollisionDetection();
		detectSpecialTiles();
		for (int i=0; i<houston.enemyLogic.allEnemysRight.size(); i++){
			houston.enemyLogic.enemy = houston.enemyLogic.allEnemysRight.get(i);
			controlEnemyMovement(houston.enemyLogic.enemy);
		}
		for (int i=0; i<houston.enemyLogic.allEnemysUp.size(); i++){
			houston.enemyLogic.enemy = houston.enemyLogic.allEnemysUp.get(i);
			controlEnemyMovement(houston.enemyLogic.enemy);
		}
	}

	// Regelt den Wechsel zur naechsten Karte
	private void nextMap() {
		// Haelt den Spieler an
		player.stop();

		if (map.getMapNumber() < (map.getCountOfMapsByLevel() - 1)) {
			// Naechste Karte
			setupNewGame(map.getLevelNumber(), (map.getMapNumber() + 1));
		} else {
			if (map.getLevelNumber() < (map.getCountOfLevel() - 1)) {
				// Naechstes Level
				setupNewGame((map.getLevelNumber() + 1), 0);
			} else {
				// Spiel gewonnen
				houston.changeAppearance(true, false, "STARTMENU");
			}
		}
	}

	// Setzt die Tastendruecke um in die Bewegung des Player
	private void controlCharacterMovement(Movable character) {
		dX = dY = 0;
		getCharacterCorners(character);
		
		// Bewegung nach Links
		if (character.left && !character.right) {
			dX = -character.speed * (delta / 1e9);
			if (isValidXMovement(topLeft, bottomLeft, dX) == 1) {
				dX = 0;
			}
			
		// Bewegung nach Rechts
		} else if (character.right && !character.left) {
			dX = character.speed * (delta / 1e9);
			if (isValidXMovement(topRight, bottomRight, dX) == 1) {
				dX = 0;
			}
		}
		
		// Bewegung nach Oben
		if (character.up && !character.down) {
			dY = -character.speed * (delta / 1e9);
			if (isValidYMovement(topLeft, topRight, dY) == 1) {
				dY = 0;
			}
			
		// Bewegung nach Unten
		} else if (character.down && !character.up) {
			dY = character.speed * (delta / 1e9);
			if (isValidYMovement(bottomLeft, bottomRight, dY) == 1) {
				dY = 0;
			}
		}
		
		// Bewegt den Spieler falls notwendig
		if (dX != 0 || dY != 0)
			character.move(dX, dY);
	}
	
	private void controlEnemyMovement(Movable character) {
		dX = dY = 0;
		getCharacterCorners(character);

		
		// Bewegung nach Links
		if (character.left && !character.right) {
			dX = -character.speed * (delta / 1e9);
			if (isValidXMovement(topLeft, bottomLeft, dX) == 1) {
				character.left = false;
				character.right = true;
				dX = 0;
			}
			
		// Bewegung nach Rechts
		} else if (character.right && !character.left) {
			dX = character.speed * (delta / 1e9);
			if (isValidXMovement(topRight, bottomRight, dX) == 1) {
				character.left = true;
				character.right = false;
				dX = 0;
			}
		}
		
		// Bewegung nach Oben
		if (character.up && !character.down) {
			dY = -character.speed * (delta / 1e9);
			if (isValidYMovement(topLeft, topRight, dY) == 1) {
				dY = 0;
				character.down = true;
				character.up = false;
			}
			
		// Bewegung nach Unten
		} else if (character.down && !character.up) {
			dY = character.speed * (delta / 1e9);
			if (isValidYMovement(bottomLeft, bottomRight, dY) == 1) {
				dY = 0;
				character.down = false;
				character.up = true;
			}
		}
		
		// Bewegt den Spieler falls notwendig
		if (dX != 0 || dY != 0)
			character.move(dX, dY);
	}
	
	private void playerEnemyCollisionDetection (){
		if(!houston.enemyLogic.allEnemysRight.isEmpty() || !houston.enemyLogic.allEnemysUp.isEmpty()){
			for (int i=0; i<houston.enemyLogic.allEnemysRight.size(); i++){
				if(player.bounds.intersects(houston.enemyLogic.allEnemysRight.get(i).bounds)){
					player.health = player.health -2;
				}
			}
			for (int i=0; i<houston.enemyLogic.allEnemysUp.size(); i++){
				if(player.bounds.intersects( houston.enemyLogic.allEnemysUp.get(i).bounds)){
					player.health = player.health -2;
				}
			}
		}
	}

	// Ermittelt die Koordinaten der 4 Eckpunkte des Player
	private void getCharacterCorners(Movable character) {
		topLeft.setLocation(character.getX(), character.getY());
		topRight.setLocation(character.getX() + character.getWidth(), character.getY());
		bottomLeft.setLocation(character.getX(), character.getY() + character.getHeight());
		bottomRight.setLocation(character.getX() + character.getWidth(), character.getY() + character.getHeight());
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
		setupNewGame(map.getLevelNumber(), 0);
	}

	private void checkIfIsStillAlive() {
		if (player.getHealth() <= 0) {
			houston.changeAppearance(true, false, "STARTMENU");
		}
	}

}
