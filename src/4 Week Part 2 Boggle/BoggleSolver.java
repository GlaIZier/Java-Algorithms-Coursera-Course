import java.util.Set;
import java.util.TreeSet;

/*************************************************************************
 *  Forth week coursera course Java Algorithms part 2
 *  Boggle
 *  @Date 23.02.14
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/boggle.html
 *             http://coursera.cs.princeton.edu/algs4/checklists/boggle.html
 *             http://algs4.cs.princeton.edu/windows/
 *  Compilation:   javac-algs4 BoggleSolver.java
 *  Execution:     java-algs4 BoggleSolver <name_of_princeton_format_dictionary_txt_file> 
 *                     <name_of_princeton_format_Boggle_board_txt_file> 
 *                     with uncommented main method
 *  
 *  Examples of the format of txt files are in test folder
 *                     
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  
 *************************************************************************/

public class BoggleSolver {
   
   private static final char Q_LETTER = 'Q';
   
   private static final String QU_STRING = "QU";
   
   private final FastPrefixTST<Integer> dictionary;
   
   // Initializes the data structure using the given array of strings as the dictionary.
   // (each word in the dictionary contains only the uppercase letters A through Z.)
   public BoggleSolver(final String[] dictionary) {
      // shuffle alphabet dictionary to create more balanced trie
      shuffle(dictionary);
      this.dictionary = new FastPrefixTST<Integer>();
      int[] points = {0, 0, 0, 1, 1, 2, 3, 5, 11};
      for (String s : dictionary) {
         // if maxpoint word
         if (s.length() >= points.length - 1) {
            this.dictionary.put(s, points[points.length - 1]);
         }
         else {
            this.dictionary.put(s, points[s.length()]);
         }
      }
   }

   // Knuth shuffling algorithm
   private void shuffle(String[] dictionary) {
      for (int i = 1; i < dictionary.length; i++) {
         // choose random string from left part of i including i
         int randLeftIndex = (int) (Math.random() * (i + 1) );
         String buffer = dictionary[randLeftIndex];
         dictionary[randLeftIndex] = dictionary[i];
         dictionary[i] = buffer;
      }
   }

   // Returns the set of all valid words in the given Boggle board, as an Iterable.
   public Iterable<String> getAllValidWords(BoggleBoard board) {
      if (board == null) 
         throw new java.lang.IllegalArgumentException("Board is null!");
      // choose set because each word must appears only once
      Set<String> foundWords = new TreeSet<String>();
      // for all dices as first letter
      for (int row = 0; row < board.rows(); row++) {
         for (int col = 0; col < board.cols(); col++) {
            // current watching string
            String charSequence = addLetter("", board.getLetter(row, col));
            // array with visited dices
            boolean marked[][] = new boolean[board.rows()][board.cols()];
            marked[row][col] = true;
            dfs(foundWords, charSequence, marked, row, col, board);
         }
      }
      return foundWords;
   }

   // depth first search for searching valid words
   private void dfs(Set<String> foundWords, String charSequence, boolean[][] marked,
         int startRow, int startCol, BoggleBoard board) {
      // add valid word to set
      if (isValidWord(charSequence) ) foundWords.add(charSequence);
      // check all adjacent dices 
      // max & min methods to avoid going beyond the borders of board
      // start upper left adjacent dice
      for (int row = Math.max(0, startRow - 1); row <= Math.min(board.rows() - 1, startRow + 1); row++) {
         for (int col = Math.max(0, startCol - 1); col <= Math.min(board.cols() - 1,startCol + 1); col++) {
            if (marked[row][col]) continue;
            if (!dictionary.hasPrefix(charSequence)) continue;
            // prepare to recursive call
            marked[row][col] = true;
            dfs(foundWords, addLetter(charSequence, board.getLetter(row, col)), marked, row, col, board);
            // roll back after recursive call
            marked[row][col] = false;
         }
      }
   }

   private String addLetter(String to, char letter) {
      if (letter == Q_LETTER) return to + QU_STRING;
      else return to + letter;
   }
   
   private boolean isValidWord(String currentWord) {
      if (currentWord == null) return false;
      if (dictionary.contains(currentWord) && currentWord.length() > 2) return true;
      else return false;
   }
   
   // Returns the score of the given word if it is in the dictionary, zero otherwise.
   // (the word contains only the uppercase letters A through Z.)
   public int scoreOf(String word) {
      if (word == null || word.length() == 0) 
         throw new java.lang.IllegalArgumentException("You want to score empty string!");
      Integer score = dictionary.get(word);
      if (score == null) 
         return 0;
      else 
         return score;
   }
   
   // Test
   public static void main(String[] args) {
       In in = new In(args[0]);
       String[] dictionary = in.readAllStrings();
       BoggleSolver solver = new BoggleSolver(dictionary);
       BoggleBoard board = new BoggleBoard(args[1]);
       // Stress test
       // BoggleBoard board = new BoggleBoard(10, 11);
       int score = 0;
       for (String word : solver.getAllValidWords(board))
       {
           StdOut.println(word + " " + solver.dictionary.get(word));
           score += solver.scoreOf(word);
       }
       StdOut.println("Score = " + score);
   }
}
