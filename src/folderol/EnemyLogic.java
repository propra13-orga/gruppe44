package folderol;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class EnemyLogic {

	private Houston houston;
	public ArrayList<Enemy> enemies;

	public EnemyLogic(Houston houston) {
		this.houston = houston;
		enemies = new ArrayList<Enemy>();
	}

	public void setSpawnPositions() {
//Vertikale Gegner, die nicht schieﬂen
		for (Point2D singleEnemyPosition : houston.map.multiSearch(30)) {
			if(houston.map.getLevelNumber()==0)
				enemies.add(new Enemy(singleEnemyPosition, 0, 0));
			if(houston.map.getLevelNumber()==1)
				enemies.add(new Enemy(singleEnemyPosition, 0, 3));
			if(houston.map.getLevelNumber()==2)
				enemies.add(new Enemy(singleEnemyPosition, 0, 6));
		}
//Vertikale Gegner, die schieﬂen
		for (Point2D singleEnemyPosition : houston.map.multiSearch(31)) {
			if(houston.map.getLevelNumber()==0)
				enemies.add(new Enemy(singleEnemyPosition, 0, 1));
			if(houston.map.getLevelNumber()==1)
				enemies.add(new Enemy(singleEnemyPosition, 0, 4));
			if(houston.map.getLevelNumber()==2)
				enemies.add(new Enemy(singleEnemyPosition, 0, 7));
		}
//Vertikale Bossgegner
		for (Point2D singleEnemyPosition : houston.map.multiSearch(32)) {
			if(houston.map.getLevelNumber()==0)
				enemies.add(new Enemy(singleEnemyPosition, 0, 2));
			if(houston.map.getLevelNumber()==1)
				enemies.add(new Enemy(singleEnemyPosition, 0, 5));
			if(houston.map.getLevelNumber()==2)
				enemies.add(new Enemy(singleEnemyPosition, 0, 8));
		}
//Horizontale Gegner, die nicht schieﬂen
		for (Point2D singleEnemyPosition : houston.map.multiSearch(33)) {
			if(houston.map.getLevelNumber()==0)
				enemies.add(new Enemy(singleEnemyPosition, 3, 0));
			if(houston.map.getLevelNumber()==1)
				enemies.add(new Enemy(singleEnemyPosition, 3, 3));
			if(houston.map.getLevelNumber()==2)
				enemies.add(new Enemy(singleEnemyPosition, 3, 6));
		}
//Horizontale Gegner, die schieﬂen
		for (Point2D singleEnemyPosition : houston.map.multiSearch(34)) {
			if(houston.map.getLevelNumber()==0)
				enemies.add(new Enemy(singleEnemyPosition, 3, 1));
			if(houston.map.getLevelNumber()==1)
				enemies.add(new Enemy(singleEnemyPosition, 3, 4));
			if(houston.map.getLevelNumber()==2)
				enemies.add(new Enemy(singleEnemyPosition, 3, 7));
		}
//Horizontale Bossgegner
		for (Point2D singleEnemyPosition : houston.map.multiSearch(35)) {
			if(houston.map.getLevelNumber()==0)
				enemies.add(new Enemy(singleEnemyPosition, 3, 2));
			if(houston.map.getLevelNumber()==1)
				enemies.add(new Enemy(singleEnemyPosition, 3, 5));
			if(houston.map.getLevelNumber()==2)
				enemies.add(new Enemy(singleEnemyPosition, 3, 8));
		}
	}
	
	public void removeAll() {
		enemies.clear();
	}
	
	public void removeEnemy(Enemy enemy) {
		enemies.remove(enemy);
	}

}
