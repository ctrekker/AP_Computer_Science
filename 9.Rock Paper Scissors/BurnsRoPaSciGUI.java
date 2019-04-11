import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Date;

// GUI Managment class
public class BurnsRoPaSciGUI extends JFrame {
   private final String initNameHTML=getNameHTML("<i>Please type your name</i>");

   // Utility class for calculating the computer's choice
   private BurnsComputer computer = new BurnsComputer();
   // Name of file to save cache to. More details in BurnsHistoryManager class
   private String historyFileName = ".burnsrpshistorycache";
   // Utility class for generating and saving gameplay history
   private BurnsHistoryManager historyManager = BurnsHistoryManager.loadFromSaveFile(historyFileName);
   // Used to check whether or not the historyManager has changed in length
   private int historyStartSize=0;
   private int wins, losses, ties;
   private boolean playRound=true;
   // The label holding the response
   // i.e. "You win!", "You lose", etc.
   private JLabel response, intro, nameLabel;
   private JTextField nameInput;
   // Define buttons inside class so they can be accessed
   // elsewhere
   private JButton rock, paper, scissor, stats, close, nameButton, historyButton;
   private String playerName="";

   public BurnsRoPaSciGUI() {
      // Baseline start value (initial size after file load)
      historyStartSize=historyManager.getEntriesLength();

      // Set window properties
      // title, size, and defaultcloseoperation
      setTitle("Play Rock, Paper, Scissors!");
      setSize(360, 270);
      setResizable(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      // Detect window closing, and force a file save to prevent corruption
      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            // See updateHistoryEntry() for more
            updateHistoryEntry();
         }
      });

      // JComponents used to display and retrieve the user's name
      nameLabel=new JLabel(initNameHTML);
      nameInput=new JTextField(16);
      nameButton=new JButton("Change Name");
      nameLabel.setFont(new Font("Ariel", Font.PLAIN, 20));
      nameButton.addActionListener(new ButtonManager("name"));
      
      // Sets rock, paper, and scissor variables to
      // their appropriate JButtons
      rock=new JButton("Rock");
      paper=new JButton("Paper");
      scissor=new JButton("Scissors");
      stats=new JButton("Stats");
      // Used to ask the user whether or not they
      // want to play again.
      close=new JButton("No");
      close.setVisible(false);
      // Shows history
      historyButton=new JButton("View History");
      
      // Assign JLabel initally to contain instructions
      // Will be overwritten after first play
      response=new JLabel("");
      response.setFont(new Font("Ariel", Font.PLAIN, 14));
      intro=new JLabel("<html>Press \"Rock\", \"Paper\", or \"Scissors\" to begin!<br><i>To check stats, click \"Stats\".</i></html>");
      intro.setFont(new Font("Ariel", Font.PLAIN, 14));
      
      // Attach action listeners to the buttons
      rock.addActionListener(new ButtonManager("rock"));
      paper.addActionListener(new ButtonManager("paper"));
      scissor.addActionListener(new ButtonManager("scissor"));
      stats.addActionListener(new ButtonManager("stats"));
      close.addActionListener(new ButtonManager("close"));
      historyButton.addActionListener(new ButtonManager("history"));

      // Set background color to get rid of that ugly gradient background that
      // appears on windows computers.
      rock.setBackground(   Color.ORANGE);
      paper.setBackground(  Color.ORANGE);
      scissor.setBackground(Color.ORANGE);
      stats.setBackground(Color.LIGHT_GRAY);
      close.setBackground(Color.RED);
      nameButton.setBackground(Color.LIGHT_GRAY);
      historyButton.setBackground(Color.WHITE);

      JPanel panel=new JPanel();
      panel.setBackground(Color.WHITE);
      // Creates a nicer looking area, as this creates 10 pixels of space on
      // all four sides (margin)
      panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      // Add the components to a JPanel
      panel.add(nameLabel);
      panel.add(nameInput);
      panel.add(nameButton);
      panel.add(intro);
      panel.add(rock);
      panel.add(paper);
      panel.add(scissor);
      panel.add(response);
      panel.add(stats);
      panel.add(close);
      panel.add(historyButton);
      
      // Add the JPanel to the JFrame contentPane
      add(panel);
      
      // Set the frame to be visible
      setVisible(true);
   }
   
   // Button action listener utility class
   private class ButtonManager implements ActionListener {
      // Name of button
      // Used to identify whether "rock", "paper",
      // or "scissor" was pressed
      private String name;
      public ButtonManager(String name) {
         this.name=name;
      }
      public void actionPerformed(ActionEvent e) {
         /*
         Tests to make sure the user has inputted their name. If not, it will show a message dialog telling
         them to do so. The two buttons that can be pressed without a user are obviously the "name" button,
         which registers the user, as well as the history button.
          */
         if(playerName.equals("")&&!name.equals("name")&&!name.equals("history")) {
            JOptionPane.showMessageDialog(null, "Please enter your name first");
            return;
         }

         // If the button pressed was the "No" button (id=close), then re-initialize the player name,
         // reset the name label, and update all history entries.
         // See BurnsHistoryManager for more info on history registration
         if(name.equals("close")) {
            updateHistoryEntry();
            playerName="";
            nameLabel.setText(initNameHTML);
         }
         // Check if the button pressed was the name button, which registers the user
         else if(name.equals("name")) {
            // Update the history entries
            // See BurnsHistoryManager for more info on history registration
            updateHistoryEntry();
            // Perform name set operations
            if(!nameInput.getText().equals("")) {
               nameLabel.setText(getNameHTML(nameInput.getText()));
               nameInput.setText("");
            }
            else {
               nameLabel.setText(getNameHTML("Anonymous"));
               nameInput.setText("");
            }
            // Reset individualized player stats
            wins=0;
            losses=0;
            ties=0;
            // Add a new history entry for the new user
            addHistoryEntry();
         }
         /*
          Below is a condition making sure that these conditions are true:
          (1) A gameplay button (rock, paper, scissors, etc) was pressed, and not a utility button like "stats"
          (2) Make sure the frame is in the playRound state, which means it is asking the user to pick their weapon (rock, paper, or scissors)
           */
         if((!name.equals("stats")&&!name.equals("name")&&!name.equals("history")) || !playRound) {
            // Assign a variable to hold the response message
            String msg="";
            // Get a random choice from the computer
            // Either "rock" "paper" or "scissor"
            // See BurnsComputer.java for more info
            String compChoice="";
            if(playRound) {
               compChoice = computer.nextChoice();
               if(name.equals(compChoice)) {
                  // If the computer's choice and the user's choice
                  // are the same, the game is a tie.
                  msg="It's a tie!";
                  ties++;
               }
               // Check for every possible player win scenario.
               // If any of the statements within the parenthesis
               // are true, it means the user won.
               else if(  (name.equals("rock")    && compChoice.equals("scissor"))
                       ||(name.equals("scissor") && compChoice.equals("paper"))
                       ||(name.equals("paper")   && compChoice.equals("rock")))
               {
                  msg="You win!";
                  wins++;
               }
               // Any other combinations mean a loss for the player
               else {
                  msg="You lose!";
                  losses++;
               }
            }

            // After a gameplay button press, switch the JFrame's state to the opposite
            playRound=!playRound;

            // Assign visibility values based on the JFrame's state
            // NOTE: Unchanged visibility values are either unchanged,
            //       or recycled for reuse with different content
            //         i.e. stats component
            rock.setVisible(playRound);
            paper.setVisible(playRound);
            scissor.setVisible(playRound);
            
            close.setVisible(!playRound);
            
            response.setVisible(!playRound);
            intro.setVisible(playRound);

            historyButton.setVisible(playRound);

            // The stats button serves as both the
            // "Yes, I want to play again" button, and the
            // "Stats" button.
            if(playRound) {
               stats.setText("View Stats");
            }
            else {
               stats.setText("Yes");  
            }
            
            /*
            Generate an html skeleton containing all of the
            desired output. To form newlines, the html tag
            "<br>" is used. 
            
            OFFICIAL W3C DOCS
            https://www.w3.org/wiki/HTML/Elements/br
            */
            msg="<html>You selected "+name+"<br>"
                  +"The computer chose "+compChoice+"<br>"
                  +""+msg+"<br><b>Would you like to play again?</b>"
                  +"<br>------------------------------------------------------------------</html>";
                  
            // Set the response label to the generated html markup
            response.setText(msg);
         }
         // Check if the button pressed was the "history" button
         else if(name.equals("history")) {
            // Update history entries
            // See BurnsHistoryManager for more info on history registration
            updateHistoryEntry();
            // Use BurnsHistoryManager's built-in showGUI function to automatically
            // open a window containing the application's history
            historyManager.showGUI();
         }
         else if(name.equals("stats")) {
            /*
            If the button pressed was stats, then perform these operations:
            1. Create a new listener devoted to when the user presses the
               "reset" button on the popup
            2. Put appropriate date (wins, losses, ties, and the reset listener)
               as parameters for a new stats window
            3. Call setWindow with the stats window as a parameter. This registers
               the current window with the listener class so the listener has the
               power to manipulate the window. In this listener's case, it uses the
               window reference to close the window, and open a new one with the 
               updated stats. See ResetStatsListener action handler for more details. 
            */
            ResetStatsListener statListener = new ResetStatsListener(); 
            BurnsRoPaSciStats window = new BurnsRoPaSciStats(wins, losses, ties, statListener);
            statListener.setWindow(window);
         }
         // Updates the history entries
         // If certain conditions are true, this may be redundant. However, it
         // is best in this case to be redundant in case the first file write fails.
         // See BurnsHistoryManager for more info on history registration
         updateHistoryEntry();
      }
   }
   // Adds a new user to the history entries with the current stats, which
   // in most cases are default stats (wins=0,losses=0,ties=0,etc)
   private void addHistoryEntry() {
      // Temporary stats entry to be added to the history entry as a parameter
      // Will be saved as instance variable
      BurnsStatsEntry statsEntry=new BurnsStatsEntry(wins, losses, ties);
      // History entry to be added to the BurnsHistoryManager object
      BurnsHistoryEntry newEntry=new BurnsHistoryEntry(playerName, new Date(), 0, statsEntry);
      historyManager.addEntry(newEntry);
      // Save the current history to a cached file.
      // See method for more info
      saveHistoryManager();
   }
   // Updates the current user with the updated stats and other things like the session length
   private void updateHistoryEntry() {
      // Makes sure not to modify an entry created by a previous instance of the program
      if(historyStartSize!=historyManager.getEntriesLength()) {
         // Gets the last entry added, and syncs its instance variables with that of this class's
         BurnsHistoryEntry lastEntry = historyManager.lastEntry();
         // Make sure the last entry actually exists
         if (lastEntry != null) {
            // Update all of its dynamic values with updated ones
            lastEntry.setLength(new Date().getTime() - lastEntry.getStart().getTime());
            lastEntry.getStats().setWins(wins);
            lastEntry.getStats().setLosses(losses);
            lastEntry.getStats().setTies(ties);
         }
      }
      // Save the current history to a cached file.
      // See method for more info
      saveHistoryManager();
   }
   /*
   This is a very important method which serves as a simple utility method to access
   BurnsHistoryManager's saveToFile method. Because it throws an IOException due to
   various data streams, it needs to be caught. This method automatically catches a
   possible exception, and prints an error message to the console. In a perfect world,
   the exception would never be thrown. But alas, not every computer likes to cooperate,
   especially dealing with its file system.
    */
   private void saveHistoryManager() {
      try {
         // Attempt a save operation
         historyManager.saveToFile(historyFileName);
      }
      catch(IOException exception) {
         // Print out an error message to the system's PrintStream object
         System.out.println("Unable to save history cache file!");
      }
   }

   /*
   Listener used to reset stats and update the stats window.
   */
   private class ResetStatsListener implements ActionListener {
      /*
      Required for window closing and opening. It dispatches the close event,
      which the JFrame responds to by disposing of its contents and closing the
      frame.
      
      IMPORTANT
      *** Program will throw NullPointerException if not set with setWindow. This
      *** in theory should never occur in production, though, since setWindow is always
      *** called after initiating a stat window and stat reset listener.
      */
      private BurnsRoPaSciStats window;
      // Action event listener. Overrides parent interface
      public void actionPerformed(ActionEvent e) {
         // Set wins, losses, and ties to zero, their default value
          wins=0;
          losses=0;
          ties=0;
         // Close the window by dispatching an event which simulates a click on the X button
         window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
         
         // Register a new Jframe to update the stats.
         ResetStatsListener statListener = new ResetStatsListener(); 
         BurnsRoPaSciStats window = new BurnsRoPaSciStats(wins, losses, ties, statListener);
         statListener.setWindow(window);
      }
      // Important utility class for setting the stat window
      public void setWindow(BurnsRoPaSciStats window) {
         // Refer to "this" to avoid variable name conflict
         this.window=window;
      }
      
      
   }
   // Simple utility class which gets html code for displaying the name.
   // For more info on HTML, its specs, and why I used it visit line
   private String getNameHTML(String name) {
      this.playerName=name;
      return "<html>***  Your name is  ***<br><center>"+name+"</center></html>";
   }
}

/*
Check out the HTML and JavaScript Rock, Paper, Scissors
I made when I was in 4th grade!
http://ctrekker.mjtrekkers.com/games/data/RockPaperScissors.php

One of my latest simplistic games:
http://ctrekker.mjtrekkers.com/games/data/falling_cubes/

A cool little moon phases simulation:
http://ctrekker.mjtrekkers.com/school/MoonPhases/#0.5
*/