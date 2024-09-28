import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

public class PointST<Value> {
    private RedBlackBST<Point2D, Value> tree;

    // construct an empty symbol table of points 
    public PointST() {
        tree = new RedBlackBST<Point2D, Value>();
    }

    // is the symbol table empty? 
    public boolean isEmpty() { return tree.isEmpty();}

    // number of points
    public int size() { return tree.size(); }

    // associate the value val with point p
    public void put(Point2D p, Value val) { 
        if (p == null || val == null) 
            throw new IllegalArgumentException();
        tree.put(p, val); 
    }
    // value associated with point p 

    public Value get(Point2D p) { 
        return tree.get(p); 
    }
    // does the symbol table contain point p? 

    public boolean contains(Point2D p) {
        return tree.contains(p); 
    }

    // all points in the symbol table 
    public Iterable<Point2D> points() { 
        return tree.keys(); 
    }

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> points = new Queue<Point2D>();
        for (Point2D p: points()) {
            if (rect.contains(p)) points.enqueue(p);
        }
        return points;
    }

    // a nearest neighbor of point p; null if the symbol table is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (tree.size() == 0) return null;
        Point2D nearestPoint = tree.select(size()/2);
        for (Point2D point: points()) {
            if (nearestPoint == null ||
            p.distanceSquaredTo(point)<p.distanceSquaredTo(nearestPoint)) {
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RectHV rect = new RectHV(0, 0.25, 0.25, 0.75);
        StdOut.println(rect.xmin());
        StdOut.println(rect.ymin());
    }

}