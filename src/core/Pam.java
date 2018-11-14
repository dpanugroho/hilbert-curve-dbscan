package core;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation of Partition Around Medoid (PAM)
 * Maybe we can add variable to store usedMedoid to avoid infinite loop
 */

public class Pam {
    private double[][] dataset;
    private int k;

    public Pam(double[][] dataset, int k) {
        this.dataset = dataset;
        this.k = k;

    }

    public double getCost(int[] labels) {
        // for each cluster label in [0..k]
        double totalCost = 0;
        for (int i = 0; i < labels.length; i++) {
            totalCost += Distance.getL2Distance(dataset[i], dataset[labels[i]]);
        }
        return totalCost;
    }

    private int[] assign() {
        int[] labels = new int[dataset.length];

        // Initialize random index to pick initial random medoid from dataset
        int[] randomMedoidIndex = new int[this.k];
        for (int i = 0; i < k; i++) {
            randomMedoidIndex[i] = ThreadLocalRandom.current().nextInt(0, dataset.length);
        }

        // Assign non meodid to it's closest medoid
        for (int i = 0; i < dataset.length; i++) {
            double[] row = dataset[i];

            // calculate distance between medoid and other cluster
            double min = Double.POSITIVE_INFINITY;
            for (int medoidIndex : randomMedoidIndex) {
                // Only compute point which is not medoid
                if (row != dataset[medoidIndex]) {
                    double currentDistance = Distance.getL2Distance(row, dataset[medoidIndex]);
                    if (currentDistance < min) {
                        min = currentDistance;
                        labels[i] = medoidIndex;
                    }
                }
            }
        }

        return labels;
    }

    public int[] fit(int maxIter) {
        // after all points assigned, we can get the current total cost of the current cluster label assignment
        int[] currentLabel = this.assign();
        double currentCost = this.getCost(currentLabel);

        int counter = 0;
        while (counter < maxIter) {
            // call assign() again to get new assignment with different medoid
            int[] modifiedLabel = this.assign();
            double modifiedCost = this.getCost(modifiedLabel);

            if (modifiedCost < currentCost) {
                currentLabel = modifiedLabel;
            }
            counter += 1;
        }
        return currentLabel;
    }
}
