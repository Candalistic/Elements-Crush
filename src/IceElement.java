import javax.swing.*;

public class IceElement extends Element {

	public IceElement(int x, int y) {
		super(x, y);
		setType(Element.ICE);
		setImage(new ImageIcon("images\\ice.png"));
		setWidth(getImage().getIconWidth());
		setHeight(getImage().getIconHeight());

	}

	@Override
	public void animation() {
		setType(Element.EMPTY);
		setImage(new ImageIcon("images\\snowstorm.png"));

	}
}
