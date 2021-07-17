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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class PaintedObjects extends JFrame
{
	// Number of rows & columns

	protected static int ROWS = 9;
	protected static int COLS = 9;

	// Color and size of the square highlight

	private static Color ourRectColor = new Color(28,222,144);
	protected static int ourRectWidth = 60;
	protected static int ourRectHeight = 60;

	// Current row & column of the square highlight

	protected static int currentRow = 0;
	protected static int currentCol = 0;

	// Exact location of the square highlight

	protected static Point ourRecLocation = new Point(100,100);

	// Whether the arrow keys will currently move the square around
	// and the number keys will enter numbers into the Sudoku

	protected static boolean controlsOn = true;



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


	// CoreGrid object can paint itself

	public class CoreGrid
	{
		protected void paint(Graphics2D g2d)
		{
			BasicStroke thickerBorder = new BasicStroke(1.5F);
			BasicStroke normalBorder = new BasicStroke(1F);

			int counter = 0;

			// Set color to black and the line to normal thickness
			// When drawing, if the line you draw is line 0, 3, 6,or 9,
			// then use the thicker border for that line only

			g2d.setColor(new Color(0,0,0));
			g2d.setStroke(normalBorder);

			// Draw Horizontal Lines

			for(int i=100;i<=640;i+=60)
			{
				if(counter == 0 || counter == 3 || counter == 6 || counter == 9)
					g2d.setStroke(thickerBorder);

				g2d.drawLine(100,i,640,i);

				if(counter == 0 || counter == 3 || counter == 6 || counter == 9)
					g2d.setStroke(normalBorder);

				counter++;
			}

			counter = 0;

			// Draw Vertical Lines

			for(int i=100;i<=640;i+=60)
			{
				if(counter == 0 || counter == 3 || counter == 6 || counter == 9)
					g2d.setStroke(thickerBorder);

				g2d.drawLine(i,100,i,640);

				if(counter == 0 || counter == 3 || counter == 6 || counter == 9)
					g2d.setStroke(normalBorder);

				counter++;
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

	public class SolveText
	{
		protected Integer numSquaresSolved = null;
		private String fullSquaresSolvedQuote = "";

		protected int completionCode = -5;
		private String completionMessage = "";

		protected int BottomPracY = (640+(640+60))/2 - smallerFM.getHeight()/2 + smallerFM.getAscent();
		protected int BottomPracX;

		protected int TopPracY = (100+(100-60))/2 - smallerFM.getHeight()/2 + smallerFM.getAscent();
		protected int TopPracX;

		protected void resetThisAll()
		{
			numSquaresSolved = null;
			fullSquaresSolvedQuote = "";
			completionCode = -5;
			completionMessage = "";
		}

		protected void paint(Graphics2D g2d)
		{
			g2d.setColor(thePurple);
			g2d.setFont(smallerFont);

			if(numSquaresSolved != null)
			{
				fullSquaresSolvedQuote = "Squares Solved: "+String.valueOf(numSquaresSolved);

				if(completionCode == 0)
					g2d.setColor(theBlue);
			}

			BottomPracX = (100+(100+540))/2 - smallerFM.stringWidth(fullSquaresSolvedQuote)/2;
			g2d.drawString(fullSquaresSolvedQuote,BottomPracX,BottomPracY);

			if(completionCode == 0)
				completionMessage = "Puzzle is complete!";
			else if(completionCode == 1)
				completionMessage = "Puzzle is NOT complete. There are numbers besides 1-9.";
			else if(completionCode == 2)
				completionMessage = "Puzzle is NOT complete. There are dupes in a row/column/box.";
			else if(completionCode == 3)
				completionMessage = "Puzzle is NOT complete. There are empty spaces.";
				
			TopPracX = (100+(100+540))/2 - smallerFM.stringWidth(completionMessage)/2;
			g2d.drawString(completionMessage,TopPracX,TopPracY);
		}

	} // SolveText class


	// OurSolveText can create a SolveText and call paint() on it

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

		public void setCompletionCode(int theInput)
		{
			capableSolveText.completionCode = theInput;
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


	// NumHolder object can paint itself

	public class NumHolder
	{
		protected String numHeld;
		protected String[] endPossArray;

		protected int ownRow;
		protected int ownCol;

		protected Point whereToDrawRec;

		protected int pracY = (100+(100+60))/2 - fm.getHeight()/2 + fm.getAscent();
		protected int pracX;

		protected int possArrayPracY;
		protected int possArrayPracX;

		protected boolean beingAddedOnSolve = false;

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

			// Fix where to draw rectangle

			whereToDrawRec = new Point(100+60*ownCol,100+60*ownRow);
		}

		protected void resetItAll()
		{
			numHeld = "";
			for(int s=0;s<9;s++)
			{
				endPossArray[s] = "";
			}
			beingAddedOnSolve = false;
		}

		protected void paint(Graphics2D g2d)
		{
			// Set the color and font, then use own variables to
			// paint the correct number at the correct location

			if(beingAddedOnSolve && numHeld.equals(""))
			{
				g2d.setColor(thePurple);

				g2d.setFont(possArrayFont);

				for(int w=0;w<endPossArray.length;w++)
				{
					possArrayPracY = (100+(100+20))/2 - possArrayFontFM.getHeight()/2 + possArrayFontFM.getAscent() + howFarY[w];
					possArrayPracX = (100+(100+20))/2 - possArrayFontFM.stringWidth(endPossArray[w])/2 + howFarX[w];

					g2d.drawString(endPossArray[w],possArrayPracX+ownCol*60,possArrayPracY+ownRow*60);

				}
			}

			else if(beingAddedOnSolve)
			{
				g2d.setColor(theBlue);

				g2d.setFont(myFont);

				pracX = (100+(100+60))/2 - fm.stringWidth(numHeld)/2;
				g2d.drawString(numHeld,pracX+ownCol*60,pracY+ownRow*60);
			}

			else
			{
				g2d.setColor(theBlack);

				g2d.setFont(myFont);

				pracX = (100+(100+60))/2 - fm.stringWidth(numHeld)/2;
				g2d.drawString(numHeld,pracX+ownCol*60,pracY+ownRow*60);
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
				capableNumHolder.numHeld = Integer.toString(v);
			else
				capableNumHolder.numHeld = "";
		}

		protected String getNum()
		{
			return capableNumHolder.numHeld;
		}

		protected boolean getSolveAddStatus()
		{
			return capableNumHolder.beingAddedOnSolve;
		}

		protected void setFinalPossArray(Integer[] Input)
		{
			if(Input != null)
			{
				for(int i=0;i<Input.length;i++)
				{
					if(Input[i] != null)
					{
						capableNumHolder.endPossArray[i] = Integer.toString(Input[i]);
					}
				}
			}
		}

		protected void adjustSolve(Boolean b)
		{
			capableNumHolder.beingAddedOnSolve = b;
		}

		protected Point getDrawRec()
		{
			return capableNumHolder.whereToDrawRec;
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


	// ClickButton. It's a button.

	public class ClickButton extends JButton
	{
		private JButton capableButton;

		public ClickButton()
		{
			capableButton = new JButton();
		}

	} // ClickButton class

} // PaintedObjects


class Square
{

	protected Integer result; // The final number that you see
	protected Integer[] possArray; // Every possible number it could still be

	protected boolean answerAtStart = false;

	protected int ownRow;
	protected int ownCol;
	protected int ownBox;

	protected int VARIETY; // Total amount of possibilities at start
	protected int numPossLeft; // Number of possibilities left

	private static int[][] findBox = new int[][]{ {0,1,2},
												  {3,4,5},
												  {6,7,8} };


	// Constructor

	protected Square(int selfRow, int selfCol)
	{
		// Initialize result to null

		result = null;

		// Fix own row and column

		ownRow = selfRow;
		ownCol = selfCol;
		ownBox = findBox[selfRow/3][selfCol/3];

		// Set full range of possibilities

		VARIETY = 9;
		numPossLeft = VARIETY;
		possArray = new Integer[VARIETY];

		// Every possibility is its index plus 1

		for(int i=0;i<VARIETY;i++)
		{ possArray[i] = i+1; }

		// If result is finalized, result is not null and possArray is null
		// If result is not finalized, result is null and possArray is not null
	}

} // Square