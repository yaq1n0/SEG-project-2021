package uk.ac.soton.comp2211.model;

import javafx.scene.canvas.Canvas;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import uk.ac.soton.comp2211.exceptions.LoadingException;
import uk.ac.soton.comp2211.exceptions.ReadingException;
import uk.ac.soton.comp2211.exceptions.SchemaException;
import uk.ac.soton.comp2211.exceptions.SizeException;
import uk.ac.soton.comp2211.exceptions.WritingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

public class SystemModel {
    private static final String AIRPORT_DATA_FOLDER = "/airports";
    private static final String AIRPORT_SCHEMA = "/airport.xsd";
    private static final String OBSTACLE_DATA_FILE = "/obstacles.xml";
    private static final String OBSTACLE_SCHEMA = "/obstacles.xsd";
    private static final String DATA_FILE_REGEX = "[a-zA-Z0-9-_]+.xml";
    private static final String CALCULATIONS_FOLDER = "/calculations";
    private static final String IMAGES_FOLDER = "/images";
    private static final String NOTIFICATIONS_FOLDER = "/notifications";

    protected static final Logger LOGGER = LogManager.getLogger(SystemModel.class);

    private static File airportSchemaFile;
    private static File obstalceSchemaFile;

    private static DataReader dataReader = new DataReader();
    private static DataWriter dataWriter = new DataWriter();

    private static Airport airport;
    private static Obstacle[] obstacles;

