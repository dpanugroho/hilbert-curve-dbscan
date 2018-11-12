package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class InputReader {

    public InputReader() {
    }

    public String[][] readCsv(String path) throws FileNotFoundException {
        ArrayList<ArrayList<String>> dataFrame = new ArrayList<>();

        Scanner rowScanner = new Scanner(new File(path));
        rowScanner.useDelimiter("\n");
        while (rowScanner.hasNext()) {
            Scanner colScanner = new Scanner(rowScanner.next());
            colScanner.useDelimiter(",");

            ArrayList<String> currentRow = new ArrayList<>();

            while (colScanner.hasNext()) {
                currentRow.add(colScanner.next());
            }
            dataFrame.add(currentRow);
        }
        rowScanner.close();

        String[][] dataFrameArray = new String[dataFrame.size()][];
        for (int i = 0; i < dataFrame.size(); i++) {
            ArrayList<String> row = dataFrame.get(i);
            dataFrameArray[i] = row.toArray(new String[row.size()]);
        }
        return dataFrameArray;

    }
}