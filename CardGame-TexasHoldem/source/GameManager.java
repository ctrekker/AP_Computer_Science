/*
This class is primarily a class used for organization of important objects. It
is responsible for initializing the game objects and player objects, as well as
the game GUI.
 */
public class GameManager {
    // Stores the corresponding game object
    private TexasHoldem game;
    // Stores the corresponding local player object
    public LocalPlayer mainPlayer;
    // Stores the corresponding server's uuid
    private String serverUuid;
    // Stores the corresponding game gui
    public GameGUI gui;
    public GameManager(String serverUuid, String username) throws Exception {
        // Set important instance variables
        this.serverUuid=serverUuid;
        // Initialize different objects used for gameplay
        mainPlayer=new LocalPlayer(username);

        game=new TexasHoldem(serverUuid);

        game.addPlayer(mainPlayer);

        gui=new GameGUI(game);
    }
    // Called after the completion of a round
    public void roundCallback() {
        System.out.println("Done with round!");
    }
}
