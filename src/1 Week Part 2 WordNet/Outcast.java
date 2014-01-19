

/*************************************************************************
 *  First week coursera course Java Algorithms part 2
 *  Outcast
 *  @Date 19.01.14
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html
 *             http://coursera.cs.princeton.edu/algs4/checklists/wordnet.html
 *             http://algs4.cs.princeton.edu/windows/
 *  Compilation:   javac-algs4 Outcast.java with uncommented main method
 *  Execution:     java-algs4 Outcast <name of Princeton formatted synset file> 
 *                   <name of Princeton formatted hypernyms file> 
 *                   <name of Princeton formatted outcast file 1>
 *                   ...
 *                   <name of Princeton formatted outcast file n>
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  Calculation of outcast word.
 *
 *************************************************************************/

public class Outcast {
   
   private WordNet wordnet;
   
   private String outcast;
   
   private int distance;
   
   // constructor takes a WordNet object
   public Outcast(WordNet wordnet) {
      this.wordnet = wordnet;
      this.outcast = null;
      this.distance = -1;
   }
   
   // given an array of WordNet nouns, return an outcast
   public String outcast(String[] nouns) {
      int curDistance = 0;
      String curOutacast = nouns[0];
      for (String thisNoun : nouns) {
         int thisNounDistance = 0;
         for (String thatNoun : nouns) {
            thisNounDistance += wordnet.distance(thisNoun, thatNoun);
         }
         if (thisNounDistance > curDistance) {
            curDistance = thisNounDistance;
            curOutacast = thisNoun;
         }
      }
      distance = curDistance;
      outcast = curOutacast;
      return outcast;
   }
   
   // for unit testing of this class (such as the one below)
   public static void main(String[] args) {
      WordNet wordnet = new WordNet(args[0], args[1]);
      Outcast outcast = new Outcast(wordnet);
      for (int t = 2; t < args.length; t++) {
         String[] nouns = In.readStrings(args[t]);
         StdOut.println(args[t] + ": " + outcast.outcast(nouns));
      }
   }
}
