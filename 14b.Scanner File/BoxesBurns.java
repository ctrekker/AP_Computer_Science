import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BoxesBurns {
    public BoxesBurns() {
        try {
            Scanner file=new Scanner(new File("box.dat"));
            try {
                int iterations=file.nextInt();
                char latest=' ';
                for(int i=0; i<iterations*2; i++) {
                    if(i%2==1) {
                        printBox(latest, file.nextInt());
                    }
                    else {
                        latest=file.next().charAt(0);
                    }
                }
            }
            catch(InputMismatchException e) {
                System.out.println("Your dat file is in the incorrect format! Please refer to the docs for format examples and rules.");
                System.out.println("Docs are located in docs.txt");
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("box.dat is missing!");
        }
    }

    private void printBox(char c, int levels) {
        for(int x=0; x<levels; x++) {
            for(int y=0; y<levels; y++) {
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        new BoxesBurns();
    }
}
