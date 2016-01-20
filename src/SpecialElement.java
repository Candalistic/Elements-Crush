import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class SpecialElement extends Element {
	public SpecialElement(int x, int y, int type) {
		super(x, y);
		setType(type);
		switch (type) {
		case Element.FIRE:
			setImage(new ImageIcon("images\\fireS.png"));
			break;
		case Element.WATER:
			setImage(new ImageIcon("images\\waterS.png"));
			break;
		case Element.WIND:
			setImage(new ImageIcon("images\\windS.png"));
			break;
		case Element.EARTH:
			setImage(new ImageIcon("images\\earthS.png"));
			break;
		case Element.ICE:
			setImage(new ImageIcon("images\\iceS.png"));
			break;
		case Element.ELECTRICITY:
			setImage(new ImageIcon("images\\electricityS.png"));
			break;
		}
		setWidth(getImage().getIconWidth());
		setHeight(getImage().getIconHeight());
	}

	@Override
	public void animation() {
		try {
			int type = getType();
			setType(Element.EMPTY);
			InputStream in = null;
			switch (type) {
			case Element.FIRE:
				in = new FileInputStream("music\\fire-01.wav");
				break;
			case Element.WATER:
				in = new FileInputStream("music\\rain-drips-01.wav");
				break;
			case Element.WIND:
				in = new FileInputStream("music\\wind-howl-03.wav");
				break;
			case Element.EARTH:
				in = new FileInputStream("music\\earthquake.wav");
				break;
			case Element.ICE:
				in = new FileInputStream("music\\ice-cracking-02.wav");
				break;
			case Element.ELECTRICITY:
				in = new FileInputStream("music\\thunder-01.wav");
				break;
			default:
				in = null;
				break;
			}
			if (in != null) {
				AudioStream audioStream = new AudioStream(in);
				AudioPlayer.player.start(audioStream);
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	@Override
	public String toString() {
		return "Special";
	}
	
	
}
