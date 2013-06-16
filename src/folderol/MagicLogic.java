package folderol;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MagicLogic {

	private Houston houston;
	private Player player;
	
	public ArrayList<Magic> magics;
	private int manaCost = 5;
	
	public MagicLogic(Houston houston) {
		this.houston = houston;
		this.player = houston.player;
		magics = new ArrayList<Magic>();
	}

	public void doMagic(Point2D mouseClickPosition) {
		if (houston.player.getMana() >= manaCost) {
			player.decreaseMana(manaCost);
			magics.add(new Magic(houston, player.getCenterPosition(), mouseClickPosition));
		}
	}
	
}
