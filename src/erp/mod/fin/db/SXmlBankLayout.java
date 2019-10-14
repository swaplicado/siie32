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
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores
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
        clear();

        Document document = SXmlUtils.parseDocument(xml);
        NodeList nodeList = SXmlUtils.extractElements(document, SXmlBankLayout.NAME);
        
        // Bank layout attributes:
        
        NamedNodeMap namedNodeMap = nodeList.item(0).getAttributes();
        moLayId.setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, ATT_LAY_ID, true)));
        mvXmlAttributes.add(moLayId);
        
        // Payments:

        Vector<Node> paymentNodes = SXmlUtils.extractChildElements(nodeList.item(0), SXmlBankLayoutPayment.NAME);
        
        for (Node paymentNode : paymentNodes) {
            SXmlBankLayoutPayment xmlPayment = new SXmlBankLayoutPayment();
            NamedNodeMap paymentNodeMap = paymentNode.getAttributes();
            
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).setValue(SLibUtils.parseDouble(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_AMT, true)));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT_CY).setValue(SLibUtils.parseDouble(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_AMT_CY, true)));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CUR).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_CUR, true)));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_EXR).setValue(SLibUtils.parseDouble(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_EXR, true)));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE, false));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_REF).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_REF, false));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_CON).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_CON, false));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REF_ALPHA).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_REF_ALPHA, true));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CON).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_CON, true));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_VOU).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_VOU, true));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_ACC_TP).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_ACC_TP, true));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_DBT).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_DBT, true));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_CRD).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_CRD, true));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_BANK_CODE).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_BANK_CODE, true));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_DESCRIP).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_DESCRIP, true));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_SANT_BANK_CODE).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_SANT_BANK_CODE, true));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_BANK_CODE).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_BANK_CODE, true));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_NICK).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_NICK, true));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_KEY).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_KEY, false)));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED, true).compareTo("" + true) == 0);
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BP).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BP, true)));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BPB).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BPB, true)));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK, true)));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR, true)));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_PER).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_REC_PER, true)));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_BKC).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_REC_BKC, true)));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_REC_TP).setValue(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_REC_REC_TP, true));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_NUM).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_REC_NUM, true)));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_YEAR).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_YEAR, true)));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_NUM).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_NUM, true)));
            xmlPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_ROW_REF_REC).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(paymentNodeMap, SXmlBankLayoutPayment.ATT_LAY_ROW_REF_REC, true)));
            
            // Documents:
            
            if (SXmlUtils.hasChildElement(paymentNode, SXmlBankLayoutPaymentDoc.NAME)) {
                Vector<Node> paymentDocNodes = SXmlUtils.extractChildElements(paymentNode, SXmlBankLayoutPaymentDoc.NAME);
                
                for (Node paymentDocNode : paymentDocNodes) {
                    SXmlBankLayoutPaymentDoc xmlPaymentDoc = new SXmlBankLayoutPaymentDoc();
                    NamedNodeMap paymentDocNodeMap = paymentDocNode.getAttributes();
                    
                    xmlPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(paymentDocNodeMap, SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR, true)));
                    xmlPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(paymentDocNodeMap, SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC, true)));
                    xmlPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).setValue(SLibUtils.parseDouble(SXmlUtils.extractAttributeValue(paymentDocNodeMap, SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT, true)));
                    xmlPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT_CY).setValue(SLibUtils.parseDouble(SXmlUtils.extractAttributeValue(paymentDocNodeMap, SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT_CY, true)));
                    xmlPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_CUR).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(paymentDocNodeMap, SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_CUR, true)));
                    xmlPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EXR).setValue(SLibUtils.parseDouble(SXmlUtils.extractAttributeValue(paymentDocNodeMap, SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EXR, true)));
                    xmlPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_REF_REC).setValue(SXmlUtils.extractAttributeValue(paymentDocNodeMap, SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_REF_REC, true));
                    xmlPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_OBS).setValue(SXmlUtils.extractAttributeValue(paymentDocNodeMap, SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_OBS, true));
                    xmlPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EMAIL).setValue(SXmlUtils.extractAttributeValue(paymentDocNodeMap, SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EMAIL, false)); // optional

                    xmlPayment.getXmlElements().add(xmlPaymentDoc);
                }
            }
            
            mvXmlElements.add(xmlPayment);
        }
    }
}
