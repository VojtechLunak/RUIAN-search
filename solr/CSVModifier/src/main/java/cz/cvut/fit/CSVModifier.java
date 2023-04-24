package cz.cvut.fit;

import java.io.*;

public class CSVModifier {
    public static void main(String[] args) throws IOException {
        String separator = System.getProperty("file.separator");

        System.setProperty("org.geotools.referencing.forceYX", "true");
        Modifier modifier = new Modifier();
        String folderPath = "." + separator + "temp";
        modifier.modifyAll(folderPath);
    }
}
