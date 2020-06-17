package AStar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AStar {

    private final Node[][] grid;
    private final Node start;
    private final Node end;
    private final boolean checkDiagonals;
    private final List<Node> openList;
    private final List<Node> closeList;

    /**
     * @param grid The grid in which the shortest path must be determined.
     * @param start Starting node.
     * @param end Ending node.
     * @param checkDiagonals Whether to consider diagonal movements as valid.
     */
    public AStar(Node[][] grid, Node start, Node end, boolean checkDiagonals) {
        this.grid = grid;
        this.start = start;
        this.end = end;
        this.checkDiagonals = checkDiagonals;
        this.openList = new ArrayList<>();
        this.closeList = new ArrayList<>();
    }

    /**
     * @return The shortest path to go from the starting node to the ending node or "@code null" if a valid path cannot be found.
     */
    public List<Node> computePath() {
        this.openList.add(this.start);

        while (!this.closeList.contains(this.end)) {
            final Node currentNode = this.getCurrent();
            if (currentNode == null) {
                return null;
            }

            this.closeList.add(currentNode);
            this.openList.remove(currentNode);

            for (final Node node : this.getSurroundingNodes(currentNode)) {

                if (node.isObstacle() || this.closeList.contains(node)) {
                    continue;
                }

                if (this.openList.contains(node)) {
                    final int g = this.calculateG(node);
                    if (g < node.getG()) {
                        node.setP(currentNode);
                        // Distance between node and the end : sqrt((node.x-end.x)^2 + (node.y-end.y)^2))
                        node.setH(Math.sqrt(Math.pow(node.getPosX() - this.end.getPosX(), 2)
                                + Math.pow(node.getPosY() - this.end.getPosY(), 2)));
                        node.setF(node.getG() + node.getH());
                    }
                    node.setG(g);
                } else {
                    this.openList.add(node);
                    node.setP(currentNode);
                    node.setG(this.calculateG(node));
                    // Distance between node and the end : sqrt((node.x-end.x)^2 + (node.y-end.y)^2))
                    node.setH(Math.sqrt(Math.pow(node.getPosX() - this.end.getPosX(), 2)
                            + Math.pow(node.getPosY() - this.end.getPosY(), 2)));
                    node.setF(node.getG() + node.getH());
                }

                if (this.openList.isEmpty()) {
                    return null;
                }
            }
        }

        final List<Node> path = new ArrayList<>();

        // We start from the end and we go back to the beginning
        Node node = this.end;
        while (!node.equals(this.start)) {
            path.add(node);
            node = node.getP();
        }

        // The path goes from end to start, so we reverse
        Collections.reverse(path);

        return path;
    }

    /**
     * @return Node with the lowest F value.
     */
    private Node getCurrent() {
        Node current = null;
        for (final Node node : this.openList) {
            if (current == null || node.getF() < current.getF()) {
                current = node;
            }
        }
        return current;
    }

    /**
     * @param node The node that we must retrieve surrounding nodes.
     * @return A list of surrounding nodes.
     */
    private List<Node> getSurroundingNodes(Node node) {
        final List<Node> around = new ArrayList<>();

        final int x = node.getPosX();
        final int y = node.getPosY();

        if (this.isPossible(x, y + 1)) {
            around.add(this.grid[x][y + 1]);
        }
        if (this.isPossible(x, y - 1)) {
            around.add(this.grid[x][y - 1]);
        }
        if (this.isPossible(x + 1, y)) {
            around.add(this.grid[x + 1][y]);
        }
        if (this.isPossible(x - 1, y)) {
            around.add(this.grid[x - 1][y]);
        }

        if (this.checkDiagonals) {
            if (this.isPossible(x + 1, y + 1)) {
                around.add(this.grid[x + 1][y + 1]);
            }
            if (this.isPossible(x + 1, y - 1)) {
                around.add(this.grid[x + 1][y - 1]);
            }
            if (this.isPossible(x - 1, y + 1)) {
                around.add(this.grid[x - 1][y + 1]);
            }
            if (this.isPossible(x - 1, y - 1)) {
                around.add(this.grid[x - 1][y - 1]);
            }
        }

        return around;
    }

    /**
     * @return "@code true" if the coordinates are not out of bounds, "@code false" otherwise.
     */
    private boolean isPossible(int x, int y) {
        return x >= 0 && x < this.grid[0].length && y >= 0 && y < this.grid.length;
    }

    /**
     * @return The number of moves required to move from the starting node to the provided node.
     */
    private int calculateG(Node node) {
        int step = 0;
        Node tempNode = node;
        while (!tempNode.equals(this.start)) {
            step++;
            tempNode = tempNode.getP();
        }
        return step;
    }
}
