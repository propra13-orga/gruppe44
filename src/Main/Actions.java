package Main;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/** Reaktion auf Tastendruck */
public class Actions {

	/** oeffnet das Ingamemenu */
	static class jumpToIngamemenu extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final Houston houston;


		public jumpToIngamemenu(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("ESC -> Ingamemenu");
			if (houston.multiPlayer.ready) {
				houston.changeAppearance(false, false, Houston.MULTIPLAYER);
			} else {
				houston.changeAppearance(false, false, Houston.INGAMEMENU);
			}
		}

	}

	/** oeffnet das Spiel */
	static class jumpToGame extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final Houston houston;

		public jumpToGame(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("ESC -> Game");
			houston.changeAppearance(false, true, Houston.GAME);
		}

	}

	/** Setzt den Spieler an die Ursprungsposition */
	static class resetPlayer extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final Houston houston;

		public resetPlayer(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("resetPlayer");
			houston.player.resetPosition();
		}

	}

	/** Spieler geht nach oben */
	static class moveUp extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final Houston houston;

		public moveUp(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("pressedUp");
			houston.gameLogic.npcv = 0;
			houston.player.setUp(100);
		}

	}

	/** Spieler geht nach unten */
	static class moveDown extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final Houston houston;

		public moveDown(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("pressedDown");
			houston.gameLogic.npcv = 0;
			houston.player.setDown(100);
		}

	}

	/** Spieler geht nach links */
	static class moveLeft extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final Houston houston;

		public moveLeft(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("pressedLeft");
			houston.gameLogic.npcv = 0;
			houston.player.setLeft(100);
		}

	}

	/**  Spieler geht nach rechts */
	static class moveRight extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final Houston houston;

		public moveRight(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("pressedRight");
			houston.gameLogic.npcv = 0;
			houston.player.setRight(100);
		}

	}

	/** Spieler geht nicht mehr nach oben */
	static class releasedUp extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final Houston houston;

		public releasedUp(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("releasedUp");
			houston.player.setUp(0);
		}

	}

	/** Spieler geht nicht mehr nach unten */
	static class releasedDown extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final Houston houston;

		public releasedDown(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("releasedDown");
			houston.player.setDown(0);
		}

	}

	/** Spieler geht nicht mehr nach links */
	static class releasedLeft extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final Houston houston;

		public releasedLeft(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("releasedLeft");
			houston.player.setLeft(0);
		}

	}

	/** Spieler geht nicht mehr nach rechts */
	static class releasedRight extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final Houston houston;

		public releasedRight(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("releasedRight");
			houston.player.setRight(0);
		}

	}

	/** Spieler nimmt sein Healthpack ein */
	static class useHealthPack extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final Houston houston;

		public useHealthPack(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			houston.inventory.useHealthPack();
		}

	}

	/** Spieler nimmt sein Manapotion */
	static class useManaPotion extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final Houston houston;

		public useManaPotion(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			houston.inventory.useManaPotion();
		}

	}

	/** Spieler spricht mit dem NPC oder betritt den Shop, abhaengig von seiner aktuellen Position */
	static class interact extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final Houston houston;

		public interact(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (houston.gameLogic.value == 5) {
				houston.shop.resetShopView();
				houston.changeAppearance(false, "SHOP");
			} else if (houston.gameLogic.npcv == 1) {
				houston.story.showText();
			} else if (houston.gameLogic.npcv == 2) {
				houston.quest.doQuest();
			}
		}

	}

	/** Spieler greift an */
	static class attack extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final Houston houston;

		public attack(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			houston.playerLogic.attack();
		}

	}

}