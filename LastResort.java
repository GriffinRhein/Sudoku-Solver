public class LastResort
{
	private FullSudoku mySudoku;
	private boolean wasSolveSuccessful;

	public LastResort(FullSudoku theSudoku)
	{
		mySudoku = theSudoku;
		wasSolveSuccessful = false;


		Square currentSquare;

		for(int y=0;y<9;y++)
		{
			for(int x=0;x<9;x++)
			{
				currentSquare = mySudoku.SudokuMap[y][x];

				if(currentSquare.result != null)
				{
					currentSquare.lastResortResult = currentSquare.result;
					currentSquare.lastResortIsResultFixed = true;
				}
				else
				{
					currentSquare.lastResortResult = null;
					currentSquare.lastResortIsResultFixed = false;
				}
			}
		}
	}


	// This should be the ONLY function outside of the constructor to do anything to FullSudoku.
	// I also don't want it to be called by anything in here; call it in DrawNumsConstructor.

	public void copyLastResortToSudoku()
	{
		// As a precautionary measure, even this function will not do anything unless
		// another solving method in this class changed wasSolveSuccessful to True.

		if(wasSolveSuccessful)
		{
			// For all squares that were not previously solved, give them their results,
			// set their numPossLeft to 1, set their possArray to null, and increment
			// squaresSolved. We should end at 81 squaresSolved during any success.

			Square currentSquare;

			for(int y=0;y<9;y++)
			{
				for(int x=0;x<9;x++)
				{
					currentSquare = mySudoku.SudokuMap[y][x];

					if(currentSquare.result == null)
					{
						currentSquare.result = currentSquare.lastResortResult;
						currentSquare.possArray = null;
						currentSquare.numPossLeft = 1;

						mySudoku.squaresSolved++;
					}
				}
			}

			// Then set every slot in the possPrevalence arrays to 1,
			// since it's appropriate for a finished puzzle.

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

	} // copyLastResortToSudoku()


	// Ensures that there is no inconsistency between what LastResort came up with
	// and whatever UsingLogicalMethods came up with. I feel that it may not be
	// necessary, but I'll keep it until I'm sure.

	public boolean sameResults()
	{
		Square officialSquare;

		for(int y=0;y<9;y++)
		{
			for(int x=0;x<9;x++)
			{
				officialSquare = mySudoku.SudokuMap[y][x];

				if(officialSquare.result != null && officialSquare.lastResortResult != null)
				{
					if(!(officialSquare.result.equals(officialSquare.lastResortResult)))
						return false;
				}
			}
		}


		return true;

	} // sameResults()


	// Called by DrawNumsConstructor

	public boolean initiateCrudeVirtualSolve()
	{
		wasSolveSuccessful = crudeVirtualSolve();

		return wasSolveSuccessful;
	}


	// Basic trial-and-error solving function.
	// Returns true is puzzle is solvable, false if not.

	private boolean crudeVirtualSolve()
	{
		Square currentSquare;


		// Set all unknown results to 0

		for(int y=0;y<9;y++)
		{
			for(int x=0;x<9;x++)
			{
				currentSquare = mySudoku.SudokuMap[y][x];

				if(currentSquare.lastResortIsResultFixed == false)
					currentSquare.lastResortResult = 0;
			}
		}


		// Start at the beginning

		currentSquare = mySudoku.SudokuMap[0][0];


		// Find the first square which is not solved.

		// nextNeededSquare() gets you to the next blank, automatically
		// moving forward at least one spot. But if the first square is not
		// solved yet, we don't want to leave it; we want to start there.

		if(currentSquare.lastResortIsResultFixed)
		{
			// Also, if nextNeededSquare gets you null at the
			// beginning, then the puzzle is already solved

			if(nextNeededSquare(currentSquare) == null)
			{
				return true;
			}

			currentSquare = nextNeededSquare(currentSquare);
		}


		// Increment the result of the first unknown square, and BEGIN

		currentSquare.lastResortResult++;

		boolean moreToDo = true;

		boolean isTheResultUnique;

		while(moreToDo)
		{
			isTheResultUnique = true;

			// First, check whether this square's (new) result appears
			// in an square sharing a row/column/box with it

			if(isTheResultUnique)
				isTheResultUnique = noDupeOfResult(currentSquare,mySudoku.provideRow(currentSquare.ownRow));

			if(isTheResultUnique)
				isTheResultUnique = noDupeOfResult(currentSquare,mySudoku.provideCol(currentSquare.ownCol));

			if(isTheResultUnique)
				isTheResultUnique = noDupeOfResult(currentSquare,mySudoku.provideBox(currentSquare.ownBox));


			// If the result is NOT unique, meaning it already appears in a square
			// sharing a row/column/box with the current square, then we cannot
			// advance. Work has to be done on this square or a previous one

			if(!(isTheResultUnique))
			{
				// If the current square's result has not yet reached 9

				if(!(currentSquare.lastResortResult.equals(9)))
				{
					// Increment it

					currentSquare.lastResortResult++;

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

						if(prevNeededSquare(currentSquare) == null)
						{
							return false;
						}

						currentSquare.lastResortResult = 0;
						currentSquare = prevNeededSquare(currentSquare);

					} while(currentSquare.lastResortResult.equals(9));

					// Increment it

					currentSquare.lastResortResult++;

					// Return to beginning of loop
				}
			}

			// If the current result in this square is UNIQUE (for now) and does not
			// appear in another square within its row/column/box, we can proceed forward.

			else
			{
				// If we haven't reached the end, increment the
				// result of the next square and keep going

				if(nextNeededSquare(currentSquare) != null)
				{
					currentSquare = nextNeededSquare(currentSquare);
					currentSquare.lastResortResult++;

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


	private Square prevNeededSquare(Square i)
	{
		Square squareToReturn = i;

		int myRow = squareToReturn.ownRow;
		int myCol = squareToReturn.ownCol;

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

			squareToReturn = mySudoku.SudokuMap[myRow][myCol];

		} while(squareToReturn.lastResortIsResultFixed);

		return squareToReturn;

	} // prevNeededSquare()


	private Square nextNeededSquare(Square i)
	{
		Square squareToReturn = i;

		int myRow = squareToReturn.ownRow;
		int myCol = squareToReturn.ownCol;

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

			squareToReturn = mySudoku.SudokuMap[myRow][myCol];

		} while(squareToReturn.lastResortIsResultFixed);

		return squareToReturn;

	} // nextNeededSquare()


	// Checks that an square's result does not appear
	// in its RCB (except within the square itself)

	private boolean noDupeOfResult(Square s, Square[] itsRCB)
	{
		Integer inputResult = s.lastResortResult;
		boolean noDupes = true;

		for(int a=0;a<itsRCB.length;a++)
		{
			if(itsRCB[a].lastResortResult.equals(inputResult))
			{
				if(!(itsRCB[a].ownRow == s.ownRow && itsRCB[a].ownCol == s.ownCol))
				{
					noDupes = false;
				}
			}
		}

		return noDupes;

	} // noDupeOfResult()

} // LastResort class