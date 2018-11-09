package core;

import java.util.ArrayList;

// TODO: Write docs
public class DBScan {
    double[][] D ;
    int[] labels;
    double eps;
    int minPts;

    public DBScan(double[][] D, double eps, int minPts) {
        // Initialize labels
        this.D = D;
        this.labels = new int[D.length];
        this.eps = eps;
        this.minPts = minPts;
    }

    // TODO: Write docs
    private static double l2(double[] source, double[] target){
        // TODO: Check to make sure source and target has the same dimension

        // Calculate the distance
        float tmp = 0;
        for (int i=0; i<source.length; i++){
            tmp+= Math.pow((source[i] - target[i]),2);
        }
        return Math.sqrt(tmp);
    }

    // TODO: Write docs
    public int[] Scan(){


        // c is ID if current cluster
        int c = 0;

        // TODO: Comments what happening here
        for (int p=0;p<this.D.length;p++){
            if(!(this.labels[p]==0)){
                continue;
            }
            ArrayList<Integer> neighborPts = regionQuery(p);
            //System.out.println(neighborPts.size());
            if (neighborPts.size() < this.minPts){
                this.labels[p] = -1;
            } else {
                c += 1;
                expandCluster(c, p, neighborPts);
            }

        }
        return labels;
    }

    // TODO: Write docs
    private void expandCluster(int c, int p, ArrayList<Integer> neighborPts){
        this.labels[p] = c;

        int i = 0;
        while (i<neighborPts.size()){
            int Pn = neighborPts.get(i);

            if(this.labels[Pn] == -1){
                labels[Pn] = c;
            } else if (this.labels[Pn] == 0){
                this.labels[Pn] = c;

                ArrayList<Integer> PnNeighborPts = regionQuery(Pn);

                if(PnNeighborPts.size() >= this.minPts){
                    neighborPts.addAll(PnNeighborPts);
                }
            }
            i += 1;
        }


    }

    /**
     * Find all points in dataset `D` within distance `eps` of point `P`.
     *
     * @param P index of core point
     * @return
     */
    private ArrayList<Integer> regionQuery(int P){
        ArrayList<Integer> neighbors = new ArrayList<>();

        for(int i=0; i<this.D.length; i++){
            //System.out.println(l2(this.D[P], this.D[i]));
            if(l2(this.D[P], this.D[i]) < this.eps){
                neighbors.add(i);
            }
        }

        return neighbors;
    }
}
