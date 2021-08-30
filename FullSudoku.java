import java.util.LinkedList;

public class FullSudoku
{
	// Squares themselves

	public Square[][] SudokuMap = new Square[9][9];

	public String[][] forNumsUseFill;

	protected int squaresSolved = 0;

	protected int initialInputSquares = 0;

	public final int numRowCol = 9;

	protected BoxTranslator theUnboxer = new BoxTranslator();

	private boolean impossiblePuzzle = false;

	public boolean isPuzzleImpossible()
	{
		return impossiblePuzzle;
	}


	// What's neat is that this should never result in a
	// NullPointerException. From the moment the constructor
	// uses NakedSingle() to process the user-input squares
	// from the linked list, mostRecentSingle is aimed
	// at an existing square in SudokuMap

	private Square mostRecentNakedSingle;

	public Square getMostRecentNakedSingle()
	{
		return mostRecentNakedSingle;
	}


	// Linked List for what squares to finalize.
	// Squares in the Linked List already have their
	// results determined and their possArray null,
	// but their answers haven't been removed from
	// squares sharing a row/column/box with them

	private LinkedList<Square> readyToFinalize = new LinkedList<Square>();


	// Line 0 is for RCB 0, Line 1 is for RCB 1, etc.
	// Within a line, index 0 details how many squares
	// still have 1 in the possArray, index 1 details how
	// many squares still have 2 in the possArray, etc.

	public int[][] rowPossPrevalence = new int[9][9];
	public int[][] colPossPrevalence = new int[9][9];
	public int[][] boxPossPrevalence = new int[9][9];


	// Provide a row of squares

	public Square[] provideRow(int rowNum)
	{
		Square[] rowProvided = new Square[9];

		for(int i=0;i<9;i++)
		{
			rowProvided[i] = SudokuMap[rowNum][i];
		}

		return rowProvided;
	}

	// Provide a column of squares

	public Square[] provideCol(int colNum)
	{
		Square[] colProvided = new Square[9];

		for(int i=0;i<9;i++)
		{
			colProvided[i] = SudokuMap[i][colNum];
		}

		return colProvided;
	}

	// Provide a box of squares

	public Square[] provideBox(int boxNum)
	{
		Square[] boxProvided = new Square[9];

		int goodRow;
		int goodCol;

		for(int i=0;i<9;i++)
		{
			goodRow = theUnboxer.rowOfBoxSquare(boxNum,i);
			goodCol = theUnboxer.colOfBoxSquare(boxNum,i);

			boxProvided[i] = SudokuMap[goodRow][goodCol];
		}

		return boxProvided;
	}

	// Provide every square sharing a row, column, or box with one particular square
	// There are exactly 20 such squares, every time

	public Square[] provideAllSharingRCB(Square rootSquare)
	{
		Square[] finalList = new Square[20];
		int listTraverse = 0;

		int goodRow;
		int goodCol;
		Square squareToAdd;

		// Insert squares from row

		for(int a=0;a<9;a++)
		{
			if(a != rootSquare.ownCol)
			{
				finalList[listTraverse] = SudokuMap[rootSquare.ownRow][a];
				listTraverse++;
			}
		}

		// Insert squares from column

		for(int a=0;a<9;a++)
		{
			if(a != rootSquare.ownRow)
			{
				finalList[listTraverse] = SudokuMap[a][rootSquare.ownCol];
				listTraverse++;
			}
		}

		// Insert squares from box

		for(int a=0;a<9;a++)
		{
			goodRow = theUnboxer.rowOfBoxSquare(rootSquare.ownBox,a);
			goodCol = theUnboxer.colOfBoxSquare(rootSquare.ownBox,a);

			// The only squares you still need from the box are the ones
			// which share neither a row nor a column with rootSquare

			if(goodRow != rootSquare.ownRow && goodCol != rootSquare.ownCol)
			{
				finalList[listTraverse] = SudokuMap[goodRow][goodCol];
				listTraverse++;
			}
		}

		return finalList;
	}


	// Should be used in every solve. Calling this function will wash
	// a number from a square's possArray if is there, then put the
	// square in the linked list if it is now down to 1 possibility.

