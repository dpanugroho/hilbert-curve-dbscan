package util;

import java.util.Arrays;
import java.util.Collections;

public class MathUtil {
    /**
     * @return Normalized value of point on its column
     */
    public static double[] normalize(Double[] series) {
        double[] normalized = new double[series.length];

        for (int i = 0; i < series.length; i++) {
            normalized[i] = series[i] / (Collections.max(Arrays.asList(series)) - Collections.min(Arrays.asList(series)));
        }

        return normalized;
    }

    // Assume hilbert is square
    public static long[] getHilbertCoordinate(double[] normalizedDataPoints, long hilbertSize) {
        long[] coordinates = new long[normalizedDataPoints.length];
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = (long) Math.ceil(normalizedDataPoints[i] * (double) hilbertSize);
        }
        return coordinates;
    }

    // TODO: Write docs
    public static double getL2Distance(double[] source, double[] target) {
        // TODO: Check to make sure source and target has the same dimension

        // Calculate the distance
        float tmp = 0;
        for (int i = 0; i < source.length; i++) {
            tmp += Math.pow((source[i] - target[i]), 2);
        }
        return Math.sqrt(tmp);
    }

    public static double L2Cost(double[][] dataset, double[] currentRandomNeighbor) {
        double totalCost = 0;
        for(int i = 0; i < currentRandomNeighbor.length; i++){
            totalCost += getL2Distance(dataset[i], currentRandomNeighbor);
        }
        return totalCost;
    }
}

