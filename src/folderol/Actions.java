package folderol;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class Actions {

	static class jumpToIngamemenu extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public jumpToIngamemenu(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("ESC -> Ingamemenu");
			houston.changeAppearance(false, false, "INGAMEMENU");
			houston.player.up = false;
			houston.player.down = false;
			houston.player.left = false;
			houston.player.right = false;
		}

	}

	static class jumpToGame extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public jumpToGame(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("ESC -> Game");
			houston.changeAppearance(false, true, "GAME");
		}

	}

	static class resetPlayer extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public resetPlayer(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("pressedUp");
			houston.player.resetPosition();
		}

	}
	
	static class useHealthpack extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public useHealthpack(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
	
			houston.healthpack.useH = true;
			houston.healthpack.useHealthpack();
		}

	}	
	
	static class useManatrank extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public useManatrank(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
	
			houston.manatrank.useM = true;
			houston.manatrank.useManatrank();
		}
	}
	
	static class shop extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public shop(Houston houston) {
			this.houston = houston;
		}
		public void actionPerformed(ActionEvent e) {
			if(houston.logic.detectSpecialTiles()==5){
			houston.player.up = false;
			houston.player.down = false;
			houston.player.left = false;
			houston.player.right = false;
			if (houston.player.money < 40 || houston.healthpack.maximalItems == 3 ){
				houston.c7b1.setEnabled(false);
				
			}else if((houston.healthpack.maximalItems < 3) && houston.player.money >= 40 )
				{ houston.c7b1.setEnabled(true);}
			houston.changeAppearance(false, true, "SHOP");
			
			if (houston.player.money < 100 || houston.manatrank.maximalItems == 3){
				houston.c7b2.setEnabled(false);
			}else if(houston.manatrank.maximalItems < 3 && houston.player.money >= 100){
				houston.c7b2.setEnabled(true);}
			
			}
		}

	}
	
	static class moveUp extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public moveUp(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("pressedUp");
			houston.player.up = true;
		}

	}

	static class moveDown extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public moveDown(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("pressedDown");
			houston.player.down = true;
		}

	}

	static class moveLeft extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public moveLeft(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("pressedLeft");
			houston.player.left = true;
		}

	}

	static class moveRight extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public moveRight(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("pressedRight");
			houston.player.right = true;
		}

	}

	static class releasedUp extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public releasedUp(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("releasedUp");
			houston.player.up = false;
		}

	}

	static class releasedDown extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public releasedDown(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("releasedDown");
			houston.player.down = false;
		}

	}

	static class releasedLeft extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public releasedLeft(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("releasedLeft");
			houston.player.left = false;
		}

	}

	static class releasedRight extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public releasedRight(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("releasedRight");
			houston.player.right = false;
		}

	}

}
