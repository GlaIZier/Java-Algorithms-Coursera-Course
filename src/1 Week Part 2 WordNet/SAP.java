
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
 *  Compilation:   javac-algs4 SAP.java with uncommented main method
 *  Execution:     java-algs4 SAP <name of Princeton formatted digraph file>. 
 *                     Next input <pair of integers to search ancestor between> (e.g  1 0)
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  Search for shortest ancestral path algorithm
 *  
 *  TODO!!! Can be improved. Watch 2 first points in checklist 'Optimal improvements'. 
 *  (Can cache creating of BFS and can search in two DiGraphs(v and w) simultaneously  
iously 
 *  
 *
 *************************************************************************/

public class SAP {
   
   private Digraph diG;
   
   // remember last connection length and ancestor.
   private int lastV;
   
   private int lastW;
   
   private Iterable<Integer> lastVI;
   
   private Iterable<Integer> lastWI;
   
   private int lastLength;
   
   private int lastAncestor;
	
   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G) {
      this.diG = G;
      this.lastV = -1;
      this.lastW = -1;
      this.lastVI = null;
      this.lastWI = null;
      this.lastAncestor = -1;
      this.lastLength = -1;
   }
   
   

   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w) {
      checkInput(v);
      checkInput(w);
      
      // if we calculated it last time
      if ( (lastV == v && lastW == w) || (lastV == w && lastW == v) ) {
         return lastLength;
      }
      
      bfs(v, w);
      
      return lastLength;
   }

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   public int ancestor(int v, int w) {
      checkInput(v);
      checkInput(w);
      
      // if we calculated it last time
      if ( (lastV == v && lastW == w) || (lastV == w && lastW == v) ) {
         return lastAncestor;
      }
      
      bfs(v, w);
      
      return lastAncestor;
   }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
       checkInput(v);
       checkInput(w);
       
       // if we calculated it last time
       if (lastVI != null && lastWI != null) {
          if ( (lastVI.equals(v) && lastWI.equals(w)) || (lastVI.equals(w) && lastWI.equals(v)) ) {
             return lastLength;
          }
       }
       
       bfs(v, w);
       
       return lastLength;
    }

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	 public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
	    checkInput(v);
       checkInput(w);
       
       // if we calculated it last time
       if (lastVI != null && lastWI != null) {
          if ( (lastVI.equals(v) && lastWI.equals(w)) || (lastVI.equals(w) && lastWI.equals(v)) ) {
             return lastAncestor;
          }
       }
       
       bfs(v, w);
       
       return lastAncestor;
	 }
   

   private void checkInput(int vertex) {
      if (vertex < 0 || vertex > diG.V() - 1) throw new java.lang.IndexOutOfBoundsException();      
   }
   
   private void checkInput(Iterable<Integer> vertex) {
      for (Integer v : vertex) {
         if (v < 0 || v > diG.V() - 1) throw new java.lang.IndexOutOfBoundsException();   
      }    
   }
   
   private void cache(int v, int w, int length, int ancestor) {
      lastV = v;
      lastW = w;
      lastVI = null;
      lastWI = null;
      lastLength = length;
      lastAncestor = ancestor;  
   }
   
   private void cache(Iterable<Integer> v, Iterable<Integer> w, int length, int ancestor) {
      lastV = -1;
      lastW = -1;
      lastVI = v;
      lastWI = w;
      lastLength = length;
      lastAncestor = ancestor;  
   }
   
   private void bfs(int v, int w) {
      BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(diG, v);
      BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(diG, w);
      int curLength = Integer.MAX_VALUE;
      int curAncestor = -1;
      // cycle for all verticies
      for (int vertex = 0; vertex < diG.V(); vertex++) {
         if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
            int lengthToVertex = bfsV.distTo(vertex) + bfsW.distTo(vertex);
            if (lengthToVertex < curLength) {
               curLength = lengthToVertex;
               curAncestor = vertex;
            }
         }
      }
      if (curLength == Integer.MAX_VALUE) curLength = -1;
      
      cache(v, w, curLength, curAncestor);  
   }


   private void bfs(Iterable<Integer> v, Iterable<Integer> w) {
      BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(diG, v);
      BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(diG, w);
      int curLength = Integer.MAX_VALUE;
      int curAncestor = -1;
      // cycle for all verticies
      for (int vertex = 0; vertex < diG.V(); vertex++) {
         if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
            int lengthToVertex = bfsV.distTo(vertex) + bfsW.distTo(vertex);
            if (lengthToVertex < curLength) {
               curLength = lengthToVertex;
               curAncestor = vertex;
            }
         }
      }
      if (curLength == Integer.MAX_VALUE) curLength = -1;
      
      cache(v, w, curLength, curAncestor);  
   }
   
	// for unit testing of this class (such as the one below)
	 public static void main(String[] args) {
	    In in = new In(args[0]);
       Digraph G = new Digraph(in);
       SAP sap = new SAP(G);
       while (!StdIn.isEmpty()) {
          int v = StdIn.readInt();
          int w = StdIn.readInt();
          int length   = sap.length(v, w);
          int ancestor = sap.ancestor(v, w);
          StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
       }
	 }

}
