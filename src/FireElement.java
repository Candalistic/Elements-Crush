import javax.swing.*;

public class FireElement extends Element {

	public FireElement(int x, int y) {
		super(x, y);
		setType(Element.FIRE);
		setImage(new ImageIcon("images\\fire.png"));
		setWidth(getImage().getIconWidth());
		setHeight(getImage().getIconHeight());

	}

	@Override
	public void animation() {
		setType(Element.EMPTY);
		setImage(new ImageIcon("images\\volcano.png"));
	}

	@Override
	public String toString() {
		return "Fire ";
	}

}
