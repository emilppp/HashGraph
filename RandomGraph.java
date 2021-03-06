package kth.csc.inda;

import java.util.Random;
import java.util.Stack;
/**
 * Created by Emil on 2015-04-22.
 */
public class RandomGraph {
    private static int n;
    private static Random rand;
    private static boolean[] marked;
    private static int count;
    private static int biggestComp;
    private static Graph graph;

    public RandomGraph(int n, Graph graph) {
        this.n = n;
        this.graph = graph;
        rand = new Random();
        randomHashGraph();
    }

    /**
     * Generera en HashGraph med randomv�rden
     */
    private static void randomHashGraph() {
        while(graph.numEdges() < n) {
            int from = rand.nextInt(n);
            int to = rand.nextInt(n);
            graph.addBi(from, to);
        }
    }

    /**
     * Hj�lpmetod f�r DFS. K�r dfs fr�n 0 till n, grafens 'l�ngd'
     * @param g
     */
    private static void DFSgo(Graph g) {
        marked = new boolean[n];

        for(int i = 0; i < n; i++) {
                if(!marked[i]) {
                    dfs(g, i, marked, 0);
                }
        }

    }

    /**
     *
     * @param g grafen som ska behandlas.
     * @param v vilket v�rde som ska startas vid
     * @param size storleken p� komponenten
     *
     * dfs g�r rekursiva kall f�r att g� igenom grafen. Kollar �ven vilken av komponenterna som �r st�rst i grafen
     * Samt hur m�nga komponenter grafen inneh�ller.
     *
     **/
    private static void dfs(Graph g, int v, boolean[] marked, int size) {
        int sizeOfComp = size;
        marked[v] = true;
        sizeOfComp++;

        if(biggestComp < sizeOfComp) {
            biggestComp = sizeOfComp;
        }

        VertexIterator a = g.neighbors(v);
        while(a.hasNext()) {
            int i = a.next();
            if (!marked[i]) {
                dfs(g, i, marked, sizeOfComp);
                count++;
            }
        }
    }

    /**
     * Testar och printar ut tid och komponentinfo.
     * @param args
     */
    public static void main(String args[]) {
        HashGraph hash = new HashGraph(10);
        MatrixGraph matr = new MatrixGraph(10);
        RandomGraph rand = new RandomGraph(10, hash);
        RandomGraph rand2 = new RandomGraph(10, matr);

        long startTime = System.nanoTime();
        DFSgo(hash);
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("HASH");
        System.out.println("Number of components = " + rand.count);
        System.out.println("Size of the biggest component = " + rand.biggestComp);
        System.out.println("TIME =" + estimatedTime);

        count = 0;
        biggestComp = 0;

        long startTime2 = System.nanoTime();
        DFSgo(matr);
        long estimatedTime2 = System.nanoTime() - startTime2;
        System.out.println("MATRIX");
        System.out.println("Number of components = "+ rand2.count);
        System.out.println("Size of the biggest component = " + rand2.biggestComp);
        System.out.println("TIME =" + estimatedTime2);

        System.out.println(hash.toString());

    }
}

