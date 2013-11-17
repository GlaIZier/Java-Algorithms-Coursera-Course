
/*************************************************************************
 *  Forth week coursera course Java Algorithms part 1
 *  kdTree
 *  @Date 17.11.13
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html
 *  			   http://coursera.cs.princeton.edu/algs4/checklists/kdtree.html
 *  			   http://algs4.cs.princeton.edu/windows/
 *  Compilation:   javac-algs4 PointSET.java with uncommented main method
 *  Execution:     java-algs4 PointSET
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  Brute force algorithm to resolve nearest point and range problems
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {
   
   private Set<Point2D> pointSet;
   
   // construct an empty set of points
   public PointSET() {
      // choose TreeSet (which is RB tree) to achieve log(N) time
      pointSet = new TreeSet<Point2D>(); 
   }
   
   // is the set empty?
   public boolean isEmpty() {
      return (pointSet.size() == 0);
   }
   
   // number of points in the set
   public int size() {
      return pointSet.size();
   }
   
   // add the point p to the set (if it is not already in the set)
   public void insert(Point2D p) {
      pointSet.add(p);
   }
   
   // does the set contain the point p?
   public boolean contains(Point2D p) {
      return (pointSet.contains(p));
   }
   
   // draw all of the points to standard draw 
   public void draw() {
      Iterator<Point2D> iterator = pointSet.iterator();
      while (iterator.hasNext()) {
         iterator.next().draw();
      }
   }
   
   // all points in the set that are inside the rectangle
   public Iterable<Point2D> range(RectHV rect) {
      ResizingArrayQueue<Point2D> rAQ = new ResizingArrayQueue<Point2D>();
      for (Point2D point: pointSet) {
         if (rect.contains(point)) {
            rAQ.enqueue(point);
         }
      }
      return rAQ;
   }
   
   // a nearest neighbor in the set to p; null if set is empty
   public Point2D nearest(Point2D p) {
      if ( (pointSet.isEmpty()) || (p == null) ) {
         return null;
      }
      Point2D curNearestPoint = null;
      double curSquareMinDistance = Double.POSITIVE_INFINITY;
      for (Point2D point: pointSet) {
         if (point.distanceSquaredTo(p) < curSquareMinDistance) {
            curSquareMinDistance = point.distanceSquaredTo(p);
            curNearestPoint = point;
         }
      }
      return curNearestPoint;
   }
   
   // @Test
   public static void main(String args[]) {
      PointSET pointSET = new PointSET();
      System.out.println(pointSET.isEmpty());
      for (int i = 0; i < 1000; i++) {
         double x = Math.random();
         double y = Math.random();
         Point2D point2D = new Point2D(x, y);
         pointSET.insert(point2D);
         System.out.println(pointSET.contains(point2D) + " ");
      }
      System.out.println();
      
      System.out.println("Size = " + pointSET.size());
      
      System.out.println("Is contains (0.1, 0.1) = " + pointSET.contains(new Point2D(0.1, 0.1)));
      System.out.println();
      
      Point2D nearest = pointSET.nearest(new Point2D(0.1, 0.1));
      System.out.println("Nearest point to (0.1, 0.1) is " + nearest.toString());
      System.out.println();
      
      RectHV rangeRect = new RectHV(0.45, 0.45, 0.55, 0.55);
      for (Point2D inRangePoint: pointSET.range(rangeRect)) {
         System.out.println(inRangePoint.toString());
      }
      
      pointSET.draw();
      
   }
             
}
