package MapEditor;

import java.awt.Color;
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

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import Main.Houston;
import Main.Level;
import Main.Map;

public class MapEditor extends JPanel implements ActionListener, MouseListener, MouseMotionListener, ListSelectionListener, ChangeListener {

	private static final long serialVersionUID = 1L;
	private Houston houston;
	private Map map;
	
	private JFrame editorWindowFrame;
	private JButton undoButton, startMenu, 
	openFile, saveFile, saveFileAs, 
	up, down, saveMapUrls, add, del;
	private JTabbedPane tabbedPane;
	private int indexOfSelectedTab;
	public DefaultListModel<Object> mapUrlListModel;
	private JList<Object> currentList;
	private JList<Object> mapUrlList, mapList, enemyList, itemList;
	private JFileChooser fc;
	private int selectedMapUrlIndex = -1;
	
	private int[][] currentArray;
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
			g.drawString("Speicherort ausw\u00e4hlen ...", 16, -19);

		if (!mapIsDifferentThanOriginal)
			g.drawString("Karte ist unver\u00e4ndert.", 16, -5);
		else {
			g.setColor(Color.RED);
			g.drawString("Karten\u00e4nderungen sind noch nicht gespeichert.", 16, -5);
		}

		// Zeichnet die Karte
		map.drawObjects(g);
		
		// Zeichnet Items
		int value;
		for (int row = 0; row < 20; row++) {
			for (int col = 0; col < 24; col++) {
				value = map.itemArray[row][col];
				if (value != 0)
				g.drawImage(houston.itemLogic.texture.get(value), col * 32, row * 32 - houston.itemLogic.texture.get(value).getHeight() + 32 , null);
			}
		}

