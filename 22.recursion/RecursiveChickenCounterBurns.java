import java.util.Scanner;

public class RecursiveChickenCounterBurns {
    private static String[] examples={
            "itatfun",
            "itatchickenfun",
            "chchickchickenenicken",
            "chickchickfun",
            "chickenbouncetheballchicken"
    };
    public int countChickens(String input) {
        int out=0;
        input=input.toLowerCase();
        if(input.contains("chicken")) {
            out++;
            input=input.replaceFirst("chicken", "");
            out+=countChickens(input);
        }
        return out;
    }
    public static void main(String[] args) {
        RecursiveChickenCounterBurns counter=new RecursiveChickenCounterBurns();
        System.out.println("Type \"stop\" to end this program");
        System.out.println("Enter example1, example2, etc. for example strings");
        System.out.println("There are "+examples.length+" examples");
        System.out.println();
        Scanner in=new Scanner(System.in);
        while(true) {
            System.out.print("Enter a command :: ");
            String inputChicken=in.nextLine();
            if(inputChicken.toLowerCase().equals("stop")) System.exit(0);
            if(inputChicken.substring(0, 7).equals("example")) {
                int exampleIndex=Integer.parseInt(inputChicken.substring(7, inputChicken.length()))-1;
                if(exampleIndex>examples.length) {
                    System.out.println("That example does not exist!");
                }
                else {
                    System.out.println("Enter a command :: "+examples[exampleIndex]);
                    System.out.println("There are "+counter.countChickens(examples[exampleIndex])+" in that string");
                }
            }
            else {
                System.out.println("There are "+counter.countChickens(inputChicken)+" in that string");
            }
            System.out.println();
        }
    }
}