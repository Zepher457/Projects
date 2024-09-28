
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        // Integer to keep track of current index
        int i = 0;
        // Keep adding to queue until no String are there
        while (!StdIn.isEmpty()) {
            String input = StdIn.readString();
            i++;
            if (1 <= i && i <= k) {
                queue.enqueue(input);
            }
            /* extra credit attempt logic
            Pick random number from 1,...,i
            If that is less than the number of items
            we want in the queue, add the ith item
            to the queue. Else, don't add it.
            This is a method of random sampling
            that will keep queue size to k.
            */
            else if (StdRandom.uniformInt(1, i+1) <= k) {
                queue.dequeue(); 
                queue.enqueue(input); 
                    
            }
        }
        // Iterate k random items
        for (String s: queue) {
            StdOut.println(s);
        }
    }
}
