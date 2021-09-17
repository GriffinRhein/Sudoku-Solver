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
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class DrawNumsConstructor extends PaintedObjects
{
	// Coordinates at which the Sudoku grid starts and ends

	public final static int gridLeftX = 100;
	public final static int gridRightX = gridLeftX+gridTotalWidth;

	public final static int gridTopY = 100;
	public final static int gridBottomY = gridTopY+gridTotalHeight;


	// Current row & column we are highlighting

	public static int currentCol = 0;
	public static int currentRow = 0;


	// Whether the arrow keys will currently move the square around
	// and the number keys will enter numbers into the Sudoku

	public static boolean controlsOn;


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


	StringBuilder completeText = null;

	String noProgressMessage = "No further progress can be made with the integrated logical methods.";
	String lastResortMessage = "Puzzle is complete, through trial and error.";
	String puzzleDoneMessage = "Puzzle is complete!";

	JTextArea itsTheTextArea = new JTextArea();


	int vertScrollPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
	int horiScrollPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;

	JScrollPane itsTheScrollPane = new JScrollPane(itsTheTextArea,vertScrollPolicy,horiScrollPolicy);


	OurRectangle recToWorkWith = new OurRectangle();
	OurCoreGrid gridToWorkWith = new OurCoreGrid();
	OurNumHolder[][] fillInMap = new OurNumHolder[9][9];
	OurSolveText completeBanner = new OurSolveText();

	OurRowColLabel[] rowLabels = new OurRowColLabel[9];
	OurRowColLabel[] colLabels = new OurRowColLabel[9];

	JButton itsTheResetButton = new JButton();
	JButton itsTheSolveButton = new JButton();
	JButton itsTheUndoSolveButton = new JButton();
	JButton itsTheSaveSudokuButton = new JButton();
	JButton itsTheLoadSudokuButton = new JButton();
	JButton itsTheBeginStepsButton = new JButton();



	String[][] stringCompForFinal = new String[9][9];

	PopUpPane itsThePopUpPane = new PopUpPane(this);

	JButtonCommands itsTheButtonCommands = new JButtonCommands(this);

	FullSudoku ourSentSudoku;

	CheckStartingNums ourStartChecker;

	UsingLogicalMethods inputForSolving;

	LastResort inputForLastResort;


	// Create InputMap and ActionMap

	InputMap inputMap = recToWorkWith.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
	ActionMap actionMap = recToWorkWith.getActionMap();

	ActionMap buttonActionMap = itsTheResetButton.getActionMap();


	// Mapping Shortcuts

	private void setTheAction(KeyStroke a, String b, Action c)
	{
		inputMap.put(a,b);
		actionMap.put(b,c);
	}

	public class NumAction extends AbstractAction
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

	private void setNumAction(KeyStroke a, String b, NumAction c)
	{
		inputMap.put(a,b);
		actionMap.put(b,c);
	}

	private enum direcList
	{
		Right,
		Left,
		Up,
		Down;
	}

	public class DirecAction extends AbstractAction
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

					if(currentCol != numColInGrid-1)
					{
						currentCol++;
						ourRecLocation.x += gridSquareWidth;
					}
					else
					{
						currentCol = 0;
						ourRecLocation.x = 0;
					}
					break;

				case Left:

					if(currentCol != 0)
					{
						currentCol--;
						ourRecLocation.x -= gridSquareWidth;
					}
					else
					{
						currentCol = numColInGrid-1;
						ourRecLocation.x = gridTotalWidth-gridSquareWidth;
					}
					break;

				case Up:

					if(currentRow != 0)
					{
						currentRow--;
						ourRecLocation.y -= gridSquareHeight;
					}
					else
					{
						currentRow = numRowInGrid-1;
						ourRecLocation.y = gridTotalHeight-gridSquareHeight;
					}
					break;

				case Down:

					if(currentRow != numRowInGrid-1)
					{
						currentRow++;
						ourRecLocation.y += gridSquareHeight;
					}
					else
					{
						currentRow = 0;
						ourRecLocation.y = 0;
					}
					break;
			}

			recToWorkWith.repaint();
		}
	}

	private void setDirecAction(KeyStroke a, String b, DirecAction c)
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
			enactLastResort(false);
		}
	};

	Action nothingAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{

		}
	};


	private enum stepsButtonPurpose
	{
		StartSlowSolve,
		ForwardOneStep,
		GoToLastResort,
		Nothing;
	}

	private void setStepsButton(stepsButtonPurpose a)
	{
		switch(a)
		{
			case StartSlowSolve:

			itsTheBeginStepsButton.setAction(slowSolveStartAction);
			itsTheBeginStepsButton.setText("Step-By-Step Solve");
			itsTheBeginStepsButton.setEnabled(true);

			break;


			case ForwardOneStep:

			itsTheBeginStepsButton.setAction(advanceStepAction);
			itsTheBeginStepsButton.setText("Next Step");
			itsTheBeginStepsButton.setEnabled(true);

			break;


			case GoToLastResort:

			itsTheBeginStepsButton.setAction(lastResortAction);
			itsTheBeginStepsButton.setText("Last Resort");
			itsTheBeginStepsButton.setEnabled(true);

			break;


			case Nothing:

			itsTheBeginStepsButton.setAction(nothingAction);
			itsTheBeginStepsButton.setText("");
			itsTheBeginStepsButton.setEnabled(false);

			break;
		}
	}

	// Begin solving procedures

	private void finalAct(boolean allAtOnce)
	{
		// Put all Strings input by the user into a 9x9 array

		// getNum() returns an empty String of "" if no number
		// was entered, so you can count on that being in all
		// spots of stringCompForFinal thought of as empty

		for(int i=0;i<numRowInGrid;i++)
		{
			for(int j=0;j<numColInGrid;j++)
			{
				stringCompForFinal[i][j] = fillInMap[i][j].getNum();
			}
		}


		// Create the CheckStartingNums, out of the numbers entered

		ourStartChecker = new CheckStartingNums(stringCompForFinal);


		// Make sure the user has not inserted duplicates within a row/column/box

		if(!(ourStartChecker.isEntryDupeFree()))
		{
			itsThePopUpPane.showInsertedDupeMessage();
		}

		// Make sure the user has entered at least 17 numbers

		else if(!(ourStartChecker.areThereSeventeenClues()))
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
				{
					setStepsButton(stepsButtonPurpose.Nothing);

					oneBigSolve();
				}
				else
				{
					setStepsButton(stepsButtonPurpose.ForwardOneStep);

					completeText = new StringBuilder(bundleInTilde("Puzzle Start!"));
					updateCurrentStatus(0,true);
				}
			}
		}
	}


	// Update the UI to show the current state of the Sudoku
	// pointOfSolve: 0 for initial fill, 1 for logical solve, 2 for last resort

	private void updateCurrentStatus(int pointOfSolve, boolean displayImmediately)
	{
		// Give the PaintedObjects the information

		for(int i=0;i<numRowInGrid;i++)
		{
			for(int j=0;j<numColInGrid;j++)
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

		completeBanner.setNumSquaresSolved(ourSentSudoku.squaresSolved);


		if(displayImmediately)
		{
			// Display it

			for(int i=0;i<numRowInGrid;i++)
			{
				for(int j=0;j<numColInGrid;j++)
				{
					fillInMap[i][j].repaint();
				}
			}

			completeBanner.repaint();


			itsTheTextArea.append(completeText.toString());

			completeText = null;
		}

	} // updateCurrentStatus()


	// Surrounds a String with tildes and blank lines, for now

	public String bundleInTilde(String a)
	{
		String toReturn = "~~~~~~~~~~"+"\n"+a+"\n"+"~~~~~~~~~~"+"\n";

		return toReturn;

	} // bundleInTilde()


	// Solve only one step

	private void advanceOneStep()
	{
		completeText = new StringBuilder("");

		String incomingText = inputForSolving.solveOneStep();


		if(ourSentSudoku.squaresSolved == 81)
		{
			completeText.append(bundleInTilde(incomingText));

			completeText.append(bundleInTilde(puzzleDoneMessage));

			setStepsButton(stepsButtonPurpose.Nothing);
		}
		else if(incomingText == null)
		{
			completeText.append(bundleInTilde(noProgressMessage));

			setStepsButton(stepsButtonPurpose.GoToLastResort);
		}
		else
		{
			completeText.append(bundleInTilde(incomingText));
		}


		updateCurrentStatus(1,true);

	} // advanceOneStep()


	// Solve all at once

	private void oneBigSolve()
	{
		completeText = new StringBuilder("");

		String incomingText;


		completeText.append(bundleInTilde("Puzzle Start!"));

		do
		{
			incomingText = inputForSolving.solveOneStep();

			if(incomingText != null)
			{
				completeText.append(bundleInTilde(incomingText));
			}
		}
		while(incomingText != null);


		if(ourSentSudoku.squaresSolved == 81)
		{
			completeText.append(bundleInTilde(puzzleDoneMessage));

			updateCurrentStatus(1,true);
		}
		else
		{
			completeText.append(bundleInTilde(noProgressMessage));

			updateCurrentStatus(1,false);
			enactLastResort(true);
		}

	} // oneBigSolve()


	// Last Resort once logic techniques run out

	private void enactLastResort(boolean calledInOneBigSolve)
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


		// Put everything in the UI

		if(calledInOneBigSolve)
		{
			completeText.append(bundleInTilde(lastResortMessage));
		}
		else
		{
			completeText = new StringBuilder(bundleInTilde(lastResortMessage));

			setStepsButton(stepsButtonPurpose.Nothing);
		}

		updateCurrentStatus(2,true);

	} // enactLastResort()


	// Turn user controls off

	public void turnControlsOff()
	{
		inputMap.clear();
		actionMap.clear();

		itsTheSolveButton.setEnabled(false);
		itsTheUndoSolveButton.setEnabled(true);
		itsTheLoadSudokuButton.setEnabled(false);

		// Nothing here about the beginStepsButton;
		// that has to be handled separately on the
		// one occasion this function is called.

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

		setStepsButton(stepsButtonPurpose.StartSlowSolve);

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
		setLayout(null);


		// ~~~~~~~~~~ 
		// Implement Buttons & Text Area
		// ~~~~~~~~~~


		// Save Sudoku Button

		itsTheSaveSudokuButton.setBounds(130,665,120,25);
		itsTheSaveSudokuButton.setFocusPainted(false);
		add(itsTheSaveSudokuButton);

		itsTheSaveSudokuButton.setAction(saveTheSudoku);
		itsTheSaveSudokuButton.setText("Save Sudoku");


		// Load Sudoku Button

		itsTheLoadSudokuButton.setBounds(130,715,120,25);
		itsTheLoadSudokuButton.setFocusPainted(false);
		add(itsTheLoadSudokuButton);

		itsTheLoadSudokuButton.setAction(loadTheSudoku);
		itsTheLoadSudokuButton.setText("Load Sudoku");


		// Reset Button

		itsTheResetButton.setBounds(490,665,120,25);
		itsTheResetButton.setFocusPainted(false);
		add(itsTheResetButton);

		itsTheResetButton.setAction(resetSudoku);
		itsTheResetButton.setText("Reset All");


		// Undo Solve Button

		itsTheUndoSolveButton.setBounds(490,715,120,25);
		itsTheUndoSolveButton.setFocusPainted(false);
		add(itsTheUndoSolveButton);

		itsTheUndoSolveButton.setAction(undoTheSolve);
		itsTheUndoSolveButton.setText("Undo Solve");


		// Solve Button

		itsTheSolveButton.setBounds(280,655,180,45);
		itsTheSolveButton.setFocusPainted(false);
		add(itsTheSolveButton);

		itsTheSolveButton.setAction(enterAction);
		itsTheSolveButton.setText("Full Solve");


		// Begin Steps Button

		itsTheBeginStepsButton.setBounds(280,715,180,25);
		itsTheBeginStepsButton.setFocusPainted(false);
		add(itsTheBeginStepsButton);


		// Scroll Pane & Text Area

		itsTheScrollPane.setBounds(675,100,425,540);
		itsTheScrollPane.setFocusable(false);

		itsTheTextArea.setLineWrap(true);
		itsTheTextArea.setWrapStyleWord(true);
		itsTheTextArea.setEditable(false);
		itsTheTextArea.setFocusable(false);

		add(itsTheScrollPane);


		// ~~~~~~~~~~ 
		// ~~~~~~~~~~
		// ~~~~~~~~~~


		// Add Square Highlight

		recToWorkWith.setOpaque(false);
		recToWorkWith.setBounds(gridLeftX,gridTopY,gridTotalWidth,gridTotalHeight);

		add(recToWorkWith);

		recToWorkWith.setLayout(null);


		// Draw Sudoku Grid Lines

		gridToWorkWith.setOpaque(false);
		gridToWorkWith.setBounds(0,0,gridTotalWidth,gridTotalHeight);

		recToWorkWith.add(gridToWorkWith);

		gridToWorkWith.setLayout(null);


		int consX;
		int consY;

		// Add Labels for Rows & Cols

		for(int i=0;i<numRowInGrid;i++)
		{
			consX = gridLeftX-gridSquareWidth;
			consY = gridTopY+(i*gridSquareHeight);

			rowLabels[i] = new OurRowColLabel(HouseType.Row,i);
			rowLabels[i].setOpaque(false);
			rowLabels[i].setBounds(consX,consY,gridSquareWidth,gridSquareHeight);
			add(rowLabels[i]);
		}

		for(int j=0;j<numColInGrid;j++)
		{
			consX = gridLeftX+(j*gridSquareWidth);
			consY = gridTopY-gridSquareHeight;

			colLabels[j] = new OurRowColLabel(HouseType.Col,j);
			colLabels[j].setOpaque(false);
			colLabels[j].setBounds(consX,consY,gridSquareWidth,gridSquareHeight);
			add(colLabels[j]);
		}


		// Add Objects To Accept & Display Numbers

		for(int i=0;i<numRowInGrid;i++)
		{
			for(int j=0;j<numColInGrid;j++)
			{
				consX = j*gridSquareWidth;
				consY = i*gridSquareHeight;

				fillInMap[i][j] = new OurNumHolder(i,j);
				fillInMap[i][j].setOpaque(false);
				fillInMap[i][j].setBounds(consX,consY,gridSquareWidth,gridSquareHeight);
				gridToWorkWith.add(fillInMap[i][j]);
			}
		}


		// This just makes it so hitting spacebar does nothing

		InputMap im = (InputMap)UIManager.get("Button.focusInputMap");
		im.put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
		im.put(KeyStroke.getKeyStroke("released SPACE"), "none");


		// Ensure user control is allowed at start

		turnControlsOn();
		itsTheResetButton.setEnabled(true);
		itsTheSaveSudokuButton.setEnabled(true);


		// Last bit of JFrame setup

		getContentPane().setBackground( new Color(255,225,225) );

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200,800);
		setVisible(true);
	}

} // DrawNumsConstructor, our outermost class