/*
 * Copyright 2010-2013 Software Aplicado SA de CV
 * All rights reserved.
 */

package erp.lib.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Sergio Flores
 */
public abstract class SXmlUtilities {

    public static void writeXml(final String xml, final String filePath) throws FileNotFoundException, UnsupportedEncodingException, IOException, Exception {
        BufferedWriter bw = null;

        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
        bw.write(xml);
        bw.close();
    }

    public static String readXml(final String filePath) throws FileNotFoundException, UnsupportedEncodingException, IOException, Exception {
        String line = "";
        String xml = "";
        BufferedReader br = null;

        br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));

        while (true) {
            line = br.readLine();
            if (line == null) {
                break;
            }
            else {
                xml += line;
            }
        }

        br.close();

        return xml;
    }

    public static Document parseDocument(final String xml) throws ParserConfigurationException, SAXException, IOException, Exception {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
    }

    public static boolean hasChildElement(final Node node, final String name) {
        boolean has = false;
        NodeList nodeList = node.getChildNodes();

        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeName().compareTo(name) == 0) {
                    has = true;
                    break;
                }
            }
        }

        return has;
    }

    public static NodeList extractElements(final Document document, final String name) throws Exception {
        NodeList nodeList = document.getElementsByTagName(name);

        if (nodeList == null) {
            throw new Exception("XML elements '" + name + "' not found!");
        }

        return nodeList;
    }

    public static Vector<Node> extractChildElements(final Node node, final String name) throws Exception {
        Vector<Node> children = new Vector<Node>();
        NodeList nodeList = node.getChildNodes();

        if (nodeList == null) {
            throw new Exception("XML children elements '" + name + "' not found!");
        }
        else {
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeName().compareTo(name) == 0) {
                    children.add(nodeList.item(i));
                }
            }

            if (children.size() == 0) {
                throw new Exception("XML children elements '" + name + "' not found!");
            }
        }

        return children;
    }

    public static String extractAttributeValue(final NamedNodeMap namedNodeMap, final String name, final boolean mandatory) throws Exception {
        String value = "";
        Node node = namedNodeMap.getNamedItem(name);

        if (node == null) {
            if (mandatory) {
                throw new Exception("XML element attribute '" + name + "' not found!");
            }
        }
        else {
            value = node.getNodeValue();
        }

        return value;
    }
}
