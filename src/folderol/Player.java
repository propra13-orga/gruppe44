package folderol;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Player {

	private Rectangle2D bounds;
	private Point2D resetPoint;
	private Color color;

	private double speed;
	private double dx;
	private double dy;
	private double newX;
	private double newY;

	public Player() {
		// setze Farbe des Players
		setColor(new Color(0, 255, 64));
		
		// setze Geschwindigkeit des Players
		speed = 128;
		
		// Setze den Player hierhin zurück
		resetPoint = new Point2D.Double(64, 96);
		
		// setze position und größe des Players
		bounds = new Rectangle2D.Double(resetPoint.getX(), resetPoint.getY(), 32, 32);
	}
	
	
	

	public void drawObjects(Graphics2D g) {
		g.setColor(color);
		g.fillRect((int) bounds.getX(), (int) bounds.getY(),
				(int) bounds.getWidth(), (int) bounds.getHeight());
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	void setPosition(double x, double y) {
		bounds.setRect(x, y, bounds.getWidth(), bounds.getHeight());
	}

	void resetPosition() {
		setPosition(resetPoint.getX(), resetPoint.getY());
	}

	void setResetPosition(double x, double y) {
		resetPoint.setLocation(x, y);
	}

	void move(long delta) {
		// Wenn dx oder dy ungleich 0 sind, dann bewege dich
		if (dx != 0 || dy != 0) {
			newX = bounds.getX() + dx * (delta / 1e9);
			newY = bounds.getY() + dy * (delta / 1e9);
			bounds.setRect(newX, newY, bounds.getWidth(), bounds.getHeight());
		}
	}

	void setMovement(double x, double y) {
		dx = x * speed;
		dy = y * speed;
	}

	void setXMovement(double x) {
		dx = x * speed;
	}

	void setYMovement(double y) {
		dy = y * speed;
	}

	double getX() {
		return bounds.getX();
	}

	double getY() {
		return bounds.getY();
	}

	double getWidth() {
		return bounds.getWidth();
	}

	double getHeight() {
		return bounds.getHeight();
	}

}