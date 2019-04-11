import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TexasHoldem {
    /*
    List of players in the game
     */
    protected ArrayList<Player> players;
    /*
    List of player uuids
     */
    protected ArrayList<String> playerUuids;
    /*
    Index of current dealer
     */
    protected int dealerIndex=0;
    /*
    Better index
     */
    protected int betterIndex=1;
    /*
    Tells whether the game has been started or not
     */
    protected boolean gameStarted=false;
    protected boolean gameOver=false;
    /*
    The list of cards with the drawn cards on the board (flop, turn, and river)
     */
    protected Card[] draw=new Card[5];
    /*
    A ChipBank object is used for the pot (where all the chips are put)
     */
    protected ChipBank pot;
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
    protected byte phase=0;
    // Other important instance variables
    protected int roundCount=0;
    protected int minimumBet;
    protected int currentBet;

    // Generic server info
    protected String serverUuid;
    protected String serverName;
    public TexasHoldem(String serverUuid) {
        // Init variables
        this.players=new ArrayList<>();
        this.playerUuids=new ArrayList<>();

        // Set the server uuid
        this.serverUuid=serverUuid;

        // Schedule 1 second updates to the server state
        Timer t=new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                update();
            }
        }, 400, 1000);
    }
    // Start the game (at least on the client side)
    public void beginGame() {
        gameStarted=true;
    }
    // Responsible for updating ALL server info
    public void update() {
        // Get data, then parse it into something readable
        JSONObject serverInfo=new JSONObject(HTTPUtils.sendGet(GameLauncher.SERVER_URL+"/serverInfo?uuid="+serverUuid));
        // Start the game if the server says to
        if(serverInfo.getBoolean("gameStarted")) {
            beginGame();
        }

        // Set all the instance variables from the parsed server data
        // And yes, a lot of trust is put into the server here...
        JSONObject serverData=serverInfo.getJSONObject("data");
        this.phase=(byte)serverData.getInt("phase");
        this.roundCount=serverData.getInt("roundCount");
        this.betterIndex=serverData.getInt("betterIndex");
        this.serverName=serverInfo.getString("name");
        this.serverUuid=serverInfo.getString("uuid");
        this.minimumBet=serverData.getInt("minimumBet");
        this.currentBet=serverData.getInt("currentBet");
        this.gameOver=serverData.getBoolean("gameOver");

        // Handle drawn cards
        JSONArray draw=serverData.getJSONArray("drawn");
        for(int i=0; i<this.draw.length; i++) {
            this.draw[i]=null;
        }
        for(int i=0; i<draw.length(); i++) {
            JSONObject currentCard=draw.getJSONObject(i);
            this.draw[i]=Card.fromNumeric(currentCard.getInt("type"), currentCard.getInt("value"));
        }

        // Handle players
        if(!gameStarted) {
            // Get list of players
            JSONArray playerList=serverInfo.getJSONArray("players");
            for(Object playerObj : playerList) {
                // Cast the uuid objects into strings
                String playerUuid=(String)playerObj;
                // If either of those things aren't ready, skip that player
                if(playerUuid==null||GameLauncher.manager==null) continue;
                // If the current uuid is equal to that of the local player's then...
                if(playerUuid.equals(GameLauncher.manager.mainPlayer.getUUID())) {
                    // Remove the player from the list
                    removePlayer(getPlayerList().indexOf(GameLauncher.manager.mainPlayer));
                    // Then re-add it to update ui
                    addPlayer(GameLauncher.manager.mainPlayer);
                }
                // If we haven't loaded a player yet then...
                if(!playerUuids.contains(playerUuid)) {
                    // Make a new RemotePlayer to handle that connection and that player
                    // The username will be changed from "Loading..." to the appropriate username
                    // once the first connection is established
                    Player rp=new RemotePlayer("Loading...", playerUuid);
                    // Add that player to the list of players in the game
                    addPlayer(rp);
                }
            }
            int c=0;
            // Make sure all the players are in the correct slots in the ui
            for(Object playerObj : playerList) {
                // Player's current uuid
                String playerUuid = (String) playerObj;
                // Current and desired player indicies
                int currentIndex=0;
                int desiredIndex=c;
                int d=0;
                // Do the calculations for swapping the players
                for(Player p : players) {
                    if(p.getUUID()==null) continue;
                    if(p.getUUID().equals(playerUuid)) currentIndex=d;
                    d++;
                }
                // Swap the two players
                Player currentPlayer=players.get(currentIndex);
                Player otherPlayer=players.get(desiredIndex);
                players.set(desiredIndex, currentPlayer);
                players.set(currentIndex, otherPlayer);
                c++;
            }
        }
    }

    /**
     * Adds a player to the list of players in the game if the game hasn't started
     * @param p The desired player object to add
     */
    public void addPlayer(Player p) {
        if(!gameStarted) {
            players.add(p);
            playerUuids.add(p.getUUID());
        }
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
    public void removePlayer(String uuid) {
        int i=0;
        for(Player p : players) {
            // If the uuid equals the given one, then remove them from the player list
            if(p.getUUID().equals(uuid)) {
                removePlayer(i);
                break;
            }
            i++;
        }
    }
    /**
     * Gets the arraylist of players
     * @return An ArrayList of players
     */
    public ArrayList<Player> getPlayerList() {
        return players;
    }
}