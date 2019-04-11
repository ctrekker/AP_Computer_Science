import org.json.JSONObject;

import javax.swing.*;

// A specific type of RemotePlayer which is controlled by the local client, rather than the remote server
public class LocalPlayer extends RemotePlayer {
    // VERY IMPORTANT: this key is a required part of the trust of the client and the server
    // To prevent unauthorized requests (for example, someone else viewing your cards), the server
    // will only respond to clients with properly authorized keys. Can be thought of as a sort of
    // "password" of the user.
    private String key;
    public LocalPlayer(String username) {
        // Provide the remotePlayer with the necessary info
        // UUID is empty because it hasn't been obtained yet
        super(username, "");
        // Run LocalPlayer specific init method
        init();
    }
    // Initializes the user with a valid, server-certified username and corresponding key
    private void init() {
        // Send a request to the server asking to initiate a new client
        String res=HTTPUtils.sendGet(GameLauncher.SERVER_URL+"/clientInit?username="+getUsername());
        // The response is in the form of JSON, which contains a uuid and a key which should be kept safe and private
        JSONObject pMeta=new JSONObject(res);
        // Set the UUID (method within Player class)
        setUUID(pMeta.getString("uuid"));
        // Set the Key (method within own class)
        setKey(pMeta.getString("key"));
        // Also set the RemotePlayer's key for remote syncing as well
        // This would normally be null, but since the player is local the
        // remote server needs authentication
        super.setKey(getKey());

        // Get other important data bout the user
        JSONObject obj=pMeta.getJSONObject("obj");
        // Set the local username to the responded username
        setUsername(obj.getString("username"));

        // Ask the server to let the player with the corresponding uuid and key into the selected server
        res=HTTPUtils.sendGet(GameLauncher.SERVER_URL+"/serverJoin?uuid="+getUUID()+"&key="+getKey()+"&sid="+GameLauncher.serverUuid);
        // Print the response for debug. This is the massive amount of "jibberish" that comes out at the beginning
        // I can read that stuff :)
        System.out.println(res);

        System.out.println("Local player created with username "+getUsername());
        System.out.println("\tuuid -> "+getUUID());
        System.out.println("\tkey  -> "+getKey());
    }
    protected boolean sync() {
        return super.sync();
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        super.setKey(key);
        this.key=key;
    }

    /**
     * Sets the state 'bet' on the server side
     * @param bet The new value of bet
     */
    public void setBetSS(int bet) {
        String res=HTTPUtils.sendGet(GameLauncher.SERVER_URL+"/action_setBet?uuid="+getUUID()+"&key="+getKey()+"&bet="+bet);
        if(res.contains("error")) {
            // A minimum bet error was thrown
            if(res.contains("too_low")) {
                JOptionPane.showMessageDialog(new JFrame(), "That is below the minimum bet. Please raise above the minimum bet!", "Raise too low", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Sets the state 'call' on the server side
     */
    public void setCallSS() {
        String res=HTTPUtils.sendGet(GameLauncher.SERVER_URL+"/action_call?uuid="+getUUID()+"&key="+getKey());
        System.out.println(res);
    }
    /**
     * Sets the state 'folded' on the server side
     * @param folded The value of folded
     */
    public void setFoldedSS(boolean folded) {
        String res=HTTPUtils.sendGet(GameLauncher.SERVER_URL+"/action_setFolded?uuid="+getUUID()+"&key="+getKey()+"&folded="+folded);
        System.out.println(res);
    }
}
