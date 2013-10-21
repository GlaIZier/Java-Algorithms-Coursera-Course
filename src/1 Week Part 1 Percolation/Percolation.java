/*
 *  First week coursera course Java Algorithms part 1
 *  @Date 18.10.13
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *  			   http://coursera.cs.princeton.edu/algs4/checklists/percolation.html
 *  			   http://algs4.cs.princeton.edu/windows/
 *  Compilation:   javac-algs4 Percolation.java with uncommented main
 *  Execution:     java-algs4 Percolation
 *  Execution only with installed algs4.jar and stdlib.jar
 */

public class Percolation
{
	 private static final int[][] SHIFT_TO_CONNECTED_SITES = {
		 {0, -1},
		 {-1, 0},
		 {0, 1},
		 {1, 0}
	 };
	
	 private static final int BOTTOM_AND_TOP_INDEX = 2;
	 
	 private WeightedQuickUnionUF weightedQuickUnionUF = null; 
	   
	 private int gridSize = 0;
	 
	 private int topIndex = 0;
	 
	 private int bottomIndex = 0;
	 
	 private boolean[][] isSiteOpen;
	 
	// create N-by-N grid, with all sites blocked
	 public Percolation(int N) { 
		 if (N <= 0) {
			 throw new java.lang.IllegalArgumentException("Size of the grid must be greater than 0");
		 }
	     gridSize = N;
	     int numberOfSites = (gridSize * gridSize) + BOTTOM_AND_TOP_INDEX;
	     weightedQuickUnionUF = new WeightedQuickUnionUF(numberOfSites);
	     topIndex = gridSize * gridSize;
	     bottomIndex = gridSize * gridSize + 1;
	     isSiteOpen = new boolean[gridSize][gridSize];
	     for (int i = 0; i < gridSize; i++) {
	    	 for (int j = 0; j < gridSize; j++) {
	    		 isSiteOpen[i][j] = false;
	    	 }
	     }
	     // connect virtual bottom and top site to last and first row
	     for (int i = 0; i < gridSize; i++) {
	    	 int bottomRow1DIndex = twoDToOneD(correctCoordToZeroArrayBeginning(gridSize), i);
	    	 weightedQuickUnionUF.union(bottomIndex, bottomRow1DIndex);
	    	 int topRow1DIndex = twoDToOneD(correctCoordToZeroArrayBeginning(1), i);
	    	 weightedQuickUnionUF.union(topIndex, topRow1DIndex);
	     }
	   }  
	 
	// open site (row i, column j) if it is not already  
	 public void open(int i, int j) {
		 checkCoordsOutOfBorders(i, j);
		 int row = correctCoordToZeroArrayBeginning(i);
		 int col = correctCoordToZeroArrayBeginning(j);
		 if (!isSiteOpen[row][col]) {
			 isSiteOpen[row][col] = true;
			 connectToOpenSites(row, col);
		 }
	 }
	 
	 private int correctCoordToZeroArrayBeginning(int beforeCorrection) {
		 return beforeCorrection - 1;
	 }
	 
	 // if there are open sites connected to opening cell, then we should unite them in weightedQuickUnionUF
	 private void connectToOpenSites(int row, int col) {
		 for (int i = 0; i < SHIFT_TO_CONNECTED_SITES.length; i++) {
			int connectedSiteRow = row + SHIFT_TO_CONNECTED_SITES[i][0];
			int connectedSiteCol = col + SHIFT_TO_CONNECTED_SITES[i][1];
			boolean isSiteConnectsToOpenSite = (!isIndexOutOfBorders(connectedSiteRow)) 
												&& (!isIndexOutOfBorders(connectedSiteCol))
												&& (isSiteOpen[connectedSiteRow][connectedSiteCol]);
			if (isSiteConnectsToOpenSite) {
					int currentSite1DIndex = twoDToOneD(row, col);
					int connectedSite1DIndex = twoDToOneD(connectedSiteRow, connectedSiteCol);
					weightedQuickUnionUF.union(currentSite1DIndex, connectedSite1DIndex);
			}
		 }
	 }
	 
	 private boolean isIndexOutOfBorders(int index) {
		return (index < 0) || (index > (gridSize - 1)); 
	 }
	 
	 // returns index of id in weightedQuickUnionUF 
	 private int twoDToOneD(int row, int col) {
		 int index = row * gridSize + col;
		 return index;
	 }
	 
	 // throws exception if coordinates not in borders
	 private void checkCoordsOutOfBorders(int i, int j) {
		 boolean isICoordsOutOfBorders = ((i < 1) || (i > gridSize) || (j < 1) || (j > gridSize));
		 if (isICoordsOutOfBorders) {
			 throw new IndexOutOfBoundsException("You have inputted wrong coordinates");
		 }
	 }
	 
	 // is site (row i, column j) open?
	 public boolean isOpen(int i, int j) {
		 checkCoordsOutOfBorders(i, j);
		 return (isSiteOpen[correctCoordToZeroArrayBeginning(i)][correctCoordToZeroArrayBeginning(j)]);
	 }
	 
	 // is site (row i, column j) full?
	 public boolean isFull(int i, int j) { 
		 checkCoordsOutOfBorders(i, j);
		 // first row is always connected to virtual top site because of the constructor.
		 // last row may be full even if it is not opened because of the constuctor.
		 // we should look if it was opened or not.
		 if (((i == 1) || (i == gridSize)) && (!isOpen(i, j))) {
			 return false;
		 }
		 return  weightedQuickUnionUF.connected(topIndex, 
				 twoDToOneD(correctCoordToZeroArrayBeginning(i), correctCoordToZeroArrayBeginning(j)));
	 }
	 
	 // does the system percolate?
	 public boolean percolates() {
		 return weightedQuickUnionUF.connected(topIndex, bottomIndex);
	 }  
	 
	 // @Test ******************************************************
	 /*
	  * 
	  * 
	  * 
	  * 
	 public static void main(String[] args) {
	        Percolation p = new Percolation(4);
	        p.open(1, 1);
	        isPercolateMsgOut(p);
	        p.open(2, 1);
	        isPercolateMsgOut(p);
	        p.open(2, 2);
	        isPercolateMsgOut(p);
	        p.open(3, 2);
	        isPercolateMsgOut(p);
	        p.open(3, 3);
	        isPercolateMsgOut(p);
	        p.open(4, 3);
	        isPercolateMsgOut(p);
	 }
	 
	 private static void isPercolateMsgOut(Percolation p) {
		 if (p.percolates()) {
	       	System.out.println("Percolated!");
	     }
         else {
        	System.out.println("Not percolated.");	
         } 
	 }
	 *
	 *
	 *
	 *
	 *
	 */
}
