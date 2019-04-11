import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/*
Extremely fundamentally important class!
This initializes a connection to the server specifically for each remote player
Note that LocalPlayer is just an extension of this, and this is an extension of Player
 */
public class RemotePlayer extends Player {
    // Instance variables
    private Timer timer;
    private boolean loaded=false;
    private String key="NULL";
    public RemotePlayer(String username, String uuid) {
        super(username);
        // Set up a new update timer for updating the information retrieved from the server
        // By default, this is once every seconds.
        // This means that there is a maximum latency of 2 seconds if connections to the server were instant
        timer=new Timer("RP_Manager--"+getUsername());
        timer.schedule(new TimerTask() {
            public void run() {
                if(!sync()) {
                    GameLauncher.manager.gui.game.removePlayer(uuid);
                }
            }
        }, 1000, 1000);

        // Set its uuid
        setUUID(uuid);
    }
    // Gets run every second
    protected boolean sync() {
        try {
            // Calculate request url
            String serverUrl = GameLauncher.SERVER_URL;
            String addon="";
            if(!key.equals("NULL")) {
                addon="&key="+key;
            }
            // Send request for either generic or specific player details (depending on whether its remote or local)
            String clientInfoRaw = HTTPUtils.sendGet(serverUrl + "/clientInfo?uuid=" + getUUID()+addon);

            // Parse json into objects
            JSONObject clientInfo = new JSONObject(clientInfoRaw);

            // Handle username
            setUsername(clientInfo.getString("username"));

            // Handle bets
            try {
                setHasBet(clientInfo.getBoolean("hasBet"));
                if (clientInfo.getBoolean("hasBet")) {
                    setBet(clientInfo.getInt("bet"));
                } else setBet(clientInfo.getInt("betRound"));
            }
            catch(JSONException e) {
                setBet(clientInfo.getInt("betRound"));
            }

            // Handle loss detection
            setHasLost(clientInfo.getBoolean("hasLost"));

            // Handle total bet
            setTotalBet(clientInfo.getInt("totalBet"));

            // Handle blind
            setBlind(clientInfo.getInt("blind"));

            // Handle hand if it is the local player
            try {
                if (clientInfo.getJSONArray("hand").length() > 0) {
                    Card[] hand = {
                            // Convert the server data into card data
                            Card.fromNumeric(
                                    clientInfo.getJSONArray("hand").getJSONObject(0).getInt("type"),
                                    clientInfo.getJSONArray("hand").getJSONObject(0).getInt("value")
                            ),
                            Card.fromNumeric(
                                    clientInfo.getJSONArray("hand").getJSONObject(1).getInt("type"),
                                    clientInfo.getJSONArray("hand").getJSONObject(1).getInt("value")
                            )
                    };
                    setHand(hand);
                } else {
                    Card[] hand = {null, null};
                    setHand(hand);
                }
            }
            catch(JSONException e) {
                // Set hand to null if the server doesn't respond with one
                Card[] hand = {null, null};
                setHand(hand);
            }

            // Handle chips
            getChips().fromJSON(clientInfo.getJSONObject("chips"));

            // Handle dealer
            setDealer(clientInfo.getBoolean("dealer"));

            // Handle folded
            setFolded(clientInfo.getBoolean("folded"));

            // Set loaded to true so the program knows this player has been loaded at least once
            loaded=true;

            // Return true to signify successful syncing
            return true;
        }
        catch(JSONException e) {
            System.out.println("WARN: unable to sync player. Possibly very slow server connection");
            // Return false to signify failure
            return false;
        }
    }
    // Getters and setters
    public boolean isLoaded() {
        return loaded;
    }
    public void setKey(String key) {
        this.key=key;
    }
}