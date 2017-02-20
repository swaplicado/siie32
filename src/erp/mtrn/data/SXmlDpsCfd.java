/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import org.w3c.dom.Document;
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
    }
}
