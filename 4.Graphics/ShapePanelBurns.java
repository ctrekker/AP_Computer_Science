//© A+ Computer Science  -  www.apluscompsci.com
//Name - Connor Burns
//Date - 8/28/17
//Class - AP Computer Science
//Lab  - Graphics: Shape


import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShapePanelBurns extends JPanel
{
	public ShapePanelBurns()
	{
		setBackground(Color.WHITE);
		setVisible(true);
	}

	public void update(Graphics window)
	{
		paint(window);
	}

	/*
	 *All of your test code should be placed in paint.
	 */
	public void paint(Graphics window)
	{
		window.setColor(Color.WHITE);
		window.fillRect(0,0,getWidth(), getHeight());
		window.setColor(Color.BLACK);
		window.drawRect(20,20,getWidth()-40,getHeight()-40);
		window.setFont(new Font("TAHOMA",Font.BOLD,18));
		window.drawString("CREATE YOUR OWN SHAPE!",40,40);

      // #1
      //instantiate a Shape
      ShapeBurns shape1 = new ShapeBurns(100, 100, 150, 100, Color.BLUE);
      //tell your shape to draw
      shape1.draw(window);
		
      // #2
      //instantiate a Shape
      ShapeBurns shape2 = new ShapeBurns(600, 200, 50, 200, Color.RED);
      //tell your shape to draw
      shape2.draw(window);

      // #3
		//instantiate a Shape
      ShapeBurns shape3 = new ShapeBurns(200, 400, 150, 30, Color.GREEN);
		//tell your shape to draw
      shape3.draw(window);
	}
}