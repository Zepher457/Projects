
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Percolation {
    private int dimension; // dimensions of the n x n grid
    private int openSites; // the number of open sites in grid
    private int top; // virtual top index
    private int bottom; // virtual bottom index

    /* Grid that will be operated on. Using boolean as that makes life easy 
    since some class methods are booleans and seems natural. */
    private boolean[][] grid;

    /* This is the unionfind data structure with a virtual bottom index, 
    but as we see later this leads to backwash. */
    private WeightedQuickUnionUF ufWashed;  

    // Extra credit addition to eliminate backwash
    private WeightedQuickUnionUF uf; 

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        dimension = n;
        grid = new boolean[n][n]; 
        ufWashed = new WeightedQuickUnionUF(n * n + 1);
        uf = new WeightedQuickUnionUF(n * n); 
        openSites = 0;
        top = 0;
        bottom = n * n;
    }
    /* Translates the notion of grid index to notion of 
    set index so we can use UnionFind functions. */
    private int index(int row, int col) {
        StdOut.println(row);
        StdOut.println(col);
        if (row < 0 || col < 0 || row >= dimension || col >= dimension) {
            throw new IllegalArgumentException("Indices out of bounds."
            + "Must be values between 0 and " + (dimension - 1));
        }
        return dimension * row + col;

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            grid[row][col] = true;
            openSites++;
        }
        int siteIndex = index(row, col);
        if (row == 0) {
            uf.union(siteIndex, top);
            ufWashed.union(siteIndex, top);
        }
        if (row == dimension - 1) {
            ufWashed.union(siteIndex, bottom);
        }
        /* We need to union with all open sites adjacent 
        to the recently opened index */
        unionNeighbors(siteIndex, row - 1, col, true);
        unionNeighbors(siteIndex, row + 1, col, true);
        unionNeighbors(siteIndex, row, col - 1, false);
        unionNeighbors(siteIndex, row, col + 1, false);
        
    }
    /* Method to simplify the purpose of above since we're
    basically doing the same thing with a +/- 1 to row or col */
    private void unionNeighbors(int siteIndex, int neighborRow, 
                                int neighborCol, boolean rowChange) {
        if ((rowChange && neighborRow >= 0 && neighborRow <= dimension - 1) 
        || (!rowChange && neighborCol >= 0 && neighborCol <= dimension - 1)) {
            if (isOpen(neighborRow, neighborCol)) {
                uf.union(siteIndex, index(neighborRow, neighborCol));
                ufWashed.union(siteIndex, index(neighborRow, neighborCol));
            }
            
        }
    }
    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || col < 0 || row >= dimension || col >= dimension) {
            throw new IllegalArgumentException("Indices out of bounds");
        }
        return grid[row][col];
    }
    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return (isOpen(row, col) && uf.find(index(row, col)) == uf.find(top)); 
        /* Verify whether the index is indeed open and then see whether 
        that index is connected to the virtual top index. */
    }
    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites; 
    }
    // does the system percolate?
    public boolean percolates() {
        /* checking whether virtual bottom index is 
        in the same tree as virtual top node. */
        return (ufWashed.find(bottom) == ufWashed.find(top)); 
    }
    // unit testing (required)
    public static void main(String[] args) {
        // Percolation perc = new Percolation(50);
        // while (!perc.percolates()) {
        //     // generating random indices to open until the system percolates
        //     int row = StdRandom.uniformInt(50);
        //     int col = StdRandom.uniformInt(50);
        //     perc.open(row, col);
        //     // testing whether open actually opens the index
        //     StdOut.println(perc.isOpen(row, col));
        //     // checking whether our newest open index is full
        //     StdOut.println(perc.isFull(row, col));
        // }
        // StdOut.println(perc.percolates());
        // // prints out the number of open sites when the system percolates.
        // StdOut.println(perc.numberOfOpenSites()); 
        int n = 2; // grid dimensions of 2x2 for testing
        Percolation perc = new Percolation(n); // shortened for convenience
 
        // testing Percolation() and open()
        
        perc.open(1, 1);
        // testing isFull()
        System.out.println("This box is full: " + perc.isFull(1, 1));
        // testing isOpen(), should return false
        System.out.println("The below box is open: "
                                   + perc.isOpen(1, 1));
        perc.open(2, 1);
        // testing isOpen() on the same box after open(), should return true
        System.out.println("This box is now open: "
                                   + perc.isOpen(2, 1));
 
        // testing map() to see if it correct converts to 1D, should return 6
        int mapResult = perc.index(2, 2);
        System.out.println(mapResult);
 
        // testing percolates(), should return true
        System.out.println("Percolates: " + perc.percolates());
        // testing numberOfOpenSites(), should return 2
        System.out.println("Number of open boxes: "
                                   + perc.numberOfOpenSites());
    }
}
