package MapEditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import Main.Houston;
import Main.Map;

public class MapEditor extends JPanel implements ActionListener, MouseListener, MouseMotionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	private Houston houston;
	private Map map;
	
	private JFrame editorWindowFrame;
	private JButton undoButton, startMenu, 
	dateiLesen, dateiSpeichern, dateiSpeichernUnter, 
	neuesLevel, neueKarte;
	private JList<Object> list;
	private JFileChooser fc;
	
	private String filePath;
	private Color bgColor;
	private Font plainFont;
	private Point2D mouseClickPosition;
	private Point2D selected;
	
	private int drawValue = -1;
	private int countOfUndos = 20;
	private int undoArrayPosition;
	private int[] undoTileValues;
	private Point2D[] undoTilePositions;
	private boolean mapIsDifferentThanOriginal;

	public MapEditor(Houston houston) {
		this.houston = houston;
		this.map = houston.map;
		bgColor = new Color(240, 240, 240);
		plainFont = new Font("Arial", Font.PLAIN, 12);
		
		addMouseListener(this);
		addMouseMotionListener(this);		
	}

	@Override
	protected void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		g.translate(0, 32);
		g.setFont(plainFont);
		
		g.setColor(bgColor);
		g.fillRect(0, -32, houston.width, houston.height);
		
		g.setColor(Color.BLACK);
		if (filePath.length() > 0)
			g.drawString("Speicherort: " + filePath, 16, -19);
		else
			g.drawString("Speicherort auswählen ...", 16, -19);

		if (!mapIsDifferentThanOriginal)
			g.drawString("Karte ist unverändert.", 16, -5);
		else {
			g.setColor(Color.RED);
			g.drawString("Kartenänderungen sind noch nicht gespeichert.", 16, -5);
		}

		map.drawObjects(g);

		g.setColor(Color.GREEN);
		g.drawRect((int) selected.getX(), (int) selected.getY(), 32, 32);
	}
	
	public void showEditorWindow() {
		createEditorWindow();
	}
	
	public void hideEditorWindow() {
		editorWindowFrame.dispose();
	}
	
	private void createEditorWindow() {
		// Erstellt eine neue blanke Karte
		map.clearMap(0);
		
		// Erstellet Punkte fuer die ClickPosition und die ausgewaehlte Kachel
		mouseClickPosition = new Point2D.Double();
		selected = new Point2D.Double(99999, 99999);
		
		// Initialisiert die Undo Funktionen
		undoTileValues = new int[countOfUndos]; 
		undoTilePositions = new Point2D[countOfUndos];
		undoArrayPosition = 0;
		
		// Dialog zum Auswaehlen von Dateien
		if (fc == null)
			fc = new JFileChooser();
		fc.setCurrentDirectory(new File("./res/maps/"));
		fc.setFileFilter(new FileNameExtensionFilter("Text File", "txt"));
		filePath = new String();
		
		// Editor Fenster
		editorWindowFrame = new JFrame("Editor");
		editorWindowFrame.setLayout(null);
		editorWindowFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		editorWindowFrame.setBounds(0, 0, 300, 400);
		editorWindowFrame.setPreferredSize(new Dimension(360, 400));
		editorWindowFrame.setLocation(houston.frame.getX()+houston.frame.getWidth(), houston.frame.getY());
		editorWindowFrame.setResizable(false);
		editorWindowFrame.setVisible(true);
		
		// Oberste Button
		undoButton = new JButton("Rückgängig");
		undoButton.setEnabled(false);
		undoButton.addActionListener(this);
		undoButton.setBounds(5, 5, 140, 30);
		editorWindowFrame.add(undoButton);
		
		startMenu = new JButton("Hauptmenü");
		startMenu.addActionListener(this);
		startMenu.setBounds(155, 5, 140, 30);
		editorWindowFrame.add(startMenu);
		
		// Tabs
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(0, 40, 300, 340);
		
		JPanel panel1 = new JPanel(false);
		panel1.setLayout(null);
		tabbedPane.addTab("Datei", panel1);
		
		JPanel panel2 = new JPanel(false);
		panel2.setLayout(null);
		tabbedPane.addTab("Karte", panel2);
		
		JPanel panel3 = new JPanel(false);
		panel3.setLayout(null);
		tabbedPane.addTab("Gegner", panel3);
		
		JPanel panel4 = new JPanel(false);
		panel4.setLayout(null);
		tabbedPane.addTab("Items", panel4);
		
		editorWindowFrame.add(tabbedPane);
		
		
		// Tab #1
		dateiLesen = new JButton("Karte laden");
		dateiLesen.setBounds(10, 10, 260, 30);
		dateiLesen.addActionListener(this);
		panel1.add(dateiLesen);
		
		dateiSpeichern = new JButton("Karte speichern");
		dateiSpeichern.setBounds(10, 40, 260, 30);
		dateiSpeichern.addActionListener(this);
		panel1.add(dateiSpeichern);
		
		dateiSpeichernUnter = new JButton("Karte speichern unter");
		dateiSpeichernUnter.setBounds(10, 70, 260, 30);
		dateiSpeichernUnter.addActionListener(this);
		panel1.add(dateiSpeichernUnter);
		
		neuesLevel = new JButton("Neues Level erstellen");
		neuesLevel.setBounds(10, 120, 260, 30);
		neuesLevel.addActionListener(this);
		neuesLevel.setEnabled(false);
		panel1.add(neuesLevel);
		
		neueKarte = new JButton("Neue Karte erstellen");
		neueKarte.setBounds(10, 150, 260, 30);
		neueKarte.addActionListener(this);
		neueKarte.setEnabled(false);
		panel1.add(neueKarte);
		
		// Tab #2
		ListRenderer listRenderer = new ListRenderer(houston);
		list = new JList<>(map.texture.keySet().toArray());
		list.setCellRenderer(listRenderer);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setBounds(80, 40, 200, 200);
		list.setVisibleRowCount(-1);
		list.ensureIndexIsVisible(10);
		list.addListSelectionListener(this);
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(10, 10, 260, 280);
		scrollPane.setVisible(true);
		panel2.add(scrollPane);
	}
	
	private void paintTile(Point2D mouseClickPosition) {
		selectTileByPosition(mouseClickPosition);
		undoTileValues[undoArrayPosition] = getTileValueByPosition(mouseClickPosition);
		undoTilePositions[undoArrayPosition] = new Point2D.Double(selected.getX(), selected.getY());
		
		undoArrayPosition = (((undoArrayPosition + 1) + countOfUndos) % countOfUndos);
		
		// Aendere Wert der Kachel
		changeTileValueAtPosition(mouseClickPosition, drawValue);
		// Aktiviere Undo Button
		undoButton.setEnabled(true);
		// Zeichnet die aktuelle Karte neu
		repaint();
	}
	
	private void undoAction() {
		// Gibt es eine zuletzt gespeicherte Position im Array
		if (undoTilePositions[(((undoArrayPosition - 1) + countOfUndos) % countOfUndos)] != null) {
			// Vermindert die ArrayPosition um 1
			// Wenn ArrayPosition - 1 ist, soll es zu countOfUndos - 1 werden
			undoArrayPosition = (((undoArrayPosition - 1) + countOfUndos) % countOfUndos);
			
			// Macht die letzte Aenderung rueckgaengig
			changeTileValueAtPosition(undoTilePositions[undoArrayPosition], undoTileValues[undoArrayPosition]);
			// Setzt die ausgewaehlte Kachel zurueck
			selectTileByPosition(undoTilePositions[undoArrayPosition]);
			
			// Entfernt die letzte Aenderung aus den Arrays
			undoTileValues[undoArrayPosition] = 0;
			undoTilePositions[undoArrayPosition] = null;
			
			// Wenn es keine weiteren letzten Aenderungen gibt, dann schalte den Button aus
			if (undoTilePositions[(((undoArrayPosition - 1) + countOfUndos) % countOfUndos)] == null)
				undoButton.setEnabled(false);
		}
	}
	
	private void openFile() {
		if (fc.showOpenDialog(dateiLesen) == JFileChooser.APPROVE_OPTION) {
			filePath = fc.getSelectedFile().toString();
			map.readMapByFile(fc.getSelectedFile().toString());
			mapIsDifferentThanOriginal = false;
		} else {
			System.out.println("Lesen abgebrochen");
		}
	}
	
	private void saveFile() {
		if (filePath.length() > 0) {
			map.saveMapToFile(filePath);
			JOptionPane.showMessageDialog(null, "Die Karte wurde erfolgreich gepspeichert unter\n" + filePath);
			mapIsDifferentThanOriginal = false;
		} else {
			saveFileAs();
		}
	}
	
	private void saveFileAs() {
		if (fc.showSaveDialog(dateiSpeichern) == JFileChooser.APPROVE_OPTION) {
			filePath = fc.getSelectedFile().toString();
			map.saveMapToFile(filePath);
			JOptionPane.showMessageDialog(null, "Die Karte wurde erfolgreich gepspeichert unter\n" + filePath);
			mapIsDifferentThanOriginal = false;
		} else {
			System.out.println("Speichern abgebrochen");
		}
	}
	
	private void selectTileByPosition(Point2D point) {
		selected.setLocation(point.getX() - point.getX() % 32, point.getY() - point.getY() % 32);
	}
	
	private int getTileValueByPosition(Point2D point) {
		return map.mapArray[(int) (point.getY() / 32)][(int) (point.getX() / 32)];
	}
	
	private void changeTileValueAtPosition(Point2D point, int newValue) {
		map.mapArray[(int) point.getY() / 32][(int) point.getX() / 32] = newValue;
		// Gibt an, ob Änderungen vorgenommen wurden
		mapIsDifferentThanOriginal = true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Ermittelt die Quelle des Tastendrucks
		Object buttonClicked = e.getSource();

		// Kuemmert sich um die Events der einzelnen Editor Buttons
		if (buttonClicked == undoButton) {
			undoAction();
		} else if (buttonClicked == startMenu) {
			hideEditorWindow();
			houston.changeAppearance("STARTMENU");
		} else if (buttonClicked == dateiLesen) {
			openFile();
		} else if (buttonClicked == dateiSpeichern) {
			saveFile();
		} else if (buttonClicked == dateiSpeichernUnter) {
			saveFileAs();
		} else if (buttonClicked == neuesLevel) {
			System.out.println("neuesLevel");
		} else if (buttonClicked == neueKarte) {
			System.out.println("neueKarte");
		}
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mouseClickPosition.setLocation(e.getX(), e.getY() - 32);
		
		if (mouseClickPosition.getY() >= 0 && drawValue != -1) {
			paintTile(mouseClickPosition);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			if (list.getSelectedIndex() == -1) {
				drawValue = -1;
			} else {
				drawValue = (int) list.getSelectedValue();
			}
		}
	}

}



class ListRenderer extends JLabel implements ListCellRenderer<Object> {

	private static final long serialVersionUID = 1L;
	private Houston houston;
	private Icon icon;
	
	public ListRenderer(Houston houston) {
		this.houston = houston;
		
		setOpaque(true);
		setBorder(new EmptyBorder(2, 10, 2, 10));
		setIconTextGap(10);
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list,
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		
		if (isSelected) {
			setBackground(Color.GRAY);
		} else {
			setBackground(Color.WHITE);
		}
		
		icon = new ImageIcon(houston.map.texture.get(value));
		
		setIcon(icon);
		setText(String.format("%s", value));
		
		return this;
	}
	
}
