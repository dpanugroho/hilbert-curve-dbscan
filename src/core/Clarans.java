package core;

import java.util.List;
import java.util.Random;

public class Clarans {
    private double[][] dataset;
    private int maxNeighborToCheck;
    private List<Integer> initialMedoid; //List of index of the initial medoid

    public Clarans(double[][] dataset, int maxNeighborToCheck, List<Integer> initialMedoid) {
        this.dataset = dataset;
        this.maxNeighborToCheck = maxNeighborToCheck;
        this.initialMedoid = initialMedoid;
    }

    private double getCost(List<Integer> medoidIndexes) {

        // Assign each point to its nearest medoid
        // Calculate the cost along the way

        double totalCost = 0;

        for (double[] row : this.dataset) {
            double minCost = Double.POSITIVE_INFINITY;
            for (Integer medoidIndex : medoidIndexes) {
                double[] currentMedoidPoint = this.dataset[medoidIndex];
                double currentCost = Distance.getL2Distance(row, currentMedoidPoint);

                if (currentCost < minCost) {
                    minCost = currentCost;
                }
            }
            totalCost += minCost;
        }
        return totalCost;
    }

    public Integer[] assign(int maxIter) {
        int iterCount = 0;
        int checkedNeighbor = 0;
        Integer[] labels = new Integer[this.dataset.length];

        // Initiate bestMedoid as initial medoid, and bestCost as cost of thi initial medoid
        List<Integer> bestMedoid = this.initialMedoid;
        double bestCost = this.getCost(bestMedoid);

        while (iterCount < maxIter) {
            while (checkedNeighbor < maxNeighborToCheck) {
                List<Integer> currentRandomNeighbor = this.getRandomNeighbor(bestMedoid);
                double currentCost = this.getCost(currentRandomNeighbor);
                if (currentCost < bestCost) {
                    bestMedoid = currentRandomNeighbor;
                    bestCost = currentCost;
                }
                checkedNeighbor++;
            }
            iterCount++;
        }

        for (int i = 0; i < dataset.length; i++) {
            double minCost = Double.POSITIVE_INFINITY;
            for (int j = 0; j < bestMedoid.size(); j++) {
                double[] currentMedoidPOint = this.dataset[bestMedoid.get(j)];
                double currentCost = Distance.getL2Distance(dataset[i], currentMedoidPOint);

                if (currentCost < minCost) {
                    minCost = currentCost;
                    labels[i] = bestMedoid.get(j);
                }
            }
        }
        return labels;
    }

    private List<Integer> getRandomNeighbor(List<Integer> bestMedoid) {
        List<Integer> neighbor = bestMedoid;
        Random r = new Random();
        int indexToReplace = r.nextInt(neighbor.size());

        // Prevent substitution is exist in current medoid list
        boolean isSubstituteExist = true;
        Integer randomSubstitute = 0;
        while (isSubstituteExist) {
            randomSubstitute = r.nextInt(dataset.length);
            if (!neighbor.contains(randomSubstitute)) {
                isSubstituteExist = false;
            }
        }
        neighbor.set(indexToReplace, randomSubstitute);
        return neighbor;

    }
}