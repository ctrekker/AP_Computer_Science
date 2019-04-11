import java.util.Scanner;

public class MonsterRunner {

    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        System.out.print("Enter monster 1's name: ");
        String name1=in.nextLine();
        System.out.print("Enter monster 1's size: ");
        int size1=in.nextInt();
        in.nextLine();
        System.out.print("Enter monster 2's name: ");
        String name2=in.nextLine();
        System.out.print("Enter monster 2's size: ");
        int size2=in.nextInt();
        in.nextLine();

        Monster monster1=new Skeleton(name1, size1);
        Monster monster2=new Skeleton(name2, size2);
        System.out.println();
        System.out.println("Monster 1: "+monster1);
        System.out.println("Monster 2: "+monster2);
        System.out.println();
        if(monster1.isBigger(monster2)) {
            System.out.println("Monster 1 is bigger than monster 2");
        }
        else if(monster1.isSmaller(monster2)) {
            System.out.println("Monster 2 is bigger than monster 1");
        }
        else {
            System.out.println("Monster 1 and monster 2 are the same size");
        }

        if(monster1.namesTheSame(monster2)) {
            System.out.println("Monster 1 has the same name as monster 2");
        }
        else {
            System.out.println("Monster 1 does not have the same name as monster 2");
        }
    }
}
