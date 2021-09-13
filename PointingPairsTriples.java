// Utilized by UsingLogicalMethods

public class PointingPairsTriples
{
	// Go through each of the 9 boxes, and determine whether any (unsolved) number
	// in any box is limited to one row or column within that box. If so, eliminate
	// that number from all squares in that row or column but not in that box.

	private FullSudoku mySudoku;
	private BoxTranslator myUnboxer;
	private MethodExplanations myMethods;

	public PointingPairsTriples(FullSudoku theSudoku, MethodExplanations theMethods)
	{
		mySudoku = theSudoku;
		myUnboxer = theSudoku.theUnboxer;
		myMethods = theMethods;
	}


	// int from 0 to 8 indicating the box

	private int whichBox;

	// Actual squares making up the box

	private Square[] squaresOfBox;

	// PossPrevalence array for the box

	private int[] herePossPrevalence;

	// Number being tested

	private int numberWeTest;

	// Based on the number's PossPrevalence: Pair or Triple

	private PTQ whichMayWeFind;

	// int from 0 to 8 indicating the affected row/column

	private int whichRow;
	private int whichCol;

	// Whether a Pointing Pair/Triple has made progress in solving

	private boolean didWeGetOne;


	// Function, on its own, runs through the procedure for every one of the nine boxes.

	public boolean PointingPairsTriples()
	{
		didWeGetOne = false;


		// From Box 0 through Box 8, each box gets a run through the loop.

		for(int a=0;a<9;a++)
		{
			whichBox = a;


			// We already have a function which can provide the squares of a box, provideBox(int boxNum)
			// The first step for any box will be running that to get an array of the nine squares

			squaresOfBox = mySudoku.provideBox(whichBox);


			// PossPrevalence arrays will be utilized to determine the amount of
			// squares within any box that are capable of containing the number

			herePossPrevalence = mySudoku.boxPossPrevalence[whichBox];


			// Next step

			checkEachNumber();

		} // Everything in the loop occurs once for each of the 9 boxes


		return didWeGetOne;

	} // PointingPairsTriples()


	private void checkEachNumber()
	{
		Square[] hasInPossArray = new Square[3];
		int lovelyCounter;
		boolean canWeChangeItRow;
		boolean canWeChangeItCol;

		// Each number steps up to the plate

		for(int b=1;b<=9;b++)
		{
			// Continue if we haven't already found a Pointing Pair/Triple

			// This is a good point to have this cutoff because the 9 boxes can contain
			// no more than one Pointing Pair/Triple for any of the 9 numbers, so from
			// this point onward a maximum of one Pointing Pair/Triple can be found
			// before the program returns to this line again.

			if(!(didWeGetOne))
			{
				numberWeTest = b;


				// This is where the PossPrevalence arrays come in. Continue only
				// if the number exists in the possArray of either 2 or 3 squares

				if(herePossPrevalence[numberWeTest-1] == 2 || herePossPrevalence[numberWeTest-1] == 3)
				{
					// possPrevalence of 2 means that if we find anything, it's a Pointing Pair
					// possPrevalence of 3 means that if we find anything, it's a Pointing Triple

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


					// Check every square in the box, adding any square
					// with the number in its possArray to hasInPossArray

					for(int c=0;c<9;c++)
					{
						if(squaresOfBox[c].result == null)
						{
							if(squaresOfBox[c].possArray[numberWeTest - 1] != null)
							{
								hasInPossArray[lovelyCounter] = squaresOfBox[c];
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
							whichRow = hasInPossArray[lovelyCounter].ownRow;
							whichCol = hasInPossArray[lovelyCounter].ownCol;
						}
						else
						{
							if(whichRow != hasInPossArray[lovelyCounter].ownRow)
								canWeChangeItRow = false;

							if(whichCol != hasInPossArray[lovelyCounter].ownCol)
								canWeChangeItCol = false;
						}

						lovelyCounter++;
					}


					// Final step

					if(canWeChangeItRow)
						rowOrColElimination(HouseType.Row);

					if(canWeChangeItCol)
						rowOrColElimination(HouseType.Col);
				}

			}

		} // End of loop for all numbers within a single box

	} // checkEachNumber()


	private void rowOrColElimination(HouseType whichOne)
	{
		Square[] squaresOfElimSet;
		Square squareProbe;

		boolean bigBoom;

		int affectedCounter = 0;


		// Retrieve Row or Col as necessary

		if(whichOne == HouseType.Row)
			squaresOfElimSet = mySudoku.provideRow(whichRow);
		else
			squaresOfElimSet = mySudoku.provideCol(whichCol);


		// Go through every square in that Row or Col

		// In a row, i = the square's Col
		// In a col, i = the square's Row

		for(int i=0;i<9;i++)
		{
			squareProbe = squaresOfElimSet[i];


			// Make sure the square is not in the box

			boolean canWePass = false;

			if(whichOne == HouseType.Row)
			{
				if(!(myUnboxer.doesColOverlapBox(whichBox,i)))
					canWePass = true;
			}
			else
			{
				if(!(myUnboxer.doesRowOverlapBox(whichBox,i)))
					canWePass = true;
			}

			if(canWePass)
			{

				// Knock numberWeTest out of the square's possArray. For each of the six squares
				// in the Row or Col where this is attempted, bigBoom goes to true if the number
				// we test is eliminated right now with this, or false if the number had already
				// been eliminated using this or another solving technique.

				bigBoom = mySudoku.elimFromPossArray(squareProbe,numberWeTest);

				if(bigBoom)
				{
					// Progress made. Starting making notes in MethodExplanations


					// For stuff which needs to happen only once when a meaningful Pointing Pair/Triple is found

					if(!(didWeGetOne))
					{
						// Clear enough in myMethods to explain elimination of one possibility

						myMethods.clearEnoughForNew(SolveMethod.PointingPairTriple);


						// Insert number we use for the Pointing Pair/Triple

						myMethods.arrayOfKilledPoss[0] = numberWeTest;


						// Put the other constants in place

						if(whichOne == HouseType.Row)
						{
							myMethods.houseTargetType = HouseType.Row;
							myMethods.intTargetSet = whichRow;
						}
						else
						{
							myMethods.houseTargetType = HouseType.Col;
							myMethods.intTargetSet = whichCol;
						}

						myMethods.subsetType = whichMayWeFind;
						myMethods.miscNumsA[0] = whichBox;

						didWeGetOne = true;
					}

					// Record square in squaresForKilledPoss, increment counter

					myMethods.squaresForKilledPoss[0][affectedCounter] = i;
					affectedCounter++;
				}
			}
		}

	} // rowOrColElimination()

} // PointingPairsTriples