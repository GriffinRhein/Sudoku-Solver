public class SudokuSolve
{
	FullSudoku mySudoku;

	public SudokuSolve(String[][] thisInput)
	{
		mySudoku = new FullSudoku(thisInput);


		PointingPairsTriples TrickSolveOne = new PointingPairsTriples();
		NakedPairsTripsQuads TrickSolveTwo = new NakedPairsTripsQuads();
		HiddenPairsTripsQuads TrickSolveThree = new HiddenPairsTripsQuads();
		FishTwoThreeFour TrickSolveFour = new FishTwoThreeFour();


		int thisBeSquaresSolved;
		int theIncrementer = 0;

		do
		{
			thisBeSquaresSolved = mySudoku.squaresSolved;

			mySudoku.continuousIntenseSolve();


			// Trick solves beyond "Only number that can go in square" and
			// "Only square which can contain number R/C/B needs"


			// ~~~~~~~~~~

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveOne.PointingPairsTriples(mySudoku);
			}

			// In addition to Pointing Pairs, I would also like to do Claiming Pairs

			// ~~~~~~~~~~

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveTwo.NakedPairsRCB(mySudoku);
			}

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveTwo.NakedTriplesRCB(mySudoku);
			}

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveTwo.NakedQuadsRCB(mySudoku);
			}

			// ~~~~~~~~~~

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveThree.HiddenPairsRCB(mySudoku);
			}

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveThree.HiddenTriplesRCB(mySudoku);
			}

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveThree.HiddenQuadsRCB(mySudoku);
			}

			// ~~~~~~~~~~

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveFour.TwoFish_XWing(mySudoku);
			}

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveFour.ThreeFish_Swordfish(mySudoku);
			}

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveFour.FourFish_Jellyfish(mySudoku);
			}

			// ~~~~~~~~~~

			if(thisBeSquaresSolved != mySudoku.squaresSolved)
				theIncrementer = 0;
			else
				theIncrementer++;

		}
		while(theIncrementer < 4 && mySudoku.squaresSolved != 81);

	} // Constructor

} // SudokuSolve