public class NakedPairsTripsQuads
{
	// Handy boolean

	boolean canWePass;

	// Array of 9 Squares which make up the row/column/box being looked at

	Square[] singleRCB;

	// int for the type of Naked Subset being sought. 2 for Pairs, 3 for Triples, 4 for Quads

	int nakedType;

	// int for Squares being examined

	int square1;
	int square2;
	int square3;
	int square4;


	// This function is used for Naked Pairs, Naked Triples, and Naked Quads.
	// (Functions which call this one are at the bottom of this document)

	private void oneSubsetTypeOneRCB(FullSudoku mySudoku, Square[] inputRCB, int whichNaked)
	{
		singleRCB = inputRCB;
		nakedType = whichNaked;

		// For a Naked Pair, all combinations of two different squares from 0 through 8 must be checked
		// For a Naked Triple, all combinations of three different squares from 0 through 8 must be checked
		// For a Naked Quad, all combinations of four different squares from 0 through 8 must be checked

		// This is put into motion with four loops, with a,b,c,d becoming square1,square2,square3,square4, the examined squares

		// For a Naked Pair, duplicate combinations are avoided by making sure a<b,c=0,d=0 (c & d aren't used past this point)
		// For a Naked Triple, duplicate combinations are avoided by making sure a<b,b<c,d=0 (d isn't used past this point)
		// for a Naked Quad, duplicate combinations are avoided by making sure a<b,b<c,c<d

		for(int a=0;a<9;a++)
		{
			square1 = a;

			for(int b=0;b<9;b++)
			{
				square2 = b;

				for(int c=0;c<9;c++)
				{
					square3 = c;

					for(int d=0;d<9;d++)
					{
						square4 = d;

						// Here is where the ensure the unique combination
						// Success means we continue to the next function

						canWePass = false;

						switch(nakedType)
						{
							case 2: if(square1 < square2 && square3 == 0 && square4 == 0) canWePass = true; break;
							case 3: if(square1 < square2 && square2 < square3 && square4 == 0) canWePass = true; break;
							case 4: if(square1 < square2 && square2 < square3 && square3 < square4) canWePass = true; break;
						}

						if(canWePass)
						{
							checkNumPossLeft(mySudoku);
						}

					}
				}
			}
		}

	} // oneSubsetTypeOneRCB()


	private void checkNumPossLeft(FullSudoku mySudoku)
	{
		// For a Naked Pair, all squares must have a numPossLeft of 2
		// For a Naked Triple, all squares must have a numPossLeft of 2-3
		// For a Naked Quad, all squares must have a numPossLeft of 2-4

		int w = singleRCB[square1].numPossLeft;
		int x = singleRCB[square2].numPossLeft;
		int y = singleRCB[square3].numPossLeft;
		int z = singleRCB[square4].numPossLeft;

		canWePass = false;

		switch(nakedType)
		{
			case 2: if(inRange(w,2,2) && inRange(x,2,2)) canWePass = true; break;
			case 3: if(inRange(w,2,3) && inRange(x,2,3) && inRange(y,2,3)) canWePass = true; break;
			case 4: if(inRange(w,2,4) && inRange(x,2,4) && inRange(y,2,4) && inRange(z,2,4)) canWePass = true; break;
		}

		if(canWePass)
		{
			checkForNakedSubset(mySudoku);
		}

	} // checkNumPossLeft()


	private void checkForNakedSubset(FullSudoku mySudoku)
	{
		// Time to see whether we actually have a Naked Subset

		// Each square will be sent to checkingPossArray so that its
		// possArray can be examined. If the square can contain a number,
		// that number will be marked.

		// For a Naked Pair, the total amount of marked numbers must be 2
		// For a Naked Triple, the total amount of marked numbers must be 3
		// For a Naked Quad, the total amount of marked numbers must be 4
		// If the amount marked is greater, then there is no Naked Subset


		// Start by resetting leftInPossArray and totalLeft

		for(int s=0;s<9;s++)
			leftInPossArray[s] = false;

		totalLeft = 0;


		// Send each square to checkingPossArray

		checkingPossArray(singleRCB[square1].possArray);
		checkingPossArray(singleRCB[square2].possArray);
		if(nakedType >= 3) checkingPossArray(singleRCB[square3].possArray);
		if(nakedType >= 4) checkingPossArray(singleRCB[square4].possArray);


		// Do you have it?

		canWePass = false;

		switch(nakedType)
		{
			case 2: if(totalLeft <= 2) canWePass = true; break;
			case 3: if(totalLeft <= 3) canWePass = true; break;
			case 4: if(totalLeft <= 4) canWePass = true; break;
		}

		if(canWePass)
		{
			elimTime(mySudoku);
		}

	} // checkForNakedSubset()


	private void elimTime(FullSudoku mySudoku)
	{
		// Elimination time. The two, three, or four numbers forming the Naked Subset are
		// booted from every square in the row/column/box except for the ones we tested.

		// Go through every number from 1 through 9

		for(int f=1;f<10;f++)
		{
			// Proceed only if that number is part of the Naked Subset

			if(leftInPossArray[f-1] == true)
			{
				// Go through every square in the row/column/box

				for(int g=0;g<9;g++)
				{
					// If the square is not one of the tested squares,
					// the square loses the number from its possArray

					canWePass = false;

					switch(nakedType)
					{
						case 2: if(g != square1 && g != square2) canWePass = true; break;
						case 3: if(g != square1 && g != square2 && g != square3) canWePass = true; break;
						case 4: if(g != square1 && g != square2 && g != square3 && g != square4) canWePass = true; break;
					}

					if(canWePass)
					{
						mySudoku.elimFromPossArray(singleRCB[g],f);
					}
				}
			}
		}

		// If we have accomplished something, activate continuousIntenseSolve()

		mySudoku.continuousIntenseSolve();

	} // elimTime()


	// Quick function just to see whether a number falls within
	// a range given by two others. Allows for shorthand.

	private Boolean inRange(int testing,int a,int b)
	{
		if(testing >= a && testing <= b)
			return true;

		return false;

	} // inRange()


	// Handy for checking whether the squares are a Naked Subset.
	// Uses leftInPossArray to record the numbers left in a single possArray,
	// and increments totalLeft for each number not already in leftInPossArray.

	// Like with the actual possArray, leftInPossArray[0] represents 1, etc.

	private boolean[] leftInPossArray = new boolean[9];
	private int totalLeft;

	private void checkingPossArray(Integer[] theArray)
	{
		for(int x=0;x<9;x++)
		{
			if(theArray[x] != null)
			{
				if(leftInPossArray[x] == false)
				{
					leftInPossArray[x] = true;
					totalLeft++;
				}
			}
		}

	} // checkingPossArray()


	// ~~~ Everything else deals with what is inserted into all of this ~~~


	// Provides appropriate parameters to oneSubsetTypeOneRCB()

	private void NakedSubsetsRCB(FullSudoku mySudoku, int x)
	{
		// Every row/column/box is inserted along with an int
		// noting the type of Hidden Subset to be looked at.

		// And the sudoku is inserted as well, of course.

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


	// Public functions putting everything to use. Calls NakedSubsetsRCB()
	// with a 2 for Pairs, 3 for Triples, 4 for Quads

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


} // NakedPairsTripsQuads