public class Square
{
	public Integer result; // The final number that you see
	public Integer[] possArray; // Every possible number it could still be

	public boolean answerAtStart = false;

	public int ownRow;
	public int ownCol;
	public int ownBox;

	public int VARIETY; // Total amount of possibilities at start
	public int numPossLeft; // Number of possibilities left

	private static int[][] findBox = new int[][]{ {0,1,2},
												  {3,4,5},
												  {6,7,8} };

	// Constructor

	public Square(int selfRow, int selfCol)
	{
		// Initialize result to null

		result = null;

		// Fix own row and column

		ownRow = selfRow;
		ownCol = selfCol;
		ownBox = findBox[selfRow/3][selfCol/3];

		// Set full range of possibilities

		VARIETY = 9;
		numPossLeft = VARIETY;
		possArray = new Integer[VARIETY];

		// Every possibility is its index plus 1

		for(int i=0;i<VARIETY;i++)
		{ possArray[i] = i+1; }

		// If result is finalized, result is not null and possArray is null
		// If result is not finalized, result is null and possArray is not null
	}

} // Square