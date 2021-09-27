// Utilized by UsingLogicalMethods

public class HiddenSingle
{
	// Check each row, column, and box to see whether
	// any number which does not yet exist as a result
	// in that house has been killed as a possibility
	// for all but one square.

	// Should one such number be found, the square where
	// it remains in the possArray will lose everything
	// in that possArray except for our number.

	// Then, since elimFromPossArray() is guaranteed to
	// put that square in the Linked List, and there is
	// not much point in making a separate step out of
	// setting the result, call NakedSingle()

	private FullSudoku mySudoku;
	private MethodExplanations myMethods;

	HiddenSingle(FullSudoku theSudoku, MethodExplanations theMethods)
	{
		mySudoku = theSudoku;
		myMethods = theMethods;
	}


	private Square[] currentRCB = new Square[9];
	private int[] herePossPrevalence = new int[9];

	private int recordOfA;
	private int recordOfB;

	private Square squareInRCB;

	private int traverseTheSet;
	private boolean foundThatSquare;

	private boolean didWeGetOne;


	// Hidden Single Implementation

	boolean HiddenSingle()
	{
		didWeGetOne = false;

		// a=0 for rows, a=1 for columns, a=2 for boxes

		for(int a=0;a<3;a++)
		{
			recordOfA = a;

			// b=0 for first RCB, b=1 for second RCB, etc.

			for(int b=0;b<9;b++)
			{
				recordOfB = b;

				if(a == 0)
				{
					currentRCB = mySudoku.provideRow(b);
					herePossPrevalence = mySudoku.rowPossPrevalence[b];
				}
				else if(a == 1)
				{
					currentRCB = mySudoku.provideCol(b);
					herePossPrevalence = mySudoku.colPossPrevalence[b];
				}
				else if(a == 2)
				{
					currentRCB = mySudoku.provideBox(b);
					herePossPrevalence = mySudoku.boxPossPrevalence[b];
				}

				bringUpEachNumber();


			} // Everything in this loop occurs once for each of the 27 sets
		}


		return didWeGetOne;

	} // HiddenSingle()


	private void bringUpEachNumber()
	{
		// Each number from 1 through 9 gets a turn at bat

		for(int testInt=1;testInt<=9;testInt++)
		{
			// Continue if we haven't already found a Hidden Single

			if(!(didWeGetOne))
			{
				// Check whether the PossPrevalence array indicates that there is
				// only one square within the RCB capable of containing the number.

				if(herePossPrevalence[testInt-1] < 2)
				{
					examineRCB(testInt);
				}
			}
		}

	} // bringUpEachNumber()


	private void examineRCB(int testInt)
	{
		// Go through the RCB and find the square where the number is still available.

		traverseTheSet = 0;
		foundThatSquare = false;

		// Note that a number which has only one possible square retains PossPrevalence of 1
		// after being officially solved for the RCB, so most of the time when we reach this
		// point it will be with the number already in the result slot for some square.

		while(traverseTheSet<9 && !(foundThatSquare))
		{
			squareInRCB = currentRCB[traverseTheSet];


			// If you find a square that is already solved

			if(squareInRCB.result != null)
			{
				// Just check whether its result is this number

				if(squareInRCB.result == testInt)
				{
					// If so, no need to look any further through the set.
					// Just come back to this function with the next number.

					foundThatSquare = true;
				}
			}


			// If you find a square that is not solved

			else
			{
				// By the time we have reached this point, we know that only
				// one square in this RCB contains the number in its possArray,
				// and that the square we are on is not solved.

				if(squareInRCB.possArray[testInt-1] != null)
				{
					// If this square contains the number in its possArray, it is
					// the only square in the RCB to still do so, and therefore
					// that number must be its result.

					boolean temp;

					for(int g=1;g<=9;g++)
					{
						if(g != testInt)
						{
							temp = mySudoku.elimFromPossArray(squareInRCB,g);

							if(temp)
							{
								didWeGetOne = true;

								myMethods.SquareA = squareInRCB;
								myMethods.foundResult = testInt;
								myMethods.intTargetSet = recordOfB;
								myMethods.houseTargetType = HouseType.values()[recordOfA];
							}
						}
					}

					foundThatSquare = true;
				}
			}


			// On to the next square in the row/column/box, assuming we have not
			// found either a square which already contains the number or the
			// lone square in the row/column/box which must contain the number.

			traverseTheSet++;
		}


		// Common understanding of Hidden Singles is that they immediately
		// solve the square in question. No need to bother with a separate
		// step to make the square's only remaining possibility its result

		if(didWeGetOne)
		{
			// Since HiddenSingle is called only when NakedSingle has failed on
			// the run through the loop, the linked list must have been empty
			// before HiddenSingle was called, so any Hidden Single found
			// and inserted into the list will be at the front.

			mySudoku.NakedSingle();
		}

	} // examineRCB()

} // HiddenSingle