import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
// import edu.princeton.cs.algs4.RedBlackBST;

public class KdTreeST<Value> {
    private Node root;
    private int size;
    private class Node {   
        private Point2D p;
        private Value val;
        private Node lb, rt;
        private RectHV rect;
        private boolean vertical;
    }
    public KdTreeST() {
        root = null;
        size = 0;
    }

    // is the symbol table empty? 
    public boolean isEmpty() { return size == 0;}

    // number of points
    public int size() { return this.size; }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null) throw new IllegalArgumentException();
        root = put(root, p, val, true, 
        Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 
        Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }
    private Node put(Node x, Point2D p, Value val, boolean bool, 
    double xmin, double ymin, double xmax, double ymax) {
        if (x == null) {
            Node node = new Node();
            node.val = val;
            node.p = p;
            node.vertical = bool;
            node.rect = new RectHV(xmin, ymin, xmax, ymax);
            size++;
            return node;
        }
        if (x.vertical) {
            if (p.x() < x.p.x()) 
                x.lb = put(x.lb, p, val, !x.vertical,
                x.rect.xmin(), x.rect.ymin(), x.p.x(), x.rect.ymax()); 
            else if (p.x()>x.p.x()) 
                x.rt = put(x.rt, p, val, !x.vertical, 
                x.p.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax()); 
            else if (p.y() != x.p.y())
                x.rt = put(x.rt, p, val, !x.vertical,
                x.p.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
            else x.val = val;
        }
        else {
            if (p.y() < x.p.y()) 
                x.lb = put(x.lb, p, val, !x.vertical,
                x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.p.y()); 
            else if (p.y()>x.p.y()) 
                x.rt = put(x.rt, p, val, !x.vertical,
                x.rect.xmin(), x.p.y(), x.rect.xmax(), x.rect.ymax()); 
            else if (p.x() != x.p.x())
                x.rt = put(x.rt, p, val, !x.vertical,
                x.rect.xmin(), x.p.y(), x.rect.xmax(), x.rect.ymax());
            else x.val = val;
        }
        return x;

    }

    // value associated with point p 
    public Value get(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return get(root, p, true);
    }
 
    private Value get(Node current, Point2D p, boolean vertical) {
        if (current == null) {
            return null;
        }
 
        if (vertical) {
 
            if (p.x() < current.p.x()) {
                return get(current.lb, p, !vertical);
            }
            else if (p.x() > current.p.x()) {
                return get(current.rt, p, !vertical);
            }
            else if (p.y() != current.p.y()) {
                return get(current.rt, p, !vertical);
            }
            else return current.val;
        }
        else {
 
            if (p.y() < current.p.y()) {
                return get(current.lb, p, !vertical);
            }
            else if (p.y() > current.p.y()) {
                return get(current.rt, p, !vertical);
            }
            else if (p.x() != current.p.x()) {
                return get(current.rt, p, !vertical);
            }
            else return current.val;
        }
    }

    // does the symbol table contain point p? 
    public boolean contains(Point2D p) {
        return (get(p) != null);
    }

    // all points in the symbol table 
    public Iterable<Point2D> points() {
        Queue<Point2D> points = new Queue<Point2D>();
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            points.enqueue(x.p);
            queue.enqueue(x.lb);
            queue.enqueue(x.rt);
        }
        return points;
    }
 
    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> points = new Queue<Point2D>();
        range(root, rect, points);
        return points;
    }
    private void range(Node x, RectHV rect, Queue<Point2D> points) {
        if (x == null) return;
        if (!x.rect.intersects(rect)) return;
        if (rect.contains(x.p)) points.enqueue(x.p);
        range(x.lb, rect, points);
        range(x.rt, rect, points);
    }
    // a nearest neighbor of point p; null if the symbol table is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        return nearest(root, p, root.p);
    }
    private Point2D nearest(Node x, Point2D p, Point2D champ) {
        if (x == null) return champ;
        if (champ == null) champ = x.p;
        if (champ.distanceSquaredTo(p) < x.rect.distanceSquaredTo(p)) return champ;
        if (champ.distanceSquaredTo(p) > x.p.distanceSquaredTo(p)) champ = x.p;
        if ((x.vertical && x.p.x() > p.x()) || (!x.vertical && x.p.y() > p.y())) {
            champ = nearest(x.lb, p, champ);
            champ = nearest(x.rt, p, champ);
        }
        else {
            champ = nearest(x.rt, p, champ);
            champ = nearest(x.lb, p, champ);
        }
        return champ;
            

    }

    // unit testing (required)
    public static void main(String[] args) {
        KdTreeST<Integer> tree = new KdTreeST<>();
        StdOut.println(tree.isEmpty());
        Point2D p1 = new Point2D(0, 0);
        Point2D p2 = new Point2D(1, 0);
        Point2D p3 = new Point2D(0, 1);
        Point2D p4 = new Point2D(1, 1);
        // Point2D p5 = new Point2D(0, 0);

        tree.put(p1, 1);
        tree.put(p2, 2);
        tree.put(p3, 3);
        tree.put(p4, 4);
        tree.put(p1, 5);
        StdOut.println(tree.get(p1));
        StdOut.println(tree.get(p3));
        tree.put(p3, 10);
        StdOut.println(tree.get(p3));
        StdOut.println(tree.size());
        RectHV rect = new RectHV(0, 0, 1, 0.5);
        StdOut.println(tree.range(rect));
        Point2D queryP = new Point2D(0.25, 0.5);
        StdOut.println(tree.nearest(queryP));



    }


}
