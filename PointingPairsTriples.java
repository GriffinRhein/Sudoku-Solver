public class PointingPairsTriples
{
	// Go through each of the 9 boxes, and determine whether any (unsolved) number
	// in any box is limited to one row or column within that box. If so, eliminate
	// that number from all squares in that row or column but not in that box.

	Square[] hasInPossArray = new Square[3];

	int numberWeTest = -33;
	int lovelyCounter = -33;

	int intBox = -33;

	boolean canWeChangeItRow;
	boolean canWeChangeItCol;

	int itsRow = -33;
	int itsCol = -33;

	Square squareProbe;
	int templateCol = -33;
	int templateRow = -33;

	Square[] trickBox;

	int[] herePossPrevalence = new int[9];


	// Function, on its own, runs through the procedure for every one of the nine boxes.

	public void PointingPairsTriples(FullSudoku mySudoku)
	{
		// From Box 0 through Box 8, each box gets a run through the loop.

		for(int a=0;a<9;a++)
		{
			intBox = a;


			// We already have a function which can provide the squares of a box, provideBox(int boxNum)
			// The first step for any box will be running that to get an array of the nine squares

			trickBox = mySudoku.provideBox(intBox);


			// PossPrevalence arrays will be utilized to determine the amount of
			// squares within any box that are capable of containing the number

			herePossPrevalence = mySudoku.boxPossPrevalence[intBox];


			// Next step

			checkEachNumber(mySudoku);


		} // Everything in the loop occurs once for each of the 9 boxes

	} // PointingPairsTriples()


	private void checkEachNumber(FullSudoku mySudoku)
	{
		// Each number steps up to the plate

		for(int b=1;b<=9;b++)
		{
			// This is where the PossPrevalence arrays come in. Continue only
			// if the number exists in either 2 or 3 possArrays in the box

			if(herePossPrevalence[b-1] == 2 || herePossPrevalence[b-1] == 3)
			{
				// Reset hasInPossArray

				for(int c=0;c<3;c++)
				{
					hasInPossArray[c] = null;
				}

				numberWeTest = b;
				lovelyCounter = 0;


				// Check every square in the box, adding any square
				// with the number in its possArray to hasInPossArray

				for(int c=0;c<9;c++)
				{
					if(trickBox[c].result == null)
					{
						if(trickBox[c].possArray[numberWeTest - 1] != null)
						{
							hasInPossArray[lovelyCounter] = trickBox[c];
							lovelyCounter++;
						}
					}
				}


				// Decide if all squares where that number can go are within a single row or column
				// (Check whether ownRow or ownCol are all identical). If so, move to final step.

				canWeChangeItRow = true;
				canWeChangeItCol = true;
				lovelyCounter = 0;

				while(lovelyCounter < 3 && hasInPossArray[lovelyCounter] != null)
				{
					if(lovelyCounter == 0)
					{
						itsRow = hasInPossArray[lovelyCounter].ownRow;
						itsCol = hasInPossArray[lovelyCounter].ownCol;
					}
					else
					{
						if(itsRow != hasInPossArray[lovelyCounter].ownRow)
							canWeChangeItRow = false;

						if(itsCol != hasInPossArray[lovelyCounter].ownCol)
							canWeChangeItCol = false;
					}

					lovelyCounter++;
				}


				// Final step

				if(canWeChangeItRow)
					rowElimination(mySudoku);

				if(canWeChangeItCol)
					colElimination(mySudoku);
			}

		} // End of loop for all numbers within a single box

	} // checkEachNumber()


	private void rowElimination(FullSudoku mySudoku)
	{
		// Go through every square in that row

		for(int i=0;i<9;i++)
		{
			squareProbe = mySudoku.SudokuMap[itsRow][i];

			templateCol = squareProbe.ownCol;


			// Make sure the square is not in the box

			if(templateCol != mySudoku.boxesToCheck[intBox][3] && templateCol != mySudoku.boxesToCheck[intBox][4] && templateCol != mySudoku.boxesToCheck[intBox][5])
			{
				mySudoku.elimFromPossArray(squareProbe,numberWeTest);
			}
		}

		// If we have accomplished something, activate continuousIntenseSolve()

		mySudoku.continuousIntenseSolve();

	} // rowElimination()


	private void colElimination(FullSudoku mySudoku)
	{
		// Go through every square in that column

		for(int i=0;i<9;i++)
		{
			squareProbe = mySudoku.SudokuMap[i][itsCol];

			templateRow = squareProbe.ownRow;


			// Make sure the square is not in the box

			if(templateRow != mySudoku.boxesToCheck[intBox][0] && templateRow != mySudoku.boxesToCheck[intBox][1] && templateRow != mySudoku.boxesToCheck[intBox][2])
			{
				mySudoku.elimFromPossArray(squareProbe,numberWeTest);
			}
		}

		// If we have accomplished something, activate continuousIntenseSolve()

		mySudoku.continuousIntenseSolve();

	} // colElimination()

} // PointingPairsTriples