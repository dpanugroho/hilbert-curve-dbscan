package util;

import java.io.*;
import java.util.List;

public class ListUtil {

    // Avoid List modification affects another list after copy values of a list to a new list
    public static List<Integer> deepCopy(List<Integer> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<Integer> dest = (List<Integer>) in.readObject();

        return dest;
    }
}
