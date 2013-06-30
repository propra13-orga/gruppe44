package Logic;


import java.awt.Component;
import java.awt.geom.Point2D;

import javax.swing.JOptionPane;

import Main.Enemy;
import Main.Houston;
import Main.Player;

public class PlayerLogic {

	private Houston houston;
	private Player player;
	int attackDamage = 5;

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
			houston.changeAppearance(true, false, Houston.STARTMENU);
		}
	}

	public void attack() {
		for (Enemy enemy : houston.enemyLogic.enemies) {
			if (player.attackBox.intersects(enemy.getBounds())) {
				calculateAttackDamage();
				enemy.decreaseHealth(attackDamage);
				if(enemy.getHealth() <= 0){
					if(houston.enemyLogic.bossIsAlive)
						houston.enemyLogic.bossIsAlive = false;
					enemy.remove= true;
					player.increaseMoney(10);
					player.increaseExperience(15);
				}
			}
		}
		checkExperience();
	}
	//erhoeht die Attacke bei allen graden Leveln und 1
	public void calculateAttackDamage(){
		if(player.getplayerLevel() % 2 == 0){
			attackDamage ++;
		}
	}
	
	public void levelUpFrame(){
		Component frame = null;
		if(player.getplayerLevel() % 2 == 0 && player.getplayerLevel() == 2){
			JOptionPane.showMessageDialog(frame,"<html>Lineare Algebra Magie freigeschaltet <p/>" + "Angriff um 1 erh\u00f6ht <html>", "Level aufgestiegen", JOptionPane.INFORMATION_MESSAGE);
		}else if(player.getplayerLevel() % 2 == 0){
			JOptionPane.showMessageDialog(frame, "<html>Angriff um 1 erh\u00f6ht <html>", "Level aufgestiegen", JOptionPane.INFORMATION_MESSAGE);
		}else if(player.getplayerLevel() % 2 == 1 && player.getplayerLevel() == 3){
			JOptionPane.showMessageDialog(frame, "<html>Informatik Magie freigeschaltet <p/>" + "Magie um 1 erh\u00f6ht <html>", "Level aufgestiegen", JOptionPane.INFORMATION_MESSAGE);
		}else if(player.getplayerLevel() % 2 == 1){
			JOptionPane.showMessageDialog(frame, "<html>Magie um 1 erh\u00f6ht <html>", "Level aufgestiegen", JOptionPane.INFORMATION_MESSAGE);
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
	
	public void checkExperience(){
		if (player.getExperience() >=100){
			player.setPlayerLevel(player.getplayerLevel()+1);
			player.setExperience( player.getExperience() % 100);
			levelUpFrame();
		}
	}
}
