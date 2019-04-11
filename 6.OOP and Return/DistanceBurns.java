import java.util.Scanner;
public class DistanceBurns {
   private Scanner in;
   public DistanceBurns() {
      in = new Scanner(System.in);
   }
   public void executeUserInputs() throws Exception {
      System.out.print("Enter X1 :: ");
      double x1=in.nextInt();
      System.out.print("Enter Y1 :: ");
      double y1=in.nextInt();
      System.out.print("Enter X2 :: ");
      double x2=in.nextInt();
      System.out.print("Enter Y2 :: ");
      double y2=in.nextInt();
      
      double distance=Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
      System.out.printf("distance == %.3f\n\n", distance);
   }
   
   
   
   public static void main(String[] args) {
      DistanceBurns program = new DistanceBurns();
      
      while(true) {
         try {
            program.executeUserInputs();
         }
         catch(Exception e) {
            break;
         }
      }
   }
}