
/*************************************************************************
 *  Forth week coursera course Java Algorithms part 1
 *  kdTree
 *  @Date 17.11.13
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html
 *             http://coursera.cs.princeton.edu/algs4/checklists/kdtree.html
 *             http://algs4.cs.princeton.edu/windows/
 *  Compilation:   javac-algs4 PointSET.java with uncommented main method
 *  Execution:     java-algs4 PointSET
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  KdTree algorithm to resolve nearest point and range problems
 *
 *************************************************************************/
public class KdTree {
   
   private static class Node implements Comparable<Node> {
      
      private Point2D p;      // the point
      
      private RectHV rect;    // the axis-aligned rectangle corresponding to this node
      
      private Node lb;        // the left/bottom subtree
      
      private Node rt;        // the right/top subtree
      
      private boolean isVertLineNode;
      
      private Node(Point2D point, RectHV rectRange, boolean isVertLineNode) {
         this.p = point;
         this.rect = rectRange;
         this.lb = null;
         this.rt = null;
         this.isVertLineNode = isVertLineNode;
      }
      
      public int compareTo(Node that) {
         if (this.isVertLineNode) {
            if (this.p.x() < that.p.x()) {
               return -1;
            }
            else if ( (this.p.x() == that.p.x() ) && (this.p.y() == that.p.y() )) {
               return 0;
            }
            else {
               return 1;
            }
         }
         else {
            if (this.p.y() < that.p.y()) {
               return -1;
            }
            else if ( (this.p.y() == that.p.y()) && (this.p.x() == that.p.x())) {
               return 0;
            }
            else {
               return 1;
            }
         }
      }
   }
   
   private Node root;
   
   private int size;
   
   // construct an empty set of points
   public KdTree() {
      root = null;
      size = 0;
   }
   
   // is the set empty?
   public boolean isEmpty() {
      return (size == 0);
   }
   
   // number of points in the set
   public int size() {
      return size;
   }
   
   // add the point p to the set (if it is not already in the set)
   public void insert(Point2D p) {
      // create new Node
      Node newNode = new Node(p, null, false);
      // if isEmpty => new root
      if (isEmpty()) {
         newNode.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
         newNode.isVertLineNode = true;
         root = newNode;
         size++;
         return;
      }
      // begin initialization
      Node curNode = root;
      Node prevCurNode = null;
      boolean isLastStepLeft = true;
      // go down the tree until next subtree will be null
      while (curNode != null) {
         prevCurNode = curNode;
         // check if p is in the tree (then return)
         if (curNode.p.equals(p)) {
            return;
         }
         //  compare x coords or y coords and go to the corresponding subtree
         if (curNode.compareTo(newNode) == -1) {
            curNode = curNode.rt;
            isLastStepLeft = false;
         }
         else if (curNode.compareTo(newNode) == 1) {
            curNode = curNode.lb;
            isLastStepLeft = true;
         }
         else {
            return;
            //throw new UnsupportedOperationException("Unexpected situation. Two node points are equal!");
         }
      }
      // go back to prev Node
      // place new Node, size++
      curNode = prevCurNode;
      if (isLastStepLeft) {
         curNode.lb = newNode;
      }
      else {
         curNode.rt = newNode;
      }
      if (curNode.isVertLineNode) {
         newNode.isVertLineNode = false;
         // hor left line
         if (isLastStepLeft) {
            newNode.rect = new RectHV(curNode.rect.xmin(), curNode.rect.ymin(), curNode.p.x(), curNode.rect.ymax());
         }
         // hor right line
         else {
            newNode.rect = new RectHV(curNode.p.x(), curNode.rect.ymin(), curNode.rect.xmax(), curNode.rect.ymax());
         }
      }
      else {
         newNode.isVertLineNode = true;
         // vert bottom line
         if (isLastStepLeft) {
            newNode.rect = new RectHV(curNode.rect.xmin(), curNode.rect.ymin(), curNode.rect.xmax(), curNode.p.y());
         }
         // vert top line
         else {
            newNode.rect = new RectHV(curNode.rect.xmin(), curNode.p.y(), curNode.rect.xmax(), curNode.rect.ymax());
         }
      }
      size++;
      curNode = null;
      newNode = null;
      prevCurNode = null;
   }
   
