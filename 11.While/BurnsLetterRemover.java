import java.util.Scanner;

public class BurnsLetterRemover {
    private final Scanner in;
    private boolean firstTime=true;

    public static void main(String[] args) {
        BurnsLetterRemover program = new BurnsLetterRemover();
        while(true) {
            program.nextLoop();
        }
    }

    public BurnsLetterRemover() {
        this.in=new Scanner(System.in);
    }

    private void nextLoop() {
        if(firstTime) {
            System.out.println("As it is shown in the document, type an input string, followed by 3 spaces, then the desired character you want to replace");
            System.out.println("Please note that character replacement is case sensitive. To change that, run this program with option \"false\"");
            System.out.println();
            firstTime=false;
        }
        System.out.println("Enter a valid input string or \"exit\" to exit:");
        String userInput=in.nextLine();
        if(userInput.equals("exit")) System.exit(0);
        String[] userParts=userInput.split(" {3}");
        if(userParts.length!=2) System.out.println("Invalid input format.\nValid input is the desired string, followed by 3 spaces, then the letter you want to remove.");

        String input=userParts[0];
        String replaceLetter=userParts[1];
        while(input.contains(replaceLetter)) {
            input=input.replace(replaceLetter, "");
        }
        System.out.println("Output:");
        System.out.println(input+"\n");
    }
}
