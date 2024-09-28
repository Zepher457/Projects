import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

 
public class BinarySearchDeluxe {
 
    // Returns the index of the first key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        // check for valid inputs
        if (a == null || key == null || comparator == null)
            throw new IllegalArgumentException("Arguments cannot be null");

        // create pointers for lower bound and upper bound
        int lo = 0;
        int hi = a.length - 1;

        // loop as long as the pointers don't cross each other
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1; // Adapted from Elementary Sorts Lecture
            int compare = comparator.compare(key, a[mid]);  

            // update upper bound if the element is in the first half
            if (compare < 0) hi = mid - 1;
            // update lower bound if the element is in the second half
            else if (compare > 0) lo = mid + 1;
            // if you already found the element, update the higher bound
            // to check if there are previous matching elements
            else if (lo != mid) hi = mid;
            else return mid;
        }
        return -1;
    }
 
    // Returns the index of the last key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        // check for valid inputs
        if (a == null || key == null || comparator == null)
            throw new IllegalArgumentException("Arguments cannot be null");

        // create pointers for lower bound and upper bound
        int lo = 0;
        int hi = a.length - 1;

        // loop as long as the pointers don't cross each other
        while (lo <= hi) {
            int mid = (lo + hi + 1) >>> 1;
            int compare = comparator.compare(key, a[mid]);

            // update upper bound if the element is in the first half
            if (compare < 0)  hi = mid - 1;
            // update lower bound if the element is in the second half
            else if (compare > 0) lo = mid + 1;
            // if you already found the element, update the lower bound
            // to check if there are successive matching elements
            else if (hi != mid) lo = mid;
            else return mid;
        }
        return -1;
    }
    // unit testing (required)
    public static void main(String[] args) {
        // create a string array of length 100, filled with 0s
        String[] a = new String[1000];
        for (int i = 0; i < a.length; i++) {
            a[i] = "" + 0; 
        }

        Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;
        StdOut.println("first index: " + firstIndexOf(a, "0", comparator));
        StdOut.println("last index: " + lastIndexOf(a, "0", comparator));
    }
}