   // does the set contain the point p?
   public boolean contains(Point2D p) {
      // go through the tree to leafs;
      if (isEmpty()) {
         return false;
      }
      Node desiredNode = new Node(p, null, false);
      Node curNode = root;
      while (curNode != null) {
         if (curNode.p.equals(desiredNode.p)) {
            return true;
         }
         if (curNode.compareTo(desiredNode) == -1) {
            curNode = curNode.rt;
         }
         else if (curNode.compareTo(desiredNode) == 1) {
            curNode = curNode.lb;
         }
         else {
            return true;
            //throw new UnsupportedOperationException("Unexpected situation. Two node points are equal!");
         }
      }
      return false;
   }
   
   // draw all of the points to standard draw
   public void draw() {
      drawPointsAndLines(root, 0.0, 0.0, 1.0, 1.0);
   }
   
   private void drawPointsAndLines(Node curNode, double minX, double minY, double maxX, double maxY) {
      // check if cur is null
      if (curNode == null) {
         return;
      }
      // draw cur Point
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.setPenRadius(.01);
      curNode.p.draw();
      
      StdDraw.setPenRadius(.001);
      // if cur is vertical 
      if (curNode.isVertLineNode) {
         // draw vert red line
         StdDraw.setPenColor(StdDraw.RED);
         StdDraw.line(curNode.p.x(), minY, curNode.p.x(), maxY);
         // recursive call to subtrees
         drawPointsAndLines(curNode.lb, minX, minY, curNode.p.x(), maxY);
         drawPointsAndLines(curNode.rt, curNode.p.x(), minY, maxX, maxY);
      }
      // else
      else {
         // draw hor blue line 
         StdDraw.setPenColor(StdDraw.BLUE);
         StdDraw.line(minX, curNode.p.y(), maxX, curNode.p.y());
         // recursive call to subtrees
         drawPointsAndLines(curNode.lb, minX, minY, maxX, curNode.p.y());
         drawPointsAndLines(curNode.rt, minX, curNode.p.y(), maxX, maxY);
      }
   }
   
   // draw green rectangle by the coords
   private void drawRect(double minX, double minY, double maxX, double maxY) {
      StdDraw.setPenColor(StdDraw.GREEN);
      StdDraw.setPenRadius(.001);
      StdDraw.line(minX, minY, minX, maxY);
      StdDraw.line(minX, maxY, maxX, maxY);
      StdDraw.line(maxX, maxY, maxX, minY);
      StdDraw.line(maxX, minY, minX, minY);
   }
   
   // all points in the set that are inside the rectangle
   public Iterable<Point2D> range(RectHV rect) {
      ResizingArrayQueue<Point2D> rAQ = new ResizingArrayQueue<Point2D>();
      findAllPoints(root, rect, rAQ);
      return rAQ;
   }
   
   private void findAllPoints(Node curNode, final RectHV rect, 
         ResizingArrayQueue<Point2D> rAQ) {
      // recursive strategy
      if (curNode == null) {
         return;
      }
      // if cur in rect add cur to rAQ
      if (rect.contains(curNode.p)) {
         rAQ.enqueue(curNode.p); 
      }
      // if rect intersects rect (~ line) of current node
      if (curNode.rect.intersects(rect)) {
         // call findAllPoints for 2 subtrees
         findAllPoints(curNode.lb, rect, rAQ);
         findAllPoints(curNode.rt, rect, rAQ);
      }
      // else if rect less(to the left or below) than curPoint
      else if ( ( (curNode.isVertLineNode) && (rect.xmax() < curNode.p.x() ) ) || 
            ( (!curNode.isVertLineNode) && (rect.ymax() < curNode.p.y() ) ) ) {
         // call findAllPoints for left subtree
         findAllPoints(curNode.lb, rect, rAQ);
      }
      // else
      else {
         // call findAllPoints for right subtree
         findAllPoints(curNode.rt, rect, rAQ);
      }      
   }
   
