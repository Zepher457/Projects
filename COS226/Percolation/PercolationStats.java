import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    // constant to be used in confidence interval calculaton
    private static final double CONFIDENCE_CONST = 1.96; 
    // storing the thresholds for each trial
    private double[] threshold;
    private int trials; // number of percolation trials
    
    

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Inputs must be positive");
        }
        this.trials = trials;
        threshold = new double[trials];
        // Conducting the random trials on a grid of size n x n.
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniformInt(n);
                int col = StdRandom.uniformInt(n);
                perc.open(row, col);
            }
            /* Calculating percolation threshold for each trial 
            to use for stats methods below. */
            threshold[i] = perc.numberOfOpenSites() / (double) (n * n);
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(threshold);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - CONFIDENCE_CONST * stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + CONFIDENCE_CONST * stddev() / Math.sqrt(trials);
    }

   // test client (see below)
   public static void main(String[] args) {
    // created to measure the run time of the program.
        Stopwatch watch = new Stopwatch(); 
        // test client
        PercolationStats stats =
        new PercolationStats(Integer.parseInt(args[0]),
                            Integer.parseInt(args[1]));
        StdOut.println("mean() = " + stats.mean());
        StdOut.println("stddev() = " + stats.stddev());
        StdOut.println("confidenceLow() = " + stats.confidenceHigh());
        StdOut.println("confidenceHigh() = " + stats.confidenceLow());
        StdOut.println("elapsed time = " + watch.elapsedTime());
   }

}