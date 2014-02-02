import java.awt.Color;

/*************************************************************************
 *  Second week coursera course Java Algorithms part 2
 *  Seam Carving
 *  @Date 26.01.14
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/seamCarving.html
 *             http://coursera.cs.princeton.edu/algs4/checklists/seamCarving.html
 *             http://algs4.cs.princeton.edu/windows/
 *  Compilation:   javac-algs4 SeamCarver.java
 *  Execution:     java-algs4 SeamCarver <name_of_image_file.png> with uncommented main method
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  Seam Carving algorithm for image resizing
 *  
 *  TODO Optimization: can not recreate picture each time we invoke removeSeam(). 
 *                     We can recreate image only when we invoke picture()
 *                     But we should store colors of the image then.
 *                     More details in checklist in optimization section
 *                     http://coursera.cs.princeton.edu/algs4/checklists/seamCarving.html
 *************************************************************************/
public class SeamCarver {
   
   private static final double BORDER_ENERGY = 195075;
   
   private Picture picture;
   
   private double[][] picEnergy;
   
   public SeamCarver(Picture picture) {
      this.picture = picture;
      picEnergy();
   }
   
   // current picture
   public Picture picture() {  
      return picture;
   }

   // width  of current picture
   public int width() {
      return picEnergy.length;
   }
   
   // height of current picture
   public int height() {
      return picEnergy[0].length;
   }
   
