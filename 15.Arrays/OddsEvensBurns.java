import java.util.ArrayList;
import java.util.Scanner;

public class OddsEvensBurns {
    public OddsEvensBurns() {
        Scanner in=new Scanner(System.in);
        System.out.println("Enter a sequence of numbers, or \"exit\" to exit.");
        while(true) {
            System.out.print("=> ");
            String line=in.nextLine();
            if(line.toLowerCase().equals("exit")) System.exit(0);
            doSequence(line);
        }
    }

    private void doSequence(String line) {
        Scanner nums=new Scanner(line);
        ArrayList<Integer> even=new ArrayList<>();
        ArrayList<Integer> odd=new ArrayList<>();
        while(nums.hasNextInt()) {
            int num=nums.nextInt();
            if(num%2==0) even.add(num);
            else odd.add(num);
        }
        System.out.print("Odds - ");
        printArray(odd);
        System.out.print("Evens - ");
        printArray(even);
        System.out.println();
    }

    private void printArray(ArrayList<Integer> list) {
        System.out.print("[");
        boolean first=true;
        for(int i : list) {
            if (!first) {
                System.out.print(", "+i);
            }
            else {
                System.out.print(i);
                first=false;
            }
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        new OddsEvensBurns();
    }
}