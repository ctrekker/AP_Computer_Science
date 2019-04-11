import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class FancyWordsBurns {
    public static void main(String[] args) {
        try {
            Scanner in=new Scanner(new File("fancywords.dat"));
            int number=Integer.parseInt(in.nextLine());
            for(int i=0; i<number; i++) {
                String sentence=in.nextLine();
                String[] parts=sentence.split(" ");
                int currentIndex=0;
                while(true) {
                    boolean any=false;
                    for(int j=parts.length-1; j>=0; j--) {
                        if(!(currentIndex>=parts[j].length())) {
                            System.out.print(parts[j].charAt(parts[j].length()-1-currentIndex));
                            any=true;
                        }
                        else {
                            System.out.print(" ");
                        }
                    }
                    System.out.println();
                    currentIndex++;
                    if(!any) break;
                }
                System.out.print("\n");
            }
        } catch(IOException e) {
            System.out.println("fancywords.dat is either missing or corrupted! Please fix this then relaunch this program!");
        }

    }
}
