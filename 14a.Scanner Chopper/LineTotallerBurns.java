import java.util.Scanner;

public class LineTotallerBurns {
    public final Scanner in;
    public LineTotallerBurns() {
        in=new Scanner(System.in);

        System.out.println("Enter a sequence of numbers seperated by a space (\"exit\" to exit)");
        while(true) {
            System.out.print("=> ");
            int count=0;
            String input=in.nextLine();
            if(input.toLowerCase().equals("exit")) {
                System.exit(0);
            }
            Scanner scan=new Scanner(input);
            while(scan.hasNextInt()) {
                count+=scan.nextInt();
            }
            System.out.println("Sum = "+count);
        }
    }
    public static void main(String[] args) {
        new LineTotallerBurns();
    }
}
