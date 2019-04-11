import java.util.ArrayList;
import java.util.Scanner;

public class ExpressionSolverBurns {
    public ExpressionSolverBurns() {

    }
    public double solve(String equation) {
        String[] partsArr=equation.split(" ");
        ArrayList<String> parts=new ArrayList<>();
        for(String str : partsArr) {
            parts.add(str);
        }
        // Perform * and /
        while(parts.contains("*")||parts.contains("/")) {
            for (int i = 0; i < parts.size(); i++) {
                boolean hasOp = parts.get(i).equals("*") || parts.get(i).equals("/");
                if (parts.get(i).equals("*")) {
                    parts.set(i, Double.toString(Double.parseDouble(parts.get(i - 1)) * Double.parseDouble(parts.get(i + 1))));
                } else if (parts.get(i).equals("/")) {
                    parts.set(i, Double.toString(Double.parseDouble(parts.get(i - 1)) / Double.parseDouble(parts.get(i + 1))));
                }

                if (hasOp) {
                    parts.remove(i + 1);
                    parts.remove(i - 1);
                }
            }
        }

        // Perform + and -
        while(parts.contains("+")||parts.contains("-")) {
            for (int i = 0; i < parts.size(); i++) {
                boolean hasOp = parts.get(i).equals("+") || parts.get(i).equals("-");
                if (parts.get(i).equals("+")) {
                    parts.set(i, Double.toString(Double.parseDouble(parts.get(i - 1)) + Double.parseDouble(parts.get(i + 1))));
                } else if (parts.get(i).equals("-")) {
                    parts.set(i, Double.toString(Double.parseDouble(parts.get(i - 1)) - Double.parseDouble(parts.get(i + 1))));
                }

                if (hasOp) {
                    parts.remove(i + 1);
                    parts.remove(i - 1);
                }
            }
        }

        return Double.parseDouble(parts.get(0));
    }
    private boolean isNumber(String str) {
        for(int i=0; i<str.length(); i++) {
            if(str.charAt(i)!='0'&&
                    str.charAt(i)!='1'&&
                    str.charAt(i)!='2'&&
                    str.charAt(i)!='2'&&
                    str.charAt(i)!='3'&&
                    str.charAt(i)!='4'&&
                    str.charAt(i)!='5'&&
                    str.charAt(i)!='6'&&
                    str.charAt(i)!='7'&&
                    str.charAt(i)!='8'&&
                    str.charAt(i)!='9') return false;
        }
        return true;
    }
    public static void main(String[] args) {
        ExpressionSolverBurns solver=new ExpressionSolverBurns();
        Scanner in=new Scanner(System.in);
        System.out.println("Be sure to seperate each part with a space!");
        System.out.println("Type an equation, or \"exit\" to exit.");
        while(true) {
            System.out.print("> ");
            String equation=in.nextLine();
            if(equation.toLowerCase().equals("exit")) System.exit(0);
            double result=solver.solve(equation);
            System.out.println("\t== "+result);
        }
    }
}