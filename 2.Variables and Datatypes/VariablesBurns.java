//© A+ Computer Science  -  www.apluscompsci.com
//Name - Connor Burns
//Date - 8/21/17
//Class - AP Computer Science
//Lab  - Variables and Datatypes

import java.awt.Font;
import javax.swing.*;
public class VariablesBurns
{
	public static void main ( String[] args )
	{
		//define 1 variable of each of the
		//following data types
		//byte		short		int 		long
		//float		double
		//char      boolean		String

		//integer variables
		byte byteOne = 127;
      short shortOne = 3252;
      int intOne = 23952;
      long longOne = 230759729;


		//decimal variables
      float floatOne = 324.23f;
      double doubleOne = 3.14159265359;


		//other integer types
      char charOne = 'D';
      boolean boolOne = false;


		//other types
      String stringOne = "Hello GUI!";


		//output your information here
		System.out.println("/////////////////////////////////");
		System.out.println("*Jim Bob                08/18/06*");
		System.out.println("*                               *");
		System.out.println("*        integer types          *");
		System.out.println("*                               *");
		System.out.println("*8 bit - byteOne = "+byteOne+"          *");
      System.out.println("*16 bit - shortOne = "+shortOne+"       *");
      System.out.println("*32 bit - intOne = "+intOne+"        *");
      System.out.println("*64 bit - longOne = "+longOne+"   *");
      System.out.println("*                               *");
      System.out.println("*        decimal types          *");
		System.out.println("*                               *");
      System.out.println("*32 bit - floatOne = "+floatOne+"     *");
      System.out.println("*64 bit - doubleOne = "+doubleOne+"*");
      System.out.println("*                               *");
      System.out.println("*      other integer types      *");
		System.out.println("*                               *");
      System.out.println("*16 bit - charOne = "+charOne+"           *");
      System.out.println("*                               *");
      System.out.println("*             random            *");
		System.out.println("*                               *");
      System.out.println("*1 bit - booleanOne = "+boolOne+"     *");
      System.out.println("*stringOne = "+stringOne+"         *");
      System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
      
      String[] names;
      Object[] values;
      names=new String[]{"byteOne", "shortOne", "intOne", "longOne", "floatOne", "doubleOne", "charOne", "booleanOne", "stringOne"};
      values=new Object[]{byteOne, shortOne, intOne, longOne, floatOne, doubleOne, charOne, boolOne, stringOne};
      new SwingInterface(names, values);
	}
   private static class SwingInterface extends JFrame {
      public SwingInterface(String[] names, Object values[]) {
         setTitle("Test");
         setSize(640, 480);
         setDefaultCloseOperation(EXIT_ON_CLOSE);
         JPanel container = new JPanel();
         container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
         
         JLabel topLabel=new JLabel("////////////////////////////////////");
         topLabel.setFont(new Font("monospaced", Font.PLAIN, 12));
         container.add(topLabel);
         for(int i=0; i<names.length; i++) {
            JLabel title=new JLabel();
            switch(i) {
               case 0:
                  title=new JLabel("*          integer types           *");
                  title.setFont(new Font("monospaced", Font.PLAIN, 12));
                  break;
               case 4:
                  title=new JLabel("*          decimal types           *");
                  title.setFont(new Font("monospaced", Font.PLAIN, 12));
                  break;
               case 6:
                  title=new JLabel("*       other integer types        *");
                  title.setFont(new Font("monospaced", Font.PLAIN, 12));
                  break;
               case 8:
                  title=new JLabel("*             random               *");
                  title.setFont(new Font("monospaced", Font.PLAIN, 12));
                  break;
            }
            container.add(title);
            
            String str="* "+names[i]+" = "+values[i];
            int addSpaces=36-str.length()-3;
            for(int x=0; x<addSpaces; x++) {
               str+=" ";
            }
            str+="  *";
            JLabel label=new JLabel(str);
            label.setFont(new Font("monospaced", Font.PLAIN, 12));
            container.add(label);
         }
         JLabel bottomLabel=new JLabel("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
         bottomLabel.setFont(new Font("monospaced", Font.PLAIN, 12));
         container.add(bottomLabel);
         add(container);
         
         setVisible(true);
      }
   }
}