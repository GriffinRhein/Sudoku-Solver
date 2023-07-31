import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class PaintedObjects extends JFrame
{
	// Sudoku Grid Info

	final int numColInGrid = 9;
	final int numColBetweenThick = 3;

	final int numRowInGrid = 9;
	final int numRowBetweenThick = 3;

	final int numPossPerSquare = 9;


	// Current row & column we are highlighting

	int currentCol = 0;
	int currentRow = 0;


	// Drawing Tools

	private Color theBlack = new Color(0,0,0);
	private Color theRed = new Color(255,0,0);
	private Color theBlue = new Color(0,0,255);
	private Color thePurple = new Color(125,0,225);

	private Font myFont = new Font("TimesRoman",Font.PLAIN,42);
	private Canvas c1 = new Canvas();
	private FontMetrics fm = c1.getFontMetrics(myFont);

	private Font possArrayFont = new Font("TimesRoman",Font.PLAIN,18);
	private Canvas c2 = new Canvas();
	private FontMetrics possArrayFontFM = c2.getFontMetrics(possArrayFont);


	// Influenced by size of JFrame

	int gridSquareWidth;
	int gridSquareHeight;

	int fullGridWidth;
	int fullGridHeight;


	// Function which sets those values

	void setSizeValues(int everythingWidth, int everythingHeight)
	{
		gridSquareWidth = everythingWidth/(numColInGrid+11);
		gridSquareHeight = everythingHeight/(numRowInGrid+4);

		fullGridWidth = gridSquareWidth*numColInGrid;
		fullGridHeight = gridSquareHeight*numRowInGrid;

	} // setSizeValues()


	// ~~~~~~~~~~
	// ~~~~~~~~~~
	// ~~~~~~~~~~


	class ColLabels extends JPanel
	{
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g.create();

			// ~~~~~~~~~~

			g2d.setColor(theBlack);
			g2d.setFont(possArrayFont);

			int locY = gridSquareHeight/2 - possArrayFontFM.getHeight()/2 + possArrayFontFM.getAscent();
			int locX;

			String toWrite;

			for(int i=1;i<numColInGrid+1;i++)
			{
				toWrite = Integer.toString(i);

				locX = ((2*i+1)*(gridSquareWidth))/2 - possArrayFontFM.stringWidth(toWrite)/2;

				g2d.drawString(toWrite,locX,locY);
			}

			// ~~~~~~~~~~

			g2d.dispose();
		}

	} // ColLabels Class

	ColLabels theColLabels = new ColLabels();


	// ~~~~~~~~~~
	// ~~~~~~~~~~
	// ~~~~~~~~~~


	class RowLabels extends JPanel
	{
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g.create();

			// ~~~~~~~~~~

			g2d.setColor(theBlack);
			g2d.setFont(possArrayFont);

			int locY;
			int locX;

			String toWrite;

			for(int i=0;i<numColInGrid;i++)
			{
				toWrite = Integer.toString(i+1);

				locY = ((2*i+1)*(gridSquareHeight))/2 - possArrayFontFM.getHeight()/2 + possArrayFontFM.getAscent();
				locX = gridSquareWidth/2 - possArrayFontFM.stringWidth(toWrite)/2;

				g2d.drawString(toWrite,locX,locY);
			}

			// ~~~~~~~~~~

			g2d.dispose();
		}

	} // RowLabels class

	RowLabels theRowLabels = new RowLabels();


	// ~~~~~~~~~~
	// ~~~~~~~~~~
	// ~~~~~~~~~~


	class RecHighlight extends JPanel
	{
		private Color ourRectColor = new Color(28,222,144);
		private boolean showIt = true;

		void setDrawStatus(boolean a)
		{
			showIt = a;
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g.create();

			// ~~~~~~~~~~

			int whereX = currentCol*gridSquareWidth;
			int whereY = currentRow*gridSquareHeight;

			g2d.setColor(ourRectColor);

			if(showIt)
			{ g2d.fillRect(whereX,whereY,gridSquareWidth,gridSquareHeight); }
			else
			{ g2d.fillRect(whereX,whereY,0,0); }

			// ~~~~~~~~~~

			g2d.dispose();
		}

	} // RecHighlight class

	RecHighlight recToWorkWith = new RecHighlight();


	// ~~~~~~~~~~
	// ~~~~~~~~~~
	// ~~~~~~~~~~


	class GridLines extends JPanel
	{
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g.create();

			// ~~~~~~~~~~


			BasicStroke thickerBorder = new BasicStroke(1.5F);
			BasicStroke normalBorder = new BasicStroke(1F);

			g2d.setColor(new Color(0,0,0));
			g2d.setStroke(normalBorder);


			int handyCoord;

			// Draw Vertical Lines

			for(int i=0;i<=numColInGrid;i++)
			{
				handyCoord = i*gridSquareWidth;

				if(i % numColBetweenThick == 0)
				{
					g2d.setStroke(thickerBorder);
					g2d.drawLine(handyCoord,0,handyCoord,fullGridHeight);
					g2d.setStroke(normalBorder);
				}
				else
				{
					g2d.drawLine(handyCoord,0,handyCoord,fullGridHeight);
				}
			}

			// Draw Horizontal Lines

			for(int j=0;j<=numRowInGrid;j++)
			{
				handyCoord = j*gridSquareHeight;

				if(j % numRowBetweenThick == 0)
				{
					g2d.setStroke(thickerBorder);
					g2d.drawLine(0,handyCoord,fullGridWidth,handyCoord);
					g2d.setStroke(normalBorder);
				}
				else
				{
					g2d.drawLine(0,handyCoord,fullGridWidth,handyCoord);
				}
			}


			// ~~~~~~~~~~

			g2d.dispose();
		}

	} // GridLines class

	GridLines gridToWorkWith = new GridLines();


	// ~~~~~~~~~~
	// ~~~~~~~~~~
	// ~~~~~~~~~~


	class NumHolder extends JPanel
	{
		private String numHeld;
		private String[] endPossArray = new String[numPossPerSquare];

		private int ownRow;
		private int ownCol;

		private int[] howFarY;
		private int[] howFarX;

		private int pracY = gridSquareHeight/2 - fm.getHeight()/2 + fm.getAscent();
		private int pracX;

		private int possArrayPracY;
		private int possArrayPracX;


		boolean addedOnHumanSolve = false;
		boolean addedOnLastResort = false;

		boolean didSquareStartEmpty = true;

		private Integer stepSolved;
		private Integer[] stepPossEliminated = new Integer[9];


		// Constructor

		NumHolder(int selfRow, int selfCol)
		{
			// Initialize numHeld to a blank

			numHeld = "";

			// Initialize endPossArray to all empty strings

			for(int s=0;s<numPossPerSquare;s++)
			{
				endPossArray[s] = "";
			}

			// Initialize own row and column

			ownRow = selfRow;
			ownCol = selfCol;

			// Initialize info to display remaining possibilities

			howFarY = new int[]{0,0,0,20,20,20,40,40,40};
			howFarX = new int[]{0,20,40,0,20,40,0,20,40};

			// Initialize stepSolved to null

			stepSolved = null;

			// Initialize stepPossEliminated to nine null entries

			for(int s=0;s<stepPossEliminated.length;s++)
			{
				stepPossEliminated[s] = null;
			}
		}

		void resetItAll()
		{
			numHeld = "";

			for(int s=0;s<numPossPerSquare;s++)
			{
				endPossArray[s] = "";
			}

			stepSolved = null;
			
			for(int s=0;s<stepPossEliminated.length;s++)
			{
				stepPossEliminated[s] = null;
			}

			addedOnHumanSolve = false;
			addedOnLastResort = false;
			didSquareStartEmpty = true;
		}

		void setNum(Integer numToUse, Integer stepOfSolve)
		{
			if(numToUse != null)
			{
				if(stepOfSolve != null && numHeld.isBlank())
				{
					stepSolved = stepOfSolve;
				}

				numHeld = Integer.toString(numToUse);
			}
			else
			{
				numHeld = "";
			}
		}

		String getNum()
		{
			return numHeld;
		}

		void setPossArray(Integer[] Input, Integer stepOfSolve)
		{
			if(Input != null)
			{
				for(int i=0;i<Input.length;i++)
				{
					if(Input[i] == null)
					{
						if(endPossArray[i] != "")
						{
							stepPossEliminated[i] = stepOfSolve;
						}

						endPossArray[i] = "";
					}
					else
						endPossArray[i] = Integer.toString(Input[i]);
				}
			}
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g.create();

			// Set the color and font, then use own variables to
			// paint the correct number at the correct location

			if(numHeld.equals(""))
			{
				g2d.setColor(thePurple);

				g2d.setFont(possArrayFont);

				for(int w=0;w<endPossArray.length;w++)
				{
					possArrayPracY = 10 - possArrayFontFM.getHeight()/2 + possArrayFontFM.getAscent() + howFarY[w];
					possArrayPracX = 10 - possArrayFontFM.stringWidth(endPossArray[w])/2 + howFarX[w];

					g2d.drawString(endPossArray[w],possArrayPracX,possArrayPracY);

				}
			}

			else if(addedOnLastResort)
			{
				g2d.setColor(theRed);

				g2d.setFont(myFont);

				pracX = gridSquareWidth/2 - fm.stringWidth(numHeld)/2;
				g2d.drawString(numHeld,pracX,pracY);
			}


			else if(addedOnHumanSolve)
			{
				g2d.setColor(theBlue);

				g2d.setFont(myFont);

				pracX = gridSquareWidth/2 - fm.stringWidth(numHeld)/2;
				g2d.drawString(numHeld,pracX,pracY);
			}

			else
			{
				g2d.setColor(theBlack);

				g2d.setFont(myFont);

				pracX = gridSquareWidth/2 - fm.stringWidth(numHeld)/2;
				g2d.drawString(numHeld,pracX,pracY);
			}

			g2d.dispose();
		}

	} // NumHolder class

	NumHolder[][] fillInMap = new NumHolder[numRowInGrid][numColInGrid];


	// ~~~~~~~~~~
	// ~~~~~~~~~~
	// ~~~~~~~~~~


	class EmptyLayer extends JPanel
	{
		String name;

		EmptyLayer(String a)
		{
			name = a;
		}

	} // EmptyLayer class

	EmptyLayer northBorder = new EmptyLayer("North Border");
	EmptyLayer southBorder = new EmptyLayer("South Border");
	EmptyLayer westBorder = new EmptyLayer("West Border");
	EmptyLayer eastBorder = new EmptyLayer("East Border");
	EmptyLayer containsEverything = new EmptyLayer("Contains Everything");

	EmptyLayer sudokuSideBackdrop = new EmptyLayer("Sudoku Side Backdrop");
	EmptyLayer highMiddleGap = new EmptyLayer("Sudoku/Text Gap");
	EmptyLayer textSideBackdrop = new EmptyLayer("Text Side Backdrop");

	EmptyLayer panelOfButtonPanels = new EmptyLayer("Panel Of Button Panels");

	EmptyLayer emptyButtonBackdrop = new EmptyLayer("Empty Button Backdrop");
	EmptyLayer westButtonBackdrop = new EmptyLayer("Leftmost Button Backdrop");
	EmptyLayer centerButtonBackdrop = new EmptyLayer("Center Button Backdrop");
	EmptyLayer eastButtonBackdrop = new EmptyLayer("Rightmost Button Backdrop");

	EmptyLayer normalButtonGap1 = new EmptyLayer("First Button Gap");
	EmptyLayer normalButtonGap2 = new EmptyLayer("Second Button Gap");
	EmptyLayer normalButtonGap3 = new EmptyLayer("Third Button Gap");
	EmptyLayer normalButtonGap4 = new EmptyLayer("Fourth Button Gap");

	EmptyLayer solveButtonGap1 = new EmptyLayer("First Solve Button Gap");
	EmptyLayer solveButtonGap2 = new EmptyLayer("Second Solve Button Gap");

	EmptyLayer textGap1 = new EmptyLayer("North Text Gap");
	EmptyLayer textGap2 = new EmptyLayer("South Text Gap");

	void debuggingSizeCheck()
	{
		EmptyLayer[] coolArray = new EmptyLayer[]{northBorder,southBorder,westBorder,eastBorder,
		containsEverything,sudokuSideBackdrop,highMiddleGap,textSideBackdrop,panelOfButtonPanels,
		emptyButtonBackdrop,westButtonBackdrop,centerButtonBackdrop,eastButtonBackdrop,normalButtonGap1,
		normalButtonGap2,normalButtonGap3,normalButtonGap4,solveButtonGap1,solveButtonGap2,textGap1,textGap2};

		for(int i=0;i<coolArray.length;i++)
		{
			System.out.println(coolArray[i].name+": ("+coolArray[i].getWidth()+", "+coolArray[i].getHeight()+")");
		}
	}


	// ~~~~~~~~~~
	// ~~~~~~~~~~
	// ~~~~~~~~~~


	class NumAction extends AbstractAction
	{
		Integer numToHandle;

		NumAction(Integer w)
		{
			super();
			numToHandle = w;
		}

		public void actionPerformed(ActionEvent e)
		{
			fillInMap[currentRow][currentCol].setNum(numToHandle,null);

			if(numToHandle != null)
				fillInMap[currentRow][currentCol].didSquareStartEmpty = false;
			else
				fillInMap[currentRow][currentCol].didSquareStartEmpty = true;

			fillInMap[currentRow][currentCol].repaint();
		}
	}

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


	// ~~~~~~~~~~
	// ~~~~~~~~~~
	// ~~~~~~~~~~


	Action rightAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			if(currentCol != numColInGrid-1)
			{ currentCol++; }
			else
			{ currentCol = 0; }

			recToWorkWith.repaint();
		}
	};

	Action leftAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			if(currentCol != 0)
			{ currentCol--; }
			else
			{ currentCol = numColInGrid-1; }

			recToWorkWith.repaint();
		}
	};

	Action downAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			if(currentRow != numRowInGrid-1)
			{ currentRow++; }
			else
			{ currentRow = 0; }

			recToWorkWith.repaint();
		}
	};

	Action upAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			if(currentRow != 0)
			{ currentRow--; }
			else
			{ currentRow = numRowInGrid-1; }

			recToWorkWith.repaint();
		}
	};


	// ~~~~~~~~~~
	// ~~~~~~~~~~
	// ~~~~~~~~~~


	JButton itsTheResetButton = new JButton();
	JButton itsTheSolveButton = new JButton();
	JButton itsTheUndoSolveButton = new JButton();
	JButton itsTheSaveSudokuButton = new JButton();
	JButton itsTheLoadSudokuButton = new JButton();
	JButton itsTheBeginStepsButton = new JButton();


	// ~~~~~~~~~~
	// ~~~~~~~~~~
	// ~~~~~~~~~~


	class AllStepsHolder extends JPanel
	{
		private int currentStepNum = 1;
		private double myWeight = 1e-300;

		void resetStepNumber()
		{
			currentStepNum = 1;
			myWeight = 1e-300;
		}

		void addOneStep(String message)
		{
			JTextArea newStep = new JTextArea(message);

			newStep.setLineWrap(true);
			newStep.setWrapStyleWord(true);
			newStep.setEditable(false);
			newStep.setFocusable(false);

			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.PAGE_START;
			c.gridy = currentStepNum;
			c.weightx = 1d;
			c.weighty = myWeight;

			add(newStep,c);

			pack();

			currentStepNum++;
			myWeight *= 1000.0;
		}

	} // AllStepsHolder class

	AllStepsHolder holderOfAllSteps = new AllStepsHolder();


//	JTextArea itsTheTextArea = new JTextArea();

	private int vertScrollPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
	private int horiScrollPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;

	JScrollPane itsTheScrollPane = new JScrollPane(holderOfAllSteps,vertScrollPolicy,horiScrollPolicy);
	JScrollBar verticalBar = itsTheScrollPane.getVerticalScrollBar();


} // PaintedObjects