import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;

public class WordNet {
    private HashMap<Integer, String> synset; // Hashmap to store synsets
    private HashMap<String, HashSet<Integer>> synset2; // Hashmap to store nouns
    private Digraph dag; // graph to store hypernyms
    // sca object used to find noun distance and sca
    private ShortestCommonAncestor sca; 

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        synset = new HashMap<Integer, String>();
        synset2 = new HashMap<String, HashSet<Integer>>();
        if (synsets == null || hypernyms == null) // exceptions
            throw new IllegalArgumentException();
        In synFile = new In(synsets); // inputing files for analysis
        In hypFile = new In(hypernyms);
        while (synFile.hasNextLine()) {
            // getting the id and synset data
            String[] line = synFile.readLine().split(",");
            // storing synsets
            synset.put(Integer.parseInt(line[0]), line[1]);
            // now storing nouns
            String[] nouns = line[1].split(" ");
            for (String noun: nouns) {
                // some nouns have the same id, 
                // so we need to create new sets
                HashSet<Integer> set = synset2.get(noun);
                if (set == null) {
                    set = new HashSet<Integer>();
                }
                set.add(Integer.parseInt(line[0]));
                synset2.put(noun, set);
            }
        }
        // storing hypernyms
        dag = new Digraph(synset.size());
        while (hypFile.hasNextLine()) {
            // getting synset id and hypernym ids
            String[] line = hypFile.readLine().split(",");
            int synsetId = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                // setting edge link betwen synset and parent synsets
                dag.addEdge(synsetId, Integer.parseInt(line[i]));
            }
        }
        // defining sca object
        sca = new ShortestCommonAncestor(dag);

    }
 
    // the set of all WordNet nouns
    public Iterable<String> nouns() {
        return synset2.keySet();
    }
 
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return synset2.containsKey(word);
    }
  
    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        if (!isNoun(noun1) || !isNoun(noun2))
            throw new IllegalArgumentException();
        // using this to get the id of the sca to these nouns
        int v = sca.ancestorSubset(synset2.get(noun1), synset2.get(noun2));
        // returning synset associated to this id
        return synset.get(v);
        
    }
 
    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if (!isNoun(noun1) || !isNoun(noun2)) 
            throw new IllegalArgumentException();
        return sca.lengthSubset(synset2.get(noun1), synset2.get(noun2));
    }
 
    // unit testing (required)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        StdOut.println(wordnet.nouns());
        StdOut.println(wordnet.isNoun("A"));
        StdOut.println(wordnet.distance("1750s", "1900s"));
        // checking whether the IllegalArgumentException is triggered
        StdOut.println(wordnet.distance("1750s", "1910s"));
        StdOut.println(wordnet.sca("1750s", "1900s"));
        // checking whether the IllegalArgumentException is triggered
        StdOut.println(wordnet.sca("1750s", "1910s"));

        

    }
 
 }