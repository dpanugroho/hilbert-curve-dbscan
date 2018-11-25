package core;

import util.MathUtil;

import java.util.ArrayList;
import java.util.List;

import beans.Cluster;
import beans.Point;

// TODO: Write docs
public class Merge {
    double eps;
    double merge_threshold;
    List<Cluster> clusterList = new ArrayList<>();

    public Merge(double eps, double merge_threshold, List<Cluster> clusterList) {
        this.eps = eps;
        this.merge_threshold = merge_threshold;
        this.clusterList = clusterList;
    }

    private Cluster getBoarderCluster(Cluster cluster) {
    	Cluster onlyBorder = new Cluster();
    	for(Point p: cluster.getMembers()) {
    		if(p.isBorder()) {
    			onlyBorder.add(p);
    		}
    	} 	
    	return onlyBorder;
    }
    
    private List<Cluster> getBoarderClusterList(List<Cluster> clusterList){
    	List<Cluster> boarderClusterList = new ArrayList<>();  	
    	for (int i = 0; i < clusterList.size(); i++) {
    		boarderClusterList.add(i, getBoarderCluster(clusterList.get(i)));
    	}	
    	return boarderClusterList;
    }
    
    private double findNumOfInterCon(Cluster boarderClusterA, Cluster boarderClusterB){
    	double numOfInterConnect = 0;   	
    	for(int i=0 ; i < boarderClusterA.getMembers().size() ; i++) {
    		for(int j=0 ; j < boarderClusterB.getMembers().size() ; j++) {
    			if (MathUtil.getL2Distance(boarderClusterA.getMembers().get(i), boarderClusterB.getMembers().get(j)) < eps)
    				numOfInterConnect++;
    		}
    	}
    	return numOfInterConnect;
    }
    
    private boolean mergeEvaluate(Cluster boarderClusterA, Cluster boarderClusterB){
    	double numOfInterConnect = findNumOfInterCon(boarderClusterA,boarderClusterB);
    	double sizeOfBoarderClusterA = (double) boarderClusterA.getMembers().size();
    	double sizeOfBoarderClusterB = (double) boarderClusterB.getMembers().size();;
    	
    	if ( (numOfInterConnect / ((sizeOfBoarderClusterA + sizeOfBoarderClusterB)/2)) >= merge_threshold ) {   		 
   		  return true;
   	 	}
    	return false;
    }
    
    private Cluster mergeCluster(Cluster clusterA, Cluster clusterB) {
    	for(Point p: clusterB.getMembers()) {
    		clusterA.add(p);
    	} 	
    	return clusterA; 
    }
    
    private List<Cluster> mainProcess(){
    	List<Cluster> clusterListAfterMerge = new ArrayList<>();
    	List<Cluster> boarderCluster = new ArrayList<>();
    	boarderCluster = getBoarderClusterList(clusterList);
    	
    	/* Assume that we have A,B,C,D => 4 clusters
    	   A -> B, C, D
    	   B -> C, D (because B->A is same as A->B(already done))
    	   C -> D
    	   D -> no need to do
    	 * */
    	for(int i=0 ; i < boarderCluster.size()-1 ; i++) {
    		for(int j=i+1 ; j < boarderCluster.size() ; j++) {
    			mergeEvaluate(boarderCluster.get(i), boarderCluster.get(j));
    		}   		
    	}
    	
    	return clusterListAfterMerge;
    }
    
}