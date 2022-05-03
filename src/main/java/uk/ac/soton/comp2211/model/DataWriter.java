package uk.ac.soton.comp2211.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;

import java.awt.image.RenderedImage;

import javafx.embed.swing.SwingFXUtils;

public class DataWriter {

    protected static final Logger LOGGER = LogManager.getLogger(DataWriter.class);

    private static DocumentBuilderFactory documentBuilderFactory;
    private static DocumentBuilder documentBuilder;
    private static Document document;

    private static TransformerFactory transformerFactory;
    private static Transformer transf;

    private static final String NOTIFICATIONS_FOLDER = "/notifications";

    private static void setupDataWriter() throws ParserConfigurationException, TransformerConfigurationException {
        if (documentBuilderFactory == null)
            documentBuilderFactory = DocumentBuilderFactory.newInstance();

        if (documentBuilder == null)
            documentBuilder = documentBuilderFactory.newDocumentBuilder();

        if (transformerFactory == null)
            transformerFactory = TransformerFactory.newInstance();

        if (transf == null) {
            transf = transformerFactory.newTransformer();

            transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transf.setOutputProperty(OutputKeys.INDENT, "yes");
            transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        }
    }

    public static void writeAirport(Airport _airport, File _newAirportFile) throws SAXException, IOException, TransformerException, ParserConfigurationException {
        setupDataWriter();

        document = documentBuilder.newDocument();

        Element airportNode = document.createElement("airport");
        airportNode.setAttribute("name", _airport.getName());
        document.appendChild(airportNode);

        int tarmacID = 1;
        for (Tarmac tarmac : _airport.getTarmacs()) {
            Element tarmacNode = document.createElement("tarmac");
            tarmacNode.setAttribute("id", String.valueOf(tarmacID));

            int runwayID = 1;
            for (Runway runway : tarmac.getRunways()) {
                Element designatorNode = document.createElement("designator");
                designatorNode.setTextContent(runway.getRunwayDesignator());

                Element toraNode = document.createElement("tora");
                toraNode.setTextContent(String.valueOf(runway.getOriginalValues().getTORA()));

                Element todaNode = document.createElement("toda");
                todaNode.setTextContent(String.valueOf(runway.getOriginalValues().getTODA()));

                Element asdaNode = document.createElement("asda");
                asdaNode.setTextContent(String.valueOf(runway.getOriginalValues().getASDA()));

                Element ldaNode = document.createElement("lda");
                ldaNode.setTextContent(String.valueOf(runway.getOriginalValues().getLDA()));

                // Element thresholdNode = document.createElement("threshold");
                // thresholdNode.setTextContent(String.valueOf(runway.getOriginalValues().getDT()));

                // Element stopwayNode = document.createElement("stopway");
                // stopwayNode.setTextContent(String.valueOf(runway.getOriginalValues().getStopway()));

                // Element clearwayNode = document.createElement("clearway");
                // clearwayNode.setTextContent(String.valueOf(runway.getOriginalValues().getClearway()));

                Element runwayNode = document.createElement("runway");
                runwayNode.setAttribute("id", String.valueOf(runwayID));
                runwayNode.appendChild(designatorNode);
                runwayNode.appendChild(toraNode);
                runwayNode.appendChild(todaNode);
                runwayNode.appendChild(asdaNode);
                runwayNode.appendChild(ldaNode);
                // runwayNode.appendChild(thresholdNode);
                // runwayNode.appendChild(stopwayNode);
                // runwayNode.appendChild(clearwayNode);

                tarmacNode.appendChild(runwayNode);

                runwayID++;
            }

            airportNode.appendChild(tarmacNode);
            tarmacID++;
        }

        writeFile(_newAirportFile);
    }

    public static void writeObstacle(Obstacle _obstacle, File _obstacleFile) throws TransformerException, SAXException, IOException, ParserConfigurationException {
        setupDataWriter();

        document = documentBuilder.parse(_obstacleFile);
        
        Element obstacleName = document.createElement("name");
        obstacleName.appendChild(document.createTextNode(_obstacle.getName()));

        Element obstacleLength = document.createElement("length");
        obstacleLength.appendChild(document.createTextNode(String.valueOf(_obstacle.getLength())));

        Element obstacleWidth = document.createElement("width");
        obstacleWidth.appendChild(document.createTextNode(String.valueOf(_obstacle.getWidth())));

        Element obstacleHeight = document.createElement("height");
        obstacleHeight.appendChild(document.createTextNode(String.valueOf(_obstacle.getHeight())));

        int obstacleCount = document.getElementsByTagName("obstacle").getLength();

        Element newObstacle = document.createElement("obstacle");
        newObstacle.setAttribute("id", String.valueOf(obstacleCount));
        newObstacle.appendChild(obstacleName);
        newObstacle.appendChild(obstacleLength);
        newObstacle.appendChild(obstacleWidth);
        newObstacle.appendChild(obstacleHeight);

        Node root = document.getElementsByTagName("obstacles").item(0);
        root.appendChild(newObstacle);

        writeFile(_obstacleFile);
    }

    private static void writeFile(File _file) throws TransformerException {
        DOMSource source = new DOMSource(document);

        StreamResult result = new StreamResult(_file);

        transf.transform(source, result);
    }

    public static void writeCalculationLog(String _airportName, String _runwayDesignator, String[] _calculations, File _file) throws IOException {
        String fileContent = "";
        fileContent += "Date: " + LocalDate.now();
        fileContent += "\nTime: " + LocalTime.now();

        fileContent += "\n\nAirport: " + _airportName;
        fileContent += "\nRunway: " + _runwayDesignator;

        fileContent += "\nCalculations:\n";
        for (String calculation : _calculations)
            fileContent += "\n\t" + calculation;

        FileWriter fileWriter;
        fileWriter = new FileWriter(_file);
        fileWriter.write(fileContent);
        fileWriter.close();
    }

    /**
     * Append a new notification to notifications.txt
     * @param notif notification to be added
     */
    public static void writeNotification(String notif) {
        File notifFile;
        
        try {
            String folder = DataReader.class.getResource(NOTIFICATIONS_FOLDER).getPath();
            notifFile = new File(folder, "notifications.txt");
            FileWriter fw = new FileWriter(notifFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            
            bw.write(notif);
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException e) {
            LOGGER.error("Couldn't open notifications file for writing. {}", e.getMessage());
        }
    }

    public static void savePicture(Canvas _canvas, File _file) {
        try {
            WritableImage writableImage = new WritableImage((int) _canvas.getWidth(), (int) _canvas.getHeight());
            _canvas.snapshot(null, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(renderedImage, "png", file);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error!");
        }
    }
}
