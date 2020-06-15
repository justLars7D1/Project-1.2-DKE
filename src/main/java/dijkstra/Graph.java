package dijkstra;

import java.awt.*;

public interface Graph {
	// Initialize a n-vertices graph
	void init(int n);

	// Return the number of vertices
	int nodeCount();

	// Return the number of edges
	int edgeCount();

	// Get the value of node with index v
	Object getValue(int v);

	// Set the value of node with index v
	void setValue(int v, Object val);

	// Add a new edge from node v to node w with weight wgt
	void addEdge(int v, int w, double wgt);

	// Get the weight value for an edge
	double weight(int v, int w);

	// Remove the edge from the graph
	void removeEdge(int v, int w);

	// Return an array containing the indices of the neighbors of v
	int[] neighbors(int v);

}
