import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*************************************************************************
 *  Fifth week coursera course Java Algorithms part 2
 *  Burrows-Wheeler data compression
 *  @Date 15.03.14
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/burrows.html
 *             http://coursera.cs.princeton.edu/algs4/checklists/burrows.html
 *             http://algs4.cs.princeton.edu/windows/
 *             http://stackoverflow.com/questions/15008099/how-to-use-libraries-for-algorithms-part-i-coursera-course-in-eclipse
 *  Compilation:   javac-algs4 BurrowsWheeler.java
 *  Execution:     java-algs4 BurrowsWheeler <->/<+> < <name_of_the_data_file>
 *                   (e.g., java-algs4 BurrowsWheeler - < ../test_files/abra.txt)
 *                 To watch Hex output use HexDump (from algs4) it must be compiled
 *                   (e.g., javac-algs4 HexDump.java;
 *                      java-algs4 BurrowsWheeler - < ../test_files/abra.txt | 
 *                                  java-algs4 HexDump 16)
 *  
 *  Burrows-Wheeler transform. More details 
 *    http://coursera.cs.princeton.edu/algs4/assignments/burrows.html
 *    http://coursera.cs.princeton.edu/algs4/checklists/burrows.html
 *                     
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  <p>
 *  Usage of general Burrows-Wheeler data compression:
 *  Compress:
 *  java-algs4 BurrowsWheeler - < <file_name_to_compress> | java-algs4 MoveToFront - | 
 *    java-algs4 Huffman - > <file_name_of_compressed_file>
 *  Uncompress:
 *  java-algs4 Huffman + < <file_name_of_compressed_file> | java-algs4 MoveToFront + | 
 *    java-algs4 BurrowsWheeler + > <file_name_of_uncompressed_file>  
 *  <p>
 *  
 *************************************************************************/

public class BurrowsWheeler {
   
   private static final int CHAR_BITS = 8;
   
   /**
    *  Apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    */
   public static void encode() {
      // read string from std input
      String input = BinaryStdIn.readString();
      // create circ suff arr for it
      CircularSuffixArray circularSuffixArray = new CircularSuffixArray(input);
      // look for first row in original suffix with 0 offset(index[i] = 0)
      for (int i = 0; i < circularSuffixArray.length(); i++) {
         if (circularSuffixArray.index(i) == 0) {
            // output number of first row in sorted suffixes
            BinaryStdOut.write(i);
            break;
         }
      }
      // make output as last chars of sorted suffixes
      for (int i = 0; i < circularSuffixArray.length(); i++) {
         int index = circularSuffixArray.index(i);
         if (index == 0) {
            BinaryStdOut.write(input.charAt(input.length() - 1), CHAR_BITS);
            continue;
         }
         BinaryStdOut.write(input.charAt(index - 1), CHAR_BITS);
      }
      // BinaryStdOut must be closed
      BinaryStdOut.close();
   }

   /**
    *  Apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    */
   public static void decode() {
      // take first, t[] from input
      int first = BinaryStdIn.readInt();
      String chars = BinaryStdIn.readString();
      char[] t = chars.toCharArray();
      chars = null;
      // construct next[]
      int next[] = new int[t.length];
      // Algorithm: Brute Force requires O(n^2) =>
      // go through t, consider t as key remember positions of t's in the Queue
      Map<Character, Queue<Integer>> positions = new HashMap<Character, Queue<Integer>>();
      for (int i = 0; i < t.length; i++) {
         if(!positions.containsKey(t[i]))
            positions.put(t[i], new Queue<Integer>());
         positions.get(t[i]).enqueue(i);
      }
      // get first chars array
      Arrays.sort(t);
      // go consistently through sorted firstChars array
      for (int i = 0; i < t.length; i++) {
         next[i] = positions.get(t[i]).dequeue();
      }
      // decode msg
      // for length of the msg
      for (int i = 0, curRow = first; i < t.length; i++, curRow = next[curRow])
         // go from first to next.
         BinaryStdOut.write(t[curRow]);
      BinaryStdOut.close();
   }

   /**
    * Call encode or decode of Burrows Wheeler transform
    * @param args
    *    args[0] = '-' apply Burrows-Wheeler encoding or   
    *    '+' apply Burrows-Wheeler decoding; 
    *    args[1] File to transform 
    */
   public static void main(String[] args) {
      if (args.length == 0) 
         throw new java.lang.IllegalArgumentException("Usage: input '+' for encoding or '-' for decoding");
      if (args[0].equals("-")) 
         encode();
      else if (args[0].equals("+")) 
         decode();
      else 
         throw new java.lang.IllegalArgumentException("Usage: input '+' for encoding or '-' for decoding");
   }

}
