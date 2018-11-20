import core.Clarans;
import core.Pam;
import util.InputReader;
import util.MathUtil;
import core.DBScan;
import hilbert.HilbertCurve;
import hilbert.HilbertProcess;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        long startTime = System.nanoTime();
       
        double[][] dataFrameInDouble = new double[0][];
        long[][] dataFrameHilbert = new long[0][]; 
        double[][] testNormalized = new double[0][]; 
        int bits = 2;
        int dimensions = 4;
        int[] hilbertDistance = new int[(int)Math.pow(Math.pow(2,bits),dimensions)];
        
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
        
        System.out.println(dataFrameInDouble[0].length); //4D
        System.out.println(dataFrameInDouble.length); //150
        
		testNormalized = new double[dataFrameInDouble.length][dataFrameInDouble[0].length];
		dataFrameHilbert = new long[dataFrameInDouble.length][dataFrameInDouble[0].length];
        //normalize data,get coordinate,get Hilbert index
        for(int j = 0; j < dataFrameInDouble[0].length; j++) {
        	Double[] column = new Double[dataFrameInDouble.length];
        	for(int i=0; i<dataFrameInDouble.length;i++) {
        		column[i]=dataFrameInDouble[i][j];     		
        	}
        	//normalize
        	double[] normalizedData = MathUtil.normalize(column).clone();
            //getHilbertCoordinate
            long[] HilbertCoordinate = MathUtil.getHilbertCoordinate(normalizedData,bits).clone();
            for(int i=0;i<dataFrameInDouble.length;i++) {
        		testNormalized[i][j] = normalizedData[i] ;
        		dataFrameHilbert[i][j] = HilbertCoordinate[i] ;
    		}     	
        }
        
        
        System.out.println("-----------------------normalized----------------------------------");
        for (int i = 0; i < dataFrameInDouble.length; i++) {
            for (int j = 0; j < dataFrameInDouble[0].length; j++) {
                System.out.print(testNormalized[i][j] + "\t\t");
            }
            System.out.println();
        }
        System.out.println("-------------------------coordinate--------------------------------");
        for (int i = 0; i < dataFrameInDouble.length; i++) {
            for (int j = 0; j < dataFrameInDouble[0].length; j++) {
                System.out.print(dataFrameHilbert[i][j] + "\t\t");
            }
            System.out.println();
        }
        
        HilbertProcess hilbertProcess = new HilbertProcess();
        
        //Find Hilbert index
        for (int i = 0; i < dataFrameInDouble.length; i++) {
        	long[] coordinates= new long[dataFrameInDouble[0].length];
        	BigInteger index = BigInteger.valueOf(0);
            for (int j = 0; j < dataFrameInDouble[0].length; j++) {       	
            	coordinates[j] = dataFrameHilbert[i][j];       	
            }
            System.out.println(i);
            index = hilbertProcess.mapCoordinatesToIndex(coordinates, bits, dimensions);
            System.out.println("index="+index);
            hilbertProcess.createHilbertDistanceList(index,hilbertDistance);
        }
        
        Integer[] numberOfCellPoints = new Integer[hilbertDistance.length];
        for (int j = 0; j < hilbertDistance.length; j++) {
        	numberOfCellPoints[j] = hilbertDistance[j];
        }
     
        //System.out.println(numberOfCellPoints);
        //System.out.println(hilbertProcess.clusterAdjacentCell(numberOfCellPoints));
        System.out.println(hilbertProcess.getMedoidPointList(hilbertProcess.clusterAdjacentCell(numberOfCellPoints)));
        
//        Integer[] numOfCellPoints = {0,4,2,1,0,1,0,0,0,1,1,0,0,1,4};
//        System.out.println(hilbertProcess.clusterAdjacentCell(numOfCellPoints));
//        System.out.println(hilbertProcess.getMedoidPointList(hilbertProcess.clusterAdjacentCell(numOfCellPoints)));

        //DBScan mDBScan = new DBScan(dataFrameInDouble, 0.5, 5);
        //int[] labels = mDBScan.Scan();

        // Pam mPam = new Pam(dataFrameInDouble, 5);
        // int[] labels = mPam.fit(100);

        Clarans mClara = new Clarans(dataFrameInDouble, 5, 3, 30);
        Integer[] labels = mClara.fit(100, true);

        for (int i = 0; i < labels.length; i++) {
            System.out.println(i + 1 + "\t\t" + labels[i]);
        }

        long elapsedTime = (System.nanoTime() - startTime);
        System.out.println("Elapsed time: " + String.valueOf(elapsedTime));
    }
}
