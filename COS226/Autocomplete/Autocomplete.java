import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Autocomplete {

    private Term[] terms; // array of terms

    // Initializes the data structure from the given array of terms.
    public Autocomplete(Term[] terms) {
        // terms array cannot be null
        if (terms == null)
            throw new IllegalArgumentException("Array cannot be null");

        this.terms = new Term[terms.length];

        // copies items from argument array into initialized array
        for (int i = 0; i < terms.length; i++) {
            if (terms[i] == null)
                throw new IllegalArgumentException("Argument cannot be null");

            this.terms[i] = terms[i];
        }

        // calls Mergesort on the array
        Arrays.sort(this.terms);

    }

    // Returns all terms that start with the given prefix,
    // in descending order of weight.
    public Term[] allMatches(String prefix) {
        // checks for valid prefix
        if (prefix == null)
            throw new IllegalArgumentException("Argument cannot be null");

        // finds the first matching term by running binary search
        int firstKey = BinarySearchDeluxe.firstIndexOf(terms, new Term(prefix, 0),
                                                 Term.byPrefixOrder(prefix.length()));
        // if there is no matching term, return an empty array
        if (firstKey == -1) return new Term[0];

        // find the last matching term by running binary search
        int lastKey = BinarySearchDeluxe.lastIndexOf(terms, new Term(prefix, 0),
                                               Term.byPrefixOrder(prefix.length()));

        // create array of terms and fill it with the matching terms 
        Term[] matches = new Term[lastKey - firstKey + 1];

        for (int i = 0; i < matches.length; i++) {
            matches[i] = terms[firstKey++];
        }

        // sort by reverse weight order
        Arrays.sort(matches, Term.byReverseWeightOrder());

        return matches;
    }

    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
        // checks for valid prefix
        if (prefix == null)
            throw new IllegalArgumentException("Argument cannot be null");

        // finds first matching term by running binary search
        int firstKey = BinarySearchDeluxe.firstIndexOf(terms, new Term(prefix, 0),
                                                Term.byPrefixOrder(prefix.length()));
        // if there is no matching term, return an empty array
        if (firstKey == -1) return 0;

        // find the last matching term by running binary search
        int lastKey = BinarySearchDeluxe.lastIndexOf(terms, new Term(prefix, 0),
                                               Term.byPrefixOrder(prefix.length()));
        
        return lastKey - firstKey + 1;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // read in the terms from a file
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        Term[] terms = new Term[n];
        for (int i = 0; i < n; i++) {
            long weight = in.readLong();           // read the next weight
            in.readChar();                         // scan past the tab
            String query = in.readLine();          // read the next query
            terms[i] = new Term(query, weight);    // construct the term
        }

        // read in queries from standard input and print the top k matching terms
        int k = Integer.parseInt(args[1]);
        Autocomplete autocomplete = new Autocomplete(terms);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            StdOut.printf("%d matches\n", autocomplete.numberOfMatches(prefix));
            for (int i = 0; i < Math.min(k, results.length); i++)
                StdOut.println(results[i]);
        }

    }
}
