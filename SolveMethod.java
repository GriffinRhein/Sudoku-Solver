public enum SolveMethod
{
	// These exist for the benefit of MethodExplanations,
	// which needs to know how to use the data it has been
	// given to output an explanation of what happened.

	// Whichever SolveMethod is input into MethodExplanations will, on its own,
	// tell MethodExplanations what String format to use. The additional numbers
	// are indicative of how to use the squaresForKilledPoss array.

	NakedSingle(0,0,0,MoreGeneral.Single),
	HiddenSingle(0,0,0,MoreGeneral.Single),
	PointingPairTriple(1,1,6,MoreGeneral.PCNH),
	ClaimingPairTriple(1,1,6,MoreGeneral.PCNH),
	NakedPair(2,2,7,MoreGeneral.PCNH),
	NakedTriple(3,3,6,MoreGeneral.PCNH),
	NakedQuad(4,4,5,MoreGeneral.PCNH),
	HiddenPair(7,7,2,MoreGeneral.PCNH),
	HiddenTriple(6,6,3,MoreGeneral.PCNH),
	HiddenQuad(5,5,4,MoreGeneral.PCNH),
	X_Wing(2,2,7,MoreGeneral.Fish),
	Swordfish(3,3,6,MoreGeneral.Fish),
	Jellyfish(4,4,5,MoreGeneral.Fish),
	XY_Wing(2,1,12,MoreGeneral.Wing);

	private SolveMethod(int a, int b, int c, MoreGeneral d)
	{
		linesNeeded = a;
		timesAcross = b;
		slotsNeeded = c;
		genMethod = d;
	}


	// Number of lines to be reserved
	// in squaresForKilledPoss

	private final int linesNeeded;

	int getLinesNeeded()
	{
		return linesNeeded;
	}


	// Number of times addElimStrings() runs from
	// left to right in squaresForKilledPoss

	// Usually identical to linesNeeded, but some solves
	// may use multiple rows during a single run, with
	// this number being lower in those cases.

	private final int timesAcross;

	int getTimesAcross()
	{
		return timesAcross;
	}


	// Number of entries to be reserved
	// per line of squaresForKilledPoss

	private final int slotsNeeded;

	int getSlotsNeeded()
	{
		return slotsNeeded;
	}


	// For determining which String
	// to use in addElimStrings()

	enum MoreGeneral
	{
		Single,
		PCNH,
		Fish,
		Wing;
	}

	private final MoreGeneral genMethod;

	MoreGeneral getGenMethod()
	{
		return genMethod;
	}
}