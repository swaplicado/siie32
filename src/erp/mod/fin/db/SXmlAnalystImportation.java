/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sa.lib.SLibUtils;
import sa.lib.xml.SXmlAttribute;
import sa.lib.xml.SXmlDocument;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author Edwin Carmona
 */
public class SXmlAnalystImportation extends SXmlDocument {

    public static final String NAME = "Layout";
    public static final String ATT_ANALYST_ID = "AnalystId";

    protected SXmlAttribute moAnalystId;
    
    public SXmlAnalystImportation() {
        super(NAME);

        moAnalystId = new SXmlAttribute(ATT_ANALYST_ID);

        mvXmlAttributes.add(moAnalystId);
    }

    @Override
    public void processXml(String xml) throws Exception {
        Document document = null;
        NodeList nodeList = null;
        Vector<Node> childNodes = null;        
        NamedNodeMap namedNodeMap = null;

        clear();

        document = SXmlUtils.parseDocument(xml);
        nodeList = SXmlUtils.extractElements(document, SXmlAnalystImportation.NAME);
        
        // Attributes layout
        
        moAnalystId.setValue(nodeList.item(0).getAttributes().getNamedItem(ATT_ANALYST_ID).getNodeValue());
        mvXmlAttributes.add(moAnalystId);
        
        // Payments:

        childNodes = SXmlUtils.extractChildElements(nodeList.item(0), SXmlAnalystImportationPayment.NAME);
        for (Node child : childNodes) {
            SXmlAnalystImportationPayment row = new SXmlAnalystImportationPayment();
            namedNodeMap = child.getAttributes();
            
            row.getAttribute(SXmlAnalystImportationPayment.ATT_PAYMENT_ID).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlAnalystImportationPayment.ATT_PAYMENT_ID, true)));
            row.getAttribute(SXmlAnalystImportationPayment.ATT_PAYMENT_IMPORTED).setValue(Boolean.parseBoolean(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlAnalystImportationPayment.ATT_PAYMENT_IMPORTED, true)));
            row.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_ACC_REF).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlAnalystImportationPayment.ATT_PAY_ANA_ACC_REF, true));
            row.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_AMOUNT_CY).setValue(SLibUtils.parseDouble(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlAnalystImportationPayment.ATT_PAY_ANA_AMOUNT_CY, true)));
            row.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_EXCH_RATE).setValue(SLibUtils.parseDouble(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlAnalystImportationPayment.ATT_PAY_ANA_EXCH_RATE, false)));
            row.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_AMOUNT).setValue(SLibUtils.parseDouble(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlAnalystImportationPayment.ATT_PAY_ANA_AMOUNT, false)));
            row.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_YEAR_ID).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_YEAR_ID, true)));
            row.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_PER_ID).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_PER_ID, true)));
            row.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_BKC_ID).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_BKC_ID, true)));
            row.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_TP_ID).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_TP_ID, true));
            row.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_NUM_ID).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_NUM_ID, true)));
            row.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_BKC_YEAR).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlAnalystImportationPayment.ATT_PAY_ANA_BKC_YEAR, true)));
            row.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_BKC_NUM).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlAnalystImportationPayment.ATT_PAY_ANA_BKC_NUM, true)));
            
            mvXmlElements.add(row);
        }
    }
}
