
/*
 *  Second week coursera course Java Algorithms part 1
 *  @Date 26.10.13
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 *  			   http://coursera.cs.princeton.edu/algs4/checklists/queues.html
 *  			   http://algs4.cs.princeton.edu/windows/
 *  !!!!!!!!!!!!!!!!!!!!!!!!!!Compilation:   javac-algs4 Subset.java 
 *  !!!!!!!!!!!!!!!!!!!!!!!!!!Execution:     java-algs4 Subset <Number of random elements to show = k>
 *  Execution only with installed algs4.jar and stdlib.jar; compiled RandomizedQueue.java and Deque.java
 *  
 *  
 */
public class Subset {
	
	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]);
		RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();
//		while (true) {
//			String item = StdIn.readString();
//			if (item.equals("q")) {
//				break;
//			}
//			randomizedQueue.enqueue(item);
//		}
		while (!StdIn.isEmpty()) {
			String item = StdIn.readString();
			randomizedQueue.enqueue(item);
		}
		for (int i = 0; i < k; i++) {
			System.out.println(randomizedQueue.dequeue());
		}
	}
	
}
