import java.util.LinkedList;

public class FullSudoku
{
	// Squares themselves

	public Square[][] SudokuMap = new Square[9][9];

	public String[][] forNumsUseFill;

	protected int squaresSolved = 0;


	// Linked List for what squares to finalize.
	// Squares in the Linked List already have their
	// results determined and their possArray null,
	// but their answers haven't been removed from
	// squares sharing a row/column/box with them

	private LinkedList<Square> readyToFinalize = new LinkedList<Square>();


	// Array 0 is for RCB 0, Array 1 is for RCB 1, etc.
	// Within an array, index 0 details how many squares
	// still have 1 in the possArray, index 1 details how
	// many squares still have 2 in the possArray, etc.

	public int[][] rowPossPrevalence = new int[9][9];
	public int[][] colPossPrevalence = new int[9][9];
	public int[][] boxPossPrevalence = new int[9][9];


	// Assists in traversing boxes

	public int[][] boxesToCheck = new int[][]{	{0,1,2,0,1,2},
												{0,1,2,3,4,5},
												{0,1,2,6,7,8},
												{3,4,5,0,1,2},
												{3,4,5,3,4,5},
												{3,4,5,6,7,8},
												{6,7,8,0,1,2},
												{6,7,8,3,4,5},
												{6,7,8,6,7,8} };


	// Provide a row of squares

	public Square[] provideRow(int rowNum)
	{
		Square[] rowProvided = new Square[9];

		for(int i=0;i<9;i++)
		{
			rowProvided[i] = SudokuMap[rowNum][i];
		}

		return rowProvided;
	}

	// Provide a column of squares

	public Square[] provideCol(int colNum)
	{
		Square[] colProvided = new Square[9];

		for(int i=0;i<9;i++)
		{
			colProvided[i] = SudokuMap[i][colNum];
		}

		return colProvided;

	}

	// Provide a box of squares

	public Square[] provideBox(int boxNum)
	{

		Square[] boxProvided = new Square[9];

		for(int i=0;i<9;i++)
		{
			boxProvided[i] = SudokuMap[boxesToCheck[boxNum][i / 3]][boxesToCheck[boxNum][(i % 3) + 3]];
		}

		return boxProvided;

	}


	// Should be used in each solve. Calling this function will wash
	// a number from a square's possArray if is there, then put the
	// square in the linked list if it is now down to 1 possibility.

	public void elimFromPossArray(Square a,Integer b)
	{
		Square testSquare = a;
		Integer possOut = b;

		// First, check that the square is still unsolved

		if( testSquare.result == null )
		{
			// Then check that the value is not already eliminated

			if( testSquare.possArray[possOut-1] != null )
			{
				testSquare.possArray[possOut-1] = null;
				testSquare.numPossLeft--;

				rowPossPrevalence[testSquare.ownRow][possOut-1]--;
				colPossPrevalence[testSquare.ownCol][possOut-1]--;
				boxPossPrevalence[testSquare.ownBox][possOut-1]--;

				// If this takes the numPossLeft down to one,
				// set the last Poss as the result, set possArray to null,
				// and put the square in the Linked List

				if( testSquare.numPossLeft == 1 )
				{
					for(int u=0;u<9;u++)
					{
						if(testSquare.possArray[u] != null)
							testSquare.result = testSquare.possArray[u];
	
					}
					testSquare.possArray = null;
					squaresSolved++;
					readyToFinalize.add(testSquare);
				}
			}
		}

	} // elimFromPossArray()


	// Procedure for removing a single item from the finalization Linked List
	// by retrieving its row & column, then ruling result out from the squares
	// sharing a row or column or box.

	// Naked Single Implementation

