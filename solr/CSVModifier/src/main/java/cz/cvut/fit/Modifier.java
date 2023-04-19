package cz.cvut.fit;

import org.geotools.geometry.DirectPosition2D;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

import static cz.cvut.fit.CoordinatesConverter.convert;

@Slf4j
public class Modifier {
    private String separator = System.getProperty("file.separator");

    public void modifyAll(String path) throws IOException {
        File folder = new File(path);
        if(!folder.exists()) {
            log.error("Folder " + folder.getCanonicalPath() + " doesn't exist.");
            return;
        }
        File[] files = folder.listFiles();

        log.info(files.length + " files will be modified.");

        for(File file : files) {
            modifyFile(file);
            file.delete();
        }
        log.info("Modifying files completed.");
    }

    public void modifyFile(File file) throws IOException {
        File csvFile = file;
        File output = new File("." + separator + "data" + separator + csvFile.getName());
        output.createNewFile();
        if(!output.exists()) {
            log.error("Couldn't create " + output.getCanonicalPath());
            return;
        }
        @Cleanup
        BufferedWriter csvWriter = new BufferedWriter(new FileWriter(output));
        @Cleanup 
        BufferedReader csvReader = new BufferedReader(new FileReader(csvFile));
        String row = csvReader.readLine();
        csvWriter.write(row + ";Identifikace;Coordinates_lat_lon;Text_adresy\n");
        
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(";");
            Double x = null;
            Double y = null;
            String houseNumber = data[12];
            String orientationalNumber = data[13];
            String orientationalNumberLetter = data[14];
            String transformedCoordinates = "";
            String identification = "";

            if(orientationalNumber.equals(null) || orientationalNumber.isEmpty()) {
                identification = houseNumber;
            } else {
                identification = houseNumber + "/"
                + orientationalNumber
                + orientationalNumberLetter;
            }

            try {
                x = Double.parseDouble(data[17]);
                y = Double.parseDouble(data[16]);

                DirectPosition2D transformation = convert(x, y);
                Double transformedX = transformation.x;
                Double transformedY = transformation.y;

                transformedCoordinates = transformedX + "," + transformedY;
            } catch(Exception e) {
            } finally {
                String textAddress = "";
                if (!data[10].isEmpty())
                    textAddress += data[10] + " " + identification;
                else
                    textAddress += data[8] + " " + identification;


                textAddress += ", " + data[15] + " ";
                if (!data[6].isEmpty())
                    textAddress += data[6];
                else
                    textAddress += data[2];

                csvWriter.write(row + ";"
                        + identification + ";"
                        + transformedCoordinates + ";"
                        +textAddress + "\n");
            }
        }
    }
}
