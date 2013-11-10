
/*************************************************************************
 *  Forth week coursera course Java Algorithms part 1
 *  8 Puzzle
 *  @Date 09.11.13
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
 *  			   http://coursera.cs.princeton.edu/algs4/checklists/8puzzle.html
 *  			   http://algs4.cs.princeton.edu/windows/
 *  Compilation:   javac-algs4 Solver.java with uncommented main method
 *  Execution:     java-algs4 Solver <name_of_the_file_with_puzzle>
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  
 *
 *************************************************************************/

public class Solver {
	
	private class SearchNode implements Comparable<SearchNode> {
		
		private int priority;
		
		private int moves;
		
		private int manhattan;
		
		private Board board;
		
		private SearchNode prevSearchNode;
		
		public SearchNode(Board board, SearchNode prevSearchNode) {
			this.board = board;
			this.prevSearchNode = prevSearchNode;
			this.manhattan = board.manhattan();
			if (this.prevSearchNode == null) {
				moves = 0;
			}
			else {
				moves = prevSearchNode.moves + 1;
			}
			this.priority = this.manhattan + this.moves;
			// property of A* algorithm
			assert ( (prevSearchNode == null) || (this.priority >= prevSearchNode.priority) ); 
		}
		
		public int compareTo(SearchNode that) {
			if (this.priority > that.priority) {
				return 1;
			}
			else if (this.priority == that.priority) {
				return 0;
			}
			else {
				return -1;
			}
		}
	}

	private SearchNode solutionNode;
	
	private SearchNode solutionTwinNode;
	
	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
		// if it is goal then it is result
		// else 
		// 		create 2 MinPQ: for the normal solution
		//		and for checking if it has no solution (by adding a twin of board to that MinPQ)
		//		add first board and twin to PQs
		//      while won't find a solution
		//			dequeue node with min priority from 2 PQs
		//          if it's not solution and not solution in twin PQ
		// 				add all neighbors of normal board and twin to PQs except 
		// 			    boards equals to previousSearchNode board
		//			else 
		//				return result = lastSearchNode or null respectively
		if (initial.isGoal()) {
			solutionNode = new SearchNode(initial, null);
		}
		else {
			MinPQ<SearchNode> normalPQ = new MinPQ<SearchNode>();
			MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();
			normalPQ.insert(new SearchNode(initial, null));
			twinPQ.insert(new SearchNode(initial.twin(), null));
			while (true) {
				// work with twin board case
				solutionTwinNode = makeStep(twinPQ);
				if (solutionTwinNode.board.isGoal()) {
					clearMemory(solutionNode);
					clearMemory(solutionTwinNode);
					// if there is solution in twin than there is no solution in initial board
					solutionNode = null;
					return;
				}
				// work with normal board
				solutionNode = makeStep(normalPQ);
				if (solutionNode.board.isGoal()) {
					clearMemory(solutionTwinNode);
					return;
				}	
			}
		}
		
 	}
	
	private SearchNode makeStep(MinPQ<SearchNode> minPQ) {
		SearchNode currentBestNode = minPQ.delMin();
		addNeighbors(currentBestNode, minPQ);
		return currentBestNode;
	}
	
	private void clearMemory(SearchNode first) {
		if (first == null) {
			return;
		}
		SearchNode current = first.prevSearchNode;
		while (current != null) {
			first = null;
			first = current;
			current = current.prevSearchNode;
		}
		first = null;
	}
	
	private void addNeighbors(SearchNode searchNode, MinPQ<SearchNode> minPQ) {
		for (Board neighbor: searchNode.board.neighbors()) {
			if ( (searchNode.prevSearchNode == null) || (!neighbor.equals(searchNode.prevSearchNode.board) ) ) {
				minPQ.insert(new SearchNode(neighbor, searchNode));
			}	
		}
	}
	
	// is the initial board solvable?
	public boolean isSolvable() {
		if (solutionNode == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
    // min number of moves to solve initial board; -1 if no solution 
	public int moves() {
		if (isSolvable()) {
			return solutionNode.moves;
		}
		else {
			return -1;
		}
	}
	
	// sequence of boards in a shortest solution; null if no solution
	public Iterable<Board> solution() {
		if (!isSolvable()) {
			return null;
		}
		Stack<Board> stack = new Stack<Board>();
		SearchNode current = solutionNode;
		while (current != null) {
			stack.push(current.board);
			current = current.prevSearchNode;
		}
		return stack;
	}
	
	// solve a slider puzzle (given below)
	public static void main(String[] args) {
		// create initial board from file
	    In in = new In(args[0]);
	    int N = in.readInt();
	    int[][] blocks = new int[N][N];
	    for (int i = 0; i < N; i++) {
	        for (int j = 0; j < N; j++) {
	            blocks[i][j] = in.readInt();
	        }
	    }
		// @Test 
		// unsolvable
//		int[][] blocks = {
//				{8, 6, 7},
//				{2, 5, 4},
//				{1, 3, 0}
//		};
		// @Test
		// 11 steps	
//		int[][] blocks = {
//				{1, 0, 2},
//				{7, 5, 4},
//				{8, 6, 3}
//		};
	    
	    Board initial = new Board(blocks);
	    // solve the puzzle
	    Solver solver = new Solver(initial);

	    // print solution to standard output
	    if (!solver.isSolvable())
	        StdOut.println("No solution possible");
	    else {
	        StdOut.println("Minimum number of moves = " + solver.moves());
	        for (Board board : solver.solution())
	            StdOut.println(board);
	    }
	}
       
}
