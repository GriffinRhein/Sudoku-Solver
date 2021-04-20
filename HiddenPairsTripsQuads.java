public class HiddenPairsTripsQuads
{
	// Functions called by SudokuSolve

	public void HiddenPairsRCB(FullSudoku mySudoku)
	{
		HiddenSubsetsRCB(mySudoku,2);

	} // HiddenPairsRCB()

	public void HiddenTriplesRCB(FullSudoku mySudoku)
	{
		HiddenSubsetsRCB(mySudoku,3);

	} // HiddenTriplesRCB()

	public void HiddenQuadsRCB(FullSudoku mySudoku)
	{
		HiddenSubsetsRCB(mySudoku,4);

	} // HiddenQuadsRCB()


	// Called by the above functions, this one gives the
	// appropriate parameters to our big procedure

	private void HiddenSubsetsRCB(FullSudoku mySudoku, int x)
	{
		for(int i=0;i<9;i++)
		{
			oneSubsetTypeOneRCB(mySudoku,mySudoku.provideRow(i),x,85,i);
		}

		for(int j=0;j<9;j++)
		{
			oneSubsetTypeOneRCB(mySudoku,mySudoku.provideCol(j),x,86,j);
		}

		for(int k=0;k<9;k++)
		{
			oneSubsetTypeOneRCB(mySudoku,mySudoku.provideBox(k),x,87,k);
		}

	} // HiddenSubsetsRCB


	// Quick function just to see whether a number falls within
	// a range given by two others. Allows for shorthand later.

	private Boolean inBetween(int testing,int a,int b)
	{
		if(testing >= a && testing <= b)
			return true;

		return false;
	}


	// Handy for checking whether the numbers are a Hidden Subset.
	// Uses totalSquaresIn to record squares where a number is in a possArray,
	// and increments totalCounter for each square not already in totalSquaresIn.

	// Squares are represented by integers from 0 through 8.
	// No loops here; the loop is in the big function this time.

	private int[] totalSquaresIn = new int[9];
	private int totalCounter = 0;

	private void totalChecker(Square[] theRCB, int where, int num)
	{
		if(theRCB[where].result == null)
		{
			if(theRCB[where].possArray[num - 1] != null)
			{
				if(totalSquaresIn[where] == -20)
				{
					totalSquaresIn[where] = where;
					totalCounter++;
				}
			}
		}
	}


	private void oneSubsetTypeOneRCB(FullSudoku mySudoku, Square[] singleRCB, int hiddenType, int setType, int whichSet)
	{
		Boolean canWePass;

		for(int a=1;a<10;a++)
		{
			for(int b=1;b<10;b++)
			{
				for(int c=1;c<10;c++)
				{
					for(int d=1;d<10;d++)
					{
						// First, we make sure that the numbers are all different, and
						// that we are not going through any scenario more than once.

						canWePass = false;

						switch(hiddenType)
						{
							case 2: if(a < b && c == 1 && d == 1) canWePass = true; break;
							case 3: if(a < b && b < c && d == 1) canWePass = true; break;
							case 4: if(a < b && b < c && c < d) canWePass = true; break;
						}

						if(canWePass)
						{
							// Next, check that each number appears in an appropriate
							// number of possArrays throughout the RCB.

							// PossPrevalence arrays make this quick

							int[][] thisPossPrevalence = new int[4][9];

							switch(setType)
							{
								case 85: thisPossPrevalence = mySudoku.rowPossPrevalence; break;
								case 86: thisPossPrevalence = mySudoku.colPossPrevalence; break;
								case 87: thisPossPrevalence = mySudoku.boxPossPrevalence; break;
							}

							int w = thisPossPrevalence[whichSet][a - 1];
							int x = thisPossPrevalence[whichSet][b - 1];
							int y = thisPossPrevalence[whichSet][c - 1];
							int z = thisPossPrevalence[whichSet][d - 1];

							canWePass = false;

							switch(hiddenType)
							{
								case 2: if(inBetween(w,2,2) && inBetween(x,2,2)) canWePass = true; break;
								case 3: if(inBetween(w,2,3) && inBetween(x,2,3) && inBetween(y,2,3)) canWePass = true; break;
								case 4: if(inBetween(w,2,4) && inBetween(x,2,4) && inBetween(y,2,4) && inBetween(z,2,4)) canWePass = true; break;
							}

							if(canWePass)
							{
								// Make sure we have a Hidden Subset


								// Reset totalSquaresIn and totalCounter.
								// -20 is used to mean that it's empty.

								for(int f=0;f<9;f++)
									totalSquaresIn[f] = -20;

								totalCounter = 0;


								// Send each number to totalChecker with
								// every square in each row/column/box

								for(int f=0;f<9;f++)
								{
									totalChecker(singleRCB,f,a);
									totalChecker(singleRCB,f,b);
									if(hiddenType >= 3) totalChecker(singleRCB,f,c);
									if(hiddenType >= 4) totalChecker(singleRCB,f,d);
								}


								canWePass = false;

								switch(hiddenType)
								{
									case 2: if(totalCounter <= 2) canWePass = true; break;
									case 3: if(totalCounter <= 3) canWePass = true; break;
									case 4: if(totalCounter <= 4) canWePass = true; break;
								}

								if(canWePass)
								{
									// Elimination time.

									for(int g=0;g<9;g++)
									{
										if(totalSquaresIn[g] != -20)
										{
											for(int h=1;h<10;h++)
											{
												switch(hiddenType)
												{
													case 2: if(h != a && h != b) mySudoku.elimFromPossArray(singleRCB[g],h); break;
													case 3: if(h != a && h != b && h != c) mySudoku.elimFromPossArray(singleRCB[g],h); break;
													case 4: if(h != a && h != b && h != c && h != d) mySudoku.elimFromPossArray(singleRCB[g],h); break;
												}
											}
										}
									}

									// If we have accomplished something, activate continuousIntenseSolve()

									mySudoku.continuousIntenseSolve();
								}
							}
						}

					} // What is done for each combination of numbers
				}
			}
		}

	} // oneSubsetTypeOneRCB()

} // HiddenPairsTripsQuads