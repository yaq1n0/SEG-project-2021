package uk.ac.soton.comp2211.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import uk.ac.soton.comp2211.exceptions.ExtractionException;
import uk.ac.soton.comp2211.exceptions.LoadingException;
import uk.ac.soton.comp2211.exceptions.SchemaException;

public class SystemModel {
    private static final String AIRPORT_DATA_FOLDER = "/airports";
    private static final String AIRPORT_SCHEMA = "/airport.xsd";
    private static final String OBSTACLE_DATA_FILE = "/obstacles.xml";
    private static final String OBSTACLE_SCHEMA = "/obstacles.xsd";
    private static final String DATA_FILE_REGEX = "[a-zA-Z0-9-_]+.xml";

    protected static final Logger LOGGER = LogManager.getLogger(SystemModel.class);

    private static File airportSchemaFile;
    private static File obstalceSchemaFile;

    private static Airport airport;
    private static Obstacle[] obstacles;

    /**
     * Load XSD schema files, used to validate XML files.
     * 
     * @throws LoadingException
     */
    private static void loadSchemas() throws LoadingException {
        LOGGER.info("Loading XSD schema files...");

        try {
            String airportSchemaPath = SystemModel.class.getResource(AIRPORT_SCHEMA).getPath();
            airportSchemaFile = new File(airportSchemaPath);
            LOGGER.info("Airport XSD schema loaded.");
        } catch (Exception e) {
            String errorMessage = "Airport XSD schema not found!";
            LOGGER.error(errorMessage);
            throw new LoadingException(errorMessage);
        }

        try {
            String obstacleSchemaPath = SystemModel.class.getResource(OBSTACLE_SCHEMA).getPath();
            obstalceSchemaFile = new File(obstacleSchemaPath);
            LOGGER.info("Obstacle XSD schema loaded.");
        } catch (Exception e) {
            String errorMessage = "Obstacle XSD schema not found!";
            LOGGER.error(errorMessage);
            throw new LoadingException(errorMessage);
        }
    }

