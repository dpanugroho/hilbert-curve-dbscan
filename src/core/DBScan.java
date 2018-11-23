package core;

import beans.Point;
import util.MathUtil;

import java.util.ArrayList;

// TODO: Write docs
public class DBScan {
    Point[] D;
    int[] labels;
    boolean[] visited;
    double eps;
    int minPts;

    public DBScan(Point[] D, double eps, int minPts) {
        this.D = D;
        this.labels = new int[D.length];
        this.visited = new boolean[D.length];
        this.eps = eps;
        this.minPts = minPts;
    }

    /**
     * Main DBScan process, iterating through all points in dataset
     *
     * @return labels for each points
     */
    public int[] Scan() {
        // c is ID if current cluster
        int c = 0;

        // Go through all points in dataset
        for (int p = 0; p < this.D.length; p++) {
            // 0: not visited yet, -1: doesn't belong to any cluster, other: belong to the specified cluster
            if (!this.visited[p]) {
                ArrayList<Integer> neighborPts = regionQuery(p, this.eps);
                if (neighborPts.size() < this.minPts) {
                    this.visited[p] = true;
                } else {
                    c++; // change to the next cluster
                    neighborPts.addAll(expandCluster(p, neighborPts, c, this.eps, this.minPts));
                }
            }
        }
        return labels;
    }

    /**
     * From current cluster, expand cluster with neighbor points
     *
     * @param p           a point
     * @param neighborPts neighbor points of p
     * @param c           cluster id
     * @param eps         epsion value
     * @param minPts      minimum number of neighbors for a point to be considered as a cluster
     */
    private ArrayList<Integer> expandCluster(int p, ArrayList<Integer> neighborPts, int c, double eps, double minPts) {
        this.visited[p] = true; // mark p as visited
        this.labels[p] = c; // add p to cluster c

        for (int i = 0; i < neighborPts.size(); i++) {
            int Pn = neighborPts.get(i);

            if (!this.visited[Pn]) {
                this.visited[Pn] = true; // mark P' as visited
                ArrayList<Integer> PnNeighborPts = regionQuery(Pn, eps);
                if (PnNeighborPts.size() >= minPts) {
                    neighborPts.addAll(PnNeighborPts);
                }
            }
            if (this.labels[Pn] < 1) { // if P' is not yet member of any cluster
                this.labels[Pn] = c; // add P' to cluster c
            }
        }

        return neighborPts;
    }

    /**
     * Find all points in dataset `D` within distance `eps` of point `P`.
     *
     * @param P index of core point
     * @return
     */
    private ArrayList<Integer> regionQuery(int P, double eps) {
        ArrayList<Integer> neighbors = new ArrayList<>();

        for (int i = 0; i < this.D.length; i++) {
            if (MathUtil.getL2Distance(this.D[P], this.D[i]) < eps) {
                neighbors.add(i);
            }
        }

        return neighbors;
    }
}