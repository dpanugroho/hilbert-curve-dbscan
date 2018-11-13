import util.InputReader;
import core.DBScan;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        double[][] dataFrameInDouble = new double[0][];
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

        DBScan mDBScan = new DBScan(dataFrameInDouble, 0.5, 5);
        int[] labels = mDBScan.Scan();
        for (int i = 0; i < labels.length; i++) {
            System.out.println(i + 1 + "\t\t" + labels[i]);
        }


    }
}