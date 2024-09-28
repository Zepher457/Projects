import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    // wordnet variable
    private WordNet net;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        net = wordnet;
    }
 
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        // initialize as smallest possible value
        int greatestDist = Integer.MIN_VALUE;
        // initialize outcast string
        String outcast = "";
        // traverse all nouns in given array
        for (String noun: nouns) {
            // sum of distances variable
            int dist = 0;
            // find the distance between a particular noun 
            // and all nouns
            for (int i = 0; i < nouns.length; i++) {
                dist += net.distance(noun, nouns[i]);
            }
            // change distance and outcast if necessary
            if (dist > greatestDist) {
                greatestDist = dist;
                outcast = noun;
            }
            
        }
        return outcast;
    }
 
    // test client (see below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
 
 }