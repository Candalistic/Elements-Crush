import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * <h1>public class Grid</h1>
 * The implementation of a class responsible for creating, keeping and managing the gaming grid.
 * <p>
 * Also keeps track of the score.
 * @author Dmitry Ten
 * @since May 2015
 */
public class Grid {
	private int cols, rows;
	private int score;
	private Element[][] elements;
	private Random rnd;

	/**
	 * The default constructor of the class. Creates the grid of the size 10x10.
	 */
	public Grid() {
		rnd = new Random();
		cols = 10;
		rows = 10;
		score = 0;
		elements = new Element[10][10];
		fillGrid();
	}

	/**
	 * The constructor of the class. Creates grid with the specified number of rows and columns.
	 * @param r the number of rows in the grid.
	 * @param c the number of columns in the grid.
	 */
	public Grid(int r, int c) {
		rnd = new Random();
		cols = c;
		rows = r;
		score = 0;
		elements = new Element[r][c];
		fillGrid();
	}

	public void draw(Graphics2D g2) {
		for (Element[] r : elements)
			for (Element e : r) {
				e.draw(g2);
			}
	}

	/** 
	 * <h1>fillGrid</h1>
	 * A method that fills every cell of the random elements.
	 * First, every row is randomly filled using the method fillRow, then every cell is checked to verify
	 * that there are no 3 elements of the same type in the row initially.
	 * @return void 
	 * @see fillRow(), Element
	 */
	public void fillGrid() {
		for(int i=0; i< rows; i++)
			for(int j=0; j<cols; j++)
				elements[i][j] = new EmptyElement(0,0);
		int x = 400;
		int y = 150;
		// filling the grid element by element
		for(int i=0; i< rows; i++){
			for(int j=0; j< cols; j++){
				ArrayList<Integer> options = getOptions(i, j);
				while(options.contains(Element.EMPTY))
					options.remove(Integer.valueOf(Element.EMPTY));
				int choice = rnd.nextInt(options.size());
				generateElement(x, y, options.get(choice));
				x+=50;
			}
			y+=50;
		}
	}
	
	/**
	 * A helper method for fillGrid(). Returns the ArrayList with identifiers of legal elements
	 * <p>
	 * for the specified cell in the grid.
	 * @param i the row in the grid.
	 * @param j the column in the grid.
	 * @return ArrayList<Integer> The list of identifiers for allowed elements.
	 */
	private ArrayList<Integer> getOptions(int i, int j){
		ArrayList<Integer> options = new ArrayList<Integer>();
		options.add(Element.EARTH);
		options.add(Element.ELECTRICITY);
		options.add(Element.FIRE);
		options.add(Element.ICE);
		options.add(Element.WATER);
		options.add(Element.WIND);
		int length_north, length_south, length_west, length_east;
		if(i<2)
			length_north = i;
		else
			length_north = 2;
		
		if(rows - i - 1 < 2)
			length_south = rows - i - 1;
		else
			length_south = 2;
		
		if(j<2)
			length_west = j;
		else
			length_west = 2;
		
		if(cols - j - 1 < 2)
			length_east = cols - j - 1;
		else
			length_east = 2;
		
		int index = 0;
		while(index < options.size()){
			for(int row_index = i-length_west; row_index < i+length_east; row_index++)
				if(isCompleteSequence(elements[row_index][j], elements[row_index-1][j], elements[row_index-2][j])){
					options.set(index, Element.EMPTY);
					break;
				}
			index++;
			}
		index = 0;
		while(index < options.size()){
				for(int col_index = i-length_north; col_index < i+length_south; col_index++)
					if(isCompleteSequence(elements[col_index][j], elements[col_index-1][j], elements[col_index-2][j])){
						options.set(index, Element.EMPTY);
						break;
					}
			index++;
		}
		
		for(Integer el: options)
			System.out.println(el);
			
		return options;
	}
	/**
	 * A helper method for fillGrid(). Determines whether the given 3 elements are of the same type.
	 * @param e1 the first element
	 * @param e2 the second element
	 * @param e3 the third element
	 * @return boolean <p>
	 * Returns true if elements are of the same type, false otherwise.
	 */
	private boolean isCompleteSequence(Element e1, Element e2, Element e3){
		return e1.equals(e2) && e1.equals(e3);
	}

