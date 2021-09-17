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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class PaintedObjects extends JFrame
{
	protected final static int gridSquareWidth = 60;
	protected final static int numColInGrid = 9;
	protected final static int gridTotalWidth = gridSquareWidth*numColInGrid;
	protected final static int numColBetweenThick = 3;

	protected final static int gridSquareHeight = 60;
	protected final static int numRowInGrid = 9;
	protected final static int gridTotalHeight = gridSquareHeight*numRowInGrid;
	protected final static int numRowBetweenThick = 3;


	// CoreGrid object can paint itself

	private class CoreGrid
	{
		private void paint(Graphics2D g2d)
		{
			BasicStroke thickerBorder = new BasicStroke(1.5F);
			BasicStroke normalBorder = new BasicStroke(1F);

			int rightX = gridSquareWidth*numColInGrid;
			int bottomY = gridSquareHeight*numRowInGrid;

			int handyCoord;

			// Set color to black and the line to normal thickness
			// When drawing, if the line you draw is line 0, 3, 6,or 9,
			// then use the thicker border for that line only

			g2d.setColor(new Color(0,0,0));
			g2d.setStroke(normalBorder);


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

		}

	} // CoreGrid class


	// OurCoreGrid can create a CoreGrid and call paint() on it

	protected class OurCoreGrid extends JPanel
	{
		private CoreGrid capableCoreGrid;

		protected OurCoreGrid()
		{
			capableCoreGrid = new CoreGrid();
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g.create();

			capableCoreGrid.paint(g2d);

			g2d.dispose();
		}

	} // OurCoreGrid class



	// Color and size of the highlight

	private final static Color ourRectColor = new Color(28,222,144);

	protected static int ourRectWidth = gridSquareWidth;
	protected static int ourRectHeight = gridSquareHeight;


	// Exact location of the highlight

	protected static Point ourRecLocation = new Point(0,0);


	// Rectangle object can paint itself

	private class Rectangle
	{
		private void paint(Graphics2D g2d)
		{
			// The variables needed to draw the thing are provided above

			g2d.setColor(ourRectColor);
			g2d.fillRect(ourRecLocation.x, ourRecLocation.y, ourRectWidth, ourRectHeight);
		}

	} // Rectangle class


	// OurRectangle can create a Rectangle and call paint() on it

	protected class OurRectangle extends JPanel
	{
		private Rectangle capableRectangle;

		protected OurRectangle()
		{
			capableRectangle = new Rectangle();
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g.create();

			capableRectangle.paint(g2d);

			g2d.dispose();
		}

	} // OurRectangle class



	// Create the fonts and the metrics

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


	// SolveText object can paint itself
	// Currently NOT USED

	private class SolveText
	{
		private Integer numSquaresSolved = null;

		private String incompleteString = "";
		private String completeString = "Puzzle is complete!";

		private String shownMessage = incompleteString;
		private int shownMessY = (100+(100-60))/2 - smallerFM.getHeight()/2 + smallerFM.getAscent();
		private int shownMessX;

		private void resetThisAll()
		{
			numSquaresSolved = null;
			shownMessage = incompleteString;
		}

		private void paint(Graphics2D g2d)
		{
			g2d.setColor(theBlue);
			g2d.setFont(smallerFont);

			if(numSquaresSolved != null && numSquaresSolved.equals(81))
					shownMessage = completeString;

			shownMessX = (100+640)/2 - smallerFM.stringWidth(shownMessage)/2;
			g2d.drawString(shownMessage,shownMessX,shownMessY);
		}

	} // SolveText class


	// OurSolveText can create a SolveText and call paint() on it
	// Currently NOT USED

	protected class OurSolveText extends JPanel
	{
		private SolveText capableSolveText;

		protected OurSolveText()
		{
			capableSolveText = new SolveText();
		}

		protected void setNumSquaresSolved(int theInput)
		{
			capableSolveText.numSquaresSolved = theInput;
		}

		protected void resetSolveText()
		{
			capableSolveText.resetThisAll();
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g.create();

			capableSolveText.paint(g2d);

			g2d.dispose();
		}

	} // OurSolveText class


	// RowColLabel object can paint itself

	private class RowColLabel
	{
		private String labelString;

		private HouseType rowOrCol;
		private int whichRowCol;

		private int locY = gridSquareHeight/2 - possArrayFontFM.getHeight()/2 + possArrayFontFM.getAscent();
		private int locX;

		private RowColLabel(HouseType a, int b)
		{
			rowOrCol = a;
			whichRowCol = b;

			labelString = Integer.toString(whichRowCol+1);
			locX = gridSquareWidth/2 - possArrayFontFM.stringWidth(labelString)/2;
		}

		private void paint(Graphics2D g2d)
		{
			g2d.setColor(theBlack);
			g2d.setFont(possArrayFont);

			g2d.drawString(labelString,locX,locY);
		}

	} // RowColLabel Class


	// OurRowColLabel can create a RowColLabel and call paint() on it

	protected class OurRowColLabel extends JPanel
	{
		private RowColLabel capableRowColLabel;

		protected OurRowColLabel(HouseType a, int b)
		{
			capableRowColLabel = new RowColLabel(a,b);
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g.create();

			capableRowColLabel.paint(g2d);

			g2d.dispose();
		}

	} // OurRowColLabel class


	// NumHolder object can paint itself

	private class NumHolder
	{
		private String numHeld;
		private String[] endPossArray;

		private int ownRow;
		private int ownCol;

		private int pracY = gridSquareHeight/2 - fm.getHeight()/2 + fm.getAscent();
		private int pracX;

		private int possArrayPracY;
		private int possArrayPracX;

		private boolean addedOnHumanSolve = false;
		private boolean addedOnLastResort = false;

		private boolean didSquareStartEmpty = true;

		private int[] howFarY = new int[]{0,0,0,20,20,20,40,40,40};
		private int[] howFarX = new int[]{0,20,40,0,20,40,0,20,40};


		// Constructor

		private NumHolder(int selfRow, int selfCol)
		{
			// Initialize numHeld to a blank

			numHeld = "";

			// Initialize endPossArray to 9 empty strings

			endPossArray = new String[]{"","","","","","","","",""};

			// Fix own row and column

			ownRow = selfRow;
			ownCol = selfCol;
		}

		private void resetItAll()
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

		private void paint(Graphics2D g2d)
		{
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


		}

	} // NumHolder class


	// OurNumHolder can create a NumHolder and call paint() on it

	protected class OurNumHolder extends JPanel
	{
		private NumHolder capableNumHolder;

		protected OurNumHolder(int i, int j)
		{
			capableNumHolder = new NumHolder(i, j);
		}

		protected void setNum(Integer v)
		{
			if( v != null )
			{
				capableNumHolder.numHeld = Integer.toString(v);
			}
			else
			{
				capableNumHolder.numHeld = "";
			}
		}

		protected void changeStartEmpty(boolean b)
		{
			capableNumHolder.didSquareStartEmpty = b;
		}

		protected boolean wasSquareEmpty()
		{
			return capableNumHolder.didSquareStartEmpty;
		}

		protected String getNum()
		{
			return capableNumHolder.numHeld;
		}

		protected void setPossArray(Integer[] Input)
		{
			if(Input != null)
			{
				for(int i=0;i<Input.length;i++)
				{
					if(Input[i] == null)
						capableNumHolder.endPossArray[i] = "";
					else
						capableNumHolder.endPossArray[i] = Integer.toString(Input[i]);
				}
			}
		}

		protected void adjustHumanSolve(Boolean b)
		{
			capableNumHolder.addedOnHumanSolve = b;
		}

		protected void adjustLastResort(Boolean b)
		{
			capableNumHolder.addedOnLastResort = b;
		}

		protected void resetNumHolder()
		{
			capableNumHolder.resetItAll();
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g.create();

			capableNumHolder.paint(g2d);

			g2d.dispose();
		}

	} // OurNumHolder class

} // PaintedObjects