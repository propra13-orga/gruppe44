package folderol;

import java.awt.geom.Point2D;

public class PlayerLogic {

	private Houston houston;
	private Player player;

	public PlayerLogic(Houston houston) {
		this.houston = houston;
		this.player = houston.player;
	}

	public void onLevelChange() {
		// Sucht und setzt die Ursprungsposition des Player
		Point2D spawn = new Point2D.Double();
		if ((spawn = houston.map.singleSearch(8)) != null)
			houston.player.setResetPosition(spawn);

		// Setzt den Player auf seine Ursprungsposition
		houston.player.resetPosition();
	}

	public void doGameUpdates() {
		checkIfPlayerIsStillAlive();
		houston.gameLogic.detectSpecialTiles();
		houston.gameLogic.controlCharacterMovement(player);
	}

	private void checkIfPlayerIsStillAlive() {
		if (player.getHealth() <= 0) {
			player.lives--;
			player.increaseHealth(100);
		}
		if (player.lives == 0){
			houston.changeAppearance(true, false, "STARTMENU");
		}
	}

	public void attack() {
		for (Enemy enemy : houston.enemyLogic.enemies) {
			if (player.attackBox.intersects(enemy.bounds)) {
				enemy.decreaseHealth(5);
				if(enemy.health <= 0){
					if(houston.enemyLogic.bossIsAlive)
						houston.enemyLogic.bossIsAlive = false;
					enemy.remove= true;
					player.increaseMoney(10);
				}
			}
		}
	}

}
