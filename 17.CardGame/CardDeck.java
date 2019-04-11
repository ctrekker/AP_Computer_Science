import java.util.ArrayList;

public class CardDeck {
    public final static int DECK_LENGTH = 52;
    private ArrayList<Card> cards;
    public CardDeck() {
        cards=new ArrayList<>();
        for(int i=0; i<DECK_LENGTH; i++) {
            Card current=Card.randomCard();
            while(hasCard(current)) {
                current=Card.randomCard();
            }
            cards.add(current);
        }
    }

    private boolean hasCard(Card c) {
        for(int i=0; i<cards.size(); i++) {
            if(cards.get(i).equals(c)) return true;
        }
        return false;
    }

    public Card getCard(int index) {
        return cards.get(index);
    }
    public ArrayList<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        String out="Deck "+this.hashCode()+":\n";
        for(int i=0; i<cards.size(); i++) {
            out+=cards.get(i).toString()+"\n";
        }
        return out;
    }
}
