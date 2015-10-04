import javax.swing.*;
import java.awt.*;

public abstract class Element {
	private ImageIcon img;
	private int type, width, height, xPos, yPos;
	public final static int FIRE = 0, WATER = 1, EARTH = 2, WIND = 3, ICE = 4,
			ELECTRICITY = 5, EMPTY = -1;

	public Element() {

	}

	public Element(int x, int y) {
		xPos = x;
		yPos = y;
	}

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

	public int compareTo(Element e) {
		return this.getType() - e.getType();
	}

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
