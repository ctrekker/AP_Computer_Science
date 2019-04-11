import java.util.Scanner;
public class BurnsNumGuess {
   private int guesses=0;
   private int correct;
   public BurnsNumGuess() {
      correct=(int)Math.ceil(Math.random()*100);
   }
   public int getGuesses() {
      return guesses;
   }
   public int nextGuess(int guess) {
      guesses++;
      if(guess==correct) return 0;
      else if(guess<correct) return -1;
      else return 1;
   }
   
   public static void main(String[] args) {
      BurnsNumGuess app = new BurnsNumGuess();
      Scanner in = new Scanner(System.in);
      while(true) {
         System.out.print("What is your guess? ");
         int guess=in.nextInt();
         switch(app.nextGuess(guess)) {
            case 0:
               System.out.println("You guessed it in "+app.getGuesses()+" moves!");
               System.exit(0);
            case 1:
               System.out.println("Too high!");
               break;
            case -1:
               System.out.println("Too low!");
               break;
         }
      }
   }
}