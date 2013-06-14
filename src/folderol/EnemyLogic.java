package folderol;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class EnemyLogic {

	Houston houston;
	Enemy enemy;
	Point2D resetPoint;
	Rectangle2D bounds;
	ArrayList<Point2D> enemyPositionsRight;
	ArrayList<Point2D> enemyPositionsUp;
	ArrayList<Enemy> allEnemysRight = new ArrayList<Enemy>( );
	ArrayList<Enemy> allEnemysUp = new ArrayList<Enemy>( );
	
	public EnemyLogic(Houston houston){
		this.houston = houston;
	}
	
	public void setSpawnPosition() {
		
			enemyPositionsRight = houston.map.multiSearch(6);
			
			for (int i=0; i<enemyPositionsRight.size(); i++){
				resetPoint = enemyPositionsRight.get(i);
				enemy = new Enemy(resetPoint, 3);
				enemy.left = true;
				allEnemysRight.add(new Enemy(resetPoint, 3));
			}
			
			enemyPositionsUp = houston.map.multiSearch(4);
			
			for (int i=0; i<enemyPositionsUp.size(); i++){
				resetPoint = enemyPositionsUp.get(i);
				enemy = new Enemy(resetPoint, 0);
				enemy.up = true;
				allEnemysUp.add(new Enemy(resetPoint, 0));
			}
	}
	
}