   // energy of pixel at column x and row y in current picture
   /**
    * Energy of pixel. (0,0) - left upper corner of picture
    * @param x
    *    column of picture
    * @param y
    *    row of picture
    * @return 
    *    energy of pixel
    */
   public double energy(int x, int y) {
      if (x < 0 || y < 0 || x > width() - 1 || y > height() - 1) throw new 
         java.lang.IndexOutOfBoundsException("Bad argument value!");
      if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) 
         return BORDER_ENERGY;
      if (picEnergy[x][y] != 0) 
         return picEnergy[x][y];
      return squareGradX(x, y) + squareGradY(x, y);
      
   }

   private double squareGradX(int x, int y) {
      Color leftNeighbour = picture.get(x - 1, y);
      Color rightNeighbour = picture.get(x + 1, y);
      int redDiffer = rightNeighbour.getRed() - leftNeighbour.getRed();
      int greenDiffer = rightNeighbour.getGreen() - leftNeighbour.getGreen();
      int blueDiffer = rightNeighbour.getBlue() - leftNeighbour.getBlue();
      return redDiffer * redDiffer + greenDiffer * greenDiffer + blueDiffer * blueDiffer;
   }
   
   
   private double squareGradY(int x, int y) {
      Color upperNeighbour = picture.get(x, y - 1);
      Color bottomNeighbour = picture.get(x, y + 1);
      int redDiffer = bottomNeighbour.getRed() - upperNeighbour.getRed();
      int greenDiffer = bottomNeighbour.getGreen() - upperNeighbour.getGreen();
      int blueDiffer = bottomNeighbour.getBlue() - upperNeighbour.getBlue();
      return redDiffer * redDiffer + greenDiffer * greenDiffer + blueDiffer * blueDiffer;
   }

   // sequence of indices for horizontal seam in current picture
   /**
    * Get sequence of indices for horizontal seam in current picture
    * 
    * @return
    *    sequence of row indices (one per each col) for the seam
    */
   public int[] findHorizontalSeam() {
      
      double[] distTo = new double[width() * height()];
      int[] edgeTo = new int[width() * height()];
      initDistToEdgeTo(distTo, edgeTo);

      // main cycle for computing the seam with smallest energy
      // for each cell in picEnergy except last column
      for (int col = 0; col < width() - 1; col++) {
         for (int row = 0; row < height(); row++) {
            int pixel = pixelNumber(col, row);
            // relax right-upper edge if it exists
            if (row - 1 >= 0)
               relaxEdge(pixel, pixelNumber(col + 1, row - 1), distTo, edgeTo);
            // relax right-middle edge
            relaxEdge(pixel, pixelNumber(col + 1, row), distTo, edgeTo);
            // relax right-bottom edge if it exists
            if (row + 1 <= height() - 1)
               relaxEdge(pixel, pixelNumber(col + 1, row + 1), distTo, edgeTo);
         }
      }

      // search for min distTo. It's a beginning of the seam with smallest
      // energy
      double curMinDist = Double.POSITIVE_INFINITY;
      int lastSeamPixel = -1;
      for (int row = 0; row < height(); row++) {
         if (distTo[pixelNumber(width() - 1, row)] < curMinDist) {
            curMinDist = distTo[pixelNumber(width() - 1, row)];
            lastSeamPixel = pixelNumber(width() - 1, row);
         }
      }
      return restoreHorSeam(lastSeamPixel, edgeTo);
   }
   
   private void initDistToEdgeTo(double[] distTo, int[] edgeTo) {
      for (int col = 0; col < width(); col++) {
         for (int row = 0; row < height(); row++) {
            if (col == 0) 
               distTo[pixelNumber(col, row)] = 0;
            else 
               distTo[pixelNumber(col, row)] = Double.POSITIVE_INFINITY;
            edgeTo[pixelNumber(col, row)] = -1;
         }
      }
      
   }

   private void picEnergy() {
      picEnergy = new double[picture.width()][picture.height()];
      for (int col = 0; col < width(); col++) 
         for (int row = 0; row < height(); row++)
            picEnergy[col][row] = energy(col, row);
   }
   
   private int pixelNumber(int col, int row) {
      return width() * row + col;
   }
   
   private int colFromNumber(int pixelNumber) {
      return pixelNumber % width();
   }
   
   private int rowFromNumber(int pixelNumber) {
      return pixelNumber / width();
   }
   
   private void relaxEdge(int fromPixel, int toPixel, double[] distTo, int[] edgeTo) {
      
      if (distTo[fromPixel] + picEnergy[colFromNumber(toPixel)][rowFromNumber(toPixel)] <
            distTo[toPixel]) {
         distTo[toPixel] = distTo[fromPixel] + 
               picEnergy[colFromNumber(toPixel)][rowFromNumber(toPixel)];
         edgeTo[toPixel] = fromPixel;
      }
   }
   
   private int[] restoreHorSeam(int lastHorSeamPixel, int[] edgeTo) {
      int[] horSeam = new int[width()];
      int curPixel = lastHorSeamPixel;
      int i = horSeam.length - 1;
      // while not beginning of the seam. LeftCol = -1
      while (curPixel != -1) {
         horSeam[i] = rowFromNumber(curPixel);
         curPixel = edgeTo[curPixel];
         i--;
      }
      return horSeam;
   }
   
   /**
    * Get sequence of indices for vertical   seam in current picture
    * 
    * @return
    *    sequence of col indices (one per each row) for the seam
    */
   // sequence of indices for vertical   seam in current picture
   public int[] findVerticalSeam() {
      // transpose image
      double[][] backupPicEnergy = picEnergy;
      transposePicEnergy();
      // find horizontal seam
      int[] verSeam  = findHorizontalSeam();
      // transpose back
      picEnergy = backupPicEnergy;
      return verSeam;
   }
   
   private void transposePicEnergy() {
      double[][] transposedPicEnergy = new double[height()][width()];
      for (int col = 0; col < width(); col++) 
         for (int row = 0; row < height(); row++) 
            transposedPicEnergy[row][col] = picEnergy[col][row];
      picEnergy = transposedPicEnergy;
   }

   /**
    * Remove horizontal seam from current picture
    * 
    * @param a
    *    sequence of row indices (one per each col) for the seam to remove
    */
   // remove horizontal seam from current picture
   public void removeHorizontalSeam(int[] a) {
      if (height() <= 1) {
         throw new java.lang.IllegalArgumentException("height <= 1");
      }

      if (a.length != width()) {
         throw new java.lang.IllegalArgumentException();
      }
      
      Picture updatedPicture = new Picture(width(),height() - 1);
      double[][] updatedPicEnergy = new double[width()][height() - 1];
      for (int col = 0; col < width(); col++) {
         // copy the upper part of the picture and picEnergy
         for (int row = 0; row < a[col]; row++) {
            updatedPicture.set(col, row, picture.get(col, row));
            updatedPicEnergy[col][row] = picEnergy[col][row];
         }
         // System.arraycopy(picEnergy[col], 0, updatedPicEnergy[col], 0, a[col]); 
         
         // copy the bottom part of the picEnergy
         for (int row = a[col] + 1; row < height(); row++) {
            updatedPicture.set(col, row - 1, picture.get(col, row));
            updatedPicEnergy[col][row - 1] = picEnergy[col][row];
         }
         // System.arraycopy(picEnergy, a[col] + 1, updatedPicEnergy, a[col], picEnergy[col].length - a[col]);
      }
      picture = updatedPicture;
      picEnergy = updatedPicEnergy;
   }
   
   /**
    * Remove vertical seam from current picture
    * 
    * @param a
    *    sequence of col indices (one per each row) for the seam to remove
    */
   // remove vertical   seam from current picture
   public void removeVerticalSeam(int[] a) {
      if (width() <= 1) {
         throw new java.lang.IllegalArgumentException("width <= 1");
      }

      if (a.length != height()) {
         throw new java.lang.IllegalArgumentException();
      }
      
      Picture updatedPicture = new Picture(width() - 1,height());
      double[][] updatedPicEnergy = new double[width() - 1][height()];
      for (int row = 0; row < height(); row++) {
         // copy the upper part of the picture and picEnergy
         for (int col = 0; col < a[row]; col++) {
            updatedPicture.set(col, row, picture.get(col, row));
            updatedPicEnergy[col][row] = picEnergy[col][row];
         }
         // copy the bottom part of the picEnergy
         for (int col = a[row] + 1; col < width(); col++) {
            updatedPicture.set(col - 1, row, picture.get(col, row));
            updatedPicEnergy[col - 1][row] = picEnergy[col][row];
         }  
      }
      
      picture = updatedPicture;
      picEnergy = updatedPicEnergy;
   }
   
   public static void main(String[] args) {
      Picture pic = new Picture(args[0]);
      SeamCarver sc = new SeamCarver(pic);
      System.out.println("Picture width = " + sc.width());
      System.out.println("Picture height = " + sc.height());
      
      // (0,0) - left upper corner of picture
      for (int row = 0; row < sc.height(); row++) {
         for (int col = 0; col < sc.width(); col++) { 
            System.out.print(sc.energy(col, row) + " ");
         }
         System.out.println();
      }
      System.out.println();
      
      System.out.print("Horizontal Seam = ");
      int[] horSeam = sc.findHorizontalSeam();
      for (int i = 0; i < horSeam.length; i++) {
         System.out.print(horSeam[i] + " ");
      }
      System.out.println();
      sc.removeHorizontalSeam(horSeam);
      
      
      System.out.print("Vertical Seam = ");
      int[] verSeam = sc.findVerticalSeam();
      for (int i = 0; i < verSeam.length; i++) {
         System.out.print(verSeam[i] + " ");
      }
      System.out.println();
      sc.removeVerticalSeam(verSeam);
      
      System.out.println("Energies after removal = ");
      for (int row = 0; row < sc.height(); row++) {
         for (int col = 0; col < sc.width(); col++) { 
            System.out.print(sc.energy(col, row) + " ");
         }
         System.out.println();
      }
      System.out.println();
   }

}
