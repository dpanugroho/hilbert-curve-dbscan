package core;

import beans.Point;
import util.MathUtil;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation of Partition Around Medoid (PAM)
 * Maybe we can add variable to store usedMedoid to avoid infinite loop
 */

public class Pam {
    private Point[] dataset;
    private int k;

    public Pam(Point[] dataset, int k) {
        this.dataset = dataset;
        this.k = k;

    }

    public double getCost(Integer[] labels) {
        // for each cluster label in [0..k]
        double totalCost = 0;
        for (int i = 0; i < labels.length; i++) {
            totalCost += MathUtil.getL2Distance(dataset[i], dataset[labels[i]]);
        }
        return totalCost;
    }

    private Integer[] assign() {
        Integer[] labels = new Integer[dataset.length];

        // Initialize random index to pick initial random medoid from dataset
        int[] randomMedoidIndex = new int[this.k];
        for (int i = 0; i < k; i++) {
            randomMedoidIndex[i] = ThreadLocalRandom.current().nextInt(0, dataset.length);
        }

        // Assign non meodid to it's closest medoid
        for (int i = 0; i < dataset.length; i++) {
            Point row = dataset[i];

            // calculate distance between medoid and other cluster
            double min = Double.POSITIVE_INFINITY;
            for (int medoidIndex : randomMedoidIndex) {
                // Only compute point which is not medoid
                if (row != dataset[medoidIndex]) {
                    double currentDistance = MathUtil.getL2Distance(row, dataset[medoidIndex]);
                    if (currentDistance < min) {
                        min = currentDistance;
                        labels[i] = medoidIndex;
                    }
                }
            }
        }

        return labels;
    }

    public Integer[] fit(int maxIter) {
        // after all points assigned, we can get the current total cost of the current cluster label assignment
        Integer[] currentLabel = this.assign();
        double currentCost = this.getCost(currentLabel);

        int counter = 0;
        while (counter < maxIter) {
            // call assign() again to get new assignment with different medoid
            Integer[] modifiedLabel = this.assign();
            double modifiedCost = this.getCost(modifiedLabel);

            if (modifiedCost < currentCost) {
                currentLabel = modifiedLabel;
            }
            counter += 1;
        }
        return currentLabel;
    }
}
