import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

// This is the main GUI class which handles all game graphics
public class GameGUI extends JFrame {
    // Standardized font for frame
    public final static Font STANDARD_FONT=new Font("Arial", Font.PLAIN, 14);

    // Defines constants which should scale the image properly
    private final static int CHIP_SPACE_X=190;
    private final static int CHIP_SPACE_Y=100;
    private final static int CARD_SPACE_X=80;
    private final static int CARD_SPACE_Y=50;
    private final static int CHIP_INDEX_BASE_Y=100;

    // Defines constants for relative positioning ratios
    private final static double LEFT=0.1;
    private final static double CENTER=0.5;
    private final static double RIGHT=0.9;
    private final static double TOP=0.2;
    private final static double BOTTOM=0.8;

    /*
    An array of ratios which players should be located at (data)
    X   -> X ratio
    Y   -> Y ratio
    COX -> Chip Offset X
    COY -> Chip Offset Y
     */
    private final static double[][] playerSpots={
            // X      Y     COX  COY
            {CENTER, TOP,     1,  0},
            {RIGHT,  TOP,     1,  1},
            {RIGHT,  CENTER,  0,  1},
            {RIGHT,  BOTTOM, -1,  1},
            {CENTER, BOTTOM, -1,  0},
            {LEFT,   BOTTOM, -1, -1},
            {LEFT,   CENTER,  0, -1},
            {LEFT,   TOP,     1, -1}
    };
    // Game class which stores info about game
    public TexasHoldem game;
    // Graphics class of the game
    public GameGraphics gameGraphics;
    public GameGUI(TexasHoldem game) {
        this.game=game;

        // Attempt to set a fancy "T H" icon for windows and macs
        // If it fails, it will not hurt the program
        try {
            setIconImage(ImageIO.read(new File(GameLauncher.RESOURCE_ROOT+"icon.png")));
        }
        catch(IOException e) {
            System.out.println("Error loading window icon");
            System.out.println("Continuing without asset...");
        }
        // Set window stuff
        setTitle("Texas Hold'em!");
        setSize(640, 420);

        // Set other window stuff
        setUndecorated(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a new instance of GameGraphics, and add it to the JFrame
        this.gameGraphics=new GameGraphics();
        add(gameGraphics);

        // Set the frame to visible
        setVisible(true);
    }
    /*
    This class defines ALL graphic components and elements for the game.
    As a result, it ends up being fairly lengthy, just as all GUI stuff
    seems to end up being.
     */
    class GameGraphics extends JComponent implements MouseListener, MouseMotionListener {
        // Instance variables used later and explained later
        private Timer time;
        private ArrayList<Rectangle> buttonBounds;
        private ArrayList<String> buttonNames;
        private ArrayList<Boolean> buttonVisibilities;

        // Instance variables corresponding with mouse positions and states
        private int mX=-1;
        private int mY=-1;
        private boolean mDown=false;

        // A counter which times the simple fade in animation
        private int gameOverCount=0;

        public GameGraphics() {
            // Set itself as a mouse listener for both movement and the buttons
            addMouseListener(this);
            addMouseMotionListener(this);

            // Arrays responsible for storing info on the simple gui buttons
            buttonBounds=new ArrayList<>();
            buttonNames=new ArrayList<>();
            buttonVisibilities=new ArrayList<>();

            // A timer which will run 20 times a second, although server updates will occur slower.
            // 20 frames provides enough for simple animation, but won't overload the CPU.
            time=new Timer();
            time.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    repaint();
                }
            }, 500, 50);
        }
        // Run on the paint of the component
        public void paint(Graphics g) {
            // Try-catch to prevent errors from being thrown, although they rarely if ever should be
            try {
                // Cast the simple Graphics object to a Graphics2D
                Graphics2D g2 = (Graphics2D) g;

                // Set up visibility ArrayList
                for(int i=0; i<buttonVisibilities.size(); i++) {
                    buttonVisibilities.set(i, false);
                }

                // draw the poker table
                BufferedImage img = ImageIO.read(new File(GameLauncher.RESOURCE_ROOT+"table.png"));
                int tableWidth  = (int)(getWidth()*0.8);
                int tableHeight = (int)(getHeight()*0.8);
                int tableX      = (getWidth()-tableWidth)/2;
                int tableY      = (getHeight()-tableHeight)/2;

                int margin=10;
                int optX=margin;
                int optY=getHeight()-50+margin;

                // Actually draw the image here
                g2.drawImage(img, tableX, tableY, tableWidth, tableHeight, null);

                // Calculate which spot the player should be located at
                // This is a fairly cool feature which prevents players from being bunched up at one end, and also
                // is implemented without any trigonometry, which I tend to avoid at all costs for these sorts of things :)
                int spotFactor=playerSpots.length/(game.getPlayerList().size());
                for(int i=0; i<game.getPlayerList().size(); i++) {
                    // Get the player object
                    // Player class provides a simple base class, but no instances actually are created directly
                    // The only instances are those hiding under the child instances of RemotePlayer and LocalPlayer
                    Player p=game.getPlayer(i);
                    // If the player has lost the game or is out of the game, skip them
                    if(p.hasLost()) continue;

                    // Boolean for whether the player is the current better or not
                    boolean isBetter=game.betterIndex==i;

                    // If the current better is the local player (the one this instance of the program is controlling), then...
                    if(p.getUUID().equals(GameLauncher.manager.mainPlayer.getUUID())&&isBetter&&game.gameStarted) {
                        // Draw the fold button
                        drawButton("Fold", optX, optY, margin, g2);
                        // Draw the call/check button
                        if(game.currentBet!=0&&GameLauncher.manager.mainPlayer.getBet()!=game.currentBet) {
                            drawButton("Call ("+(game.currentBet-p.getBet())+")", optX+200, optY, margin, g2);
                        }
                        else {
                            drawButton("Check", optX+200, optY, margin, g2);
                        }
                        // Draw the raise button
                        drawButton("Raise", optX+400, optY, margin, g2);
                    }

                    // Calculate the current player's x-y position based on the non-trigonometric system implemented
                    double[] currentPlayerSpot=playerSpots[i*spotFactor];
                    int spotX=(int)(currentPlayerSpot[0]*getWidth());
                    int spotY=(int)(currentPlayerSpot[1]*getHeight());

                    // Calculate the base x and y position of the player's chips
                    // Formatting is the way it is to help with editing inside IntelliJ
                    int csX;
                    int csY;
                    if     (currentPlayerSpot[0]>0.5) csX=spotX-CHIP_SPACE_X;
                    else if(currentPlayerSpot[0]<0.5) csX=spotX+CHIP_SPACE_X;
                    else               csX=spotX;
                    if     (currentPlayerSpot[1]>0.5) csY=spotY-CHIP_SPACE_Y;
                    else if(currentPlayerSpot[1]<0.5) csY=spotY+CHIP_SPACE_Y;
                    else               csY=spotY;
                    Point chipSpot=new Point(csX, csY);
                    // Draw the chips at the calculated position
                    p.getChips().draw(chipSpot.x, chipSpot.y, (int)currentPlayerSpot[2], (int)currentPlayerSpot[3], g2);

                    // Calculate the base card x and y position of the player's hand
                    int cardSpotY=spotY;
                    if     (currentPlayerSpot[1]>=0.5) cardSpotY=spotY+CARD_SPACE_Y;
                    else if(currentPlayerSpot[1]<0.5) cardSpotY=spotY-CARD_SPACE_Y;
                    // Draw the player's hand at the calculated position
                    if(!p.isFolded()) p.drawHand(spotX, cardSpotY, g2);

                    // Calculate whether or not text should be added to the user's username
                    // to show information about their role
                    String addedText="";
                    if(p.isDealer()) addedText=" (Dealer)";
                    if(p.getBlind()==1) addedText=" (Little Blind)";
                    if(p.getBlind()==2) addedText=" (Big Blind)";
                    if(p.isDealer()&&p.getBlind()==2) addedText=" (Dealer & Big Blind)";
                    int playerFS=Font.PLAIN;
                    // Make the current better's name bold
                    if(isBetter&&game.gameStarted) {
                        playerFS=Font.BOLD;
                    }
                    // Draw the calculated strings, as well as the username and the total number of chips the player has
//                    drawCenteredString(p.getUsername()+addedText, spotX, spotY-10, new Font("Arial", playerFS, 18), g2);
                    String playerStr=p.getUsername()+addedText;
                    drawCenteredString(playerStr, spotX, spotY-10, new Font("Arial", playerFS, 18), g2);
                    drawCenteredString(p.getChips().getTotal()+" Chips", spotX, spotY+10, STANDARD_FONT, g2);
                }
                // Draw server-wide variables
                // Draw current round
                if(game.gameStarted) {
                    drawCenteredString("Round "+game.roundCount, getWidth()/2, 30, new Font("Arial", Font.PLAIN, 24), g2);
                }

                // Draw chip keys
                // This tells the user globally what each of the chips correspond to
                g2.drawRect(-1, CHIP_INDEX_BASE_Y-10, 100, 100);
                g2.drawImage(ChipBank.IMG_C100, 0, CHIP_INDEX_BASE_Y, ChipBank.CHIP_SIZE, ChipBank.CHIP_SIZE, null);
                g2.drawImage(ChipBank.IMG_C50, 0, CHIP_INDEX_BASE_Y+ChipBank.CHIP_SIZE, ChipBank.CHIP_SIZE, ChipBank.CHIP_SIZE, null);
                g2.drawImage(ChipBank.IMG_C25, 0, CHIP_INDEX_BASE_Y+ChipBank.CHIP_SIZE*2, ChipBank.CHIP_SIZE, ChipBank.CHIP_SIZE, null);
                g2.drawImage(ChipBank.IMG_C5, 0, CHIP_INDEX_BASE_Y+ChipBank.CHIP_SIZE*3, ChipBank.CHIP_SIZE, ChipBank.CHIP_SIZE, null);
                g2.drawImage(ChipBank.IMG_C1, 0, CHIP_INDEX_BASE_Y+ChipBank.CHIP_SIZE*4, ChipBank.CHIP_SIZE, ChipBank.CHIP_SIZE, null);
                g2.setFont(STANDARD_FONT);
                g2.drawString("= $100", ChipBank.CHIP_SIZE, CHIP_INDEX_BASE_Y+ChipBank.CHIP_SIZE);
                g2.drawString("= $50",  ChipBank.CHIP_SIZE, CHIP_INDEX_BASE_Y+ChipBank.CHIP_SIZE*2);
                g2.drawString("= $25",  ChipBank.CHIP_SIZE, CHIP_INDEX_BASE_Y+ChipBank.CHIP_SIZE*3);
                g2.drawString("= $5",   ChipBank.CHIP_SIZE, CHIP_INDEX_BASE_Y+ChipBank.CHIP_SIZE*4);
                g2.drawString("= $1",   ChipBank.CHIP_SIZE, CHIP_INDEX_BASE_Y+ChipBank.CHIP_SIZE*5);

                // Draw the current bet
                g2.drawString("Current Bet: "+game.currentBet, optX+getWidth()-200, optY+(getHeight()-optY)/2);

                // Draw the start game button
                if(!game.gameStarted) drawButton("Start Game", 15, 15, margin, g2);

                // Calculate the drawn card's position
                int drawnX=getWidth()/2;
                int drawnY=getHeight()/2;
                // Draw a static hand at the calculated "drawn" position
                // This also draws outlines around the cards so it doesn't look like they haven't been flipped yet
                Player.drawUniversalHand(drawnX, drawnY, game.draw, true, g2);

                // Draws the divider for actions
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(0, getHeight()-50, getWidth(), getHeight()-50);

                // Detects if the game is over or not
                if(game.gameOver) {
                    // Get the winner's message
                    String winnerUsername="";
                    for(Player p : game.players) {
                        if(!p.hasLost()) {
                            winnerUsername=p.getUsername();
                        }
                    }
                    String winnerMessage=winnerUsername+" wins!";
                    // If the local player won, then display "You win!"
                    if(!GameLauncher.manager.mainPlayer.hasLost()) {
                        winnerMessage="You win!";
                    }

                    // Set the background
                    g2.setPaint(new Color(255, 255, 255, (int)((gameOverCount/255.0)*220)));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    // Draw the win message
                    g2.setPaint(new Color(64, 64, 64, gameOverCount));
                    drawCenteredString(winnerMessage, getWidth() / 2, getHeight() / 2, new Font("Arial", Font.PLAIN, 60), g2);
                    // Increment the game counter to create animation
                    if(gameOverCount<255-10) gameOverCount+=10;
                }

                // Dispose of the Graphics2D object to prevent memory leaks
                g2.dispose();
            }
            // Catch any error thrown. Most likely due to image failure
            catch(IOException e) {
                System.out.println("Unable to retrieve image");
            }
        }
        // Draws a button at the given coordinates with given display text
        private void drawButton(String s, int X, int Y, int margin, Graphics2D g2) {
            Color preColor=g2.getColor();
            // If the button data arrays doesn't already contain a button with the same name, then add it to the data arrays
            if(!buttonNames.contains(s.substring(0, 2))) {
                buttonNames.add(s.substring(0, 2));
                buttonBounds.add(new Rectangle(X, Y, 150, 50 - margin * 2));
                buttonVisibilities.add(true);
            }
            // Set the button visibility to true so it can be clicked
            buttonVisibilities.set(buttonNames.indexOf(s.substring(0, 2)), true);

            // Check if the latest mouse coordinates overlapped with the button's bounding box
            Rectangle r=new Rectangle(X, Y, 150, 50-margin*2);
            if(r.contains(mX, mY)) {
                // If the mouse isn't pressed, just show the hover color
                if(!mDown) g2.setColor(Color.LIGHT_GRAY);
                // If it is pressed, show the pressed color
                else g2.setColor(Color.GRAY);
                // Draw the actual background
                g2.fillRect((int)r.getX(), (int)r.getY(), (int)r.getWidth(), (int)r.getHeight());
            }
            g2.setColor(Color.BLACK);
            // Draw the outline for the button
            g2.drawRect((int)r.getX(), (int)r.getY(), (int)r.getWidth(), (int)r.getHeight());
            // Draw the button label
            drawCenteredString(s, X+75, Y+(50-margin*2)/2, STANDARD_FONT, g2);

            // Set the graphic's color to what is was before the calling of this method
            g2.setColor(preColor);
        }
        // The callback called when a button is pressed
        private void buttonCallback(String s) {
            // Print the name of the button for debugging purposes
            System.out.println(s);
            if(buttonNames.contains(s)) {
                // Make sure the button clicked on was actually visible
                if(buttonVisibilities.get(buttonNames.indexOf(s))) {
                    // Switch through the possible button presses
                    switch(s) {
                        case "Fo":
                            // Send a message to the server saying "I want to fold"
                            GameLauncher.manager.mainPlayer.setFoldedSS(true);
                            break;
                        case "Ch":
                            // Send a message to the server saying "I want to call"
                            // This IS NOT a mistake. The server interprets calling and
                            // checking as the same action, as they both fundamentally
                            // do the same thing, but are just called something different
                            // depending on the scenario.
                            GameLauncher.manager.mainPlayer.setCallSS();
                            break;
                        case "Ca":
                            // Send a message to the server saying "I want to call"
                            GameLauncher.manager.mainPlayer.setCallSS();
                            break;
                        case "Ra":
                            // Create a new JFrame to make a prompt out of
                            JFrame frame = new JFrame("Enter new bet");

                            // prompt the user to enter their name
                            while(true) {
                                try {
                                    // Show an input dialog
                                    String betStr = JOptionPane.showInputDialog(frame, "Enter your new bet here (must be above current bet)");
                                    // Attempt to parse input
                                    int bet = Integer.parseInt(betStr);
                                    // Send a message to the server saying "I want to bet ___ much"
                                    GameLauncher.manager.mainPlayer.setBetSS(bet);
                                    break;
                                } catch (Exception e) {
                                    // Print that there was invalid input, then ignore it and move on
                                    System.out.println("Bad Input!");
                                    break;
                                }
                            }
                            break;
                        case "St":
                            // Handl game start request by sending the server a message saying "I want to start ___ server"
                            String res=HTTPUtils.sendGet(GameLauncher.SERVER_URL+"/startServer?uuid="+game.serverUuid);
                            // If the response contains an error message, then say there's not enough players.
                            // It is assumed that it is a lack of players error, because that is the only error the
                            // server will send back from this command.
                            if(res.contains("error")) {
                                JOptionPane.showMessageDialog(new JFrame(), "Not enough players!", "", JOptionPane.WARNING_MESSAGE);
                            }
                            break;
                    }
                }
            }
        }
        // Utility method for drawing a string based on central coordinates
        public void drawCenteredString(String s, int sX, int sY, Font f, Graphics g) {
            int w=getWidth();
            int h=getHeight();

            // Set the graphic's font to the provided one
            g.setFont(f);
            FontMetrics fm = g.getFontMetrics();
            // Calculate the string's x and y coordinates
            int x = (w - fm.stringWidth(s)) / 2;
            int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
            // Draw the string on the graphcis object
            Font pre=g.getFont();
            g.drawString(s, x-w/2+sX, y-h/2+sY);
            g.setFont(pre);
        }
        // Unused methods that must be implemented
        public void mouseEntered(MouseEvent e) {

        }
        // Unused methods that must be implemented
        public void mouseExited(MouseEvent e) {

        }
        // Set the mouse down to false if the mouse button is released
        public void mouseReleased(MouseEvent e) {
            mDown=false;
        }
        public void mousePressed(MouseEvent e) {
            // Set the mouse button to down
            mDown=true;
            int i=0;
            // Iterate through each button bounding box
            for(Rectangle r : buttonBounds) {
                // Check if the click point intersects with the rectangle's area
                if(r.contains(e.getPoint())) {
                    // If it does, call the buttonCallback with the appropriate button display text
                    buttonCallback(buttonNames.get(i));
                }
                i++;
            }
        }
        // Unused methods that must be implemented
        public void mouseClicked(MouseEvent e) {

        }
        // Set mX and mY to the current mouse position
        public void mouseMoved(MouseEvent e) {
            mX=e.getX();
            mY=e.getY();
        }
        // Unused methods that must be implemented
        public void mouseDragged(MouseEvent e) {

        }
    }
}