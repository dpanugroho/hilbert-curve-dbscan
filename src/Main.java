import core.Clara;
import core.Clarans;
import util.InputReader;
import util.MathUtil;
import hilbert.HilbertProcess;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
//        long startTime = System.nanoTime();

        double[][] dataFrameInDouble = new double[0][];
        long[][] dataFrameHilbert = new long[0][];
//        double[][] testNormalized = new double[0][];
        int bits = 2;
        int dimensions = 4;
        int threshold = 3;
        int[] hilbertDistance = new int[(int) Math.pow(Math.pow(2, bits), dimensions)];
        int[] indexOfCoordinates = new int[(int) Math.pow(Math.pow(2, bits), dimensions)];

        InputReader inputReader = new InputReader();
        try {
            // Reading input csv
            String[][] dataFrame = inputReader.readCsv("iris.data.txt");
            dataFrameInDouble = new double[dataFrame.length][dataFrame[0].length];
            for (int i = 0; i < dataFrame.length; i++) {
                for (int j = 0; j < dataFrame[i].length; j++) {
                    dataFrameInDouble[i][j] = Double.valueOf(dataFrame[i][j]);
                    System.out.print(dataFrameInDouble[i][j] + "\t\t");
                }
                System.out.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        System.out.println(dataFrameInDouble[0].length); // 4D
//        System.out.println(dataFrameInDouble.length); // 150

//        testNormalized = new double[dataFrameInDouble.length][dataFrameInDouble[0].length];
        dataFrameHilbert = new long[dataFrameInDouble.length][dataFrameInDouble[0].length];
        // normalize data,get coordinate,get Hilbert index
        for (int j = 0; j < dataFrameInDouble[0].length; j++) {
            Double[] column = new Double[dataFrameInDouble.length];
            for (int i = 0; i < dataFrameInDouble.length; i++) {
                column[i] = dataFrameInDouble[i][j];
            }
            // normalize
            double[] normalizedData = MathUtil.normalize(column).clone();
            // getHilbertCoordinate
            long[] HilbertCoordinate = MathUtil.getHilbertCoordinate(normalizedData, bits).clone();
            for (int i = 0; i < dataFrameInDouble.length; i++) {
//                testNormalized[i][j] = normalizedData[i];
                dataFrameHilbert[i][j] = HilbertCoordinate[i];
            }
        }

//        System.out.println("-----------------------normalized----------------------------------");
//        for (int i = 0; i < dataFrameInDouble.length; i++) {
//            for (int j = 0; j < dataFrameInDouble[0].length; j++) {
//                System.out.print(testNormalized[i][j] + "\t\t");
//            }
//            System.out.println();
//        }
//        System.out.println("-------------------------coordinate--------------------------------");
//        for (int i = 0; i < dataFrameInDouble.length; i++) {
//            for (int j = 0; j < dataFrameInDouble[0].length; j++) {
//                System.out.print(dataFrameHilbert[i][j] + "\t\t");
//            }
//            System.out.println();
//        }
//
        HilbertProcess hilbertProcess = new HilbertProcess(bits, dimensions, threshold);
//
//        // Find Hilbert index
        for (int i = 0; i < dataFrameInDouble.length; i++) {
            long[] coordinates = new long[dataFrameInDouble[0].length];
            BigInteger index = BigInteger.valueOf(0);
            for (int j = 0; j < dataFrameInDouble[0].length; j++) {
                coordinates[j] = dataFrameHilbert[i][j];
            }
//            System.out.println("i=" + i);
            index = hilbertProcess.mapCoordinatesToIndex(coordinates);
            indexOfCoordinates[index.intValue()] = i;
//            System.out.println(index.intValue() + " indexOfCoordinates" + indexOfCoordinates[index.intValue()]);
            hilbertProcess.createHilbertDistanceList(index, hilbertDistance);
        }
//
        Integer[] numberOfCellPoints = new Integer[hilbertDistance.length];
        for (int j = 0; j < hilbertDistance.length; j++) {
            numberOfCellPoints[j] = hilbertDistance[j];
        }

        // get List<Integer> of initialMedoid Using Hilbert
        List<Integer> initialMedoid = hilbertProcess.getMedoidPointIndexList(hilbertProcess.clusterAdjacentCell(numberOfCellPoints), indexOfCoordinates);
        // getPartition Using Clarans
        Clarans mClara = new Clarans(dataFrameInDouble, 3, initialMedoid);
        Integer[] labels = mClara.assign(10);

        // TODO: Run DBScan for each partition genrated by CLarans

    }
}
