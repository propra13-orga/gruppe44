package Main;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class MenuCards {

	static class card1 extends JPanel {

		private static final long serialVersionUID = 1L;

		// Baut das Hauptmenue
		public card1(Houston houston) {
			this.setLayout(null);
			houston.c1b1 = new JButton("Neues Spiel");
			houston.c1b1.addActionListener(houston);
			houston.c1b1.setBounds(284, 300, 200, 40);
			this.add(houston.c1b1);

			houston.c1b2 = new JButton("Einstellungen");
			houston.c1b2.addActionListener(houston);
			houston.c1b2.setBounds(284, 360, 200, 40);
			this.add(houston.c1b2);

			houston.c1b3 = new JButton("Mitwirkende");
			houston.c1b3.addActionListener(houston);
			houston.c1b3.setBounds(284, 420, 200, 40);
			this.add(houston.c1b3);

			houston.c1b4 = new JButton("Karten Editor");
			houston.c1b4.addActionListener(houston);
			houston.c1b4.setBounds(284, 480, 200, 40);
			this.add(houston.c1b4);

			houston.c1b5 = new JButton("Beenden");
			houston.c1b5.addActionListener(houston);
			houston.c1b5.setBounds(284, 600, 200, 40);
			this.add(houston.c1b5);

			houston.c1b6 = new JButton("Multiplayer");
			houston.c1b6.addActionListener(houston);
			houston.c1b6.setBounds(284, 540, 200, 40);
			this.add(houston.c1b6);
		}

	}

	static class card2 extends JPanel {

		private static final long serialVersionUID = 1L;

		// Baut das Einstellungsfenster
		public card2(Houston houston) {
			this.setLayout(null);

			houston.c2b1 = new JButton("Hauptmen\u00fc");
			houston.c2b1.setBounds(284, 360, 200, 40);
			houston.c2b1.addActionListener(houston);
			this.add(houston.c2b1);

			houston.maenlich = new JRadioButton("Spieler");
			houston.maenlich.setBounds(284, 250, 210, 40);
			houston.maenlich.addActionListener(houston);
			this.add(houston.maenlich);
			houston.maenlich.setSelected(true);

			houston.weiblich = new JRadioButton("Spielerin");
			houston.weiblich.setBounds(284, 300, 210, 40);
			houston.weiblich.addActionListener(houston);
			this.add(houston.weiblich);

			ButtonGroup g = new ButtonGroup();
			g.add(houston.maenlich);
			g.add(houston.weiblich);
		}

	}

	static class card4 extends JPanel {

		private static final long serialVersionUID = 1L;

		// Baut das Spielmenue
		public card4(Houston houston) {
			this.setLayout(null);

			houston.c4b1 = new JButton("Zur\u00fcck ins Spiel");
			houston.c4b1.addActionListener(houston);
			houston.c4b1.setBounds(284, 300, 200, 40);
			this.add(houston.c4b1);

			houston.c4b2 = new JButton("Hauptmen\u00fc");
			houston.c4b2.addActionListener(houston);
			houston.c4b2.setBounds(284, 360, 200, 40);
			this.add(houston.c4b2);
		}

	}

	static class card5 extends JPanel {

		private static final long serialVersionUID = 1L;

		// Baut das Mitwirkendenfenster
		public card5(Houston houston) {
			this.setLayout(null);

			JLabel picsandy = new JLabel(new ImageIcon("./res/img/credits/sandy.png"));
			picsandy.setBounds(29, 200, 130, 130);
			this.add(picsandy);

			JLabel picjana = new JLabel(new ImageIcon("./res/img/credits/jana.png"));
			picjana.setBounds(174, 200, 130, 130);
			this.add(picjana);

			JLabel picphil = new JLabel(new ImageIcon("./res/img/credits/phil.png"));
			picphil.setBounds(319, 200, 130, 130);
			this.add(picphil);

			JLabel picphilipp = new JLabel(new ImageIcon("./res/img/credits/philipp.png"));
			picphilipp.setBounds(464, 200, 130, 130);
			this.add(picphilipp);

			JLabel picdavid = new JLabel(new ImageIcon("./res/img/credits/david.png"));
			picdavid.setBounds(609, 200, 130, 130);
			this.add(picdavid);

			houston.c5b1 = new JButton("Hauptmen\u00fc");
			houston.c5b1.addActionListener(houston);
			houston.c5b1.setBounds(284, 420, 200, 40);
			this.add(houston.c5b1);
		}

	}

	static class card6 extends JPanel {

		private static final long serialVersionUID = 1L;

		// Baut das Introductionfenster
		public card6(Houston houston) {
			this.setLayout(null);

			houston.c6b1 = new JButton("Weiter");
			houston.c6b1.addActionListener(houston);
			houston.c6b1.setBounds(284, 300, 200, 40);
			this.add(houston.c6b1);

			// Button fuer Testzwecke
			houston.c6map1 = new JButton("Map 1");
			houston.c6map1.addActionListener(houston);
			houston.c6map1.setBounds(100, 400, 80, 20);
			this.add(houston.c6map1);
			houston.c6map2 = new JButton("Map 2");
			houston.c6map2.addActionListener(houston);
			houston.c6map2.setBounds(100, 430, 80, 20);
			this.add(houston.c6map2);
			houston.c6map3 = new JButton("Map 3");
			houston.c6map3.addActionListener(houston);
			houston.c6map3.setBounds(100, 460, 80, 20);
			this.add(houston.c6map3);
			houston.c6map4 = new JButton("Map 4");
			houston.c6map4.addActionListener(houston);
			houston.c6map4.setBounds(200, 400, 80, 20);
			this.add(houston.c6map4);
			houston.c6map5 = new JButton("Map 5");
			houston.c6map5.addActionListener(houston);
			houston.c6map5.setBounds(200, 430, 80, 20);
			this.add(houston.c6map5);
			houston.c6map6 = new JButton("Map 6");
			houston.c6map6.addActionListener(houston);
			houston.c6map6.setBounds(200, 460, 80, 20);
			this.add(houston.c6map6);
			houston.c6map7 = new JButton("Map 7");
			houston.c6map7.addActionListener(houston);
			houston.c6map7.setBounds(300, 400, 80, 20);
			this.add(houston.c6map7);
			houston.c6map8 = new JButton("Map 8");
			houston.c6map8.addActionListener(houston);
			houston.c6map8.setBounds(300, 430, 80, 20);
			this.add(houston.c6map8);
			houston.c6map9 = new JButton("Map 9");
			houston.c6map9.addActionListener(houston);
			houston.c6map9.setBounds(300, 460, 80, 20);
			this.add(houston.c6map9);

			JLabel label = new JLabel(
					"<html>06:30 Der Wecker klingelt. <p/>" +
							"06:35 Der Wecker klingelt nochmal. <p/>" +
							"06:40 Der Wecker klingelt nochmal. <p/>" +
							"06:45 Der Wecker klingelt nochmal. <p/>" +
							"Musterstudierender steht auf. Zeit: Knapp. Motivation: M\u00e4\u00dfig. <p/>" +
							"Was w\u00fcrde in der Uni nur auf Musterstudierender warten? <p/>" +
							"Erste Vorlesung: Ein bisschen mit dem Handy spielen. <p/>" +
							"\u00dcbung: Sich anstrengen nicht einzuschlafen. <p/>" +
							"Dann ab in die Schweinemensa. <p/>" +
					"Doch was Musterstudierender nicht wusste, heute sollte kein gew\u00f6hnlicher Tag werden....<html>");
			label.setBounds(100, 20, 600, 300);
			this.add(label);
		}

	}

	static class card7 extends JPanel {

		private static final long serialVersionUID = 1L;

		// Baut den Shop
		public card7(Houston houston) {
			this.setLayout(null);

			houston.c7l4 = new JLabel(new ImageIcon("./res/img/shop/Club_mate.png"));
			houston.c7l4.setBounds(50, 200, 200, 149);
			this.add(houston.c7l4);

			houston.c7b1 = new JButton("Healthpack - 40 CP");
			houston.c7b1.addActionListener(houston);
			houston.c7b1.setBounds(50, 360, 200, 40);
			ImageIcon healthpack = new ImageIcon("./res/img/tiles/health.png");
			houston.c7b1.setIcon(healthpack);
			this.add(houston.c7b1);

			houston.c7l6 = new JLabel("Im Inventar: ");
			houston.c7l6.setBounds(50, 400, 200, 50);
			this.add(houston.c7l6);

			houston.c7l8 = new JLabel("0");
			houston.c7l8.setBounds(130, 400, 200, 50);
			this.add(houston.c7l8);

			houston.c7l5 = new JLabel(new ImageIcon("./res/img/shop/Killepitsch.png"));
			houston.c7l5.setBounds(284, 200, 200, 149);
			this.add(houston.c7l5);

			houston.c7b2 = new JButton("Manatrank - 80 CP");
			houston.c7b2.addActionListener(houston);
			houston.c7b2.setBounds(284, 360, 200, 40);
			ImageIcon manatrank = new ImageIcon("./res/img/tiles/mana.png");
			houston.c7b2.setIcon(manatrank);
			this.add(houston.c7b2);

			houston.c7l7 = new JLabel("Im Inventar: ");
			houston.c7l7.setBounds(284, 400, 200, 50);
			this.add(houston.c7l7);

			houston.c7l9 = new JLabel("0");
			houston.c7l9.setBounds(364, 400, 200, 50);
			this.add(houston.c7l9);

			houston.c7l10 = new JLabel("Geld: ");
			houston.c7l10.setBounds(284, 560, 200, 40);
			this.add(houston.c7l10);

			houston.c7l11 = new JLabel("200 CP");
			houston.c7l11.setBounds(324, 560, 200, 40);
			this.add(houston.c7l11);

			houston.c7b3 = new JButton("zur\u00fcck ins Spiel");
			houston.c7b3.addActionListener(houston);
			houston.c7b3.setBounds(284, 600, 200, 40);
			this.add(houston.c7b3);
		}

	}

}
