public class HiddenPairsTripsQuads
{
	// Handy boolean

	boolean canWePass;

	// Array of 9 Squares which make up the row/column/box being looked at

	Square[] singleRCB;

	// int from 0 to 8 indicating the row/column/box being looked at

	int whichSet;

	// int telling you whether the set in question is a row, column, or box. 85 for row, 86 for column, 87 for box

	int setType;

	// int for the type of Hidden Subset being sought. 2 for Pairs, 3 for Triples, 4 for Quads

	int hiddenType;

	// Numbers being examined

	int num1;
	int num2;
	int num3;
	int num4;

	// Relevant possPrevalence array. Initialization does not matter.

	int[][] thisPossPrevalence = new int[1][1];


	// This function is used for Hidden Pairs, Hidden Triples, and Hidden Quads.
	// (Functions which call this one are at the bottom of this document)

	private void oneSubsetTypeOneRCB(FullSudoku mySudoku, Square[] examinedRCB, int setInt, int rowOrColOrBox, int whichHidden)
	{
		singleRCB = examinedRCB;
		whichSet = setInt;
		setType = rowOrColOrBox;
		hiddenType = whichHidden;
		
		// For a Hidden Pair, all combinations of two different numbers from 1 through 9 must be checked
		// For a Hidden Triple, all combinations of three different numbers from 1 through 9 must be checked
		// For a Hidden Quad, all combinations of four different numbers from 1 through 9 must be checked

		// This is put into motion with four loops, with a,b,c,d becoming num1,num2,num3,num4, the examined numbers

		// For a Hidden Pair, duplicate combinations are avoided by making sure a<b,c=1,d=1 (c & d aren't used past this point)
		// For a Hidden Triple, duplicate combinations are avoided by making sure a<b,b<c,d=1 (d isn't used past this point)
		// for a Hidden Quad, duplicate combinations are avoided by making sure a<b,b<c,c<d

		for(int a=1;a<10;a++)
		{
			num1 = a;

			for(int b=1;b<10;b++)
			{
				num2 = b;

				for(int c=1;c<10;c++)
				{
					num3 = c;

					for(int d=1;d<10;d++)
					{
						num4 = d;

						// Here is where the ensure the unique combination
						// Success means we continue to the next function

						canWePass = false;

						switch(hiddenType)
						{
							case 2: if(num1 < num2 && num3 == 1 && num4 == 1) canWePass = true; break;
							case 3: if(num1 < num2 && num2 < num3 && num4 == 1) canWePass = true; break;
							case 4: if(num1 < num2 && num2 < num3 && num3 < num4) canWePass = true; break;
						}

						if(canWePass)
						{
							checkPossPrevalence(mySudoku);
						}
					}
				}
			}
		}

	} // oneSubsetTypeOneRCB()


	private void checkPossPrevalence(FullSudoku mySudoku)
	{
		// Recall that possPrevalence of a number refers to the amount of squares
		// in a row/column/box which still contains the number in its possArray

		// For a Hidden Pair, all numbers must have a possPrevalence of 2
		// For a Hidden Triple, all numbers must have a possPrevalence of 2-3
		// For a Hidden Quad, all numbers must have a possPrevalence of 2-4

		// Set thisPossPrevalence to the appropriate possPrevalence array,
		// and for the current row/column/box retrieve the prevalance of
		// each of the numbers. Continue if the relevant ones are acceptable.
			
		switch(setType)
		{
			case 85: thisPossPrevalence = mySudoku.rowPossPrevalence; break;
			case 86: thisPossPrevalence = mySudoku.colPossPrevalence; break;
			case 87: thisPossPrevalence = mySudoku.boxPossPrevalence; break;
		}

		int w = thisPossPrevalence[whichSet][num1 - 1];
		int x = thisPossPrevalence[whichSet][num2 - 1];
		int y = thisPossPrevalence[whichSet][num3 - 1];
		int z = thisPossPrevalence[whichSet][num4 - 1];

		canWePass = false;

		switch(hiddenType)
		{
			case 2: if(inRange(w,2,2) && inRange(x,2,2)) canWePass = true; break;
			case 3: if(inRange(w,2,3) && inRange(x,2,3) && inRange(y,2,3)) canWePass = true; break;
			case 4: if(inRange(w,2,4) && inRange(x,2,4) && inRange(y,2,4) && inRange(z,2,4)) canWePass = true; break;
		}

		if(canWePass)
		{
			checkForHiddenSubset(mySudoku);
		}

	} // checkPossPrevalence()


