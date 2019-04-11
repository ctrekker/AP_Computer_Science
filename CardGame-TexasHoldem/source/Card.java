import java.util.Arrays;

public class Card {
    // Stores the suit and value of the cards
    private final String suit;
    private final String value;

    // List of values in which the card class would understand
    public final static String[] VALUES={"TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING", "ACE"};

    // List of suits the card class would understand
    public final static String DIAMONDS = "DIAMONDS";
    public final static String CLUBS    = "CLUBS";
    public final static String HEARTS   = "HEARTS";
    public final static String SPADES   = "SPADES";

    // Converts the all-numeric values of cards sent from the server into the string values this class understands
    public static Card fromNumeric(int type, int value) {
        String suit="";
        // Switch-case suit
        switch(type) {
            case 0:
                suit=DIAMONDS;
                break;
            case 1:
                suit=HEARTS;
                break;
            case 2:
                suit=SPADES;
                break;
            case 3:
                suit=CLUBS;
                break;
        }
        String val="";
        // Use numeric conversion algorithm to turn server numeric value into string values
        int arrIndex=value-2;
        if(arrIndex==-1) {
            val=VALUES[12];
        }
        else {
            val=VALUES[arrIndex];
        }

        // Return a new card with the calculated string values
        return new Card(suit, val);
    }
    /**
     *
     * @param suit A string representing the suit of the card. Use Card.DIAMONDS, Card.CLUBS, etc. for this
     * @param value The value of the card. Use Card.TWO, Card.THREE, etc. for this
     */
    public Card(String suit, String value) {
        this.suit=suit;
        this.value=value;
    }

    // Returns the suit/value
    public String getSuit() {
        return suit;
    }
    public String getValue() {
        return value;
    }

    // Get the corresponding server number id from the card's string value
    public int getValueNumber() {
        int val=getValueIndex(value);
        if(val>=0) {
            val+=2;
            if(val>=14) {
                val=1;
            }
            return val;
        }
        return 2;
    }
    // Get the index of a particular string value within the values array (final and static)
    public static int getValueIndex(String value) {
        for(int i=0; i<VALUES.length; i++) {
            if(VALUES[i].equals(value)) return i;
        }
        return 0;
    }

    // Overrides the default toString method so cards can be printed for development
    @Override
    public String toString() {
        return getValue()+" of "+getSuit();
    }

    // Overrides the default equals method so cards can be compared properly (compares values, not references)
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Card) {
            if(getSuit().equals(((Card)obj).getSuit())&&getValue().equals(((Card)obj).getValue())) {
                return true;
            }
        }
        return false;
    }

    // Creates a random card and returns it
    public static Card randomCard() {
        return new Card(randomSuit(), randomValue());
    }

    // Returns a random value obtained from the VALUES array
    private static String randomValue() {
        return VALUES[(int)((Math.random()*VALUES.length))];
    }

    // Returns a random suit using Math.random and switch-case statements
    private static String randomSuit() {
        int rand=(int)(Math.random()*4);
        switch(rand) {
            case 0:
                return DIAMONDS;
            case 1:
                return CLUBS;
            case 2:
                return HEARTS;
            case 3:
                return SPADES;
            // Should NEVER happen (Math.random)
            default:
                return DIAMONDS;
        }
    }
}