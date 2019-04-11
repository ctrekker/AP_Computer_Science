import java.util.Scanner;

public class FibonacciBurns {
    public FibonacciBurns() {
        Scanner in=new Scanner(System.in);
        System.out.println("Enter a number to get its corresponding number in the fibonacci sequence, or \"exit\" to exit.");
        while(true) {
            System.out.print("=> ");
            String line=in.nextLine();
            try {
                if (line.toLowerCase().equals("exit")) System.exit(0);
                else if (!line.equals("")) {
                    int num = Integer.parseInt(line);
                    if(num>100) {
                        System.out.println("Numbers over 100 start to overflow the long maximum!");
                        System.out.println("It doesn't seem that high, but due to fibonacci's extreme exponential");
                        System.out.println("nature, the numbers increase rapidly.");
                    }
                    else System.out.println("fibonacci[" + line + "] = " + doFibo(num - 1));
                }
            } catch(NumberFormatException e) {
                System.out.println("Invalid number");
            }
        }
    }

    private long doFibo(int num) {
        long current=1;
        long previous=1;
        for(int i=1; i<num; i++) {
            long tmp=previous;
            previous=current;
            current+=tmp;
        }
        return current;
    }

    public static void main(String[] args) {
        new FibonacciBurns();
    }
}
