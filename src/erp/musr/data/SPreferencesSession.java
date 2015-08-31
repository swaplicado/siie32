/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

import erp.lib.xml.SXmlAttribute;
import erp.lib.xml.SXmlDocument;
import erp.lib.xml.SXmlElement;
import erp.lib.xml.SXmlUtilities;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 *
 * @author Juan Barajas
 */
public final class SPreferencesSession extends SXmlDocument {

    private int mnShowDetailBackground;

    public SPreferencesSession(String name) {
        super(name);
        mvXmlElements = new Vector<>();
        mnShowDetailBackground = 0;
    }
    
    public void setShowDetailBackground(int n) { mnShowDetailBackground = n; }
    
    public int getShowDetailBackground() { return mnShowDetailBackground; }

    @Override
    public void processXml(String xml) throws Exception {
        Document doc = SXmlUtilities.parseDocument(xml);
        NodeList nodeList = null;
        Node node = null;
        NamedNodeMap namedNodeMap = null;

        nodeList = SXmlUtilities.extractElements(doc, "DetailBackground");
        node = nodeList.item(0);
        namedNodeMap = node.getAttributes();

        mnShowDetailBackground = Integer.parseInt(SXmlUtilities.extractAttributeValue(namedNodeMap, "showDetailBackground", false));
    }
    
    @Override
    public void updateNodes() {
        clear();
        SXmlElement element = new SXmlElement("DetailBackground");
        SXmlAttribute att = new SXmlAttribute("showDetailBackground", mnShowDetailBackground);
        element.getXmlAttributes().add(att);
        
        mvXmlElements.add(element);        
    }
}
