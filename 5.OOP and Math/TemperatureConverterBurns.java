//Name - Connor Burns
//Date - 8/30/17
//Class - AP Computer Science
//Lab  - OOP/Math

/*
Use this program by simply entering a 
Fahrenheit degree when it asks, and it
will convert the given degree measurement
to Celcius.

To end the program, type "Quit"
-- Technically, you can type in any non-double
-- value to end the program (uses try-catch)
*/

import java.util.ArrayList;
import java.util.Scanner;
public class TemperatureConverterBurns 
{
   public TemperatureConverterBurns()
   {
      
   }
   public void askPrompt() 
   {
      Scanner in = new Scanner(System.in);
      
      System.out.print("Enter a temperature in Fahrenheit (type \"quit\" to stop): ");
      double temp = in.nextDouble();
      printOutput(temp, fToC(temp));
   }
   // Fahrenheit to Celsius conversion method
   private double fToC(double tempF)
   {
      return (((tempF-32)*5)/9);
   }
   private void printOutput(double tempF, double tempC)
   {
      System.out.printf("%.2f degrees Fahrenheit == %.2f degrees Celsius\n", tempF, tempC);
   }
   
   
   public static void main(String[] args)
   {
      TemperatureConverterBurns converter = new TemperatureConverterBurns();
      while(true) {
         try {
            converter.askPrompt();
         }
         catch(Exception e) {
            System.out.println("Thank you for using the Fahrenheit to Celsius converter!");
            System.exit(0);
         }
      }
   }
}