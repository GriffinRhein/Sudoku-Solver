public class SolveChecker
{
	private FullSudoku mySudoku;
	private int numRowCol;

	public SolveChecker(FullSudoku a)
	{
		mySudoku = a;
		numRowCol = mySudoku.numRowCol;
	}


	public boolean isSudokuDupeFree()
	{
		boolean areWeDupeFree = true;
		int currentRCB = 0; 

		while(areWeDupeFree && currentRCB < numRowCol)
		{
			areWeDupeFree = checkOneRCB(mySudoku.provideRow(currentRCB));
			currentRCB++;
		}

		currentRCB = 0;

		while(areWeDupeFree && currentRCB < numRowCol)
		{
			areWeDupeFree = checkOneRCB(mySudoku.provideCol(currentRCB));
			currentRCB++;
		}

		currentRCB = 0;

		while(areWeDupeFree && currentRCB < numRowCol)
		{
			areWeDupeFree = checkOneRCB(mySudoku.provideBox(currentRCB));
			currentRCB++;
		}

		return areWeDupeFree;

	} // isSudokuDupeFree()


	private boolean checkOneRCB(Square[] theRCB)
	{
		// Create array of booleans, and fill it with false

		boolean[] allPoss = new boolean[numRowCol];

		for(int a=0;a<numRowCol;a++)
		{
			allPoss[a] = false;
		}


		Integer currentResult;

		// Check the result of each square. If that result has not appeared before,
		// set its corresponding boolean in allPoss to true. If the result has
		// appeared before, areWeDupeFree is false.

		for(int a=0;a<numRowCol;a++)
		{
			currentResult = theRCB[a].result;

			if(currentResult != null)
			{
				if(allPoss[currentResult - 1] == false)
				{
					allPoss[currentResult - 1] = true;
				}
				else
				{
					return false;
				}
			}
		}

		return true;

	} // checkForDupesOneRCB()

} // SolveChecker class