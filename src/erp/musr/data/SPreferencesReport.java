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
public final class SPreferencesReport extends SXmlDocument {
    
    public SPreferencesReport(String name) {
        super(name);
        mvXmlElements = new Vector<>();
    }

    @Override
    public void processXml(String xml) throws Exception {
    }
    
    @Override
    public void updateNodes() {
    }
}
