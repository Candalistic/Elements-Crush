import javax.swing.*;

public class WindElement extends Element {

	public WindElement(int x, int y) {
		super(x, y);
		setType(Element.WIND);
		setImage(new ImageIcon("images\\wind.png"));
		setWidth(getImage().getIconWidth());
		setHeight(getImage().getIconHeight());

	}

	@Override
	public void animation() {
		setType(Element.EMPTY);
		setImage(new ImageIcon("images\\tornado.png"));
	}
}
