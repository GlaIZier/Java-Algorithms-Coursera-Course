


/*************************************************************************
 *  Third week coursera course Java Algorithms part 1
 *  @Date 31.10.13
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 *  			   http://coursera.cs.princeton.edu/algs4/checklists/collinear.html
 *  			   http://algs4.cs.princeton.edu/windows/
 *  Compilation:   javac-algs4 Fast.java
 *  Execution:     java-algs4 Fast <name of txt file with points, e.g. input400.txt> O(N) = N^2 * logN
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  
 * Description: Fast algorithm for searching collinear line segments of 4 and more points
 * 				It uses sorting algorithm to search for line segments.
 * 				We sort all points using SLOPE_ORDER in Point class, 
 * 			       so all points with equal slope are together.   
 *
 *************************************************************************/
import java.io.IOException;
import java.util.*;

public class Fast {
	
	private static final int NUMBERS_IN_SEQUENCE = 4;
	
	private static Set<String> usedSegments = new HashSet<String>(); // to define which segments we have printed already
	
	public static void main(String[] args) throws IOException {
		// read in the input
        String filename = args[0];
        In in = new In(filename);
        ArrayList<Point> points = readPointsAndSort(in);
        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        // for each i point in points collection
        for (int i = 0; i < points.size(); i++) {
			// sort collection relative to i point's slope
        	ArrayList<Point> auxPoints = new ArrayList<Point>(points); // copy to aux array
        	Collections.sort(auxPoints, auxPoints.get(i).SLOPE_ORDER);
        	
        	// search for points with equal slope to i
        	List<Point> listToPrint = new ArrayList<Point>();
        	listToPrint.add(points.get(i)); // add first point
        	boolean needToAddLastSeqEl = false; 
        	for (int j = 1; j < auxPoints.size(); j++) {
				// if these near points have equal slopes
        		if ((points.get(i).slopeTo(auxPoints.get(j - 1))) == points.get(i).slopeTo(auxPoints.get(j))) {
					listToPrint.add(auxPoints.get(j - 1));
					needToAddLastSeqEl = true;
				}
        		else if (needToAddLastSeqEl) { // add last element in equal slope sequance
        			listToPrint.add(auxPoints.get(j - 1));
        			if (listToPrint.size() >= NUMBERS_IN_SEQUENCE) {
        				printResult(listToPrint);
        				// clear listToPrint
        				listToPrint = new ArrayList<Point>();
        	        	listToPrint.add(points.get(i)); // add first point
        			}
        			needToAddLastSeqEl = false;
        		}
			}
        	if ((points.get(i).slopeTo(auxPoints.get(auxPoints.size() - 2))) == // prevlast element
        		(points.get(i).slopeTo(auxPoints.get(auxPoints.size() - 1)))) { // if last element in sequence
        			listToPrint.add(auxPoints.get(auxPoints.size() - 1));
        	}
        	if (listToPrint.size() >= NUMBERS_IN_SEQUENCE) {
				printResult(listToPrint);
				// "free" listToPrint
				listToPrint = null;
			}
        	
        	points.get(i).draw(); // draw i point
		}
	}
	
	private static void printResult(List<Point> listToPrint) {
		boolean toPrint = true;
		for (int i = 1; i < listToPrint.size(); i++) {
			String segment = listToPrint.get(i - 1) + " -> " + listToPrint.get(i);
			if (!usedSegments.contains(segment)) {
				usedSegments.add(segment);
			}
			else {
				toPrint = false;
			}
		}
		if (toPrint) {
			for (int i = 0; i < listToPrint.size() - 1; i++) {
				System.out.print(listToPrint.get(i) + " -> ");
			}
			System.out.println(listToPrint.get(listToPrint.size() - 1));
			listToPrint.get(0).drawTo(listToPrint.get(listToPrint.size() - 1)); // draw a line
		}
		
	}
	
	private static ArrayList<Point> readPointsAndSort(In in) throws IOException {
		ArrayList<Point> points = new ArrayList<Point>();
        int pointsNumber = in.readInt();
        for (int i = 0; i < pointsNumber; i++) {
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);
            points.add(p);
        }
        // sort to guarantee that line segments will out only at once
        Collections.sort(points); // it uses compareTo() in Point class. First is left bottom point 
        return points;
	}	
}