	/**
	 * Fills the specified row with random elements.
	 * @param r This is the row which will be filled.
	 * @return void
	 * @see Element
	 */
	public void fillRow(int r) {
		int x = 400;
		for (int j = 0; j < cols; j++) {
			elements[r][j] = generateElement(x, 150 + r * 50);
			while (j >= 2 && elements[r][j].equals(elements[r][j - 1])
					&& elements[r][j].equals(elements[r][j - 2])){
				elements[r][j] = generateElement(x, 150 + r * 50);
			}
			x += 50;
		}
	}

	/**
	 * Fills the specified row with the elements of specified type.
	 * @param r This is the row which will be filled.
	 * @param t This is the type of element with which the row will be filled. For the possible types,
	 * see Element
	 * @return void
	 * @see Element
	 */
	public void fillRow(int r, int t) {
		int x = 400;
		int y = 150 + r * 50;
		for (int i = 0; i < cols; i++) {
			switch (t) {
			case Element.FIRE:
				elements[r][i] = new FireElement(x, y);
				break;
			case Element.WATER:
				elements[r][i] = new WaterElement(x, y);
				break;
			case Element.EARTH:
				elements[r][i] = new EarthElement(x, y);
				break;
			case Element.WIND:
				elements[r][i] = new WindElement(x, y);
				break;
			case Element.ICE:
				elements[r][i] = new IceElement(x, y);
				break;
			default:
				elements[r][i] = new ElectricityElement(x, y);
				break;
			}
			x += 50;
		}
	}

	/**
	 * Fills the specified column with random elements.
	 * @param c This is the column which will be filled.
	 * @return void
	 * @see Element
	 */
	public void fillCol(int c) {
		int x = 400 + c * 50;
		int y = 150;
		for (int i = 0; i < rows; i++) {
			elements[i][c] = generateElement(x, y);
			while (i >= 2 && elements[i - 1][c].equals(elements[i][c])
					&& elements[i - 2][c].equals(elements[i][c]))
				elements[i][c] = generateElement(elements[i][c].getX(),
						elements[i][c].getY());
			y += 50;
		}
	}

	/**
	 * Fills the specified column with the elements of specified type.
	 * @param r This is the column which will be filled.
	 * @param t This is the type of element with which the column will be filled. For the possible types,
	 * see Element
	 * @return void
	 * @see Element
	 */
	public void fillCol(int c, int t) {
		int x = 400 + c * 50;
		int y = 150;
		for (int i = 0; i < rows; i++) {
			switch (t) {
			case Element.FIRE:
				elements[i][c] = new FireElement(x, y);
				break;
			case Element.WATER:
				elements[i][c] = new WaterElement(x, y);
				break;
			case Element.EARTH:
				elements[i][c] = new EarthElement(x, y);
				break;
			case Element.WIND:
				elements[i][c] = new WindElement(x, y);
				break;
			case Element.ICE:
				elements[i][c] = new IceElement(x, y);
				break;
			default:
				elements[i][c] = new ElectricityElement(x, y);
				break;
			}
			y += 50;
		}
	}

	/**
	 * Method that shuffles the grid.
	 * <p>
	 * @return void
	 * @see Element, Collections
	 */
	public void reassign() {
		// First, copy every value of elements into the one dimensional array.
		ArrayList<Element> temp_list = new ArrayList<>(rows*cols);
		for(Element[] row: elements)
			for(Element el: row){
				temp_list.add(el);
			}
		// Shuffle the resulting array.
		Collections.shuffle(temp_list);
		// Refill the grid.
		int index = 0;
		for(int i=0; i<rows; i++)
			for(int j=0; j<cols; j++){
				elements[i][j] = temp_list.get(index);
				index++;
			}
	}

