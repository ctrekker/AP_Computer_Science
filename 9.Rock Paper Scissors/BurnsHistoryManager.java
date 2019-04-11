import javax.swing.*;
import java.io.*;
import java.util.*;

/*
This is a somewhat complicated class which serves as the entire interface used to track, log, modify, save, and retrieve
all game history.

Feature list:
 - Can track all important rock paper scissors values (scores, etc.)
 - For each user, it generates an entry under their name to individualize scores
 - Tracks the time range which the user participated in the app
 - Can be SAVED to a local file on your computer. As of now, the file name is ".burnshistorycache"
 - Provides a method which automatically creates a JFrame through only the data within its own instance variables
 - Can create a new instance of itself from an existing file
 - Automatically parses through a history cache file and generates an instance of itself with the data

Pretty cool!
 */
public class BurnsHistoryManager {
   // Splitter used to split the file contents into readable parts
   private static final String SPLITTER = "<->";
   // ArrayList to store all of the different entries in an object oriented manner
   private ArrayList<BurnsHistoryEntry> entries;
   // "Parameter-less" constructor. Must use methods to add data.
   public BurnsHistoryManager() {
      entries=new ArrayList<>();
   }
   // Adds an entry
   public void addEntry(BurnsHistoryEntry entry) {
      entries.add(entry);
   }
   // Gets an entry
   public BurnsHistoryEntry getEntry(int index) {
      return entries.get(index);
   }
   // Gets last entry in ArrayList
   // Also makes sure that the index exists
   public BurnsHistoryEntry lastEntry() {
      try{
         return entries.get(entries.size()-1);
      } catch (ArrayIndexOutOfBoundsException e) {
         return null;
      }
   }
   // Gets how many entries there are
   public int getEntriesLength() {
      return entries.size();
   }
   // Gets the ArrayList directly for more advanced operations (slice, etc)
   // Currently unused, and is here mainly for "extendability"
   public ArrayList<BurnsHistoryEntry> getEntries() {
      return entries;
   }
   // Powerful method which generates and displays a table of historic users
   public void showGUI() {
      // Bland frame/panel init
      JFrame frame=new JFrame();
      frame.setTitle("History");
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      JPanel panel=new JPanel();

      // Array of column headers (used for JTable constructor)
      Object[] titles={"Name", "Session start", "Session length", "Wins", "Losses", "Ties"};
      // Init a JTable, then add it to a scrollable pane in case overflow occurs
      JTable historyTable=new JTable(getTableData(entries), titles);
      panel.add(new JScrollPane(historyTable));

      // More bland frame/panel init
      frame.add(panel);
      frame.pack();
      frame.setVisible(true);
   }
   // Saves the entire object into something a future instance of the project can read
   // Requires a filename to save to, which will also be required for getting data on
   // program init
   public void saveToFile(String fileName) throws IOException {
      // New file object to perform filesystem operations (other than read and write)
      File saveFile=new File(fileName);
      // If the file doesn't already exist, create it
      // If it does, the program will just overwrite it
      if(!saveFile.exists()) saveFile.createNewFile();
      // Create an object that will serve as a writer for the file
      // I used PrintWriter because its methods are exactly that of
      // System.out, as System.out is an instance in itself of PrintWriter
      // Also, BufferedWriter and FileWriter serve as the actual adapters to
      // "connect" to the file, so to speak
      PrintWriter writer=
            new PrintWriter(
               new BufferedWriter(
                  new FileWriter(fileName, false)));
      // Loop through each entry, and generate the readable line
      // using concatenation and splitters...
      for(int i=0; i<entries.size(); i++) {
         BurnsHistoryEntry e=entries.get(i);
         BurnsStatsEntry stats=e.getStats();
         writer.println(
                 e.getName()            + SPLITTER
               + e.getStart().getTime() + SPLITTER
               + e.getLength()          + SPLITTER
               + stats.getWins()        + SPLITTER
               + stats.getLosses()      + SPLITTER
               + stats.getTies());
      }
      // Close the writer to prevent memory leaking
      // Even if this line was omitted, it probably wouldn't make much of a
      // difference because this method isn't called very often.
      writer.close();
   }
   // Utility function to convert the array of entries to a primitive object array
   private Object[][] getTableData(ArrayList<BurnsHistoryEntry> list) {
      Object[][] out=new Object[list.size()][6];
      for(int i=0; i<list.size(); i++) {
         BurnsHistoryEntry currentEntry=list.get(i);
         out[i]=getTableRow(currentEntry);
      }

      return out;
   }
   // Utility function to convert a single entry into a one-dimensional primitive array
   private Object[] getTableRow(BurnsHistoryEntry currentEntry) {
      return new Object[]{
            currentEntry.getName(),
            currentEntry.getStartFormatted(),
            Math.round(currentEntry.getLength()/1000)+" seconds",
            currentEntry.getStats().getWins(),
            currentEntry.getStats().getLosses(),
            currentEntry.getStats().getTies()
      };
   }
   // USED FOR DEBUGGING
   // Just prints out string representations of each entry
   public String toString() {
      String out="";
      for(int i=0; i<entries.size(); i++) {
         out+=entries.get(i).toString()+"\n";
      }
      return out;
   }
   /*
   Another powerful function which returns an instance of itself with the decoded contents of
   the given file path. Used for loading the cache file, then resuming the application state
   even after being completely terminated then reopened. In other words, it transfers the saved
   state of the application on the hard drive to RAM.
    */
   public static BurnsHistoryManager loadFromSaveFile(String fileName) {
      // File instance to perform filesystem actions
      File saveFile=new File(fileName);
      // Make sure the file actually exists
      if(saveFile.exists()) {
         // Make a new manager, which will contain the decoded data from the file
         // Also, this will be returned
         BurnsHistoryManager manager=new BurnsHistoryManager();
         try {
            // Create a new buffered reader so we can loop through
            // each line individually, instead of having to perform
            // a split by newline operation
            BufferedReader reader =
                  new BufferedReader(
                        new FileReader(fileName));
            // Init a variable to store line data
            String line;
            // Make sure the file has another line to read
            // If it does, then.....
            while((line = reader.readLine()) != null) {
               // Split the line by the splitter
               String[] entryData=line.split(SPLITTER);
               // Make a new stats entry, and provide it with the corresponding data in the array resulting from the split operation
               BurnsStatsEntry statsEntry=new BurnsStatsEntry(Integer.parseInt(entryData[3]), Integer.parseInt(entryData[4]), Integer.parseInt(entryData[5]));
               // Make a new history entry, and provide it with the corresponding data...
               BurnsHistoryEntry entry=new BurnsHistoryEntry(entryData[0], new Date(Long.parseLong(entryData[1])), Long.parseLong(entryData[2]), statsEntry);
               // Add this entry to the parent manager
               manager.addEntry(entry);
            }
         }
         catch(FileNotFoundException e) {
            // NOT fatal. It will just return an empty BurnsHistoryManager object
            // In theory this should never happen, because we have an if statement
            // earlier which checks to make sure the file exists.
            System.out.println("WARNING: Unable to resume previous application state: cache file does not exist yet.");
            System.out.println("INFO: Cache file creation added to que");
         }
         // After we are FINALLY done with all this crazy stuff,
         // return the manager.
         // See what I did there ;)
         finally {
            return manager;
         }
      }
      // Return an empty history manager if the cache file doesn't acutally exist
      // This will happen the first time the program is run, as the file is automatically
      // generated on its own.
      else return new BurnsHistoryManager();
   }
}
