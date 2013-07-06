package Logic;


import java.awt.Component;
import java.awt.geom.Point2D;

import javax.swing.JOptionPane;

import Main.Enemy;
import Main.Houston;
import Main.Player;

public class PlayerLogic {

	private final Houston houston;
	private final Player player;

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

	public void attack() {
		for (Enemy enemy : houston.enemyLogic.enemies) {
			if (player.attackBox.intersects(enemy.getBounds())) {
				enemy.decreaseHealth(getAttackDamage(player.getplayerLevel()));
				if(enemy.getHealth() <= 0){
					boolean isBoss = false;
					if(houston.enemyLogic.bossIsAlive){
						houston.enemyLogic.bossIsAlive = false;
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
	//erhoeht die Attacke bei allen graden Leveln und 1
	
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
			return 20;
			}
	}

	public void levelUpFrame(){
		Component frame = null;
		if(player.getplayerLevel() == 2){
			JOptionPane.showMessageDialog(frame, "<html>Lineare Algebra Magie freigeschaltet <html>", 
					"Level aufgestiegen", JOptionPane.INFORMATION_MESSAGE);
		}else if(player.getplayerLevel() == 3){
			JOptionPane.showMessageDialog(frame, "<html>Angriff um 3 erh\u00f6ht <p/>" + "Magie um 5 erh\u00f6ht <html>",
					"Level aufgestiegen", JOptionPane.INFORMATION_MESSAGE);
		}else if(player.getplayerLevel() == 4){
			JOptionPane.showMessageDialog(frame,"<html>Info Magie freigeschaltet <p/>" + "Angriff um 3 erh\u00f6ht" + "Magie um 5 erh\u00f6ht <html>",
					"Level aufgestiegen", JOptionPane.INFORMATION_MESSAGE);
		}else if(player.getplayerLevel() == 5){
			JOptionPane.showMessageDialog(frame, "<html>Angriff um 3 erh\u00f6ht <p/>" + "Magie um 5 erh\u00f6ht <html>",
					"Level aufgestiegen", JOptionPane.INFORMATION_MESSAGE);
		}
	}

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
	
	public int getNeededExperience(){
		switch (player.getplayerLevel()){
		
		case 1:
			return 100;
		case 2:
			return 150;
		case 3:
			return 350;
		case 4:
			return 580;
		default:
			return 999;
		}
	}

	public void checkExperience(){
		if (player.getExperience() >= getNeededExperience()){
			player.setExperience( player.getExperience() % getNeededExperience());
			player.setPlayerLevel(player.getplayerLevel()+1);
			levelUpFrame();
		}	
	}
	

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
