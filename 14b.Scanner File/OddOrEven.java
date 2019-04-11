import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OddOrEven {
    public OddOrEven() {
        try {
            Scanner file=new Scanner(new File("oddoreven.dat"));
            try {
                int iterations=file.nextInt();
                for(int i=0; i<iterations; i++) {
                    int num=file.nextInt();
                    System.out.print(num);
                    if(num%2==0) {
                        System.out.println(" is even.");
                    }
                    else {
                        System.out.println(" is odd.");
                    }
                }
            }
            catch(InputMismatchException e) {
                System.out.println("Your dat file is in the incorrect format! Please refer to the docs for format examples and rules.");
                System.out.println("Docs are located in docs.txt");
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("oddoreven.dat is missing!");
        }
    }

    public static void main(String[] args) {
        new OddOrEven();
    }
}
