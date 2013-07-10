package Logic;


import java.awt.geom.Point2D;

import Main.Enemy;
import Main.Houston;
import Main.Player;
import Main.Sounds;

/** enthaelt die Logik vom Spieler */
public class PlayerLogic {

	private final Houston houston;
	private final Player player;

	/**
	 * initialisiert den Spieler
	 * @param houston
	 */
	public PlayerLogic(Houston houston) {
		this.houston = houston;
		this.player = houston.player;
	}

	/** Sucht und setzt die Ursprungsposition des Player */
	public void onLevelChange() {
		// Sucht und setzt die Ursprungsposition des Player
		Point2D spawn = new Point2D.Double();
		if ((spawn = houston.map.singleSearch(houston.map.mapArray, 8)) != null)
			houston.player.setResetPosition(spawn);

		// Setzt den Player auf seine Ursprungsposition
		houston.player.resetPosition();
	}

	/**
	 * prueft, ob der Spieler noch lebt;
	 * legt die Textur fest;
	 * kontrolliert die Bewegung
	 */
	public void doGameUpdates() {
		checkIfPlayerIsStillAlive();
		setTexture();
		houston.gameLogic.detectSpecialTiles();
		houston.gameLogic.controlCharacterMovement(player);
	}

	private void checkIfPlayerIsStillAlive() {
		if (player.getHealth() <= 0) {
			player.setLives(player.getLives() - 1);
			player.increaseHealth(100);
		}
		if (player.getLives() == 0){
			houston.changeAppearance(true, false, Houston.STARTMENU);
		}
	}

	/** Spieler greift mit Leertaste an */
	public void attack() {
		houston.sounds.playSound(Sounds.Type.ATTACK);
		for (Enemy enemy : houston.enemyLogic.enemies) {
			if (player.attackBox.intersects(enemy.getBounds())) {
				enemy.decreaseHealth(getAttackDamage(player.getplayerLevel()));
				if(enemy.getHealth() <= 0){
					boolean isBoss = false;
					if(houston.enemyLogic.bossIsAlive){
						isBoss = true;
					}
					enemy.remove= true;
					player.increaseMoney(10);
					player.increaseExperience(enemy.getExperience(houston.map.getLevelNumber()+1, isBoss));
					checkExperience();
				}
			}
		}
	}

	/**
	 * errechnet wieviel Schaden eine Attacke bringt, abhaengig vom Erfahrungslevel
	 * @param playerLevel
	 * @return damage
	 */
	public int getAttackDamage(int playerLevel){
		if( playerLevel == 1){
			return 10;
		}if( playerLevel == 2){
			return 10;
		}if( playerLevel == 3){
			return 13;
		}if( playerLevel == 4){
			return 16;
		}else {
			return 19;
		}
	}

	/** wechselt den Zauber vom Spieler */
	public void changeMagicType() {
		//Schaltet die La Magie ab Level 2 frei
		if(player.getplayerLevel() == 2){
			if (player.magicType == MagicLogic.ANA) {
				player.magicType = MagicLogic.LA;
			}else if(player.magicType == MagicLogic.LA){
				player.magicType = MagicLogic.ANA;}
			// Schaltet die Info Magie ab Level 3 frei
		}else if(player.getplayerLevel() > 2){
			if (player.magicType == MagicLogic.ANA) {
				player.magicType = MagicLogic.LA;
			}else if(player.magicType == MagicLogic.LA){
				player.magicType = MagicLogic.INFO;
			}else
				player.magicType = MagicLogic.ANA;
		}
	}

	/**
	 * errechnet abhaengig vom Erfahrungslevel, wieviele Erfahrungspunkte benoetigt werden, um ein Level aufzusteigen
	 * @return needed Experience
	 */
	public int getNeededExperience(){
		switch (player.getplayerLevel()){

		case 1:
			return 100;
		case 2:
			return 150;
		case 3:
			return 300;
		case 4:
			return 500;
		default:
			return 750;
		}
	}

	/** laesst den Spieler ein Erfahrungslevel aufsteigen, wenn er genug Erfahrungspunkte hat */
	public void checkExperience(){
		if (player.getExperience() >= getNeededExperience()){
			player.setExperience( player.getExperience() % getNeededExperience());
			player.setPlayerLevel(player.getplayerLevel()+1);
		}
	}


	/** legt die Textur vom Spieler fest, abhaengig von der Richtung in die er geht */
	public void setTexture(){
		if(player.getLeft() > 0)
			houston.player.texture = houston.player.tex_left;
		if(player.getRight() > 0)
			houston.player.texture = houston.player.tex_right;
		if(player.getUp() > 0)
			houston.player.texture = houston.player.tex_up;
		if(player.getDown() > 0)
			houston.player.texture = houston.player.tex_down;
	}
}
