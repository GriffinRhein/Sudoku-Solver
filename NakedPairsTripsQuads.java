public class NakedPairsTripsQuads
{
	// Functions called by SudokuSolve

	public void NakedPairsRCB(FullSudoku mySudoku)
	{
		NakedSubsetsRCB(mySudoku,2);

	} // NakedPairsRCB()

	public void NakedTriplesRCB(FullSudoku mySudoku)
	{
		NakedSubsetsRCB(mySudoku,3);

	} // NakedTriplesRCB()

	public void NakedQuadsRCB(FullSudoku mySudoku)
	{
		NakedSubsetsRCB(mySudoku,4);

	} // NakedQuadsRCB()


	// Called by the above functions, this one gives the
	// appropriate parameters to our big procedure

	private void NakedSubsetsRCB(FullSudoku mySudoku, int x)
	{
		for(int i=0;i<9;i++)
		{
			oneSubsetTypeOneRCB(mySudoku,mySudoku.provideRow(i),x);
		}

		for(int j=0;j<9;j++)
		{
			oneSubsetTypeOneRCB(mySudoku,mySudoku.provideCol(j),x);
		}

		for(int k=0;k<9;k++)
		{
			oneSubsetTypeOneRCB(mySudoku,mySudoku.provideBox(k),x);
		}

	} // NakedSubsetsRCB


	// Quick function just to see whether a number falls within
	// a range given by two others. Allows for shorthand later.

	private Boolean inBetween(int testing,int a,int b)
	{
		if(testing >= a && testing <= b)
			return true;

		return false;
	}


	// Handy for checking whether the squares are a Naked Subset.
	// Uses leftInPossArray to record the numbers left in a single possArray,
	// and increments totalLeft for each number not already in leftInPossArray.

	private Integer[] leftInPossArray = new Integer[9];
	private int totalLeft;

	private void checkingPossArray(Integer[] theArray)
	{
		for(int x=0;x<9;x++)
		{
			if(theArray[x] != null)
			{
				if(leftInPossArray[x] == null)
				{
					leftInPossArray[x] = theArray[x];
					totalLeft++;
				}
			}
		}
	}


	// Big Kahuna

	private void oneSubsetTypeOneRCB(FullSudoku mySudoku, Square[] singleRCB, int nakedType)
	{
		Boolean canWePass;

		for(int a=0;a<9;a++)
		{
			for(int b=0;b<9;b++)
			{
				for(int c=0;c<9;c++)
				{
					for(int d=0;d<9;d++)
					{
						// Squares are represented by these integers from 0 through 8

						// First, we make sure that the squares are all different, and
						// that we are not going through any scenario more than once.

						canWePass = false;

						switch(nakedType)
						{
							case 2: if(a < b && c == 0 && d == 0) canWePass = true; break;
							case 3: if(a < b && b < c && d == 0) canWePass = true; break;
							case 4: if(a < b && b < c && c < d) canWePass = true; break;
						}

						if(canWePass)
						{
							// Next, check that each square relevant to our scenario
							// has an appropriate number of possibilities left.

							int w = singleRCB[a].numPossLeft;
							int x = singleRCB[b].numPossLeft;
							int y = singleRCB[c].numPossLeft;
							int z = singleRCB[d].numPossLeft;

							canWePass = false;

							switch(nakedType)
							{
								case 2: if(inBetween(w,2,2) && inBetween(x,2,2)) canWePass = true; break;
								case 3: if(inBetween(w,2,3) && inBetween(x,2,3) && inBetween(y,2,3)) canWePass = true; break;
								case 4: if(inBetween(w,2,4) && inBetween(x,2,4) && inBetween(y,2,4) && inBetween(z,2,4)) canWePass = true; break;
							}

							if(canWePass)
							{
								// Make sure we have a Naked Subset


								// Reset leftInPossArray and totalLeft

								for(int s=0;s<9;s++)
									leftInPossArray[s] = null;

								totalLeft = 0;


								// Send each row/column/box to checkingPossArray

								checkingPossArray(singleRCB[a].possArray);
								checkingPossArray(singleRCB[b].possArray);
								if(nakedType >= 3) checkingPossArray(singleRCB[c].possArray);
								if(nakedType >= 4) checkingPossArray(singleRCB[d].possArray);


								canWePass = false;

								switch(nakedType)
								{
									case 2: if(totalLeft <= 2) canWePass = true; break;
									case 3: if(totalLeft <= 3) canWePass = true; break;
									case 4: if(totalLeft <= 4) canWePass = true; break;
								}

								if(canWePass)
								{
									// Onward to elimination

									Integer numWeTakeOut;

									for(int f=0;f<9;f++)
									{
										numWeTakeOut = leftInPossArray[f];

										if(numWeTakeOut != null)
										{
											for(int g=0;g<9;g++)
											{
												canWePass = false;

												switch(nakedType)
												{
													case 2: if(g != a && g != b) canWePass = true; break;
													case 3: if(g != a && g != b && g != c) canWePass = true; break;
													case 4: if(g != a && g != b && g != c && g != d) canWePass = true; break;
												}

												if(canWePass)
												{
													mySudoku.elimFromPossArray(singleRCB[g],numWeTakeOut);
												}
											}
										}
									}

									// If we have accomplished something, activate continuousIntenseSolve()

									mySudoku.continuousIntenseSolve();
								}
							}
						}

					} // What is done for each combination of squares
				}
			}
		}

	} // oneSubsetTypeOneRCB()

} // NakedPairsTripsQuads