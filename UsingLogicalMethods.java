interface ToCallMethod
{
	public boolean callMethod();
}

public class UsingLogicalMethods
{
	// Sudoku which everything is based upon

	FullSudoku mySudoku;


	// For printing out the messages detailing the solve

	MethodExplanations stringPrint;


	// Gain ability to call all solving techniques

	HiddenSingle trickHS;
	PointingPairsTriples trickPPT;
	ClaimingPairsTriples trickCPT;
	NakedPairsTripsQuads trickNPTQ;
	HiddenPairsTripsQuads trickHPTQ;
	FishTwoThreeFour trickFTTF;
	XY_Wing trickXYW;

	ToCallMethod callNakedSingle;
	ToCallMethod callHiddenSingle;
	ToCallMethod callPointingPairTriple;
	ToCallMethod callClaimingPairTriple;
	ToCallMethod callNakedPair;
	ToCallMethod callNakedTriple;
	ToCallMethod callNakedQuad;
	ToCallMethod callHiddenPair;
	ToCallMethod callHiddenTriple;
	ToCallMethod callHiddenQuad;
	ToCallMethod callX_Wing;
	ToCallMethod callSwordfish;
	ToCallMethod callJellyfish;
	ToCallMethod callXY_Wing;

	ToCallMethod[] useForCalling;
	SolveMethod[] useForString;

	int amountOfSolveMethods;


	// Called by DrawNumsConstructor

	public UsingLogicalMethods(FullSudoku inwardPuzzle)
	{
		mySudoku = inwardPuzzle;

		stringPrint = new MethodExplanations(this);

		trickHS = new HiddenSingle(mySudoku,stringPrint);
		trickPPT = new PointingPairsTriples(mySudoku,stringPrint);
		trickCPT = new ClaimingPairsTriples(mySudoku,stringPrint);
		trickNPTQ = new NakedPairsTripsQuads(mySudoku,stringPrint);
		trickHPTQ = new HiddenPairsTripsQuads(mySudoku,stringPrint);
		trickFTTF = new FishTwoThreeFour(mySudoku,stringPrint);
		trickXYW = new XY_Wing(mySudoku,stringPrint);

		callNakedSingle = new ToCallMethod(){ public boolean callMethod(){ return mySudoku.NakedSingle(); } };
		callHiddenSingle = new ToCallMethod(){ public boolean callMethod(){ return trickHS.HiddenSingle(); } };
		callPointingPairTriple = new ToCallMethod(){ public boolean callMethod(){ return trickPPT.PointingPairsTriples(); } };
		callClaimingPairTriple = new ToCallMethod(){ public boolean callMethod(){ return trickCPT.ClaimingPairsTriples(); } };
		callNakedPair = new ToCallMethod(){ public boolean callMethod(){ return trickNPTQ.NakedPairs(); } };
		callNakedTriple = new ToCallMethod(){ public boolean callMethod(){ return trickNPTQ.NakedTriples(); } };
		callNakedQuad = new ToCallMethod(){ public boolean callMethod(){ return trickNPTQ.NakedQuads(); } };
		callHiddenPair = new ToCallMethod(){ public boolean callMethod(){ return trickHPTQ.HiddenPairs(); } };
		callHiddenTriple = new ToCallMethod(){ public boolean callMethod(){ return trickHPTQ.HiddenTriples(); } };
		callHiddenQuad = new ToCallMethod(){ public boolean callMethod(){ return trickHPTQ.HiddenQuads(); } };
		callX_Wing = new ToCallMethod(){ public boolean callMethod(){ return trickFTTF.X_Wing(); } };
		callSwordfish = new ToCallMethod(){ public boolean callMethod(){ return trickFTTF.Swordfish(); } };
		callJellyfish = new ToCallMethod(){ public boolean callMethod(){ return trickFTTF.Jellyfish(); } };
		callXY_Wing = new ToCallMethod(){ public boolean callMethod(){ return trickXYW.XY_Wing(); } };

		useForCalling = new ToCallMethod[]{callNakedSingle,callHiddenSingle,
											callPointingPairTriple,callClaimingPairTriple,
											callNakedPair,callHiddenPair,
											callNakedTriple,callHiddenTriple,
											callNakedQuad,callHiddenQuad,
											callX_Wing,callSwordfish,callJellyfish,
											callXY_Wing};

		useForString = new SolveMethod[]{SolveMethod.NakedSingle,SolveMethod.HiddenSingle,
											SolveMethod.PointingPairTriple,SolveMethod.ClaimingPairTriple,
											SolveMethod.NakedPair,SolveMethod.HiddenPair,
											SolveMethod.NakedTriple,SolveMethod.HiddenTriple,
											SolveMethod.NakedQuad,SolveMethod.HiddenQuad,
											SolveMethod.X_Wing,SolveMethod.Swordfish,SolveMethod.Jellyfish,
											SolveMethod.XY_Wing};

		amountOfSolveMethods = 14;

	} // Constructor


	public String solveOneStep()
	{
		// Every time this function is called, somethingChanged is set to false, and
		// it will remain that way until a solving method makes progress.

		// When a solving method achieves something and somethingChanged is set to true,
		// no more solving methods will be called on that run through the function.

		boolean somethingChanged = false;

		int methodCounter = 0;

		String stepDescription = null;


		while((!(somethingChanged)) && methodCounter < amountOfSolveMethods)
		{
			// Attempt one specific solving method, once.

			somethingChanged = useForCalling[methodCounter].callMethod();


			// If it made progress, get explanation of what happens

			if(somethingChanged)
			{
				// Retrieve the relevant String. This is the only spot where stepDescription can change.

				stepDescription = stringPrint.getStepOutput(useForString[methodCounter]);
			}


			// Increment methodCounter, moving on to the next solving method

			methodCounter++;
		}


		return stepDescription;

	} // solveOneStep()

} // SudokuSolveHumanMethods