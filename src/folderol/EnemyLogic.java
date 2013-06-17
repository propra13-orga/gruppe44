package folderol;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class EnemyLogic {

	private Houston houston;
	public ArrayList<Enemy> enemies;
	private Enemy enemy;

	public EnemyLogic(Houston houston) {
		this.houston = houston;
		enemies = new ArrayList<Enemy>();
	}

	public void onLevelChange() {
		enemies.clear();

		for (Point2D singleEnemyPosition : houston.map.multiSearch(6)) {
			enemies.add(new Enemy(singleEnemyPosition, 3));
		}
		for (Point2D singleEnemyPosition : houston.map.multiSearch(4)) {
			enemies.add(new Enemy(singleEnemyPosition, 0));
		}
	}

	public void doGameUpdates() {
		for (int i = enemies.size() - 1; i >= 0; i--) {
			enemy = enemies.get(i);

			// Filtert die zu löschenden Enemies raus
			if (enemy.remove) {
				enemies.remove(i);
			}
			houston.logic.controlCharacterMovement(enemy);
			playerEnemyCollisionDetection(enemy);
		}
	}

	private void playerEnemyCollisionDetection(Enemy enemy) {
		if (houston.player.bounds.intersects(enemy.bounds)) {
			houston.player.decreaseHealth(2);
		}
	}

}
