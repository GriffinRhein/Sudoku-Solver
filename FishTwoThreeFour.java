public class FishTwoThreeFour
{
	// Specific base sets we work with

	private Square[] firstBaseSet;
	private Square[] secondBaseSet;
	private Square[] thirdBaseSet;
	private Square[] fourthBaseSet;


	// The following function is used for X-Wing, Swordfish, and Jellyfish
	// (Functions which call this one are at the bottom of this document)

	// Inputs are:

	// the Sudoku
	// int from 0 to 8 indicating the 1st row/column being looked at
	// int from 0 to 8 indicating the 2nd row/column being looked at
	// int from 0 to 8 indicating the 3rd row/column being looked at
	// int from 0 to 8 indicating the 4th row/column being looked at
	// int from 2 to 4 which represents the fish type
	// boolean which must be input as True if the sets input are rows and False if the sets input are columns
	// Integer for the actual number being tested

	// Worth nothing that for X-Wing, RC3 and RC4 are never looked at, and for Swordfish, RC4 is never looked at.

	private void singleSimpleFish(FullSudoku mySudoku, int RC1, int RC2, int RC3, int RC4, int fishType, Boolean inputRows, Integer numToCheck)
	{
		// Set base sets

		if(inputRows)
		{
			firstBaseSet = mySudoku.provideRow(RC1);
			secondBaseSet = mySudoku.provideRow(RC2);
			if(fishType >= 3) thirdBaseSet = mySudoku.provideRow(RC3);
			if(fishType >= 4) fourthBaseSet = mySudoku.provideRow(RC4);
		}
		else
		{
			firstBaseSet = mySudoku.provideCol(RC1);
			secondBaseSet = mySudoku.provideCol(RC2);
			if(fishType >= 3) thirdBaseSet = mySudoku.provideCol(RC3);
			if(fishType >= 4) fourthBaseSet = mySudoku.provideCol(RC4);
		}


		// Retrieve the appropriate PossPrevalence

		int[][] ourPrev;

		if(inputRows)
		{
			ourPrev = mySudoku.rowPossPrevalence;
		}
		else
		{
			ourPrev = mySudoku.colPossPrevalence;
		}


		// Take the prevalence of the possibility in the input rows/columns.

		// X-Wing requires exactly 2 squares in each of the two base sets to
		// have the Poss available, Swordfish requires 2-3 squares in each of
		// the three base sets to have it, and Jellyfish requires 2-4 squares
		// in each of the four base sets.

		// Function called inRange() checks if a number is in a range

		int prevRC1 = -33; int prevRC2 = -33; int prevRC3 = -33; int prevRC4 = -33;

		prevRC1 = ourPrev[RC1][numToCheck-1];
		prevRC2 = ourPrev[RC2][numToCheck-1];
		if(fishType >= 3) prevRC3 = ourPrev[RC3][numToCheck-1];
		if(fishType >= 4) prevRC4 = ourPrev[RC4][numToCheck-1];

		Boolean canWePass = false;

		switch(fishType)
		{
			case 2: if(inRange(prevRC1,2,2) && inRange(prevRC2,2,2)) canWePass = true; break;
			case 3: if(inRange(prevRC1,2,3) && inRange(prevRC2,2,3) && inRange(prevRC3,2,3)) canWePass = true; break;
			case 4: if(inRange(prevRC1,2,4) && inRange(prevRC2,2,4) && inRange(prevRC3,2,4) && inRange(prevRC4,2,4)) canWePass = true; break;
		}


		// ~~~ If no square possesses a possPrevalence which makes the sought-after fish impossible, then on to the next step ~~~


		if(canWePass)
		{
			// Function goodCoverSets(), further down, looks through the base
			// sets and determines whether the sought-after fish exists

			goodCoverSets(fishType,numToCheck);

			canWePass = false;

			switch(fishType)
			{
				case 2: if(fullCounter <= 2) canWePass = true; break;
				case 3: if(fullCounter <= 3) canWePass = true; break;
				case 4: if(fullCounter <= 4) canWePass = true; break;
			}


			// ~~~ If the fish exists, then on to the elimination step ~~~


			if(canWePass)
			{
				// To erase the number from all squares in cover sets
				// but not base sets, we will need the cover sets

				Square[][] theCoverSets = new Square[fishType][9];

				int newCounter = 0;

				for(int z=0;z<9;z++)
				{
					// Thanks to goodCoverSets(), the cover sets are those
					// whose associated spot in fullArray is marked true.  

					if(fullArray[z] == true)
					{
						if(inputRows)
						{
							theCoverSets[newCounter] = mySudoku.provideCol(z);
							newCounter++;
						}
						else
						{
							theCoverSets[newCounter] = mySudoku.provideRow(z);
							newCounter++;
						}
					}
				}


				// Elimination time. Go through each square of the cover sets, and
				// knock off the possibility from all squares not in the base sets

				for(int r=0;r<9;r++)
				{
					canWePass = false;

					switch(fishType)
					{
						case 2: if(r != RC1 && r != RC2) canWePass = true; break;
						case 3: if(r != RC1 && r != RC2 && r != RC3) canWePass = true; break;
						case 4: if(r != RC1 && r != RC2 && r != RC3 && r != RC4) canWePass = true; break;
					}

					if(canWePass)
					{
						mySudoku.elimFromPossArray(theCoverSets[0][r],numToCheck);
						mySudoku.elimFromPossArray(theCoverSets[1][r],numToCheck);
						if(fishType >= 3) mySudoku.elimFromPossArray(theCoverSets[2][r],numToCheck);
						if(fishType >= 4) mySudoku.elimFromPossArray(theCoverSets[3][r],numToCheck);
					}
				}

				// If we have accomplished something, activate continuousIntenseSolve()

				mySudoku.continuousIntenseSolve();
			}
		}

	} // singleSimpleFish()


	// Quick function just to see whether a number falls within
	// a range given by two others. Allows for shorthand.

	private Boolean inRange(int testing,int a,int b)
	{
		if(testing >= a && testing <= b)
			return true;

		return false;

	} // inRange()


	// goodCoverSets() is used to record the specific squares
	// across all base sets which can still contain the number.

	// We start with fullArray representing nine squares, and
	// knock off squares as we see that the number is still in
	// the possArray for that square in ANY of the 2-4 base sets,
	// incrementing fullCounter each time a square is axed.

	// When we're done checking all base sets, fullCounter will
	// be the total amount of cover sets needed to cover every
	// square in base sets which has the number in a possArray.

	private boolean[] fullArray = new boolean[9];
	private int fullCounter = 0;

	private void goodCoverSets(int theFishType, int checkedNum)
	{
		// Reset fullArray

		for(int i=0;i<9;i++)
			fullArray[i] = false;

		fullCounter = 0;


		// Handy array to hold base sets

		Square[][] theBaseSets = new Square[][]{firstBaseSet,secondBaseSet,thirdBaseSet,fourthBaseSet};

		// Handy boolean

		boolean isThisRelevant;


		// Get to work. Every square from 0-8 which can hold the number within any of
		// the base sets will be marked on fullArray, with fullCounter incremented

		// Nine squares within a base set

		for(int a=0;a<9;a++)
		{
			// Maximum of four base sets

			for(int b=0;b<4;b++)
			{
				isThisRelevant = false;

				switch(theFishType)
				{
					case 2: if(b < 2) isThisRelevant = true; break;
					case 3: if(b < 3) isThisRelevant = true; break;
					case 4: if(b < 4) isThisRelevant = true; break;
				}

				if(isThisRelevant)
				{
					// Make sure the square is not solved

					if(theBaseSets[b][a].result == null)
					{
						// Make sure the square can still contain the number

						if(theBaseSets[b][a].possArray[checkedNum-1] != null)
						{
							// Make sure that spot hasn't already been marked off here

							if(fullArray[a] == false)
							{
								fullArray[a] = true;
								fullCounter++;
							}
						}
					}
				}
			}
		}

	} // goodCoverSets()


	// ~~~ Public functions putting the ones above to use. ~~~


	public void TwoFish_XWing(FullSudoku mySudoku)
	{
		int zilch = -33;

		// Operation occurs for every possible
		// combination of 2 rows or 2 columns

		for(int a=0;a<9;a++)
		{
			for(int b=0;b<9;b++)
			{
				// Ensures that the operation occurs only
				// once per combination of rows or columns

				if(a < b)
				{
					// Every number is tested

					for(Integer e=1;e<10;e++)
					{
						singleSimpleFish(mySudoku,a,b,zilch,zilch,2,true,e);
						singleSimpleFish(mySudoku,a,b,zilch,zilch,2,false,e);
					}
				}
			}
		}
	}

	public void ThreeFish_Swordfish(FullSudoku mySudoku)
	{
		int zilch = -33;

		// Operation occurs for every possible
		// combination of 3 rows or 3 columns

		for(int a=0;a<9;a++)
		{
			for(int b=0;b<9;b++)
			{
				for(int c=0;c<9;c++)
				{
					// Ensures that the operation occurs only
					// once per combination of rows or columns

					if(a < b && b < c)
					{
						// Every number is tested

						for(Integer e=1;e<10;e++)
						{
							singleSimpleFish(mySudoku,a,b,c,zilch,3,true,e);
							singleSimpleFish(mySudoku,a,b,c,zilch,3,false,e);
						}
					}
				}
			}
		}
	}

	public void FourFish_Jellyfish(FullSudoku mySudoku)
	{
		// Operation occurs for every possible
		// combination of 4 rows or 4 columns

		for(int a=0;a<9;a++)
		{
			for(int b=0;b<9;b++)
			{
				for(int c=0;c<9;c++)
				{
					for(int d=0;d<9;d++)
					{
						// Ensures that the operation occurs only
						// once per combination of rows or columns

						if(a < b && b < c && c < d)
						{
							// Every number is tested

							for(Integer e=1;e<10;e++)
							{
								singleSimpleFish(mySudoku,a,b,c,d,4,true,e);
								singleSimpleFish(mySudoku,a,b,c,d,4,false,e);
							}
						}
					}
				}
			}
		}
	}

} // FishTwoThreeFour