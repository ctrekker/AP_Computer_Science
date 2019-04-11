//© A+ Computer Science  -  www.apluscompsci.com
//Name - Connor Burns
//Date - 8/28/17
//Class - AP Computer Science
//Lab  - Graphics: Shape


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class ShapeBurns
{
   //instance variables
	private int x;
	private int y;
	private int width;
	private int height;
	private Color color;
   private Graphics lastGraphics;

   public ShapeBurns(int x, int y, int width, int height, Color color)
   {
	   this.x = x;
		this.y = y;
      this.width = width;
      this.height = height;
      this.color = color;
   }


   public void draw(Graphics w)
   {
      Graphics2D w2=(Graphics2D)w;
      lastGraphics=w;
      
      w.setColor(color);
      w2.setStroke(new BasicStroke(3));
      w2.drawRect(x, y, width, height);
      w2.drawArc(x, y, width, height, 0, 360);
      w2.drawLine(x-10, y-10, x+width+10, y+height+10);
      w2.drawLine(x+width+10, y-10, x-10, y+height+10);
   }
   
   //BONUS
   //add in set and get methods for all instance variables
   
   // X
   public void setX(int x) 
   {
      this.x = x;
   }
   public int getX() 
   {
      return x;
   }
   // Y
   public void setY(int y) 
   {
      this.y = y;
   }
   public int getY() 
   {
      return y;
   }
   // Width
   public void setWidth(int width) 
   {
      this.width = width;
   }
   public int getWidth() 
   {
      return width;
   }
   // Height
   public void setHeight(int height) 
   {
      this.height = height;
   }
   public int getHeight() 
   {
      return height;
   }
   // Color
   public void setColor(Color color) 
   {
      this.color = color;
   }
   public Color getColor() 
   {
      return color;
   }

   // Should this have an @Override annotation, for
   // "documentation" and code readability reasons?
   public String toString()
   {
   	return x+" "+y+" "+width+" "+height+" "+color;
   }
}