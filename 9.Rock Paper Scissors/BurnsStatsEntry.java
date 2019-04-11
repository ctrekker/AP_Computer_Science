// Same sort of deal as the BurnsHistoryEntry class
// It just stores variables in an object oriented
// manner.
public class BurnsStatsEntry {
   private int wins;
   private int losses;
   private int ties;
   public BurnsStatsEntry(int wins, int losses, int ties) {
      this.wins=wins;
      this.losses=losses;
      this.ties=ties;
   }
   // Used for debugging -- called by BurnsHistoryEntry's toString() method
   public String toString() {
      return "{wins:"+wins+",losses:"+losses+",ties:"+ties+"}";
   }

   // Private variable getters and setters
   // Might not be used, but good to have for
   // utility and expandability options
   public int getWins() {
      return wins;
   }
   public void setWins(int wins) {
      this.wins = wins;
   }
   public int getLosses() {
      return losses;
   }
   public void setLosses(int losses) {
      this.losses = losses;
   }
   public int getTies() {
      return ties;
   }
   public void setTies(int ties) {
      this.ties = ties;
   }
}
