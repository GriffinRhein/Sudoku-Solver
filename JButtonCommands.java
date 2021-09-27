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
		theHub.itsTheTextArea.setText("");

		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				theHub.fillInMap[i][j].resetItAll();
				theHub.fillInMap[i][j].repaint();
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
		theHub.itsTheTextArea.setText("");

		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				if(theHub.stringCompForFinal[i][j] == "")
				{
					theHub.fillInMap[i][j].resetItAll();
					theHub.fillInMap[i][j].repaint();
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

		// Go through each square in the 9x9 grid

		for(int y=0;y<9;y++)
		{
			for(int x=0;x<9;x++)
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

					if((y == 8 && x == 8) || blankCounter > 25)
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


		Integer[][] sudokuToLoad = new Integer[9][9];

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
					if(loadY > 8)
						return 3;

					sudokuToLoad[loadY][loadX] = null;

					if(loadX != 8)
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
				if(loadY > 8)
					return 3;

				sudokuToLoad[loadY][loadX] = Character.getNumericValue(charArray[a]);

				if(loadX != 8)
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

		if(loadY < 9)
			return 2;


		// Otherwise, give fillInMap everything

		for(int s=0;s<9;s++)
		{
			for(int t=0;t<9;t++)
			{
				theHub.fillInMap[s][t].setNum(sudokuToLoad[s][t]);

				if(sudokuToLoad[s][t] != null)
					theHub.fillInMap[s][t].didSquareStartEmpty = false;
				else
					theHub.fillInMap[s][t].didSquareStartEmpty = true;

				theHub.fillInMap[s][t].repaint();
			}
		}

		return 0;

	} // loadingSudoku()

} // JButtonCommands class