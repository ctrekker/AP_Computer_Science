//© A+ Computer Science  -  www.apluscompsci.com
//Name - Connor Burns
//Date - 8/16/2017
//Class - Period 7
//Lab  - AsciiBox

public class AsciiBoxBurns {
	public static void main(String[] args) {
		System.out.println("Connor Burns \t  8/16/2017 \n\n" );
		
		char specialChar='A';
      char mainChar='+';
      int[] specialIndexes={3,4,8,9,13,14};
		for(int i=0; i<18; i++) {
			if(arrayContains(specialIndexes, i)) {
            System.out.println(repeatChar(specialChar, 25));
         }
         else {
            System.out.println(repeatChar(mainChar, 25));
         }
		}
	}
   public static String repeatChar(char character, int repeat) {
      String out="";
      for(int i=0; i<repeat; i++) {
         out+=character;
      }
      return out;
   }
   public static boolean arrayContains(int[] array, int val) {
      for(int i=0; i<array.length; i++) {
         if(array[i]==val) return true;
      }
      return false;
   }
}