    /**
     * Generates a list of airport names from 
     * the available airport XML data files.
     * @throws LoadingException
     */
    public static String[][] listAirports() throws LoadingException {
        LOGGER.info("Generating a list of airports from: " + AIRPORT_DATA_FOLDER);

        if (airportSchemaFile == null || obstalceSchemaFile == null) loadSchemas(); 

        // Get all files within the data folder that have 
        // names that match the data filename format.
        String airportFolderPath;
        try {
            airportFolderPath = SystemModel.class.getResource(AIRPORT_DATA_FOLDER).getPath();
        } catch (Exception e) {
            String errorMessage = "Airport data folder not found!";
            LOGGER.error(errorMessage);
            throw new LoadingException(errorMessage);
        }

        File airportFolder = new File(airportFolderPath);
        LOGGER.info("Airport data folder loaded.");

        System.out.println(airportFolder.getAbsolutePath());

        FilenameFilter filter = (d, s) -> { return s.matches(DATA_FILE_REGEX); };
        File[] files = airportFolder.listFiles(filter);

        if (files == null) {
            String errorMessage = "No airport data files loaded!";
            LOGGER.error(errorMessage);
            throw new LoadingException(errorMessage);
        }

        String[][] airportList = new String[files.length][2];
        for (int i = 0; i < files.length; i++) {
            airportList[i][0] = files[i].getName();

            try {
                DataReader.loadFile(files[i], airportSchemaFile);
                airportList[i][1] = DataReader.getAirportName();

            } catch (Exception e) {
                LOGGER.error("Couldn't read " + files[i].getName() + ": " + e.getMessage());
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

        LOGGER.info("Loading airport data from: " + _airportFilename);

        if (airportSchemaFile == null || obstalceSchemaFile == null) loadSchemas(); 

        String airportFolderPath;
        try {
            airportFolderPath = SystemModel.class.getResource(AIRPORT_DATA_FOLDER).getPath();
        } catch (Exception e) {
            String errorMessage = "Airport data file not found!";
            LOGGER.error(errorMessage);
            throw new Exception(errorMessage);
        }

        // Get airport data file.
        File airportFile = new File(airportFolderPath, _airportFilename);
        LOGGER.info("Airport data file loaded.");

        try {
            // Load the airport data file into the data reader, with the XSD schema.
            DataReader.loadFile(airportFile, airportSchemaFile);

            // Extract airport name from airport data file.
            String airportName = DataReader.getAirportName();

            // Extract tarmac data from airport data file.
            Tarmac[] tarmacs = DataReader.getTarmacs();

            // Instantiate the airport with the airport name and runway data.
            airport = new Airport(airportName, tarmacs, _airportFilename);
        } catch (Exception e) {
            LOGGER.error("Failed to extract airport data: " + e.getMessage());
            e.printStackTrace();

            throw e;
        }
    }

    /**
     * Extract airport data from XML file.
     * 
     * Side effects:
     *  - Loads schemas if not already loaded.
     *
     * @param _airportFilename
     * @throws Exception
     */
    public static void importAirport(String _airportFilename) throws Exception {
        LOGGER.info("Loading airport data from: " + _airportFilename);

        if (airportSchemaFile == null || obstalceSchemaFile == null) loadSchemas(); 

        // Get airport data file.
        File airportFile = new File(_airportFilename);

        // Load the airport data file into the data reader, with the XSD schema.
        DataReader.loadFile(airportFile, airportSchemaFile);

        // Extract airport name from airport data file.
        String airportName = DataReader.getAirportName();

        // Extract tarmac data from airport data file.
        Tarmac[] tarmacs = DataReader.getTarmacs();

        // Instantiate the airport with the airport name and runway data.
        airport = new Airport(airportName, tarmacs, _airportFilename);
    }

    public static Airport getAirport() throws Exception {
        if (airport == null) throw new Exception("Airport not loaded into model!");

        return airport; 
    }

    /**
     * Extract obstacle data from obstacle XML data file.
     * 
     * Side effects:
     *  - Load schemas if not already loaded.
     * @throws Exception
     */
    public static void loadObstacles() throws Exception {
        LOGGER.info("Loading obstacle data from: " + OBSTACLE_DATA_FILE);

        if (airportSchemaFile == null || obstalceSchemaFile == null) loadSchemas(); 

        String obstacleFilePath;
        try {
            obstacleFilePath = SystemModel.class.getResource(OBSTACLE_DATA_FILE).getPath();
        } catch (Exception e) {
            String errorMessage = "Obstacle data file not found!";
            LOGGER.error(errorMessage);
            throw new Exception(errorMessage);
        }

        File obstacleFile = new File(obstacleFilePath);
        LOGGER.info("Obstacle data file loaded.");

        try {
            DataReader.loadFile(obstacleFile, obstalceSchemaFile);

            obstacles = DataReader.getObstacles();

            LOGGER.info("Obstacle data extracted.");
        } catch (Exception e) {
            LOGGER.error("Failed to extract obstacle data: " + e.getMessage());

            throw e;
        }
    }

    /**
     * Adds a new obstacle to the obstacle data file,
     * and then reloads the obstacles from that file.
     * 
     * @param _newObstacle
     * @throws Exception
     */
    public static void addObstacle(Obstacle _newObstacle) throws Exception {
        LOGGER.info("Adding new obstacle...");

        String obstacleFilePath;
        obstacleFilePath = SystemModel.class.getResource(OBSTACLE_DATA_FILE).getPath();

        File obstacleFile = new File(obstacleFilePath);

        if (obstacleFile.exists()) {
            LOGGER.info("Writing obstacle data to XML file...");
            
            DataWriter.writeObstacle(_newObstacle, obstacleFile);

            LOGGER.info("New obstacle added successfully.");

        } else {
            String errorMessage = "Obstacle data file not found!";

            LOGGER.error(errorMessage);

            throw new Exception(errorMessage);
        }

        loadObstacles();
    }

    /**
     * Adds a new airport to a new XML file,
     * and then loads that airport into the model.
     * 
     * @param _newAirport
     * @param _airportFileName
     * @throws Exception
     */
    public static void addAirport(Airport _newAirport, String _airportFileName) throws Exception {
        LOGGER.info("Adding new airport...");

        String airportFilePath = SystemModel.class.getResource(AIRPORT_DATA_FOLDER).getPath() + "/" + _airportFileName;

        File airportFile = new File(airportFilePath);

        if (airportFile.exists()) {
            String errorMessage = "Cannot write airport data to a file that already exists!";

            LOGGER.error(errorMessage);

            throw new Exception(errorMessage);
        }

        LOGGER.info("Writing airport data to XML file...");

        DataWriter.writeAirport(_newAirport, airportFile);

        LOGGER.info("New airport added successfully.");

        loadAirport(_airportFileName);
    }

    public static Obstacle[] getObstacles() { return obstacles; }
    
    public static void deleteTarmac(Runway _runway) throws SAXException, IOException, TransformerException, ParserConfigurationException {
        LOGGER.info("Remove tarmac from airport.");

        for (Runway runway : airport.getRunways())
            if (runway == _runway) airport.getTarmacs().remove(runway.getTarmac());

        LOGGER.info("Modifying airport data file.");

        String airportFileName = airport.getDataFile();
        String airportFilePath = SystemModel.class.getResource(AIRPORT_DATA_FOLDER).getPath() + "/" + airportFileName;

        File airportFile = new File(airportFilePath);

        DataWriter.writeAirport(airport, airportFile);
    }
        
    /**
     * Add a new tarmac to the currently loaded airport 
     * and modify the airport data file.
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     * @throws SAXException
     */
    public static void addTarmac(Tarmac _tarmac) throws SAXException, IOException, TransformerException, ParserConfigurationException {
        LOGGER.info("Adding tarmac to airport.");

        airport.getTarmacs().add(_tarmac);

        LOGGER.info("Modifying airport data file.");

        String airportFileName = airport.getDataFile();
        String airportFilePath = SystemModel.class.getResource(AIRPORT_DATA_FOLDER).getPath() + "/" + airportFileName;

        File airportFile = new File(airportFilePath);

        DataWriter.writeAirport(airport, airportFile);
    }
}
