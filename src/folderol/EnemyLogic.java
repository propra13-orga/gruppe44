package folderol;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class EnemyLogic {

	Houston houston;
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();

	public EnemyLogic(Houston houston) {
		this.houston = houston;
	}

	public void setSpawnPosition() {
		for (Point2D singleEnemyPosition : houston.map.multiSearch(6)) {
			enemies.add(new Enemy(singleEnemyPosition, 3));
		}
		for (Point2D singleEnemyPosition : houston.map.multiSearch(4)) {
			enemies.add(new Enemy(singleEnemyPosition, 0));
		}
	}

}
