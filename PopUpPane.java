import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class PopUpPane
{
	private DrawNumsConstructor parentFrame;


	// Constructor

	public PopUpPane(DrawNumsConstructor a)
	{
		parentFrame = a;
	}


	// Gives the string for the Sudoku currently entered

	public void outputTheString(String theString)
	{
		JOptionPane theWindow = new JOptionPane();

		JTextArea theText = new JTextArea();

		theText.setText(theString);
		theText.setEditable(false);
		theText.setEnabled(true);

		theWindow.showMessageDialog(parentFrame,theText,"String Output",JOptionPane.PLAIN_MESSAGE);
	}


	// Allows user to input a string to quickly enter a Sudoku

	public String getString()
	{
		JOptionPane theWindow = new JOptionPane();

		String theMessage = "                    Input string obtained from Save Sudoku                    ";

		return theWindow.showInputDialog(parentFrame,theMessage,"String Input",JOptionPane.PLAIN_MESSAGE);
	}


	public void showInsertedDupeMessage()
	{
		JOptionPane theWindow = new JOptionPane();

		String dupeMessage = "You have entered duplicates within a row, column, or box.";

		theWindow.showMessageDialog(parentFrame,dupeMessage,"Solve Error",JOptionPane.ERROR_MESSAGE);
	}


	public void showNotEnoughCluesMessage()
	{
		JOptionPane theWindow = new JOptionPane();

		String clueMessage = "You must insert at least 17 numbers total.";

		theWindow.showMessageDialog(parentFrame,clueMessage,"Solve Error",JOptionPane.ERROR_MESSAGE);
	}


	public void showNoSolutionMessage()
	{
		JOptionPane theWindow = new JOptionPane();

		String noSolutionMessage = "This puzzle has no solution.";

		theWindow.showMessageDialog(parentFrame,noSolutionMessage,"Solve Error",JOptionPane.ERROR_MESSAGE);
	}

	public void showItBrokeMessage()
	{
		JOptionPane theWindow = new JOptionPane();

		String itBrokeMessage = "Solving machine broke.";

		theWindow.showMessageDialog(parentFrame,itBrokeMessage,"Solve Error",JOptionPane.ERROR_MESSAGE);
	}


	// Error message for invalid String

	public void showErrorMessage(int errorCode)
	{
		JOptionPane theWindow = new JOptionPane();

		String errorMessage = "Error.";

		if(errorCode == 1)
			errorMessage = "String contains invalid characters.";
		if(errorCode == 2)
			errorMessage = "Invalid string. Not all 81 squares are accounted for.";
		if(errorCode == 3)
			errorMessage = "Invalid string. More than 81 squares are accounted for.";

		theWindow.showMessageDialog(parentFrame,errorMessage,"Load Error",JOptionPane.ERROR_MESSAGE);
	}

} // PopUpPane class