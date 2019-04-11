//© A+ Computer Science  -  www.apluscompsci.com
//Name - Connor Burns
//Date - 8/23/17
//Class - AP Computer Science
//Lab  - Inputs

//import static java.lang.System.*;
import java.util.Scanner;
import java.util.InputMismatchException;

public class InputBurns
{
	public static void main (String[] args)
	{
		Scanner keyboard = new Scanner(System.in);

		int intOne, intTwo;
		double doubleOne, doubleTwo;
		float floatOne, floatTwo;
		short shortOne, shortTwo;

      try {
         //Integers
   		System.out.print("Enter an integer :: ");
   		intOne = keyboard.nextInt();
         System.out.print("Enter another integer :: ");
   		intTwo = keyboard.nextInt();
   
   
         //Doubles
   		System.out.print("\nEnter a double :: ");
   		doubleOne = keyboard.nextDouble();
         System.out.print("Enter another double :: ");
   		doubleTwo = keyboard.nextDouble();
         
         
         //Float
   		System.out.print("\nEnter a float :: ");
   		floatOne = keyboard.nextFloat();
         System.out.print("Enter another float :: ");
   		floatTwo = keyboard.nextFloat();
         
         
         //Short
   		System.out.print("\nEnter a short :: ");
   		shortOne = keyboard.nextShort();
         System.out.print("Enter another short :: ");
   		shortTwo = keyboard.nextShort();
   
         System.out.print("\n\n");
   
   		//add in output for all variables
         String outString="";
         outString+="integer one = %s\n";
         outString+="integer two = %s\n";
         outString+="double one = %s\n";
         outString+="double two = %s\n";
         outString+="float one = %s\n";
         outString+="float two = %s\n";
         outString+="short one = %s\n";
         outString+="short two = %s\n\n";
         outString+="integer total = %s\n";
         outString+="double total = %s\n";
         outString+="float total = %s\n";
         outString+="short total = %s\n";
         
   
         System.out.printf(outString, intOne, intTwo, doubleOne, doubleTwo, floatOne, floatTwo, shortOne, shortTwo, intOne+intTwo, doubleOne+doubleTwo, floatOne+floatTwo, shortOne+shortTwo);
	   }
      catch(InputMismatchException e) {
         System.out.println("Sorry, but the number you entered was not in the appropriate format! Please restart this code and enter in proper variable values.");
      }
   }
}