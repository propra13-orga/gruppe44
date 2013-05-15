package folderol;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class Player {

	private Rectangle bounds;
	private Point resetPoint;
	private Color color;

	private int dx;
	private int dy;

	public Player() {
		bounds = new Rectangle();
		resetPoint = new Point(64, 96); // Setze den Spieler hierhin zurück
		setColor(new Color(0, 255, 64));

		bounds.setBounds((int) resetPoint.getX(), (int) resetPoint.getY(), 32, 32);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	void setPosition(int x, int y) {
		bounds.setLocation(x, y);
	}

	void resetPosition() {
		setPosition((int) resetPoint.getX(), (int) resetPoint.getY());
	}
	
	void setResetPosition(double x, double y) {
		resetPoint.setLocation(x, y);
	}

	void move() {
		bounds.setLocation((int) bounds.getX() + dx, (int) bounds.getY() + dy);
	}

	void setMovement(int x, int y) {
		dx = x * 32;
		dy = y * 32;
	}

	void setXMovement(int x) {
		dx = x * 32;
	}

	void setYMovement(int y) {
		dy = y * 32;
	}

	int getX() {
		return (int) bounds.getX();
	}

	int getY() {
		return (int) bounds.getY();
	}

	int getWidth() {
		return (int) bounds.getWidth();
	}

	int getHeight() {
		return (int) bounds.getHeight();
	}

}
