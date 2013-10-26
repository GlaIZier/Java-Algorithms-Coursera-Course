/*
 *  Second week coursera course Java Algorithms part 1
 *  @Date 23.10.13
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 *  			   http://coursera.cs.princeton.edu/algs4/checklists/queues.html
 *  			   http://algs4.cs.princeton.edu/windows/
 *  !!!!!!!!!!!!!!!!!!!!!!!!!!Compilation:   javac-algs4 Deque.java with uncommented main
 *  !!!!!!!!!!!!!!!!!!!!!!!!!!Execution:     java-algs4 Deque
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  
 *  I use linked list approach to resolve this problem
 */


import java.util.Iterator;

// must implements iterator which returns iterator;
public class Deque<Item> implements Iterable<Item> {
   
   private int size;  // size of Deque
   
   private Node<Item> first;  // top of Deque
   
   private Node<Item> last;  // tail of Deque
   
   private static class Node<Item> {
      
      private Item item;
	  
      private Node<Item> next;
      
      private Node<Item> prev;
   }
   
   // construct an empty deque
   public Deque() {
	   size = 0;
	   first = null;
	   last = null;
   }
   
   // is the deque empty?
   public boolean isEmpty() {
	   return (size == 0);
   }
   
   // return the number of items on the deque
   public int size() {
	   return size;
   }
   
   // insert the item at the front
   public void addFirst(Item item) {
	   if (item == null) {
		   throw new java.lang.NullPointerException("You want to add empty element");
	   }
	   Node<Item> newFirstNode = new Node<Item>();
	   newFirstNode.next = first;
	   newFirstNode.prev = null;
	   newFirstNode.item = item;
	   if (first != null) {
		   first.prev = newFirstNode;
	   }
	   else { // add first node
		   last = newFirstNode;
	   }
	   first = newFirstNode;
	   newFirstNode = null;
	   size++;
   }
   
   // insert the item at the end
   public void addLast(Item item) {
	   if (item == null) {
		   throw new java.lang.NullPointerException("You want to add empty element");
	   }
	   Node<Item> newLastNode = new Node<Item>();
	   newLastNode.next = null;
	   newLastNode.prev = last;
	   newLastNode.item = item;
	   if (last != null) {
		   last.next = newLastNode;
	   }
	   else { // add first node
		   first = newLastNode; 
	   }
	   last = newLastNode;
	   newLastNode = null;
	   size++;
   }

   // delete and return the item at the front
   public Item removeFirst() {
	   if (isEmpty()) {
		   throw new java.util.NoSuchElementException("Deque is empty!");
	   }
	   Node<Item> removingNode = first;
	   first = first.next;
	   removingNode.next = null;
	   if (first != null) {
		   first.prev = null;
	   }
	   else { // remove last node.
		   last = null;
	   }
	   Item removingItem = removingNode.item;
	   removingNode = null; // let gc do it's job
	   size--;
	   return removingItem;
   }
  
   // delete and return the item at the end
   public Item removeLast() {
	   if (isEmpty()) {
		   throw new java.util.NoSuchElementException("Deque is empty!");
	   }
	   Node<Item> removingNode = last;
	   last = last.prev;
	   removingNode.prev = null;
	   if (last != null) {
		   last.next = null;
	   }
	   else { // remove last node 
		   first = null;
	   }
	   Item removingItem = removingNode.item;
	   removingNode = null; // let gc do it's job
	   size--;
	   return removingItem;
   }
   
   // return an iterator over items in order from front to end
   public Iterator<Item> iterator() {
	   return new DequeIterator<Item>(first);
   }
   
   // nested class
   // iterator must implements hasNext, next and remove
   private class DequeIterator<Item> implements Iterator<Item> {
       
	   private Node<Item> current;

       public DequeIterator(Node<Item> first) {
           current = first;
       }
       
       public boolean hasNext() { 
    	   return (current != null); 
       }
       
       public void remove() { 
    	   throw new java.lang.UnsupportedOperationException("We don't support this operation!"); 
       }

       public Item next() {
           if (!hasNext()) {
        	   throw new java.util.NoSuchElementException("There is nothing to iterate now!");
           }
           Item item = current.item;
           current = current.next; // go to the next node
           return item;
       }
   }
   
   /**
    * Unit tests the Deque data type.
    */
   public static void main(String[] args) {
       Deque<String> deque = new Deque<String>();
       while (true) {
           String item = StdIn.readString();
           if (item.equals("q")) {
        	   break;
           }
           else if (item.equals("-")) {
        	   StdOut.print(deque.removeLast());
           }
           else if (item.equals("=")) {
        	   StdOut.print(deque.removeFirst()); 
           }
           else if (item.contains("tail")) {
        	   deque.addLast(item);
           }
           else {
        	   deque.addFirst(item);
           }
       }
       
       // first way to iterate
       Iterator<String> iterator = deque.iterator();
       while (iterator.hasNext()) {
    	   System.out.print(iterator.next() + " ");
       }
       System.out.println();
       
       // second way to iterate
       for (String i: deque) {
    	   System.out.print(i + " ");
       }
       System.out.println();
       
       System.out.println("Size of deque = " + deque.size());
   }
}
