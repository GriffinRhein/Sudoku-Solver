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
	public final static int gridSquareWidth = 60;
	public final static int numColInGrid = 9;
	public final static int gridTotalWidth = gridSquareWidth*numColInGrid;
	public final static int numColBetweenThick = 3;

	public final static int gridSquareHeight = 60;
	public final static int numRowInGrid = 9;
	public final static int gridTotalHeight = gridSquareHeight*numRowInGrid;
	public final static int numRowBetweenThick = 3;


	// CoreGrid object can paint itself

	public class CoreGrid
	{
		protected void paint(Graphics2D g2d)
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

	public class OurCoreGrid extends JPanel
	{
		private CoreGrid capableCoreGrid;

		public OurCoreGrid()
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

	public static int ourRectWidth = gridSquareWidth;
	public static int ourRectHeight = gridSquareHeight;


	// Exact location of the highlight

	public static Point ourRecLocation = new Point(0,0);


	// Rectangle object can paint itself

	public class Rectangle
	{
		protected void paint(Graphics2D g2d)
		{
			// The variables needed to draw the thing are provided above

			g2d.setColor(ourRectColor);
			g2d.fillRect(ourRecLocation.x, ourRecLocation.y, ourRectWidth, ourRectHeight);
		}

	} // Rectangle class


	// OurRectangle can create a Rectangle and call paint() on it

	public class OurRectangle extends JPanel
	{
		private Rectangle capableRectangle;

		public OurRectangle()
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

	protected Color theBlack = new Color(0,0,0);
	protected Color theRed = new Color(255,0,0);
	protected Color theBlue = new Color(0,0,255);
	protected Color thePurple = new Color(125,0,225);

	protected Font myFont = new Font("TimesRoman",Font.PLAIN,42);
	protected Canvas c1 = new Canvas();
	protected FontMetrics fm = c1.getFontMetrics(myFont);

	protected Font smallerFont = new Font("TimesRoman",Font.PLAIN,28);
	protected Canvas c2 = new Canvas();
	protected FontMetrics smallerFM = c2.getFontMetrics(smallerFont);

	protected Font possArrayFont = new Font("TimesRoman",Font.PLAIN,18);
	protected Canvas c3 = new Canvas();
	protected FontMetrics possArrayFontFM = c3.getFontMetrics(possArrayFont);


	// SolveText object can paint itself
	// Currently NOT USED

	public class SolveText
	{
		protected Integer numSquaresSolved = null;

		private String incompleteString = "";
		private String completeString = "Puzzle is complete!";

		private String shownMessage = incompleteString;
		private int shownMessY = (100+(100-60))/2 - smallerFM.getHeight()/2 + smallerFM.getAscent();
		private int shownMessX;

		protected void resetThisAll()
		{
			numSquaresSolved = null;
			shownMessage = incompleteString;
		}

		protected void paint(Graphics2D g2d)
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

	public class OurSolveText extends JPanel
	{
		private SolveText capableSolveText;

		public OurSolveText()
		{
			capableSolveText = new SolveText();
		}

		public void setNumSquaresSolved(int theInput)
		{
			capableSolveText.numSquaresSolved = theInput;
		}

		public void resetSolveText()
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

	public class RowColLabel
	{
		private String labelString;

		private HouseType rowOrCol;
		private int whichRowCol;

		private int locY = gridSquareHeight/2 - possArrayFontFM.getHeight()/2 + possArrayFontFM.getAscent();
		private int locX;

		public RowColLabel(HouseType a, int b)
		{
			rowOrCol = a;
			whichRowCol = b;

			labelString = Integer.toString(whichRowCol+1);
			locX = gridSquareWidth/2 - possArrayFontFM.stringWidth(labelString)/2;
		}

		protected void paint(Graphics2D g2d)
		{
			g2d.setColor(theBlack);
			g2d.setFont(possArrayFont);

			g2d.drawString(labelString,locX,locY);
		}

	} // RowColLabel Class


	// OurRowColLabel can create a RowColLabel and call paint() on it

	public class OurRowColLabel extends JPanel
	{
		private RowColLabel capableRowColLabel;

		public OurRowColLabel(HouseType a, int b)
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

	public class NumHolder
	{
		protected String numHeld;
		protected String[] endPossArray;

		protected int ownRow;
		protected int ownCol;

		protected int pracY = gridSquareHeight/2 - fm.getHeight()/2 + fm.getAscent();
		protected int pracX;

		protected int possArrayPracY;
		protected int possArrayPracX;

		protected boolean addedOnHumanSolve = false;
		protected boolean addedOnLastResort = false;

		protected boolean didSquareStartEmpty = true;

		protected int[] howFarY = new int[]{0,0,0,20,20,20,40,40,40};
		protected int[] howFarX = new int[]{0,20,40,0,20,40,0,20,40};


		// Constructor

		protected NumHolder(int selfRow, int selfCol)
		{
			// Initialize numHeld to a blank

			numHeld = "";

			// Initialize endPossArray to 9 empty strings

			endPossArray = new String[]{"","","","","","","","",""};

			// Fix own row and column

			ownRow = selfRow;
			ownCol = selfCol;
		}

		protected void resetItAll()
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

		protected void paint(Graphics2D g2d)
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

	public class OurNumHolder extends JPanel
	{
		private NumHolder capableNumHolder;

		public OurNumHolder(int i, int j)
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