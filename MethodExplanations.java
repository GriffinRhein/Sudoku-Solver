import java.lang.StringBuilder;

public class MethodExplanations
{
	private UsingLogicalMethods mainSolver;
	private BoxTranslator myUnboxer;

	public MethodExplanations(UsingLogicalMethods woohoo)
	{
		mainSolver = woohoo;
		myUnboxer = woohoo.mySudoku.theUnboxer;
	}


	public Square SquareA;
	public Square SquareB;
	public Square SquareC;

	public int foundResult;

	public HouseType houseContextType;
	public HouseType houseTargetType;

	public int intTargetSet;

	public PTQ subsetType;

	public int[] miscNumsA = new int[4];
	public int[] miscNumsB = new int[4];

	public Integer[] arrayOfKilledPoss = new Integer[7];

	public Integer[][] squaresForKilledPoss = new Integer[7][12];



	// For Pointing Pairs/Triples, Claiming Pairs/Triples.
	// Naked Pairs/Trips/Quads, and Hidden Pairs/Trips/Quads:

	// First row in squaresForKilledPoss will list the squares in the
	// row/column/box where the first number of arrayOfKilledPoss is
	// eliminated, second row in squaresForKilledPoss will list the
	// squares in the row/column/box where the second number of
	// arrayOfKilledPoss is eliminated, etc.


	// For X-Wing/Swordfish/Jellyfish:

	// Since no more than one unique number will ever be eliminated at once,
	// the logic is changed. First row in squaresForKilledPoss will list
	// integers identifying the squares in the first column/row which lose
	// the number, the second row in squaresForKilledPoss will list integers
	// identifying the squares in the second column/row which lose it, etc.


	// For XY-Wing:

	// No more than one unique number will ever be eliminated at once,
	// but those eliminations are not restricted to a single house.
	// So, the first row in squaresForKilledPoss will list the rows of
	// the affected squares, and the second row will list the colums.	
	// For example, the coordinates of the fifth changed square will
	// be [squaresForKilledPoss[0][4]][squaresForKilledPoss[1][4]].



	public void clearEnoughForNew(SolveMethod t)
	{
		int linesToClear = t.getLinesNeeded();
		int slotsToClear = t.getSlotsNeeded();

		// Nullify as much as needed.

		for(int i=0;i<linesToClear;i++)
		{
			arrayOfKilledPoss[i] = null;

			for(int j=0;j<slotsToClear;j++)
			{
				squaresForKilledPoss[i][j] = null;
			}
		}

	} // clearEnoughForNow()


	private String coordStr(HouseType a, int whichSet, int theSquareInSet)
	{
		String stringToReturn;

		if(a == HouseType.Row)
		{
			stringToReturn = "["+whichSet+"]["+theSquareInSet+"]";
		}
		else if(a == HouseType.Col)
		{
			stringToReturn = "["+theSquareInSet+"]["+whichSet+"]";
		}
		else
		{
			int myY = myUnboxer.rowOfBoxSquare(whichSet,theSquareInSet);
			int myX = myUnboxer.colOfBoxSquare(whichSet,theSquareInSet);

			stringToReturn = "["+myY+"]["+myX+"]";
		}

		return stringToReturn;

	} // coordStr()


