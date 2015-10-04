import javax.swing.*;
import javax.swing.Timer;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class ElementsCrushGame extends JPanel implements ActionListener {
	private Rectangle r;
	private JButton btnStart, btnHelp, btnHighscore, btnStop;
	private JLabel lblScore, lblGameControl, lblHighscore;
	private Grid grid;
	private Element firstElement, currentElement;
	private String highscore0 = "HIGHSCORE: ", highscore1 = "HIGHSCORE: ";
	private String[] highscoreClassic = new String[11];
	private String[] highscoreTime = new String[11];
	private int turnsRemaining;
	private int timeRemaining;
	private int gameMode = -1;
	private int delay;
	private boolean checkDraw = false;
	private Timer gameTimer, moveTimer;
	private ArrayList<Element> elementsToMove = new ArrayList<Element>();
	private ArrayList<Integer> yEnd = new ArrayList<Integer>();
	private File fileHighscoreClassic, fileHighscoreTime;

	public static void main(String[] args) {
		new ElementsCrushGame();
	}

	ElementsCrushGame() {
		// set up rectangle for mouse
		r = new Rectangle(1, 1);

		// set up timers
		moveTimer = new Timer(5, this);
		gameTimer = new Timer(1000, this);
		// set up grid
		grid=new Grid(9,9);

		// set up files with highscores
		fileHighscoreClassic = new File("highscores\\classicHighscore.txt");
		fileHighscoreTime = new File("highscores\\timeHighscore.txt");
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					fileHighscoreClassic));
			highscore0 = in.readLine();
			String line = "";
			highscoreClassic[0] = highscore0;
			int i = 1;
			while (line != null) {
				line = in.readLine();
				highscoreClassic[i] = line;
				if (i < 10)
					i++;
			}
			in.close();
		} catch (IOException e) {
			System.err.println(e);
			highscore0 = "HIGHSCORE: N/A";
		}

		try {
			BufferedReader in = new BufferedReader(new FileReader(
					fileHighscoreTime));
			highscore1 = in.readLine();
			String line = "";
			highscoreTime[0] = highscore1;
			int i = 1;
			while (line != null) {
				line = in.readLine();
				highscoreTime[i] = line;
				if (i < 10)
					i++;
			}
			in.close();
		} catch (IOException e) {
			System.err.println(e);
			highscore1 = "HIGHSCORE: N/A";
		}

		// set up buttons
		btnStart = new JButton("START");
		btnStart.setBackground(new Color(30, 144, 255));
		btnStart.setForeground(Color.WHITE);
		btnStart.setFont(new Font("Tahoma", Font.BOLD, 26));
		btnStart.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		btnStart.setFocusable(false);
		btnStart.setBounds(50, 150, 200, 70);
		btnStart.addActionListener(this);

		btnStop = new JButton("MENU");
		btnStop.setBackground(new Color(30, 144, 255));
		btnStop.setForeground(Color.WHITE);
		btnStop.setFont(new Font("Tahoma", Font.BOLD, 24));
		btnStop.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		btnStop.setFocusable(false);
		btnStop.setBounds(260, 150, 80, 70);
		btnStop.addActionListener(this);
		btnStop.setVisible(false);

		btnHelp = new JButton("?");
		btnHelp.setBackground(new Color(30, 144, 255));
		btnHelp.setForeground(Color.WHITE);
		btnHelp.setFont(new Font("Tahoma", Font.BOLD, 26));
		btnHelp.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		btnHelp.setFocusable(false);
		btnHelp.setBounds(20, 20, 25, 25);
		btnHelp.addActionListener(this);

		btnHighscore = new JButton("HIGHSCORES");
		btnHighscore.setBackground(new Color(30, 144, 255));
		btnHighscore.setForeground(Color.WHITE);
		btnHighscore.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnHighscore.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		btnHighscore.setFocusable(false);
		btnHighscore.setBounds(50, 20, 150, 25);
		btnHighscore.addActionListener(this);

		// set up labels
		lblScore = new JLabel("SCORE: 0");
		lblScore.setFont(new Font("Tahoma", Font.BOLD, 26));
		lblScore.setForeground(Color.WHITE);
		lblScore.setBounds(90, 240, 200, 50);
		lblScore.setVisible(false);

		lblGameControl = new JLabel();
		lblGameControl.setFont(new Font("Tahoma", Font.BOLD, 26));
		lblGameControl.setForeground(Color.WHITE);
		lblGameControl.setBounds(90, 300, 250, 50);
		lblGameControl.setVisible(false);

		lblHighscore = new JLabel();
		lblHighscore.setFont(new Font("Tahoma", Font.BOLD, 26));
		lblHighscore.setForeground(Color.WHITE);
		lblHighscore.setBounds(530, 100, 250, 50);

		// set up panel
		setLayout(null);
		add(btnStart);
		add(btnStop);
		add(btnHelp);
		add(btnHighscore);
		add(lblScore);
		add(lblGameControl);
		add(lblHighscore);

		// set up listeners
		addMouseListener(new MListener());
		addMouseMotionListener(new MListener());

		// set up frame
		JFrame frame = new JFrame();
		frame.setContentPane(this);
		frame.setTitle("Elements Crush");
		frame.setSize(900, 675);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.addWindowListener(new WindowListener(frame));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(new ImageIcon("images\\background.jpg").getImage(), 0, 0,
				this);
		if (checkDraw) {
			grid.draw(g2);
		} else {
			g2.drawImage(new ImageIcon("images\\four-elements.jpg").getImage(),
					400, 150, this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnStart) {
			// start the game
			if (btnStart.getText().equals("START")) {
				lblGameControl.setVisible(true);
				Object[] options = { "CLASSIC", "TIME MODE" };
				gameMode = JOptionPane
						.showOptionDialog(null, "Choose game mode",
								"Elements Crush", JOptionPane.DEFAULT_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[0]);
				if (gameMode == 0) {
					turnsRemaining = 20;
					lblGameControl.setText("TURNS LEFT: " + turnsRemaining);
					lblHighscore.setText(highscore0);
				} else if (gameMode == 1) {
					timeRemaining = 40;
					lblGameControl.setText("SECONDS LEFT: " + timeRemaining);
					lblHighscore.setText(highscore1);
					gameTimer.start();
				}
				if (gameMode == 0 || gameMode == 1) {
					lblScore.setVisible(true);
					lblHighscore.setVisible(true);
					checkDraw = true;
					btnStart.setText("PAUSE");
				}
			} else if (btnStart.getText().equals("PAUSE")) {
				// pauses the game, hides the grid
				btnStop.setVisible(true);
				checkDraw = false;
				if (gameTimer.isRunning())
					gameTimer.stop();
				moveTimer.stop();
				btnStart.setText("RESUME");
			} else if (btnStart.getText().equals("RESUME")) {
				// resumes the game
				checkDraw = true;
				if (gameMode == 1)
					gameTimer.start();
				moveTimer.start();
				btnStart.setText("PAUSE");
			}
			repaint();
		}
		if (e.getSource() == btnStop) {
			int option = JOptionPane.showConfirmDialog(null,
					"Are you sure? Current score will be lost.",
					"Elements Crush", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION)
				resetGame();
		}
		if (e.getSource() == btnHelp) {
			// shows rules and contorls for the game
			String text1 = "RULES:\nYou can swap two adjacent elements if the swap will form a horizontal/vertical line of 3 or more elements of the same type.\nChoose first element for the swap by clicking on it. "
					+ "The element will be highlighted.\nThen choose second element by clicking on it.\n"
					+ "If a swap is possible element(s) will move, all lines formed will disappear.\n"
					+ "If a horizontal or vertical line of 4 or more elements is formed, you will get special element.\n"
					+ "Special element can destroy other elements when it is activated in the line. Action depends on type of the element:\n"
					+ "1. Fire destroys all adjacent elements.\n2."
					+ " Earth and Water destroy all elements in the horizontal line.\n"
					+ "3.Wind and Ice destroy all elements in a vertical line.\n"
					+ "4. Electricity randomly destroys 8 elements.\n";
			String text2 = "\nGAME MODES:\nTo start the game click 'START' button.\n"
					+ "Before the game begins you will be asked to choose game mode:\n"
					+ "\t1. CLASSIC MODE.\n-At the begining of the game you are given a set number of turns.\n-The game ends when you have no turns left.\n-The goal is to get the highest score before running out of turns\n"
					+ "\t2.TIMER MODE.\n-Every second time left is decreasing.\n-The game ends when there is no time left.\n-The goal is to get the highest score before time runs out.\n-Making lines gives some additional time.";
			JOptionPane.showMessageDialog(null, text1 + text2,
					"Elements Crush", JOptionPane.PLAIN_MESSAGE);
		}

		if (e.getSource() == gameTimer) {
			if (timeRemaining > 0)
				timeRemaining--;
			lblGameControl.setText("SECONDS LEFT: " + timeRemaining);
			// stops the game if the game is in time mode when there is no time
			// left
			if (timeRemaining == 0 && !moveTimer.isRunning()) {
				gameTimer.stop();

				highscorePane();
				int option = JOptionPane.showConfirmDialog(null,
						"Do you want to play again?", "Elements Crush",
						JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					resetGame();
				} else {
					JOptionPane.showMessageDialog(null,
							"Thank you for playing Elements Crush",
							"Elements Crush", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}
			}
		}

		if (e.getSource() == moveTimer) {
			delay++;
			if (delay >= 70) {
				if (delay == 70)
					// making deleted elements invisible after the delay has
					// passed
					for (int i = 0; i < grid.getNumRows(); i++)
						for (int j = 0; j < grid.getNumCols(); j++)
							if (grid.getElement(i, j).getType() == Element.EMPTY) {
								grid.getElement(i, j).setImage(
										new ImageIcon(
												"images\\emptyElement.png"));
							}
				// moving elements above deleted element down
				moveAnimation(elementsToMove, yEnd);
				int r;
				// checking if all moved elements are in the correct positions
				for (r = 0; r < elementsToMove.size(); r++) {
					if (elementsToMove.get(r).getY() != yEnd.get(r))
						break;
				}
				// when all elements are moved down to the correct positions
				if (r == elementsToMove.size()) {
					moveTimer.stop();
					elementsToMove.clear();
					yEnd.clear();
					for (int i = 0; i < grid.getNumRows(); i++)
						for (int j = 0; j < grid.getNumCols(); j++) {
							if (grid.getElement(i, j).getType() == Element.EMPTY) {
								for (int k = i; k >= 0; k--) {
									if (k > 0) {
										grid.setElement(k, j,
												grid.getElement(k - 1, j));
									} else {
										grid.setElement(k, j, grid
												.generateElement(grid
														.getElement(k, j)
														.getX(), 150));
									}
								}
							}
						}

					int y = 150;
					for (int i = 0; i < grid.getNumRows(); i++) {
						for (int j = 0; j < grid.getNumCols(); j++)
							grid.getElement(i, j).setY(y);
						y += 50;
					}
					int previousScore = grid.getScore();
					if (grid.findLine()) {
						deleteLines();
						if (gameMode == 1) {
							// giving player additional time when line is made
							int difference = grid.getScore() - previousScore;
							timeRemaining += difference / 2;
							lblGameControl.setText("SECONDS LEFT: "
									+ timeRemaining);
						}
						lblScore.setText("SCORE: " + grid.getScore());
					} else if (turnsRemaining == 0 && gameMode == 0) {
						if (gameTimer.isRunning())
							gameTimer.stop();
						highscorePane();

						int option = JOptionPane.showConfirmDialog(null,
								"Do you want to play again?", "Elements Crush",
								JOptionPane.YES_NO_OPTION);
						if (option == JOptionPane.YES_OPTION) {
							resetGame();
						} else {
							JOptionPane.showMessageDialog(null,
									"Thank you for playing Elements Crush",
									"Elements Crush",
									JOptionPane.INFORMATION_MESSAGE);
							System.exit(0);
						}
					}
				}
			}
		}
		if (e.getSource() == btnHighscore) {
			String h1 = "CLASSIC MODE\n", h2 = "TIME MODE\n";
			for (int i = 1; i < 10; i++) {
				h1 = h1 + highscoreClassic[i] + "\n";
				h2 = h2 + highscoreTime[i] + "\n";
			}
			if (btnStart.getText().equals("START")) {
				Object[] options = { "OK", "RESET SCORES" };
				int option = JOptionPane.showOptionDialog(null, h1 + "\n" + h2,
						"Elements Crush", JOptionPane.DEFAULT_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if (option == 1) {
					int resetOption = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to reset highscores?",
							"Elements Crush", JOptionPane.YES_NO_OPTION);
					if (resetOption == JOptionPane.YES_OPTION) {
						fileHighscoreTime.delete();
						fileHighscoreClassic.delete();
						try {
							fileHighscoreTime.createNewFile();
							fileHighscoreClassic.createNewFile();
						} catch (IOException e1) {
							System.err.println(e1);
						}
						try {
							BufferedWriter out = new BufferedWriter(
									new FileWriter(fileHighscoreTime));
							out.write("HIGHSCORE: 0");
							highscore1 = "HIGHSCORE: 0";
							out.newLine();
							for (int i = 1; i < 11; i++) {
								out.write(i + ". unknown 0");
								highscoreTime[i] = i + ". unknown 0";
								out.newLine();
							}
							out.close();
						} catch (IOException arg) {
							System.err.println(arg);
						}
						try {
							BufferedWriter out = new BufferedWriter(
									new FileWriter(fileHighscoreClassic));
							out.write("HIGHSCORE: 0");
							highscore0 = "HIGHSCORE: 0";
							out.newLine();
							for (int i = 1; i < 11; i++) {
								out.write(i + ". unknown 0");
								highscoreClassic[i] = i + ". unknown 0";
								out.newLine();
							}
							out.close();
						} catch (IOException arg) {
							System.err.println(arg);
						}
					}
				}
			} else {
				JOptionPane
						.showMessageDialog(
								null,
								h1
										+ "\n"
										+ h2
										+ "\nYou can`t reset scores while game is in progress.",
								"Elements Crush", JOptionPane.PLAIN_MESSAGE);
			}
		}
	}

	public void moveAnimation(ArrayList<Element> e, ArrayList<Integer> yEnd) {
		for (int i = 0; i < e.size(); i++) {
			if (e.get(i).getY() < yEnd.get(i))
				e.get(i).setY(e.get(i).getY() + 1);
		}
		repaint();
	}

	private class MListener extends MouseAdapter implements
			MouseMotionListener, ActionListener {
		private int clickCounter = 0, i1, j1, i2, j2;
		private Timer swapTimer;
		private int xStart, xEnd, yStart, yEnd;
		private ImageIcon standardImg;
		private boolean moveForward = false, swapInProccess = false;

		@Override
		public void mouseDragged(MouseEvent arg0) {
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (!swapInProccess) {
				r.setLocation(e.getX(), e.getY());
				for (int i = 0; i < grid.getNumRows(); i++)
					for (int j = 0; j < grid.getNumCols(); j++) {
						if (r.intersects(grid.getElement(i, j).getRect())) {
							currentElement = grid.getElement(i, j);
							if (clickCounter == 0 && !moveTimer.isRunning()) {
								standardImg = currentElement.getImage();
								switch (currentElement.getType()) {
								case Element.EARTH:
									currentElement.setImage(new ImageIcon(
											"images\\earthC.png"));
									break;
								case Element.ELECTRICITY:
									currentElement.setImage(new ImageIcon(
											"images\\electricityC.png"));
									break;
								case Element.FIRE:
									currentElement.setImage(new ImageIcon(
											"images\\fireC.png"));
									break;
								case Element.WIND:
									currentElement.setImage(new ImageIcon(
											"images\\windC.png"));
									break;
								case Element.WATER:
									currentElement.setImage(new ImageIcon(
											"images\\waterC.png"));
									break;
								case Element.ICE:
									currentElement.setImage(new ImageIcon(
											"images\\iceC.png"));
									break;
								}
								clickCounter++;
								firstElement = currentElement;
								i1 = i;
								j1 = j;
							} else if (clickCounter == 1) {
								swapInProccess = true;
								clickCounter = 0;
								firstElement.setImage(standardImg);
								moveForward = true;
								if (((i == i1 + 1 || i == i1 - 1) && j == j1)
										|| ((j == j1 + 1 || j == j1 - 1) && i == i1)) {
									i2 = i;
									j2 = j;
									xEnd = currentElement.getX();
									xStart = firstElement.getX();
									yEnd = currentElement.getY();
									yStart = firstElement.getY();
									swapTimer = new Timer(5, this);
									swapTimer.start();
								} else
									swapInProccess = false;
							}
							break;
						}
					}
				repaint();
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == swapTimer) {
				swapAnimation(firstElement, currentElement);
				if (xStart == xEnd && yStart == yEnd) {
					swapTimer.stop();
					grid.setElement(i1, j1, currentElement);
					grid.setElement(i2, j2, firstElement);
					int previousScore = grid.getScore();
					boolean line = grid.findLine(i1, j1, i2, j2);
					if (!moveForward && swapInProccess)
						swapInProccess = false;
					// if no lines were found returns elements to their original
					// positions
					if (!line && moveForward) {
						Element tmp = firstElement;
						firstElement = currentElement;
						currentElement = tmp;
						xEnd = currentElement.getX();
						xStart = firstElement.getX();
						yEnd = currentElement.getY();
						yStart = firstElement.getY();
						swapTimer.start();
						moveForward = false;
					}
					// remove all the lines, move remaining elements down,
					// generate new elements on the top
					else if (line) {
						swapInProccess = false;
						deleteLines();
						lblScore.setText("SCORE: " + grid.getScore());
						if (gameMode == 0) {
							if (turnsRemaining > 0)
								turnsRemaining--;
							lblGameControl.setText("TURNS LEFT: "
									+ turnsRemaining);

						} else {
							int difference = grid.getScore() - previousScore;
							timeRemaining += difference / 2;
							lblGameControl.setText("SECONDS LEFT: "
									+ timeRemaining);
						}
					}
				}
				repaint();
			}
		}

		public void swapAnimation(Element e1, Element e2) {
			if (xStart < xEnd) {
				e1.setX(e1.getX() + 1);
				e2.setX(e2.getX() - 1);
				xStart++;
			} else if (xStart > xEnd) {
				e1.setX(e1.getX() - 1);
				e2.setX(e2.getX() + 1);
				xStart--;
			}

			if (yStart < yEnd) {
				e1.setY(e1.getY() + 1);
				e2.setY(e2.getY() - 1);
				yStart++;
			} else if (yStart > yEnd) {
				e1.setY(e1.getY() - 1);
				e2.setY(e2.getY() + 1);
				yStart--;
			}
			repaint();
		}
	}

	public void deleteLines() {
		for (int i = 0; i < grid.getNumRows(); i++)
			for (int j = 0; j < grid.getNumCols(); j++) {
				if (grid.getElement(i, j).getType() == Element.EMPTY) {
					// finding all elements that need to be moved down, adding
					// them to array list
					for (int s = i; s >= 0; s--) {
						if (!elementsToMove.contains(grid.getElement(s, j))) {
							elementsToMove.add(grid.getElement(s, j));
							yEnd.add(grid.getElement(s, j).getY() + 50);
						} else {
							int index = elementsToMove.indexOf(grid.getElement(
									s, j));
							yEnd.set(index, yEnd.get(index) + 50);
						}
					}
				}
			}
		delay = 0;
		moveTimer.start();
	}

	public void resetGame() {
		grid.setScore(0);
		grid.fillGrid();
		lblScore.setVisible(false);
		lblScore.setText("SCORE: 0");
		lblGameControl.setVisible(false);
		lblGameControl.setText("");
		lblHighscore.setVisible(false);
		btnStart.setText("START");
		btnStop.setVisible(false);
		checkDraw = false;
		repaint();
	}

	public void highscorePane() {
		String playerName = JOptionPane.showInputDialog(null,
				"Game Over!\nYou finished with " + grid.getScore()
						+ " points\nPlease enter your name:", "Elements Crush",
				JOptionPane.INFORMATION_MESSAGE);
		if (playerName == null || playerName.equals(""))
			playerName = "unknown";
		for (int i = 1; i < 10; i++) {
			if (gameMode == 0) {
				int index1 = highscoreClassic[i].indexOf(" ");
				int index2 = highscoreClassic[i].lastIndexOf(" ");
				String currentScore = highscoreClassic[i].substring(index2 + 1);
				if (Integer.parseInt(currentScore) <= grid.getScore()) {
					for (int j = 10; j >= i + 1; j--) {
						highscoreClassic[j] = highscoreClassic[j - 1];
						highscoreClassic[j] = j
								+ highscoreClassic[j].substring(1);
					}
					highscoreClassic[i] = highscoreClassic[i].substring(0,
							index1 + 1) + playerName + " " + grid.getScore();
					break;
				}
			} else {
				int index1 = highscoreTime[i].indexOf(" ");
				int index2 = highscoreTime[i].lastIndexOf(" ");
				String currentScore = highscoreTime[i].substring(index2 + 1);
				if (Integer.parseInt(currentScore) <= grid.getScore()) {
					for (int j = 10; j >= i + 1; j--) {
						highscoreTime[j] = highscoreTime[j - 1];
						highscoreTime[j] = j + highscoreTime[j].substring(1);
					}
					highscoreTime[i] = highscoreTime[i]
							.substring(0, index1 + 1)
							+ playerName
							+ " "
							+ grid.getScore();
					break;
				}
			}
		}
		if (gameMode == 0) {
			String highestScore = highscoreClassic[1]
					.substring(highscoreClassic[1].lastIndexOf(" ") + 1);
			if (grid.getScore() > Integer.parseInt(highestScore))
				highscoreClassic[0] = "HIGHSCORE: " + grid.getScore();
			else
				highscoreClassic[0] = "HIGHSCORE: " + highestScore;
		} else {
			String highestScore = highscoreTime[1].substring(highscoreTime[1]
					.lastIndexOf(" ") + 1);
			if (grid.getScore() > Integer.parseInt(highestScore))
				highscoreTime[0] = "HIGHSCORE: " + grid.getScore();
			else
				highscoreTime[0] = "HIGHSCORE: " + highestScore;
		}
		// changing highscore in the highscore file
		if (gameMode == 1) {
			fileHighscoreTime.delete();
			try {
				fileHighscoreTime.createNewFile();
			} catch (IOException e1) {
				System.err.println(e1);
			}
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(
						fileHighscoreTime));
				for (int i = 0; i < 11; i++)
					if (highscoreTime[i] != null) {
						out.write(highscoreTime[i]);
						out.newLine();
					}
				out.close();
				highscore1 = highscoreTime[0];
			} catch (IOException arg) {
				System.err.println(arg);
			}
		} else {
			fileHighscoreClassic.delete();
			try {
				fileHighscoreClassic.createNewFile();
			} catch (IOException e1) {
				System.err.println(e1);
			}
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(
						fileHighscoreClassic));
				for (int i = 0; i < 11; i++)
					if (highscoreClassic[i] != null) {
						out.write(highscoreClassic[i]);
						out.newLine();
					}
				out.close();
				highscore0 = highscoreClassic[0];
			} catch (IOException arg) {
				System.err.println(arg);
			}
		}
	}

	private class WindowListener extends WindowAdapter {
		private JFrame frame;

		public WindowListener(JFrame f) {
			frame = f;
		}

		@Override
		public void windowClosing(WindowEvent e) {
			if (e.getSource() == frame) {
				boolean checkTimer = gameTimer.isRunning();
				if (checkTimer)
					gameTimer.stop();
				if (moveTimer.isRunning())
					moveTimer.stop();
				checkDraw = false;
				repaint();
				int option = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to exit?", "Elements Crush",
						JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					JOptionPane.showMessageDialog(null,
							"Thank you for playing Elements Crush",
							"Elements Crush", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				} else {
					if (gameMode != -1)
						checkDraw = true;
					repaint();
					if (gameMode == 1 && checkTimer)
						gameTimer.start();
				}
			}
		}
	}
}
