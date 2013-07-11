package Logic;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import Main.Enemy;
import Main.Houston;
import Main.Magic;
import Main.Player;

/** enthaelt die Logik des Zaubers */
public class MagicLogic {

	private final Houston houston;
	private final Player player;
	private final EnemyLogic enemyLogic;

	/** Magicarray, enthaelt alle aktiven Zauber */
	public ArrayList<Magic> magics;
	private Magic magic;
	private Enemy enemy;
	private final int manaCost = 5;
	private Timer timer;
	private TimerTask enemyMagic;
	private String magicType;

	/** enthaelt die Texturen der unterschiedlichen Zauber */
	public HashMap<String, BufferedImage> texture;

	/** String ANALYSIS */
	public final static String ANA = "ANALYSIS";

	/** String LINEAREALGEBRA */
	public final static String LA = "LINEARALGEBRA";

	/** String INFORMATIK */
	public final static String INFO = "INFORMATIK";

	/**
	 * initialisiert das Magicarray
	 * @param houston
	 */
	public MagicLogic(Houston houston) {
		this.houston	= houston;
		this.player		= houston.player;
		this.enemyLogic = houston.enemyLogic;
		texture = new HashMap<>();
		initializeHashMap();

		magics = new ArrayList<Magic>();
		setMagic();
	}

	/** prueft welche Gegner gerade zaubern und erstellt den Zauber */
	public void setMagic(){
		timer = new Timer();

		enemyMagic = new TimerTask(){

			/** fuegt Zauber vom Gegner dem Array hinzu
			 * @see java.util.TimerTask#run()
			 */
			@Override
			public void run (){
				for (Enemy enemy : houston.enemyLogic.enemies) {
					if(enemy.shoot == 1){
						magicType = enemy.enemyField;
						magics.add(new Magic(texture.get(magicType), enemy.getCenterPosition(), player.getCenterPosition(), false, magicType));
					}
				}
			}
		};

		timer.schedule(enemyMagic, 100, 2000);
	}

	/**
	 * Spieler Zauber per Mausklick
	 * @param mouseClickPosition
	 */
	public void doMagic(Point2D mouseClickPosition) {
		if (houston.player.getMana() >= manaCost) {
			player.decreaseMana(manaCost);
			magics.add(new Magic(texture.get(player.magicType), player.getCenterPosition(), mouseClickPosition, true, player.magicType));
		}
	}

	/** prueft, ob der Zauber vom Spieler den Gegner bzw der Zauber vom Gegner den Spieler trifft und reagiert entsprechend */
	public void doGameUpdates() {
		for (int i = magics.size() - 1; i >= 0; i--) {
			magic = magics.get(i);

			for (int j = enemyLogic.enemies.size() - 1; j >= 0; j--) {
				enemy = enemyLogic.enemies.get(j);

				if((magic.getBounds().intersects(enemy.getBounds())) && (magic.isMagicFromPlayer())) {
					if(magic.magicType == enemy.enemyField){
						enemy.decreaseHealth(calculateMagicDamage(player.getplayerLevel()));
					}else{
						enemy.decreaseHealth(calculateMagicDamage(player.getplayerLevel()) + 5);
					}
					if(enemy.getHealth() <= 0){
						boolean isBoss = false;
						if(houston.enemyLogic.bossIsAlive){
							isBoss = true;
						}
						enemy.remove= true;
						player.increaseMoney(10);
						player.increaseExperience(enemy.getExperience(houston.map.getLevelNumber()+1, isBoss));
						houston.playerLogic.checkExperience();
					}
					magic.remove = true;
				}
			}
			// Kollisionserkennung mit Magic, die von Gegnern kommt
			if ((magic.getBounds()).intersects(player.getBounds()) && (magic.isMagicFromPlayer() == false)) {
				player.decreaseHealth(10);
				magic.remove = true;
			}

			// Filtert die zu loeschenden Magics raus
			if (magic.remove) {
				magics.remove(i);
				continue;
			}

			houston.gameLogic.controlCharacterMovement(magic);
		}
	}

	/**
	 * erhoeht die Magie bei ungraden Leveln um 5
	 * @param playerLevel
	 * @return MagicDamage
	 */
	public int calculateMagicDamage(int playerLevel){
		if( playerLevel == 1){
			return 5;
		}if( playerLevel == 2){
			return 5;
		}if( playerLevel == 3){
			return 10;
		}if( playerLevel == 4){
			return 15;
		}else {
			return 20;
		}
	}

	/** entfernt alle Zauber bei Level wechsel */
	public void onLevelChange() {
		magics.clear();
	}

	private void initializeHashMap(){
		try {
			BufferedImage texture1 = ImageIO.read(new File("./res/img/tiles/analysis.png"));
			BufferedImage texture2 = ImageIO.read(new File("./res/img/tiles/lineareAlgebra.png"));
			BufferedImage texture3 = ImageIO.read(new File("./res/img/tiles/informatik.png"));
			texture.put(ANA, texture1);
			texture.put(LA, texture2);
			texture.put(INFO, texture3);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
