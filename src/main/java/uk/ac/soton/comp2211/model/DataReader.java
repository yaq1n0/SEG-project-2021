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
     * @param _xmlFile xml
     * @param _xsdFile xsd
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

    public static Tarmac[] getTarmacs() throws XPathExpressionException {
        int tarmacCount = ((Number) xpath.evaluate("count(//runway)", document, XPathConstants.NUMBER)).intValue();
        Tarmac[] tarmacs = new Tarmac[tarmacCount];

        for (int tarmacID = 1; tarmacID <= tarmacCount; tarmacID++) {
            Tarmac tarmac = new Tarmac(tarmacID);
            tarmac.setRunways(getTarmacRunways(tarmac));
            tarmacs[tarmacID - 1] = tarmac;
        }

        return tarmacs;
    }

    private static Runway[] getTarmacRunways(Tarmac _tarmac) throws XPathExpressionException {
        int runwayCount = ((Number) xpath.evaluate("count(//tarmac[@id='" + _tarmac.getID()
                                                    + "']/runway)", document, XPathConstants.NUMBER)).intValue();
        Runway[] runways = new Runway[runwayCount];

        for (int runwayID = 1; runwayID <= runwayCount; runwayID++) {
            String designator = (String) xpath.evaluate("//tarmac[@id='" + _tarmac.getID()
                                                        + "']/runway[@id='" + runwayID
                                                        + "']/designator", document, XPathConstants.STRING);
            int tora = ((Number) xpath.evaluate("//tarmac[@id='" + _tarmac.getID()
                                                + "']/runway[@id='" + runwayID
                                                + "']/tora", document, XPathConstants.NUMBER)).intValue();
            int toda = ((Number) xpath.evaluate("//tarmac[@id='" + _tarmac.getID()
                                                + "']/runway[@id='" + runwayID 
                                                + "']/toda", document, XPathConstants.NUMBER)).intValue();
            int asda = ((Number) xpath.evaluate("//tarmac[@id='" + _tarmac.getID()
                                                + "']/runway[@id='" + runwayID 
                                                + "']/asda", document, XPathConstants.NUMBER)).intValue();
            int lda = ((Number) xpath.evaluate("//tarmac[@id='" + _tarmac.getID()
                                                + "']/runway[@id='" + runwayID 
                                                + "']/lda", document, XPathConstants.NUMBER)).intValue();
            int threshold = ((Number) xpath.evaluate("//tarmac[@id='" + _tarmac.getID()
                                                    + "']/runway[@id='" + runwayID 
                                                    + "']/threshold", document, XPathConstants.NUMBER)).intValue();

            RunwayValues values = new RunwayValues(tora, toda, asda, lda);
            
            runways[runwayID - 1] = new Runway(designator, _tarmac, values, threshold);
        }

        return runways;
    }

    public static Obstacle[] getObstacles() throws SizeException {
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
