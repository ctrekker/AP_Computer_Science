import java.util.Scanner;

public class LineBreakerBurns {
    public final Scanner in;
    public LineBreakerBurns() {
        in=new Scanner(System.in);

        System.out.println("Enter a sequence of numbers seperated by a space (\"exit\" to exit)");
        int it=0;
        while(true) {
            System.out.print("Enter sequence => ");
            int count=0;
            if(it>0) in.nextLine();
            String input=in.nextLine();
            if(input.toLowerCase().equals("exit")) {
                System.exit(0);
            }
            Scanner scan=new Scanner(input);

            System.out.print("Enter line break length => ");
            int breakLen=in.nextInt();
            if(breakLen<=0) {
                System.out.println("That would result in an infinite loop!");
                continue;
            }
            int currentLen=0;
            while(scan.hasNext()) {
                if(currentLen>=breakLen) {
                    System.out.println();
                    currentLen=0;
                }
                System.out.print(scan.next());
                currentLen++;
            }
            System.out.println();
            it++;
        }
    }
    public static void main(String[] args) {
        new LineBreakerBurns();
    }
}
