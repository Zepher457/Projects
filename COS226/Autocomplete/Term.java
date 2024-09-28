import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Term implements Comparable<Term> {
    private String query; // query of the term
    private long weight; // weight of the term

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        // checks for valid inputs
        if (query == null)
            throw new IllegalArgumentException("Query cannot be null");
        if (weight < 0)
            throw new IllegalArgumentException("Weight cannot be negative");
        this.query = query;
        this.weight = weight;
    }

    // Compares the two terms in descending order by weight
    public static Comparator<Term> byReverseWeightOrder() {
        return new WeightCompare();
    }

    // helper class for comparing two terms in descending order by weight
    private static class WeightCompare implements Comparator<Term> {
        public int compare(Term term1, Term term2) {
            if (term1.weight - term2.weight < 0) return 1;
            else if (term1.weight - term2.weight == 0) return 0;
            else return -1;
        }
    }

    // Compares the two terms in lexicographic order,
    // but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) {
        if (r < 0)
            throw new IllegalArgumentException("r cannot be negative");
        return new PrefixCompare(r);
    }

    // helper class for comparing two terms in lexographic order
    // using the first r characters of each query
    private static class PrefixCompare implements Comparator<Term> {
        private int r; // the length of the prefix

        // initializes prefix length
        public PrefixCompare(int r) {
            this.r = r;
        }

        public int compare(Term term1, Term term2) {
            // checks if query is less than prefix
            for (int i = 0; i < r; i++) {
                // queries have been the same up to i->they equal
                if (i >= term1.query.length() && i >= term2.query.length()) {
                    return 0;
                }
                // term1 length < term2 length-> term1 < term2
                if (i >= term1.query.length()) {
                    return -1;
                }
                // term1 length > term2 length -> term1 > term2
                if (i >= term2.query.length()) {
                    return 1;
                }
                // queries are not the same, return the difference where they differ.
                if (term1.query.charAt(i) != term2.query.charAt(i)) {
                    return (int) term1.query.charAt(i) - term2.query.charAt(i);
                }
            }
            return 0;
        }
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that) {
        return this.query.compareTo(that.query);
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        return weight + "\t" + query;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Term p = new Term("AGCCCC", 2);
        Term q = new Term("GTTA", 4);

        StdOut.println(p);
        StdOut.println(q);
        StdOut.println("byPrefixOrder(5): " + Term.byPrefixOrder(5).compare(p, q));
        StdOut.println("byReverseWeightOrder(): " 
        + Term.byReverseWeightOrder().compare(p, q));
        StdOut.println("compareTo: " + p.compareTo(q));
        
    }

}