	private void checkForHiddenSubset(FullSudoku mySudoku)
	{
		// Time to see whether we actually have a Hidden Subset

		// Each number will be sent to totalChecker with each square
		// in the row/column/box, and if a square can contain one of
		// the numbers, it will be marked.

		// For a Hidden Pair, the total amount of marked squares must be 2
		// For a Hidden Triple, the total amount of marked squares must be 3
		// For a Hidden Quad, the total amount of marked squares must be 4
		// If the amount marked is greater, then there is no Hidden Subset


		// Start by resetting totalSquaresIn & totalCounter

		for(int f=0;f<9;f++)
			totalSquaresIn[f] = false;

		totalCounter = 0;


		// Send each number to totalChecker with each square in the set. f represents each square.
		// For f=0, totalChecker checks singleRCB[0], et cetera.

		for(int f=0;f<9;f++)
		{
			totalChecker(num1,f);
			totalChecker(num2,f);
			if(hiddenType >= 3) totalChecker(num3,f);
			if(hiddenType >= 4) totalChecker(num4,f);
		}


		// Do you have it?

		canWePass = false;

		switch(hiddenType)
		{
			case 2: if(totalCounter <= 2) canWePass = true; break;
			case 3: if(totalCounter <= 3) canWePass = true; break;
			case 4: if(totalCounter <= 4) canWePass = true; break;
		}

		if(canWePass)
		{
			itIsElimination(mySudoku);
		}

	} // checkForHiddenSubset()


	private void itIsElimination(FullSudoku mySudoku)
	{
		// Elimination time. The two, three, or four squares we tested in the
		// row/column/box lose every number except for the ones we tested.

		for(int g=0;g<9;g++)
		{
			if(totalSquaresIn[g] == true)
			{
				for(int h=1;h<10;h++)
				{
					switch(hiddenType)
					{
						case 2: if(h != num1 && h != num2) mySudoku.elimFromPossArray(singleRCB[g],h); break;
						case 3: if(h != num1 && h != num2 && h != num3) mySudoku.elimFromPossArray(singleRCB[g],h); break;
						case 4: if(h != num1 && h != num2 && h != num3 && h != num4) mySudoku.elimFromPossArray(singleRCB[g],h); break;
					}
				}
			}
		}

		// If we have accomplished something, activate continuousIntenseSolve()

		mySudoku.continuousIntenseSolve();

	} // itIsElimination()


	// Quick function just to see whether a number falls within
	// a range given by two others. Allows for shorthand.

	private Boolean inRange(int testing,int a,int b)
	{
		if(testing >= a && testing <= b)
			return true;

		return false;

	} // inRange()


	// Used for checking whether the numbers form a Hidden Subset.
	// totalSquaresIn records squares where a number is in a possArray,
	// and increments totalCounter for each new square recorded.

	// No loops here; the loop is in the big function this time.

	private boolean[] totalSquaresIn = new boolean[9];
	private int totalCounter = 0;

	private void totalChecker(int num, int where)
	{
		// Make sure the square is not solved

		if(singleRCB[where].result == null)
		{
			// Make sure the square can still contain the number

			if(singleRCB[where].possArray[num - 1] != null)
			{
				// Make sure that spot hasn't already been marked off here

				if(totalSquaresIn[where] == false)
				{
					totalSquaresIn[where] = true;
					totalCounter++;
				}
			}
		}

	} // totalChecker()


	// ~~~ Everything else deals with what is inserted into all of this ~~~


	// Provides appropriate parameters to oneSubsetTypeOneRCB()

	private void HiddenSubsetsRCB(FullSudoku mySudoku, int x)
	{
		// Every row/column/box is inserted along with an int noting which
		// row/column/box it is, an int noting whether it is a row or column
		// or box, and an int noting the type of Hidden Subset to be looked at.

		// And the sudoku is inserted as well, of course.

		for(int i=0;i<9;i++)
		{
			oneSubsetTypeOneRCB(mySudoku,mySudoku.provideRow(i),i,85,x);
		}

		for(int j=0;j<9;j++)
		{
			oneSubsetTypeOneRCB(mySudoku,mySudoku.provideCol(j),j,86,x);
		}

		for(int k=0;k<9;k++)
		{
			oneSubsetTypeOneRCB(mySudoku,mySudoku.provideBox(k),k,87,x);
		}

	} // HiddenSubsetsRCB


	// Public functions putting everything to use. Calls HiddenSubsetsRCB()
	// with a 2 for Pairs, 3 for Triples, 4 for Quads

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


} // HiddenPairsTripsQuads