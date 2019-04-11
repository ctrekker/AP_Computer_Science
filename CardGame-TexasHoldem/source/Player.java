import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Player {
    /**
     * The size, in bytes, of the player's uuid.
     * NOTE: Desynchronized sizes throughout the clients and the server
     * can cause game failure!
     */
    public final static int UUID_SIZE=16;
    // Instance variables which store ALL the important player data (except key)
    private String username;
    private String uuid;
    private Card[] hand;
    private boolean dealer=false;
    private ChipBank chips;
    private int bet=-1;
    private int totalBet=0;
    private boolean folded=false;
    private boolean hasBet=false;
    private boolean hasLost=false;
    /*
    0 -> No blind
    1 -> Little blind
    2 -> Big blind
     */
    private int blind=0;
    /**
     * Creates a new player
     * @param username The desired username to init with
     */
    public Player(String username) {
        this.username=username;
        // Default chips
        chips=new ChipBank();
        // In texas hold-em, you get two cards dealt to you
        hand=new Card[2];

        //UUID must be handled by parent classes
    }
    /**
     * Gets the player's username
     * @return A string containing the player's username
     */
    public String getUsername() {
        return username;
    }
    /**
     * Sets the player's username
     * @param username Desired new username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Gets the player's uuid
     * @return An array of bytes which make the uuid
     */
    public String getUUID() {
        return uuid;
    }
    /**
     * Set the uuid. Only allow subclasses to do so though
     * @param uuid The desired uuid in string format (hex)
     */
    protected void setUUID(String uuid) {
        this.uuid=uuid;
    }
    /**
     * Converts the uuid bytes into a hexadecimal string
     * @return A string containing hex values of the uuid
     */
    public String stringUUID() {
        return uuid;
    }
    // Getters and setters for dealer
    public boolean isDealer() {
        return dealer;
    }
    public void setDealer(boolean dealer) {
        this.dealer = dealer;
    }

    public Card[] getHand() {
        return hand;
    }

    /**
     * Puts a card in a player's hand
     * @param index Location where card should be put
     * @param c The card which should be put in the hand
     */
    public void putHand(int index, Card c) {
        hand[index]=c;
    }
    // Sets the hand
    public void setHand(Card[] hand) {
        this.hand = hand;
    }

    // Draws the current player's hand on a Graphics2D object
    public void drawHand(int x, int y, Graphics2D g2) {
        drawUniversalHand(x, y, hand, false, g2);
    }
    // Static utility method for drawing hands
    public static void drawUniversalHand(int x, int y, Card[] hand, boolean outline, Graphics2D g2) {
        try {
            // Get a sample image
            BufferedImage genImg = ImageIO.read(new File(GameLauncher.RESOURCE_ROOT + "1clubs" + ".png"));
            // Use it for width and height
            int cardWidth = (int) (genImg.getWidth() / 3.6);
            int cardHeight = (int) (genImg.getHeight() / 3.6);
            // For all the cards in the hand...
            for (int i = 0; i < hand.length; i++) {
                // Calculate this card's x and y position
                int cardX = x - (hand.length * cardWidth) / 2 + cardWidth * i;
                int cardY = y - cardHeight / 2;
                // Do drawing stuff
                if (hand[i] == null && outline) {
                    g2.drawRoundRect(cardX, cardY, cardWidth, cardHeight, 5, 5);
                    continue;
                }
                String imgName="back";
                if(hand[i] != null) {
                    int cardPrefix = hand[i].getValueNumber();
                    String cardSuffix = hand[i].getSuit().toLowerCase();
                    imgName=cardPrefix + cardSuffix;
                }
                // Get the image file and draw it
                BufferedImage cardImg = ImageIO.read(new File(GameLauncher.RESOURCE_ROOT + imgName + ".png"));
                g2.drawImage(cardImg, cardX, cardY, cardWidth, cardHeight, null);
            }
        }
        // Catch any file errors that might have occurred
        catch(IOException e) {
            System.out.println("Error reading image!");
            System.out.println("Printing stack trace...");
            e.printStackTrace();
        }
    }

    // Overridden toString for debugging
    @Override
    public String toString() {
        return "Player{username="+username+";hand="+Arrays.toString(hand)+";dealer="+dealer+";blind="+blind+";uuid="+stringUUID()+";chips="+chips.getTotal()+";bet="+bet+"}";
    }

    /*
    Below are lots and lots of getters and setters. They don't do anything but get and
    set the private instance variables. This helps organize all the data into methods
     */
    public ChipBank getChips() {
        return chips;
    }
    public void setChips(ChipBank chips) {
        this.chips = chips;
    }
    public int getBlind() {
        return blind;
    }
    public void setBlind(int blind) {
        this.blind = blind;
    }
    public boolean hasBet() {
        return hasBet;
    }
    public void setHasBet(boolean hasBet) {
        this.hasBet=hasBet;
    }
    public int getBet() {
        return bet;
    }
    public void setBet(int bet) {
        this.bet = bet;
    }
    public boolean isFolded() {
        return folded;
    }
    public void setFolded(boolean folded) {
        this.folded=folded;
    }
    public int getTotalBet() {
        return totalBet;
    }
    public void setTotalBet(int tb) {
        totalBet=tb;
    }
    public boolean hasLost() {
        return hasLost;
    }
    public void setHasLost(boolean hasLost) {
        this.hasLost = hasLost;
    }
}