	private void addElimStrings(StringBuilder s, SolveMethod t)
	{
		int lookStr;

		int timesAcross = t.getTimesAcross();
		int slotsNeeded = t.getSlotsNeeded();
		SolveMethod.MoreGeneral genMethod = t.getGenMethod();

		for(int i=0;i<timesAcross;i++)
		{
			switch(genMethod)
			{
				case PCNH: s.append(arrayOfKilledPoss[i]+" is eliminated from "); break;
				case Fish: s.append("Within "+houseTargetType+" "+miscNumsB[i]+", "+arrayOfKilledPoss[0]+" is eliminated from "); break;
				case Wing: s.append(arrayOfKilledPoss[i]+" is eliminated from "); break;
			}

			lookStr = 0;

			if(squaresForKilledPoss[i][lookStr] == null)
				s.append("no squares.");
			else
			{
				while(lookStr < slotsNeeded && squaresForKilledPoss[i][lookStr] != null)
				{
					// Write out the coordinates of the square where the number was eliminated

					switch(genMethod)
					{
						case PCNH: s.append(coordStr(houseTargetType,intTargetSet,squaresForKilledPoss[i][lookStr])); break;
						case Fish: s.append(coordStr(houseTargetType,miscNumsB[i],squaresForKilledPoss[i][lookStr])); break;
						case Wing: s.append(coordStr(HouseType.Row,squaresForKilledPoss[i][lookStr],squaresForKilledPoss[i+1][lookStr])); break;
					}


					// Grammar

					if(lookStr+1 >= slotsNeeded || squaresForKilledPoss[i][lookStr+1] == null)
						{ s.append("."); }
					else if(lookStr+2 >= slotsNeeded || squaresForKilledPoss[i][lookStr+2] == null)
					{
						if(lookStr == 0)
							s.append(" & ");
						else
							s.append(", & ");
					}
					else
						{ s.append(", "); }


					// On to the next square the number was eliminated from

					lookStr++;
				}
			}

			if(i < timesAcross-1)
				s.append("\n\n");
		}

	} // addElimStrings


	// Big Kahuna. Responsible for putting the message together.

