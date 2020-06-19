package f;

import dijkstra.Graph;
import dijkstra.GraphAL;

import java.util.ArrayList;

public class FloydWarshall {

    public static double[][] distTable(Graph g) {
        int v= g.nodeCount();
        double[][] distanceTable= new double[v][v];
       // int[][] next= new int[v][v];

        for(int i=0; i<v; i++){
            for (int j=0; j<v; j++){
                if (i==j){
                    distanceTable[i][j]=0;
                }
                else{
                    distanceTable[i][j]=(int) Double.POSITIVE_INFINITY;
                }
            }
        }

        for (int u = 0; u < v; u++) {
            int[] uNeighbor = g.neighbors(u);
            for (int n : uNeighbor) {
                distanceTable[u][n] = g.weight(u, n);
               // next[u][n] = n;
            }
            distanceTable[u][u] = 0;
           // next[u][u] = u;
        }

        for (int k=0; k<v; k++){
            for(int l=0; l<v; l++){
                for(int m=0; m<v; m++){
                    if(distanceTable[l][m]> distanceTable[l][k]+distanceTable[k][m]){
                        distanceTable[l][m]=distanceTable[l][k]+distanceTable[k][m];
                    }
                }
            }
        }
    return distanceTable;
    }
}
