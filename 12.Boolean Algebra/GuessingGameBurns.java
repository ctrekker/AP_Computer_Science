import java.util.Scanner;

public class GuessingGameBurns {
    private final Scanner in;
    public GuessingGameBurns() {
        in=new Scanner(System.in);
    }
    public void playGame() {
        System.out.print("Guessing Game - What number would you like to go up to (inclusive) - \"exit\" to exit?: ");
        try {
            int between=in.nextInt();
            int answer=(int)(Math.random()*(between+1));
            int guess=0;
            int turns=0;
            boolean firstTime=true;
            do {
                if(!firstTime) {
                    if(guess<answer) System.out.println("Too Low!");
                    else if(guess>answer) System.out.println("Too High!");
                }
                else firstTime=false;
                System.out.print("Your guess: ");
                guess=in.nextInt();
                turns++;
            } while(answer!=guess);
            System.out.print("You got it in "+turns+" turn");
            if(turns!=1) System.out.print("s");
            System.out.println();
            System.out.printf("That means you guessed incorrectly %.2f%% of the time.\n\n", (((double)(turns-1)/turns)*100));
        }
        catch(Exception e) {
            System.out.println("Bye bye!");
            System.exit(0);
        }
    }
    public static void main(String[] args) {
        GuessingGameBurns game=new GuessingGameBurns();
        while(true) {
            game.playGame();
        }
    }
}
