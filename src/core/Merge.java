package core;

import util.MathUtil;

import java.util.ArrayList;
import java.util.List;

import beans.Cluster;
import beans.Point;

public class Merge {
    List<Cluster> clusters;
    double eps;
    double mergeThreshold;

    public Merge(List<Cluster> clusters, double eps, double mergeThreshold) {
        this.clusters = clusters;
        this.eps = eps;
        this.mergeThreshold = mergeThreshold;
    }

    private Cluster getBorderCluster(Cluster cluster) {
        Cluster onlyBorder = new Cluster();

        for (Point p : cluster.getMembers()) {
            if (p.isBorder()) {
                onlyBorder.add(p);
            }
        }

        return onlyBorder;
    }

    private List<Cluster> getBorderClusterList(List<Cluster> clusters) {
        List<Cluster> borderClusters = new ArrayList<>();

        for (Cluster cluster: clusters) {
            borderClusters.add(getBorderCluster(cluster));
        }

        return borderClusters;
    }

    private int countInterconnection(Cluster clusterA, Cluster clusterB) {
        int interconnection = 0;
        for (Point pointA: clusterA.getMembers()) {
            for (Point pointB: clusterB.getMembers()) {
                if (MathUtil.getL2Distance(pointA, pointB) < this.eps) {
                    interconnection++;
                }
            }
        }

        return interconnection;
    }

    private boolean mergeability(Cluster borderClusterA, Cluster borderClusterB) {
        int count = countInterconnection(borderClusterA, borderClusterB);
        double sizeA = (double) borderClusterA.getMembers().size();
        double sizeB = (double) borderClusterB.getMembers().size();

        return ((double) count) / ((sizeA + sizeB) / 2) >= this.mergeThreshold;
    }

    private Cluster merge(Cluster clusterA, Cluster clusterB) {
        for (Point p : clusterB.getMembers()) {
            clusterA.add(p);
            // TODO: Set points' label to cluster A's label
        }

        return clusterA;
    }

    public List<Cluster> mergeAll() {
        List<Cluster> clustersAfterMerge = new ArrayList<>();
        List<Cluster> borderClusters = getBorderClusterList(clusters);
    	
    	/* Assume that we have A,B,C,D => 4 clusters
    	   A -> B, C, D
    	   B -> C, D (because B->A is same as A->B(already done))
    	   C -> D
    	   D -> no need to do
    	 * */
        for (int i = 0; i < borderClusters.size() - 1; i++) {
            for (int j = i + 1; j < borderClusters.size(); j++) {
                boolean mergeability = mergeability(borderClusters.get(i), borderClusters.get(j));
            }
        }

        return clustersAfterMerge;
    }

}