package MapEditor;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import Main.Houston;

public class ListRenderer {

	static class MapListRenderer extends JLabel implements ListCellRenderer<Object> {

		private static final long serialVersionUID = 1L;
		private Houston houston;
		private Icon icon;
		
		public MapListRenderer(Houston houston) {
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
			setText(houston.map.textureName.get(value));
			
			return this;
		}
		
	}

	static class EnemyListRenderer extends JLabel implements ListCellRenderer<Object> {

		private static final long serialVersionUID = 1L;
		private Houston houston;
		private Icon icon;
		
		public EnemyListRenderer(Houston houston) {
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
			setText(houston.map.textureName.get(value));
			
			return this;
		}
		
	}

	static class ItemListRenderer extends JLabel implements ListCellRenderer<Object> {

		private static final long serialVersionUID = 1L;
		private Houston houston;
		private Icon icon;
		
		public ItemListRenderer(Houston houston) {
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
			
			icon = new ImageIcon(houston.itemLogic.texture.get(value));
			
			setIcon(icon);
			setText(houston.itemLogic.itemName.get(value));
			
			return this;
		}
		
	}
	
}