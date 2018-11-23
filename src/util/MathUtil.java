package util;

import beans.Point;

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
    public static double getL2Distance(Point source, Point target) {
        // TODO: Check to make sure source and target has the same dimension

        // Calculate the distance
        float tmp = 0;
        for (int i = 0; i < source.getCoordinates().length; i++) {
            tmp += Math.pow((source.getCoordinates()[i] - target.getCoordinates()[i]), 2);
        }
        return Math.sqrt(tmp);
    }

    public static double L2Cost(Point[] dataset, Point currentRandomNeighbor) {
        double totalCost = 0;
        for(int i = 0; i < currentRandomNeighbor.getCoordinates().length; i++){
            totalCost += getL2Distance(dataset[i], currentRandomNeighbor);
        }
        return totalCost;
    }
}

