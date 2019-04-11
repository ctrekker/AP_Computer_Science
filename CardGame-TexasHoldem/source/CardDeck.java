import java.util.ArrayList;

public class CardDeck {
    // Standard deck length
    public final static int DECK_LENGTH = 52;
    // List of cards in the deck
    private ArrayList<Card> cards;
    public CardDeck() {
        // Init the array list
        cards=new ArrayList<>();
        // Randomly select and add 52 cards (essentially shuffles randomly)
        for(int i=0; i<DECK_LENGTH; i++) {
            Card current=Card.randomCard();
            while(hasCard(current)) {
                current=Card.randomCard();
            }
            cards.add(current);
        }
    }

    /**
     * Draws a random card out of the deck then removes it
     * @return The random card selected
     */
    public Card drawCard() {
        // If there are no cards, return null
        if(cards.size()==0) return null;
        // Otherwise, select a random one, remove it, then return it
        int index=(int)(Math.random()*cards.size());
        Card selected=cards.get(index);
        cards.remove(index);

        return selected;
    }

    // Checks if the deck has a card
    private boolean hasCard(Card c) {
        for(int i=0; i<cards.size(); i++) {
            if(cards.get(i).equals(c)) return true;
        }
        return false;
    }

    // Gets a card at a desired index - Currently unused
    public Card getCard(int index) {
        return cards.get(index);
    }
    // Returns the array list of cards
    public ArrayList<Card> getCards() {
        return cards;
    }

    // Overrides default toString for debugging purposes
    @Override
    public String toString() {
        String out="Deck "+this.hashCode()+":\n";
        for(int i=0; i<cards.size(); i++) {
            out+=cards.get(i).toString()+"\n";
        }
        return out;
    }
}
