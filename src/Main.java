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
            String[][] dataFrame = inputReader.readCsv("../iris.data.txt");
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
        long total_startTime = System.nanoTime();

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

        long elapsedTime = (System.nanoTime() - startTime);
        startTime = System.nanoTime();
        System.out.println("Finding initial medoids Elapsed time: " + String.valueOf(elapsedTime));

        // Run CLARANS on dataset, and get list of cluster
        Clarans clarans = new Clarans(datasetInPoint, threshold, initialMedoid);
        List<Point[]> partitions = clarans.assign(10);

        elapsedTime = (System.nanoTime() - startTime);
        startTime = System.nanoTime();
        System.out.println("Run CLARANS Elapsed time: " + String.valueOf(elapsedTime));

        double EPS = 0.65;
        List<Cluster> dbScanResult = new ArrayList<>();

        // Parallel
        partitions.parallelStream().forEach((partition) -> {
            DBScan scan = new DBScan(partition, EPS, 3);
            List<Cluster> currentResult = scan.Scan();
            dbScanResult.addAll(currentResult);
        });
        
        elapsedTime = (System.nanoTime() - startTime);
        System.out.println("Run DBSCAN Parallel Elapsed time: " + String.valueOf(elapsedTime));

        // TODO: Look up for why some of the clusters are null
        List<Cluster> finalDbScanResult = dbScanResult.stream().filter(Objects::nonNull).collect(Collectors.toList());

        startTime = System.nanoTime();
        // TODO: Analyze & perform tests
        ClusterMerger clusterMerger = new ClusterMerger(finalDbScanResult, 0.1, EPS);
        List<Cluster> finalClusters = clusterMerger.mergeAll();
        
        elapsedTime = (System.nanoTime() - startTime);
        System.out.println("Merging Partitions - Elapsed time: " + String.valueOf(elapsedTime));
        System.out.println("totalTime" + String.valueOf(System.nanoTime() -total_startTime));

        System.out.println("\nDBScan with Hilbert Curve and Paralleled");

        int pointCount = 0;
        for (Cluster cluster: finalClusters) {
            System.out.println("Cluster "+cluster.getLabel());
            for (Point point: cluster.getMembers()) {
                System.out.println(" |- "+point.toString());
                pointCount++;
            }
        }
        
        Point[] points = new Point[dataFrameInDouble.length];

        for (int i = 0; i < dataFrameInDouble.length; i++) {
            Point point = new Point(dataFrameInDouble[i]);
            points[i] = point;
        }

        System.out.println("\nDBScan");

        startTime = System.nanoTime();

        DBScan scan = new DBScan(points, EPS, 3);
        List<Cluster> withoutParallel = scan.Scan();

        pointCount = 0;

        for (Cluster cluster: withoutParallel) {
            System.out.println("Cluster "+cluster.getLabel());
            for (Point point: cluster.getMembers()) {
                System.out.println(" |- "+point.toString());
                pointCount++;
            }
        }

        elapsedTime = (System.nanoTime() - startTime);
        System.out.println("Elapsed time: " + String.valueOf(elapsedTime));
    }
}
