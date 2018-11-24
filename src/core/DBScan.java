package core;

import beans.Point;
import util.MathUtil;

import java.util.ArrayList;

// TODO: Write docs
public class DBScan {
    Point[] D;
    private double eps;
    private int minPts;

    public DBScan(Point[] D, double eps, int minPts) {
        this.D = D;
        this.eps = eps;
        this.minPts = minPts;
    }

    /**
     * Main DBScan process, iterating through all points in dataset
     *
     * @return labels for each points
     */
    public Point[] Scan() {
        // c is ID if current cluster
        int c = 0;

        // Go through all points in dataset
        for (Point p: this.D) {
            if (p.isVisited()) {
                ArrayList<Point> neighborPts = regionQuery(p, this.eps);
                if (neighborPts.size() < this.minPts) {
                    p.setVisited(true);
                } else {
                    c++; // change to the next cluster
                    // a point is a core point if it has more than a specified number of points (MinPts) within Eps
                    p.setCore(true);
                    neighborPts.addAll(expandCluster(p, neighborPts, c, this.eps, this.minPts));
                }
            }
        }
        return this.D;
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
    private ArrayList<Point> expandCluster(Point p, ArrayList<Point> neighborPts, int c, double eps, double minPts) {
        p.setVisited(true); // mark p as visited
        p.setLabel(c); // add p to cluster c

        for (int i = 0; i < neighborPts.size(); i++) {
            Point Pn = neighborPts.get(i);

            if (!Pn.isVisited()) {
                Pn.setVisited(true); // mark P' as visited
                ArrayList<Point> PnNeighborPts = regionQuery(Pn, eps);
                if (PnNeighborPts.size() >= minPts) {
                    neighborPts.addAll(PnNeighborPts);
                }
            }
            if (Pn.getLabel() < 1) { // if P' is not yet member of any cluster
                Pn.setLabel(c); // add P' to cluster c
            }
        }

        return neighborPts;
    }

    /**
     * Find all points in dataset `D` within distance `eps` from point `P`.
     *
     * @param p core point
     * @return neighborPoints that is within distance `eps` from point `P`.
     */
    private ArrayList<Point> regionQuery(Point p, double eps) {
        ArrayList<Point> neighbors = new ArrayList<>();

        for (Point neighborCandidate: this.D) {
            if (MathUtil.getL2Distance(p, neighborCandidate) < eps) {
                neighbors.add(neighborCandidate);
            }
        }

        return neighbors;
    }
}