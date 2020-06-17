package f;

import dijkstra.Graph;
import dijkstra.GraphAL;

import java.util.ArrayList;

public class FloydWarshall {

    public static int[][] distTable(Graph g) {
        int v= g.nodeCount();
        int[][] distanceTable= new int[v][v];

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

        /*
        TO FILL WITH THE EDGES
         */
        
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
