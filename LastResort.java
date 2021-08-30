import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class LastResort
{
	private PrimitiveSquare[][] computerSolveMap;

	private boolean wasSolveSuccessful;

	public LastResort(FullSudoku theSudoku)
	{
		computerSolveMap = new PrimitiveSquare[9][9];
		wasSolveSuccessful = false;

		Integer a;
		boolean b;
		int c;
		int d;
		int e;

		for(int y=0;y<9;y++)
		{
			for(int x=0;x<9;x++)
			{
				a = theSudoku.SudokuMap[y][x].result;
				b = false;   if(a != null){b = true;}
				c = theSudoku.SudokuMap[y][x].ownRow;
				d = theSudoku.SudokuMap[y][x].ownCol;
				e = theSudoku.SudokuMap[y][x].ownBox;

				computerSolveMap[y][x] = new PrimitiveSquare(a,b,c,d,e);
			}
		}

		// As a temporary measure to ensure that these primitive squares
		// are not linked to the originals, they will be serialized.

		serializeItAll();
	}





	// This should be the ONLY function outside of the constructor to do anything to FullSudoku.
	// I also don't want it to be called by anything in here; call it in DrawNumsConstructor.

	public void copyLastResortToSudoku(FullSudoku mySudoku)
	{
		// As a precautionary measure, even this function will not do anything unless
		// another solving method in this class changed wasSolveSuccessful to True.

		if(wasSolveSuccessful)
		{
			// For all squares that were not previously solved, give them their results,
			// set their numPossLeft to 1, set their possArray to null, and increment
			// squaresSolved. We should end at 81 squaresSolved during any success.

			Square presentSquare;

			for(int y=0;y<9;y++)
			{
				for(int x=0;x<9;x++)
				{
					presentSquare = mySudoku.SudokuMap[y][x];

					if(presentSquare.result == null)
					{
						presentSquare.numPossLeft = 1;
						presentSquare.result = computerSolveMap[y][x].primResult;
						presentSquare.possArray = null;

						mySudoku.squaresSolved++;
					}
				}
			}

			// Then set every slot in the possPrevalence arrays to 1, just because.

			for(int i=0;i<9;i++)
			{
				for(int j=0;j<9;j++)
				{
					mySudoku.rowPossPrevalence[i][j] = 1;
					mySudoku.colPossPrevalence[i][j] = 1;
					mySudoku.boxPossPrevalence[i][j] = 1;
				}
			}

			// Shouldn't REALLY matter, but this should never be called twice in a row

			wasSolveSuccessful = false;
		}
	}


	// Called by DrawNumsConstructor to make there is no inconsistency between
	// this and whatever UsingLogicalMethods came up with. I feel that it may
	// not be necessary, but I'll keep it until I'm sure.

	public boolean sameResults(FullSudoku mySudoku)
	{
		Square officialSquare;
		PrimitiveSquare lastResortSquare;

		for(int y=0;y<9;y++)
		{
			for(int x=0;x<9;x++)
			{
				officialSquare = mySudoku.SudokuMap[y][x];
				lastResortSquare = computerSolveMap[y][x];

				if(officialSquare.result != null && lastResortSquare.primResult != null)
				{
					if(!(officialSquare.result.equals(lastResortSquare.primResult)))
						return false;
				}
			}
		}


		return true;

	} // sameResults()


	// Called by DrawNumsConstructor

	public boolean initiateCrudeVirtualSolve()
	{
		boolean didItWork = crudeVirtualSolve();

		if(didItWork)
			wasSolveSuccessful = true;

		return didItWork;
	}


	// Basic trial-and-error solving function.
	// Returns true is puzzle is solvable, false if not.

	private boolean crudeVirtualSolve()
	{
		// Set results in all unknown PrimitiveSquare to 0

		for(int y=0;y<9;y++)
		{
			for(int x=0;x<9;x++)
			{
				if(computerSolveMap[y][x].primResultFixed == false)
					computerSolveMap[y][x].primResult = 0;
			}
		}


		// Start at the beginning

		PrimitiveSquare nowSquare = computerSolveMap[0][0];


		// Find the first PrimitiveSquare which is not solved.

		// nextNeededSquare() gets you to the next blank, automatically
		// moving forward at least one spot. But if the first square is not
		// solved yet, we don't want to leave it; we want to start there.

		if(nowSquare.primResultFixed)
		{
			// Also, if nextNeededSquare gets you null at the
			// beginning, then the puzzle is already solved

			if(nextNeededSquare(nowSquare) == null)
			{
				return true;
			}

			nowSquare = nextNeededSquare(nowSquare);
		}


		// Increment the result of the first unknown square, and BEGIN

		nowSquare.primResult++;

		boolean moreToDo = true;

		boolean isTheResultUnique;

		while(moreToDo)
		{
			isTheResultUnique = true;

			// First, check whether this square's (new) result appears
			// in an oval sharing a row/column/box with it

			if(isTheResultUnique)
				isTheResultUnique = noDupeOfResult(nowSquare,provideRow(nowSquare.primRow));

			if(isTheResultUnique)
				isTheResultUnique = noDupeOfResult(nowSquare,provideCol(nowSquare.primCol));

			if(isTheResultUnique)
				isTheResultUnique = noDupeOfResult(nowSquare,provideBox(nowSquare.primBox));


			// If the result is NOT unique, meaning it already appears in a
			// square sharing a row/column/box with nowSquare, then we cannot
			// advance. Work has to be done on this square or a previous one

			if(!(isTheResultUnique))
			{
				// If the current square's result has not yet reached 9

				if(!(nowSquare.primResult.equals(9)))
				{
					// Increment it

					nowSquare.primResult++;

					// Return to beginning of loop
				}

				// If the current square's result has reached 9

				else
				{
					// Go back to the previous square whose result is less than 9, setting any
					// other results along the way (including the current square) to zero.

					do
					{
						// If this takes you all the way back past the
						// beginning, the puzzle cannot be solved.

						if(prevNeededSquare(nowSquare) == null)
						{
							return false;
						}

						nowSquare.primResult = 0;
						nowSquare = prevNeededSquare(nowSquare);

					} while(nowSquare.primResult.equals(9));

					// Increment it

					nowSquare.primResult++;

					// Return to beginning of loop
				}
			}

			// If the current result in this square is UNIQUE (for now) and does not
			// appear in another oval within its row/column/box, we can proceed forward.

			else
			{
				// If we haven't reached the end, increment the
				// result of the next square and keep going

				if(nextNeededSquare(nowSquare) != null)
				{
					nowSquare = nextNeededSquare(nowSquare);
					nowSquare.primResult++;

					// Return to beginning of loop
				}

				// If we've reached the end, the puzzle is solved

				else
				{
					moreToDo = false;
				}
			}

		} // End of While loop

		return true;

	} // crudeVirtualSolve()


	private PrimitiveSquare prevNeededSquare(PrimitiveSquare i)
	{
		PrimitiveSquare squareToReturn = i;

		int myRow = squareToReturn.primRow;
		int myCol = squareToReturn.primCol;

		do
		{
			if(myRow == 0 && myCol == 0)
				return null;


			if(myCol == 0)
			{
				myRow--;
				myCol = 8;
			}
			else
				myCol--;

			squareToReturn = computerSolveMap[myRow][myCol];

		} while(squareToReturn.primResultFixed);

		return squareToReturn;
	}

	private PrimitiveSquare nextNeededSquare(PrimitiveSquare i)
	{
		PrimitiveSquare squareToReturn = i;

		int myRow = squareToReturn.primRow;
		int myCol = squareToReturn.primCol;

		do
		{
			if(myRow == 8 && myCol == 8)
				return null;


			if(myCol == 8)
			{
				myRow++;
				myCol = 0;
			}
			else
				myCol++;

			squareToReturn = computerSolveMap[myRow][myCol];

		} while(squareToReturn.primResultFixed);

		return squareToReturn;
	}


	// Serialization functions

	private void serializeItAll()
	{
		for(int y=0;y<9;y++)
		{
			for(int x=0;x<9;x++)
			{
				try
					{ SerializeSquare(computerSolveMap[y][x]); }
				catch(Exception p)
					{ }

				try
					{ computerSolveMap[y][x] = DeserializeSquare(); }
				catch(Exception q)
					{ }
			}
		}
	}

	private void SerializeSquare(PrimitiveSquare a) throws IOException
	{
		try
		{
			FileOutputStream fos = new FileOutputStream("Oval.obj");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(a);
		}
		catch(Exception p)
		{

		}
	}

	private PrimitiveSquare DeserializeSquare() throws IOException, ClassNotFoundException
	{
		PrimitiveSquare toReturn = null;

		try
		{
			FileInputStream fis = new FileInputStream("Oval.obj");
			ObjectInputStream ois = new ObjectInputStream(fis);
			toReturn = (PrimitiveSquare)ois.readObject();
		}
		catch(Exception q)
		{

		}

		return toReturn;
    }


	// Assists in traversing boxes

	private int[][] boxesToCheck = new int[][]{	{0,1,2,0,1,2},
												{0,1,2,3,4,5},
												{0,1,2,6,7,8},
												{3,4,5,0,1,2},
												{3,4,5,3,4,5},
												{3,4,5,6,7,8},
												{6,7,8,0,1,2},
												{6,7,8,3,4,5},
												{6,7,8,6,7,8} };


	// Provide a row of ovals

	private PrimitiveSquare[] provideRow(int rowNum)
	{
		PrimitiveSquare[] rowProvided = new PrimitiveSquare[9];

		for(int i=0;i<9;i++)
		{
			rowProvided[i] = computerSolveMap[rowNum][i];
		}

		return rowProvided;
	}

	// Provide a column of ovals

	private PrimitiveSquare[] provideCol(int colNum)
	{
		PrimitiveSquare[] colProvided = new PrimitiveSquare[9];

		for(int i=0;i<9;i++)
		{
			colProvided[i] = computerSolveMap[i][colNum];
		}

		return colProvided;

	}

	// Provide a box of ovals

	private PrimitiveSquare[] provideBox(int boxNum)
	{
		PrimitiveSquare[] boxProvided = new PrimitiveSquare[9];

		for(int i=0;i<9;i++)
		{
			boxProvided[i] = computerSolveMap[boxesToCheck[boxNum][i / 3]][boxesToCheck[boxNum][(i % 3) + 3]];
		}

		return boxProvided;

	}


	// Checks that an oval's result does not appear
	// in its RCB (except within the oval itself)

	private boolean noDupeOfResult(PrimitiveSquare s, PrimitiveSquare[] itsRCB)
	{
		Integer inputResult = s.primResult;
		boolean noDupes = true;

		for(int a=0;a<itsRCB.length;a++)
		{
			if(itsRCB[a].primResult.equals(inputResult))
			{
				if(!(itsRCB[a].primRow == s.primRow && itsRCB[a].primCol == s.primCol))
				{
					noDupes = false;
				}
			}
		}

		return noDupes;
	}

}

class PrimitiveSquare implements Serializable
{
	protected Integer primResult;

	protected boolean primResultFixed;

	protected int primRow;
	protected int primCol;
	protected int primBox;

	public PrimitiveSquare(Integer a, boolean b, int c, int d, int e)
	{
		primResult = a;
		primResultFixed = b;
		primRow = c;
		primCol = d;
		primBox = e;
	}
}