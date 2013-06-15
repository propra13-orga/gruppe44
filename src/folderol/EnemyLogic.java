package folderol;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class EnemyLogic {

	Houston houston;
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	
	public EnemyLogic(Houston houston){
		this.houston = houston;
	}
	
	public void setSpawnPosition() {
		
			for (Point2D position : houston.map.multiSearch(6)){
				enemies.add(new Enemy(position, 3));
			}
			
			for (Point2D position : houston.map.multiSearch(4)){
				enemies.add(new Enemy(position, 0));
			}
	}
	
}
