package core;

import beans.Cluster;
import beans.Point;
import util.MathUtil;

import java.util.*;

public class ClusterMerger {
    List<Cluster> clustersToMerge;
    double mergingThreshold;
    private double eps;

    public ClusterMerger(List<Cluster> clustersToMerge, double mergingThreshold, double eps) {
        this.clustersToMerge = clustersToMerge;
        this.mergingThreshold = mergingThreshold;
        this.eps = eps;
    }

    /*
       Given List of cluster, the first function will generate MergabilityMap as n intermediate result storing
       information in a map datatype with a Cluster as key and List of other Cluster as value
     */

    private int getInterconnectivityCount(Cluster cluster) {
        int interconnectivityCount = 0;
        for (int i = 0; i < cluster.getMembers().size(); i++) {
            for (int j = i + 1; j < cluster.getMembers().size(); j++) {
                Point pointA = cluster.getMembers().get(i);
                Point pointB = cluster.getMembers().get(j);
                double currentDistance = MathUtil.getL2Distance(pointA, pointB);
                if (currentDistance < this.eps) {
                    interconnectivityCount++;
                }
            }
        }

        return interconnectivityCount;
    }

    private int getInterconnectivityCount(Cluster clusterA, Cluster clusterB) {
        int interconnectivityCount = 0;
        for (Point pointA : clusterA.getMembers()) {
            for (Point pointB : clusterB.getMembers()) {
                double currentDistance = MathUtil.getL2Distance(pointA, pointB);
                if (currentDistance < this.eps) {
                    interconnectivityCount++;
                }
            }
        }

        return interconnectivityCount;
    }

    // TODO: Test
    private List<List<Cluster>> getMergableClusters() {

        // Initialize map to store the result
        Map<Cluster, List<Cluster>> mergabilityMap = new HashMap<>();

        // Loop for each cluster in mergabilityInput
        for (int i = 0; i < this.clustersToMerge.size(); i++) {
            Cluster clusterA = this.clustersToMerge.get(i);
            // get number of edges in A
            int nA = this.getInterconnectivityCount(clusterA);
            for (int j = i + 1; j < this.clustersToMerge.size(); j++) {
                Cluster clusterB = this.clustersToMerge.get(j);
                // get number of edges in B
                double nB = (double) this.getInterconnectivityCount(clusterB);
                // get number of edges in AB
                double nAB = (double) this.getInterconnectivityCount(clusterA, clusterB);

                double relativeInterconnectivity = nAB / (0.5 * (nA + nB));
                // TODO: Should split with "-" and take first element to compare whether they are come from equal cluster
                if (relativeInterconnectivity > this.mergingThreshold && !clusterA.getLabel().split("_")[0].equals(clusterB.getLabel().split("_")[0])) {
                    List<Cluster> tmp = new ArrayList<>();
                    // Check if we already have clusterA as a key by looking it up in the map
                    if (mergabilityMap.get(clusterA) != null) {
                        tmp.addAll(mergabilityMap.get(clusterA));
                    }
                    if (!tmp.contains(clusterB)) {
                        tmp.add(clusterB);
                    }
                    mergabilityMap.put(clusterA, tmp);
                }
            }
        }

        // Transform mergability map to mergability group
        List<List<Cluster>> mergabilityGroup = new ArrayList<>();
        // Loop over key
        for (Cluster key : mergabilityMap.keySet()) {
            List<Cluster> currentGroup = new ArrayList<>();
            currentGroup.addAll(mergabilityMap.get(key));
            for (Cluster val : mergabilityMap.get(key)) {
                List<Cluster> itemAtVal = mergabilityMap.get(val);
                if (itemAtVal != null) {
                    currentGroup.addAll(mergabilityMap.get(val));
                }
            }

            // remove duplicate
            Set<Cluster> tmpClusterSet = new HashSet<>();
            tmpClusterSet.addAll(currentGroup);
            currentGroup.clear();
            currentGroup.addAll(tmpClusterSet);
            Collections.sort(currentGroup, Comparator.comparing(Cluster::getLabel));
            boolean isMegabilityGroupContainCurrentGroup = mergabilityGroup.contains(currentGroup);
            if(!isMegabilityGroupContainCurrentGroup){
                mergabilityGroup.add(currentGroup);
            }
        }
        return mergabilityGroup;
    }

    // TODO: Test
    private Cluster mergeClusterGroup(List<Cluster> clusterGroup){
        Cluster firstCluster = clusterGroup.get(0);
        Cluster mergedCluster = new Cluster();
        for (Cluster cluster: clusterGroup) {
            for (Point p: cluster.getMembers()) {
                p.setLabel(firstCluster.getLabel()); // Merging means setting all points in this cluster to the same label. e.g: firstCluster's label
                mergedCluster.add(p);
            }
        }
        mergedCluster.setLabel(firstCluster.getLabel()); // Merging means setting all labels to the same label, e.g: firstCluster's label
        return mergedCluster;
    }

    // TODO: Test
    public List<Cluster> mergeAll(){
        List<Cluster> finalClusters = new ArrayList<>();

        List<List<Cluster>> clusterGroup = getMergableClusters();
        for (List<Cluster> group: clusterGroup){
            finalClusters.add(mergeClusterGroup(group));
        }

        return finalClusters;
    }
}
