
/*
 *  Second week coursera course Java Algorithms part 1
 *  @Date 26.10.13
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 *  			   http://coursera.cs.princeton.edu/algs4/checklists/queues.html
 *  			   http://algs4.cs.princeton.edu/windows/
 *  !!!!!!!!!!!!!!!!!!!!!!!!!!Compilation:   javac-algs4 RandomizedQueue.java with uncommented main
 *  !!!!!!!!!!!!!!!!!!!!!!!!!!Execution:     java-algs4 RandomizedQueue
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  
 *  I use resizing array to approach to resolve this problem
 */

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
	
	private int size;
	
	private Item[] items;
	
	// construct an empty randomized queue
	public RandomizedQueue() {
		items = (Item[]) new Object[2]; // beginning size of items
		size = 0;
	}
	
	// is the queue empty?
	public boolean isEmpty() {
		return (size == 0);
	}
	
	// return the number of items on the queue
	public int size() {
		return size;
	}
	
	// add the item
	public void enqueue(Item item) {
		if (item == null) {
			throw new java.lang.NullPointerException("You want to add empty element");
		}
		if (size == items.length) {
			resizeItems(2 * items.length);
		}
		items[size] = item;
		size++;
	}
	
	// delete and return a random item
	public Item dequeue() {
		if (isEmpty()) {
			throw new java.util.NoSuchElementException("RandomizedQueue is empty!");
		}
		int returnIndex = StdRandom.uniform(size); // choose random item to dequeue in [0, size - 1]
		exchange(returnIndex, (size - 1));
		Item returnItem = items[size - 1];
		items[size - 1] = null; // let gc do it's work
		size--;
		if ((size > 0) && (size == (items.length / 4))) {
			resizeItems(items.length / 2); // decrease items size
		}
		return returnItem;
	}
	
	private void exchange(int randomIndex, int lastItemIndex) {
		if (randomIndex == lastItemIndex) {
			return;
		}
		Item swap = items[randomIndex];
		items[randomIndex] = items[lastItemIndex];
		items[lastItemIndex] = swap;
		swap = null;
	}

	private void resizeItems(int newCapacity) {
		Item[] newItems = (Item[]) new Object[newCapacity];
		for (int i = 0; i < size; i++) {
			newItems[i] = items[i];
		}
		items = newItems;
		newItems = null;
	}
    
	// return (but do not delete) a random item
	public Item sample() {
		if (isEmpty()) {
			throw new java.util.NoSuchElementException("RandomizedQueue is empty!");
		}
		return items[StdRandom.uniform(size)]; // choose random item to dequeue in [0, size - 1]
	}
	
	// return an independent iterator over items in random order
	public Iterator<Item> iterator() {
		return new RandomizedQueueIterator();
	}
	
	// an iterator, doesn't implement remove() since it's optional
    private class RandomizedQueueIterator implements Iterator<Item> {
        
    	private int numberUnwatchedItems;
    	
    	private int[] indexSequence;

        public RandomizedQueueIterator() {
        	numberUnwatchedItems = size;
        	indexSequence = new int[size];
        	for (int i = 0; i < size; i++) {
				indexSequence[i] = i;
			}
        	StdRandom.shuffle(indexSequence); // shuffle sequence of indexes to iterate in random order
        }

        public boolean hasNext() {
            return numberUnwatchedItems > 0;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException("We don't support this operation!");
        }

        public Item next() {
            if (!hasNext()) {
            	throw new java.util.NoSuchElementException("There is nothing to iterate now!");
            }
            numberUnwatchedItems--;
            return items[indexSequence[numberUnwatchedItems]];
        }
    }
	
    /**
     * Unit tests the RandomizedQueue data type.
     */
    public static void main(String[] args) {
    	 RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();
         while (true) {
             String item = StdIn.readString();
             if (item.equals("q")) {
          	   break;
             }
             else if (item.equals("-")) {
          	   StdOut.print(randomizedQueue.dequeue());
             }
             else {
            	 randomizedQueue.enqueue(item);
             }
         }
         
         System.out.println(randomizedQueue.sample());
         
         Iterator<String> iterator = randomizedQueue.iterator();
         while (iterator.hasNext()) {
        	 System.out.print(iterator.next() + " ");
         }
         System.out.println();
         
         for (String i: randomizedQueue) {
        	 System.out.print(i + " ");
         }
         System.out.println();
         
         System.out.println("Size of randomizedQueue = " + randomizedQueue.size());
    }
}
