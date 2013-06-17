package folderol;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MagicLogic {

	private Houston houston;
	private Player player;
	
	public ArrayList<Magic> magics;
	private int manaCost = 5;
	private Timer timer;
	private TimerTask enemyMagic;
	
	public MagicLogic(final Houston houston) {
		this.houston = houston;
		this.player = houston.player;
		magics = new ArrayList<Magic>();
		
timer = new Timer();
		
		enemyMagic = new TimerTask(){
			public void run (){
				for (Enemy enemy : houston.enemyLogic.enemies) {
					if(enemy.shoot == 1){
					magics.add(new Magic(houston, enemy.getCenterPosition(), player.getCenterPosition(), false));
					}
				}
			}	
		};
		
		timer.schedule(enemyMagic, 100, 2000);
	}

	public void doMagic(Point2D mouseClickPosition) {
		if (houston.player.getMana() >= manaCost) {
			player.decreaseMana(manaCost);
			magics.add(new Magic(houston, player.getCenterPosition(), mouseClickPosition, true));
		}
	}
	
}
