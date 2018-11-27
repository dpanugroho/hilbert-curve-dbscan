package core;

import util.MathUtil;

import java.util.*;

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

    private Cluster merge(Cluster clusterA, Cluster clusterB) {
        for (Point p : clusterB.getMembers()) {
            clusterA.add(p);
            // TODO: Set points' label to cluster A's label
        }

        return clusterA;
    }

    public void mergeAll() {
//        List<Cluster> clustersAfterMerge = new ArrayList<>();
//        List<Cluster> borderClusters = getBorderClusterList(this.clusters);
    	
    	/* Assume that we have A,B,C,D => 4 clusters
    	   A -> B, C, D
    	   B -> C, D (because B->A is same as A->B(already done))
    	   C -> D
    	   D -> no need to do
    	 * */

    	List<Cluster> borderClusters = new ArrayList<>();

    	Cluster cluster1 = new Cluster();
    	double[] coordinate = new double[] {1, 1};
    	Point point = new Point(coordinate);
    	cluster1.add(point);
    	cluster1.setLabel(1);
    	borderClusters.add(cluster1);

        Cluster cluster2 = new Cluster();
        coordinate = new double[] {2, 2};
        point = new Point(coordinate);
        cluster2.add(point);
        cluster2.setLabel(2);
        borderClusters.add(cluster2);

        Cluster cluster3 = new Cluster();
        coordinate = new double[] {3, 3};
        point = new Point(coordinate);
        cluster3.add(point);
        cluster3.setLabel(3);
        borderClusters.add(cluster3);

//        Cluster cluster4 = new Cluster();
//        coordinate = new double[] {100, 100};
//        point = new Point(coordinate);
//        cluster4.add(point);
//        cluster4.setLabel(4);
//        borderClusters.add(cluster4);
//
//        Cluster cluster5 = new Cluster();
//        coordinate = new double[] {101, 101};
//        point = new Point(coordinate);
//        cluster5.add(point);
//        cluster5.setLabel(5);
//        borderClusters.add(cluster5);
//
//        Cluster cluster6 = new Cluster();
//        coordinate = new double[] {500, 500};
//        point = new Point(coordinate);
//        cluster6.add(point);
//        cluster6.setLabel(6);
//        borderClusters.add(cluster6);

        Map<Integer, List<Cluster>> map = new HashMap<>();
        for (Cluster clusterA: borderClusters) {
            for (Cluster clusterB: borderClusters) {
                List<Cluster> clusterList = new ArrayList<>();

                if (map.containsKey(clusterA.getLabel())) {
                    clusterList = map.get(clusterB.getLabel());
                }

                if (clusterList == null) {
                    clusterList = new ArrayList<>();
                }

                if (!clusterList.contains(clusterA.getLabel())) {
                    clusterList.add(clusterB);
                    Collections.sort(clusterList);
                }

                map.put(clusterA.getLabel(), clusterList);
            }
        }

        List<List<Cluster>> clustersAfterMerge = new ArrayList<>();

        for (Integer key : map.keySet()) {
            List<Cluster> clusters = map.get(key);
            if (!clustersAfterMerge.contains(clusters)) {
                clustersAfterMerge.add(clusters);
            }
        }

        System.out.println(clustersAfterMerge);

//        return cl;
    }

}