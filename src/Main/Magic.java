package Main;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/** Klasse vom Zauber */
public class Magic extends Movable {

	private BufferedImage texture;
	private Point2D centerPosition;
	private Point2D endPosition;

	/** gibt an, ob der Zauber geloescht werden kann */
	public boolean remove;
	private boolean magicFromPlayer;

	/** gibt den Zaubertyp an; ANALYSIS, LINEAREALGEBRA oder INFORMATIK */
	public String magicType;

	/**
	 * erstellt den Zauber abhaengig von den Parametern
	 * @param texture
	 * @param centerPosition
	 * @param endPosition
	 * @param playerMagic
	 * @param magicType
	 */
	public Magic(BufferedImage texture, Point2D centerPosition, Point2D endPosition, boolean playerMagic, String magicType) {
		this.texture = texture;
		this.centerPosition = centerPosition;
		this.endPosition = endPosition;
		this.setMagicFromPlayer(playerMagic);
		this.magicType = magicType;

		setSpeed(300);

		setBounds(new Rectangle2D.Double(centerPosition.getX() - (texture.getWidth() / 2), centerPosition.getY() - (texture.getHeight() / 2), texture.getWidth(), texture.getHeight()));

		calculateDirection();
	}

	/** @return magicFromPlayer */
	public boolean isMagicFromPlayer() {
		return magicFromPlayer;
	}

	/**
	 * gibt an, dass die Magie vom Spieler ist
	 * @param magicFromPlayer
	 */
	public void setMagicFromPlayer(boolean magicFromPlayer) {
		this.magicFromPlayer = magicFromPlayer;
	}

	/**
	 * legt den Magietyp fest
	 * @param magicType
	 */
	public void setMagicType(String magicType){
		this.magicType = magicType;
	}

	/** Startposition des Zaubers beim Erzeuger
	 * @see Main.Movable#move(double, double)
	 */
	@Override
	public void move(double dX, double dY) {
		super.move(dX, dY);
		centerPosition = getCenterPosition();
	}


	/** Zeichnet den Zauber
	 * @see Main.Movable#drawObjects(java.awt.Graphics2D)
	 */
	@Override
	public void drawObjects(Graphics2D g) {
		g.drawImage(texture, (int) getBounds().getX(), (int) getBounds().getY(), null);
	}

	private void calculateDirection() {
		double adjacent = endPosition.getX() - centerPosition.getX();
		double opposite = endPosition.getY() - centerPosition.getY();
		double hypotenuse = Math.sqrt(adjacent * adjacent + opposite * opposite);

		// sin() = Ankathete/Hypotenuse = x-Koordinate auf dem Einheitskreis;
		double x_multiplier = adjacent / hypotenuse;
		// cos() = Gegenkathete/Hypotenuse = y-Koordinate auf dem Einheitskreis
		double y_multiplier = opposite / hypotenuse;

		if (x_multiplier <= 0) {
			setLeft((int) (-100 * x_multiplier));
		} else {
			setRight((int) (100 * x_multiplier));
		}
		if (y_multiplier <= 0) {
			setUp((int) (-100 * y_multiplier));
		} else {
			setDown((int) (100 * y_multiplier));
		}
	}

	/** Zauber wird entfernt, wenn er auf eine Wand trifft
	 * @see Main.Movable#onWallHit()
	 */
	@Override
	public void onWallHit() {
		remove = true;
	}

}
