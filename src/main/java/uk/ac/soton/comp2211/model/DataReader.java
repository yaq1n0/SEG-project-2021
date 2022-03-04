package uk.ac.soton.comp2211.model;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DataReader {
    private static DocumentBuilderFactory documentBuilderFactory;
    private static DocumentBuilder documentBuilder;
    private static Document document;

    /**
     * Load XML file into memory so data can be extracted.
     * 
     * @param _file
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static void loadFile(File _file) throws ParserConfigurationException, SAXException, IOException {
        if (documentBuilderFactory == null || documentBuilder == null) {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        }

        document = documentBuilder.parse(_file);
    }


    public static String getAirportName() {
        return document.getElementsByTagName("airport")
                        .item(0)
                        .getAttributes()
                        .getNamedItem("name")
                        .getTextContent();    
    }

    public static Runway[] getRunways() {
        int runwayCount = document.getElementsByTagName("runway").getLength();

        Runway[] runways = new Runway[runwayCount];
        for (int i = 0; i < runwayCount; i++) {
            String designator = document.getElementsByTagName("designator").item(i).getTextContent();
            int tora = Integer.valueOf(document.getElementsByTagName("tora").item(i).getTextContent());
            int toda = Integer.valueOf(document.getElementsByTagName("toda").item(i).getTextContent());
            int asda = Integer.valueOf(document.getElementsByTagName("asda").item(i).getTextContent());
            int lsa = Integer.valueOf(document.getElementsByTagName("lsa").item(i).getTextContent());
            int threshold = Integer.valueOf(document.getElementsByTagName("threshold").item(i).getTextContent());

            runways[i] = new Runway(designator, new RunwayValues(tora, toda, asda, lsa, threshold));
        }

        return runways;
    }

    public static Obstacle[] getObstacles() {
        int obstacleCount = document.getElementsByTagName("obstacle").getLength();

        Obstacle[] obstacles = new Obstacle[obstacleCount];
        for (int i = 0; i < obstacleCount; i++) {
            String name = document.getElementsByTagName("name").item(i).getTextContent();
            int length = Integer.valueOf(document.getElementsByTagName("length").item(i).getTextContent());
            int width = Integer.valueOf(document.getElementsByTagName("width").item(i).getTextContent());
            int height = Integer.valueOf(document.getElementsByTagName("height").item(i).getTextContent());

            obstacles[i] = new Obstacle(name, length, width, height);
        }

        return obstacles;
    }
}
