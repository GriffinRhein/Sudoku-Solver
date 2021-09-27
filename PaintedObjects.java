import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PaintedObjects extends JFrame
{
	// Sudoku Grid Info

	final int numColInGrid = 9;
	final int numColBetweenThick = 3;

	final int numRowInGrid = 9;
	final int numRowBetweenThick = 3;


	// Current row & column we are highlighting

	int currentCol = 0;
	int currentRow = 0;


	// Influenced by size of JFrame

	int sudokuBackdropWidth;
	int sudokuBackdropHeight;

	int gridSquareWidth;
	int gridTotalWidth;

	int gridSquareHeight;
	int gridTotalHeight;

	Dimension standardButtonSize;
	Dimension solveButtonSize;
	Dimension emptySizeBlock;


	// ~~~~~~~~~~
	// ~~~~~~~~~~
	// ~~~~~~~~~~


	class SudokuBackdrop extends JPanel
	{
		void applySizeValues()
		{
			sudokuBackdropWidth = this.getWidth();
			sudokuBackdropHeight = this.getHeight();

			gridSquareWidth = sudokuBackdropWidth/(numColInGrid+1);
			gridSquareHeight = sudokuBackdropHeight/(numRowInGrid+1);

			gridTotalWidth = sudokuBackdropWidth-gridSquareWidth;
			gridTotalHeight = sudokuBackdropHeight-gridSquareHeight;

			standardButtonSize = new Dimension(gridSquareWidth*2,gridSquareHeight/2);
			solveButtonSize = new Dimension(gridSquareWidth*3,(gridSquareHeight/2)+20);
			emptySizeBlock = new Dimension(gridSquareWidth*3,(gridSquareHeight/2)-10);
		}

	} // SudokuBackdrop class

	SudokuBackdrop sudokuGridBackdrop = new SudokuBackdrop();


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

	ColLabels colLabelsToWorkWith = new ColLabels();


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

	RowLabels rowLabelsToWorkWith = new RowLabels();


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
					g2d.drawLine(handyCoord,0,handyCoord,gridTotalHeight);
					g2d.setStroke(normalBorder);
				}
				else
				{
					g2d.drawLine(handyCoord,0,handyCoord,gridTotalHeight);
				}
			}

			// Draw Horizontal Lines

			for(int j=0;j<=numRowInGrid;j++)
			{
				handyCoord = j*gridSquareHeight;

				if(j % numRowBetweenThick == 0)
				{
					g2d.setStroke(thickerBorder);
					g2d.drawLine(0,handyCoord,gridTotalWidth,handyCoord);
					g2d.setStroke(normalBorder);
				}
				else
				{
					g2d.drawLine(0,handyCoord,gridTotalWidth,handyCoord);
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


	private Color theBlack = new Color(0,0,0);
	private Color theRed = new Color(255,0,0);
	private Color theBlue = new Color(0,0,255);
	private Color thePurple = new Color(125,0,225);

	private Font myFont = new Font("TimesRoman",Font.PLAIN,42);
	private Canvas c1 = new Canvas();
	private FontMetrics fm = c1.getFontMetrics(myFont);

	private Font smallerFont = new Font("TimesRoman",Font.PLAIN,28);
	private Canvas c2 = new Canvas();
	private FontMetrics smallerFM = c2.getFontMetrics(smallerFont);

	private Font possArrayFont = new Font("TimesRoman",Font.PLAIN,18);
	private Canvas c3 = new Canvas();
	private FontMetrics possArrayFontFM = c3.getFontMetrics(possArrayFont);


	// ~~~~~~~~~~
	// ~~~~~~~~~~
	// ~~~~~~~~~~


	class NumHolder extends JPanel
	{
		private String numHeld;
		private String[] endPossArray;

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


		// Constructor

		NumHolder(int selfRow, int selfCol)
		{
			// Initialize numHeld to a blank

			numHeld = "";

			// Initialize endPossArray to 9 empty strings

			endPossArray = new String[]{"","","","","","","","",""};

			// Initialize own row and column

			ownRow = selfRow;
			ownCol = selfCol;

			// Initialize info to display remaining possibilities

			howFarY = new int[]{0,0,0,20,20,20,40,40,40};
			howFarX = new int[]{0,20,40,0,20,40,0,20,40};
		}

		void resetItAll()
		{
			numHeld = "";
			for(int s=0;s<9;s++)
			{
				endPossArray[s] = "";
			}
			addedOnHumanSolve = false;
			addedOnLastResort = false;
			didSquareStartEmpty = true;
		}

		void setNum(Integer v)
		{
			if( v != null )
			{
				numHeld = Integer.toString(v);
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

		void setPossArray(Integer[] Input)
		{
			if(Input != null)
			{
				for(int i=0;i<Input.length;i++)
				{
					if(Input[i] == null)
						endPossArray[i] = "";
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
			fillInMap[currentRow][currentCol].setNum(numToHandle);

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


	Box westButtonBackdrop = new Box(BoxLayout.Y_AXIS);
	Box centerButtonBackdrop = new Box(BoxLayout.Y_AXIS);
	Box eastButtonBackdrop = new Box(BoxLayout.Y_AXIS);


} // PaintedObjects