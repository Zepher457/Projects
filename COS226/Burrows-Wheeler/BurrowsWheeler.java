import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    private static final int R = 256;
    
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray arr = new CircularSuffixArray(s);
        int n = s.length();
        int row = 0; // sorted index of original string
        // finding row and writing it to console
        for (int i = 0; i < n; i++) {
            if (arr.index(i) == 0) {
                row = i;
                BinaryStdOut.write(row);
                break;
            }
        }
        // Finding t 
        /* Note that given index[i] we know the
        first character of some suffix array
        as a result this (index - 1 + n ) %n
        is the last index of this suffix array */
        for (int i = 0; i < n; i++) {
            int index = (arr.index(i) - 1 + n) % n;
            BinaryStdOut.write(s.charAt(index));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        int n = t.length();
        int[] next = new int[n]; // next array
        int[] count = new int[R+1]; // array for key indexed counting
        /*  key indexed counting to sort t
        to ofind next array */
        for (int i = 0; i < n; i++) {
            count[t.charAt(i)+1]++;
        }
        for (int i = 0; i < R; i++) {
            count[i+1] += count[i];
        }
        for (int i = 0; i < n; i++) {
            int index = count[t.charAt(i)]++;
            next[index] = i;
        }
        // Writing out the sorted suffixes 
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(t.charAt(next[first]));
            first = next[first];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-"))
            transform();
        else if (args[0].equals("+"))
            inverseTransform();
    }

}
