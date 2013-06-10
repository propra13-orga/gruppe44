package folderol;

public class Manatrank {
	boolean gripped;
	boolean useM;
	int maximalItems = 1; //Anzahl der derzeit vorhandenen Items (max. 3)
	Houston houston;
	Player player;
	public Manatrank(Houston houston){
		this.player = houston.player;
	}
	//prueft, ob Spieler Manatrank benutzen darf
	public void useManatrank(){
		if((useM) && (gripped)){	
			player.mana = player.mana +100;
			if (maximalItems == 1){
			gripped = false;}	
			maximalItems--;
		}
	}
	public void resetManatrank(){
		gripped = true;
	}
	

}
