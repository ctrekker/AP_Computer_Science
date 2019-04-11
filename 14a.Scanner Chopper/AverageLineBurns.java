import java.util.Scanner;

public class AverageLineBurns {
    public final Scanner in;
    public AverageLineBurns() {
        in=new Scanner(System.in);

        System.out.println("Enter a sequence of numbers seperated by a space (\"exit\" to exit)");
        while(true) {
            System.out.print("=> ");
            int count=0;
            int total=0;
            String input=in.nextLine();
            if(input.toLowerCase().equals("exit")) {
                System.exit(0);
            }
            Scanner scan=new Scanner(input);
            while(scan.hasNextInt()) {
                total+=scan.nextInt();
                count++;
            }
            System.out.println("Average = "+String.format("%.3f", ((double)total/count)));
        }
    }
    public static void main(String[] args) {
        new AverageLineBurns();
    }
}