	private void knockOutInRCB(Square spot)
	{

		Integer itsResult = spot.result;
		int itsRow = spot.ownRow;
		int itsCol = spot.ownCol;

		Square possElimTemplate;
		int[] rowToCheckBox = new int[3];  // For marking which rows the box occupies
		int[] colToCheckBox = new int[3];  // For marking which cols the box occupies


		// Now we have to take that number out of
		// the possibility arrays for every square in
		// the same row, column, and box

		// Traverse through the squares in the row,
		// eliminating the value at element-1

		for(int k=0;k<9;k++)
		{
			possElimTemplate = SudokuMap[itsRow][k];
			elimFromPossArray(possElimTemplate,itsResult);
		}


		// Traverse through the squares in the column,
		// eliminating the value at element-1

		for(int k=0;k<9;k++)
		{
			possElimTemplate = SudokuMap[k][itsCol];
			elimFromPossArray(possElimTemplate,itsResult);
		}


		// Finally, the box

		switch (itsRow)
		{
			case 0: case 1: case 2:
				rowToCheckBox[0] = 0;
				rowToCheckBox[1] = 1;
				rowToCheckBox[2] = 2;
				break;

			case 3: case 4: case 5:
				rowToCheckBox[0] = 3;
				rowToCheckBox[1] = 4;
				rowToCheckBox[2] = 5;
				break;

			case 6: case 7: case 8:
				rowToCheckBox[0] = 6;
				rowToCheckBox[1] = 7;
				rowToCheckBox[2] = 8;
				break;
		}

		switch (itsCol)
		{
			case 0: case 1: case 2:
				colToCheckBox[0] = 0;
				colToCheckBox[1] = 1;
				colToCheckBox[2] = 2;
				break;

			case 3: case 4: case 5:
				colToCheckBox[0] = 3;
				colToCheckBox[1] = 4;
				colToCheckBox[2] = 5;
				break;

			case 6: case 7: case 8:
				colToCheckBox[0] = 6;		
				colToCheckBox[1] = 7;
				colToCheckBox[2] = 8;
				break;
		}

		for(int y=0;y<rowToCheckBox.length;y++)
		{
			for(int z=0;z<colToCheckBox.length;z++)
			{
				possElimTemplate = SudokuMap[rowToCheckBox[y]][colToCheckBox[z]];
				elimFromPossArray(possElimTemplate,itsResult);
			}
		}

	} // knockOutInRCB()


	// Going through the Linked List until it is empty

	public void straightSolve()
	{
		Square templateToRemove;

		// This is the ONLY place you should call knockOutInRCB()

		while( !(readyToFinalize.isEmpty()) )
		{
			templateToRemove = readyToFinalize.poll();
			knockOutInRCB(templateToRemove);
		}

	} // straightSolve()


	// Search 9 squares for any which happen to be
	// the only possible spot for a number needed
	// If you find one (or more), fill it in, and
	// put the square in the Linked List

	// Hidden Single Implementation

	private void searchOneRCB(Square[] oneRCB, int setType, int whichSet)
	{
		// New and improved Hidden Single function uses PossPrevalence arrays

		int[] herePossPrevalence = new int[9];

		switch(setType)
		{
			case 85: herePossPrevalence = rowPossPrevalence[whichSet]; break;
			case 86: herePossPrevalence = colPossPrevalence[whichSet]; break;
			case 87: herePossPrevalence = boxPossPrevalence[whichSet]; break;
		}


		int b;
		boolean foundIt;

		// Procedure occurs for all numbers 1 through 9

		for(int a=1;a<=9;a++)
		{
			// Check whether the PossPrevalence array indicates that there is
			// only one square within the RCB capable of containing the number.

			if(herePossPrevalence[a-1] < 2)
			{
				// Now we somewhat bring back the original plan. Go through the
				// RCB and find the square where the number is still available.

				// Note that a number which has only one possible square retains
				// a PossPrevalence of 1 after being officially solved for the
				// RCB, so most of the time when we reach this point it will be
				// with the number already in the result slot for some square.

				b = 0;
				foundIt = false;

				while(b<9 && !(foundIt))
				{
					// If you ever find a square with our number as its result,
					// call the operation off here; there's no work to do.

					if(oneRCB[b].result != null)
					{
						if(oneRCB[b].result == a)
						{
							foundIt = true;
						}
					}


					// Normally we make sure the square is not already solved
					// before looking for the possArray, but we just checked
					// whether it IS already solved, so this is the "else"

					else
					{
						// Then check to see if the square still
						// contains our number in its possArray

						if(oneRCB[b].possArray[a-1] != null)
						{
							foundIt = true;

							// If so, knock out every possibility
							// in the square except for that one.

							for(int c=1;c<=9;c++)
							{
								if(c != a)
								{
									elimFromPossArray(oneRCB[b],c);
								}
							}
						}
					}

					b++;
				}
			}
		}

		// Throw in an emptying of the linked list. Let your discoveries for
		// one RCB influence your discoveries for future ones right away.

		// Calling straightSolve() after and perhaps before each solving
		// technique is advisable. Leave no squares stuck in the limbo of
		// being solved but without their numbers removed from the RCB.

		straightSolve();

	} // searchOneRCB()


