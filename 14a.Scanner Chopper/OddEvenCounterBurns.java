import java.util.Scanner;

public class OddEvenCounterBurns {
    public final Scanner in;
    public OddEvenCounterBurns() {
        in=new Scanner(System.in);

        System.out.println("Enter a sequence of numbers seperated by a space (\"exit\" to exit)");
        while(true) {
            System.out.print("=> ");
            int evenCount=0;
            int oddCount=0;
            String input=in.nextLine();
            if(input.toLowerCase().equals("exit")) {
                System.exit(0);
            }
            Scanner scan=new Scanner(input);
            while(scan.hasNextInt()) {
                int next=scan.nextInt();
                if(next%2==0) evenCount++;
                else oddCount++;
            }
            System.out.println("Even count = "+evenCount);
            System.out.println("Odd count = "+oddCount);
        }
    }
    public static void main(String[] args) {
        new OddEvenCounterBurns();
    }
}