	/**
	 * Creates and returns the element of a random type.
	 * @param x this is the row index for which element will be generated.
	 * @param y this is the column index for which element will be generated.
	 * @return Element the generated element.
	 */
	public Element generateElement(int x, int y) {
		int choice = rnd.nextInt(6);
		Element e;
		switch (choice) {
		case Element.FIRE:
			e = new FireElement(x, y);
			break;
		case Element.WATER:
			e = new WaterElement(x, y);
			break;
		case Element.EARTH:
			e = new EarthElement(x, y);
			break;
		case Element.WIND:
			e = new WindElement(x, y);
			break;
		case Element.ICE:
			e = new IceElement(x, y);
			break;
		default:
			e = new ElectricityElement(x, y);
			break;
		}
		return e;
	}
	
	/**
	 * Creates and returns the element of a specified type.
	 * @param x this is the row index for which element will be generated.
	 * @param y this is the column index for which element will be generated.
	 * @param t type of the element to be generated.
	 * @return Element the generated element.
	 * @see Element
	 */
	public Element generateElement(int x, int y, int t) {
		int choice = t;
		Element e;
		switch (choice) {
		case Element.FIRE:
			e = new FireElement(x, y);
			break;
		case Element.WATER:
			e = new WaterElement(x, y);
			break;
		case Element.EARTH:
			e = new EarthElement(x, y);
			break;
		case Element.WIND:
			e = new WindElement(x, y);
			break;
		case Element.ICE:
			e = new IceElement(x, y);
			break;
		default:
			e = new ElectricityElement(x, y);
			break;
		}
		return e;
	}

