import edu.princeton.cs.algs4.StdOut;
 
import java.util.Arrays;
import java.util.Comparator;
 
public class CircularSuffixArray {
    // array of circular suffixes
    private CircularSuffix[] strings;
    // array of starting indices of suffixes
    private int[] index;
 
    // circular suffix array of s
    public CircularSuffixArray(String s) {
        // corner case
        if (s == null) throw new IllegalArgumentException();
        // length of string
        int n = s.length();
        strings = new CircularSuffix[n];
        index = new int[n];
        // creating circular suffixes
        for (int i = 0; i < n; i++) {
            strings[i] = new CircularSuffix(s, i);
        }
        // sorting circular suffixes
        if (n > 0) Arrays.sort(strings, strings[0].byChar());
        // filling index with sorted indices
        for (int i = 0; i < n; i++) {
            index[i] = strings[i].i;
        }
    }

    // helper class to represent suffixes
    public class CircularSuffix implements Comparable<CircularSuffix> {
        // original string
        private String s;
        // staring index of suffix
        private int i;
        // constructor
        public CircularSuffix(String s, int index) {
            this.s = s;
            i = index;
        }
        // compareTo method to sort suffix strings
        public int compareTo(CircularSuffix that) {
            // loop through characters and compare
            // corresponding indices at substrings
            for (int j = 0; j < s.length(); j++) {
                // if the strings are equal keep going
                if (Character.compare(s.charAt((i + j) % s.length()),
                                      that.s.charAt((that.i + j) % s.length())) == 0) {
                    continue;
                }
                // if they aren't, compare them
                else {
                    return Character.compare(s.charAt((i + j) % s.length()),
                                             that.s.charAt((that.i + j) % s.length()));
                }
            }
            // if they're equal the entire time, return 0
            return 0;
        }
        // method to access index of suffix array
        public int ind() {
            return i;
        }
 
        // comparator
        public Comparator<CircularSuffix> byChar() {
            return new charCompare();
        }
        // comparator class
        private class charCompare implements Comparator<CircularSuffix> {
            public int compare(CircularSuffix s1, CircularSuffix s2) {
                return s1.compareTo(s2);
            }
        }
    }
 
    // length of s
    public int length() {
        return strings.length;
    }
 
    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length()) throw new IllegalArgumentException();
        return index[i];
    }
 
    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray c = new CircularSuffixArray("CADABRA!ABRA");
        StdOut.println(c.length());
        StdOut.println(c.index(0));
        StdOut.println(c.index(4));
 
        // StdOut.println("index: " + c.index(1));
    }
 
}