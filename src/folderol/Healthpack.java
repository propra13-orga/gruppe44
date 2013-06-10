package folderol;

public class Healthpack {
	boolean gripped;
	boolean useH;
	int maximalItems = 1; //Anzahl der derzeit vorhandenen Items (max. 3)
	Houston houston;
	Player player;
	public Healthpack(Houston houston){
		this.player = houston.player;
	}
	//prueft, ob Spieler Healthpack benutzen darf
	public void useHealthpack(){
			if((useH) && (gripped) && (player.health<=80)){	
				player.health = player.health +20;
				if (maximalItems == 1){
					gripped = false;}
				maximalItems--;
		}
	}
	
	public void resetHealthpack(){
		gripped = true;
	}
	
	

}
