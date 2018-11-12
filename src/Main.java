import util.InputReader;
import core.DBScan;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        // TODO: Convert this to double [][]
//        InputReader inputReader = new InputReader();
//        try {
//            // Reading input csv
//            String[][] dataFrame = inputReader.readCsv("/Users/dpanugroho/Downloads/iris.data.txt");
//            System.out.print(dataFrame[1][4]);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        double[][] sampleDataset = new double[100][5];
        for (int i = 0; i < sampleDataset.length; i++) {
            for (int j = 0; j < sampleDataset[i].length; j++) {
                sampleDataset[i][j] = Math.random()*10;
            }
        }

        // TODO: Verify the correctness of this clustering result
        DBScan mDBScan = new DBScan(sampleDataset, 4, 5);
        int[] labels = mDBScan.Scan();
        for(int i=0; i<labels.length; i++){
            System.out.println(labels[i]);
        }
    }
}