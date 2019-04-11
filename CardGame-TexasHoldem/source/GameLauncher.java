import javax.imageio.ImageIO;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

// Launcher class
public class GameLauncher {
    // Url to connect to (server must be configured properly)
    public static String SERVER_URL="http://burnscoding.com:52";
    // Location of resources. Default is below. SHOULD NOT BE CHANGED
    public final static String RESOURCE_ROOT="."+File.separator+"TexasHoldem_assets"+File.separator;
    // Game manager which will be assigned later
    public static GameManager manager;
    // UUID of the server which will be connected to
    public static String serverUuid;
    // GUI of the startup prompt
    private static StartupGUI startupGui;
    // This is the only main class in the project
    public static void main(String[] args) throws Exception {
        // To avoid unresponsive window, do time-consuming actions in a new computer thread
        new Thread(new Runnable() {
            // Automatically run when the new thread is created
            public void run() {
                // Checks to make sure all resources are present before launching
                checkResources();
            }
        }).start();
    }
    // Main-thread callback for starting up the startupGui.
    // Should only be run after resources have been checked asynchronously
    public static void launchStartup() {
        startupGui=new StartupGUI(SERVER_URL);
    }
    // Launches the game from the info given by the user inside the startupGui
    public static void launchGame(String serverUuidP, String username) {
        try {
            startupGui.dispatchEvent(new WindowEvent(startupGui, WindowEvent.WINDOW_CLOSING));
            serverUuid=serverUuidP;

            // Make a new game manager to manage the client's connections
            manager=new GameManager(serverUuid, username);
        }
        // Make sure nothing goes wrong (it never should theoretically)
        catch(Exception e) {
            // Print its stack trace, but continue program execution
            e.printStackTrace();
        }
    }
    // Check for all the needed resources (mostly images)
    public static void checkResources() {
        downloadResources();
        launchStartup();
    }
    // Opens a downloader window GUI and downloads all resources
    public static void downloadResources() {
        ResourceLoaderGUI loaderGUI=new ResourceLoaderGUI();
        // Make sure the resource directory is there. If not, make one
        new File(RESOURCE_ROOT).mkdir();

        // Request a list of needed files from the server. Source code for downloadList.php can be located by viewing INSTRUCTIONS.txt inside release folder
        String listRaw=HTTPUtils.sendGet("http://ctrekker.mjtrekkers.com/school/TexasHoldem/downloadList.php");
        // Split it into an array
        String[] imgList=listRaw.split(",");
        int num=0;
        for(String imgUrl : imgList) {
            // Turn server resources into an image, then save them all to files within the resource root
            BufferedImage image;
            try {
                if(!new File(RESOURCE_ROOT+imgUrl).exists()) {
                    loaderGUI.updateLabel(imgUrl);
                    URL url = new URL("http://ctrekker.mjtrekkers.com/school/TexasHoldem/" + imgUrl);
                    image = ImageIO.read(url);
                    ImageIO.write(image, "png", new File(RESOURCE_ROOT + imgUrl));
                }
            }
            // If it fails, continue execution, but give a warning
            catch (IOException e) {
                System.out.println("Critical error downloading file...");
                System.out.println("Continuing execution without file...");
            }
            num++;
            // Update the GUI progress to a new percentage
            loaderGUI.updateProgress((int)(((double)num/imgList.length)*100));
        }

        // Once loading is done, close the window
        loaderGUI.dispatchEvent(new WindowEvent(loaderGUI, WindowEvent.WINDOW_CLOSING));
    }
}