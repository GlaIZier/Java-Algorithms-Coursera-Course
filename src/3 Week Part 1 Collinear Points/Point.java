/*************************************************************************
 *  Third week coursera course Java Algorithms part 1
 *  @Date 31.10.13
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 *  			   http://coursera.cs.princeton.edu/algs4/checklists/collinear.html
 *  			   http://algs4.cs.princeton.edu/windows/
 *  Compilation:   javac-algs4 Point.java
 *  Execution:     java-algs4 Point
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();   
    
    private class SlopeOrder implements Comparator<Point> {
    	
    	// this method for sorting points relative to the slope to the invoked point so that:
    	// all points with slope equal to the invoked point are together  
    	public int compare(Point first, Point second) {
    		double slopeA = slopeTo(first);
    		double slopeB = slopeTo(second);
    		if (slopeA < slopeB) {
    			return -1;
    		}
    		else if (slopeA == slopeB) {
    			return 0;
    		}
    		else {
    			return 1;
    		}
    	}
    }
    
    private final int x;                              // x coordinate
    
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
    	if ((that.x == this.x) && (that.y == this.y)) { // point 
    		return Double.NEGATIVE_INFINITY;
    	}
    	else if (that.x == this.x) { // vertical line segment
    		return Double.POSITIVE_INFINITY;
    	}
    	else if (that.y == this.y) { // horizontal line segment
        	return +0.0;
        }
    	else {
    		return ((that.y - this.y) / ((double) (that.x - this.x))); // we should cast 1 of the arguments 
    																 //	to double 
    	}
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
    	if (this.y == that.y) {
    		if (this.x > that.x) {
    			return 1;
    		}
    		else if (this.x < that.x) {
    			return -1;
    		}
    		else {
    			return 0;
    		}
    	}
    	else if (this.y > that.y) {
    		return 1;
    	}
    	else {
    		return -1;
    	}
    }

    // return string representation of this point
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
    // unit test
    public static void main(String[] args) {
    	final int NUM = 5;
    	Point[] a = new Point[NUM];
    	int i = 0;
    	a[i++] = new Point(10, 0); // will sort relative to this point slopes
        while (i < NUM) {
            Integer x = StdIn.readInt();
            if (x.equals(100)) {
         	   break;
            }
            Integer y = StdIn.readInt();
            if (y.equals(100)) {
          	   break;
            }
            Point point1 = new Point(x, y);
            Point point2 = new Point(1, 1);
            System.out.println("Slope point2 to point1 = " + point2.slopeTo(point1));
            System.out.println("Point2 > Point1 : " + point2.compareTo(point1));
            a[i++] = point1;
        }
        System.out.println("Array of sorted points: ");
        Arrays.sort(a, a[0].SLOPE_ORDER);
        for (i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}	 	
    }
}
