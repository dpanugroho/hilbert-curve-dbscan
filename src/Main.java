import util.InputReader;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        InputReader inputReader = new InputReader();
        try {
            // Reading input csv
            String[][] dataFrame = inputReader.readCsv("/Users/dpanugroho/Downloads/iris.data.txt");
            System.out.print(dataFrame[1][1]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
