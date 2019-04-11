public class FirstAndLastBurns {
   public FirstAndLastBurns(String[] words) {
      for(int i=0; i<words.length; i++) {
         System.out.println("Word :: "+words[i]);
         System.out.println("first letter :: "+words[i].charAt(0));
         System.out.println("last letter :: "+words[i].charAt(words[i].length()-1)+"\n");
      }
   }
   public static void main(String[] args) {
      String[] words = {"Hello", "World", "JukeBox", "TCEA", "UIL"};
      new FirstAndLastBurns(words);
   }
}