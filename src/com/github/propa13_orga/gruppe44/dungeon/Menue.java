package com.github.propa13_orga.gruppe44.dungeon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.*;


public class Menue extends JFrame implements ActionListener{
	
//benoetigte Buttons
	
	private JButton spielen;
	private JButton einstellungen;
	private JButton programmierer;
	private JButton ende;
	
	Image img;
	
//Methoden der Buttons
	
	public void actionPerformed(ActionEvent a) {
		if (a.getSource()==spielen){
			fenster();
		}
		if (a.getSource()==programmierer){
			Object[] option = {"Schliessen"};
			
			JOptionPane.showOptionDialog(null, "Gruppe 44","Programmierer", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option [0]);
			// null, Text, Ueberschrift, ..., Buttontext
		}
		
		if (a.getSource()== einstellungen){
		Object[] option = {"Schliessen"};
			
			JOptionPane.showOptionDialog(null, "","Einstellungen", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option [0]);
			
			//noch ohne Inhalt

		}
		
		if (a.getSource()== ende){
			System.exit(0);
		}
		
	}
	

//Eigenschaften der Buttons 
	
	public Menue (String titel){
		super(titel);
		
		
		spielen = new JButton ("Spiel starten");
		spielen.setBounds(150,30,160,40); // Position, Groesse
		spielen.addActionListener(this);
		add(spielen);
		
		einstellungen = new JButton ("Einstellungen"); // noch ohne Inhalt
		einstellungen.setBounds(150,110,160,40);
		einstellungen.addActionListener(this);
		add(einstellungen);
		
		programmierer = new JButton ("Programmierer");
		programmierer.setBounds(150,190,160,40);
		programmierer.addActionListener(this);
		add(programmierer);
		
		ende = new JButton ("Spiel beenden");
		ende.setBounds(150,270,160,40);
		ende.addActionListener(this);
		add(ende);
		
		ImageIcon u = new ImageIcon ("C:/Users/Satyra/workspace/gruppe44/lighthouse.jpg");
		img = u.getImage();
	}
//notwendig fuer den Hintergrund
	
public void paint (Graphics g){
		
		super.paint(g);
		Graphics2D f2 = (Graphics2D) g;
		//Methode zeichnet sonst nur Linien, notwendig um Bilder darstellen zu können
		
		f2.drawImage(img,0,0,null);
		//Name, Position
	}
	
	
// Startbildfenster
	
	public static void main(String[] args) {
		
		Menue Menue = new Menue ("Dungeon");
		Menue.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Menue.setSize(450,400); // Fenstergroesse
		Menue.setLayout(null); //kein vorgegebenes Layout
		Menue.setVisible(true); //sichtbar
	}
	
//Spielfenster
	
	public static void fenster (){
		JFrame fenster = new JFrame ();
		fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenster.setSize(500,400);
		fenster.setVisible (true);
		fenster.add(new gui());

	}

}
