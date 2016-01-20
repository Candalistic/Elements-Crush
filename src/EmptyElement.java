
public class EmptyElement extends Element {

	public EmptyElement(int x, int y) {
		super(x, y);
		this.setType(EMPTY);
	}

	@Override
	public void animation(){
		// This should never be reached by a program
		try {
			throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "Empty";
	}

}