		g.setColor(Color.GREEN);
		g.drawRect((int) selected.getX(), (int) selected.getY(), 32, 32);
	}
	
	private void resetUndoMashine() {
		undoTileValues = new int[countOfUndos];
		undoTilePositions = new Point2D[countOfUndos];
		undoArrayPosition = 0;
		undoButton.setEnabled(false);		
	}

	public void showEditorWindow() {
		createEditorWindow();
	}
	
	public void hideEditorWindow() {
		editorWindowFrame.dispose();
	}
	
	private void createEditorWindow() {
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
		undoButton = new JButton("R\u00fcckg\u00e4ngig");
		undoButton.addActionListener(this);
		undoButton.setBounds(5, 5, 140, 30);
		editorWindowFrame.add(undoButton);
		
		startMenu = new JButton("Hauptmen\u00fc");
		startMenu.addActionListener(this);
		startMenu.setBounds(155, 5, 140, 30);
		editorWindowFrame.add(startMenu);
		
		// Tabs
		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(0, 40, 300, 340);
		tabbedPane.addChangeListener(this);
		
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
		openFile = new JButton("Karte laden");
		openFile.setBounds(10, 10, 260, 30);
		openFile.addActionListener(this);
		panel1.add(openFile);
		
		saveFile = new JButton("Karte speichern");
		saveFile.setBounds(10, 40, 260, 30);
		saveFile.addActionListener(this);
		panel1.add(saveFile);
		
		saveFileAs = new JButton("Karte speichern unter");
		saveFileAs.setBounds(10, 70, 260, 30);
		saveFileAs.addActionListener(this);
		panel1.add(saveFileAs);
		
		ListCellRenderer<Object> mapUrlListRenderer = new ListRenderer.MapUrlListRenderer(houston);
		mapUrlListModel = new DefaultListModel<>();
		for (int i = 0; i < map.mapUrls.size(); i++) {
			mapUrlListModel.add(i, map.mapUrls.get(i));
		}
		mapUrlList = new JList<Object>(mapUrlListModel);
		mapUrlList.setCellRenderer(mapUrlListRenderer);
		mapUrlList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mapUrlList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		// mapUrlList.setBounds(80, 40, 200, 200);
		mapUrlList.setVisibleRowCount(-1);
		mapUrlList.ensureIndexIsVisible(10);
		mapUrlList.addListSelectionListener(this);
		mapUrlList.addMouseListener(this);
		
		JScrollPane mapUrlScrollPane = new JScrollPane(mapUrlList);
		mapUrlScrollPane.setBounds(10, 120, 260, 140);
		mapUrlScrollPane.setVisible(true);
		panel1.add(mapUrlScrollPane);
		
		up = new JButton("^");
		up.setBounds(10, 265, 30, 20);
		up.addActionListener(this);
		panel1.add(up);
		
		down = new JButton("v");
		down.setBounds(45, 265, 30, 20);
		down.addActionListener(this);
		panel1.add(down);
		
		saveMapUrls = new JButton("Speichern");
		saveMapUrls.setBounds(90, 265, 90, 20);
		saveMapUrls.addActionListener(this);
		panel1.add(saveMapUrls);
		
		add = new JButton("+");
		add.setBounds(230, 265, 30, 20);
		add.addActionListener(this);
		panel1.add(add);
		
		del = new JButton("-");
		del.setBounds(195, 265, 30, 20);
		del.addActionListener(this);
		panel1.add(del);
		
		// Tab #2
		ListCellRenderer<Object> mapListRenderer = new ListRenderer.MapListRenderer(houston);
		mapList = new JList<>(map.texture.keySet().toArray());
		mapList.setCellRenderer(mapListRenderer);
		mapList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mapList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		// mapList.setBounds(80, 40, 200, 200);
		mapList.setVisibleRowCount(-1);
		mapList.ensureIndexIsVisible(10);
		mapList.addListSelectionListener(this);
		
		JScrollPane mapScrollPane = new JScrollPane(mapList);
		mapScrollPane.setBounds(10, 10, 260, 280);
		mapScrollPane.setVisible(true);
		panel2.add(mapScrollPane);
		
		// Tab #3
		ListCellRenderer<Object> enemyListRenderer = new ListRenderer.EnemyListRenderer(houston);
		enemyList = new JList<>();
		enemyList.setCellRenderer(enemyListRenderer);
		enemyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		enemyList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		// enemyList.setBounds(80, 40, 200, 200);
		enemyList.setVisibleRowCount(-1);
		enemyList.ensureIndexIsVisible(10);
		enemyList.addListSelectionListener(this);
		
		JScrollPane enemyScrollPane = new JScrollPane(enemyList);
		enemyScrollPane.setBounds(10, 10, 260, 280);
		enemyScrollPane.setVisible(true);
		panel3.add(enemyScrollPane);
		
		// Tab #4
		ListCellRenderer<Object> itemListRenderer = new ListRenderer.ItemListRenderer(houston);
		itemList = new JList<>(houston.itemLogic.texture.keySet().toArray());
		itemList.setCellRenderer(itemListRenderer);
		itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itemList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		// itemList.setBounds(80, 40, 200, 200);
		itemList.setVisibleRowCount(-1);
		itemList.ensureIndexIsVisible(10);
		itemList.addListSelectionListener(this);
		
		JScrollPane itemScrollPane = new JScrollPane(itemList);
		itemScrollPane.setBounds(10, 10, 260, 280);
		itemScrollPane.setVisible(true);
		panel4.add(itemScrollPane);
		
		
		// Erstellt eine neue blanke Karte
		map.clearMap(0);
		mapIsDifferentThanOriginal = true;
		currentList = mapUrlList;

		// Erstellet Punkte fuer die ClickPosition und die ausgewaehlte Kachel
		mouseClickPosition = new Point2D.Double();
		selected = new Point2D.Double(99999, 99999);

		// Initialisiert die Undo Funktionen
		resetUndoMashine();

		// Dialog zum Auswaehlen von Dateien
		if (fc == null)
			fc = new JFileChooser();
		fc.setCurrentDirectory(new File("./res/maps/"));
		fc.setFileFilter(new FileNameExtensionFilter("Text File", "txt"));
		filePath = new String();
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
		if (fc.showOpenDialog(openFile) == JFileChooser.APPROVE_OPTION) {
			filePath = fc.getSelectedFile().toString();
			map.readMapByFile(filePath);
			resetUndoMashine();
			mapIsDifferentThanOriginal = false;
		} else {
			System.out.println("Lesen abgebrochen");
		}
	}
	
	private void saveFile() {
		if (filePath.length() > 0 && mapIsDifferentThanOriginal) {	
			map.saveMapToFile(filePath);
			JOptionPane.showMessageDialog(null, "Die Karte wurde erfolgreich gepspeichert unter\n" + filePath);
			mapIsDifferentThanOriginal = false;
		} else if (filePath.length() <= 0 && mapIsDifferentThanOriginal) {
			saveFileAs();
		} else {
			// Nichts unternehmen, da die Karte unveraendert ist
		}
	}
	
	private void saveFileAs() {
		if (fc.showSaveDialog(saveFile) == JFileChooser.APPROVE_OPTION) {
			filePath = fc.getSelectedFile().toString();
			map.saveMapToFile(filePath);
			JOptionPane.showMessageDialog(null, "Die Karte wurde erfolgreich gepspeichert unter\n" + filePath);
			mapIsDifferentThanOriginal = false;
		} else {
			System.out.println("Speichern abgebrochen");
		}
	}
	
	private void writeBackMapUrls() {
		map.mapUrls.clear();
		for (int i = 0; i < mapUrlListModel.getSize(); i++) {
			map.mapUrls.add(i, (Level) mapUrlListModel.get(i));
		}
		map.writeBackMapUrls();
	}
	
	private void selectTileByPosition(Point2D point) {
		selected.setLocation(point.getX() - point.getX() % 32, point.getY() - point.getY() % 32);
	}
	
	private int getTileValueByPosition(Point2D point) {
		return map.mapArray[(int) (point.getY() / 32)][(int) (point.getX() / 32)];
	}
	
	private void changeTileValueAtPosition(Point2D point, int newValue) {
		currentArray[(int) point.getY() / 32][(int) point.getX() / 32] = newValue;
		// Gibt an, ob Aenderungen vorgenommen wurden
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
		} else if (buttonClicked == openFile) {
			openFile();
		} else if (buttonClicked == saveFile) {
			saveFile();
		} else if (buttonClicked == saveFileAs) {
			saveFileAs();
		} else if (buttonClicked == up) {
			int selected = selectedMapUrlIndex;
			if (selected > 0 && selected < mapUrlListModel.size()) {
				Object o = mapUrlListModel.remove(selected);
				mapUrlListModel.add(selected - 1, o);
				mapUrlList.setSelectedIndex(selected - 1);
			}
		} else if (buttonClicked == down) {
			int selected = selectedMapUrlIndex;
			if (selected >= 0 && selected < mapUrlListModel.size() - 1) {
				Object o = mapUrlListModel.remove(selected);
				mapUrlListModel.add(selected + 1, o);
				mapUrlList.setSelectedIndex(selected + 1);
			}
		} else if (buttonClicked == saveMapUrls) {
			System.out.println("KartenUrls gespeichert");
			writeBackMapUrls();
		} else if (buttonClicked == add) {
			if (fc.showSaveDialog(saveFile) == JFileChooser.APPROVE_OPTION) {
				String filePath = fc.getSelectedFile().toString();
				Level level = new Level(0, 0, filePath);
				mapUrlListModel.add(mapUrlListModel.size(), level);
			}
		} else if (buttonClicked == del) {
			if (selectedMapUrlIndex >= 0) {
				mapUrlListModel.remove(selectedMapUrlIndex);
			}
		}
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object clickLocation = e.getSource();
		if (clickLocation == this) {
			mouseClickPosition.setLocation(e.getX(), e.getY() - 32);
			
			if (mouseClickPosition.getY() >= 0 && drawValue != -1) {
				paintTile(mouseClickPosition);
			}
		} else if (clickLocation == mapUrlList) {
			JList<Object> list = (JList<Object>) e.getSource();
			int index;
			
			if (e.getClickCount() == 2) {
				index = list.locationToIndex(e.getPoint());
				showMapUrlDialog((Level) mapUrlListModel.get(index));
			}
		}
	}

	private void showMapUrlDialog(Level level) {
		JTextField levelInput = new JTextField(""+level.level, 5);
		JTextField mapInput = new JTextField(""+level.map, 5);
		JTextField pathInput = new JTextField(level.path, 20);
		
		JPanel inputPanel = new JPanel();
		inputPanel.add(new JLabel("Level"));
		inputPanel.add(levelInput);
		inputPanel.add(new JLabel("Map"));
		inputPanel.add(mapInput);
		inputPanel.add(new JLabel("Path"));
		inputPanel.add(pathInput);
		int result = JOptionPane.showConfirmDialog(null, inputPanel, "Eigenschaften des Level bearbeiten", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			level.level = Integer.parseInt(levelInput.getText());
			level.map = Integer.parseInt(mapInput.getText());
			level.path = pathInput.getText();
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
			if (indexOfSelectedTab > 0) {
				if (currentList.getSelectedIndex() == -1)
					drawValue = -1;
				else
					drawValue = (int) currentList.getSelectedValue();
			} else {
				if (currentList.getSelectedIndex() == -1)
					selectedMapUrlIndex = -1;
				else
					selectedMapUrlIndex = currentList.getSelectedIndex();
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		indexOfSelectedTab = tabbedPane.getSelectedIndex();
		
		if (mapList != null && enemyList != null && itemList != null) {
			mapUrlList.clearSelection();
			mapList.clearSelection();
			enemyList.clearSelection();
			itemList.clearSelection();
		}
		drawValue = -1;
		
		switch (indexOfSelectedTab) {
		case 0:
			currentList = mapUrlList;
			break;
		case 1:
			currentList = mapList;
			currentArray = map.mapArray;
			break;
		case 2:
			currentList = enemyList;
			currentArray = map.enemyArray;
			break;
		case 3:
			currentList = itemList;
			currentArray = map.itemArray;
			break;
		default:
			currentList = null;
			currentArray = null;
			break;
		}
	}

}
