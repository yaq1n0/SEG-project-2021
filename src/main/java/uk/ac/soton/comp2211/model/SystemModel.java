package uk.ac.soton.comp2211.model;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SystemModel {
    private static final String AIRPORT_DATA_FOLDER = "./src/main/resources/airports";
    private static final String AIRPORT_SCHEMA = "./src/main/resources/airport.xsd";
    private static final String OBSTACLE_DATA_FILE = "./src/main/resources/obstacles.xml";
    private static final String OBSTACLE_SCHEMA = "./src/main/resources/obstacles.xsd";
    private static final String DATA_FILE_REGEX = "[a-zA-Z0-9-_]+.xml";

    protected static final Logger LOGGER = LogManager.getLogger(SystemModel.class);

    private static File airportSchemaFile;
    private static File obstalceSchemaFile;

    private static Airport airport;
    private static Obstacle[] obstacles;

    public static void main(String[] args) throws Exception {
        loadSchemas();

        for (String[] airport : listAirports()) 
            System.out.println(airport[0] + " : " + airport[1]);

        loadAirport(listAirports()[1][0]);
        loadObstacles();
    }

    /**
     * Load XSD schema files, used to validate XML files.
     */
    public static void loadSchemas() {
        LOGGER.info("Loading XSD schema files.");

        airportSchemaFile = new File(AIRPORT_SCHEMA);
        obstalceSchemaFile = new File(OBSTACLE_SCHEMA);
    }

    /**
     * Generates a list of airport names from 
     * the available airport XML data files.
     */
    public static String[][] listAirports() {
        LOGGER.info("Generating a list of airports from: " + AIRPORT_DATA_FOLDER);

        // Get all files within the data folder that have 
        // names that match the data filename format.
        File folder = new File(AIRPORT_DATA_FOLDER);
        FilenameFilter filter = (d, s) -> { return s.matches(DATA_FILE_REGEX); };
        File[] files = folder.listFiles(filter);

        String[][] airportList = new String[files.length][2];
        for (int i = 0; i < files.length; i++) {
            airportList[i][0] = files[i].getName();

            try {
                DataReader.loadFile(files[i], airportSchemaFile);
                airportList[i][1] = DataReader.getAirportName();

            } catch (Exception e) {
                airportList[i][1] = "[ERROR] Broken file!";
            }
        }

        return airportList;
    }

    /**
     * Extract airport data from XML file.
     * 
     * @param _airportFilename
     * @throws Exception
     */
    public static void loadAirport(String _airportFilename) throws Exception {
        LOGGER.info("Loading airport data from: " + AIRPORT_DATA_FOLDER + "/" + _airportFilename);

        // Get airport data file.
        File airportFile = new File(AIRPORT_DATA_FOLDER, _airportFilename);

        // Load the airport data file into the data reader, with the XSD schema.
        DataReader.loadFile(airportFile, airportSchemaFile);

        // Extract airport name from airport data file.
        String airportName = DataReader.getAirportName();

        // Extract runway data from airport data file.
        Runway[] runways = DataReader.getRunways();

        // Instantiate the airport with the airport name and runway data.
        airport = new Airport(airportName, runways);
    }

    public static Airport getAirport() { return airport; }

    public static void loadObstacles() throws Exception {
        LOGGER.info("Loading obstacle data from: " + OBSTACLE_DATA_FILE);

        File obstacleFile = new File(OBSTACLE_DATA_FILE);
        DataReader.loadFile(obstacleFile, obstalceSchemaFile);

        obstacles = DataReader.getObstacles();
    }

    public static Obstacle[] getObstacles() { return obstacles; }
}
