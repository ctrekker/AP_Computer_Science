public class StringCheckerBurns {
   public StringCheckerBurns(String[] words, String[][] keywords) {
      for(int x=0; x<words.length; x++) {
         for(int y=0; y<keywords.length; y++) {
            System.out.println("looking for "+keywords[x][y]+": "+(words[x].indexOf(keywords[x][y])!=-1));
         }
         System.out.println(words[x]+"\n");
      }
   }
   public static void main(String[] args) {
      String[] words = {"chicken", "alligator", "COMPUTER SCIENCE IS THE BEST!"};
      String[][] keywords = {{"c", "ch", "x"},{"g", "all", "gater"},{"U", "COMP SCI", "SCIENCE"}};
      new StringCheckerBurns(words, keywords);
   }
}