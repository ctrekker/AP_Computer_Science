import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MatrixImageBurns extends JFrame {
    private MatrixImageBurns _this;
    public MatrixImageBurns() {
        setTitle("Matrix Image");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        _this=this;
    }
    public void run() {
        add(new GraphicsManager());

        setVisible(true);

    }
    public static void main(String[] args) {
        new MatrixImageBurns().run();
    }

    private class GraphicsManager extends Component {
        @Override
        public void paint(Graphics g) {
            Scanner fileIn=null;
            try {
                fileIn=new Scanner(new File("image.dat"));
            }
            catch(IOException e) {}

            if(fileIn!=null) {
                int width = Integer.parseInt(fileIn.next());
                int height = Integer.parseInt(fileIn.next());
                System.out.println("Size: ("+width+","+height+")");
                _this.setSize(width, height);
                fileIn.nextLine();

                while(fileIn.hasNext()) {
                    int yPos=fileIn.nextInt();
                    int xPos=fileIn.nextInt();
                    int color=fileIn.nextInt();
                    Color colorObj=Color.BLACK;
                    switch(color) {
                        case 2:
                            colorObj=Color.RED;
                            break;
                        case 4:
                            colorObj=Color.YELLOW;
                            break;
                        case 6:
                            colorObj=Color.YELLOW;
                            break;
                        case 8:
                            colorObj=Color.GREEN;
                            break;
                        case 10:
                            colorObj=Color.BLUE;
                            break;
                    }

                    g.setColor(colorObj);
                    g.fillRect(xPos, yPos, 10, 10);

                    System.out.println("x: "+xPos+", y: "+yPos+", color: "+color);
                }
            }
        }
    }
}
