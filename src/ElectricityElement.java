import javax.swing.ImageIcon;

public class ElectricityElement extends Element {

	public ElectricityElement(int x, int y) {
		super(x, y);
		setType(Element.ELECTRICITY);
		setImage(new ImageIcon("images\\electricity.png"));
		setWidth(getImage().getIconWidth());
		setHeight(getImage().getIconHeight());

	}

	@Override
	public void animation() {
		setType(Element.EMPTY);
		setImage(new ImageIcon("images\\thunderstorm.png"));

	}
}
