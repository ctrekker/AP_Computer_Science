//© A+ Computer Science  -  www.apluscompsci.com
//Name - Connor Burns
//Date - 8/28/17
//Class - AP Computer Science
//Lab  - Graphics: Shape

import javax.swing.JFrame;

public class GraphicsRunnerBurns extends JFrame
{
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	public GraphicsRunnerBurns()
	{
		super("Graphics Runner");

		setSize(WIDTH,HEIGHT);

		getContentPane().add(new ShapePanelBurns());
		
		//add other classes to run them 
		//BigHouse, Robot, or ShapePanel 

		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main( String args[] )
	{
		GraphicsRunnerBurns run = new GraphicsRunnerBurns();
	}
}