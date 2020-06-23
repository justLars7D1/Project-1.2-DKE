package dijkstra;

import physicsengine.Vector3d;

import java.util.*;

public class AStar {

    /**
     * Construct the shortest path
     * @param cameFrom Map
     * @param curr integer
     * @return The shortest path
     */

    private static List<Integer> reconstruct_path(Map<Integer, Integer> cameFrom, int curr) {
        List<Integer> shortestPath = new LinkedList<>();
        shortestPath.add(0, curr);
        while (cameFrom.containsKey(curr)) {
            curr = cameFrom.get(curr);
            shortestPath.add(0, curr);
        }
        return shortestPath;
    }

    /**
     * Calculate the shortest path in the graph from a start point to a goal point
     * @param g Graph
     * @param start Vertex 1 integer
     * @param goal Vertex 2 integer
     * @return The shortest path between two vertices
     */

    public static List<Integer> aStar(Graph g, int start, int goal) {
        int n = g.nodeCount();

        // The set of discovered nodes that may need to be (re-) expanded
        IndexMinPQ openSet = new IndexMinPQ(n);
        // h is the heuristic function. heuristic(k) estimates the cost to reach goal from node k.
        openSet.insert(start, heuristic(g, start, goal));

        // For node k, cameFrom.get(k) is the node immediately preceding it on the cheapest path from start
        Map<Integer, Integer> cameFrom = new HashMap<>();

        // For node k, gScore.get(k) is the cost of the cheapest path from start to k currently known
        Map<Integer, Double> gScore =  new HashMap<>();
        for (int i = 0; i < n; i++) {
            gScore.put(i, Double.POSITIVE_INFINITY);
        }
        gScore.put(start, 0.0);

        // For node k, fScore.get(k) == gScore.get(k) + heuristic(k)
        // fScore represents current best guess as how short a path from start to finish can be if it goes through k
        Map<Integer, Double> fScore =  new HashMap<>();
        fScore.put(start, heuristic(g, start, goal));

        while(!openSet.isEmpty()) {
            int curr = openSet.delMin();
            if (curr == goal)
                return reconstruct_path(cameFrom, curr);
            int[] neighbours = g.neighbors(curr);
            for (int i : neighbours) {
                // tentative_gScore is the distance from start to the neighbour through current
                double tentative_gScore = gScore.get(curr) + g.weight(curr, i);
                if (tentative_gScore < gScore.get(i)) {
                    cameFrom.put(i, curr);
                    gScore.put(i, tentative_gScore);
                    fScore.put(i, gScore.get(i) + heuristic(g, i, goal));
                    if (!openSet.contains(i))
                        openSet.insert(i, fScore.get(i));
                }
            }
        }
        // Open set is empty but goal was never reached
        return new ArrayList<>();
    }

    /**
     * Calculate the distance in the graph from a source to a destination
     * @param g Graph
     * @param i Vertex 1 integer
     * @param goal Vertex 2 integer
     * @return The distance
     */
    private static double heuristic(Graph g, int i, int goal) {
        Vector3d vv = ((Vector3d) g.getValue(i)).copy();
        Vector3d ww = ((Vector3d) g.getValue(goal)).copy();
        double dist = Math.pow(Math.pow(vv.get_x() - ww.get_x(), 2) + Math.pow(vv.get_z() - ww.get_z(), 2), 0.5);
        return dist;
    }
}
