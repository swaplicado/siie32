/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sa.lib.xml.SXmlAttribute;
import sa.lib.xml.SXmlDocument;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author Juan Barajas
 */
public class SXmlDpsCfd extends SXmlDocument {

    public static final String NAME = "Dps";
    public static final String ATT_CONF = "confirmacion";
    public static final String ATT_TP_REL = "tipoRelacion";
    public static final String ATT_USO_CFDI = "usoCFDI";

    protected SXmlAttribute moConfirmation;
    protected SXmlAttribute moRelationType;
    protected SXmlAttribute moCfdiUse;
    
    public SXmlDpsCfd() {
        super(NAME);

        moConfirmation = new SXmlAttribute(ATT_CONF);
        moRelationType = new SXmlAttribute(ATT_TP_REL);
        moCfdiUse = new SXmlAttribute(ATT_USO_CFDI);

        mvXmlAttributes.add(moConfirmation);
        mvXmlAttributes.add(moRelationType);
        mvXmlAttributes.add(moCfdiUse);
    }

    @Override
    public void processXml(String xml) throws Exception {
        Document document = null;
        NodeList nodeList = null;
        Vector<Node> childNodes = null;
        NamedNodeMap namedNodeMap = null;

        clear();

        document = SXmlUtils.parseDocument(xml);
        nodeList = SXmlUtils.extractElements(document, SXmlDpsCfd.NAME);
        
        // Attributes layout
        
        moConfirmation.setValue(nodeList.item(0).getAttributes().getNamedItem(ATT_CONF).getNodeValue());
        moRelationType.setValue(nodeList.item(0).getAttributes().getNamedItem(ATT_TP_REL).getNodeValue());
        moCfdiUse.setValue(nodeList.item(0).getAttributes().getNamedItem(ATT_USO_CFDI).getNodeValue());
        
        mvXmlAttributes.add(moConfirmation);
        mvXmlAttributes.add(moRelationType);
        mvXmlAttributes.add(moCfdiUse);
        
        childNodes = SXmlUtils.extractChildElements(nodeList.item(0), "cce11");
        for (Node child : childNodes) {
            SXmlDpsCfdCce row = new SXmlDpsCfdCce();
            namedNodeMap = child.getAttributes();
            
            row.getAttribute(SXmlDpsCfdCce.ATT_MOT_TRAS).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_MOT_TRAS).getNodeValue());
            row.getAttribute(SXmlDpsCfdCce.ATT_TP_OPE).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_TP_OPE).getNodeValue());
            row.getAttribute(SXmlDpsCfdCce.ATT_CVE_PED).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_CVE_PED).getNodeValue());
            row.getAttribute(SXmlDpsCfdCce.ATT_CERT_ORIG).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_CERT_ORIG).getNodeValue());
            row.getAttribute(SXmlDpsCfdCce.ATT_NUM_CERT_ORIG).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_NUM_CERT_ORIG).getNodeValue());
            row.getAttribute(SXmlDpsCfdCce.ATT_SUB).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_SUB).getNodeValue());
            row.getAttribute(SXmlDpsCfdCce.ATT_TP_CAMB).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_TP_CAMB).getNodeValue());
            row.getAttribute(SXmlDpsCfdCce.ATT_TOT_USD).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_TOT_USD).getNodeValue());
            row.getAttribute(SXmlDpsCfdCce.ATT_NUM_EXP_CONF).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_NUM_EXP_CONF).getNodeValue());
            
            mvXmlElements.add(row);
        }
    }
}
