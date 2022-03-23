package uk.ac.soton.comp2211.model;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

import uk.ac.soton.comp2211.exceptions.ExtractionException;
import uk.ac.soton.comp2211.exceptions.LoadingException;
import uk.ac.soton.comp2211.exceptions.SchemaException;
import uk.ac.soton.comp2211.exceptions.SizeException;

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
     * @throws SchemaException
     */
    public static void loadFile(File _xmlFile, File _xsdFile) throws SchemaException, LoadingException {
        if (documentBuilderFactory == null || documentBuilder == null) {
            try {
                documentBuilderFactory = DocumentBuilderFactory.newInstance();
                documentBuilder = documentBuilderFactory.newDocumentBuilder();

                factory = XPathFactory.newInstance();
                xpath = factory.newXPath();
            } catch (Exception e) {
                throw new LoadingException("Couldn't create XML document builder!");
            }
        }

        if (validateSchema(_xmlFile, _xsdFile)) {
            try { document = documentBuilder.parse(_xmlFile); }
            catch (Exception e) { 
                document = null;
                
                throw new LoadingException("Can't parse XML file: " + _xmlFile); 
            } 
        } else {
            document = null;

            throw new SchemaException(_xmlFile.getName() + " doesn't match the schema in " + _xsdFile.getName() + "!");
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

    public static String getAirportName() throws ExtractionException {
        try {
            Node airport = (Node) xpath.evaluate("/airport", document, XPathConstants.NODE);
            return airport.getAttributes().getNamedItem("name").getTextContent();
        } catch (XPathExpressionException e) {
            throw new ExtractionException("Failed to extract airport name from XML file!");
        }
    }

    public static Tarmac[] getTarmacs() throws XPathExpressionException {
        int tarmacCount = ((Number) xpath.evaluate("count(//tarmac)", document, XPathConstants.NUMBER)).intValue();
        Tarmac[] tarmacs = new Tarmac[tarmacCount];
        System.out.println("TarmacCount " + tarmacCount);
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
        System.out.println(runwayCount);

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

            // TODO: The following aren't needed, they are derived from the rest
            /**
            int threshold = ((Number) xpath.evaluate("//tarmac[@id='" + _tarmac.getID()
                                                    + "']/runway[@id='" + runwayID 
                                                    + "']/threshold", document, XPathConstants.NUMBER)).intValue();
            int stopway = ((Number) xpath.evaluate("//tarmac[@id='" + _tarmac.getID()
                                                    + "']/runway[@id='" + runwayID
                                                    + "']/stopway", document, XPathConstants.NUMBER)).intValue();
            int clearway = ((Number) xpath.evaluate("//tarmac[@id='" + _tarmac.getID()
                                                    + "']/runway[@id='" + runwayID
                                                    + "']/clearway", document, XPathConstants.NUMBER)).intValue();
             **/

            RunwayValues values = new RunwayValues(tora, toda, asda, lda);
            
            runways[runwayID - 1] = new Runway(designator, _tarmac, values);
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
