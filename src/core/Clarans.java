package core;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Clarans {
    double[][] dataset;
    int k;
    int n_sample;
    int sample_size;

    public Clarans(double[][] dataset, int k, int n_sample, int sample_size) {
        this.dataset = dataset;
        this.k = k;
        this.n_sample = n_sample;
        this.sample_size = sample_size;
    }

    private double[][] sample() {
        double[][] sample = new double[sample_size][this.dataset[0].length];
        int[] sample_index = new int[sample_size];
        for (int i = 0; i < sample_size; i++) {
            sample[i] = dataset[ThreadLocalRandom.current().nextInt(0, dataset.length)];
        }
        return sample;
    }

    public Integer[] fit(int maxIter, boolean parallelize) {
        Pam mPam = new Pam(sample(), this.k);
        Integer[] bestLabel = new Integer[dataset.length];
        double minCost = Double.POSITIVE_INFINITY;

        if (parallelize) {
            List<Integer[]> labels = new ArrayList<>();
            List<Double> costs = new ArrayList<>();

            Arrays.asList(n_sample).parallelStream().forEach((sample) -> {
                Integer[] currentLabel = mPam.fit(maxIter);
                double currentCost = mPam.getCost(currentLabel);

                labels.add(currentLabel);
                costs.add(currentCost);
            });

            int bestLabelIndex = costs.indexOf(Collections.min(costs));
            bestLabel = labels.get(bestLabelIndex);
        } else {
            for (int i = 0; i < n_sample; i++) {
                Integer[] currentLabel = mPam.fit(maxIter);
                double currentCost = mPam.getCost(currentLabel);
                if (currentCost < minCost) {
                    bestLabel = currentLabel;
                    minCost = currentCost;
                }
            }
        }

        return bestLabel;
    }
}
