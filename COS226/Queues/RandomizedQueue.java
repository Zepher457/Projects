import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    // Size variable
    private int size;
    // Array to store values in Queue
    private Item[] items;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() { return size == 0; }

    // return the number of items on the randomized queue
    public int size() { return size; }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        // Conditions to resize
        if (size == items.length) resize(2*items.length);
        // Add item to queue
        items[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        // Getting random index
        int randomIndex = StdRandom.uniformInt(size);
        Item item = items[randomIndex];
        /*  Order doesn't matter, and we need an efficient way 
         * to delete random item, so we send it to the back,
         * set it to null, and garbage collector unallocates memory
         * to prevent loitering
        */
        if (randomIndex != size -1) items[randomIndex] = items[size - 1];
        items[size - 1] = null;
        size--;
        // resizing logic
        if (size > 0 && size == items.length / 4) resize(items.length / 2);
        return item;
    }
    // method to resize array
    private void resize(int capacity) {
        Item[] newItems = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newItems[i] = items[i];
        }
        items = newItems;
    }
    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return items[StdRandom.uniformInt(size)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomArrayIterator();
    }
    private class RandomArrayIterator implements Iterator<Item> {
        // index to keep track of current item
        private int index = 0;
        // array containing randomized queue items
        private Item[] randomizedItems = (Item[]) new Object[size];
        // Constructor to randomize items[]
        public RandomArrayIterator() {
            for (int i = 0; i < size; i++) {
                randomizedItems[i] = items[i];
            }
            StdRandom.shuffle(randomizedItems);
        }

        public boolean hasNext() {
            return index < size;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return randomizedItems[index++];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        // Checking whether queue is empty
        StdOut.println(queue.isEmpty());
        // Adding items to queue
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");
        queue.enqueue("4");
        queue.enqueue("5");
        StdOut.println();
        // Checking whether size has changed to 5
        StdOut.println(queue.size());
        StdOut.println();
        // Sampling random item in queue
        StdOut.println(queue.sample());
        StdOut.println();
        // Deleting two random items
        queue.dequeue();
        queue.dequeue();
        // Checking whether size is 3
        StdOut.println(queue.size());
        StdOut.println();
        // Iteratoring elements
        for (String s: queue) {
            StdOut.println(s);
        }

    }

}
