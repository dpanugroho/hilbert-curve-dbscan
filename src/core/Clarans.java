package core;

import java.util.concurrent.ThreadLocalRandom;

public class Clarans  {
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

    private double[][] sample(){
        double[][] sample = new double[sample_size][this.dataset[0].length];
        int[] sample_index = new int[sample_size];
        for(int i=0; i<sample_size;i++){
            sample[i] = dataset[ThreadLocalRandom.current().nextInt(0, dataset.length)];
        }
        return sample;
    }

    // Parallelization can be done here
    public int[] fit(int maxIter){
       Pam mPam = new Pam(this.dataset, this.k);
       int[] bestLabel = new int[dataset.length];
       double minCost = Double.POSITIVE_INFINITY;
       for(int i=0;i<n_sample;i++){
           int[] currentLabel = mPam.fit(maxIter);
           double currentCost = mPam.getCost(currentLabel);
           if(currentCost<minCost){
               bestLabel = currentLabel;
               minCost = currentCost;
           }
       }
       return bestLabel;
    }


}