	public String getStepOutput(SolveMethod theMethod)
	{
		String bigStepOutput = null; // It'll be changed before being returned

		StringBuilder stringToBuild;

		String firstSqCo;
		String secondSqCo;
		String thirdSqCo;
		String fourthSqCo;

		switch(theMethod)
		{
			case NakedSingle:

			stringToBuild = new StringBuilder("");

			SquareA = mainSolver.mySudoku.getMostRecentNakedSingle();

			stringToBuild.append("Naked Single: Square["+SquareA.ownRow+"]["+SquareA.ownCol+"] must contain "+SquareA.result+".");

			bigStepOutput = stringToBuild.toString();

			break;


			case HiddenSingle:

			stringToBuild = new StringBuilder("");

			stringToBuild.append("Hidden Single: Square["+SquareA.ownRow+"]["+SquareA.ownCol+"] is the only ");
			stringToBuild.append("square in "+houseTargetType+" "+intTargetSet+" which can contain "+foundResult+".");

			bigStepOutput = stringToBuild.toString();

			break;


			case PointingPairTriple:

			stringToBuild = new StringBuilder("");

			if(subsetType == PTQ.Pair)
				stringToBuild.append("Pointing Pair: ");
			else
				stringToBuild.append("Pointing Triple: ");

			stringToBuild.append("Within Box "+miscNumsA[0]+", the only squares which can contain number ");
			stringToBuild.append(arrayOfKilledPoss[0]+" are in "+houseTargetType+" "+intTargetSet+".");
			stringToBuild.append("\n\n");
			stringToBuild.append("Eliminate number "+arrayOfKilledPoss[0]+" from all squares in ");
			stringToBuild.append(houseTargetType+" "+intTargetSet+" outside of Box "+miscNumsA[0]+".");
			stringToBuild.append("\n\n");

			addElimStrings(stringToBuild,theMethod);
			
			bigStepOutput = stringToBuild.toString();

			break;


			case ClaimingPairTriple:

			stringToBuild = new StringBuilder("");

			if(subsetType == PTQ.Pair)
				stringToBuild.append("Claiming Pair: ");
			else
				stringToBuild.append("Claiming Triple: ");

			stringToBuild.append("Within "+houseContextType+" "+miscNumsA[0]+", the only squares which can ");
			stringToBuild.append("contain number "+arrayOfKilledPoss[0]+" are in Box "+intTargetSet+".");
			stringToBuild.append("\n\n");
			stringToBuild.append("Eliminate number "+arrayOfKilledPoss[0]+" from all squares in ");
			stringToBuild.append("Box "+intTargetSet+" outside of "+houseContextType+" "+miscNumsA[0]+".");
			stringToBuild.append("\n\n");

			addElimStrings(stringToBuild,theMethod);
			
			bigStepOutput = stringToBuild.toString();

			break;


			case NakedPair:

			stringToBuild = new StringBuilder("");

			firstSqCo = coordStr(houseTargetType,intTargetSet,miscNumsA[0]);
			secondSqCo = coordStr(houseTargetType,intTargetSet,miscNumsA[1]);

			stringToBuild.append("Naked Pair: Within "+houseTargetType+" "+intTargetSet+", squares "+firstSqCo+" & "+secondSqCo+" ");
			stringToBuild.append("must not contain any number besides "+arrayOfKilledPoss[0]+" or "+arrayOfKilledPoss[1]+".");

			stringToBuild.append("\n\n");

			stringToBuild.append("Eliminate numbers "+arrayOfKilledPoss[0]+" & "+arrayOfKilledPoss[1]+" from all squares in "+houseTargetType+" "+intTargetSet+" ");
			stringToBuild.append("except for "+firstSqCo+" & "+secondSqCo+".");

			stringToBuild.append("\n\n");

			addElimStrings(stringToBuild,theMethod);

			bigStepOutput = stringToBuild.toString();

			break;


			case NakedTriple:

			stringToBuild = new StringBuilder("");

			firstSqCo = coordStr(houseTargetType,intTargetSet,miscNumsA[0]);
			secondSqCo = coordStr(houseTargetType,intTargetSet,miscNumsA[1]);
			thirdSqCo = coordStr(houseTargetType,intTargetSet,miscNumsA[2]);

			stringToBuild.append("Naked Triple: Within "+houseTargetType+" "+intTargetSet+", squares "+firstSqCo+", "+secondSqCo+", & "+thirdSqCo+" ");
			stringToBuild.append("must not contain any number besides "+arrayOfKilledPoss[0]+", "+arrayOfKilledPoss[1]+", or "+arrayOfKilledPoss[2]+".");

			stringToBuild.append("\n\n");

			stringToBuild.append("Eliminate numbers "+arrayOfKilledPoss[0]+", "+arrayOfKilledPoss[1]+", & "+arrayOfKilledPoss[2]+" from all squares ");
			stringToBuild.append("in "+houseTargetType+" "+intTargetSet+" except for "+firstSqCo+", "+secondSqCo+", & "+thirdSqCo+".");

			stringToBuild.append("\n\n");

			addElimStrings(stringToBuild,theMethod);

			bigStepOutput = stringToBuild.toString();

			break;


			case NakedQuad:

			stringToBuild = new StringBuilder("");

			firstSqCo = coordStr(houseTargetType,intTargetSet,miscNumsA[0]);
			secondSqCo = coordStr(houseTargetType,intTargetSet,miscNumsA[1]);
			thirdSqCo = coordStr(houseTargetType,intTargetSet,miscNumsA[2]);
			fourthSqCo = coordStr(houseTargetType,intTargetSet,miscNumsA[3]);

			stringToBuild.append("Naked Quad: Within "+houseTargetType+" "+intTargetSet+", squares ");
			stringToBuild.append(firstSqCo+", "+secondSqCo+", "+thirdSqCo+", & "+fourthSqCo+" must not contain any number besides ");
			stringToBuild.append(arrayOfKilledPoss[0]+", "+arrayOfKilledPoss[1]+", "+arrayOfKilledPoss[2]+", or "+arrayOfKilledPoss[3]+".");

			stringToBuild.append("\n\n");

			stringToBuild.append("Eliminate numbers "+arrayOfKilledPoss[0]+", "+arrayOfKilledPoss[1]+", "+arrayOfKilledPoss[2]+", & "+arrayOfKilledPoss[3]+" ");
			stringToBuild.append("from all squares in "+houseTargetType+" "+intTargetSet+" except for ");
			stringToBuild.append(firstSqCo+", "+secondSqCo+", "+thirdSqCo+", & "+fourthSqCo+".");

			stringToBuild.append("\n\n");

			addElimStrings(stringToBuild,theMethod);

			bigStepOutput = stringToBuild.toString();

			break;


			case HiddenPair:

			stringToBuild = new StringBuilder("");

			firstSqCo = coordStr(houseTargetType,intTargetSet,miscNumsB[0]);
			secondSqCo = coordStr(houseTargetType,intTargetSet,miscNumsB[1]);

			stringToBuild.append("Hidden Pair: Within "+houseTargetType+" "+intTargetSet+", numbers "+miscNumsA[0]+" & "+miscNumsA[1]+" ");
			stringToBuild.append("must not appear in any square besides "+firstSqCo+" or "+secondSqCo+".");

			stringToBuild.append("\n\n");

			stringToBuild.append("Eliminate all numbers except "+miscNumsA[0]+" & "+miscNumsA[1]+" from squares "+firstSqCo+" & "+secondSqCo+".");

			stringToBuild.append("\n\n");

			addElimStrings(stringToBuild,theMethod);

			bigStepOutput = stringToBuild.toString();

			break;


			case HiddenTriple:

			stringToBuild = new StringBuilder("");

			firstSqCo = coordStr(houseTargetType,intTargetSet,miscNumsB[0]);
			secondSqCo = coordStr(houseTargetType,intTargetSet,miscNumsB[1]);
			thirdSqCo = coordStr(houseTargetType,intTargetSet,miscNumsB[2]);

			stringToBuild.append("Hidden Triple: Within "+houseTargetType+" "+intTargetSet+", numbers "+miscNumsA[0]+", "+miscNumsA[1]+", & "+miscNumsA[2]+" ");
			stringToBuild.append("must not appear in any square besides "+firstSqCo+", "+secondSqCo+", or "+thirdSqCo+".");

			stringToBuild.append("\n\n");

			stringToBuild.append("Eliminate all numbers except "+miscNumsA[0]+", "+miscNumsA[1]+", & "+miscNumsA[2]+" ");
			stringToBuild.append("from squares "+firstSqCo+", "+secondSqCo+", & "+thirdSqCo+".");

			stringToBuild.append("\n\n");

			addElimStrings(stringToBuild,theMethod);

			bigStepOutput = stringToBuild.toString();

			break;


			case HiddenQuad:

			stringToBuild = new StringBuilder("");

			firstSqCo = coordStr(houseTargetType,intTargetSet,miscNumsB[0]);
			secondSqCo = coordStr(houseTargetType,intTargetSet,miscNumsB[1]);
			thirdSqCo = coordStr(houseTargetType,intTargetSet,miscNumsB[2]);
			fourthSqCo = coordStr(houseTargetType,intTargetSet,miscNumsB[3]);

			stringToBuild.append("Hidden Quad: Within "+houseTargetType+" "+intTargetSet+", numbers ");
			stringToBuild.append(miscNumsA[0]+", "+miscNumsA[1]+", "+miscNumsA[2]+", & "+miscNumsA[3]+" ");
			stringToBuild.append("must not appear in any square besides "+firstSqCo+", "+secondSqCo+", "+thirdSqCo+", or "+fourthSqCo+".");

			stringToBuild.append("\n\n");

			stringToBuild.append("Eliminate all numbers except "+miscNumsA[0]+", "+miscNumsA[1]+", "+miscNumsA[2]+", & "+miscNumsA[3]+" ");
			stringToBuild.append("from squares "+firstSqCo+", "+secondSqCo+", "+thirdSqCo+", & "+fourthSqCo+".");

			stringToBuild.append("\n\n");

			addElimStrings(stringToBuild,theMethod);

			bigStepOutput = stringToBuild.toString();

			break;


			case X_Wing:

			stringToBuild = new StringBuilder("");

			stringToBuild.append("X-Wing: Within both "+houseContextType+" "+miscNumsA[0]+" and "+houseContextType+" "+miscNumsA[1]+", ");
			stringToBuild.append("the number "+arrayOfKilledPoss[0]+" must not appear outside of ");
			stringToBuild.append(houseTargetType+" "+miscNumsB[0]+" or "+houseTargetType+" "+miscNumsB[1]+".");

			stringToBuild.append("\n\n");

			stringToBuild.append("Eliminate "+arrayOfKilledPoss[0]+" from all squares in ");
			stringToBuild.append(houseTargetType+" "+miscNumsB[0]+" & "+houseTargetType+" "+miscNumsB[1]+" ");
			stringToBuild.append("except those part of "+houseContextType+" "+miscNumsA[0]+" or "+houseContextType+" "+miscNumsA[1]+".");

			stringToBuild.append("\n\n");

			addElimStrings(stringToBuild,theMethod);

			bigStepOutput = stringToBuild.toString();

			break;


			case Swordfish:

			stringToBuild = new StringBuilder("");

			stringToBuild.append("Swordfish: Within "+houseContextType+" "+miscNumsA[0]+", "+houseContextType+" "+miscNumsA[1]+", & ");
			stringToBuild.append(houseContextType+" "+miscNumsA[2]+", the number "+arrayOfKilledPoss[0]+" must not appear outside of ");
			stringToBuild.append(houseTargetType+" "+miscNumsB[0]+", "+houseTargetType+" "+miscNumsB[1]+", or "+houseTargetType+" "+miscNumsB[2]+".");

			stringToBuild.append("\n\n");

			stringToBuild.append("Eliminate "+arrayOfKilledPoss[0]+" from all squares in "+houseTargetType+" "+miscNumsB[0]+", ");
			stringToBuild.append(houseTargetType+" "+miscNumsB[1]+", & "+houseTargetType+" "+miscNumsB[2]+" ");
			stringToBuild.append("except those part of "+houseContextType+" "+miscNumsA[0]+", ");
			stringToBuild.append(houseContextType+" "+miscNumsA[1]+", or "+houseContextType+" "+miscNumsA[2]+".");

			stringToBuild.append("\n\n");

			addElimStrings(stringToBuild,theMethod);

			bigStepOutput = stringToBuild.toString();

			break;


			case Jellyfish:

			stringToBuild = new StringBuilder("");

			stringToBuild.append("Jellyfish: Within "+houseContextType+" "+miscNumsA[0]+", "+houseContextType+" "+miscNumsA[1]+", ");
			stringToBuild.append(houseContextType+" "+miscNumsA[2]+", & "+houseContextType+" "+miscNumsA[3]+", the number "+arrayOfKilledPoss[0]+" ");
			stringToBuild.append("must not appear outside of "+houseTargetType+" "+miscNumsB[0]+", "+houseTargetType+" "+miscNumsB[1]+", ");
			stringToBuild.append(houseTargetType+" "+miscNumsB[2]+", or "+houseTargetType+" "+miscNumsB[3]+".");

			stringToBuild.append("\n\n");

			stringToBuild.append("Eliminate "+arrayOfKilledPoss[0]+" from all squares in "+houseTargetType+" "+miscNumsB[0]+", ");
			stringToBuild.append(houseTargetType+" "+miscNumsB[1]+", "+houseTargetType+" "+miscNumsB[2]+", & "+houseTargetType+" "+miscNumsB[3]+", ");
			stringToBuild.append("except those part of "+houseContextType+" "+miscNumsA[0]+", "+houseContextType+" "+miscNumsA[1]+", ");
			stringToBuild.append(houseContextType+" "+miscNumsA[2]+", or "+houseContextType+" "+miscNumsA[3]+".");

			stringToBuild.append("\n\n");

			addElimStrings(stringToBuild,theMethod);

			bigStepOutput = stringToBuild.toString();

			break;


			case XY_Wing:

			stringToBuild = new StringBuilder("");

			stringToBuild.append("XY Wing: "+coordStr(HouseType.Row,SquareA.ownRow,SquareA.ownCol)+", which must not ");
			stringToBuild.append("contain anything besides "+miscNumsA[0]+" or "+miscNumsB[0]+", shares a row/column/box with:");

			stringToBuild.append("\n\n");

			stringToBuild.append("- "+coordStr(HouseType.Row,SquareB.ownRow,SquareB.ownCol)+", which must not ");
			stringToBuild.append("contain anything besides "+miscNumsA[1]+" or "+miscNumsB[1]);

			stringToBuild.append("\n\n");

			stringToBuild.append("- "+coordStr(HouseType.Row,SquareC.ownRow,SquareC.ownCol)+", which must not ");
			stringToBuild.append("contain anything besides "+miscNumsA[2]+" or "+miscNumsB[2]);

			stringToBuild.append("\n\n");
			stringToBuild.append("\n\n");

			stringToBuild.append("Eliminate "+arrayOfKilledPoss[0]+" from all squares sharing a row/column/box with both ");
			stringToBuild.append(coordStr(HouseType.Row,SquareB.ownRow,SquareB.ownCol)+" and "+coordStr(HouseType.Row,SquareC.ownRow,SquareC.ownCol)+".");

			stringToBuild.append("\n\n");

			addElimStrings(stringToBuild,theMethod);

			bigStepOutput = stringToBuild.toString();
		}

		return bigStepOutput;
	}

}