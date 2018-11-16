package hilbert;

import java.math.BigInteger;
import java.util.Arrays;

public class HilbertCurve {
    private final int bits;
    private final int dimensions;

    private final int length;

    private HilbertCurve(int bits, int dimensions) {
        this.bits = bits;
        this.dimensions = dimensions;
        // cache a calculated values for small perf improvements
        this.length = bits * dimensions;
    }

    public static final class Builder {
        final int bits;

        private Builder(int bits) {
            Preconditions.checkArgument(bits > 0, "bits must be greater than zero");
            Preconditions.checkArgument(bits < 64, "bits must be 63 or less");
            this.bits = bits;
        }

        public HilbertCurve dimensions(int dimensions) {
            Preconditions.checkArgument(dimensions > 1, "dimensions must be at least 2");
            return new HilbertCurve(bits, dimensions);
        }
    }

    public static Builder bits(int bits) {
        return new Builder(bits);
    }

    public BigInteger index(long... point) {
        Preconditions.checkArgument(point.length == dimensions);
        return toIndex(transposedIndex(bits, point));
    }

    BigInteger toIndex(long... transposedIndex) {
        byte[] b = new byte[length];
        int bIndex = length - 1;
        long mask = 1L << (bits - 1);
        for (int i = 0; i < bits; i++) {
            for (int j = 0; j < transposedIndex.length; j++) {
                if ((transposedIndex[j] & mask) != 0) {
                    b[length - 1 - bIndex / 8] |= 1 << (bIndex % 8);
                }
                bIndex--;
            }
            mask >>= 1;
        }
        // b is expected to be BigEndian
        return new BigInteger(1, b);
    }

    static long[] transposedIndex(int bits, long... point) {
        final long M = 1L << (bits - 1);
        final int n = point.length; // n: Number of dimensions
        final long[] x = Arrays.copyOf(point, n);
        long p, q, t;
        int i;
        // Inverse undo
        for (q = M; q > 1; q >>= 1) {
            p = q - 1;
            for (i = 0; i < n; i++)
                if ((x[i] & q) != 0)
                    x[0] ^= p; // invert
                else {
                    t = (x[0] ^ x[i]) & p;
                    x[0] ^= t;
                    x[i] ^= t;
                }
        } // exchange
        // Gray encode
        for (i = 1; i < n; i++)
            x[i] ^= x[i - 1];
        t = 0;
        for (q = M; q > 1; q >>= 1)
            if ((x[n - 1] & q) != 0)
                t ^= q - 1;
        for (i = 0; i < n; i++)
            x[i] ^= t;

        return x;
    }
}
