import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.lang.StringBuilder;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public class DrawNumsConstructor extends PaintedObjects
{
	// Whether the arrow keys will currently move the square around
	// and the number keys will enter numbers into the Sudoku

	// Constructor switches it to True

	private boolean controlsOn = false;


	// Declare and initialize all KeyStroke

	private KeyStroke pressRight = KeyStroke.getKeyStroke("RIGHT");
	private KeyStroke pressLeft = KeyStroke.getKeyStroke("LEFT");
	private KeyStroke pressUp = KeyStroke.getKeyStroke("UP");
	private KeyStroke pressDown = KeyStroke.getKeyStroke("DOWN");
	private KeyStroke pressOne = KeyStroke.getKeyStroke("1");
	private KeyStroke pressTwo = KeyStroke.getKeyStroke("2");
	private KeyStroke pressThree = KeyStroke.getKeyStroke("3");
	private KeyStroke pressFour = KeyStroke.getKeyStroke("4");
	private KeyStroke pressFive = KeyStroke.getKeyStroke("5");
	private KeyStroke pressSix = KeyStroke.getKeyStroke("6");
	private KeyStroke pressSeven = KeyStroke.getKeyStroke("7");
	private KeyStroke pressEight = KeyStroke.getKeyStroke("8");
	private KeyStroke pressNine = KeyStroke.getKeyStroke("9");
	private KeyStroke pressSpace = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0);
	private KeyStroke pressBack = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,0);
	private KeyStroke pressDelete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0);
	private KeyStroke pressEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);


	// Create all JButton

	private JButton itsTheResetButton = new JButton();
	private JButton itsTheSolveButton = new JButton();
	private JButton itsTheUndoSolveButton = new JButton();
	private JButton itsTheSaveSudokuButton = new JButton();
	private JButton itsTheLoadSudokuButton = new JButton();
	private JButton itsTheBeginStepsButton = new JButton();


	// JTextArea for holding explanations of each step

	JTextArea itsTheTextArea = new JTextArea();

	private int vertScrollPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
	private int horiScrollPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;

	private JScrollPane itsTheScrollPane = new JScrollPane(itsTheTextArea,vertScrollPolicy,horiScrollPolicy);


	// StringBuilder for appending to JTextArea. 

	private StringBuilder completeText = null;

	private String noProgressMessage = "No further progress can be made with the integrated logical methods.";
	private String lastResortMessage = "Puzzle is complete, through trial and error.";
	private String puzzleDoneMessage = "Puzzle is complete!";


	// Misc. Important

	String[][] stringCompForFinal = new String[9][9];

	PopUpPane itsThePopUpPane = new PopUpPane(this);

	JButtonCommands itsTheButtonCommands = new JButtonCommands(this);

	FullSudoku ourSentSudoku;

	CheckStartingNums ourStartChecker;

	UsingLogicalMethods inputForSolving;

	LastResort inputForLastResort;


	// Create InputMap and ActionMap

	private InputMap inputMap = recToWorkWith.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
	private ActionMap actionMap = recToWorkWith.getActionMap();


	// Solving Actions

	private Action enterAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			finalAct(true);
		}
	};

	private Action slowSolveStartAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			finalAct(false);
		}
	};

	private Action advanceStepAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			advanceOneStep();
		}
	};

	private Action lastResortAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			enactLastResort(false);
		}
	};

	private Action nothingAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{

		}
	};


	// Slightly simpler function for assigning KeyStrokes to Actions

	private void setTheAction(KeyStroke a, String b, Action c)
	{
		inputMap.put(a,b);
		actionMap.put(b,c);
	}

	// Slightly simpler function for assigning KeyStrokes to NumActions

	private void setNumAction(KeyStroke a, String b, NumAction c)
	{
		inputMap.put(a,b);
		actionMap.put(b,c);
	}


	// Determines the current purpose of the single-step button

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
			itsTheBeginStepsButton.setText("Slow Solve");
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

				turnControlsOff();

				recToWorkWith.setDrawStatus(false);
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
							fillInMap[i][j].addedOnHumanSolve = true;
						else if(pointOfSolve == 2)
							fillInMap[i][j].addedOnLastResort = true;
					}

					fillInMap[i][j].setNum( ourSentSudoku.SudokuMap[i][j].result );
					fillInMap[i][j].setPossArray( ourSentSudoku.SudokuMap[i][j].possArray );
				}
			}
		}


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

			itsTheTextArea.append(completeText.toString());

			completeText = null;
		}

	} // updateCurrentStatus()


	// Surrounds a String with tildes and blank lines, for now

	private String bundleInTilde(String a)
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

	void turnControlsOff()
	{
		if(controlsOn)
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
	}


	// Turn user controls on

	void turnControlsOn()
	{
		if(!(controlsOn))
		{
			setTheAction(pressRight,"rightAction",rightAction);
			setTheAction(pressLeft,"leftAction",leftAction);
			setTheAction(pressUp,"upAction",upAction);
			setTheAction(pressDown,"downAction",downAction);

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
	}


	// Resets entire program to starting state

	private Action resetSudoku = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			itsTheButtonCommands.resetEverything();
		}
	};

	// Reverts puzzle to what the user input

	private Action undoTheSolve = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			itsTheButtonCommands.revertToUserInput();
		}
	};

	// Save Sudoku

	private Action saveTheSudoku = new AbstractAction()
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

	private Action loadTheSudoku = new AbstractAction()
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

	DrawNumsConstructor()
	{
		// Temporary

		setLayout(null);


		// ~~~~~~~~~~ 
		// ~~~~~~~~~~
		// ~~~~~~~~~~


		// Add Initialization Details

		sudokuGridBackdrop.setOpaque(false);
		sudokuGridBackdrop.setBounds(40,40,600,600);

		add(sudokuGridBackdrop);

		sudokuGridBackdrop.applySizeValues();


		// ~~~~~~~~~~ 
		// ~~~~~~~~~~
		// ~~~~~~~~~~


		sudokuGridBackdrop.setLayout( new BorderLayout() );

		// Col Labels

		colLabelsToWorkWith.setOpaque(false);
		colLabelsToWorkWith.setPreferredSize( new Dimension(sudokuBackdropWidth,gridSquareHeight) );

		// Row Labels

		rowLabelsToWorkWith.setOpaque(false);
		rowLabelsToWorkWith.setPreferredSize( new Dimension(gridSquareWidth,gridTotalHeight) );

		// Square Highlight

		recToWorkWith.setOpaque(false);
		recToWorkWith.setPreferredSize( new Dimension(gridTotalWidth,gridTotalHeight) );

		// ADD THEM

		sudokuGridBackdrop.add(colLabelsToWorkWith, BorderLayout.NORTH );
		sudokuGridBackdrop.add(rowLabelsToWorkWith, BorderLayout.WEST);
		sudokuGridBackdrop.add(recToWorkWith, BorderLayout.CENTER );


		// ~~~~~~~~~~ 
		// ~~~~~~~~~~
		// ~~~~~~~~~~


		recToWorkWith.setLayout( new BorderLayout() );

		// Add Sudoku Grid Lines

		gridToWorkWith.setOpaque(false);
		gridToWorkWith.setPreferredSize( new Dimension(gridTotalWidth,gridTotalHeight) );

		recToWorkWith.add(gridToWorkWith, BorderLayout.CENTER);


		// ~~~~~~~~~~ 
		// ~~~~~~~~~~
		// ~~~~~~~~~~


		gridToWorkWith.setLayout( new GridLayout(numRowInGrid,numColInGrid) );

		Dimension tileMeasurements = new Dimension(gridTotalWidth/numColInGrid,gridTotalHeight/numRowInGrid);

		// Create NumHolders To Accept & Display Numbers

		for(int i=0;i<numRowInGrid;i++)
		{
			for(int j=0;j<numColInGrid;j++)
			{
				fillInMap[i][j] = new NumHolder(i,j);
				fillInMap[i][j].setOpaque(false);
//	Size Test	fillInMap[i][j].setBorder(BorderFactory.createLineBorder(Color.blue));
				fillInMap[i][j].setPreferredSize(tileMeasurements);
			}
		}

		// Add NumHolders

		for(int i=0;i<numRowInGrid;i++)
		{
			for(int j=0;j<numColInGrid;j++)
			{
				gridToWorkWith.add(fillInMap[i][j]);
			}
		}


		// ~~~~~~~~~~
		// ~~~~~~~~~~
		// ~~~~~~~~~~


		westButtonBackdrop.setOpaque(false);
		westButtonBackdrop.setBounds(100,640,180,120);

		add(westButtonBackdrop);

		// Save Sudoku Button

		itsTheSaveSudokuButton.setFocusPainted(false);
		itsTheSaveSudokuButton.setMinimumSize(standardButtonSize);
		itsTheSaveSudokuButton.setMaximumSize(standardButtonSize);
		itsTheSaveSudokuButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

		// Load Sudoku Button

		itsTheLoadSudokuButton.setFocusPainted(false);
		itsTheLoadSudokuButton.setMinimumSize(standardButtonSize);
		itsTheLoadSudokuButton.setMaximumSize(standardButtonSize);
		itsTheLoadSudokuButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

		// ADD THEM

		westButtonBackdrop.add( Box.createRigidArea(standardButtonSize) );
		westButtonBackdrop.add(itsTheSaveSudokuButton);
		westButtonBackdrop.add( Box.createRigidArea(standardButtonSize) );
		westButtonBackdrop.add(itsTheLoadSudokuButton);

		// Set Actions

		itsTheSaveSudokuButton.setAction(saveTheSudoku);
		itsTheSaveSudokuButton.setText("Save Sudoku");

		itsTheLoadSudokuButton.setAction(loadTheSudoku);
		itsTheLoadSudokuButton.setText("Load Sudoku");


		// ~~~~~~~~~~
		// ~~~~~~~~~~
		// ~~~~~~~~~~


		centerButtonBackdrop.setOpaque(false);
		centerButtonBackdrop.setBounds(280,640,180,120);

		add(centerButtonBackdrop);

		// Solve Button

		itsTheSolveButton.setFocusPainted(false);
		itsTheSolveButton.setMinimumSize(solveButtonSize);
		itsTheSolveButton.setMaximumSize(solveButtonSize);
		itsTheSolveButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

		// Begin Steps Button

		itsTheBeginStepsButton.setFocusPainted(false);
		itsTheBeginStepsButton.setMinimumSize(standardButtonSize);
		itsTheBeginStepsButton.setMaximumSize(standardButtonSize);
		itsTheBeginStepsButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

		// ADD THEM

		centerButtonBackdrop.add( Box.createRigidArea(emptySizeBlock) );
		centerButtonBackdrop.add(itsTheSolveButton);
		centerButtonBackdrop.add( Box.createRigidArea(emptySizeBlock) );
		centerButtonBackdrop.add(itsTheBeginStepsButton);

		// Set Action

		itsTheSolveButton.setAction(enterAction);
		itsTheSolveButton.setText("Complete Solve");


		// ~~~~~~~~~~
		// ~~~~~~~~~~
		// ~~~~~~~~~~


		eastButtonBackdrop.setOpaque(false);
		eastButtonBackdrop.setBounds(460,640,180,120);

		add(eastButtonBackdrop);

		// Reset Button

		itsTheResetButton.setFocusPainted(false);
		itsTheResetButton.setMinimumSize(standardButtonSize);
		itsTheResetButton.setMaximumSize(standardButtonSize);
		itsTheResetButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

		// Undo Solve Button

		itsTheUndoSolveButton.setFocusPainted(false);
		itsTheUndoSolveButton.setMinimumSize(standardButtonSize);
		itsTheUndoSolveButton.setMaximumSize(standardButtonSize);
		itsTheUndoSolveButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

		// ADD THEM

		eastButtonBackdrop.add( Box.createRigidArea(standardButtonSize) );
		eastButtonBackdrop.add(itsTheResetButton);
		eastButtonBackdrop.add( Box.createRigidArea(standardButtonSize) );
		eastButtonBackdrop.add(itsTheUndoSolveButton);

		// Set Actions

		itsTheResetButton.setAction(resetSudoku);
		itsTheResetButton.setText("Reset All");

		itsTheUndoSolveButton.setAction(undoTheSolve);
		itsTheUndoSolveButton.setText("Undo Solve");


		// ~~~~~~~~~~
		// ~~~~~~~~~~
		// ~~~~~~~~~~


		// Scroll Pane & Text Area

		itsTheScrollPane.setBounds(675,100,425,540);
		itsTheScrollPane.setFocusable(false);

		itsTheTextArea.setLineWrap(true);
		itsTheTextArea.setWrapStyleWord(true);
		itsTheTextArea.setEditable(false);
		itsTheTextArea.setFocusable(false);

		add(itsTheScrollPane);


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
		setSize(1200,825);
		setVisible(true);
	}

} // DrawNumsConstructor, our outermost class