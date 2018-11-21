package hilbert;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static util.ListUtil.deepCopy;

public class HilbertProcess {

    private int threshold;
    private int bits;
    private int dimensions;
    private HilbertCurve hc;

    public HilbertProcess(int bits, int dimensions, int threshold) {
        this.bits = bits;
        this.dimensions = dimensions;
        this.threshold = threshold;
        hc = HilbertCurve.bits(bits).dimensions(dimensions);
    }

    public List<List<Integer>> clusterAdjacentCell(Integer[] numOfCellPoints) {
        List<List<Integer>> clusters = new ArrayList<>();
        List<Integer> clusterPoints = new ArrayList<>();
        int numOfClusterPoint = 0;

        // Add one cell with 0 point at the end to generate last cluster, e.g: 10,13,14,14,14,14,0 => [13,14,14,14,14]
        Integer[] numOfCellPointsPlusEndCell = new Integer[numOfCellPoints.length + 1];
        System.arraycopy(numOfCellPoints, 0, numOfCellPointsPlusEndCell, 0, numOfCellPoints.length);
        numOfCellPointsPlusEndCell[numOfCellPoints.length] = 0;

        /*         Hilbert index |_0_|_1_|_2_|_3_|_4_|_5_|_6_|_7_|_8_|_9_|_10_|_11_|_12_|_13_|_14_|_15_|
         * number of cell points | 0 | 4 | 2 | 1 | 0 | 1 | 0 | 0 | 0 | 1 | 1  | 0  | 0  | 1  | 4  | 0  |
         * if threshold= 3 , cluster=> [[1, 1, 1, 1, 2, 2, 3], [13, 14, 14, 14, 14]]                    */
        for (int i = 0; i < numOfCellPointsPlusEndCell.length; i++) {
            if (numOfCellPointsPlusEndCell[i] != 0) {
                numOfClusterPoint += numOfCellPointsPlusEndCell[i];
                for (int j = 0; j < numOfCellPointsPlusEndCell[i]; j++) {
                    clusterPoints.add(i);
                }
            } else {
                if (numOfClusterPoint >= threshold) {
                    try {
                        clusters.add(deepCopy(clusterPoints));
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }

                if (clusterPoints.size() > 0) {
                    clusterPoints.clear();
                }

                numOfClusterPoint = 0;
            }
        }

        return clusters;
    }



    public List<Integer> getMedoidPointIndexList(List<List<Integer>> clusters, int[] hbIndexMapOriginDataIndex) {
        List<Integer> medoidPointsIndexList = new ArrayList<>();
        
        for (List<Integer> cluster : clusters) {      	
            int medoidIndex;
            //num of points in the cluster is odd
            if (cluster.size() % 2 != 0) {
                medoidIndex = cluster.size() / 2;
            } else { //num of points in the cluster is even
                medoidIndex = (cluster.size() / 2) - 1;
            }
            medoidPointsIndexList.add(hbIndexMapOriginDataIndex[(int)cluster.get(medoidIndex)]);
        }
        
        return medoidPointsIndexList;
    }

    public BigInteger mapCoordinatesToIndex(long[] coordinates) {
        HilbertCurve c = HilbertCurve.bits(bits).dimensions(dimensions);
        return c.index(coordinates);
    }

    public int[] createHilbertDistanceList(BigInteger index, int[] hilbertDistance) {
        hilbertDistance[index.intValue()] += 1;
        return hilbertDistance;
    }
}
