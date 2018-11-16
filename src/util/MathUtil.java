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
}

