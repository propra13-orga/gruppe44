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
			// System.out.println("resetPlayer");
			houston.player.resetPosition();
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

	static class useHealthPack extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public useHealthPack(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			houston.inventory.useHealthPack();
		}

	}

	static class useManaPotion extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public useManaPotion(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			houston.inventory.useManaPotion();
		}

	}

	static class interact extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public interact(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (houston.logic.value == 5) {
				houston.shop.resetShopView();
				houston.changeAppearance(false, "SHOP");
			} else if (houston.logic.value == 3) {
				houston.player.stop();
				// Hier koennte, fuer spezielle NPC eine Abfrage stehen ob storyCounter erhoeht wird
				// Text einfuegen, der in storyText steht
			
			}
		}

	}

	static class attack extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public attack(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			houston.logic.attack();
		}

	}

}