   // a nearest neighbor in the set to p; null if set is empty
   public Point2D nearest(Point2D p) {
      return recursiveNearest(root, p, root.p);
   }

   private Point2D recursiveNearest(Node curNode, final Point2D queryPoint, Point2D candidatePoint) {
      if (curNode != null) {
         // calc distance to query point from cur
         // if it less then curMinDist 
         if (candidatePoint.distanceSquaredTo(queryPoint) > curNode.p.distanceSquaredTo(queryPoint)) {
            // update curNearestPoint
            candidatePoint = curNode.p;
         }
         
         // if query less then cur (to the left or below)
         Node queryNode = new Node(queryPoint, null, false);
         if (curNode.compareTo(queryNode) == 1) {
            // look to the lb subtree
            Point2D newLeftPoint = recursiveNearest(curNode.lb, queryPoint, candidatePoint);
            // if cur == returnFromLeftSubtree then it hasn't changed => have to look to another subtree 
            // But look only if the distance of subtree's rect less then curMinDist and subtree is not null
            if (newLeftPoint.equals(candidatePoint) && (curNode.rt != null) && 
                  (curNode.rt.rect.distanceSquaredTo(queryPoint) < candidatePoint.distanceSquaredTo(queryPoint) ) ) {
               candidatePoint = recursiveNearest(curNode.rt, queryPoint, candidatePoint);
            }
            else {
               // if not equal, it means that newLeftPoint is better. We must save new candidate Point 
               candidatePoint = newLeftPoint;
            }
            newLeftPoint = null;
         }
         // if larger then 
         else {
            // look to the rt subtree
            Point2D newRightPoint = recursiveNearest(curNode.rt, queryPoint, candidatePoint);
            // if cur == returnFromLeftSubtree then it hasn't changed => have to look to another subtree.
            // But look only if the distance of subtrees rect less then curMinDist and subtree is not null
            if (newRightPoint.equals(candidatePoint) && (curNode.lb != null) && 
                  (curNode.lb.rect.distanceSquaredTo(queryPoint) < candidatePoint.distanceSquaredTo(queryPoint) ) ) {
               candidatePoint = recursiveNearest(curNode.lb, queryPoint, candidatePoint);
            }
            else {
               // if new Point not equal, it means that it is better. we must save new candidate Point 
               candidatePoint = newRightPoint;
            }
            newRightPoint = null;
         }
         queryNode = null;
      }
      return candidatePoint;
   }
   
   // @Test
   public static void main(String args[]) {
      KdTree kdTree = new KdTree();
      for (int i = 0; i < 1000; i++) {
         double x = Math.random();
         double y = Math.random();
         Point2D point2D = new Point2D(x, y);
         kdTree.insert(point2D);
         System.out.println(kdTree.contains(point2D) + " ");
      }
//      kdTree.insert(new Point2D(0.206107, 0.095492));
//      kdTree.insert(new Point2D(0.975528, 0.654508));
//      kdTree.insert(new Point2D(0.024472, 0.345492));
//      kdTree.insert(new Point2D(0.793893, 0.095492));
//      kdTree.insert(new Point2D(0.793893, 0.904508));
//      kdTree.insert(new Point2D(0.975528, 0.345492));
//      kdTree.insert(new Point2D(0.206107, 0.904508));
//      kdTree.insert(new Point2D(0.500000, 0.000000));
//      kdTree.insert(new Point2D(0.024472, 0.654508));
//      kdTree.insert(new Point2D(0.500000, 1.000000));
    
      System.out.println();
      
      System.out.println("Size = " + kdTree.size());
      
      System.out.println("Is contains (0.1, 0.1) = " + kdTree.contains(new Point2D(0.1, 0.1)));
      System.out.println();
      
      kdTree.drawRect(0.45, 0.45, 0.55, 0.55);
      kdTree.draw();
      
      RectHV rangeRect = new RectHV(0.45, 0.45, 0.55, 0.55);
      for (Point2D inRangePoint: kdTree.range(rangeRect)) {
         System.out.println(inRangePoint.toString());
      }
      
      System.out.println("Nearest point is " + kdTree.nearest(new Point2D(0.5, 0.5)).toString());
   }
}
