import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChipBank {
    // Defines constants for startup
    public final static int DEFAULT_CHIPS_100=1;
    public final static int DEFAULT_CHIPS_50=1;
    public final static int DEFAULT_CHIPS_25=3;
    public final static int DEFAULT_CHIPS_5=3;
    public final static int DEFAULT_CHIPS_1=10;

    // Total "value"
    // Must be recalculated upon chip update
    private int total;
    // Chips with associated value
    private int c100;
    private int c50;
    private int c25;
    private int c5;
    private int c1;
    // Init the bank with all default values
    public ChipBank() {
        this(DEFAULT_CHIPS_100, DEFAULT_CHIPS_50, DEFAULT_CHIPS_25, DEFAULT_CHIPS_5, DEFAULT_CHIPS_1);
    }
    // Init the bank with custom values
    public ChipBank(int c100, int c50, int c25, int c5, int c1) {
        this.c100=c100;
        this.c50=c50;
        this.c25=c25;
        this.c5=c5;
        this.c1=c1;

        // If the chip images have not been set yet, then make sure to load them.
        // The images are static, because they do not change from instance to instance.
        // This saves a significant amount of memory (in RAM) as a result.
        if(IMG_C100==null) {
            // Attempt to load
            try {
                System.out.println("Loading chip images...");
                IMG_C100 = ImageIO.read(new File(GameLauncher.RESOURCE_ROOT+"c100.png"));
                IMG_C50  = ImageIO.read(new File(GameLauncher.RESOURCE_ROOT+"c50.png"));
                IMG_C25  = ImageIO.read(new File(GameLauncher.RESOURCE_ROOT+"c25.png"));
                IMG_C5   = ImageIO.read(new File(GameLauncher.RESOURCE_ROOT+"c5.png"));
                IMG_C1   = ImageIO.read(new File(GameLauncher.RESOURCE_ROOT+"c1.png"));
                System.out.println("Done loading chip images");
            }
            // Alert user through debug console of a potentially missing image
            // Then, print stack trace
            catch(IOException e) {
                System.out.println("MISSING IMAGE!!!");
                System.out.println("Printing stack trace:");
                e.printStackTrace();
            }
        }
    }

    // Setters for chips
    public void setChips100(int n) {
        c100=n;
    }
    public void setChips50(int n) {
        c50=n;
    }
    public void setChips25(int n) {
        c25=n;
    }
    public void setChips5(int n) {
        c5=n;
    }
    public void setChips1(int n) {
        c1=n;
    }

    // Adders for chips
    public void addChips100(int a) {
        c100+=a;
    }
    public void addChips50(int a) {
        c50+=a;
    }
    public void addChips25(int a) {
        c25+=a;
    }
    public void addChips5(int a) {
        c5+=a;
    }
    public void addChips1(int a) {
        c1+=a;
    }

    // Getters for chips
    public int getChips100() {
        return c100;
    }
    public int getChips50() {
        return c50;
    }
    public int getChips25() {
        return c25;
    }
    public int getChips5() {
        return c5;
    }
    public int getChips1() {
        return c1;
    }
    // Recalculates the total
    public void calculateTotal() {
        total=c100*100+c50*50+c25*25+c5*5+c1;
    }

    // Simplifies the chips into the greatest values they can be represented in
    // For examples, if you have a total of 56 chips, you don't want 56 one chips.
    // You would want a single fifty chip, a single five chip, and a single one chip.
    public void simplify() {
        // t=take
        // r=remaining
        // take amounts
        int t100=getTotal()/100;
        int r100=getTotal()%100;
        int t50=r100/50;
        int r50=r100%50;
        int t25=r50/25;
        int r25=r50%25;
        int t5=r25/5;
        int r5=r25%5;
        int t1=r5/1; // /1 is redundant, but for consistency it is included

        // Set the new values calculated using truncation and modulus
        c100=t100;
        c50=t50;
        c25=t25;
        c5=t5;
        c1=t1;
    }
    // Transfer a specified amount to a provided other ChipBank
    public void transferTo(ChipBank other, int amount) {
        // Subtract the needed chips from the other ChipBank using subtract() method'
        other.add(amount);

        // Add taken values from other chip bank to this chip bank
        subtract(amount);
        // Then simplify into larger chips
        simplify();
    }
    // Subtract an amount from the chip bank
    public void subtract(int amount) {
        int preTotal=getTotal();
        reset();
        c1=preTotal-amount;
        simplify();
    }
    // Add an amount to the chip bank
    public void add(int amount) {
        int preTotal=getTotal();
        reset();
        c1=preTotal+amount;
        simplify();
    }

    /**
     * ALWAYS use this method for getting the total, even if it's within the class itself
     * because recalculation MUST occur
     * @return The total value of chips
     */
    public int getTotal() {
        // Make sure it's updated
        calculateTotal();
        return total;
    }
    // Reset all values to zeros
    public void reset() {
        c100=0;
        c50=0;
        c25=0;
        c5=0;
        c1=0;
    }
    // Converts a JSONObject response from the server into a ChipBank object that Java can use more effectively
    public void fromJSON(JSONObject obj) {
        // Reset first, just in case
        reset();

        // Set the values
        c1=obj.getInt("c1");
        c5=obj.getInt("c5");
        c25=obj.getInt("c25");
        c50=obj.getInt("c50");
        c100=obj.getInt("c100");
    }

    // Static image declarations
    // They are so low because they are only used below
    public static BufferedImage IMG_C100 = null;
    public static BufferedImage IMG_C50  = null;
    public static BufferedImage IMG_C25  = null;
    public static BufferedImage IMG_C5   = null;
    public static BufferedImage IMG_C1   = null;
    // Other constants for drawing the chips
    public static int CHIP_SIZE = 16;
    public static Font CHIP_FONT = new Font("Arial", Font.PLAIN, 10);
    public static int TEXT_GAP = 5;

    /**
     * Draws a visual representation of the chips on the specified graphics object
     * NOTE: only draws correctly on GameGUI graphics object
     * @param x Primary x position of chips
     * @param y Primary y position of chips
     * @param offsetX Either -1, 0, or 1. Represents chip orientation offset X
     * @param offsetY Either -1, 0, or 1. Represents chip orientation offset Y
     * @param g2 Desired graphics object
     */
    public void draw(int x, int y, double offsetX, double offsetY, Graphics2D g2) {
        GameGUI.GameGraphics gameGraphics=GameLauncher.manager.gui.gameGraphics;

        /*
        This is an extremely complex method which is capable of dynamically drawing the chips
        at any calculated angle, which is a lot harder than it sounds given only Cartesian-based coordinates
         */

        // Real Offsets
        int roX=(int)offsetX;
        int roY=(int)offsetY;
        if(offsetX==0) roX=1;
        if(offsetY==0) roY=1;

        // Anchor adjustment
        int anchorX=0;
        int anchorY=0;
        if(offsetX<0) anchorX=-CHIP_SIZE;
        if(offsetY<0) anchorY=-CHIP_SIZE;

        // Ratio of Change
        double rcX=1;
        double rcY=1;

        // Adjust ratio of change depending on orientation
        if(Math.abs(offsetX)+Math.abs(offsetY)==1) {
            if(Math.abs(offsetX)==1) {
                rcY=1/5.0;
            }
            if(Math.abs(offsetY)==1) {
                rcX=1/5.0;
            }
        }
        else if(Math.abs(offsetX)+Math.abs(offsetY)==2) {
            offsetX*=0.8;
            offsetY*=0.8;
        }

        // Calculate the real x and y values of the ChipBank
        // Since the given coordinates are centered, we need to calculate the real ones that can be used on a Graphics2D object
        int realX = (int)((x - ((CHIP_SIZE * 5 * rcX)*roX) / 2)+(CHIP_SIZE*5*0.8)*(Math.signum(offsetX)-offsetX)-(CHIP_SIZE*1.25)*(Math.signum(offsetX)-offsetX));
        int realY = (int)((y - ((CHIP_SIZE * 5 * rcY)*roY) / 2)+(CHIP_SIZE*5*0.8)*(Math.signum(offsetY)-offsetY)-(CHIP_SIZE*1.25)*(Math.signum(offsetY)-offsetY));

        // I'm adding zero here just to make it look better in IntelliJ. I do know that its redundant. :)
        // Other than that, below are just a ton of simple things that come together and end up looking absolutely mental
        g2.drawImage(IMG_C100, (int)(anchorX+realX),                     (int)(anchorY+(realY)),                     CHIP_SIZE, CHIP_SIZE, null);
        g2.drawImage(IMG_C50,  (int)(anchorX+realX+CHIP_SIZE*offsetX),   (int)(anchorY+(realY+CHIP_SIZE*offsetY)),   CHIP_SIZE, CHIP_SIZE, null);
        g2.drawImage(IMG_C25,  (int)(anchorX+realX+CHIP_SIZE*2*offsetX), (int)(anchorY+(realY+CHIP_SIZE*offsetY*2)), CHIP_SIZE, CHIP_SIZE, null);
        g2.drawImage(IMG_C5,   (int)(anchorX+realX+CHIP_SIZE*3*offsetX), (int)(anchorY+(realY+CHIP_SIZE*offsetY*3)), CHIP_SIZE, CHIP_SIZE, null);
        g2.drawImage(IMG_C1,   (int)(anchorX+realX+CHIP_SIZE*4*offsetX), (int)(anchorY+(realY+CHIP_SIZE*offsetY*4)), CHIP_SIZE, CHIP_SIZE, null);

        // More mental amounts of addition and multiplication to correctly position each and every chip label
        g2.setColor(Color.WHITE);
        gameGraphics.drawCenteredString(c100+"", (int)((anchorX+realX)                    +CHIP_SIZE/2+CHIP_SIZE*offsetY), (int)((anchorY+(realY))                    +CHIP_SIZE/2-CHIP_SIZE*offsetX), CHIP_FONT, g2);
        gameGraphics.drawCenteredString(c50+"",  (int)((anchorX+realX+CHIP_SIZE*offsetX)  +CHIP_SIZE/2+CHIP_SIZE*offsetY), (int)((anchorY+(realY+CHIP_SIZE*offsetY))  +CHIP_SIZE/2-CHIP_SIZE*offsetX), CHIP_FONT, g2);
        gameGraphics.drawCenteredString(c25+"",  (int)((anchorX+realX+CHIP_SIZE*2*offsetX)+CHIP_SIZE/2+CHIP_SIZE*offsetY), (int)((anchorY+(realY+CHIP_SIZE*offsetY*2))+CHIP_SIZE/2-CHIP_SIZE*offsetX), CHIP_FONT, g2);
        gameGraphics.drawCenteredString(c5+"",   (int)((anchorX+realX+CHIP_SIZE*3*offsetX)+CHIP_SIZE/2+CHIP_SIZE*offsetY), (int)((anchorY+(realY+CHIP_SIZE*offsetY*3))+CHIP_SIZE/2-CHIP_SIZE*offsetX), CHIP_FONT, g2);
        gameGraphics.drawCenteredString(c1+"",   (int)((anchorX+realX+CHIP_SIZE*4*offsetX)+CHIP_SIZE/2+CHIP_SIZE*offsetY), (int)((anchorY+(realY+CHIP_SIZE*offsetY*4))+CHIP_SIZE/2-CHIP_SIZE*offsetX), CHIP_FONT, g2);
        g2.setColor(Color.BLACK);
    }
    // Overridden toString for debugging
    @Override
    public String toString() {
        return "ChipBank{total="+getTotal()+"}";
    }
}
