public class ClaimingPairsTriples
{
	// Look at a single row or column, and check whether
	// any number which does not yet exist in the set is
	// restricted to appearing only in a single box.

	// If so, eliminate that number as a possibility in
	// every square of the box except the row or column.

	private FullSudoku mySudoku;
	private MethodExplanations myMethods;

	public ClaimingPairsTriples(FullSudoku theSudoku, MethodExplanations theMethods)
	{
		mySudoku = theSudoku;
		myMethods = theMethods;
	}


	// Type of set being checked: Row or Column

	private HouseType setTypeExamined;

	// int from 0 to 8 indicating the row/column

	private int whichRowCol;

	// Actual squares making up the row/column

	private Square[] squaresOfRowCol;

	// PossPrevalence array for the row/column

	private int[] herePossPrevalence;

	// Number being tested

	private int numberWeTest;

	// Based on the number's PossPrevalence: Pair or Triple

	private PTQ whichMayWeFind;

	// int from 0 to 8 indicating the affected box

	private int whichBox;

	// Whether a Claiming Pair/Triple has made progress in solving

	private boolean didWeGetOne;


	// Function that starts it all. Runs the procedure on
	// each of the nine rows and each of the nine columns

	public boolean ClaimingPairsTriples()
	{
		didWeGetOne = false;


		// One loop for the 9 rows, one loop for the 9 columns

		for(int i=0;i<2;i++)
		{
			// Note whether we are currently doing rows or columns

			switch(i)
			{
				case 0: setTypeExamined = HouseType.Row; break;
				case 1: setTypeExamined = HouseType.Col; break;
			}


			// From Row/Col 0 through Row/Col 8, each set gets a run through the following loop.

			for(int a=0;a<9;a++)
			{
				whichRowCol = a;

				// Get the array of the nine squares

				if(setTypeExamined == HouseType.Row)
					squaresOfRowCol = mySudoku.provideRow(whichRowCol);
				else
					squaresOfRowCol = mySudoku.provideCol(whichRowCol);


				// Retrieve appropriate PossPrevalence array

				if(setTypeExamined == HouseType.Row)
					herePossPrevalence = mySudoku.rowPossPrevalence[whichRowCol];
				else
					herePossPrevalence = mySudoku.colPossPrevalence[whichRowCol];


				// Start running the numbers

				checkEachNumber();


			} // Everything in the loop occurs once for each of the 9 rows or columns

		} // First loop is for the 9 rows, second loop is for the 9 columns


		return didWeGetOne;

	} // ClaimingPairsTriples()


	private void checkEachNumber()
	{
		Square[] hasInPossArray = new Square[3];
		int lovelyCounter;
		boolean allInOneBox;

		// Each of the 9 numbers steps up to the plate

		for(int b=1;b<=9;b++)
		{
			// Continue if we haven't already found a Claiming Pair/Triple

			// This is a good point to have this cutoff because any of the 9 rows or 9 columns
			// can contain no more than one Claiming Pair/Triple for any of the 9 numbers, so
			// from this point onward a maximum of one Claiming Pair/Triple can be found before
			// the program returns to this line again.

			if(!(didWeGetOne))
			{
				numberWeTest = b;


				// This is where the PossPrevalence arrays come in. Continue only
				// if the number exists in the possArray of either 2 or 3 squares

				if(herePossPrevalence[numberWeTest-1] == 2 || herePossPrevalence[numberWeTest-1] == 3)
				{
					// possPrevalence of 2 means that if we find anything, it's a Claiming Pair
					// possPrevalence of 3 means that if we find anything, it's a Claiming Triple

					switch(herePossPrevalence[numberWeTest-1])
					{
						case 2: whichMayWeFind = PTQ.Pair; break;
						case 3: whichMayWeFind = PTQ.Trip; break;
					}

					// Reset hasInPossArray

					for(int c=0;c<3;c++)
					{
						hasInPossArray[c] = null;
					}

					lovelyCounter = 0;


					// Check every square in the row/column, adding any square
					// with the number in its possArray to hasInPossArray

					for(int c=0;c<9;c++)
					{
						if(squaresOfRowCol[c].result == null)
						{
							if(squaresOfRowCol[c].possArray[numberWeTest - 1] != null)
							{
								hasInPossArray[lovelyCounter] = squaresOfRowCol[c];
								lovelyCounter++;
							}
						}
					}


					// Decide if all squares where that number can go are within a single box
					// (Check whether ownBox are all identical). If so, move to final step.

					allInOneBox = true;
					lovelyCounter = 0;

					while(lovelyCounter < 3 && hasInPossArray[lovelyCounter] != null)
					{
						if(lovelyCounter == 0)
						{
							whichBox = hasInPossArray[lovelyCounter].ownBox;
						}
						else
						{
							if(whichBox != hasInPossArray[lovelyCounter].ownBox)
								allInOneBox = false;
						}

						lovelyCounter++;
					}

					if(allInOneBox)
					{
						commenceElimination();
					}
				}

			}

		} // End of loop for each number within a single row or column

	} // checkEachNumber()


	private void commenceElimination()
	{
		Square[] boxToWorkOn = mySudoku.provideBox(whichBox);

		Square squareProbe; int probeRowCol;

		boolean temp; int affectedCounter = 0;


		// Go through all 9 squares in that box

		for(int i=0;i<9;i++)
		{
			squareProbe = boxToWorkOn[i];

			if(setTypeExamined == HouseType.Row)
				probeRowCol = squareProbe.ownRow;
			else
				probeRowCol = squareProbe.ownCol;


			// Eliminate only if a square is not one of the three squares
			// in the row or column containing the Claiming Pair or Triple

			if(probeRowCol != whichRowCol)
			{
				temp = mySudoku.elimFromPossArray(squareProbe,numberWeTest);

				if(temp)
				{
					// For stuff which needs to happen only once when a Claiming Pair/Triple is found

					if(!(didWeGetOne))
					{
						// Clear enough in myMethods to explain elimination of one possibility

						myMethods.clearEnoughForNew(SolveMethod.ClaimingPairTriple);


						// Insert number we use for the Pointing Pair/Triple

						myMethods.arrayOfKilledPoss[0] = numberWeTest;


						// Put the other constants in place

						myMethods.houseContextType = setTypeExamined;
						myMethods.houseTargetType = HouseType.Box;
						myMethods.subsetType = whichMayWeFind;
						myMethods.intTargetSet = whichBox;
						myMethods.miscNumsA[0] = whichRowCol;

						didWeGetOne = true;
					}

					// Record square in squaresForKilledPoss, increment counter

					myMethods.squaresForKilledPoss[0][affectedCounter] = i;
					affectedCounter++;
				}
			}
		}

	} // commenceElimination()

} // ClaimingPairsTriples