import java.util.Scanner;

public class BurnsMultiplicationTable {
    public BurnsMultiplicationTable() {
        Scanner in = new Scanner(System.in);
        System.out.println("Sample data:");
        System.out.println("\tPut the number you want to multiply for first");
        System.out.println("\tThen put a space");
        System.out.println("\tThen put the number you want to multiply the first number up to");
        System.out.println("To exit, type \"exit\"");
        while(true) {
            System.out.print("Enter a sample data value: ");
            String line=in.nextLine();
            if(line.toLowerCase().equals("exit")) break;
            try {
                int tableNum=Integer.parseInt(line.split(" ")[0]);
                int multBy=Integer.parseInt(line.split(" ")[1]);

                printMultiplicationTable(tableNum, multBy);
            }
            catch(Exception e) {

            }
        }
        System.out.println("Thank you for using my multiplication table program!");
    }

    private void printMultiplicationTable(int tableNum, int multBy) {
        System.out.println("Multiplication table for "+tableNum);
        for(int i=1; i<=multBy; i++) {
            System.out.println(i+"     "+(tableNum*i));
        }
        System.out.print("\n\n");
    }

    public static void main(String[] args) {
        new BurnsMultiplicationTable();
    }
}