	public boolean elimFromPossArray(Square a,Integer b)
	{
		if(impossiblePuzzle)
			return false;

		Square testSquare = a;
		Integer possOut = b;

		// If the square is already solved

		if(testSquare.result != null)
		{
			// If what we want to eliminate is the square's result

			if(testSquare.result.equals(possOut))
			{
				// Puzzle is impossible

				impossiblePuzzle = true;


				// Return true, to ensure nothing else is done

				return true;
			}

			// Otherwise, just return false.
			// No work done on this square.

			return false;
		}

		// If the possibility is already eliminated from the possArray, return false

		if(testSquare.possArray[possOut-1] == null)
		{
			return false;
		}

		// If the square is not yet solved, the possibility we are eliminating is
		// still in the possArray, and numPossLeft is already down to 1

		if(testSquare.numPossLeft == 1)
		{
			// Puzzle is impossible

			impossiblePuzzle = true;


			// Return true, to ensure nothing else is done

			return true;
		}

		testSquare.possArray[possOut-1] = null;
		testSquare.numPossLeft--;

		rowPossPrevalence[testSquare.ownRow][possOut-1]--;
		colPossPrevalence[testSquare.ownCol][possOut-1]--;
		boxPossPrevalence[testSquare.ownBox][possOut-1]--;

		// If this takes the numPossLeft down to one,
		// put the square in the Linked List

		if( testSquare.numPossLeft == 1 )
			readyToFinalize.add(testSquare);


		// Since progress was made, return true

		return true;

	} // elimFromPossArray()


	// Procedure for removing a single item from the finalization Linked List
	// by retrieving its row & column, then ruling result out from the squares
	// sharing a row or column or box.

	private void knockOutInRCB(Square spot)
	{
		Integer itsResult = spot.result;
		int itsRow = spot.ownRow;
		int itsCol = spot.ownCol;
		int itsBox = spot.ownBox;

		Square elimTemplate;
		Square[] relevantRCB;


		// Now we have to take that number out of
		// the possibility arrays for every square in
		// the same row, column, and box

		relevantRCB = provideRow(itsRow);

		for(int k=0;k<9;k++)
		{
			elimTemplate = relevantRCB[k];

			if(elimTemplate.ownCol != spot.ownCol)
				elimFromPossArray(elimTemplate,itsResult);
		}


		relevantRCB = provideCol(itsCol);

		for(int k=0;k<9;k++)
		{
			elimTemplate = relevantRCB[k];

			if(elimTemplate.ownRow != spot.ownRow)
				elimFromPossArray(elimTemplate,itsResult);
		}


		relevantRCB = provideBox(itsBox);

		for(int k=0;k<9;k++)
		{
			elimTemplate = relevantRCB[k];

			if(elimTemplate.ownCol != spot.ownCol || elimTemplate.ownRow != spot.ownRow)
				elimFromPossArray(elimTemplate,itsResult);
		}

	} // knockOutInRCB()


	// Naked Single Implementation

	protected boolean NakedSingle()
	{
		if(impossiblePuzzle)
			return false;


		Square toRemove;

		// If the linked list is empty, return false

		if(readyToFinalize.isEmpty())
			return false;


		// Otherwise, take a square out of the linked list,
		// set the result to its remaining possibility,
		// and knock that possibility out of all squares
		// sharing a row/column/box with this square.

		toRemove = readyToFinalize.poll();

		for(int u=0;u<9;u++)
		{
			if(toRemove.possArray[u] != null)
				toRemove.result = toRemove.possArray[u];

		}
		toRemove.possArray = null;
		squaresSolved++;

		knockOutInRCB(toRemove);

		mostRecentNakedSingle = toRemove;

		return true;

	} // NakedSingle()


	// Constructor!

	public FullSudoku(String[][] thisInput)
	{
		forNumsUseFill = thisInput;

		// Initialize Map

		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				SudokuMap[i][j] = new Square(i,j);
			}
		}

		// Fill up the RCB records of how many
		// time each number is in them

		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				rowPossPrevalence[i][j] = 9;
				colPossPrevalence[i][j] = 9;
				boxPossPrevalence[i][j] = 9;
			}
		}

		// First Fill

		Square squareWeUse;
		Integer resultOfSquare;

		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				if( !(forNumsUseFill[i][j].equals("")) )
				{
					squareWeUse = SudokuMap[i][j];
					resultOfSquare = Integer.parseInt(forNumsUseFill[i][j]);

					initialInputSquares++;

					squareWeUse.answerAtStart = true;

					for(Integer k=1;k<=9;k++)
					{
						if(!(k.equals(resultOfSquare)))
							elimFromPossArray(squareWeUse,k);
					}
				}
			}
		}

		for(int i=0;i<initialInputSquares;i++)
		{
			// This isn't considered a use of Naked Single from the perspective
			// of the user. It's just a way of clearing the inserted numbers from
			// the possArray of all squares sharing an RCB with them

			NakedSingle();
		}

	} // Constructor

} // FullSudoku