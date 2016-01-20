import javax.swing.*;

public class EarthElement extends Element {

	public EarthElement(int x, int y) {
		super(x, y);
		setType(Element.EARTH);
		setImage(new ImageIcon("images\\earth.png"));
		setWidth(getImage().getIconWidth());
		setHeight(getImage().getIconHeight());

	}

	@Override
	public void animation() {
		setType(Element.EMPTY);
		setImage(new ImageIcon("images\\earthquake.png"));

	}
	
	@Override
	public String toString(){
		return "Earth";
	}
}
