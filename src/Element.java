import javax.swing.*;

import java.awt.*;

/**
 * <h1>Element</h1>
 * The abstract class for the element. All other elements for the game must be the subclasses of this class.
 * <p>
 * All of the subclasses must implement the abstract method animation
 * <p>
 * Provides the basic set of attributes, that all elements must have.
 * @author Dmitry Ten
 * @since May 2015
 */
public abstract class Element {
	private ImageIcon img;
	private int type, width, height, xPos, yPos;
	public final static int FIRE = 0, WATER = 1, EARTH = 2, WIND = 3, ICE = 4,
			ELECTRICITY = 5, EMPTY = -1;

	public Element(int x, int y) {
		xPos = x;
		yPos = y;
	}

	/**
	 * Draws the image representing the element on the screen
	 * @param g2 instance of a Graphics2D class
	 * @return void
	 * @see Graphics2D
	 */
	public void draw(Graphics2D g2) {
		g2.drawImage(img.getImage(), xPos, yPos, null);
	}

	public abstract void animation();

	public ImageIcon getImage() {
		return img;
	}

	public int getType() {
		return type;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getX() {
		return xPos;
	}

	public int getY() {
		return yPos;
	}

	
	/**
	 * The method for the comparison of two elements.
	 * <p>
	 * Returns whether the integer representation of the of type of this is less than of e.
	 * @param e the element with which comparison is done.
	 * @return int this returns whether type of this is less than type of e.
	 * 
	 */
	public int compareTo(Element e) {
		return this.getType() - e.getType();
	}

	/**
	 * The method for checking whether the types of two elements are equal.
	 * @param e the element with which comparison is done.
	 * @return boolean returns true if types are equal, false otherwise.
	 */
	public boolean equals(Element e) {
		return this.getType() == e.getType();
	}

	public void setImage(ImageIcon i) {
		img = i;
	}

	public void setType(int t) {
		type = t;
	}

	public void setWidth(int w) {
		width = w;
	}

	public void setHeight(int h) {
		height = h;
	}

	public void setX(int x) {
		xPos = x;
	}

	public void setY(int y) {
		yPos = y;
	}

	public Rectangle getRect() {
		return new Rectangle(xPos, yPos, width, height);
	}
}
