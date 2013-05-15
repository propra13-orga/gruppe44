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

	static class moveUp extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Houston houston;

		public moveUp(Houston houston) {
			this.houston = houston;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("moveUp");
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
			System.out.println("moveDown");
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
			System.out.println("moveLeft");
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
			System.out.println("moveRight");
		}

	}

}
