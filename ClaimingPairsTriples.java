public class ClaimingPairsTriples
{
	// Similar to Pointing Pairs & Triples. Only difference is that
	// instead of looking at whether every square where a number can
	// go within a box lies within a single row or column, you look at
	// whether every square where a number can go within a row or column
	// lies within a single box.

	Square[] hasInPossArray = new Square[3];

	int numberWeTest = -33;
	int lovelyCounter = -33;

	int isItRowOrCol = -33;

	int simpleRC = -33;

	boolean allInOneBox;

	int itsBox = -33;

	Square squareProbe;
	int templateRowOrCol = -33;

	Square[] trickRowOrCol;

	int[] herePossPrevalence = new int[9];

	// Assists in traversing boxes

	int[][] claimBoxes = new int[][]{{0,1,2,0,1,2},
									 {0,1,2,3,4,5},
									 {0,1,2,6,7,8},
									 {3,4,5,0,1,2},
									 {3,4,5,3,4,5},
									 {3,4,5,6,7,8},
									 {6,7,8,0,1,2},
									 {6,7,8,3,4,5},
									 {6,7,8,6,7,8} };


	// Function that starts it all. Runs the procedure on
	// each of the nine rows and each of the nine columns

	public void ClaimingPairsTriples(FullSudoku mySudoku)
	{
		// One loop for the 9 rows, one loop for the 9 columns

		for(int RowOrCol=0;RowOrCol<=1;RowOrCol++)
		{
			isItRowOrCol = RowOrCol;

			// From Row/Col 0 through Row/Col 8, each set gets a run through the following loop.

			for(int a=0;a<9;a++)
			{
				simpleRC = a;

				// Get the array of the nine squares

				if(isItRowOrCol == 0)
					trickRowOrCol = mySudoku.provideRow(simpleRC);
				else
					trickRowOrCol = mySudoku.provideCol(simpleRC);


				// Retrieve appropriate PossPrevalence array

				if(isItRowOrCol == 0)
					herePossPrevalence = mySudoku.rowPossPrevalence[simpleRC];
				else
					herePossPrevalence = mySudoku.colPossPrevalence[simpleRC];


				// Start running the numbers

				checkEachNumber(mySudoku);


			} // Everything in the loop occurs once for each of the 9 rows or columns

		} // First loop is for the 9 rows, second loop is for the 9 columns

	} // ClaimingPairsTriples()


	private void checkEachNumber(FullSudoku mySudoku)
	{
		// Each of the 9 numbers steps up to the plate

		for(int b=1;b<=9;b++)
		{
			// This is where the PossPrevalence arrays come in. Continue only
			// if the number exists in either 2 or 3 possArrays in the row/col

			if(herePossPrevalence[b-1] == 2 || herePossPrevalence[b-1] == 3)
			{
				// Reset hasInPossArray

				for(int c=0;c<3;c++)
				{
					hasInPossArray[c] = null;
				}

				numberWeTest = b;
				lovelyCounter = 0;


				// Check every square in the row/column, adding any square
				// with the number in its possArray to hasInPossArray

				for(int c=0;c<9;c++)
				{
					if(trickRowOrCol[c].result == null)
					{
						if(trickRowOrCol[c].possArray[numberWeTest - 1] != null)
						{
							hasInPossArray[lovelyCounter] = trickRowOrCol[c];
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
						itsBox = hasInPossArray[lovelyCounter].ownBox;
					}
					else
					{
						if(itsBox != hasInPossArray[lovelyCounter].ownBox)
							allInOneBox = false;
					}

					lovelyCounter++;
				}

				if(allInOneBox)
				{
					commenceElimination(mySudoku);
				}
			}

		} // End of loop for each number within a single row or column

	} // checkEachNumber()


	private void commenceElimination(FullSudoku mySudoku)
	{
		// Go through all 9 squares in that box

		for(int i=0;i<=2;i++)
		{
			for(int j=3;j<=5;j++)
			{
				squareProbe = mySudoku.SudokuMap[claimBoxes[itsBox][i]][claimBoxes[itsBox][j]];

				if(isItRowOrCol == 0)
					templateRowOrCol = squareProbe.ownRow;
				else
					templateRowOrCol = squareProbe.ownCol;


				// Eliminate only if a square is not one of the three squares
				// in the row or column containing the Claiming Pair or Triple

				if(templateRowOrCol != simpleRC)
				{
					mySudoku.elimFromPossArray(squareProbe,numberWeTest);
				}
			}
		}

		// If we have accomplished something, activate continuousIntenseSolve()

		mySudoku.continuousIntenseSolve();

	} // commenceElimination()

} // ClaimingPairsTriples