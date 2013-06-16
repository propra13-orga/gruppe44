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
		for (Point2D singleEnemyPosition : houston.map.multiSearch(6)) {
			enemies.add(new Enemy(singleEnemyPosition, 3));
		}
		for (Point2D singleEnemyPosition : houston.map.multiSearch(4)) {
			enemies.add(new Enemy(singleEnemyPosition, 0));
		}
	}
	
	public void removeEnemies() {
		enemies.clear();
	}
	
	public void removeEnemy(Enemy enemy) {
		enemies.remove(enemy);
	}

}
