
/*************************************************************************
 *  Third week coursera course Java Algorithms part 1
 *  @Date 31.10.13
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 *  			   http://coursera.cs.princeton.edu/algs4/checklists/collinear.html
 *  			   http://algs4.cs.princeton.edu/windows/
 *  Compilation:   javac-algs4 Brute.java
 *  Execution:     java-algs4 Brute <name of txt file with points, e.g. input400.txt> O(N) = N^4
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  
 * Description: The BruteForce algorithm to find collinear line segments of 4 points 
 *
 *************************************************************************/
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Brute {
	
	public static void main(String[] args) throws IOException {
		// read in the input
        String filename = args[0];
        In in = new In(filename);
        ArrayList<Point> points = readPointsAndSort(in);
        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        // go 4 inner loops and watch slopes to i point
        for (int i = 0; i < points.size(); i++) {
			for (int j = i + 1; j < points.size(); j++) {
				for (int k = j + 1; k < points.size(); k++) {
					for (int l = k + 1; l < points.size(); l++) {
						//watch slopes to i point
						double slopeIJ = points.get(i).slopeTo(points.get(j));
						double slopeIK = points.get(i).slopeTo(points.get(k));
						double slopeIL = points.get(i).slopeTo(points.get(l));
						// if they all are equal then it is line segment
						if ((slopeIJ == slopeIK) && (slopeIJ == slopeIL)) {
							System.out.println(points.get(i) + " " + "->" + " " +
											points.get(j) + " " + "->" + " " +
											points.get(k) + " " + "->" + " " +
											points.get(l));
							// draw this line segment
							points.get(i).drawTo(points.get(l));
						}
					}
				}
			}
			points.get(i).draw(); // draw all points
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
