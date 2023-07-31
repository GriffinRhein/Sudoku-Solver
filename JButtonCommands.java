import java.lang.StringBuilder;

public class JButtonCommands
{
	DrawNumsConstructor theHub;

	JButtonCommands(DrawNumsConstructor input)
	{
		theHub = input;
	}


	void resetEverything()
	{
		theHub.holderOfAllSteps.resetStepNumber();
		theHub.holderOfAllSteps.removeAll();
		theHub.holderOfAllSteps.repaint();

		for(int y=0;y<theHub.numRowInGrid;y++)
		{
			for(int x=0;x<theHub.numColInGrid;x++)
			{
				theHub.fillInMap[y][x].resetItAll();
				theHub.fillInMap[y][x].repaint();
			}
		}

		theHub.currentRow = 0;
		theHub.currentCol = 0;

		theHub.recToWorkWith.setDrawStatus(true);
		theHub.recToWorkWith.repaint();

		theHub.turnControlsOn();

	} // resetEverything()


	void revertToUserInput()
	{
		theHub.holderOfAllSteps.resetStepNumber();
		theHub.holderOfAllSteps.removeAll();
		theHub.holderOfAllSteps.repaint();

		for(int y=0;y<theHub.numRowInGrid;y++)
		{
			for(int x=0;x<theHub.numColInGrid;x++)
			{
				if(theHub.fillInMap[y][x].didSquareStartEmpty)
				{
					theHub.fillInMap[y][x].resetItAll();
					theHub.fillInMap[y][x].repaint();
				}
			}
		}

		theHub.recToWorkWith.setDrawStatus(true);
		theHub.recToWorkWith.repaint();

		theHub.turnControlsOn();

	} // revertToUserInput()


	String savingSudoku()
	{
		StringBuilder builtString = new StringBuilder();
		int blankCounter = 0;

		// Go through each square in the grid

		for(int y=0;y<theHub.numRowInGrid;y++)
		{
			for(int x=0;x<theHub.numColInGrid;x++)
			{
				// Check whether the square was filled in by the user

				if(!(theHub.fillInMap[y][x].didSquareStartEmpty))
				{
					// If the user entered a number, then append a letter
					// indicating how many blanks there were preceding it.
					// And reset the counter.

					if(blankCounter != 0)
					{
						builtString.append((char)(blankCounter+96));
						blankCounter = 0;
					}

					// Append the number

					builtString.append(theHub.fillInMap[y][x].getNum());
				}
				else
				{
					// If the user did not enter any number, then the square is blank.
					// Increment blankCounter.

					blankCounter++;

					// But make sure you get the final letter down if you've reached the end,
					// or throw down a "z" & reset if you've somehow gotten 26 blanks in a row

					if((y == theHub.numRowInGrid-1 && x == theHub.numColInGrid-1) || blankCounter > 25)
					{
						builtString.append((char)(blankCounter+96));
						blankCounter = 0;
					}
				}
			}
		}

		return builtString.toString();

	} // savingSudoku()


	int loadingSudoku(String theString)
	{
		// testString will be null if the user hit Cancel.
		// In that case, don't do anything, but don't
		// display any error message. So, return 0.

		if(theString == null)
			return 0;


		char[] charArray = theString.toCharArray();
		int nowASCII;

		// Make sure there are no invalid characters in the String

		for(int a=0;a<charArray.length;a++)
		{
			nowASCII = (int)charArray[a];

			if(nowASCII < 49 || (nowASCII > 57 && nowASCII < 97) || nowASCII > 122)
				return 1;
		}


		Integer[][] sudokuToLoad = new Integer[theHub.numRowInGrid][theHub.numColInGrid];

		int loadY = 0;
		int loadX = 0;
		int loadCounter = 0;

		// At this point, we know every character is a number or lowercase letter.

		for(int a=0;a<charArray.length;a++)
		{
			nowASCII = (int)charArray[a];

			// Letter if >96, Number if not.

			if(nowASCII > 96)
			{
				loadCounter = nowASCII - 96;

				while(loadCounter > 0)
				{
					if(loadY > theHub.numRowInGrid-1)
						return 3;

					sudokuToLoad[loadY][loadX] = null;

					if(loadX != theHub.numColInGrid-1)
					{
						loadX++;
					}
					else
					{
						loadX = 0;
						loadY++;
					}

					loadCounter--;
				}
			}
			else
			{
				if(loadY > theHub.numRowInGrid-1)
					return 3;

				sudokuToLoad[loadY][loadX] = Character.getNumericValue(charArray[a]);

				if(loadX != theHub.numColInGrid-1)
				{
					loadX++;
				}
				else
				{
					loadX = 0;
					loadY++;
				}

			}

		}

		if(loadY < theHub.numRowInGrid)
			return 2;


		// Otherwise, give fillInMap everything

		for(int y=0;y<theHub.numRowInGrid;y++)
		{
			for(int x=0;x<theHub.numColInGrid;x++)
			{
				theHub.fillInMap[y][x].setNum(sudokuToLoad[y][x],null);

				if(sudokuToLoad[y][x] != null)
					theHub.fillInMap[y][x].didSquareStartEmpty = false;
				else
					theHub.fillInMap[y][x].didSquareStartEmpty = true;

				theHub.fillInMap[y][x].repaint();
			}
		}

		return 0;

	} // loadingSudoku()

} // JButtonCommands class