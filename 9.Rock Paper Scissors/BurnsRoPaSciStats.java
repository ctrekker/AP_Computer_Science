import javax.swing.*;
import java.awt.event.*;
// Stat window class used to display stats
public class BurnsRoPaSciStats extends JFrame {
   // Require wins, losses, and ties to display
   // listener will be attached to the reset button
   public BurnsRoPaSciStats(int wins, int losses, int ties, ActionListener listener) {
      setTitle("Your Stats");
      setSize(200, 100);
      
      // Container for Component objects
      JPanel main = new JPanel();
      
      // Label containing the stats
      JLabel statsLabel = new JLabel();
      // Percentage of wins
      double percent;
      // Avoid a ArithmeticException (divide by zero)
      if(wins+losses!=0) percent=((double)wins/(wins+losses))*100;
      // Set it to zero if you try to divide by zero
      else percent=0;
      // Create a simple html skeleton with line breaks in between
      statsLabel.setText(String.format("<html>&nbsp;&nbsp;Wins: %s<br>&nbsp;&nbsp;Losses: %s<br>&nbsp;&nbsp;Ties: %s<br>&nbsp;&nbsp;Win Percentage: %.2f%%</html>", wins, losses, ties, percent));
      
      // Create a reset button
      JButton resetButton = new JButton("Reset");
      // Attach the supplied listener to the button
      resetButton.addActionListener(listener);
      
      // Add components to container
      main.add(resetButton);
      main.add(statsLabel);
      
      // Add container to frame
      add(main);
      // Pack the screen to smallest form
      pack();
      
      // Set the window to be visible
      setVisible(true);
   }
}