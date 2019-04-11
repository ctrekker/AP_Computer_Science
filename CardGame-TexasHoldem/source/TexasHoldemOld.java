import java.util.ArrayList;
import java.util.Arrays;
/*
THIS IS OLD AND IS CURRENTLY UNUSED
The currently used version is TexasHoldem
This is included because it is an EXTREMELY simplified version of the server, in Java form.
During the development of this game, I built a completely client-only version of texas holdem (one computer).
The majority of the code was within this class, the player class, and the GUI class. It was used as a framework
for the server after being converted from Java into NodeJS by hand, and then built on massively. Before comments,
this class was about 250 lines. The completed server is over 1200 lines.
 */
public class TexasHoldemOld {
    /**
     * An inclusive value specifying the minimum number of players that can join a game
     */
    public final static int MIN_PLAYERS=2;
    /**
     * An inclusive value specifying the maximum number of players that can join a game
     */
    public final static int MAX_PLAYERS=10;
    private CardDeck deck;
    /*
    List of players in the game
     */
    private ArrayList<Player> players;
    /*
    Index of current dealer
     */
    private int dealerIndex=0;
    /*
    Tells whether the game has been started or not
     */
    private boolean gameStarted=false;
    /*
    The list of cards with the drawn cards on the board (flop, turn, and river)
     */
    private Card[] draw;
    /*
    A ChipBank object is used for the pot (where all the chips are put)
     */
    private ChipBank pot;
    /*
    PHASE INDEX:
    0  -> Init
    1  -> Blinds
    2  -> Deal
    3  -> Second bet (before flop)
    4  -> Flop reveal
    5  -> Third bet (before turn)
    6  -> Turn reveal
    7  -> Fourth bet (before river)
    8  -> River reveal
    9  -> Fifth bet (final)
    10 -> Card reveal
    11 -> Reset + dealer/blind changes
     */
    private byte phase=0;
    private int roundCount=0;
    private int minimumBet;
    private GameManager manager;
    /**
     * Creates a new, blank texas holdem game
     */
    public TexasHoldemOld(GameManager manager, int minimumBet) {
        deck=new CardDeck();
        draw=new Card[5];
        players=new ArrayList<>();
        // Create empty pot
        pot=new ChipBank(0, 0, 0, 0, 0);
        if(minimumBet%2!=0) throw new Error("Minimum bet cannot be odd number due to blinds!");
        this.minimumBet=minimumBet;
        this.manager=manager;
    }
    /**
     * Adds a player to the list of players in the game if the game hasn't started
     * @param p The desired player object to add
     */
    public void addPlayer(Player p) {
        if(!gameStarted) players.add(p);
    }
    /**
     * Gets a player from the array of players
     * @param i Index of the player object within the array
     * @return The player object
     */
    public Player getPlayer(int i) {
        return players.get(i);
    }
    /**
     * Removes a player from the list of players in the game if the game hasn't started
     * @param i The desired index to remove
     */
    public void removePlayer(int i) {
        if(!gameStarted) players.remove(i);
    }
    /**
     * Gets the arraylist of players
     * @return An ArrayList of players
     */
    public ArrayList<Player> getPlayerList() {
        return players;
    }
    /**
     * Initializes the game with available parameters if parameters fit conditional constraints
     */
    public void beginGame() {
        if(!gameStarted&&players!=null&&players.size()>=MIN_PLAYERS&&players.size()<=MAX_PLAYERS) {
            gameStarted=true;
        }
    }
    /**
     * Asynchronously iterate through a full round
     */
    public void playRound() {
        roundCount++;
        System.out.println("ROUND #"+roundCount);
        if(gameStarted) {
            if(phase!=0) phase=0;
            // Run through all the phases
            // Step through the program in a new thread
            Thread stepRunner=new Thread(new StepRunnable(manager));
            stepRunner.start();

            phase=0;
            dealerIndex++;
            if(dealerIndex>=players.size()) dealerIndex=0;
        }
        System.out.println();
    }
    /**
     * Step through ONE step. This varies greatly and is only to be used within infinite-break loops
     * As of development, this was turned into a loop (kind of recursive)
     * NOTE: this function must ALWAYS be called in a subprocess so it doesn't block the main thread
     */
    public void nextStep() {
        if(gameStarted) {
            switch(phase) {
                // Init
                case 0:
                    // Reset and set params
                    if(!players.get(dealerIndex).isDealer()) {
                        for(Player p : players) {
                            p.setDealer(false);
                            p.setBlind(0);
                        }
                        players.get(dealerIndex).setDealer(true);

                        int littleBlindIndex=dealerIndex+1;
                        int bigBlindIndex=dealerIndex+2;
                        while(littleBlindIndex>=players.size()) {
                            littleBlindIndex-=players.size();
                        }
                        while(bigBlindIndex>=players.size()) {
                            bigBlindIndex-=players.size();
                        }
                        players.get(littleBlindIndex).setBlind(1);
                        players.get(bigBlindIndex).setBlind(2);
                    }
                    phase++;
                    break;
                // Blinds
                case 1:
                    for(Player p : players) {
                        // If the player is a certain blind then...
                        if(p.getBlind()!=0) {
                            // Little blind
                            if(p.getBlind()==1) {
                                p.getChips().transferTo(pot, minimumBet/2);
                            }
                            // Big blind
                            else if(p.getBlind()==2) {
                                p.getChips().transferTo(pot, minimumBet);
                            }
                        }
                    }
                    phase++;
                    break;
                // Deal hand
                case 2:
                    System.out.println("Dealing cards...");
                    // Deal cards to players
                    // r<2 because 2 cards should be dealt
                    for(int r=0; r<2; r++) {
                        // Deal the first card to the player left of the dealer
                        for(int i=dealerIndex+1; i<players.size(); i++) {
                            players.get(i).putHand(r, deck.drawCard());
                        }
                        // Deal the remaining cards
                        for(int i=0; i<=dealerIndex; i++) {
                            players.get(i).putHand(r, deck.drawCard());
                        }
                    }
                    phase++;
                    break;
                // Second bet (before flop)
                case 3:
                    waitAndCollectBets();
                    phase++;
                    break;
                // Reveal the flop
                case 4:
                    // Put card draws (first three) in public draws slot
                    for(int i=0; i<3; i++) {
                        draw[i]=deck.drawCard();
                    }
                    phase++;
                    break;
                // Third bet (before turn)
                case 5:
                    waitAndCollectBets();
                    phase++;
                    break;
                // Reveal the turn
                case 6:
                    draw[3]=deck.drawCard();
                    phase++;
                    break;
                // Fourth bet (before river)
                case 7:
                    waitAndCollectBets();
                    phase++;
                    break;
                // Reveal the river
                case 8:
                    draw[4]=deck.drawCard();
                    phase++;
                    break;
                // Fifth bet (before card reveal)
                case 9:
                    waitAndCollectBets();
                    phase++;
                    break;
                case 10:
                    phase++;
                    break;
                // General reset
                case 11:
                    System.out.println(pot);
                    System.out.println(Arrays.toString(draw));
                    for(Player p : players) {
                        System.out.println(p);
                    }
                    System.out.println("Resetting...");
                    // Reset all player hands
                    for(Player p : players) {
                        p.setHand(new Card[2]);
                    }
                    // Reshuffle deck for next round by creating new instance
                    deck=new CardDeck();
                    phase++;
                    break;
            }
        }
    }
    private void waitForBets() {
        while(true) {
            boolean moveOn=true;
            for(Player p : players) {
                if(!p.hasBet()) {
                    moveOn=false;
                }
            }
            if(moveOn) break;
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                System.out.println("Error delaying next player check");
            }
        }
    }
    private void waitAndCollectBets() {
        waitForBets();
        // Now that betting is done, do the chip calculations
        for(Player p : players) {
            p.getChips().transferTo(pot, p.getBet());
            p.setBet(-1);
        }
    }
    class StepRunnable implements Runnable {
        private GameManager manager;
        public StepRunnable(GameManager m) {
            manager=m;
        }
        public void run() {
            do {
                nextStep();
                // If it was a betting turn, run the bet callback in the main thread
                if(phase==3||phase==5||phase==7||phase==9) {
//                    manager.betCallback();
                }
            } while(phase<=11);
            manager.roundCallback();
        }
    }
}