	// This function calls the following searchOneRCB() on all rows, columns,
	// and boxes, while making sure to allow for emptying of the Linked List

	private void intenseSolve()
	{
		// Best to call straightSolve() first, to make sure all of the known
		// numbers are removed from the relevant possbility arrays first.
		// If the list is empty anyway, the program just moves on.

		straightSolve();

		for(int i=0;i<9;i++)
		{
			searchOneRCB(provideRow(i),85,i);
		}


		for(int j=0;j<9;j++)
		{
			searchOneRCB(provideCol(j),86,j);
		}


		for(int k=0;k<9;k++)
		{
			searchOneRCB(provideBox(k),87,k);
		}

	} // intenseSolve()


	// Calls intenseSolve() until it results in no changes.

	protected void continuousIntenseSolve()
	{
		int hereStartSquaresSolved = squaresSolved;

		intenseSolve();

		while( !(hereStartSquaresSolved == squaresSolved) )
		{
			hereStartSquaresSolved = squaresSolved;

			intenseSolve();
		}

	} // continuousIntenseSolve()


	// Constructor!

	public FullSudoku(String[][] thisInput)
	{
		forNumsUseFill = thisInput;

		// Initialize Map

		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				SudokuMap[i][j] = new Square(i,j);
			}
		}

		// Fill up the RCB records of how many
		// time each number is in them

		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				rowPossPrevalence[i][j] = 9;
				colPossPrevalence[i][j] = 9;
				boxPossPrevalence[i][j] = 9;
			}
		}

		// First Fill

		Square squareWeUse;
		Integer resultOfSquare;

		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				if( !(forNumsUseFill[i][j].equals("")) )
				{
					squareWeUse = SudokuMap[i][j];
					resultOfSquare = Integer.parseInt(forNumsUseFill[i][j]);

					for(int k=0;k<squareWeUse.VARIETY;k++)
					{
						if(k != resultOfSquare - 1)
						{
							rowPossPrevalence[squareWeUse.ownRow][k]--;
							colPossPrevalence[squareWeUse.ownCol][k]--;
							boxPossPrevalence[squareWeUse.ownBox][k]--;
						}
					}

					squareWeUse.answerAtStart = true;
					squareWeUse.numPossLeft = 1;
					squareWeUse.result = resultOfSquare;
					squareWeUse.possArray = null;
					squaresSolved++;
					readyToFinalize.add(squareWeUse);				
				}
			}
		}

	} // Constructor


	// Function that checks whether a puzzle is solved properly
	// 0 is good, 1,2,3 are various errors

	public int isPuzzleComplete()
	{
		Integer[] numsInThere = new Integer[9];
		Integer currentCheck;
		int shouldYouInsertCheck;

		// You must check each row, column, and box
		// to ensure that all of them contain the
		// numbers 1 through 9

		// If at any point a row/column/box does not check out, return false
		// Otherwise, keep going

		// You need to make sure that all numbers are there, with no repeats
		// To accomplish this, three objectives:

		// 1) Accept only the numbers 1-9
		// 2) Make sure you have no duplicates
		// 3) Make sure you have no nulls
		
		
		// Objectives 1 and 3
		
		for(int x=0;x<9;x++)
		{
			for(int y=0;y<9;y++)
			{
				currentCheck = SudokuMap[x][y].result;
				
				if(currentCheck == null)
					return 3;
				
				else if(currentCheck<1 || 9<currentCheck)
					return 1;
			}
		}


		// Now for Objective 2

		// First, the rows
		// Go through loop once for each row

		for(int x=0;x<9;x++)
		{
			// Reset numsInThere array for each row

			for(int a=0;a<numsInThere.length;a++)
			{
				numsInThere[a] = null;
			}

			currentCheck = null;


			// Go through the row

			for(int b=0;b<9;b++)
			{
				// Set the number in the examined slot to currentCheck

				currentCheck = SudokuMap[x][b].result;


				// Go through the numsInThere array

				shouldYouInsertCheck = 1;

				for(int c=0;c<numsInThere.length;c++)
				{

					// If at any point currentCheck is a dupe, return false

					if(numsInThere[c] != null)
					{
						if(numsInThere[c].equals(currentCheck))  // Objective #2 complete
							return 2;
					}


					// Put currentCheck in the first null spot you find

					if(numsInThere[c] == null && shouldYouInsertCheck == 1)
					{
						numsInThere[c] = currentCheck;
						shouldYouInsertCheck = 0;
					}
				}


			} // Loop to encapsulate all squares in one row

		} // Loop to encapsulate all rows



		// Next, the columns
		// Go through loop once for each column

		for(int x=0;x<9;x++)
		{
			// Reset numsInThere array for each column

			for(int a=0;a<numsInThere.length;a++)
			{
				numsInThere[a] = null;
			}

			currentCheck = null;


			// Go through the column

			for(int b=0;b<9;b++)
			{
				// Set the number in the examined slot to currentCheck

				currentCheck = SudokuMap[b][x].result;


				// Go through the numsInThere array

				shouldYouInsertCheck = 1;

				for(int c=0;c<numsInThere.length;c++)
				{

					// If at any point currentCheck is a dupe, return false

					if(numsInThere[c] != null)
					{
						if(numsInThere[c].equals(currentCheck))  // Objective #2 complete
							return 2;
					}


					// Put currentCheck in the first null spot you find

					if(numsInThere[c] == null && shouldYouInsertCheck == 1)
					{
						numsInThere[c] = currentCheck;
						shouldYouInsertCheck = 0;
					}
				}


			} // Loop to encapsulate all squares in one column

		} // Loop to encapsulate all columns



		// Finally, the boxes
		// Go through loop once for each box

		for(int x=0;x<boxesToCheck.length;x++)
		{

			// Reset numsInThere array for each box

			for(int a=0;a<numsInThere.length;a++)
			{
				numsInThere[a] = null;
			}

			currentCheck = null;

			
			// Go through the box

			for(int y=0;y<=2;y++)
			{
				for(int z=3;z<=5;z++)
				{
					// Set the number in the examined slot to currentCheck

					currentCheck = SudokuMap[boxesToCheck[x][y]][boxesToCheck[x][z]].result;


					// Go through the numsInThere array

					shouldYouInsertCheck = 1;

					for(int c=0;c<numsInThere.length;c++)
					{

						// If at any point currentCheck is a dupe, return false

						if(numsInThere[c] != null)
						{
							if(numsInThere[c].equals(currentCheck))  // Objective #2 complete
								return 2;
						}


						// Put currentCheck in the first null spot you find

						if(numsInThere[c] == null && shouldYouInsertCheck == 1)
						{
							numsInThere[c] = currentCheck;
							shouldYouInsertCheck = 0;
						}
					}

				} // Loop to encapsulate all squares in one row in one box

			} // Loop to encapsulate all squares in one column in one box

		} // Loop to encapsulate all boxes

		return 0; // You make it this far after going through the entire puzzle

	} // isPuzzleComplete()

} // FullSudoku