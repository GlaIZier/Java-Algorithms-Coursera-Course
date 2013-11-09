
/*************************************************************************
 *  Forth week coursera course Java Algorithms part 1
 *  8 Puzzle
 *  @Date 04.10.13
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
 *  			   http://coursera.cs.princeton.edu/algs4/checklists/8puzzle.html
 *  			   http://algs4.cs.princeton.edu/windows/
 *  Compilation:   javac-algs4 Board.java with uncommented main method
 *  Execution:     java-algs4 Board
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  
 *
 *************************************************************************/

import java.util.Random;

public class Board {
	
	private int dim;
	
	private int[][] blocks;
	
	// construct a board from an N-by-N array of blocks
	// (where blocks[i][j] = block in row i, column j)
	public Board(int[][] blocks) {
		this.dim = blocks.length;
		this.blocks = copyBlocks(blocks);
	}
	
	private int[][] copyBlocks(int[][] board) {
		int[][] copyBlocks = new int[dim][dim];
		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim; col++) {
				copyBlocks[row][col] = board[row][col];
			}
		}
		return copyBlocks;
	}
	
	// board dimension N
	public int dimension() {
		return dim;
	}
	
	// number of blocks out of place
	public int hamming() throws IndexOutOfBoundsException {
		int hamming = 0;
		// count num of blocks in wrong position
		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim; col++) {
				if ( (blocks[row][col] != (row * dim + indxTo1ArBegin(col) ) ) &&
					(blocks[row][col] != 0) ) {
					hamming++;
				}
			}
		}
		return hamming;
	}
	
	//correct array index to first index = 1 and back
	private int indxTo1ArBegin(int index) {
		return (index + 1);
	}
	
	private int indxTo0ArBegin(int index) {
		return (index - 1);
	}
	
	// sum of Manhattan distances between blocks and goal
	// if block not in the goal position, we add length to the goal position to manhattan
	public int manhattan() throws IndexOutOfBoundsException {
		int manhattan = 0;
		// for each block in blocks, watch value.
		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim; col++) {
				int value = blocks[row][col];
				boolean wrongPos = ( (value != (row * dim + indxTo1ArBegin(col) ) )  &&
					(value != 0) );
				// if it's wrong then detect goal position
				if (wrongPos) {
					int goalRow = value / dim;
					int goalCol =  indxTo0ArBegin(value - (goalRow * dim) );
					boolean lastColBlock = (goalCol == -1);
					// correct to case, e.g. 6 = (2; -1) not (1; 2)
					if (lastColBlock) {
						goalRow--;
						goalCol = dim - 1;
					}
					// and count steps to it
					manhattan += Math.abs(goalRow - row);
					manhattan += Math.abs(goalCol - col);
				}
			}
		}
		return manhattan;
	}
	
	// is this board the goal board?
	public boolean isGoal() {
		return (hamming() == 0);
	}
	
	// a board obtained by exchanging two adjacent blocks in the same row
	public Board twin() {
		int[][] twinBlocks = copyBlocks(blocks);
		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim - 1; col++) {
				if ( (twinBlocks[row][col] != 0) && (twinBlocks[row][col + 1] != 0) ) {
					//exchange adjacent blocks
					int swap = twinBlocks[row][col];
					twinBlocks[row][col] =  twinBlocks[row][col + 1];
					twinBlocks[row][col + 1] = swap;
					return new Board(twinBlocks);
				}
			}
		}
		// return null if something wrong
		return null; 
	}
	
	// does this board equal y?
	public boolean equals(Object y) {
		if (this == y) {
			return true;
		}
		if (y == null) {
			return false;
		}
		if (this.getClass() != y.getClass()) {
			return false;
		}
		Board that = (Board) y;
		if (this.dim != that.dim) {
			return false;
		}
		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim; col++) {
				if (this.blocks[row][col] != that.blocks[row][col]) {
					return false;
				}
			}
		}
		return true;
	}
	
	// all neighboring boards
	public Iterable<Board> neighbors() {
		Iterable<Board> neighbors = null;
		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim; col++) {
				if (blocks[row][col] == 0) {
					 neighbors = getNeighborsQueue(row, col);
				}
			}
		}
		return neighbors;
	}
	
	private	Iterable<Board> getNeighborsQueue(int emptyBlockRow, int emptyBlockCol) {
		Queue<Board> neighborsQueue = new Queue<Board>();
		final int[][] SHIFTS_TO_NEIGHBORS = {
				{0, -1},
				{-1, 0},
				{0, 1},
				{1, 0}
		};
		
		for (int shiftIndex = 0; shiftIndex < SHIFTS_TO_NEIGHBORS.length; shiftIndex++) {
			int row = emptyBlockRow + SHIFTS_TO_NEIGHBORS[shiftIndex][0];
			int col = emptyBlockCol + SHIFTS_TO_NEIGHBORS[shiftIndex][1];
			boolean isNeighborBlockInDim = (row >= 0) && (row < dim) && (col >= 0) && (col < dim);
			if (isNeighborBlockInDim) {
				int[][] neighborBlocks = copyBlocks(blocks);
				// change values between 2 blocks: empty and neighbor
				neighborBlocks[row][col] = blocks[emptyBlockRow][emptyBlockCol];
				neighborBlocks[emptyBlockRow][emptyBlockCol] = blocks[row][col];
				// add new neighbor board
				neighborsQueue.enqueue(new Board(neighborBlocks));
			}
		}
		return neighborsQueue;
	}
	
	// string representation of the board (in the output format specified below)
	public String toString() {
		StringBuilder sb = new StringBuilder(); // mutable String
	    sb.append(dim + "\n"); // concatenation method for StringBuilder. sb = "" + dim + "\n" = 3\n 
	    for (int row = 0; row < dim; row++) {
	        for (int col = 0; col < dim; col++) {
	            sb.append(String.format("%2d ", blocks[row][col]));
	        }	
	        sb.append("\n");
		}
	    return sb.toString();
	}
	
	
	// test client for class
	public static void main(String[] args) {
		// create a random Board
		Board board = createRandomBoard(2);
		// out this board
		System.out.println(board.toString());
		// check method dimension
		System.out.println("Dimension of this board is " + board.dimension());
		// estimate hamming & manhattan evaluations
		System.out.println("Hamming = " + board.hamming());
		System.out.println("Manhattan = " + board.manhattan());
		// check method isGoal()
		System.out.println("Is it goal board? " + board.isGoal());
		// check method twin
		System.out.println("Twin is " + board.twin().toString());
		// check equals method
		Board boardForEqual = null;
		System.out.println("Is it equal? " + board.equals(boardForEqual));
		// iterate thorough neighbors
		System.out.println("Neighbors: ");
		for(Board iter: board.neighbors()) {
			System.out.println(iter.toString());
		}
	}
	
	private static Board createRandomBoard(int dim) {
		int[][] randomBlocks = new int[dim][dim];
		// for all possible numbers in blocks
		// choose random block, 
		// and if it is empty, place number in this block
		for (int number = (dim * dim - 1); number > 0; number--) {
			int row = 0;
			int col = 0;
			do {
				Random random = new Random();
				row = random.nextInt(dim);
				col = random.nextInt(dim);
				random = null;
			} while (randomBlocks[row][col] != 0);
			randomBlocks[row][col] = number;
		}
		return new Board(randomBlocks);
	}
}
