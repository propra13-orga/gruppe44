package Logic;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import Main.Enemy;
import Main.Houston;
import Main.Magic;
import Main.Player;

public class MagicLogic {

	private Houston houston;
	private Player player;
	private EnemyLogic enemyLogic;
	
	public ArrayList<Magic> magics;
	private Magic magic;
	private Enemy enemy;
	private int manaCost = 5;
	private BufferedImage texture;
	private Timer timer;
	private TimerTask enemyMagic;
	
	public MagicLogic(Houston houston) {
		this.houston	= houston;
		this.player		= houston.player;
		this.enemyLogic = houston.enemyLogic;
		magics = new ArrayList<Magic>();
		
		try {
			texture = ImageIO.read(new File("./res/img/tiles/ungleich.png"));
		} catch (IOException e){ e.printStackTrace(); }
		
		setMagic();
	}

	public void setMagic(){
	timer = new Timer();
		
		enemyMagic = new TimerTask(){
			public void run (){
				for (Enemy enemy : houston.enemyLogic.enemies) {
					if(enemy.shoot == 1){
					magics.add(new Magic(texture, enemy.getCenterPosition(), player.getCenterPosition(), false));
					}
				}
			}	
		};
		
		timer.schedule(enemyMagic, 100, 2000);
	}
	public void doMagic(Point2D mouseClickPosition) {
		if (houston.player.getMana() >= manaCost) {
			player.decreaseMana(manaCost);
			magics.add(new Magic(texture, player.getCenterPosition(), mouseClickPosition, true));
		}
	}

	public void doGameUpdates() {
		for (int i = magics.size() - 1; i >= 0; i--) {
			magic = magics.get(i);
			
			for (int j = enemyLogic.enemies.size() - 1; j >= 0; j--) {
				enemy = enemyLogic.enemies.get(j);
				
				if((magic.getBounds().intersects(enemy.getBounds())) && (magic.isMagicFromPlayer())) {
					enemy.decreaseHealth(10);
					if(enemy.getHealth() <= 0){
						if(houston.enemyLogic.bossIsAlive)
							houston.enemyLogic.bossIsAlive = false;
						enemy.remove= true;
					}
					magic.remove = true;
					houston.player.increaseMoney(10);
				}
			}
			// Kollisionserkennung mit Magic, die von Gegnern kommt
			if ((magic.getBounds()).intersects(player.getBounds()) && (magic.isMagicFromPlayer() == false)) {
				player.decreaseHealth(10);
				magic.remove = true;
			}

			// Filtert die zu löschenden Magics raus
			if (magic.remove) {
				magics.remove(i);
				continue;
			}
			
			houston.gameLogic.controlCharacterMovement(magic);
		}
	}

	public void onLevelChange() {
		magics.clear();
	}

}
