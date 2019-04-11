import java.util.*;
import java.io.*; 

public class MatrixSummingBurns
{
    private int[][] m = {{1,2,3,4,5},
                         {6,7,8,9,0},
                         {6,7,1,2,5},
                         {6,7,8,9,0},
                         {5,4,3,2,1}}; //load in the matrix values

    public void run(String fileName) {
        System.out.println("Matrix Values");
        for(int x=0; x<m.length; x++) {
            for(int y=0; y<m[x].length; y++) {
                System.out.print(m[x][y]+"  ");
            }
            System.out.println();
        }
        System.out.println();

        Scanner fileIn=null;
        try {
            fileIn=new Scanner(new File(fileName));
        } catch(IOException e) {

        }

        if(fileIn!=null) {
            int iterations=Integer.parseInt(fileIn.nextLine());
            for(int i=0; i<iterations; i++) {
                int xCoord=Integer.parseInt(fileIn.next());
                int yCoord=Integer.parseInt(fileIn.next());
                int sum=0;
                for(int x=xCoord-1; x<=xCoord+1; x++) {
                    for(int y=yCoord-1; y<=yCoord+1; y++) {
                        try {
                            sum+=m[x][y];
                        }
                        catch(IndexOutOfBoundsException e) {} // Do nothing if we go over the edge
                    }
                }
                System.out.println("The sum of "+xCoord+","+yCoord+" is "+sum+".");
            }
        }
        else {
            System.out.println("Error performing file reader init");
        }
    }
    public static void main(String[] args) {
        MatrixSummingBurns sum=new MatrixSummingBurns();
        sum.run("matsum.dat");
    }
}