	// finding all lines for deletion
	public boolean findLine() {
		boolean isLine = false;
		int vLength = 1, hLength = 1; // length of vertical and horizontal lines
		int iStart = 0, iEnd = 0, jStart = 0, jEnd = 0;
		for (int i = 0; i < rows; i++)
			for (int j = 2; j < cols; j++)
				if (elements[i][j].equals(elements[i][j - 1])
						&& elements[i][j].equals(elements[i][j - 2])
						&& elements[i][j].getType() != -1) {
					isLine = true;
					jStart = j - 2;
					jEnd = j;
					while (jEnd < cols
							&& elements[i][jEnd].equals(elements[i][j]))
						jEnd++;
					hLength = jEnd - jStart;
					jEnd--;
					if (hLength > 3) {
						// creating a special element at the end of the line
						Element sub = elements[i][jEnd];
						int subT = sub.getType();
						if (elements[i][jEnd].getClass().getSimpleName() == "SpecialElement")
							specialElementAction(i, jEnd,
									elements[i][jEnd].getType());
						elements[i][jEnd] = new SpecialElement(sub.getX(),
								sub.getY(), subT);
						jEnd--;
					}
					for (int k = jStart; k <= jEnd; k++) {
						iStart = i;
						iEnd = i;
						if (i > 1 && elements[i][k].equals(elements[i - 1][k])
								&& elements[i][k].equals(elements[i - 2][k]))
							iStart = i - 2;
						if (i > 0 && i < rows - 1
								&& elements[i][k].equals(elements[i + 1][k])
								&& elements[i][k].equals(elements[i - 1][k])) {
							if (i - 1 < iStart)
								iStart = i - 1;
							iEnd = i + 1;
						}
						if (i < rows - 2
								&& elements[i][k].equals(elements[i + 1][k])
								&& elements[i][k].equals(elements[i + 2][k]))
							iEnd = i + 2;
						for (int s = iStart; s <= iEnd; s++) {
							if (elements[s][k].getClass().getSimpleName()
									.equals("SpecialElement"))
								specialElementAction(s, k,
										elements[s][k].getType());
							else
								elements[s][k].animation();
						}
					}
					for (int k = jStart; k <= jEnd; k++) {
						if (elements[i][k].getClass().getSimpleName()
								.equals("SpecialElement")) {
							specialElementAction(i, k, elements[i][k].getType());
						} else
							elements[i][k].animation();
					}
				}

		for (int j = 0; j < cols; j++)
			for (int i = 2; i < rows; i++) {
				if (elements[i][j].equals(elements[i - 1][j])
						&& elements[i][j].equals(elements[i - 2][j])
						&& elements[i][j].getType() != -1) {
					isLine = true;
					iStart = i - 2;
					iEnd = i + 1;
					while (iEnd < rows
							&& elements[iEnd][j].equals(elements[i][j]))
						iEnd++;
					vLength = iEnd - iStart;
					iEnd--;

					if (vLength > 3) {
						Element sub = elements[iEnd][j];
						int subT = sub.getType();
						if (sub.getClass().getSimpleName() == "SpecialElement")
							specialElementAction(iEnd, j, sub.getType());
						elements[iEnd][j] = new SpecialElement(sub.getX(),
								sub.getY(), subT);
						iEnd--;
					}
					for (int k = iStart; k <= iEnd; k++) {
						jStart = j;
						jEnd = j;
						if (j > 1 && elements[k][j].equals(elements[k][j - 1])
								&& elements[k][j].equals(elements[k][j - 2]))
							jStart = j - 2;
						if (j > 0 && j < cols - 1
								&& elements[k][j].equals(elements[k][j - 1])
								&& elements[i][j].equals(elements[k][j + 1])) {
							if (j - 1 < jStart)
								jStart = j - 1;
							jEnd = j + 1;
						}
						if (j < cols - 2
								&& elements[k][j].equals(elements[k][j + 1])
								&& elements[k][j].equals(elements[k][j + 2]))
							jEnd = j + 2;
						for (int s = jStart; s <= jEnd; s++) {
							if (elements[k][s].getClass().getSimpleName()
									.equals("SpecialElement"))
								specialElementAction(k, s,
										elements[k][s].getType());
							else
								elements[k][s].animation();
						}
					}
					for (int k = iStart; k <= iEnd; k++) {
						if (elements[k][j].getClass().getSimpleName()
								.equals("SpecialElement")) {
							specialElementAction(k, j, elements[k][j].getType());
						} else
							elements[k][j].animation();
					}
				}

			}

		if (hLength != 1)
			score += hLength;
		if (vLength != 1)
			score += vLength;
		return isLine;
	}

