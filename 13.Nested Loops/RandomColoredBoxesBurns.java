import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class RandomColoredBoxesBurns extends JFrame {
    private final Timer timer;
    private final RandomColoredBoxesGraphics graphicsComponent;
    private int delay;
    private final static short FRAME_WIDTH = 500;
    private final static short FRAME_HEIGHT = 500;
    private final static short SQUARES = 20;
    private final static short SQUARE_SIZE = FRAME_WIDTH/SQUARES;

    public RandomColoredBoxesBurns(int millis) {
        this.delay=millis;
        setTitle("Random Colored Boxes!");
        setPreferredSize(new Dimension(FRAME_WIDTH+SQUARE_SIZE*1/4, FRAME_HEIGHT+SQUARE_SIZE+SQUARE_SIZE/6));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.graphicsComponent =new RandomColoredBoxesGraphics();
        add(graphicsComponent);
        pack();
        setVisible(true);

        this.timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                graphicsComponent.repaint();
            }
        }, 0, millis);
    }
    public static void main(String[] args) {
	    new RandomColoredBoxesBurns(1000);
    }

    private class RandomColoredBoxesGraphics extends Component {
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            for(int x=0; x<SQUARES; x++) {
                for(int y=0; y<SQUARES; y++) {
                    g2.setColor(getRandomColor());
                    g2.fillRect(x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                }
            }
        }
        private Color getRandomColor() {
            return new Color((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256));
        }
    }
}
