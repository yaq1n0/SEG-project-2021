package uk.ac.soton.comp2211.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class SystemModel {
    private static final String AIRPORT_DATA_FOLDER = "./src/main/resources/airports";
    private static final String OBSTACLE_DATA_FILE = "./src/main/resources/obstacles.xml";
    private static final String DATA_FILE_REGEX = "Airport\\[[a-zA-Z]+\\].xml";

    private static Airport airport;
    private static Obstacle[] obstacles;

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        for (String[] airport : listAirports()) 
            System.out.println(airport[0] + " : " + airport[1]);

        loadAirport(listAirports()[1][0]);
        loadObstacles();
    }

    /**
     * Generates a list of airport names from 
     * the available airport XML data files.
     * 
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static String[][] listAirports() throws ParserConfigurationException { 

        // Get all files within the data folder that have 
        // names that match the data filename format.
        File folder = new File(AIRPORT_DATA_FOLDER);
        FilenameFilter filter = (d, s) -> { return s.matches(DATA_FILE_REGEX); };
        File[] files = folder.listFiles(filter);

        String[][] airportList = new String[files.length][2];
        for (int i = 0; i < files.length; i++) {
            airportList[i][0] = files[i].getName();

            try {
                DataReader.loadFile(files[i]);
                airportList[i][1] = DataReader.getAirportName();

            } catch (Exception e) {
                airportList[i][1] = "[ERROR] Broken file!";

                System.out.println(e.getMessage());
            }
        }

        return airportList;
    }

    public static void loadAirport(String _airportFilename) throws ParserConfigurationException, SAXException, IOException {
        File airportFile = new File(AIRPORT_DATA_FOLDER, _airportFilename);
        DataReader.loadFile(airportFile);

        String airportName = DataReader.getAirportName();
        Runway[] runways = DataReader.getRunways();

        airport = new Airport(airportName, runways);
    }

    public static Airport getAirport() { return airport; }

    public static void loadObstacles() throws ParserConfigurationException, SAXException, IOException {
        File obstacleFile = new File(OBSTACLE_DATA_FILE);
        DataReader.loadFile(obstacleFile);

        obstacles = DataReader.getObstacles();
    }

    public static Obstacle[] getObstacles() { return obstacles; }
}
