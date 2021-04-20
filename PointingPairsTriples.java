public class PointingPairsTriples
{
	public void PointingPairsTriples(FullSudoku mySudoku)
	{
		Square[] hasInPossArray = new Square[3];

		int numberWeTest = -33;
		int lovelyCounter = -33;

		boolean canWeChangeItRow;
		boolean canWeChangeItCol;

		int itsRow = -33;
		int itsCol = -33;

		Square squareProbe;
		int templateCol = -33;
		int templateRow = -33;


		// trickBox shall be the box we operate on at any given time

		Square[] trickBox;

		// New and improved PointingPairsTriples function uses PossPrevalence arrays

		int[] herePossPrevalence = new int[9];


		// Function, on its own, runs through the procedure for every one of the nine boxes.
		// From Box 0 through Box 8, each box gets a run through the loop.

		for(int a=0;a<9;a++)
		{
			// We already have a function which can provide the squares of a box, provideBox(int boxNum)
			// The first step for any box will be running that to get an array of the nine squares

			trickBox = mySudoku.provideBox(a);


			// Before, we created an array of nine integers representing the nine numbers
			// needed in any particular box, eliminated all which already appeared in the
			// box, then ran the pointing pairs procedure on the rest.

			// Now, we can simply use the PossPrevalence arrays

			herePossPrevalence = mySudoku.boxPossPrevalence[a];


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


					// Step 2.1: Check each square in trickBox to see whether that number is
					// in the possArray. If it is, the square goes in the array created for
					// squares that have the number in the possArray

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


					// Step 2.2: If all squares where that number can go are within a single
					// row or column (Check whether ownRow or ownCol are all identical), do Step 2.3

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


					// Step 2.3: Eliminate the appropriate numbers from the squares of the row or column
					// outside of that box, and decrease their numPossLeft if the possibility isn't already null

					// Make sure the row of all possible squares where the number can go in the box line up

					if(canWeChangeItRow)
					{

						// Go through every square in that row

						for(int i=0;i<9;i++)
						{
							squareProbe = mySudoku.SudokuMap[itsRow][i];

							templateCol = squareProbe.ownCol;


							// Make sure the square is not in the box

							if(templateCol != mySudoku.boxesToCheck[a][3] && templateCol != mySudoku.boxesToCheck[a][4] && templateCol != mySudoku.boxesToCheck[a][5])
							{
								mySudoku.elimFromPossArray(squareProbe,numberWeTest);
							}
						}

						// If we have accomplished something, activate continuousIntenseSolve()

						mySudoku.continuousIntenseSolve();
					}

					// Make sure the column of all possible squares where the number can go in the box line up

					if(canWeChangeItCol)
					{

						// Go through every square in that column

						for(int i=0;i<9;i++)
						{
							squareProbe = mySudoku.SudokuMap[i][itsCol];

							templateRow = squareProbe.ownRow;


							// Make sure the square is not in the box

							if(templateRow != mySudoku.boxesToCheck[a][0] && templateRow != mySudoku.boxesToCheck[a][1] && templateRow != mySudoku.boxesToCheck[a][2])
							{
								mySudoku.elimFromPossArray(squareProbe,numberWeTest);
							}
						}

						// If we have accomplished something, activate continuousIntenseSolve()

						mySudoku.continuousIntenseSolve();
					}
				}

			} // End of loop for all numbers within a single box

		} // Everything in the loop occurs once for each of the 9 boxes

	} // PointingPairsTriples()

} // PointingPairsTriples