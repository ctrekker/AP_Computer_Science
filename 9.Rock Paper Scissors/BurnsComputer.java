public class BurnsComputer {
   public BurnsComputer() {
   
   }
   public String nextChoice() {
      // Switch a random variable between
      // 1 and 3.
      switch((int)Math.ceil(Math.random()*3)) {
         case 1: return "rock";
         case 2: return "paper";
         case 3: return "scissor";
      }
      // This in theory, should never happen
      // If it did, the JLabel (response) would 
      // just dissapear
      // The only case which this would ever
      // happen was if Math.random errored out
      // and returned a value greater or less than
      // possible given the formula in the switch
      // statements.
      return null;
   }
}