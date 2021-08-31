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
	ClickButton itsTheBeginStepsButton = new ClickButton();
	ClickButton itsTheNextStepButton = new ClickButton();
	ClickButton itsTheLastResortButton = new ClickButton();

	String[][] stringCompForFinal = new String[9][9];

	PopUpPane itsThePopUpPane = new PopUpPane(this);

	JButtonCommands itsTheButtonCommands = new JButtonCommands(this);

	FullSudoku ourSentSudoku;
	SolveChecker ourSolveChecker;

	UsingLogicalMethods inputForSolving;

	LastResort inputForLastResort;


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

			if(numToHandle != null)
				fillInMap[currentRow][currentCol].changeStartEmpty(false);
			else
				fillInMap[currentRow][currentCol].changeStartEmpty(true);

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
			finalAct(true);
		}
	};

	Action slowSolveStartAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			finalAct(false);
		}
	};

	Action advanceStepAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			advanceOneStep();
		}
	};

	Action lastResortAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			enactLastResort();
		}
	};


	// Begin solving procedures

	protected void finalAct(boolean allAtOnce)
	{
		// Put all Strings input by the user into a 9x9 array

		// getNum() returns an empty String of "" if no number
		// was entered, so you can count on that being in all
		// spots of stringCompForFinal thought of as empty

		for(int i=0;i<ROWS;i++)
		{
			for(int j=0;j<COLS;j++)
			{
				stringCompForFinal[i][j] = fillInMap[i][j].getNum();
			}
		}


		// Create the SolveChecker, out of the numbers entered

		ourSolveChecker = new SolveChecker(stringCompForFinal);


		// Make sure the user has not inserted duplicates within a row/column/box

		if(!(ourSolveChecker.isEntryDupeFree()))
		{
			itsThePopUpPane.showInsertedDupeMessage();
		}

		// Make sure the user has entered at least 17 numbers

		else if(!(ourSolveChecker.areThereSeventeenClues()))
		{
			itsThePopUpPane.showNotEnoughCluesMessage();
		}

		// If so, advance

		else
		{
			// Create Sudoku

			ourSentSudoku = new FullSudoku(stringCompForFinal);


			// Make sure the puzzle is solvable, before doing anything else

			inputForLastResort = new LastResort(ourSentSudoku);

			boolean didVirtualSolveSucceed = inputForLastResort.initiateCrudeVirtualSolve();

			if(!(didVirtualSolveSucceed))
			{
				itsThePopUpPane.showNoSolutionMessage();
			}


			else
			{
				// Lock the controls for inputting numbers

				if(controlsOn)
					turnControlsOff();

				ourRectWidth = 0;
				ourRectHeight = 0;

				recToWorkWith.repaint();


				// The part where the numbers we inserted are sent to the
				// Sudoku Solver, in which the logical solving will occur

				inputForSolving = new UsingLogicalMethods(ourSentSudoku);

				if(allAtOnce)
					oneBigSolve();
				else
				{
					updateCurrentStatus(0,true);
					itsTheNextStepButton.setEnabled(true);
				}
			}
		}
	}


	// Update the GUI to show the current state of the Sudoku
	// pointOfSolve: 0 for initial fill, 1 for logical solve, 2 for last resort

	private void updateCurrentStatus(int pointOfSolve, boolean displayImmediately)
	{
		// Give the PaintedObjects the information

		for(int i=0;i<ROWS;i++)
		{
			for(int j=0;j<COLS;j++)
			{
				if(fillInMap[i][j].getNum().equals(""))
				{
					if(ourSentSudoku.SudokuMap[i][j].result != null)
					{
						if(pointOfSolve == 1)
							fillInMap[i][j].adjustHumanSolve(true);
						else if(pointOfSolve == 2)
							fillInMap[i][j].adjustLastResort(true);
					}

					fillInMap[i][j].setNum( ourSentSudoku.SudokuMap[i][j].result );
					fillInMap[i][j].setPossArray( ourSentSudoku.SudokuMap[i][j].possArray );
				}
			}
		}

		textToWorkWith.setNumSquaresSolved(ourSentSudoku.squaresSolved);


		if(displayImmediately)
		{
			// Display it

			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].repaint();
				}
			}

			textToWorkWith.repaint();
		}

	} // updateCurrentStatus()


	private void advanceOneStep()
	{
		boolean didWeDoSomething = inputForSolving.solveOneStep();

		updateCurrentStatus(1,true);

		if(ourSentSudoku.squaresSolved == 81)
			itsTheNextStepButton.setEnabled(false);
		else if(!(didWeDoSomething))
		{
			// IMPROVE IN FUTURE

			itsTheNextStepButton.setEnabled(false);
			itsTheLastResortButton.setEnabled(true);
		}

	} // advanceOneStep()


	// Last Resort once logic techniques run out

	private void enactLastResort()
	{
		// Should the user input a Sudoku with more than one solution, this check guarantees that the
		// solution found by LastResort does not conflict with any work done by UsingLogicalMethods.
		// Though, I'm not sure whether conflict is possible. UsingLogicalMethods works off certainty;
		// I figure that it would never be able to make any moves down a branching path.

		if(!(inputForLastResort.sameResults()))
		{
			inputForLastResort = new LastResort(ourSentSudoku);
			inputForLastResort.initiateCrudeVirtualSolve();
		}


		// Use LastResort's function to copy its results into the Sudoku squares

		inputForLastResort.copyLastResortToSudoku();


		// Put everything in the GUI

		updateCurrentStatus(2,true);


		// Disable the Last Resort button, if it is on

		itsTheLastResortButton.setEnabled(false);

	} // enactLastResort()


	// Solve all at once

	private void oneBigSolve()
	{
		inputForSolving.solveAllAtOnce();

		if(ourSentSudoku.squaresSolved == 81)
			updateCurrentStatus(1,true);
		else
		{
			updateCurrentStatus(1,false);
			enactLastResort();
		}

	} // oneBigSolve()


	// Turn user controls off

	public void turnControlsOff()
	{
		inputMap.clear();
		actionMap.clear();

		itsTheSolveButton.setEnabled(false);
		itsTheUndoSolveButton.setEnabled(true);
		itsTheLoadSudokuButton.setEnabled(false);
		itsTheBeginStepsButton.setEnabled(false);

		controlsOn = false;
	}


	// Turn user controls on

	public void turnControlsOn()
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
		itsTheBeginStepsButton.setEnabled(true);
		itsTheNextStepButton.setEnabled(false);
		itsTheLastResortButton.setEnabled(false);

		controlsOn = true;
	}



	// Resets entire program to starting state

	Action resetSudoku = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			itsTheButtonCommands.resetEverything();
		}
	};

	// Reverts puzzle to what the user input

	Action undoTheSolve = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			itsTheButtonCommands.revertToUserInput();
		}
	};

	// Save Sudoku

	Action saveTheSudoku = new AbstractAction()
	{
		String savedAs;

		public void actionPerformed(ActionEvent e)
		{
			savedAs = itsTheButtonCommands.savingSudoku();


			// Give user the String

			itsThePopUpPane.outputTheString(savedAs);
		}
	};

	// Load Sudoku

	Action loadTheSudoku = new AbstractAction()
	{
		String savedAs;
		int wasThereError;

		public void actionPerformed(ActionEvent e)
		{
			savedAs = itsThePopUpPane.getString();


			// Implement String into puzzle

			wasThereError = itsTheButtonCommands.loadingSudoku(savedAs);

			if(wasThereError != 0)
				itsThePopUpPane.showErrorMessage(wasThereError);
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

// ~~~~~~~~~~~~~~~~~~~~

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
		itsTheSolveButton.setBounds(280,690,180,45);
		itsTheSolveButton.setText("Solve Sudoku");
		itsTheSolveButton.setFocusPainted(false);

		// Undo Solve Button

		textToWorkWith.add(itsTheUndoSolveButton);
		itsTheUndoSolveButton.setAction(undoTheSolve);
		itsTheUndoSolveButton.setBounds(490,700,120,25);
		itsTheUndoSolveButton.setText("Undo Solve");
		itsTheUndoSolveButton.setFocusPainted(false);

		// Save Sudoku Button

		textToWorkWith.add(itsTheSaveSudokuButton);
		itsTheSaveSudokuButton.setAction(saveTheSudoku);
		itsTheSaveSudokuButton.setBounds(670,280,180,60);
		itsTheSaveSudokuButton.setText("Save Sudoku");
		itsTheSaveSudokuButton.setFocusPainted(false);


		// Load Sudoku Button

		textToWorkWith.add(itsTheLoadSudokuButton);
		itsTheLoadSudokuButton.setAction(loadTheSudoku);
		itsTheLoadSudokuButton.setBounds(670,400,180,60);
		itsTheLoadSudokuButton.setText("Load Sudoku");
		itsTheLoadSudokuButton.setFocusPainted(false);


		// Begin Steps Button

		textToWorkWith.add(itsTheBeginStepsButton);
		itsTheBeginStepsButton.setAction(slowSolveStartAction);
		itsTheBeginStepsButton.setBounds(700,625,150,20);
		itsTheBeginStepsButton.setText("Begin Slow Solve");
		itsTheBeginStepsButton.setFocusPainted(false);


		// Next Step Button

		textToWorkWith.add(itsTheNextStepButton);
		itsTheNextStepButton.setAction(advanceStepAction);
		itsTheNextStepButton.setBounds(700,665,150,20);
		itsTheNextStepButton.setText("Next Step");
		itsTheNextStepButton.setFocusPainted(false);


		// Last Resort Button

		textToWorkWith.add(itsTheLastResortButton);
		itsTheLastResortButton.setAction(lastResortAction);
		itsTheLastResortButton.setBounds(700,705,150,20);
		itsTheLastResortButton.setText("Last Resort");
		itsTheLastResortButton.setFocusPainted(false);


		// Ensure user control is allowed at start

		turnControlsOn();
		itsTheResetButton.setEnabled(true);
		itsTheSaveSudokuButton.setEnabled(true);


		// This just makes it so that hitting spacebar doesn't click the button

		InputMap im = (InputMap)UIManager.get("Button.focusInputMap");
		im.put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
		im.put(KeyStroke.getKeyStroke("released SPACE"), "none");



// ~~~~~~~~~~~~~~~~~~~~

		// Finally, the object at the start of that parent/child
		// chain is added to the JFrame

		add(recToWorkWith);

// ~~~~~~~~~~~~~~~~~~~~

		// Last bit of JFrame setup

		getContentPane().setBackground( new Color(255,225,225) );

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(890,800);
		setVisible(true);
	}

} // DrawNumsConstructor, our outermost class