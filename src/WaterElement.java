import javax.swing.*;

public class WaterElement extends Element {
	public WaterElement() {

	}

	public WaterElement(int x, int y) {
		super(x, y);
		setType(Element.WATER);
		setImage(new ImageIcon("images\\water.png"));
		setWidth(getImage().getIconWidth());
		setHeight(getImage().getIconHeight());

	}

	@Override
	public void animation() {
		setType(Element.EMPTY);
		setImage(new ImageIcon("images\\tsunami.png"));

	}
}
