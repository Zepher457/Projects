import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DirectedCycle;



public class ShortestCommonAncestor {
    // variable to store DAG
    private Digraph dag;
    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        // check whether G is rooted
        int roots = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) {
                roots++;
                if (roots > 1) throw new IllegalArgumentException();
            }
        }
        // check whether G is acyclic
        DirectedCycle cycle = new DirectedCycle(G);
        if (cycle.hasCycle()) throw new IllegalArgumentException();
        // if the above are not triggered, it is DAG.
        dag = new Digraph(G);

    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        // check whether v,w are within bounds
        if (v < 0 || w < 0 || v > dag.V() || w > dag.V()) 
            throw new IllegalArgumentException();
        // construct bfs objects for v and w
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(dag, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(dag, w);
        return ancestralPath(bfsV, bfsW)[0];
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        // check whether v,w are within bounds
        if (v < 0 || w < 0 || v > dag.V() || w > dag.V()) 
            throw new IllegalArgumentException();
        // construct bfs objects for v and w
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(dag, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(dag, w);
        return ancestralPath(bfsV, bfsW)[1];
        
    }
    // helper method to find common ancestors of subset of vertices
    private Queue<Integer> commonAncestors(BreadthFirstDirectedPaths bfsV,
                                            BreadthFirstDirectedPaths bfsW) {
        Queue<Integer> commonAncestors = new Queue<Integer>();
        for (int i = 0; i < dag.V(); i++) {
            // if there is a path to a vertex from both v and w
            // it is a common ancestor
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i))
                commonAncestors.enqueue(i);
        }
        return commonAncestors;
    }
    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        // check whether objects have elements in them
        if (subsetA == null || subsetB == null) 
            throw new IllegalArgumentException();
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(dag, subsetA);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(dag, subsetB);
        return ancestralPath(bfsV, bfsW)[0];
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        // check whether objects have elements in them
        if (subsetA == null || subsetB == null) 
            throw new IllegalArgumentException();
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(dag, subsetA);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(dag, subsetB);
        return ancestralPath(bfsV, bfsW)[1];
    }
    // main helper method to find the length of the ancestral path
    // also finds the shortest common ancestor between two vertices/vertex subsets.
    private int[] ancestralPath(BreadthFirstDirectedPaths bfsV, 
                                BreadthFirstDirectedPaths bfsW) {
        // maximize the value to initialize them
        int shortest = Integer.MAX_VALUE; // shortest length of ancestreal path
        int distance = Integer.MAX_VALUE; // shortest common ancestor
        Iterable<Integer> ancestors = commonAncestors(bfsV, bfsW);
        for (int ancestor: ancestors) {
            // ancestral path length
            int pathLength = bfsV.distTo(ancestor) + bfsW.distTo(ancestor);
            // the shortest ancestral path also determines the closest
            // common ancestor so change both
            if (pathLength < distance) {
                distance = pathLength;
                shortest = ancestor;
            }
        }
        // return both quantities
        return new int[] {distance, shortest};
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        // StdOut.println("starting graph construction");
        Digraph G = new Digraph(in);
        // StdOut.println("starting sca process");
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        Queue<Integer> subsetA = new Queue<Integer>();
        subsetA.enqueue(13);
        subsetA.enqueue(23);
        subsetA.enqueue(24);
        Queue<Integer> subsetB = new Queue<Integer>();
        subsetB.enqueue(6);
        subsetB.enqueue(16);
        subsetB.enqueue(17);
        StdOut.println(sca.ancestorSubset(subsetA, subsetB));
        StdOut.println(sca.lengthSubset(subsetA, subsetB));
        while (!StdIn.isEmpty()) {
            // StdOut.println("starting loop");
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
        
    }
    

}
