import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class StartupGUI extends JFrame {
    // Welcome message
    public static String welcomeMessageText=
            "Hello, and welcome to Connor Burns' Texas Hold'em Poker game! Make sure to get a friend with another computer to play with you," +
                    " or open a new window of this program, because it requires more than one player on more than one instance of the game. " +
                    "That means this game is capable of connecting you and several other players over the world-wide web in a game of poker." +
                    " <br><br>To begin, join a server using the menu on the left. Please be sure to set your username in the text box above " +
                    "the server list. This will be what shows up on other people's computers when they connect to the same server. Please not" +
                    "e that once a server has been started, nobody else can join.";
    // Stores generic server info
    JSONObject serverMeta;
    public StartupGUI(String server) {
        // Attempt to set the icon image to that fancy orange gradient with the T and H on it
        try {
            setIconImage(ImageIO.read(new File(GameLauncher.RESOURCE_ROOT+"icon.png")));
        }
        // Its ok if it doesn't work. Just print out a message for debug
        catch(IOException e) {
            System.out.println("Error loading window icon");
            System.out.println("Continuing without asset...");
        }

        // Set server stuff
        setSize(640, 340);
        setResizable(false);
        setTitle("Server Configuration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Make a new gridbag pane
        JPanel pane=new JPanel();
        pane.setPreferredSize(getSize());
        pane.setBackground(Color.WHITE);
        pane.setLayout(new GridBagLayout());

        // Create constraints for the gridbag
        GridBagConstraints c=new GridBagConstraints();
        c.fill=GridBagConstraints.HORIZONTAL;
        c.insets=new Insets(15, 15, 15, 15);

        // Add the title to the panel
        c.weightx=1;
        c.weighty=0.1;
        c.gridx=1;
        c.gridy=0;
        c.gridwidth=1;
        JLabel mainTitle=new JLabel("Texas Hold'em by Connor Burns");
        mainTitle.setFont(new Font("Arial", Font.PLAIN, 20));
        mainTitle.setHorizontalAlignment(JLabel.CENTER);
        pane.add(mainTitle, c);

        // Add the username text box to the panel
        c.gridx=0;
        c.gridy=0;
        c.weightx=0;
        c.weighty=0;
        c.gridwidth=1;
        JTextField usernameTextbox=new JTextField("Username");
        pane.add(usernameTextbox, c);

        //Add the open servers header to the panel
        c.gridx=0;
        c.gridy=1;
        c.weightx=0;
        c.weighty=0;
        c.gridwidth=1;
        c.anchor=GridBagConstraints.NORTH;
        JLabel serverListHeader=new JLabel("Open Servers");
        serverListHeader.setFont(new Font("Arial", Font.PLAIN, 16));
        pane.add(serverListHeader, c);

        // Add the list of servers to the panel
        c.gridx=0;
        c.gridy=2;
        c.weighty=1;
        c.anchor=GridBagConstraints.NORTHWEST;
        DefaultListModel<String> serverListModel=new DefaultListModel<>();
        serverListModel.addElement("Loading...");
        JList<String> serverList=new JList<>(serverListModel);
        serverList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        serverList.setVisibleRowCount(-1);
        serverList.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        serverList.setSelectedIndex(0);
        pane.add(serverList, c);

        // Add the "Select this Server" button to the panel
        c.gridx=0;
        c.gridy=3;
        c.weighty=0;
        c.anchor=GridBagConstraints.SOUTH;
        JButton selectServer=new JButton("Select this Server");
        selectServer.addActionListener(new ActionListener() {
            // Handle button press
            @Override
            public void actionPerformed(ActionEvent e) {
                // Make sure the username isn't too large
                if(usernameTextbox.getText().length()<=20) {
                    // Launch the game with the specified server uuid and local player username
                    System.out.println(serverList.getSelectedValue());
                    JSONObject obj = serverMeta.getJSONObject("meta").getJSONObject(serverList.getSelectedValue());
                    GameLauncher.launchGame(obj.getString("uuid"), usernameTextbox.getText());
                }
                else {
                    // Tell the user about how their name is just too long...
                    JOptionPane.showMessageDialog(new JFrame(), "Your name is too long! Max is 20 characters.", "Bad Name", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        pane.add(selectServer, c);

        // Add the welcome text to the panel
        c.gridx=1;
        c.gridy=1;
        c.weightx=0.75;
        c.weighty=0.9;
        c.gridwidth=1;
        c.gridheight=3;
        c.anchor=GridBagConstraints.NORTHWEST;
        JLabel welcomeMessage=new JLabel("<html>"+welcomeMessageText+"</html>");
        welcomeMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        pane.add(welcomeMessage, c);


        // Set the frame layout to a border layout
        setLayout(new BorderLayout());
        add(pane, BorderLayout.CENTER);

        // Make the frame visible
        setVisible(true);

        // Load servers, and put them onto the list
        serverMeta=new JSONObject(HTTPUtils.sendGet(server+"/listServers"));
        JSONObject meta=serverMeta.getJSONObject("meta");
        // Get the iterator, then iterate through each of the keys
        Iterator<String> keys=meta.keys();
        while(keys.hasNext()) {
            // Get the next uuid of the next server
            String uuid=keys.next();
            JSONObject serverMeta=meta.getJSONObject(uuid);
            // Make sure the listed server isn't started already
            if(!serverMeta.getBoolean("started")) {
                // Add the name element to the list model
                serverListModel.addElement(serverMeta.getString("name"));
            }
        }
        // Remove the "Loading..." element of the list
        serverListModel.remove(0);
        serverList.setSelectedIndex(0);
    }
}