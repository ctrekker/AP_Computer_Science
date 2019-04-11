import java.util.Scanner;

public class BinToTenBurns {
    public BinToTenBurns() {
        Scanner in=new Scanner(System.in);
        System.out.println("Welcome to my binary to base10 converter!");
        System.out.println("Enter a binary number to begin");
        mainloop:
        while(true) {
            System.out.print("Binary #: ");
            String line=in.nextLine();
            if(line.toLowerCase().equals("exit")) break;

            try {
                // Reverse string
                String newLine="";
                for(int i=line.length()-1; i>=0; i--) {
                    newLine+=line.charAt(i);
                }
                line=newLine;

                int out=0;
                for(int i=0; i<line.length(); i++) {
                    char bin=line.charAt(i);
                    int intBin=Integer.parseInt(Character.toString(bin));
                    if(intBin>1||intBin<0) continue mainloop;
                    if(intBin==1) out+=Math.pow(2, i);
                }
                System.out.println(line+" == "+out);
            }
            catch(Exception e) {
                continue;
            }
        }
        System.out.println("Thank you for using my binary to base10 converter!");
    }
    public static void main(String[] args) {
        new BinToTenBurns();
    }
}
