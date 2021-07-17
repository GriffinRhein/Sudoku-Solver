import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

import java.lang.StringBuilder;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class DrawNumsConstructor extends PaintedObjects
{
	// Declare all of the KeyStrokes we will need,
	// and create the Rectangle and CoreGrid as well as
	// our array of numHolders to resemble the grid

	KeyStroke pressRight = KeyStroke.getKeyStroke("RIGHT");
	KeyStroke pressLeft = KeyStroke.getKeyStroke("LEFT");
	KeyStroke pressUp = KeyStroke.getKeyStroke("UP");
	KeyStroke pressDown = KeyStroke.getKeyStroke("DOWN");
	KeyStroke pressOne = KeyStroke.getKeyStroke("1");
	KeyStroke pressTwo = KeyStroke.getKeyStroke("2");
	KeyStroke pressThree = KeyStroke.getKeyStroke("3");
	KeyStroke pressFour = KeyStroke.getKeyStroke("4");
	KeyStroke pressFive = KeyStroke.getKeyStroke("5");
	KeyStroke pressSix = KeyStroke.getKeyStroke("6");
	KeyStroke pressSeven = KeyStroke.getKeyStroke("7");
	KeyStroke pressEight = KeyStroke.getKeyStroke("8");
	KeyStroke pressNine = KeyStroke.getKeyStroke("9");
	KeyStroke pressSpace = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0);
	KeyStroke pressBack = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,0);
	KeyStroke pressDelete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0);
	KeyStroke pressEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);


	OurRectangle recToWorkWith = new OurRectangle();
	OurCoreGrid gridToWorkWith = new OurCoreGrid();
	OurNumHolder[][] fillInMap = new OurNumHolder[9][9];
	OurSolveText textToWorkWith = new OurSolveText();

	ClickButton itsTheResetButton = new ClickButton();
	ClickButton itsTheSolveButton = new ClickButton();
	ClickButton itsTheUndoSolveButton = new ClickButton();
	ClickButton itsTheSaveSudokuButton = new ClickButton();
	ClickButton itsTheLoadSudokuButton = new ClickButton();

	String[][] stringCompForFinal = new String[9][9];

	StringRetriever itsTheStringRetriever = new StringRetriever(this);

	SudokuSolveHumanMethods theSudokuWeSendTo;


	// Create InputMap and ActionMap

	InputMap inputMap = recToWorkWith.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
	ActionMap actionMap = recToWorkWith.getActionMap();

	ActionMap buttonActionMap = itsTheResetButton.getActionMap();


	// Mapping Shortcuts

	protected void setTheAction(KeyStroke a, String b, Action c)
	{
		inputMap.put(a,b);
		actionMap.put(b,c);
	}

	protected class NumAction extends AbstractAction
	{
		Integer numToHandle;

		public NumAction(Integer w)
		{
			super();
			numToHandle = w;
		}

		public void actionPerformed(ActionEvent e)
		{
			fillInMap[currentRow][currentCol].setNum(numToHandle);
			fillInMap[currentRow][currentCol].repaint();
		}
	}

	protected void setNumAction(KeyStroke a, String b, NumAction c)
	{
		inputMap.put(a,b);
		actionMap.put(b,c);
	}

	protected enum direcList{Right,Left,Up,Down}

	protected class DirecAction extends AbstractAction
	{
		direcList theDirection;

		public DirecAction(direcList input)
		{
			super();
			theDirection = input;
		}

		public void actionPerformed(ActionEvent e)
		{

			switch(theDirection)
			{
				case Right:

					if(currentCol != 8)
					{
						currentCol++;
						ourRecLocation.x += 60;
					}
					else
					{
						currentCol = 0;
						ourRecLocation.x = 100;
					}
					break;

				case Left:

					if(currentCol != 0)
					{
						currentCol--;
						ourRecLocation.x -= 60;
					}
					else
					{
						currentCol = 8;
						ourRecLocation.x = 580;
					}
					break;

				case Up:

					if(currentRow != 0)
					{
						currentRow--;
						ourRecLocation.y -= 60;
					}
					else
					{
						currentRow = 8;
						ourRecLocation.y = 580;
					}
					break;

				case Down:

					if(currentRow != 8)
					{
						currentRow++;
						ourRecLocation.y += 60;
					}
					else
					{
						currentRow = 0;
						ourRecLocation.y = 100;
					}
					break;
			}

			recToWorkWith.repaint();
		}
	}

	protected void setDirecAction(KeyStroke a, String b, DirecAction c)
	{
		inputMap.put(a,b);
		actionMap.put(b,c);
	}


	DirecAction rightAction = new DirecAction(direcList.Right);
	DirecAction leftAction = new DirecAction(direcList.Left);
	DirecAction upAction = new DirecAction(direcList.Up);
	DirecAction downAction = new DirecAction(direcList.Down);

	NumAction oneAction = new NumAction(1);
	NumAction twoAction = new NumAction(2);
	NumAction threeAction = new NumAction(3);
	NumAction fourAction = new NumAction(4);
	NumAction fiveAction = new NumAction(5);
	NumAction sixAction = new NumAction(6);
	NumAction sevenAction = new NumAction(7);
	NumAction eightAction = new NumAction(8);
	NumAction nineAction = new NumAction(9);
	NumAction backAction = new NumAction(null);
	NumAction deleteAction = new NumAction(null);

	Action enterAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			finalAct();
		}
	};


	// Enact solving procedures & resulting visual adjustments

	protected void finalAct()
	{
		for(int i=0;i<ROWS;i++)
		{
			for(int j=0;j<COLS;j++)
			{
				stringCompForFinal[i][j] = fillInMap[i][j].getNum();
			}
		}

		// The part where the numbers we inserted are sent to the
		// Sudoku Solver, in which the solving occurs

		theSudokuWeSendTo = new SudokuSolveHumanMethods(stringCompForFinal);


		// Solving procedures finished

		for(int i=0;i<ROWS;i++)
		{
			for(int j=0;j<COLS;j++)
			{
				if(fillInMap[i][j].getNum().equals(""))
					fillInMap[i][j].adjustSolve(true);

				fillInMap[i][j].setNum( theSudokuWeSendTo.mySudoku.SudokuMap[i][j].result );

				fillInMap[i][j].setFinalPossArray( theSudokuWeSendTo.mySudoku.SudokuMap[i][j].possArray );

				fillInMap[i][j].repaint();
			}
		}

		if(controlsOn)
		{
			inputMap.clear();
			actionMap.clear();
			itsTheSolveButton.setEnabled(false);
			itsTheUndoSolveButton.setEnabled(true);
			itsTheLoadSudokuButton.setEnabled(false);

			controlsOn = false;
		}

		ourRectWidth = 0;
		ourRectHeight = 0;

		recToWorkWith.repaint();

		textToWorkWith.setNumSquaresSolved(theSudokuWeSendTo.mySudoku.squaresSolved);
		textToWorkWith.setCompletionCode(theSudokuWeSendTo.mySudoku.isPuzzleComplete());
		textToWorkWith.repaint();
	}


	// Test command to reset solve text

	Action resetSudoku = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			textToWorkWith.resetSolveText();
			textToWorkWith.repaint();

			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].resetNumHolder();
					fillInMap[i][j].repaint();
				}
			}

			currentRow = 0;
			currentCol = 0;

			ourRecLocation.x = 100;
			ourRecLocation.y = 100;

			ourRectWidth = 60;
			ourRectHeight = 60;

			recToWorkWith.repaint();

			if(!(controlsOn))
			{
				setDirecAction(pressRight,"rightAction",rightAction);
				setDirecAction(pressLeft,"leftAction",leftAction);
				setDirecAction(pressUp,"upAction",upAction);
				setDirecAction(pressDown,"downAction",downAction);

				setNumAction(pressOne,"oneAction",oneAction);
				setNumAction(pressTwo,"twoAction",twoAction);
				setNumAction(pressThree,"threeAction",threeAction);
				setNumAction(pressFour,"fourAction",fourAction);
				setNumAction(pressFive,"fiveAction",fiveAction);
				setNumAction(pressSix,"sixAction",sixAction);
				setNumAction(pressSeven,"sevenAction",sevenAction);
				setNumAction(pressEight,"eightAction",eightAction);
				setNumAction(pressNine,"nineAction",nineAction);
				setNumAction(pressBack,"backAction",backAction);
				setNumAction(pressDelete,"deleteAction",deleteAction);

				setTheAction(pressEnter,"enterAction",enterAction);

				itsTheSolveButton.setEnabled(true);
				itsTheUndoSolveButton.setEnabled(false);
				itsTheLoadSudokuButton.setEnabled(true);

				controlsOn = true;
			}
		}
	};

	// Does not reset everything, but will undo a solve

	Action undoTheSolve = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			textToWorkWith.resetSolveText();
			textToWorkWith.repaint();

			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					if(stringCompForFinal[i][j] == "")
					{
						fillInMap[i][j].resetNumHolder();
						fillInMap[i][j].repaint();
					}
				}
			}

			ourRectWidth = 60;
			ourRectHeight = 60;

			recToWorkWith.repaint();

			if(!(controlsOn))
			{
				setDirecAction(pressRight,"rightAction",rightAction);
				setDirecAction(pressLeft,"leftAction",leftAction);
				setDirecAction(pressUp,"upAction",upAction);
				setDirecAction(pressDown,"downAction",downAction);

				setNumAction(pressOne,"oneAction",oneAction);
				setNumAction(pressTwo,"twoAction",twoAction);
				setNumAction(pressThree,"threeAction",threeAction);
				setNumAction(pressFour,"fourAction",fourAction);
				setNumAction(pressFive,"fiveAction",fiveAction);
				setNumAction(pressSix,"sixAction",sixAction);
				setNumAction(pressSeven,"sevenAction",sevenAction);
				setNumAction(pressEight,"eightAction",eightAction);
				setNumAction(pressNine,"nineAction",nineAction);
				setNumAction(pressBack,"backAction",backAction);
				setNumAction(pressDelete,"deleteAction",deleteAction);

				setTheAction(pressEnter,"enterAction",enterAction);

				itsTheSolveButton.setEnabled(true);
				itsTheUndoSolveButton.setEnabled(false);
				itsTheLoadSudokuButton.setEnabled(true);

				controlsOn = true;
			}
		}
	};


	public boolean isOrWasSquareEmpty(OurNumHolder theSquare)
	{
		// If the controls are on, the puzzle has not been solved yet.

		if(controlsOn)
		{
			// In this case, just check whether the square is empty.

			if(theSquare.getNum().equals(""))
				return true;
			else
				return false;
		}

		// If the controls are off, the puzzle must have been solved.

		// Each NumHolder has a built-in boolean to keep track of
		// whether it was filled in only when the user hit "Solve"

		return theSquare.getSolveAddStatus();
	}


	// Save Sudoku

	Action saveTheSudoku = new AbstractAction()
	{
		StringBuilder builtString;
		int blankCounter;
		OurNumHolder currentSquare;
		int success;

		String outputString;

		public void actionPerformed(ActionEvent e)
		{
			builtString = new StringBuilder();
			blankCounter = 0;

			// Go through each square in the 9x9 grid

			for(int y=0;y<9;y++)
			{
				for(int x=0;x<9;x++)
				{
					currentSquare = fillInMap[y][x];

					// Check whether the square was filled in by the user

					if(!(isOrWasSquareEmpty(currentSquare)))
					{
						// If the user entered a number, then append a letter
						// indicating how many blanks there were preceding it.
						// And reset the counter.

						if(blankCounter != 0)
						{
							builtString.append((char)(blankCounter+96));
							blankCounter = 0;
						}

						// Append the number

						builtString.append(currentSquare.getNum());
					}
					else
					{
						// If the user did not enter any number, then the square is blank.
						// Increment blankCounter.

						blankCounter++;

						// But make sure you get the final letter down if you've reached the end,
						// or throw down a "z" & reset if you've somehow gotten 26 blanks in a row

						if((y == 8 && x == 8) || blankCounter >= 26)
						{
							builtString.append((char)(blankCounter+96));
							blankCounter = 0;
						}
					}
				}
			}

			outputString = builtString.toString();


			// Output the String.

			itsTheStringRetriever.outputTheString(outputString);

		}
	};

	// Load Sudoku

	Action loadTheSudoku = new AbstractAction()
	{
		String testString;

		char[] charArray;

		Integer[][] sudokuToLoad;

		boolean doWeBotherLoad;
		boolean tooFew;
		boolean tooMany;

		int loadY;
		int loadX;
		int loadCounter;

		int nowASCII;

		public void actionPerformed(ActionEvent e)
		{
			testString = itsTheStringRetriever.getString();

			// testString will be null if the user hit Cancel.
			// In that case, don't do anything.

			if(testString != null)
			{
				charArray = testString.toCharArray();

				sudokuToLoad = new Integer[9][9];

				doWeBotherLoad = true;
				tooFew = false;
				tooMany = false;

				loadY = 0;
				loadX = 0;
				loadCounter = 0;

				// Make sure there are no invalid characters in the String

				for(int a=0;a<charArray.length;a++)
				{
					nowASCII = (int)charArray[a];

					if(nowASCII < 49 || (nowASCII > 57 && nowASCII < 97) || nowASCII > 122)
					{
						doWeBotherLoad = false;
					}
				}

				// If there was an invalid character, display error message. Otherwise, continue.

				if(!(doWeBotherLoad))
				{
					itsTheStringRetriever.showErrorMessage(0);
				}
				else
				{
					// At this point, we know every character is a number or lowercase letter.

					for(int a=0;a<charArray.length;a++)
					{
						if(!(tooMany))
						{
							nowASCII = (int)charArray[a];

							// Letter if >96, Number if not.

							if(nowASCII > 96)
							{
								loadCounter = nowASCII - 96;

								while(loadCounter > 0)
								{
									if(loadY > 8)
									{
										tooMany = true;
									}
									else
									{
										sudokuToLoad[loadY][loadX] = null;
									}

									if(loadX != 8)
									{
										loadX++;
									}
									else
									{
										loadX = 0;
										loadY++;
									}

									loadCounter--;
								}
							}
							else
							{
								if(loadY > 8)
								{
									tooMany = true;
								}
								else
								{
									sudokuToLoad[loadY][loadX] = Character.getNumericValue(charArray[a]);
								}

								if(loadX != 8)
								{
									loadX++;
								}
								else
								{
									loadX = 0;
									loadY++;
								}

							}
						}

					}

					if(loadY < 9)
					{
						tooFew = true;
					}

					// If there are too many or too few, display the appropriate error message
					// Otherwise, give fillInMap everything

					if(tooFew)
					{
						itsTheStringRetriever.showErrorMessage(1);
					}
					else if(tooMany)
					{
						itsTheStringRetriever.showErrorMessage(2);
					}
					else
					{
						for(int s=0;s<9;s++)
						{
							for(int t=0;t<9;t++)
							{
								fillInMap[s][t].setNum(sudokuToLoad[s][t]);
								fillInMap[s][t].repaint();
							}
						}
					}
				}
			}
		}
	};





	// Constructor!!!

	public DrawNumsConstructor()
	{

		// At the start, all we have is an array of empty
		// spots, each designated for an OurNumHolder, so
		// we need to actually fill it up with the things

		for(int i=0;i<ROWS;i++)
		{
			for(int j=0;j<COLS;j++)
			{
				fillInMap[i][j] = new OurNumHolder(i,j);
			}
		}



		// Now, the Rectangle, the CoreGrid, and every NumHolder
		// is placed into a series of parent/child relationships
		// with one another to draw all of them onto a JFrame

// ~~~~~~~~~~~~~~~~~~~~

		// The Rectangle will go in the back, so it is given a
		// BorderLayout first, and the grid is added to that BorderLayout.
		// The grid's JPanel is also set to be see-through anywhere it
		// is not drawing anything

		recToWorkWith.setOpaque(false);
		recToWorkWith.setLayout( new BorderLayout() );
		recToWorkWith.add(gridToWorkWith);
		gridToWorkWith.setOpaque(false);

// ~~~~~~~~~~~~~~~~~~~~

		// Now we do something similar for each NumHolder. The first
		// one is given to a BorderLayout for the CoreGrid, and each
		// after that is given to a BorderLayout for the previous
		// NumHolder. This is done (for now) because by default any
		// BorderLayout can display only one object at a time

		gridToWorkWith.setLayout( new BorderLayout() );
		gridToWorkWith.add(fillInMap[0][0]);
		fillInMap[0][0].setOpaque(false);

		for(int i=0;i<ROWS;i++)
		{
			for(int j=0;j<COLS;j++)
			{
				// Create a BorderLayout and add the next OurNumHolder to it,
				// then move to that OurNumHolder and create another
				// BorderLayout, etc., until you reach the end

				if( !( i==ROWS-1 && j==COLS-1) )
				{
					fillInMap[i][j].setLayout( new BorderLayout() );

					// If you're not at the end of a row, the OurNumHolder which
					// goes in your BorderLayout is the next one in the row

					if( j != COLS-1 )
					{
						fillInMap[i][j].add(fillInMap[i][j+1]);
						fillInMap[i][j+1].setOpaque(false);
					}

					// If you're at the end of a row, the OurNumHolder which
					// goes in your BorderLayout is the first one in the next row

					else
					{
						fillInMap[i][j].add(fillInMap[i+1][0]);
						fillInMap[i+1][0].setOpaque(false);
					}
				}
			}
		}

		fillInMap[8][8].setLayout( new BorderLayout() );
		fillInMap[8][8].add( textToWorkWith );
		textToWorkWith.setOpaque( false );


		textToWorkWith.setLayout(null);

		// This is the Reset Button which sets the rectangle back to
		// Row 0, Column 0, and empties the sudoku & comments

		textToWorkWith.add(itsTheResetButton);
		itsTheResetButton.setAction(resetSudoku);
		itsTheResetButton.setBounds(130,700,120,25);
		itsTheResetButton.setText("Reset All");
		itsTheResetButton.setFocusPainted(false);

		// Solve Button

		textToWorkWith.add(itsTheSolveButton);
		itsTheSolveButton.setAction(enterAction);
		itsTheSolveButton.setBounds(280,700,180,25);
		itsTheSolveButton.setText("Solve Sudoku");
		itsTheSolveButton.setFocusPainted(false);

		// Undo Solve Button

		textToWorkWith.add(itsTheUndoSolveButton);
		itsTheUndoSolveButton.setAction(undoTheSolve);
		itsTheUndoSolveButton.setBounds(490,700,120,25);
		itsTheUndoSolveButton.setText("Undo Solve");
		itsTheUndoSolveButton.setFocusPainted(false);
		itsTheUndoSolveButton.setEnabled(false);

		// Save Sudoku Button

		textToWorkWith.add(itsTheSaveSudokuButton);
		itsTheSaveSudokuButton.setAction(saveTheSudoku);
		itsTheSaveSudokuButton.setBounds(670,280,180,60);
		itsTheSaveSudokuButton.setText("Save Sudoku");
		itsTheSaveSudokuButton.setFocusPainted(false);
		itsTheSaveSudokuButton.setEnabled(true);


		// Load Sudoku Button

		textToWorkWith.add(itsTheLoadSudokuButton);
		itsTheLoadSudokuButton.setAction(loadTheSudoku);
		itsTheLoadSudokuButton.setBounds(670,400,180,60);
		itsTheLoadSudokuButton.setText("Load Sudoku");
		itsTheLoadSudokuButton.setFocusPainted(false);
		itsTheLoadSudokuButton.setEnabled(true);


		// This just makes it so that hitting spacebar doesn't click the button

		InputMap im = (InputMap)UIManager.get("Button.focusInputMap");
		im.put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
		im.put(KeyStroke.getKeyStroke("released SPACE"), "none");



// ~~~~~~~~~~~~~~~~~~~~

		// Finally, the object at the start of that parent/child
		// chain is added to the JFrame

		add(recToWorkWith);

// ~~~~~~~~~~~~~~~~~~~~

		// Set up the commands for moving around and entering numbers

		setDirecAction(pressRight,"rightAction",rightAction);
		setDirecAction(pressLeft,"leftAction",leftAction);
		setDirecAction(pressUp,"upAction",upAction);
		setDirecAction(pressDown,"downAction",downAction);

		setNumAction(pressOne,"oneAction",oneAction);
		setNumAction(pressTwo,"twoAction",twoAction);
		setNumAction(pressThree,"threeAction",threeAction);
		setNumAction(pressFour,"fourAction",fourAction);
		setNumAction(pressFive,"fiveAction",fiveAction);
		setNumAction(pressSix,"sixAction",sixAction);
		setNumAction(pressSeven,"sevenAction",sevenAction);
		setNumAction(pressEight,"eightAction",eightAction);
		setNumAction(pressNine,"nineAction",nineAction);
		setNumAction(pressBack,"backAction",backAction);
		setNumAction(pressDelete,"deleteAction",deleteAction);

		setTheAction(pressEnter,"enterAction",enterAction);

		// Last bit of JFrame setup

		getContentPane().setBackground( new Color(255,225,225) );

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(890,800);
		setVisible(true);
	}

} // DrawNumsConstructor, our outermost class