package uk.ac.soton.comp2211.model;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.URL;

public class SystemModel {
    private static final String DATA_FOLDER = "./src/main/resources/airport data";
    private static final String DATA_FILE_REGEX = "Airport\\[[a-zA-Z]+\\].xml";

    private static Airport airport;
    private static Obstacle[] obstacles;

    public static void main(String[] args) {
        for (String airport : listAirports()) 
            System.out.println(airport);
    }

    /**
     * Generates a list of airport names from 
     * the available airport XML data files.
     */
    public static String[] listAirports() { 

        // Get all files within the data folder that have 
        // names that match the data filename format.
        File folder = new File(DATA_FOLDER);
        FilenameFilter filter = (d, s) -> { return s.matches(DATA_FILE_REGEX); };
        String[] files = folder.list(filter);

        for (int i = 0; i < files.length; i++)
            files[i] = files[i].replace("Airport[", "").replace("].xml", "");


        return files;
    }

    public static void loadAirport(String _airportName) {

    }
}
