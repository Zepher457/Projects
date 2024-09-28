import java.util.Iterator;
import java.util.NoSuchElementException;


import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    // Nodes to keep track of front and back of Deque
    private Node first, last;
    // size variable
    private int size;

    private class Node {
        public Item item; // content of Node
        public Node next; // pointer to next node
        public Node prev; // pointer to previous node
    }
    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() { return first == null; }

    // return the number of items on the deque
    public int size() { return size; }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node newNode = new Node();
        // Shifting nodes to the right
        newNode.next = first;
        newNode.item = item;
        // Corner case
        if (first == null && last == null) last = newNode;
        // Creating pointer from newNode
        else first.prev = newNode;
        // Assigning new first
        first = newNode;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node newNode = new Node();
        // Shifting nodes to the left
        newNode.prev = last;
        newNode.item = item;
        // Corner case
        if (first == null && last == null) first = newNode;
        // Creating pointer to newNode
        else last.next = newNode;
        // Assinging new last 
        last = newNode;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (first == null) throw new NoSuchElementException();
        Item item = first.item;
        // corner case
        if (first.next == null) last = null;
        // eliminating pointer to old first
        else first.next.prev = null;
        // assigning new first
        first = first.next;
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (last == null) throw new NoSuchElementException();
        Item item = last.item;
        // corner case
        if (last.prev == null) first = null;
        // elinating pointer to old last
        else last.prev.next = null;
        // assigning new last
        last = last.prev;
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    private class DequeIterator implements Iterator<Item> {
        /*  Start the iterator at the first note 
         * this variable also keeps track of the current
         * node in the Deque
        */
        private Node current = first;
        
        public boolean hasNext() { return current != null; }

        public Item next() { 
            if (!hasNext()) throw new NoSuchElementException();
            Item s = current.item;
            current = current.next;
            return s;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        // Checking whether empty Deque is created
        StdOut.println(deque.isEmpty());
        // Adding some items
        deque.addFirst(1);
        deque.addLast(2);
        deque.addFirst(3);
        deque.addLast(4);
        // Removing and adding
        deque.removeLast();
        deque.addLast(10);
        deque.removeFirst();
        deque.addFirst(6);
        // Checking whether size is 4
        StdOut.println(deque.size());
        StdOut.println();
        // Iterating
        for (int i: deque) {
            StdOut.println(i);
        }
    }

}