	public boolean findLine(int iMoved1, int jMoved1, int iMoved2, int jMoved2) {
		boolean isLine = false;
		boolean specialElement = false;
		int vLength = 1, hLength = 1;
		int iStart = 0, iEnd = 0, jStart = 0, jEnd = 0, iMoved = -1, jMoved = -1;

		for (int i = 0; i < rows; i++)
			for (int j = 2; j < cols; j++)
				// checking if there is a line
				if (elements[i][j].equals(elements[i][j - 1])
						&& elements[i][j].equals(elements[i][j - 2])
						&& elements[i][j].getType() != -1) {
					// the line is found
					isLine = true;
					jStart = j - 2;
					jEnd = j;
					// finding which one of the two elements that were swapped
					// is in the line
					if (elements[i][j].getType() == elements[iMoved1][jMoved1]
							.getType()) {
						iMoved = iMoved1;
						jMoved = jMoved1;
					} else {
						iMoved = iMoved2;
						jMoved = jMoved2;
					}
					// finding the last index of the line
					while (jEnd < cols
							&& elements[i][jEnd].equals(elements[i][j]))
						jEnd++;
					hLength = jEnd - jStart;
					jEnd--;
					if (hLength > 3) {
						// creating special element at the place where player
						// has made a swap
						specialElement = true;
						Element sub = elements[iMoved][jMoved];
						int subT = sub.getType();
						if (elements[iMoved][jMoved].getClass().getSimpleName() == "SpecialElement") {
							specialElementAction(iMoved, jMoved,
									elements[iMoved][jMoved].getType());
						}
						elements[iMoved][jMoved] = new SpecialElement(
								sub.getX(), sub.getY(), subT);
					}
					// finding any additional vertical line
					iStart = i;
					iEnd = i;
					if (iMoved > 1
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved - 1][jMoved])
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved - 2][jMoved]))
						iStart = iMoved - 2;
					if (iMoved > 0
							&& iMoved < rows - 1
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved + 1][jMoved])
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved - 1][jMoved])) {
						if (iMoved - 1 < iStart)
							iStart = iMoved - 1;
						iEnd = iMoved + 1;
					}
					if (iMoved < rows - 2
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved + 1][jMoved])
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved + 2][jMoved]))
						iEnd = iMoved + 2;

					vLength = iEnd - iStart + 1;
					if (vLength >= 3) {
						if (iMoved != -1 && jMoved != -1) {
							specialElement = true;
							Element sub = elements[iMoved][jMoved];
							elements[iMoved][jMoved] = new SpecialElement(
									sub.getX(), sub.getY(),
									elements[iMoved][jMoved].getType());
						}
					}
					for (int s = iStart; s <= iEnd; s++) {
						if (elements[s][jMoved].getClass().getSimpleName()
								.equals("SpecialElement")) {
							if (!specialElement) {
								specialElementAction(s, jMoved,
										elements[s][jMoved].getType());
							}
						} else
							elements[s][jMoved].animation();
					}
					for (int k = jStart; k <= jEnd; k++) {
						if (elements[i][k].getClass().getSimpleName()
								.equals("SpecialElement")) {
							if (!specialElement || jMoved != k) {
								specialElementAction(i, k,
										elements[i][k].getType());
							}
						} else
							elements[i][k].animation();
					}
				}

		for (int j = 0; j < cols; j++)
			for (int i = 2; i < rows; i++) {
				if (elements[i][j].equals(elements[i - 1][j])
						&& elements[i][j].equals(elements[i - 2][j])
						&& elements[i][j].getType() != -1) {
					isLine = true;
					iStart = i - 2;
					iEnd = i + 1;
					if (elements[i][j].getType() == elements[iMoved1][jMoved1]
							.getType()) {
						iMoved = iMoved1;
						jMoved = jMoved1;
					} else {
						iMoved = iMoved2;
						jMoved = jMoved2;
					}
					while (iEnd < rows
							&& elements[iEnd][j].equals(elements[i][j]))
						iEnd++;
					vLength = iEnd - iStart;
					iEnd--;

					if (vLength > 3) {
						specialElement = true;
						Element sub = elements[iMoved][jMoved];
						int subT = sub.getType();
						if (sub.getClass().getSimpleName() == "SpecialElement"
								&& !specialElement) {
							specialElementAction(iMoved, jMoved, sub.getType());
						}
						elements[iMoved][jMoved] = new SpecialElement(
								sub.getX(), sub.getY(), subT);
					}

					jStart = jMoved;
					jEnd = jMoved;
					if (jMoved > 1
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved][jMoved - 1])
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved][jMoved - 2]))
						jStart = jMoved - 2;
					if (jMoved > 0
							&& jMoved < cols - 1
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved][jMoved - 1])
							&& elements[i][jMoved]
									.equals(elements[iMoved][jMoved + 1])) {
						if (jMoved - 1 < jStart)
							jStart = jMoved - 1;
						jEnd = jMoved + 1;
					}
					if (jMoved < cols - 2
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved][jMoved + 1])
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved][jMoved + 2]))
						jEnd = jMoved + 2;
					hLength = jEnd - jStart + 1;
					if (hLength >= 3) {
						if (iMoved != -1 && jMoved != -1) {
							specialElement = true;
							Element sub = elements[iMoved][jMoved];
							elements[iMoved][jMoved] = new SpecialElement(
									sub.getX(), sub.getY(),
									elements[iMoved][jMoved].getType());
						}
					}
					for (int s = jStart; s <= jEnd; s++) {
						if (elements[iMoved][s].getClass().getSimpleName()
								.equals("SpecialElement")) {
							if (!specialElement || jMoved != s) {
								specialElementAction(iMoved, s,
										elements[iMoved][s].getType());
							}
						} else
							elements[iMoved][s].animation();
					}

					for (int k = iStart; k <= iEnd; k++) {
						if (elements[k][j].getClass().getSimpleName()
								.equals("SpecialElement")) {
							if (!specialElement || iMoved != k) {
								specialElementAction(k, j,
										elements[k][j].getType());
							}
						} else
							elements[k][j].animation();
					}
				}
			}

		if (hLength != 1)
			score += hLength;
		if (vLength != 1)
			score += vLength;
		return isLine;
	}

	public Element getElement(int x, int y) {
		return elements[x][y];
	}

	public int getNumRows() {
		return rows;
	}

	public int getNumCols() {
		return cols;
	}

	public void setElement(int x, int y, Element e) {
		elements[x][y] = e;
	}

	public void specialElementAction(int i, int j, int t) {
		switch (t) {
		case Element.EARTH:
			for (int p = 0; p < cols; p++) {
				if (elements[i][p].getClass().getSimpleName() != "SpecialElement")
					elements[i][p] = new EarthElement(elements[i][p].getX(),
							elements[i][p].getY());
				else if (p != j) {
					specialElementAction(i, p, elements[i][p].getType());
				}
				elements[i][p].animation();
			}
			score += cols;
			break;
		case Element.ELECTRICITY:
			elements[i][j].animation();
			Random rnd = new Random();
			for (int p = 0; p < 8; p++) {
				int x1 = rnd.nextInt(rows);
				int y1 = rnd.nextInt(cols);
				Element tmp = elements[x1][y1];
				if (tmp.getClass().getSimpleName() != "SpecialElement")
					elements[x1][y1] = new ElectricityElement(tmp.getX(),
							tmp.getY());
				else if (x1 != i && y1 != j) {
					specialElementAction(x1, y1, tmp.getType());
				}
				elements[x1][y1].animation();

			}
			score += 8;
			break;
		case Element.FIRE:
			elements[i][j].animation();
			if (i > 0) {
				if (elements[i - 1][j].getClass().getSimpleName() != "SpecialElement") {
					int x1 = elements[i - 1][j].getX();
					int y1 = elements[i - 1][j].getY();
					Element tmp = new FireElement(x1, y1);
					elements[i - 1][j] = tmp;
					score++;
				} else
					specialElementAction(i - 1, j, elements[i - 1][j].getType());
				elements[i - 1][j].animation();
				if (j > 0) {
					if (elements[i - 1][j - 1].getClass().getSimpleName() != "SpecialElement") {
						int x2 = elements[i - 1][j - 1].getX();
						int y2 = elements[i - 1][j - 1].getY();
						Element tmp = new FireElement(x2, y2);
						elements[i - 1][j - 1] = tmp;
						score++;
					} else
						specialElementAction(i - 1, j - 1,
								elements[i - 1][j - 1].getType());
					elements[i - 1][j - 1].animation();
				}
				if (j < cols - 1) {
					if (elements[i - 1][j + 1].getClass().getSimpleName() != "SpecialElement") {
						int x2 = elements[i - 1][j + 1].getX();
						int y2 = elements[i - 1][j + 1].getY();
						Element tmp2 = new FireElement(x2, y2);
						elements[i - 1][j + 1] = tmp2;
						score++;
					} else
						specialElementAction(i - 1, j + 1,
								elements[i - 1][j + 1].getType());
					elements[i - 1][j + 1].animation();
				}
			}
			if (i < rows - 1) {
				if (elements[i + 1][j].getClass().getSimpleName() != "SpecialElement") {
					int x1 = elements[i + 1][j].getX();
					int y1 = elements[i + 1][j].getY();
					Element tmp = new FireElement(x1, y1);
					elements[i + 1][j] = tmp;
					score++;
				} else
					specialElementAction(i + 1, j, elements[i + 1][j].getType());
				elements[i + 1][j].animation();
				if (j > 0) {
					if (elements[i + 1][j - 1].getClass().getSimpleName() != "SpecialElement") {
						int x2 = elements[i + 1][j - 1].getX();
						int y2 = elements[i + 1][j - 1].getY();
						Element tmp2 = new FireElement(x2, y2);
						elements[i + 1][j - 1] = tmp2;
						score++;
					} else
						specialElementAction(i + 1, j - 1,
								elements[i + 1][j - 1].getType());
					elements[i + 1][j - 1].animation();
				}
				if (j < cols - 1) {
					if (elements[i + 1][j + 1].getClass().getSimpleName() != "SpecialElement") {
						int x2 = elements[i + 1][j + 1].getX();
						int y2 = elements[i + 1][j + 1].getY();
						Element tmp2 = new FireElement(x2, y2);
						elements[i + 1][j + 1] = tmp2;
						score++;
					} else
						specialElementAction(i + 1, j + 1,
								elements[i + 1][j + 1].getType());
					elements[i + 1][j + 1].animation();
				}
			}
			if (j > 0) {
				if (getElement(i, j - 1).getClass().getSimpleName() != "SpecialElement") {
					int x1 = getElement(i, j - 1).getX();
					int y1 = getElement(i, j - 1).getY();
					Element tmp = new FireElement(x1, y1);
					setElement(i, j - 1, tmp);
					score++;
				} else
					specialElementAction(i, j - 1, elements[i][j - 1].getType());
				getElement(i, j - 1).animation();
			}
			if (j < cols - 1) {
				if (getElement(i, j + 1).getClass().getSimpleName() != "SpecialElement") {
					int x1 = getElement(i, j + 1).getX();
					int y1 = getElement(i, j + 1).getY();
					Element tmp = new FireElement(x1, y1);
					setElement(i, j + 1, tmp);
					score++;
				} else
					specialElementAction(i, j + 1, elements[i][j + 1].getType());
				getElement(i, j + 1).animation();
			}
			break;
		case Element.ICE:
			for (int p = 0; p < rows; p++) {
				if (elements[p][j].getClass().getSimpleName() != "SpecialElement")
					elements[p][j] = new IceElement(elements[p][j].getX(),
							elements[p][j].getY());
				else if (p != i) {
					specialElementAction(p, j, elements[p][j].getType());
				}

				elements[p][j].animation();
			}

			score += rows;
			break;
		case Element.WATER:
			for (int p = 0; p < cols; p++) {
				if (elements[i][p].getClass().getSimpleName() != "SpecialElement")
					elements[i][p] = new WaterElement(elements[i][p].getX(),
							elements[i][p].getY());
				else if (p != j) {
					specialElementAction(i, p, elements[i][p].getType());
				}

				elements[i][p].animation();
			}
			score += cols;
			break;
		case Element.WIND:
			for (int p = 0; p < rows; p++) {
				if (elements[p][j].getClass().getSimpleName() != "SpecialElement")
					elements[p][j] = new WindElement(elements[p][j].getX(),
							elements[p][j].getY());
				else if (p != i) {
					specialElementAction(p, j, elements[p][j].getType());
				}

				elements[p][j].animation();
			}
			score += rows;
			break;
		}
	}

	/**
	 * A getter method for the score.
	 * @return int returns the score.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * A setter method for the score.
	 * @param s int 
	 */
	public void setScore(int s) {
		score = s;
	}
}
