package kth.csc.inda;

import java.util.*;
import java.util.regex.*;

/**
 * A graph with a fixed number of vertices implemented using adjacency maps.
 * Space complexity is &Theta;(n + m) where n is the number of vertices and m
 * the number of edges.
 *
 * @author [Name]
 * @version [Date]
 */
public class HashGraph implements Graph {
    /**
     * The map edges[v] contains the key-value pair (w, c) if there is an edge
     * from v to w; c is the cost assigned to this edge. The maps may be null
     * and are allocated only when needed.
     */
    private final Map<Integer, Integer>[] edges;
    private final static int INITIAL_MAP_SIZE = 4;

    /**
     * Number of edges in the graph.
     */
    private int numEdges;
    private final static int EMPTY = -2; // no edge
    private final static int NO_COST = -1;


    /**
     * Constructs a HashGraph with n vertices and no edges. Time complexity:
     * O(n)
     *
     * @throws IllegalArgumentException if n < 0
     */
    public HashGraph(int n) {
        if (n < 0)
            throw new IllegalArgumentException("n = " + n);

        // The array will contain only Map<Integer, Integer> instances created
        // in addEdge(). This is sufficient to ensure type safety.
        @SuppressWarnings("unchecked")
        Map<Integer, Integer>[] a = new HashMap[n];
        edges = a;
    }

    /**
     * Add an edge without checking parameters.
     */
    private void addEdge(int from, int to, int cost) {
        if (edges[from] == null)
            edges[from] = new HashMap<Integer, Integer>(INITIAL_MAP_SIZE);
        if (edges[from].put(to, cost) == null)
            numEdges++;
    }

    /**
     * {@inheritDoc Graph} Time complexity: O(1).
     */
    @Override
    public int numVertices() {
        return edges.length;
    }

    /**
     * {@inheritDoc Graph} Time complexity: O(1).
     */
    @Override
    public int numEdges() {
        return numEdges;
    }

    /**
     * {@inheritDoc Graph}
     */
    @Override
    public int degree(int v) throws IllegalArgumentException {
        checkVertexParameter(v);
        if(edges[v] != null) {
            return edges[v].size();
        }
        return 0;
    }

    /**
     * {@inheritDoc Graph}
     * Använder mig av en iterator för att utföra neighbors. En iterator har alla metoder redan som skulle krävas här
     * därför kändes det optimalt.
     */
    @Override
    public VertexIterator neighbors(int v) {
        checkVertexParameter(v);

        return new NeighborIterator(v);
    }

    private class NeighborIterator implements VertexIterator {
        private Iterator<Integer> a;

        NeighborIterator(int v) {
            if(edges[v] == null) {
                a = null;
              }
          else {
                a = edges[v].keySet().iterator();
            }
        }

        @Override
        public boolean hasNext() {
           if (a != null) {
                return a.hasNext();
           }
             return false;
        }

        @Override
        public int next() {
            if(a != null && a.hasNext()) {
                return a.next();
            }
            throw new NoSuchElementException("No more elements!");
        }
    }
    /**
     * {@inheritDoc Graph}
     */
    @Override
    public boolean hasEdge(int v, int w) {
        checkVertexParameters(v, w);
        if((edges[v] != null) && (edges[v].containsKey(w))) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc Graph}
     */
    @Override
    public int cost(int v, int w) throws IllegalArgumentException {
        checkVertexParameters(v, w);

        if (edges[v] != null && edges[v].containsKey(w)) {
            return edges[v].get(w);
        }
        return NO_COST;
    }

    /**
     * {@inheritDoc Graph}
     */
    @Override
    public void add(int from, int to) {
        checkVertexParameters(from, to);
        addEdge(from, to, NO_COST);
    }

    /**
     * {@inheritDoc Graph}
     */
    @Override
    public void add(int from, int to, int c) {
        checkVertexParameters(from, to);
        checkNonNegativeCost(c);
        addEdge(from, to, c);
    }
    /**
     * {@inheritDoc Graph}
     */
    @Override
    public void addBi (int v, int w){
        checkVertexParameters(v, w);
        addEdge(v, w, NO_COST);
        if (v == w)
            return;
        addEdge(w, v, NO_COST);

    }

    /**
     * {@inheritDoc Graph}
     */
    @Override
    public void addBi(int v, int w, int c) {
        checkVertexParameters(v, w);
        checkNonNegativeCost(c);
        addEdge(v, w, c);
        if (v == w)
            return;
        addEdge(w, v, c);
    }

    /**
     * {@inheritDoc Graph}
     */
    @Override
    public void remove(int from, int to) {
        checkVertexParameters(from, to);
        if((edges[from] != null) && (edges[from].get(to) != null)) {
            edges[from].remove(to);
            numEdges--;
        }
    }

    /**
     * {@inheritDoc Graph}
     */
    @Override
    public void removeBi(int v, int w) {
        checkVertexParameters(v, w);
        remove(v, w);
        if (v == w)
            return;
        remove(w, v);
    }

    /**
     * Returns a string representation of this graph.
     *
     * @return a String representation of this graph
     *
     * String g0s = "{}";
    String g1s = "{(0,0)}";
    String g5s1 = "{(1,0), (2,3,1)}";
    String g5s2 = "{(2,3,1), (1,0)}";
     * {(1,2),(2,3)}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        for (int i = 0; i < numVertices(); i++) {
            VertexIterator neigbours = neighbors(i);
            while (neigbours.hasNext()) {
                int a = neigbours.next();
                if (cost(i, a) == -1) {
                    builder.append("(" + i + "," + a + "), ");
                } else {
                    builder.append("(" + i + "," + a + "," + cost(i, a) + ")  ");
                }
            }
        }
        if (numEdges > 0) {
            builder.setLength(builder.length() - 2);
        }
        builder.append("}");


        return builder.toString();
    }


    /**
     * Checks a single vertex parameter v.
     *
     * @throws IllegalArgumentException if v is out of range
     */
    private void checkVertexParameter(int v) {
        if (v < 0 || v >= numVertices())
            throw new IllegalArgumentException("Out of range: v = " + v + ".");
    }

    /**
     * Checks two vertex parameters v and w.
     *
     * @throws IllegalArgumentException if v or w is out of range
     */
    private void checkVertexParameters(int v, int w) {
        if (v < 0 || v >= numVertices() || w < 0 || w >= numVertices())
            throw new IllegalArgumentException("Out of range: v = " + v
                    + ", w = " + w + ".");
    }

    /**
     * Checks that the cost c is non-negative.
     *
     * @throws IllegalArgumentException
     *             if c < 0
     */
    private void checkNonNegativeCost(int c) {
        if (c < 0)
            throw new IllegalArgumentException("Illegal cost: c = " + c + ".");
    }

    public static void main(String[] args) {
        HashGraph hashen = new HashGraph(10);
        hashen.addEdge(1, 2, 3);
        hashen.addEdge(2, 3, 4);
        System.out.println(hashen.toString());
    }


}
