public class SudokuSolveHumanMethods
{
	// Sudoku which everything is based upon

	FullSudoku mySudoku;
	SolveChecker mySolveChecker;


	// Create objects containing solve methods

	PointingPairsTriples TrickSolveOne;
	ClaimingPairsTriples TrickSolveTwo;
	NakedPairsTripsQuads TrickSolveThree;
	HiddenPairsTripsQuads TrickSolveFour;
	FishTwoThreeFour TrickSolveFive;
	XY_Wing TrickSolveSix;


	// Called by DrawNumsConstructor

	public SudokuSolveHumanMethods(FullSudoku a, SolveChecker b)
	{
		mySudoku = a;
		mySolveChecker = b;

		TrickSolveOne = new PointingPairsTriples();
		TrickSolveTwo = new ClaimingPairsTriples();
		TrickSolveThree = new NakedPairsTripsQuads();
		TrickSolveFour = new HiddenPairsTripsQuads();
		TrickSolveFive = new FishTwoThreeFour();
		TrickSolveSix = new XY_Wing();


		// Amount of squares solved at the beginning of a loop through these
		// tricks, for comparison against the sudoku's own squaresSolved

		int thisBeSquaresSolved;


		// Runs through the loop since the sudoku's squaresSolved has changed

		int theIncrementer = 0;


		// Solve

		do
		{
			// Make note of the current number of squares solved

			thisBeSquaresSolved = mySudoku.squaresSolved;


			// Solve as many squares as possible using Naked & Hidden Singles

			mySudoku.continuousIntenseSolve();



			// Utilize solving tricks in general order of increasing complexity.
			// Each trick is applied only if all previous ones did not increment
			// squaresSolved during that loop, which will be determined with a
			// comparison against thisBeSquaresSolved

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveOne.PointingPairsTriples(mySudoku);
			}

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveTwo.ClaimingPairsTriples(mySudoku);
			}

			// ~~~~~~~~~~

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveThree.NakedPairsRCB(mySudoku);
			}

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveThree.NakedTriplesRCB(mySudoku);
			}

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveThree.NakedQuadsRCB(mySudoku);
			}

			// ~~~~~~~~~~

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveFour.HiddenPairsRCB(mySudoku);
			}

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveFour.HiddenTriplesRCB(mySudoku);
			}

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveFour.HiddenQuadsRCB(mySudoku);
			}

			// ~~~~~~~~~~

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveFive.TwoFish_XWing(mySudoku);
			}

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveFive.ThreeFish_Swordfish(mySudoku);
			}

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveFive.FourFish_Jellyfish(mySudoku);
			}

			// ~~~~~~~~~~

			if(thisBeSquaresSolved == mySudoku.squaresSolved)
			{
				TrickSolveSix.XY_Wing_Begin(mySudoku);
			}

			// ~~~~~~~~~~


			// If squaresSolved has changed from the start of the loop,
			// reset the incrementer to 0. Otherwise, increment it.

			// Solving function will terminate either when all 81 squares
			// have been solved or when the loop has been executed with
			// no changes to squaresSolved four consecutive times

			if(thisBeSquaresSolved != mySudoku.squaresSolved)
				theIncrementer = 0;
			else
				theIncrementer++;

		}
		while(theIncrementer < 4 && mySudoku.squaresSolved != 81);

	} // Constructor

} // SudokuSolveHumanMethods