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

        for (Cluster cluster : clusters) {
            borderClusters.add(getBorderCluster(cluster));
        }

        return borderClusters;
    }

    private int countInterconnection(Cluster cluster) {
        int interconnection = 0;
        for (int i = 0; i < cluster.getMembers().size(); i++) {
            for (int j = i + 1; j < cluster.getMembers().size(); j++) {
                Point pointA = cluster.getMembers().get(i);
                Point pointB = cluster.getMembers().get(j);
                if (MathUtil.getL2Distance(pointA, pointB) < this.eps) {
                    interconnection++;
                }
            }
        }

        return interconnection;
    }

    private int countInterconnection(Cluster clusterA, Cluster clusterB) {
        int interconnection = 0;
        for (Point pointA : clusterA.getMembers()) {
            for (Point pointB : clusterB.getMembers()) {
                if (MathUtil.getL2Distance(pointA, pointB) < this.eps) {
                    interconnection++;
                }
            }
        }

        return interconnection;
    }

    private boolean mergeability(Cluster borderClusterA, Cluster borderClusterB) {
        int count = countInterconnection(borderClusterA, borderClusterB);
        int countA = countInterconnection(borderClusterA);
        int countB = countInterconnection(borderClusterB);

        return ((double) count) / (((double) countA + (double) countB) / 2) >= this.mergeThreshold;
    }

    /**
     * Merge list of clusters into one, doesn't care if it can be merged or not
     * @param clusters
     * @return merged clusters
     */
    private List<Cluster> merge(List<Cluster> clusters) {
        // TODO: Check if list of clusters is 0 or not
        Cluster firstCluster = clusters.get(0);
        for (Cluster cluster: clusters) {
            cluster.setLabel(firstCluster.getLabel()); // Merging means setting all labels to the same label, e.g: firstCluster's label
            for (Point p: cluster.getMembers()) {
                p.setLabel(firstCluster.getLabel()); // Merging means setting all points in this cluster to the same label. e.g: firstCluster's label
            }
        }

        return clusters; // Return modified list of clusters back
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

    	boolean[][] mergeabilityMatrix = new boolean[borderClusters.size()][borderClusters.size()];
        for (int i = 0; i < borderClusters.size() - 1; i++) {
            for (int j = i + 1; j < borderClusters.size(); j++) {
                mergeabilityMatrix[i][j] = mergeability(borderClusters.get(i), borderClusters.get(j));
            }
        }

        return clustersAfterMerge;
    }

}