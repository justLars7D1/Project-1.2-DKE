package dijkstra;

import java.util.Comparator;

public class NodeComparator implements Comparator<double[]> {

	@Override
	public int compare(double[] i, double[] j) {
		if (i[1] > j[1])
			return 1;
		else if (i[1] < j[1])
			return -1;
		return 0;
	}
	
}
