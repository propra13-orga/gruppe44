package folderol;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MagicLogic {

	private Houston houston;
	private Player player;
	
	public ArrayList<Magic> magics;
	private Magic magic;
	private int manaCost = 5;
	private Timer timer;
	private TimerTask enemyMagic;
	
	public MagicLogic(Houston houston) {
		this.houston	= houston;
		this.player		= houston.player;
		magics = new ArrayList<Magic>();
		
		setMagic();
	}

	public void setMagic(){
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

	public void doGameUpdates() {
		for (int i = magics.size() - 1; i >= 0; i--) {
			magic = magics.get(i);

			// Filtert die zu löschenden Magics raus
			if (magic.remove) {
				magics.remove(i);
			}
			houston.gameLogic.controlCharacterMovement(magic);
		}
	}

	public void onLevelChange() {
		magics.clear();
	}

}
