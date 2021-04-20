public class FishTwoThreeFour
{
	// Functions called by SudokuSolve. Gives the appropriate parameters to the big procedure,
	// and calls it for every combination of rows and combination of columns, for every number.

	public void TwoFish_XWing(FullSudoku mySudoku)
	{
		Square[] dudRCB = new Square[9];

		for(int a=0;a<9;a++)
		{
			for(int b=0;b<9;b++)
			{
				if(a < b)
				{
					for(Integer e=1;e<10;e++)
					{
						singleSimpleFish(mySudoku,mySudoku.provideRow(a),mySudoku.provideRow(b),dudRCB,dudRCB,2,true,e);
						singleSimpleFish(mySudoku,mySudoku.provideCol(a),mySudoku.provideCol(b),dudRCB,dudRCB,2,false,e);
					}
				}
			}
		}
	}

	public void ThreeFish_Swordfish(FullSudoku mySudoku)
	{
		Square[] dudRCB = new Square[9];

		for(int a=0;a<9;a++)
		{
			for(int b=0;b<9;b++)
			{
				for(int c=0;c<9;c++)
				{
					if(a < b && b < c)
					{
						for(Integer e=1;e<10;e++)
						{
							singleSimpleFish(mySudoku,mySudoku.provideRow(a),mySudoku.provideRow(b),mySudoku.provideRow(c),dudRCB,3,true,e);
							singleSimpleFish(mySudoku,mySudoku.provideCol(a),mySudoku.provideCol(b),mySudoku.provideCol(c),dudRCB,3,false,e);
						}
					}
				}
			}
		}
	}

	public void FourFish_Jellyfish(FullSudoku mySudoku)
	{
		for(int a=0;a<9;a++)
		{
			for(int b=0;b<9;b++)
			{
				for(int c=0;c<9;c++)
				{
					for(int d=0;d<9;d++)
					{
						if(a < b && b < c && c < d)
						{
							for(Integer e=1;e<10;e++)
							{
								singleSimpleFish(mySudoku,mySudoku.provideRow(a),mySudoku.provideRow(b),mySudoku.provideRow(c),mySudoku.provideRow(d),4,true,e);
								singleSimpleFish(mySudoku,mySudoku.provideCol(a),mySudoku.provideCol(b),mySudoku.provideCol(c),mySudoku.provideCol(d),4,false,e);
							}
						}
					}
				}
			}
		}
	}


	// The specific base sets we work with

	private Square[] firstBaseSet;
	private Square[] secondBaseSet;
	private Square[] thirdBaseSet;
	private Square[] fourthBaseSet;


	// Quick function just to see whether a number falls within
	// a range given by two others. Allows for shorthand later.

	private Boolean inBetween(int testing,int a,int b)
	{
		if(testing >= a && testing <= b)
			return true;

		return false;
	}


	// Used to record the specific squares across all base sets
	// which can still contain the number.

	// We start with fullArray representing nine squares, and
	// knock off squares as we see that the number is still in
	// the possArray for that square in ANY of the 2-4 base sets,
	// incrementing fullCounter each time a square is axed.

	// When we're done checking all base sets, fullCounter will
	// be the total amount of cover sets needed to cover every
	// square in the base sets with the number in a possArray.

	private int[] fullArray = new int[]{0,1,2,3,4,5,6,7,8};
	private int fullCounter = 0;

	private void goodCoverSets(int theFishType, int checkedNum)
	{
		// Reset fullArray

		for(int i=0;i<9;i++)
			fullArray[i] = i;

		fullCounter = 0;


		// Handy array to hold base sets

		Square[][] theBaseSets = new Square[][]{firstBaseSet,secondBaseSet,thirdBaseSet,fourthBaseSet};


		// Get to work. Every square from 0-8 which can hold the number
		// will be marked on fullArray, with fullCounter incremented

		Boolean canThisWork = false;

		for(int a=0;a<9;a++)
		{
			for(int b=0;b<4;b++)
			{
				canThisWork = false;

				switch(theFishType)
				{
					case 2: if(b < 2) canThisWork = true; break;
					case 3: if(b < 3) canThisWork = true; break;
					case 4: if(b < 4) canThisWork = true; break;
				}

				if(canThisWork)
				{
					if(theBaseSets[b][a].result == null)
					{
						if(theBaseSets[b][a].possArray[checkedNum-1] != null)
						{
							if(fullArray[a] != -20)
							{
								fullArray[a] = -20;
								fullCounter++;
							}
						}
					}
				}
			}
		}
	}


	private void singleSimpleFish(FullSudoku mySudoku, Square[] RC1, Square[] RC2, Square[] RC3, Square[] RC4, int fishType, Boolean inputRows, Integer numToCheck)
	{
		// Set base sets

		firstBaseSet = RC1;
		secondBaseSet = RC2;
		if(fishType >= 3) thirdBaseSet = RC3;
		if(fishType >= 4) fourthBaseSet = RC4;


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


		// Make note of which rows/columns we have.
		// 21 is just junk for initialization.

		int firstRC = 21;
		int secondRC = 21;
		int thirdRC = 21;
		int fourthRC = 21;

		if(inputRows)
		{
			firstRC = firstBaseSet[0].ownRow;
			secondRC = secondBaseSet[0].ownRow;
			if(fishType >= 3) thirdRC = thirdBaseSet[0].ownRow;
			if(fishType >= 4) fourthRC = fourthBaseSet[0].ownRow;
		}
		else
		{
			firstRC = firstBaseSet[0].ownCol;
			secondRC = secondBaseSet[0].ownCol;
			if(fishType >= 3) thirdRC = thirdBaseSet[0].ownCol;
			if(fishType >= 4) fourthRC = fourthBaseSet[0].ownCol;
		}


		// Take the prevalence of the possibility in the 2-4 rows/columns.
		// 22 is just junk for initialization.

		int firstPrev = 22;
		int secondPrev = 22;
		int thirdPrev = 22;
		int fourthPrev = 22;

		firstPrev = ourPrev[firstRC][numToCheck-1];
		secondPrev = ourPrev[secondRC][numToCheck-1];
		if(fishType >= 3) thirdPrev = ourPrev[thirdRC][numToCheck-1];
		if(fishType >= 4) fourthPrev = ourPrev[fourthRC][numToCheck-1];


		// Make sure that each base set contains the number in an
		// appropriate number of squares' possArrays.

		// PossPrevalence arrays are very convenient.

		Boolean canWePass = false;

		switch(fishType)
		{
			case 2: if(inBetween(firstPrev,2,2) && inBetween(secondPrev,2,2)) canWePass = true; break;
			case 3: if(inBetween(firstPrev,2,3) && inBetween(secondPrev,2,3) && inBetween(thirdPrev,2,3)) canWePass = true; break;
			case 4: if(inBetween(firstPrev,2,4) && inBetween(secondPrev,2,4) && inBetween(thirdPrev,2,4) && inBetween(fourthPrev,2,4)) canWePass = true; break;
		}

		if(canWePass)
		{
			// Make sure that all squares which can contain our number
			// fall within the appropriate number of cover sets

			goodCoverSets(fishType,numToCheck);

			canWePass = false;

			switch(fishType)
			{
				case 2: if(fullCounter <= 2) canWePass = true; break;
				case 3: if(fullCounter <= 3) canWePass = true; break;
				case 4: if(fullCounter <= 4) canWePass = true; break;
			}

			if(canWePass)
			{
				// Now, erase the number from all squares
				// in cover sets but not base sets

				Square[][] theCoverSets = new Square[fishType][9];

				int newCounter = 0;

				for(int z=0;z<9;z++)
				{
					if(fullArray[z] == -20)
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


				// Elimination time. Go through each square of the cover sets

				for(int r=0;r<9;r++)
				{
					// Make sure we are not in one of the 2-4 base sets

					canWePass = false;

					switch(fishType)
					{
						case 2: if(r != firstRC && r != secondRC) canWePass = true; break;
						case 3: if(r != firstRC && r != secondRC && r != thirdRC) canWePass = true; break;
						case 4: if(r != firstRC && r != secondRC && r != thirdRC && r != fourthRC) canWePass = true; break;
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

} // FishTwoThreeFour