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


	// Incremented every time we are about to make progress

	private int currentStepNum;

	int getStepNum()
	{
		return currentStepNum;
	}


	// Gain ability to call all solving techniques

	private HiddenSingle trickHS;
	private PointingPairsTriples trickPPT;
	private ClaimingPairsTriples trickCPT;
	private NakedPairsTripsQuads trickNPTQ;
	private HiddenPairsTripsQuads trickHPTQ;
	private FishTwoThreeFour trickFTTF;
	private XY_Wing trickXYW;

	private ToCallMethod callNakedSingle;
	private ToCallMethod callHiddenSingle;
	private ToCallMethod callPointingPairTriple;
	private ToCallMethod callClaimingPairTriple;
	private ToCallMethod callNakedPair;
	private ToCallMethod callNakedTriple;
	private ToCallMethod callNakedQuad;
	private ToCallMethod callHiddenPair;
	private ToCallMethod callHiddenTriple;
	private ToCallMethod callHiddenQuad;
	private ToCallMethod callX_Wing;
	private ToCallMethod callSwordfish;
	private ToCallMethod callJellyfish;
	private ToCallMethod callXY_Wing;

	private ToCallMethod[] useForCalling;
	private SolveMethod[] useForString;

	private int amountOfSolveMethods;


	// Called by DrawNumsConstructor

	UsingLogicalMethods(FullSudoku inwardPuzzle)
	{
		// Sudoku itself

		mySudoku = inwardPuzzle;


		// Used to create Strings describing what just happened

		stringPrint = new MethodExplanations(this);


		// 

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

		currentStepNum = 0;

	} // Constructor


	String solveOneStep()
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
				currentStepNum++;


				// Retrieve the relevant String. This is the only spot where stepDescription can change.

				stepDescription = stringPrint.getStepOutput(useForString[methodCounter]);
			}


			// Increment methodCounter, moving on to the next solving method

			methodCounter++;
		}


		// Returns null if nothing changed. Otherwise, it returns the explanation String

		return stepDescription;

	} // solveOneStep()

} // UsingLogicalMethods