public class Square
{
	Integer result; // The final number that you see
	Integer[] possArray; // Every possible number it could still be

	String delHistory; // Record of when each possibility was axed

	boolean answerAtStart; // Whether the result was entered by the user

	int numPossLeft; // Number of possibilities currently left


	int ownRow, ownCol, ownBox;

	private static int[][] findBox = new int[][]{ {0,1,2}, {3,4,5}, {6,7,8} };


	Integer lastResortResult;
	boolean lastResortIsResultFixed;


	// Constructor

	Square(int selfRow, int selfCol)
	{
		// Initialize variables to that of an unsolved square.
		// If the result is entered directly by the user before the
		// solving takes place, FullSudoku will adjust these.

		result = null;
		delHistory = "";
		answerAtStart = false;

		// Fix own row and column

		ownRow = selfRow;
		ownCol = selfCol;
		ownBox = findBox[selfRow/3][selfCol/3];

		// Set full range of possibilities

		numPossLeft = 9;
		possArray = new Integer[9];

		// Every possibility is its index plus 1

		for(int i=0;i<9;i++)
		{ possArray[i] = i+1; }

		// If result is finalized, result is not null and possArray is null
		// If result is not finalized, result is null and possArray is not null
	}

} // Square