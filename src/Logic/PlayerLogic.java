package Logic;

import java.awt.geom.Point2D;

import Main.Enemy;
import Main.Houston;
import Main.Player;

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
		if ((spawn = houston.map.singleSearch(houston.map.mapArray, 8)) != null)
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
			player.setLives(player.getLives() - 1);
			player.increaseHealth(100);
		}
		if (player.getLives() == 0){
			houston.changeAppearance(true, false, "STARTMENU");
		}
	}

	public void attack() {
		for (Enemy enemy : houston.enemyLogic.enemies) {
			if (player.attackBox.intersects(enemy.getBounds())) {
				enemy.decreaseHealth(5);
				if(enemy.getHealth() <= 0){
					if(houston.enemyLogic.bossIsAlive)
						houston.enemyLogic.bossIsAlive = false;
					enemy.remove= true;
					player.increaseMoney(10);
				}
			}
		}
	}

	public void changeMagicType() {
		if (player.magicType == MagicLogic.ANA) {
			player.magicType = MagicLogic.LA;
		}
		else if (player.magicType == MagicLogic.LA) {
			player.magicType = MagicLogic.INFO;
		}else{
			player.magicType = MagicLogic.ANA;
		}
	}
}
