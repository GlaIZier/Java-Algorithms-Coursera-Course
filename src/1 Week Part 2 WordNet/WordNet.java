import java.util.HashMap;
import java.util.Map;

/*************************************************************************
 *  First week coursera course Java Algorithms part 2
 *  WordNet
 *  @Date 19.01.14
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html
 *             http://coursera.cs.princeton.edu/algs4/checklists/wordnet.html
 *             http://algs4.cs.princeton.edu/windows/
 *  Compilation:   javac-algs4 WordNet.java with uncommented main method
 *  Execution:     java-algs4 WordNet <name of Princeton formatted synset file> 
 *                   <name of Princeton formatted hypernyms file> 

 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  Representing of WordNet
 *
 *************************************************************************/

public class WordNet {
   
   // for quick search ancestor
   // for bind < (id - which is vertex in graph) with it's (synset and definition) > like this
   // < (36), (AND_circuit AND_gate,a circuit in a computer that fires only when all of its inputs fire) >
   private Map<Integer, String> id2SynsetDefinition;
   
   // for quick search noun in WordNet. Use bag for values, because there can be more then 1 id correspond to 
   // the word
   // Key - noun
   // Value - Bag with sequence of id's where id binded with this key noun
   private Map<String, Bag<Integer>> synset2id;
   
   private SAP sap;
   
   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) {
      id2SynsetDefinition = new HashMap<Integer, String>();
      synset2id = new HashMap<String, Bag<Integer>>();
      createMaps(synsets);
      createSAP(hypernyms);
   }


   
   // the set of nouns (no duplicates), returned as an Iterable
   public Iterable<String> nouns() {
      return synset2id.keySet();
   }
   
   // is the word a WordNet noun?
   public boolean isNoun(String word) {
      return synset2id.containsKey(word);
   }
   
   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB) {
      if (!isNoun(nounA) || !isNoun(nounB)) throw new java.lang.IllegalArgumentException("No such nouns in WordNet!");
      return sap.length(synset2id.get(nounA), synset2id.get(nounB));
   }
   
   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB) {
      if (!isNoun(nounA) || !isNoun(nounB)) throw new java.lang.IllegalArgumentException("No such nouns in WordNet!");
      int ancestorId = sap.ancestor(synset2id.get(nounA), synset2id.get(nounB));
      String valueFields[] = id2SynsetDefinition.get(ancestorId).split(",");
      return valueFields[0];
   }

   private void createMaps(String synsets) {
      In in = new In(synsets);
      while (in.hasNextLine()) {
         String curString = in.readLine();
         String[] fields = curString.split(",");
         for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
         }
         
         int id = Integer.parseInt(fields[0]);
         String synsetDefinition = fields[1] + "," + fields[2];
         id2SynsetDefinition.put(id, synsetDefinition);
         
         String synonyms[] = fields[1].split(" ");
         for (int i = 0; i < synonyms.length; i++) {
            synonyms[i] = synonyms[i].trim();
            Bag<Integer> bag = synset2id.get(synonyms[i]);
            if (bag == null) {
               Bag<Integer> newBag = new Bag<Integer>();
               newBag.add(id);
               synset2id.put(synonyms[i], newBag);
            }
            else {
               bag.add(id);
            }
         }
      }
   }
   
   private void createSAP(String hypernyms) {
      In in = new In(hypernyms);
      Digraph diG = new Digraph(id2SynsetDefinition.size());
      while (in.hasNextLine()) {
         String curString = in.readLine();
         String[] fields = curString.split(",");
         for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
         }
         for (int i = 1; i < fields.length; i++) {
            diG.addEdge(Integer.parseInt(fields[0]), Integer.parseInt(fields[i]));
         }
      }
      
      if(!isRootedDAG(diG)) {
         throw new java.lang.IllegalArgumentException("Not rooted DAG!");
      }
      
      sap = new SAP(diG);
   }



   private boolean isRootedDAG(Digraph diG) {
      // check if there is no Cycle 
      DirectedCycle diCycle = new DirectedCycle(diG);
      if (diCycle.hasCycle()) {
         return false;
      }
      
      // check if there is one root
      // root - vertex with no outgoing edges
      int roots = 0;
      for (int vertex = 0; vertex < diG.V(); vertex++) {
         if (!diG.adj(vertex).iterator().hasNext() ) roots++;
      }
      if (roots != 1) return false;
      return true;
   }

   
   // for unit testing of this class
   public static void main(String[] args) {
      WordNet wn = new WordNet(args[0], args[1]);
      for (String s : wn.nouns()) {
         StdOut.println(s);
      }
      while (!StdIn.isEmpty()) {
         String nounA = StdIn.readLine();
         String nounB = StdIn.readLine();
         int distance   = wn.distance(nounA, nounB);
         String ancestor = wn.sap(nounA, nounB);
         StdOut.println("length = " + distance);
         StdOut.println("ancestor = " + ancestor);
      }
   }

}
