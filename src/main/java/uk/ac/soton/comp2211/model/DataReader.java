package uk.ac.soton.comp2211.model;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class DataReader {
    private static DocumentBuilderFactory documentBuilderFactory;
    private static DocumentBuilder documentBuilder;
    private static Document document;

    private static XPathFactory factory;
    private static XPath xpath;

    /**
     * Load XML file into memory so data can be extracted.
     * 
     * The XML file is validated against the corresponding XSD schema file.
     * 
     * @param _file
     * @throws Exception
     */
    public static void loadFile(File _xmlFile, File _xsdFile) throws Exception {
        if (documentBuilderFactory == null || documentBuilder == null) {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();

            factory = XPathFactory.newInstance();
            xpath = factory.newXPath();
        }

        if (validateSchema(_xmlFile, _xsdFile)) document = documentBuilder.parse(_xmlFile);
        else {
            document = null;

            throw new Exception(_xmlFile.getName() + " doesn't match the schema in " + _xsdFile.getName() + "!");
        }
    }

    private static boolean validateSchema(File _xmlFile, File _xsdFile) {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(_xsdFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(_xmlFile));
        } catch (IOException | SAXException e) {
            return false;
        }

        return true;
    }

    public static String getAirportName() throws XPathExpressionException {
        Node airport = (Node) xpath.evaluate("/airport", document, XPathConstants.NODE);
        return airport.getAttributes().getNamedItem("name").getTextContent();
    }

    public static Runway[] getRunways() throws XPathExpressionException {
        int runwayCount = ((Number) xpath.evaluate("count(//runway)", document, XPathConstants.NUMBER)).intValue();
        Runway[] runways = new Runway[runwayCount];

        int runwayIndex = 0;
        int tarmacCount = ((Number) xpath.evaluate("count(//tarmac)", document, XPathConstants.NUMBER)).intValue();
        for (int i = 1; i <= tarmacCount; i++) {
            Tarmac tarmac = new Tarmac(i, null);

            int tarmacRunwayCount = ((Number) xpath.evaluate("count(//tarmac[@id='" + i + "']/runway)", document, XPathConstants.NUMBER)).intValue();
            for (int j = 0; i <= tarmacRunwayCount; i++) {
                String designator = (String) xpath.evaluate("//tarmac[@id='" + i + "']/runway[@id='" + j + "']/designator", document, XPathConstants.STRING);
                int tora = ((Number) xpath.evaluate("//tarmac[@id='" + i + "']/runway[@id='" + j + "']/tora", document, XPathConstants.NUMBER)).intValue();
                int toda = ((Number) xpath.evaluate("//tarmac[@id='" + i + "']/runway[@id='" + j + "']/toda", document, XPathConstants.NUMBER)).intValue();
                int asda = ((Number) xpath.evaluate("//tarmac[@id='" + i + "']/runway[@id='" + j + "']/asda", document, XPathConstants.NUMBER)).intValue();
                int lsa = ((Number) xpath.evaluate("//tarmac[@id='" + i + "']/runway[@id='" + j + "']/lsa", document, XPathConstants.NUMBER)).intValue();
                int threshold = ((Number) xpath.evaluate("//tarmac[@id='" + i + "']/runway[@id='" + j + "']/threshold", document, XPathConstants.NUMBER)).intValue();

                RunwayValues runwayValues = new RunwayValues(tora, toda, asda, lsa);
                Runway runway = new Runway(designator, tarmac, runwayValues, threshold);

                runways[runwayIndex] = runway;
                runwayIndex++;
            }
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
