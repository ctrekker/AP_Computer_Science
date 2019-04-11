/*
Connor Burns
9/18/17
Period 7
*/

import java.util.Scanner;
public class BurnsSocialSecurity {
   public BurnsSocialSecurity() {
      
   }
   public int run(String ss) {
      String[] stringArr=ss.split("-");
      int out=0;
      for(int i=0; i<stringArr.length; i++) {
         out+=Integer.parseInt(stringArr[i]);
      }
      return out;
   }  
   
   
   public static void main(String[] args) {
      Scanner in=new Scanner(System.in);
      BurnsSocialSecurity sec = new BurnsSocialSecurity();
      System.out.println("Type \"exit\" to exit the program.");
      while(true) {
         try {
            System.out.print("Enter an example social security number: ");
            String ss=in.nextLine();
            if(ss.toLowerCase().equals("exit")) break;
            System.out.println("SS# "+ss+"has a total of "+sec.run(ss));
         }
         catch(Exception e) {
            System.out.println("Sorry, but that is not a valid input!");
            continue;
         }
      }
      System.out.println("Thank you for using the Social Security number adder!");
   }
}