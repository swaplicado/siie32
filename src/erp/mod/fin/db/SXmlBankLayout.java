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
 * @author Juan Barajas
 */
public class SXmlBankLayout extends SXmlDocument {

    public static final String NAME = "Layout";
    public static final String ATT_LAY_ID = "LayId";

    protected SXmlAttribute moLayId;
    
    public SXmlBankLayout() {
        super(NAME);

        moLayId = new SXmlAttribute(ATT_LAY_ID);

        mvXmlAttributes.add(moLayId);
    }

    @Override
    public void processXml(String xml) throws Exception {
        Document document = null;
        NodeList nodeList = null;
        Vector<Node> childNodes = null;
        Vector<Node> grandChildNodes = null;
        NamedNodeMap namedNodeMap = null;

        clear();

        document = SXmlUtils.parseDocument(xml);
        nodeList = SXmlUtils.extractElements(document, SXmlBankLayout.NAME);
        
        // Attributes layout
        
        moLayId.setValue(nodeList.item(0).getAttributes().getNamedItem(ATT_LAY_ID).getNodeValue());
        mvXmlAttributes.add(moLayId);
        
        // Payments:

        childNodes = SXmlUtils.extractChildElements(nodeList.item(0), SXmlBankLayoutPayment.NAME);
        for (Node child : childNodes) {
            SXmlBankLayoutPayment row = new SXmlBankLayoutPayment();
            namedNodeMap = child.getAttributes();

            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).setValue(SLibUtils.parseDouble(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).getNodeValue()));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REF).setValue(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_REF).getNodeValue());
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CPT).setValue(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_CPT).getNodeValue());
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_VOU).setValue(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_VOU).getNodeValue());
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_ACC_TP).setValue(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_ACC_TP).getNodeValue());
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_BANK_CODE).setValue(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_BANK_CODE).getNodeValue());
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_DBT).setValue(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_DBT).getNodeValue());
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_CRD).setValue(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_CRD).getNodeValue());
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_DCRP).setValue(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_DCRP).getNodeValue());
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_SAN_BANK_CODE).setValue(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_SAN_BANK_CODE).getNodeValue());
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_BANK_CODE).setValue(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_BANK_CODE).getNodeValue());
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_NICK).setValue(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_NICK).getNodeValue());
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_KEY).setValue(SLibUtils.parseInt(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_KEY).getNodeValue()));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).setValue(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).getNodeValue().compareTo("" + true) == 0);
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).setValue(SLibUtils.parseInt(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).getNodeValue()));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).setValue(SLibUtils.parseInt(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).getNodeValue()));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR).setValue(SLibUtils.parseInt(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR).getNodeValue()));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_PER).setValue(SLibUtils.parseInt(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_PER).getNodeValue()));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_BKC).setValue(SLibUtils.parseInt(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_BKC).getNodeValue()));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_REC_TP).setValue(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_REC_TP).getNodeValue());
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_NUM).setValue(SLibUtils.parseInt(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_NUM).getNodeValue()));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_YEAR).setValue(SLibUtils.parseInt(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_YEAR).getNodeValue()));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_NUM).setValue(SLibUtils.parseInt(namedNodeMap.getNamedItem(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_NUM).getNodeValue()));

            // Documents:
            
            if (SXmlUtils.hasChildElement(child, SXmlBankLayoutPaymentDoc.NAME)) {
                grandChildNodes = SXmlUtils.extractChildElements(child, SXmlBankLayoutPaymentDoc.NAME);
                for (Node grandChild : grandChildNodes) {
                    SXmlBankLayoutPaymentDoc doc = new SXmlBankLayoutPaymentDoc();
                    namedNodeMap = grandChild.getAttributes();
                    
                    doc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR).setValue(SLibUtils.parseInt(namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR).getNodeValue()));
                    doc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC).setValue(SLibUtils.parseInt(namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC).getNodeValue()));
                    doc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).setValue(SLibUtils.parseDouble(namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).getNodeValue()));

                    row.getXmlElements().add(doc);
                }
            }
            
            mvXmlElements.add(row);
        }
    }
}
