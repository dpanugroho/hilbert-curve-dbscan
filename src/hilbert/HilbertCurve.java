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
    
    public long[] point(BigInteger index) {
        Preconditions.checkNotNull(index);
        Preconditions.checkArgument(index.signum() != -1, "index cannot be negative");
        return transposedIndexToPoint(bits, transpose(index));
    }

    public long[] point(long index) {
        return point(BigInteger.valueOf(index));
    }
    
    long[] transpose(BigInteger index) {
        long[] x = new long[dimensions];
        transpose(index, x);
        return x;
    }

    private void transpose(BigInteger index, long[] x) {
        byte[] b = index.toByteArray();
        for (int idx = 0; idx < 8 * b.length; idx++) {
            if ((b[b.length - 1 - idx / 8] & (1L << (idx % 8))) != 0) {
                int dim = (length - idx - 1) % dimensions;
                int shift = (idx / dimensions) % bits;
                x[dim] |= 1L << shift;
            }
        }
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
    
    static long[] transposedIndexToPoint(int bits, long... x) {
        final long N = 2L << (bits - 1);
        // Note that x is mutated by this method (as a performance improvement
        // to avoid allocation)
        int n = x.length; // number of dimensions
        long p, q, t;
        int i;
        // Gray decode by H ^ (H/2)
        t = x[n - 1] >> 1;
        // Corrected error in Skilling's paper on the following line. The
        // appendix had i >= 0 leading to negative array index.
        for (i = n - 1; i > 0; i--)
            x[i] ^= x[i - 1];
        x[0] ^= t;
        // Undo excess work
        for (q = 2; q != N; q <<= 1) {
            p = q - 1;
            for (i = n - 1; i >= 0; i--)
                if ((x[i] & q) != 0L)
                    x[0] ^= p; // invert
                else {
                    t = (x[0] ^ x[i]) & p;
                    x[0] ^= t;
                    x[i] ^= t;
                }
        } // exchange
        return x;
    }
}
