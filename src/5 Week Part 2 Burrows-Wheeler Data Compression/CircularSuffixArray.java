import java.util.Arrays;
import java.util.Comparator;

/*************************************************************************
 *  Fifth week coursera course Java Algorithms part 2
 *  Burrows-Wheeler data compression
 *  @Date 13.03.14
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/burrows.html
 *             http://coursera.cs.princeton.edu/algs4/checklists/burrows.html
 *             http://algs4.cs.princeton.edu/windows/
 *  Compilation:   javac-algs4 CircularSuffixArray.java
 *  Execution:     java-algs4 CircularSuffixArray with uncommented main method
 *  
 *  Circular suffix array data structure. More details 
 *    http://coursera.cs.princeton.edu/algs4/assignments/burrows.html
 *                     
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *************************************************************************/

public class CircularSuffixArray {
   
   private String input;
   
   private Integer[] index;
   
   /**
    *  Constructs circular suffix array of input String s
    * @param s Input String
    */
   public CircularSuffixArray(String s) {
      if  (s == null || s.equals("") ) 
         throw new java.lang.IllegalArgumentException("Can't get suffix array for empty string!");
      input = s;
      index = new Integer[length()];
      for (int i = 0; i < index.length; i++) 
         index[i] = i;
      
      // algorithm: not to store strings; just compare them using number of shifts
      Arrays.sort(index, new Comparator<Integer>() {
         @Override
         public int compare(Integer first, Integer second) {
            // get start indexes of chars to compare
            int firstIndex = first;
            int secondIndex = second;
            // for all characters
            for (int i = 0; i < input.length(); i++) {
               // if out of the last char then start from beginning
               if (firstIndex > input.length() - 1) 
                  firstIndex = 0;
               if (secondIndex > input.length() - 1) 
                  secondIndex = 0;
               // if first string > second
               if (input.charAt(firstIndex) < input.charAt(secondIndex)) 
                  return -1;
               else if (input.charAt(firstIndex) > input.charAt(secondIndex)) 
                  return 1;
               // watch next chars
               firstIndex++;
               secondIndex++;
            }
            // equal strings
            return 0;
         }
      });
   }
   
   /**
    *  Length of circular array string 
    */
   public int length() { return input.length(); }
   
   /**
    *  Returns ordinal row (index) in the original suffix of ith sorted suffix
    * @param i Ordinal number of sorted index 
    */
   public int index(int i) { return index[i]; }
   
   /**
    * Test
    */
   public static void main(String[] args) {
      CircularSuffixArray csa = new CircularSuffixArray("AAA\n");
      for (int i = 0; i < csa.length(); i++ ) System.out.println(csa.index(i) );
      System.out.println();
      csa = new CircularSuffixArray("ABRACADABRA!");
      for (int i = 0; i < csa.length(); i++ ) System.out.println(csa.index(i) );
   }
}
