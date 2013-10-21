/*
 *  First week coursera course Java Algorithms part 1
 *  @Date 18.10.13
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *  			   http://coursera.cs.princeton.edu/algs4/checklists/percolation.html
 *  			   http://algs4.cs.princeton.edu/windows/
 *  
 *  Compilation:   javac-algs4 PercolationStats.java with uncommented main
 *  Execution:     java-algs4 PercolationStats <gridSize> <experimentsNum>
 *  Execution only with installed algs4.jar and stdlib.jar
 */


public class PercolationStats
{
	private final int gridSize;
	
	private final int experimentsNum;
	
	private double[] fractionOpenedSites;
	
	private double fractionOpenedSitesSum;
	
	private double mean;
	
	private double deviation;
	
    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
    	if ((!isNumPositive(N)) || ((!isNumPositive(T)))) {
    		throw new java.lang.IllegalArgumentException("N and T must be greater than 0!");
    	}
    	gridSize = N;
    	experimentsNum = T;
    	fractionOpenedSites = new double[experimentsNum];
    	fractionOpenedSitesSum = 0.0;
    	mean = 0.0;
    	deviation = 0.0;
    	runMonteCarloSimulation();
    }
    
    private boolean isNumPositive(int num) {
		return (num > 0); 
	 }
    
    // main method for simulation experimentsNum of experiments
    private void runMonteCarloSimulation() {
    	for (int experimentIter = 0; experimentIter < experimentsNum; experimentIter++) {
			int curBorderSite = oneExperiment();
			fractionOpenedSites[experimentIter] = ((double) curBorderSite / (gridSize * gridSize));
			fractionOpenedSitesSum += fractionOpenedSites[experimentIter];
		}
    }
    
    // search last site to open for percolation in grid
    private int oneExperiment() {
    	Percolation percolation = new Percolation(gridSize);
		int siteNum = 0;
		while ((!percolation.percolates()) && (siteNum <= (gridSize * gridSize))) {
			int row = 1 + (int) (Math.random() * gridSize);
			int col = 1 + (int) (Math.random() * gridSize);
			if (percolation.isOpen(row, col)) {
				continue;
			}
			else {
				percolation.open(row, col);
				siteNum++;
			}
		}
		return siteNum;
    }
    
    // sample mean of percolation threshold
    public double mean() {
    	mean = (double) (fractionOpenedSitesSum / experimentsNum);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
    	if (experimentsNum == 1) {
    		return Double.NaN;
    	}
    	deviation = StdStats.stddev(fractionOpenedSites);
        return deviation;
    }

    // returns lower bound of the 95% confidence interval
    public double confidenceLo() {
        return (mean - ((1.96 * deviation) / (Math.sqrt((double) experimentsNum))));
    }

    // returns upper bound of the 95% confidence interval
    public double confidenceHi() {
        return (mean + ((1.96 * deviation) / (Math.sqrt((double) experimentsNum))));
    }
    
    // @Test
    // method for testing class
    public static void main(String[] args) {
    	// @Test from keyboard during execution int N = StdIn.readInt()
    	// @Test from keyboard during execution  int T = StdIn.readInt()
        int N = Integer.parseInt(args[0]); 
        int T = Integer.parseInt(args[1]); 
        PercolationStats percolationStats = new PercolationStats(N, T);
        StdOut.println("Mean                    = " + percolationStats.mean());
        StdOut.println("Stddev                  = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = " + percolationStats.confidenceLo() +", "+ percolationStats.confidenceHi());
    }

}
