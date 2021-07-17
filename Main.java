import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main
{
	private static DrawNumsConstructor theBigFrame;

	private static void callConstructor()
	{
		EventQueue.invokeLater(

		new Runnable()
		{
			@Override
			public void run()
			{
				theBigFrame = new DrawNumsConstructor();
			}
		});
	}


	// Main simply calls the above function to bring the
	// JFrame constructor into the event dispatch thread

	public static void main(String[] argv)
	{
		callConstructor();
	}

} // Main class