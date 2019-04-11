public class Card {
    private final String suit;
    private final String value;
    public final static String[] VALUES={"TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING", "ACE"};

    public final static String DIAMONDS = "DIAMONDS";
    public final static String CLUBS    = "CLUBS";
    public final static String HEARTS   = "HEARTS";
    public final static String SPADES   = "SPADES";

    /**
     *
     * @param suit A string representing the suit of the card. Use Card.DIAMONDS, Card.CLUBS, etc. for this
     * @param value The value of the card. Use Card.TWO, Card.THREE, etc. for this
     */
    public Card(String suit, String value) {
        this.suit=suit;
        this.value=value;
    }

    public String getSuit() {
        return suit;
    }
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue()+" of "+getSuit();
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Card) {
            if(getSuit().equals(((Card)obj).getSuit())&&getValue().equals(((Card)obj).getValue())) {
                return true;
            }
        }
        return false;
    }

    public static Card randomCard() {
        return new Card(randomSuit(), randomValue());
    }

    private static String randomValue() {
        return VALUES[(int)((Math.random()*VALUES.length))];
    }

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
