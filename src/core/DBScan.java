package core;

import java.util.ArrayList;

// TODO: Write docs
public class DBScan {
    double[][] D;
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
    private static double l2(double[] source, double[] target) {
        // TODO: Check to make sure source and target has the same dimension

        // Calculate the distance
        float tmp = 0;
        for (int i = 0; i < source.length; i++) {
            tmp += Math.pow((source[i] - target[i]), 2);
        }
        return Math.sqrt(tmp);
    }

    /**
     * Main DBScan process, iterating through all points in dataset
     * @return labels for each points
     */
    public int[] Scan() {
        // c is ID if current cluster
        int c = 0;

        // Go through all points in dataset
        for (int p = 0; p < this.D.length; p++) {
            // 0: not visited yet, -1: doesn't belong to any cluster, other: belong to the specified cluster
            if (this.labels[p] == 0) {
                ArrayList<Integer> neighborPts = regionQuery(p);
                //System.out.println(neighborPts.size());
                if (neighborPts.size() < this.minPts) {
                    this.labels[p] = -1; // this point doesn't have enough neighbor to be considered a cluster
                } else {
                    c++; // change to the next cluster
                    expandCluster(c, p, neighborPts);
                }
            }
        }
        return labels;
    }

    /**
     * From current cluster, expand cluster with neighbor points
     * @param c cluster id
     * @param p a point
     * @param neighborPts neighbor points of p
     */
    private void expandCluster(int c, int p, ArrayList<Integer> neighborPts) {
        this.labels[p] = c; // mark p as visited and add p to cluster c

        for (int i = 0; i < neighborPts.size(); i++) {
            int Pn = neighborPts.get(i);

            // add P' to cluster c is it's not member of any cluster yet
            if (this.labels[Pn] == -1) {
                labels[Pn] = c; // mark P' as visited and add to cluster c
            } else if (this.labels[Pn] == 0) {
                this.labels[Pn] = c; // mark P' as visited and add to cluster c

                ArrayList<Integer> PnNeighborPts = regionQuery(Pn);

                if (PnNeighborPts.size() >= this.minPts) {
                    // TODO: clarify with dpanugroho what he is trying to do
                    neighborPts.addAll(PnNeighborPts);
                }
            }
        }
    }

    /**
     * Find all points in dataset `D` within distance `eps` of point `P`.
     *
     * @param P index of core point
     * @return
     */
    private ArrayList<Integer> regionQuery(int P) {
        ArrayList<Integer> neighbors = new ArrayList<>();

        for (int i = 0; i < this.D.length; i++) {
            //System.out.println(l2(this.D[P], this.D[i]));
            if (l2(this.D[P], this.D[i]) < this.eps) {
                neighbors.add(i);
            }
        }

        return neighbors;
    }
}