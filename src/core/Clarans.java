package core;

import util.MathUtil;

import java.util.List;
import java.util.Random;

public class Clarans {
    private double[][] dataset;
    private int maxNeighborToCheck;
    private List<List<Integer>> initialMedoid;
    private Integer[] labels;

    public Clarans(double[][] dataset, int maxNeighborToCheck, List<List<Integer>> initialMedoid) {
        this.dataset = dataset;
        this.maxNeighborToCheck = maxNeighborToCheck;
        this.initialMedoid = initialMedoid;

        this.labels = new Integer[dataset.length];
    }

//    public Integer[] assign(int maxIter){
//        int iterCount = 0;
//        int checkedNeighbor = 0;
//
//        double bestCost = MathUtil.L2Cost(this.dataset, initialMedoid);
//        List<List<Integer>> bestMedoid = initialMedoid;
//        // initially assign all point in dataset to this initial cluster
//        while (iterCount < maxIter){
//            while (checkedNeighbor < maxNeighborToCheck){
//                List<List<Integer>> currentRandomNeighbor = this.getRandomNeighbor();
//                double currentCost = MathUtil.L2Cost(this.dataset, currentRandomNeighbor);
//                if (currentCost < bestCost){
//                    bestMedoid = currentRandomNeighbor;
//                    bestCost = currentCost;
//                }
//                checkedNeighbor++;
//            }
//            iterCount++;
//        }
//
//        // Assign all data in the dataset with the best medoid
//        double minCost = Double.POSITIVE_INFINITY;
//        for(int i = 0; i < dataset.length; i++){
//            for(int j = 0; j<bestMedoid.size(); j++){
//                double currentCost = Distance.getL2Distance(dataset[i], dataset[bestMedoid[j]]);
//                if (currentCost < minCost){
//                    minCost = currentCost;
//                    this.labels[i]=bestMedoid[j];
//                }
//            }
//        }
//    }

//    private List<List<Integer>> getRandomNeighbor(List<List<Integer>> bestMedoid) {
//        List<List<Integer>> neighbor = bestMedoid;
//        Random r = new Random();
//        int indexToReplace = r.nextInt(neighbor.size());
//
//        // TODO: The substitute should not be one of the currentMedoid
//        double[] randomSubstitute = dataset[r.nextInt(dataset.length)];
//        neighbor[indexToReplace] = randomSubstitute;
//        return  neighbor;
//
//    }
}