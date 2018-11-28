import beans.Cluster;
import beans.Point;
import core.Clarans;
import core.ClusterMerger;
import core.DBScan;
import util.InputReader;
import util.MathUtil;
import hilbert.HilbertProcess;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        // Store dataset into 2D array of double
        double[][] dataFrameInDouble = new double[0][0];
        long[][] dataFrameHilbert;

        // Reading input csv
        InputReader inputReader = new InputReader();
        try {
            String[][] dataFrame = inputReader.readCsv("iris.data.txt");
            dataFrameInDouble = new double[dataFrame.length][dataFrame[0].length];
            for (int i = 0; i < dataFrame.length; i++) {
                for (int j = 0; j < dataFrame[i].length; j++) {
                    dataFrameInDouble[i][j] = Double.valueOf(dataFrame[i][j]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        long startTime = System.nanoTime();

        int dimensions = dataFrameInDouble[0].length; // Dimension of the dataset
        int bits = 2; // Bits of the Hilbert Curve
        int threshold = 3;

        int[] hilbertDistance = new int[(int) Math.pow(Math.pow(2, bits), dimensions)];
        int[] indexOfCoordinates = new int[(int) Math.pow(Math.pow(2, bits), dimensions)];

        Point[] datasetInPoint = new Point[dataFrameInDouble.length];
        for (int i = 0; i < dataFrameInDouble.length; i++) {
            datasetInPoint[i] = new Point(dataFrameInDouble[i]);
        }

        dataFrameHilbert = new long[datasetInPoint.length][datasetInPoint[0].getCoordinates().length];

        for (int j = 0; j < datasetInPoint[0].getCoordinates().length; j++) {
            Double[] column = new Double[datasetInPoint.length];
            for (int i = 0; i < datasetInPoint.length; i++) {
                column[i] = datasetInPoint[i].getCoordinates()[j];
            }
            // Normalize
            double[] normalizedData = MathUtil.normalize(column).clone();
            // Get coordinate of dataset in hilbert space
            long[] HilbertCoordinate = MathUtil.getHilbertCoordinate(normalizedData, bits).clone();
            for (int i = 0; i < datasetInPoint.length; i++) {
                dataFrameHilbert[i][j] = HilbertCoordinate[i];
            }
        }

        HilbertProcess hilbertProcess = new HilbertProcess(bits, dimensions, threshold);

        // Find Hilbert index
        for (int i = 0; i < datasetInPoint.length; i++) {
            long[] coordinates = new long[datasetInPoint[0].getCoordinates().length];
            System.arraycopy(dataFrameHilbert[i], 0, coordinates, 0,
                    datasetInPoint[0].getCoordinates().length);
            BigInteger index = hilbertProcess.mapCoordinatesToIndex(coordinates);
            indexOfCoordinates[index.intValue()] = i;
            hilbertProcess.createHilbertDistanceList(index, hilbertDistance);
        }

        Integer[] numberOfCellPoints = new Integer[hilbertDistance.length];
        for (int j = 0; j < hilbertDistance.length; j++) {
            numberOfCellPoints[j] = hilbertDistance[j];
        }

        // Get list of initialMedoid Using Hilbert Curve
        List<Integer> initialMedoid = hilbertProcess.getMedoidPointIndexList(hilbertProcess
                .clusterAdjacentCell(numberOfCellPoints), indexOfCoordinates);

        // Run CLARANS on dataset, and get list of cluster
        Clarans clarans = new Clarans(datasetInPoint, threshold, initialMedoid);
        List<Point[]> partitions = clarans.assign(10);


        double EPS = 10;
        List<Cluster> dbScanResult = new ArrayList<>();

        // Parallel
        partitions.parallelStream().forEach((partition) -> {
            DBScan scan = new DBScan(partition, EPS, 3);
            List<Cluster> currentResult = scan.Scan();
            dbScanResult.addAll(currentResult);
        });

        // TODO: Look up for why some of the clusters are null
        List<Cluster> finalDbScanResult = dbScanResult.stream().filter(Objects::nonNull).collect(Collectors.toList());

        // TODO: Analyze & perform tests
        ClusterMerger clusterMerger = new ClusterMerger(finalDbScanResult, 0.05, EPS);
        List<Cluster> finalClusters = clusterMerger.mergeAll();

        System.out.println("DBScan with Hilbert Curve and Paralleled");

        int pointCount = 0;
        for (Cluster cluster: finalClusters) {
            for (Point point: cluster.getMembers()) {
                System.out.print(pointCount + " ");
                System.out.println(point.toString());
                pointCount++;
            }
        }

        long elapsedTime = (System.nanoTime() - startTime);
        System.out.println("Elapsed time: " + String.valueOf(elapsedTime));

        Point[] points = new Point[dataFrameInDouble.length];

        for (int i = 0; i < dataFrameInDouble.length; i++) {
            Point point = new Point(dataFrameInDouble[i]);
            points[i] = point;
        }

        System.out.println("DBScan");

        startTime = System.nanoTime();

        DBScan scan = new DBScan(points, EPS, 3);
        List<Cluster> withoutParallel = scan.Scan();

        pointCount = 0;

        for (Cluster cluster: withoutParallel) {
            for (Point point: cluster.getMembers()) {
                System.out.print(pointCount + " ");
                System.out.println(point.toString());
                pointCount++;
            }
        }

        elapsedTime = (System.nanoTime() - startTime);
        System.out.println("Elapsed time: " + String.valueOf(elapsedTime));
    }
}
