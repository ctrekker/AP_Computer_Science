import java.text.SimpleDateFormat;
import java.util.Date;

// Utility class to store history info
// Nothing too special here, just saves
// instance variables and offers ways to
// modify them.
public class BurnsHistoryEntry {
   private String name;
   private Date start;
   private long length;
   private BurnsStatsEntry stats;

   public BurnsHistoryEntry(String name, Date start, long length, BurnsStatsEntry stats) {
      this.name=name;
      this.start=start;
      this.length=length;
      this.stats=stats;
   }
   // Used for debugging -- called by BurnsHistoryManager's toString() method
   public String toString() {
      return "{name:"+name+",start:"+start+",length:"+length+",stats:"+stats.toString()+"}";
   }

   // Getters and setters of the instance variables.
   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }
   public Date getStart() {
      return start;
   }
   // Return a formatted version of the "start" instance variable
   // Used for GUI table generation in BurnsHistoryManager
   public String getStartFormatted() {
      SimpleDateFormat format=new SimpleDateFormat("hh:mm a, MM/dd/yy");
      return format.format(start);
   }
   public void setStart(Date start) {
      this.start = start;
   }
   public long getLength() {
      return length;
   }
   public void setLength(long length) {
      this.length = length;
   }
   public BurnsStatsEntry getStats() {
      return stats;
   }
   public void setStats(BurnsStatsEntry stats) {
      this.stats = stats;
   }
}