    public static void setup() throws LoadingException {
        LOGGER.info("Setting up system model...");

        loadSchemas();
    }

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
            throw new LoadingException(LOGGER, "Airport XSD schema not found!");
        }

        try {
            String obstacleSchemaPath = SystemModel.class.getResource(OBSTACLE_SCHEMA).getPath();
            obstalceSchemaFile = new File(obstacleSchemaPath);
            LOGGER.info("Obstacle XSD schema loaded.");
        } catch (Exception e) {
            throw new LoadingException(LOGGER, "Obstacle XSD schema not found!");
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
            throw new LoadingException(LOGGER, "Airport data folder not found!");
        }

        File airportFolder = new File(airportFolderPath);
        LOGGER.info("Airport data folder loaded.");

        FilenameFilter filter = (d, s) -> { return s.matches(DATA_FILE_REGEX); };
        File[] files = airportFolder.listFiles(filter);

        if (files == null) {
            throw new LoadingException(LOGGER, "No airport data files loaded!");
        }

        String[][] airportList = new String[files.length][2];
        for (int i = 0; i < files.length; i++) {
            airportList[i][0] = files[i].getName();

            try {
                dataReader.loadFile(files[i], airportSchemaFile);
                airportList[i][1] = dataReader.getAirportName();

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
     * @param _airportFilename filename
     * @throws LoadingException
     * @throws Exception
     */
    public static void openAirport(String _airportFilename) throws LoadingException {
        LOGGER.info("Loading airport data from: " + _airportFilename);

        String airportFolderPath;
        try {
            airportFolderPath = SystemModel.class.getResource(AIRPORT_DATA_FOLDER).getPath();
        } catch (Exception e) {
            throw new LoadingException(LOGGER, "Airport data file not found!");
        }

        // Get airport data file.
        File airportFile = new File(airportFolderPath, _airportFilename);

        loadAirportData(airportFile);

    }

    /**
     * Extract airport data from XML file.
     * 
     * Side effects:
     *  - Loads schemas if not already loaded.
     *
     * @param _airportPath filename
     * @throws Exception
     */
    public static void importAirport(String _airportPath) throws LoadingException {
        LOGGER.info("Importing airport data from: " + _airportPath);

        // Get airport data file.
        File airportFile = new File(_airportPath);

        loadAirportData(airportFile);
    }

    private static void loadAirportData(File _airportFile) throws LoadingException {
        if (airportSchemaFile == null || obstalceSchemaFile == null) loadSchemas();

        try {
            // Load the airport data file into the data reader, with the XSD schema.
            dataReader.loadFile(_airportFile, airportSchemaFile);

            // Extract airport name from airport data file.
            String airportName = dataReader.getAirportName();

            // Extract tarmac data from airport data file.
            Tarmac[] tarmacs = dataReader.getTarmacs();

            // Instantiate the airport with the airport name and runway data.
            airport = new Airport(airportName, tarmacs, _airportFile);
        } catch (Exception e) {
            throw new LoadingException(LOGGER, "Failed to extract airport data: " + e.getMessage());
        }
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
     * @throws LoadingException
     * @throws SchemaException
     * @throws SizeException
     * @throws Exception
     */
    public static void loadObstacles() throws LoadingException, SchemaException, SizeException {
        LOGGER.info("Loading obstacle data from: " + OBSTACLE_DATA_FILE);

        if (airportSchemaFile == null || obstalceSchemaFile == null) loadSchemas(); 

        String obstacleFilePath;
        try {
            obstacleFilePath = SystemModel.class.getResource(OBSTACLE_DATA_FILE).getPath();
        } catch (Exception e) {
            throw new LoadingException(LOGGER, "Obstacle data file not found!");
        }

        File obstacleFile = new File(obstacleFilePath);
        LOGGER.info("Obstacle data file loaded.");

        dataReader.loadFile(obstacleFile, obstalceSchemaFile);

        obstacles = dataReader.getObstacles();

        LOGGER.info("Obstacle data extracted.");
    }

    /**
     * Adds a new obstacle to the obstacle data file,
     * and then reloads the obstacles from that file.
     * @throws WritingException
     * @throws SizeException
     * @throws SchemaException
     */
    public static void addObstacle(Obstacle _newObstacle) throws LoadingException, WritingException, SchemaException, SizeException {
        LOGGER.info("Adding new obstacle...");

        String obstacleFilePath;
        obstacleFilePath = SystemModel.class.getResource(OBSTACLE_DATA_FILE).getPath();

        File obstacleFile = new File(obstacleFilePath);

        if (!obstacleFile.exists()) {
            throw new LoadingException(LOGGER, "Obstacle data file not found!");
        }
        
        LOGGER.info("Writing obstacle data to XML file...");
        
        try {
            dataWriter.writeObstacle(_newObstacle, obstacleFile);
        } catch (TransformerException | SAXException | IOException | ParserConfigurationException e) {
            throw new WritingException(LOGGER, "Failed to write obstacle data.");
        }

        LOGGER.info("New obstacle added successfully.");

        loadObstacles();
    }

    /**
     *  Adds a new airport to a new XML file,
     * and then loads that airport into the model.
     * @param _airportName
     * @param _tarmacs
     * @throws WritingException
     * @throws LoadingException
     */
    public static void addAirport(String _airportName, Tarmac[] _tarmacs) throws WritingException, LoadingException {
        LOGGER.info("Adding new airport...");

        String airportFilePath = SystemModel.class.getResource(AIRPORT_DATA_FOLDER).getPath() + "/" + _airportName + ".xml";

        File airportFile = new File(airportFilePath);

        if (airportFile.exists()) {
            throw new WritingException(LOGGER, "Cannot write airport data to a file that already exists!");
        }

        LOGGER.info("Writing airport data to XML file...");

        Airport newAirport = new Airport(_airportName, _tarmacs, airportFile);

        try {
            dataWriter.writeAirport(newAirport, airportFile);
        } catch (SAXException | IOException | TransformerException | ParserConfigurationException e) {
            throw new WritingException(LOGGER, "Failed to write airport data to XML file.");
        }

        LOGGER.info("New airport added successfully.");

        loadAirportData(airportFile);
    }

    public static Obstacle[] getObstacles() { return obstacles; }
    
    public static void deleteTarmac(Runway _runway) {
        LOGGER.info("Remove tarmac from airport.");

        for (Runway runway : airport.getRunways())
            if (runway == _runway) {
                System.out.println("testing");

                airport.getTarmacs().remove(runway.getTarmac());
            }

        LOGGER.info("Modifying airport data file ...");

        File airportFile = airport.getDataFile();

        try {
            dataWriter.writeAirport(airport, airportFile);

            LOGGER.info("Airport data file modified successfully.");
        } catch (Exception e) {
            LOGGER.error("Failed to modify airport data file!");
        }
    }
        
    /**
     * Add a new tarmac to the currently loaded airport 
     * and modify the airport data file.
     * @throws WritingException
     */
    public static void addTarmac(Tarmac _tarmac) throws WritingException {
        LOGGER.info("Adding tarmac to airport.");

        airport.getTarmacs().add(_tarmac);

        LOGGER.info("Modifying airport data file.");

        File airportFile = airport.getDataFile();

        try {
            dataWriter.writeAirport(airport, airportFile);
        } catch (SAXException | IOException | TransformerException | ParserConfigurationException e) {
            throw new WritingException(LOGGER, "Failed to write new tarmac data to airport data file.");
        }
    }

    public static String recordCalculation(String _runwayDesignator, String[] _calculations) throws WritingException {
        String airportName = airport.getName();
        String calculationsFolderPath = SystemModel.class.getResource(CALCULATIONS_FOLDER).getPath();
        File calculationsFolder = new File(calculationsFolderPath);

        String fileNamePrefix = airportName + "-" + _runwayDesignator + "-";
        int fileNamePostfix = 0;
        File calculationsLog = new File(calculationsFolder, fileNamePrefix + fileNamePostfix);
        while (calculationsLog.exists()) {
            fileNamePostfix++;
            calculationsLog = new File(calculationsFolder, fileNamePrefix + fileNamePostfix);
        }

        try {
            dataWriter.writeCalculationLog(airportName, _runwayDesignator, _calculations, calculationsLog);
        } catch (IOException e) {
            throw new WritingException(LOGGER, "Failed to write calculations to a log file.");
        }
        
        return calculationsLog.getPath();
    }

    public static String printAirport(Canvas _canvas) throws WritingException, NullPointerException {
        String airportName = airport.getName();
        String imagesFolderPath = SystemModel.class.getResource(IMAGES_FOLDER).getPath();
        File imagesFolder = new File(imagesFolderPath);

        if (!imagesFolder.exists()) imagesFolder.mkdirs();

        String fileNamePrefix = airportName + "-";
        int fileNamePostfix = 0;
        File imageFile = new File(imagesFolder, fileNamePrefix + fileNamePostfix + ".png");
        while (imageFile.exists()) {
            fileNamePostfix++;
            imageFile = new File(imagesFolder, fileNamePrefix + fileNamePostfix + ".png");
        }

        try {
            dataWriter.savePicture(_canvas, imageFile);
        } catch (IOException e) {
            throw new WritingException(LOGGER, "Failed to canvas as an image.");
        }
        
        return imageFile.getPath();
    }

    public static void storeNotification(String _notification) throws WritingException, LoadingException {
        String notificationsFolderPath;
        try {
            notificationsFolderPath = SystemModel.class.getResource(NOTIFICATIONS_FOLDER).getPath();
        } catch (NullPointerException e) {
            throw new LoadingException(LOGGER, "Notifications folder not found!");
        }
        
        File notificationsFolder = new File(notificationsFolderPath);
        File notificationsFile = new File(notificationsFolder, "notifications.txt");

        try {
            dataWriter.writeNotification(notificationsFile, _notification);
        } catch (IOException e) {
            throw new WritingException(LOGGER, "Couldn't write to notifications.txt");
        }
    }

    public static ArrayList<String> getNotifications() throws ReadingException, LoadingException {
        String notificationsFolderPath;
        try {
            notificationsFolderPath = SystemModel.class.getResource(NOTIFICATIONS_FOLDER).getPath();
        } catch (NullPointerException e) {
            throw new LoadingException(LOGGER, "Notifications folder not found!");
        }
        
        File notificationsFolder = new File(notificationsFolderPath);
        File notificationsFile = new File(notificationsFolder, "notifications.txt");

        ArrayList<String> notifications;
        try {
            notifications = dataReader.readNotifications(notificationsFile);
        } catch (IOException e) {
            throw new ReadingException(LOGGER, "Couldn't read from notifications.txt");
        }


        return notifications;
    }
}
