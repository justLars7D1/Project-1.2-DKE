package dijkstra;

import java.util.LinkedList;
import java.util.Queue;

public class FloyWarshall {

    public static int[] getShortestPath(Graph g, int source, int destination) {
        int n = g.nodeCount();
        double[][] dist = new double[n][n];
        int[][] next = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = Double.POSITIVE_INFINITY;
                next[i][j] = -1;
            }
        }
        for (int u = 0; u < n; u++) {
            int[] uNeighbor = g.neighbors(u);
            for (int v : uNeighbor) {
                dist[u][v] = g.weight(u, v);
                next[u][v] = v;
            }
            dist[u][u] = 0;
            next[u][u] = u;
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][j] > dist[i][k] + dist[k][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        int[] sourceToDestination = new int[0];
        if (next[source][destination] == -1) return sourceToDestination;
        while (source != destination) {
            source = next[source][destination];
            queue.add(source);
        }
        int i = 0;
        sourceToDestination = new int[queue.size()];
        while (!queue.isEmpty()) {
            sourceToDestination[i++] = queue.remove();
        }
        return sourceToDestination;
    }
}
