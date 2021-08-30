public enum PTQ
{
	Pair(2),
	Trip(3),
	Quad(4);

	private PTQ(int a)
	{
		easyInt = a;
	}


	// Number corresponding to each
	// should require no explanation

	private final int easyInt;

	public int getEasyInt()
	{
		return easyInt;
	}
}