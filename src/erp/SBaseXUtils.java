package erp;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class SBaseXUtils {

    private static final SimpleDateFormat mDateFormatter = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");

    private static SBaseXClient BaseXClient = null;
    private static DocumentBuilder DocumentBuilder = null;
    private static XPath XPath = null;

    /**
     * Creates a {@link SBaseXClient} connection instance.
     *
     * @param host The BaseX database host address
     * @param port The BaseX database port number
     * @param username The BaseX database user name
     * @param password The BaseX database user password
     * @return {@link SBaseXClient} singleton instance
     */
    public static SBaseXClient getBaseXSessionInstance(final String host, final int port, final String username,
            final String password) throws IOException {

        if (BaseXClient == null) {
            BaseXClient = new SBaseXClient(host, port, username, password);
        }

        return BaseXClient;
    }

    /**
     * Executes the provided BaseX query using the SBaseXClient instance.
     *
     * @param session The {@link SBaseXClient} that represents the connection to
     * the BaseX database
     * @param input BaseX query string to be executed
     * @return ArrayList instance containing the query results as Strings
     */
    public static ArrayList<String> executeBaseXQuery(SBaseXClient session, String input) throws Exception {
        if (BaseXClient == null) {
            throw new Exception("BaseX Client not initialized.");
        }

        ArrayList<String> results = new ArrayList<>();

        SBaseXClient.Query query = session.query(input);
        while (query.more()) {
            String nextValue = query.next();
            results.add(nextValue);
        }

        return results;
    }

    /**
     * Retrieves a value contained within an XML structure using a
     * {@link XPathExpression}.
     *
     * @param docString String representing the XML document
     * @param expression {@link XPathExpression} used to traverse the XML node
     * structure and locate the desired value
     * @return String instance containing the value indicated by the {
     * @param expression}
     */
    public static String getXmlValue(String docString, String expression) throws IOException, SAXException, XPathExpressionException, ParserConfigurationException {
        initHelperClasses();

        InputSource source = new InputSource(new StringReader(docString));

        Document document = DocumentBuilder.parse(source);

        return XPath.evaluate(expression, document);
    }

    /**
     * Retrieves an XML Element contained within an XML document using a
     * {@link XPathExpression}.
     *
     * @param docString String representing the XML document
     * @param expression {@link XPathExpression} used to traverse the XML node
     * structure and locate the desired value
     * @return String instance containing the element indicated by the {
     * @param expression}
     */
    public static String getXmlElement(String docString, String expression) throws TransformerException, IOException, SAXException, XPathExpressionException, ParserConfigurationException {
        initHelperClasses();

        InputSource source = new InputSource(new StringReader(docString));

        Document document = DocumentBuilder.parse(source);

        Element docElement = (Element) XPath.compile(expression).evaluate(document, XPathConstants.NODE);

        return parseXmlElement(docElement);
    }

    /**
     * Escapes the XMLs special characters contained in the document body string
     * to prevent parsing errors
     *
     * @param xmlDoc String representing the xml document body.
     * @return
     */
    public static String escapeSpecialCharacters(String xmlDoc) {
        xmlDoc = xmlDoc.replaceAll("\"", "\'");
        xmlDoc = xmlDoc.replaceAll("&amp;", "&amp;amp;");
        xmlDoc = xmlDoc.replaceAll("&apos;", "&amp;apos;");
        xmlDoc = xmlDoc.replaceAll("&quot;", "&amp;quot;");
        xmlDoc = xmlDoc.replaceAll("&lt;", "&amp;lt;");
        xmlDoc = xmlDoc.replaceAll("&gt;", "&amp;gt;");

        return xmlDoc;
    }

    /**
     *
     * Extracts the database host name from a MySQL URL string
     *
     * @param databseURL The MySQL database URL string.
     * @return
     */
    public static String getDbHostFromUrl(String databseURL) {
        String[] splitResult = databseURL.split("/");

        return splitResult[2].split(":")[0];
    }

    /**
     *
     * Extracts the database name from a MySQL URL string
     *
     * @param databseURL The MySQL database URL string.
     * @return
     */
    public static String getDbNameFromUrl(String databseURL) {
        String[] splitResult = databseURL.split("/");

        String databaseName = splitResult[3];
        if (databaseName.contains("?")) {
            databaseName = splitResult[3].split("\\?")[0];
        }

        return databaseName;
    }

    /**
     * Write the provided error String to a text file in the root directory
     *
     * @param errorString
     */
    public static void logError(String errorString) {
        try (FileWriter fw = new FileWriter("BaseXErrorLog.txt", true)) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String body = mDateFormatter.format(timestamp) + ": " + errorString + "\r\n";

            fw.write(body);
            fw.close();
        } catch (IOException ex) {
            System.err.println("Could not write error to log: " + ex.getMessage());
        }
    }

    /**
     * Helper method used to parse an XML {@link Element} object and return its
     * String representation.
     *
     * @param element {@link Element} to be parsed
     * @return String representation of the {@link Element} object
     */
    private static String parseXmlElement(Element element) throws TransformerException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        Source sources = new DOMSource(element);
        Result target = new StreamResult(out);
        transformer.transform(sources, target);
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + out.toString();
    }

    /**
     * Helper method used to initialized required class instances needed to
     * handle XML content. *
     */
    private static void initHelperClasses() throws ParserConfigurationException {
        if (DocumentBuilder == null) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder = dbf.newDocumentBuilder();
        }

        if (XPath == null) {
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath = xpathFactory.newXPath();
        }
    }

    /**
     * Generate a unique uuid string that does not already exist in the database represented by the connection object provided.
     *
     * @param connection
     * @return New UUID String.
     * @throws SQLException
     */
    public static String generateUniqueXmlId(Connection connection) throws SQLException {
        String newUuid;
        boolean uuidExists;
        do {
            newUuid = UUID.randomUUID().toString();
            String verifyColumnQuery = "SELECT count(*) FROM trn_cfd WHERE doc_xml_uuid = '" + newUuid + "' LIMIT 1";
            
            ResultSet checkResult = connection.createStatement().executeQuery(verifyColumnQuery);
            checkResult.next();
            uuidExists = checkResult.getInt("count(*)") == 1;
        } while (uuidExists);

        return newUuid;        
    }

}
