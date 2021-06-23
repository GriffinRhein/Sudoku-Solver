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

	KeyStroke NakedSingleKey = KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke HiddenSingleKey = KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke PointingPairKey1 = KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke PointingPairKey2 = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke ClaimingPairKey1 = KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke ClaimingPairKey2 = KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke NakedPairKey1 = KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke NakedPairKey2 = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke HiddenPairKey1 = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke HiddenPairKey2 = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke NakedTripleKey1 = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke NakedTripleKey2 = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke HiddenTripleKey1 = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke HiddenTripleKey2 = KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke NakedQuadKey1 = KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke NakedQuadKey2 = KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke HiddenQuadKey1 = KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke HiddenQuadKey2 = KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke XWingKey1 = KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke XWingKey2 = KeyStroke.getKeyStroke(KeyEvent.VK_SEMICOLON, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke SwordfishKey1 = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke SwordfishKey2 = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke JellyfishKey1 = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke JellyfishKey2 = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke XYWingKey1 = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.SHIFT_DOWN_MASK);
	KeyStroke XYWingKey2 = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.SHIFT_DOWN_MASK);


	OurRectangle recToWorkWith = new OurRectangle();
	OurCoreGrid gridToWorkWith = new OurCoreGrid();
	OurNumHolder[][] fillInMap = new OurNumHolder[9][9];
	OurSolveText textToWorkWith = new OurSolveText();

	ClickButton itsTheResetButton = new ClickButton();
	ClickButton itsTheSolveButton = new ClickButton();
	ClickButton itsTheUndoSolveButton = new ClickButton();

	String[][] stringCompForFinal = new String[9][9];

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


	// Test Sudoku

	Action NakedSingleAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {4,null,2,7,null,6,null,8,9},
										 {null,null,null,null,null,null,null,null,6},
										 {5,6,null,null,1,null,null,7,null},
										 {6,9,null,null,5,null,2,1,null},
										 {1,null,null,null,4,3,null,null,8},
										 {null,8,7,null,9,null,null,null,null},
										 {null,3,null,null,7,4,null,6,5},
										 {8,null,null,null,null,5,null,null,null},
										 {null,null,null,9,null,8,4,null,1} };

			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action HiddenSingleAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYesYes = { {null,1,null,9,null,null,7,4,null},
										 {null,null,null,8,null,null,null,null,3},
										 {null,7,null,null,2,null,6,9,null},
										 {null,null,4,null,3,null,2,null,null},
										 {null,null,null,6,null,2,null,null,null},
										 {null,null,8,null,1,null,3,null,null},
										 {null,8,1,null,7,null,null,3,null},
										 {3,null,null,null,null,8,null,null,null},
										 {null,6,9,null,null,3,null,2,null} };

			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYesYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action PointingPairAction1 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {9,8,4,null,null,null,null,null,null},
										 {null,null,null,5,null,null,null,4,null},
										 {null,null,null,null,null,null,null,null,2},
										 {null,null,6,null,9,7,2,null,null},
										 {null,null,3,null,null,2,null,null,null},
										 {null,null,null,null,null,null,null,1,null},
										 {null,null,5,null,6,null,null,null,3},
										 {4,null,7,null,5,1,8,9,null},
										 {null,3,null,null,null,9,7,null,null} };

			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action PointingPairAction2 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYesYes = { {3,4,null,null,null,6,null,7,null},
										 {null,8,null,null,null,null,9,3,null},
										 {null,null,2,null,null,null,null,6,null},
										 {null,null,null,null,1,null,null,null,null},
										 {null,9,7,null,null,null,8,5,null},
										 {null,null,null,null,null,2,null,null,null},
										 {null,null,null,null,null,null,null,null,null},
										 {null,null,null,6,null,8,null,9,null},
										 {null,null,null,9,2,3,7,8,5} };

			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYesYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action ClaimingPairAction1 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {null,1,null,null,null,null,4,null,null},
										  {null,null,null,6,null,3,null,null,null},
										  {null,null,6,null,8,null,null,null,3},
										  {8,null,null,9,5,null,1,null,null},
										  {null,2,null,4,null,null,null,5,8},
										  {7,9,5,null,null,null,null,null,null},
										  {null,null,null,5,null,null,7,null,null},
										  {null,null,null,null,null,7,null,null,null},
										  {null,null,null,null,3,null,6,4,1} };

			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action ClaimingPairAction2 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {null,6,2,null,null,8,null,null,1},
										  {9,null,null,null,null,null,null,null,6},
										  {null,null,null,null,null,null,null,8,null},
										  {4,7,8,null,null,null,null,null,null},
										  {null,null,null,null,null,9,null,7,3},
										  {null,null,null,null,null,null,4,2,null},
										  {null,null,5,null,null,1,null,null,null},
										  {null,null,7,6,8,null,3,null,null},
										  {null,null,null,null,3,2,7,null,8} };

			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action NakedPairAction1 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {7,null,null,null,null,9,null,3,null},
										 {null,null,null,1,null,5,null,null,6},
										 {4,null,null,2,6,null,null,null,9},
										 {null,null,2,null,8,3,9,5,1},
										 {null,null,7,null,null,null,null,null,null},
										 {null,null,5,6,null,null,null,null,null},
										 {null,null,null,null,null,null,null,null,3},
										 {1,null,null,null,null,null,null,6,null},
										 {null,null,null,null,null,4,null,1,null} };

			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action NakedPairAction2 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYesYes = { {null,null,7,null,null,4,null,2,null},
										 {null,null,null,null,null,2,6,null,null},
										 {null,4,null,null,5,6,null,7,8},
										 {3,1,null,null,null,null,null,4,null},
										 {null,6,null,null,null,null,3,null,null},
										 {null,null,null,null,null,null,null,null,1},
										 {null,9,6,null,null,1,null,null,null},
										 {2,null,null,null,null,null,null,5,7},
										 {null,null,null,null,null,null,null,6,null} };

			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYesYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action HiddenPairAction1 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {null,null,null,null,3,2,null,null,null},
										 {null,null,null,null,null,null,null,null,null},
										 {null,null,7,6,null,null,9,1,4},
										 {null,9,6,null,null,null,8,null,null},
										 {null,null,5,null,null,8,null,null,null},
										 {null,3,null,null,4,null,null,null,5},
										 {null,5,null,2,null,null,null,null,null},
										 {7,null,null,null,null,null,5,6,null},
										 {9,null,4,null,1,null,null,null,null} };

			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action HiddenPairAction2 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYesYes = { {null,null,null,null,null,null,null,null,null},
										 {null,null,null,null,4,2,7,3,null},
										 {null,null,6,7,null,null,null,4,null},
										 {null,9,4,null,null,null,null,null,null},
										 {null,null,null,null,9,6,null,null,null},
										 {null,null,7,null,null,null,null,2,3},
										 {1,null,null,null,null,null,null,8,5},
										 {null,6,null,null,8,null,2,7,null},
										 {null,null,5,null,1,null,null,null,null} };

			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYesYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action NakedTripleAction1 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {null,null,null,null,null,4,null,null,null},
										 {null,null,null,1,7,null,6,null,null},
										 {4,8,null,3,5,6,1,null,null},
										 {null,null,4,null,null,7,5,null,null},
										 {null,null,null,null,1,null,7,null,null},
										 {5,null,null,null,2,null,null,3,4},
										 {9,5,null,null,null,null,null,null,6},
										 {1,2,null,null,null,null,null,null,8},
										 {null,null,null,null,null,null,null,null,null} };

			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action NakedTripleAction2 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYesYes = { {3,9,null,null,null,null,null,null,null},
										 {null,null,null,null,null,null,6,5,null},
										 {null,null,7,null,null,null,3,4,9},
										 {null,null,9,3,8,null,null,null,6},
										 {null,null,null,null,5,4,null,null,null},
										 {8,5,3,null,null,null,null,null,null},
										 {null,null,null,8,null,null,1,null,null},
										 {null,null,2,9,4,null,null,6,null},
										 {4,null,null,null,null,null,null,null,7} };

			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYesYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action HiddenTripleAction1 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {2,null,null,null,null,null,4,null,null},
										 {5,null,null,null,null,null,null,null,6},
										 {null,null,1,null,3,4,null,8,null},
										 {null,null,null,5,null,null,null,4,null},
										 {null,null,null,null,null,null,null,null,null},
										 {null,6,null,7,9,null,null,null,null},
										 {null,9,null,2,null,null,6,null,null},
										 {null,null,3,null,null,9,null,null,1},
										 {null,null,null,null,8,null,null,3,7} };


			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action HiddenTripleAction2 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYesYes = { {5,null,null,6,2,null,null,3,7},
										 {null,null,4,8,9,null,null,null,null},
										 {null,null,null,null,5,null,null,null,null},
										 {9,3,null,null,null,null,null,null,null},
										 {null,2,null,null,null,null,6,null,5},
										 {7,null,null,null,null,null,null,null,3},
										 {null,null,null,null,null,9,null,null,null},
										 {null,null,null,null,null,null,7,null,null},
										 {6,8,null,5,7,null,null,null,2} };


			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYesYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action NakedQuadAction1 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {null,null,null,null,null,null,null,6,null},
										 {null,null,null,null,3,null,null,4,7},
										 {null,3,2,5,null,null,null,null,null},
										 {6,null,null,null,null,7,null,null,5},
										 {2,null,7,null,1,null,9,null,8},
										 {null,8,1,null,null,4,null,null,null},
										 {null,null,null,null,null,2,null,null,null},
										 {null,null,null,null,null,null,null,null,1},
										 {null,null,5,8,7,null,null,null,null} };


			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action NakedQuadAction2 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYesYes = { {null,3,null,null,8,null,null,null,null},
										 {9,null,8,null,4,1,null,null,null},
										 {null,null,1,9,null,null,2,null,7},
										 {null,2,5,4,null,null,6,null,null},
										 {null,null,3,null,1,7,null,5,null},
										 {7,null,null,null,null,null,null,null,null},
										 {null,null,null,null,null,null,null,null,null},
										 {null,null,null,null,null,5,1,null,6},
										 {null,null,null,3,null,null,null,9,8} };


			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYesYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action HiddenQuadAction1 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {8,null,null,5,7,null,2,9,null},
										 {3,9,null,null,null,null,null,null,null},
										 {null,null,null,2,null,null,null,null,null},
										 {null,null,1,null,null,null,5,null,8},
										 {null,null,null,4,9,6,null,null,null},
										 {null,null,null,8,null,null,null,null,null},
										 {2,null,9,null,null,null,null,null,1},
										 {null,null,8,null,null,null,null,7,null},
										 {5,6,null,null,null,null,null,8,2} };


			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action HiddenQuadAction2 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYesYes = { {null,3,null,null,null,null,null,1,null},
										 {null,null,8,null,9,null,null,null,null},
										 {4,null,null,6,null,8,null,null,null},
										 {null,null,null,null,7,6,9,4,null},
										 {null,null,null,null,null,null,5,2,null},
										 {null,null,null,1,2,4,null,null,null},
										 {2,null,6,null,null,null,1,9,null},
										 {null,null,null,7,null,null,null,null,null},
										 {null,9,5,null,null,null,4,7,null} };


			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYesYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action XWingAction1 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {null,null,null,null,null,null,null,null,null},
										 {7,6,null,null,null,3,null,null,2},
										 {null,null,2,6,4,null,null,null,9},
										 {4,null,3,9,null,null,null,7,null},
										 {null,null,null,null,null,4,9,null,3},
										 {null,null,5,null,null,null,null,2,null},
										 {null,1,null,5,6,null,null,null,null},
										 {3,7,null,null,9,null,null,4,1},
										 {null,null,null,null,null,null,null,6,null} };


			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action XWingAction2 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {9,null,null,null,6,2,7,null,null},
										 {null,null,5,null,null,3,null,null,null},
										 {null,null,null,null,null,null,null,null,6},
										 {7,null,null,null,3,null,null,null,null},
										 {null,null,null,null,null,9,null,null,null},
										 {8,null,2,null,4,5,null,null,9},
										 {null,null,3,5,null,1,null,2,8},
										 {null,4,null,null,null,null,null,null,5},
										 {null,1,null,null,null,null,null,null,null} };


			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action SwordfishAction1 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {1,6,null,5,4,null,null,7,null},
										 {null,null,8,null,null,1,null,3,null},
										 {null,3,null,8,null,null,null,null,null},
										 {7,null,null,null,5,null,null,6,9},
										 {6,null,null,9,null,2,null,5,7},
										 {null,null,null,null,null,null,null,null,null},
										 {null,null,null,null,3,null,null,4,null},
										 {null,null,null,null,null,null,null,1,6},
										 {null,null,null,1,6,4,5,null,null} };


			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action SwordfishAction2 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {1,5,null,null,null,3,9,null,null},
										 {null,null,null,null,null,null,8,null,null},
										 {null,null,null,null,5,null,null,null,null},
										 {null,3,8,6,null,null,2,null,7},
										 {null,null,null,null,null,null,null,null,8},
										 {null,2,null,5,null,null,6,null,null},
										 {null,null,null,null,4,6,null,null,9},
										 {3,7,null,null,null,null,1,null,null},
										 {4,8,null,3,null,2,null,null,null} };


			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action JellyfishAction1 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {2,null,null,null,null,null,null,null,3},
										 {null,8,null,null,3,null,null,5,null},
										 {null,null,3,4,null,2,1,null,null},
										 {null,null,1,2,null,5,4,null,null},
										 {null,null,null,null,9,null,null,null,null},
										 {null,null,9,3,null,8,6,null,null},
										 {null,null,2,5,null,6,9,null,null},
										 {null,9,null,null,2,null,null,7,null},
										 {4,null,null,null,null,null,null,null,1} };


			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action JellyfishAction2 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = { {2,null,null,null,null,6,null,3,null},
										 {null,null,null,null,null,1,null,null,2},
										 {null,null,null,null,5,null,1,null,6},
										 {null,null,4,9,null,8,5,null,3},
										 {null,2,null,null,1,null,null,4,null},
										 {3,null,5,4,null,2,8,null,null},
										 {5,null,6,null,9,null,null,null,null},
										 {8,null,null,6,null,null,null,null,null},
										 {null,1,null,8,null,null,null,null,4} };


			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action XYWingAction1 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = {{null,null,null,null,6,null,null,null,null},
										 {null,null,null,null,1,null,8,6,3},
										 {null,null,3,null,null,9,null,null,null},
										 {9,null,4,null,null,null,null,null,null},
										 {3,null,null,null,null,null,7,null,4},
										 {5,7,null,8,2,null,null,null,null},
										 {null,null,null,null,null,6,5,8,null},
										 {6,9,null,null,null,7,null,null,null},
										 {null,null,null,null,4,null,null,3,null} };


			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

			finalAct();
		}
	};

	Action XYWingAction2 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Integer[][] testingTheYes = {{null,1,null,null,null,null,5,null,8},
										 {null,null,null,4,null,3,null,null,null},
										 {null,5,6,7,null,null,null,null,null},
										 {null,null,null,null,2,null,null,8,null},
										 {4,null,null,null,null,null,3,null,2},
										 {2,null,null,3,7,6,null,null,1},
										 {9,null,8,null,null,null,2,5,4},
										 {null,null,null,null,null,7,null,null,null},
										 {null,null,null,null,null,null,null,null,3} };


			for(int i=0;i<ROWS;i++)
			{
				for(int j=0;j<COLS;j++)
				{
					fillInMap[i][j].setNum(testingTheYes[i][j]);
				}
			}

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

			controlsOn = false;
		}

		ourRectWidth = 0;
		ourRectHeight = 0;

		recToWorkWith.repaint();

		textToWorkWith.setNumSquaresSolved(theSudokuWeSendTo.mySudoku.squaresSolved);
		textToWorkWith.setCompletionCode(theSudokuWeSendTo.mySudoku.isPuzzleComplete());
		textToWorkWith.repaint();
	}


	// Maps debugging keys to debugging actions

	public void setDebugActions()
	{
		setTheAction(NakedSingleKey,"NakedSingleString",NakedSingleAction);
		setTheAction(HiddenSingleKey,"HiddenSingleString",HiddenSingleAction);
		setTheAction(PointingPairKey1,"PointingPairString1",PointingPairAction1);
		setTheAction(PointingPairKey2,"PointingPairString2",PointingPairAction2);
		setTheAction(ClaimingPairKey1,"ClaimingPairString1",ClaimingPairAction1);
		setTheAction(ClaimingPairKey2,"ClaimingPairString2",ClaimingPairAction2);
		setTheAction(NakedPairKey1,"NakedPairString1",NakedPairAction1);
		setTheAction(NakedPairKey2,"NakedPairString2",NakedPairAction2);
		setTheAction(HiddenPairKey1,"HiddenPairString1",HiddenPairAction1);
		setTheAction(HiddenPairKey2,"HiddenPairString2",HiddenPairAction2);
		setTheAction(NakedTripleKey1,"NakedTripleString1",NakedTripleAction1);
		setTheAction(NakedTripleKey2,"NakedTripleString2",NakedTripleAction2);
		setTheAction(HiddenTripleKey1,"HiddenTripleString1",HiddenTripleAction1);
		setTheAction(HiddenTripleKey2,"HiddenTripleString2",HiddenTripleAction2);
		setTheAction(NakedQuadKey1,"NakedQuadString1",NakedQuadAction1);
		setTheAction(NakedQuadKey2,"NakedQuadString2",NakedQuadAction2);
		setTheAction(HiddenQuadKey1,"HiddenQuadString1",HiddenQuadAction1);
		setTheAction(HiddenQuadKey2,"HiddenQuadString2",HiddenQuadAction2);
		setTheAction(XWingKey1,"XWingString1",XWingAction1);
		setTheAction(XWingKey2,"XWingString2",XWingAction2);
		setTheAction(SwordfishKey1,"SwordfishString1",SwordfishAction1);
		setTheAction(SwordfishKey2,"SwordfishString2",SwordfishAction2);
		setTheAction(JellyfishKey1,"JellyfishString1",JellyfishAction1);
		setTheAction(JellyfishKey2,"JellyfishString2",JellyfishAction2);
		setTheAction(XYWingKey1,"XYWingString1",XYWingAction1);
		setTheAction(XYWingKey2,"XYWingString2",XYWingAction2);

		// Add the rest of the test Sudoku you've been using
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

				setDebugActions();

				itsTheSolveButton.setEnabled(true);
				itsTheUndoSolveButton.setEnabled(false);

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

				setDebugActions();

				itsTheSolveButton.setEnabled(true);
				itsTheUndoSolveButton.setEnabled(false);

				controlsOn = true;
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

		setDebugActions();

		// Last bit of JFrame setup

		getContentPane().setBackground( new Color(255,225,225) );

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,800);
		setVisible(true);
	}


	// Main kicks things off by putting all of the above
	// in the Event Dispatch thread

	public static void main(String[] argv)
	{
		EventQueue.invokeLater(

		new Runnable()
		{
			@Override
			public void run()
			{
				new DrawNumsConstructor();
			}
		});
	}

} // DrawNumsConstructor, our outermost class