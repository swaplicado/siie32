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
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.xml.SXmlAttribute;
import sa.lib.xml.SXmlDocument;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author Juan Barajas, Alfredo Pérez
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
            
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).setValue(SLibUtils.parseDouble(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_AMT, true)));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CUR).setValue(SLibUtils.parseDouble(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_CUR, false)));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_EXT_RATE).setValue(SLibUtils.parseDouble(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_EXT_RATE, false)));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REF_ALP).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_REF_ALP, true));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CPT).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_CPT, true));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_VOU).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_VOU, true));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_ACC_TP).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_ACC_TP, true));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_BANK_CODE).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_BANK_CODE, true));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_DBT).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_DBT, true));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_CRD).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_CRD, true));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_DCRP).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_DCRP, true));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_SAN_BANK_CODE).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_SAN_BANK_CODE, true));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_BANK_CODE).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_BANK_CODE, true));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_NICK).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_NICK, true));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_KEY).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_KEY, false)));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED, true).compareTo("" + true) == 0);
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BP).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BP, false)));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP, true)));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK, true)));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR, true)));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_PER).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_REC_PER, true)));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_BKC).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_REC_BKC, true)));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_REC_TP).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_REC_REC_TP, true));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_NUM).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_REC_NUM, true)));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_YEAR).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_YEAR, true)));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_NUM).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_NUM, true)));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE, false));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_REF).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_REF, false));
            row.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_CON).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_CON, false));
            
            // Documents:
            
            if (SXmlUtils.hasChildElement(child, SXmlBankLayoutPaymentDoc.NAME)) {
                grandChildNodes = SXmlUtils.extractChildElements(child, SXmlBankLayoutPaymentDoc.NAME);
                for (Node grandChild : grandChildNodes) {
                    SXmlBankLayoutPaymentDoc doc = new SXmlBankLayoutPaymentDoc();
                    namedNodeMap = grandChild.getAttributes();
                    
                    doc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR).setValue((namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR) == null ? SLibConsts.UNDEFINED : SLibUtils.parseInt(namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR).getNodeValue())));
                    doc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC).setValue((namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC) == null ? SLibConsts.UNDEFINED : SLibUtils.parseInt(namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC).getNodeValue())));
                    doc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).setValue((namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT) == null ? 0 : SLibUtils.parseDouble(namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).getNodeValue())));
                    doc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT_CY).setValue((namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT_CY) == null ? 0 : SLibUtils.parseDouble(namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT_CY).getNodeValue())));
                    doc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_CUR).setValue((namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_CUR) == null ? SLibConsts.UNDEFINED : SLibUtils.parseInt(namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_CUR).getNodeValue())));
                    doc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EXT_RATE).setValue((namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EXT_RATE) == null ? 0 : SLibUtils.parseDouble(namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EXT_RATE).getNodeValue())));
                    doc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_OBS).setValue((namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_OBS) == null ? "" : (namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_OBS).getNodeValue())));
                    doc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_REF_REC).setValue((namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_REF_REC) == null ? "" : (namedNodeMap.getNamedItem(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_REF_REC).getNodeValue())));

                    row.getXmlElements().add(doc);
                }
            }
            
            mvXmlElements.add(row);
        }
    }
}
