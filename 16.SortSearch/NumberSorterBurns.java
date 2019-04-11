import java.util.Scanner;

public class NumberSorterBurns {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        System.out.print("Enter a 16 bit integer: ");
        int num=in.nextInt();
        int[] split=splitDigits(num);
        split=sortDigits(split);
        printOutput(split);
    }

    private static void printOutput(int[] arr) {
        for(int i=0; i<arr.length; i++) {
            System.out.print(arr[i]+" ");
        }
        System.out.println();
    }

    private static int[] sortDigits(int[] digits) {
        int[] out=new int[digits.length];
        for(int x=0; x<digits.length; x++) {
            int min=10;
            int minI=0;
            for(int y=0; y<digits.length; y++) {
                if(digits[y]<min) {
                    min=digits[y];
                    minI=y;
                }
            }
            digits[minI]=10;

            out[x]=min;
        }
        return out;
    }

    private static int[] splitDigits(int num) {
        int first=num%10;
        int others=num/10;

        int digits=1;
        int dCount=num;
        while(dCount>9) {
            dCount/=10;
            digits++;
        }

        int[] out=new int[digits];
        out[out.length-1]=first;
        if(others<10) out[0]=others;
        else {
            int[] otherArr=splitDigits(others);
            for(int i=0; i<otherArr.length; i++) {
                out[i]=otherArr[i];
            }
        }

        return out;
    }
}
