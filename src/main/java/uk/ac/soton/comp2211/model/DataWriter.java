package uk.ac.soton.comp2211.model;

import java.io.File;
import java.io.IOException;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class DataWriter {

    private static DocumentBuilderFactory documentBuilderFactory;
    private static DocumentBuilder documentBuilder;
    private static Document document;

    private static TransformerFactory transformerFactory;
    private static Transformer transf;

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
        
        document = documentBuilder.parse(_newAirportFile);

        Element airportNode = document.createElement("airport");
        airportNode.setAttribute("name", _airport.getName());

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

                Element thresholdNode = document.createElement("threshold");
                thresholdNode.setTextContent(String.valueOf(runway.getDisplacedThreshold()));

                Element stopwayNode = document.createElement("stopway");
                stopwayNode.setTextContent(String.valueOf(runway.getStopway()));

                Element clearwayNode = document.createElement("clearway");
                clearwayNode.setTextContent(String.valueOf(runway.getClearway()));

                Element runwayNode = document.createElement("runway");
                runwayNode.setAttribute("id", String.valueOf(runwayID));
                runwayNode.appendChild(designatorNode);
                runwayNode.appendChild(toraNode);
                runwayNode.appendChild(todaNode);
                runwayNode.appendChild(asdaNode);
                runwayNode.appendChild(ldaNode);
                runwayNode.appendChild(thresholdNode);
                runwayNode.appendChild(stopwayNode);
                runwayNode.appendChild(clearwayNode);

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
;
        StreamResult result = new StreamResult(_file);

        transf.transform(source, result);
    }
}
