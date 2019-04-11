import java.util.Arrays;

public class NumberShifterBurns {

    public static void main(String[] args) {
        System.out.println("Iterating 100 times");
        NumberShifterBurns shift=new NumberShifterBurns();
        for(int i=0; i<100; i++) shift.randomIteration();
        System.out.println("Completed");
    }

    private void randomIteration() {
        int[] original=new int[20];
        int[] shifted=new int[20];
        for(int i=0; i<original.length; i++) {
            original[i]=(int)Math.ceil(Math.random()*10);
        }
        for(int i=0; i<original.length; i++) {
            shifted[i]=original[i];
        }
        for(int i=0; i<shifted.length; i++) {
            if(shifted[i]==7) {
                for(int x=0; x<shifted.length; x++) {
                    if(shifted[x]!=7) {
                        shifted[i]=shifted[x];
                        shifted[x]=7;
                        break;
                    }
                }
            }
        }
        System.out.println(Arrays.toString(original));
        System.out.println(Arrays.toString(shifted)+"\n");
    }
}