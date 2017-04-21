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
public class SXmlImportFile extends SXmlDocument {

    public static final String NAME = "Layout";
    public static final String ATT_ACC_ID = "AccountId";
    public static final String ATT_ACC = "Account";
    public static final String ATT_CUR_ID = "CurrencyId";
    public static final String ATT_CUR_CODE = "CurrencyCode";

    protected SXmlAttribute moAccountId;
    protected SXmlAttribute moAccount;
    protected SXmlAttribute moCurrencyId;
    protected SXmlAttribute moCurrencyCode;
    
    public SXmlImportFile() {
        super(NAME);

        moAccountId = new SXmlAttribute(ATT_ACC_ID);
        moAccount = new SXmlAttribute(ATT_ACC);
        moCurrencyId = new SXmlAttribute(ATT_CUR_ID);
        moCurrencyCode = new SXmlAttribute(ATT_CUR_CODE);

        mvXmlAttributes.add(moAccountId);
        mvXmlAttributes.add(moAccount);
        mvXmlAttributes.add(moCurrencyId);
        mvXmlAttributes.add(moCurrencyCode);
    }

    @Override
    public void processXml(String xml) throws Exception {
        Document document = null;
        NodeList nodeList = null;
        Vector<Node> childNodes = null;        
        NamedNodeMap namedNodeMap = null;

        clear();

        document = SXmlUtils.parseDocument(xml);
        nodeList = SXmlUtils.extractElements(document, SXmlImportFile.NAME);
        
        // Attributes layout
        
        moAccountId.setValue(nodeList.item(0).getAttributes().getNamedItem(ATT_ACC_ID).getNodeValue());
        moAccount.setValue(nodeList.item(0).getAttributes().getNamedItem(ATT_ACC).getNodeValue());
        moCurrencyId.setValue(nodeList.item(0).getAttributes().getNamedItem(ATT_CUR_ID).getNodeValue());
        moCurrencyCode.setValue(nodeList.item(0).getAttributes().getNamedItem(ATT_CUR_CODE).getNodeValue());
        mvXmlAttributes.add(moAccountId);
        mvXmlAttributes.add(moAccount);
        mvXmlAttributes.add(moCurrencyId);
        mvXmlAttributes.add(moCurrencyCode);
        
        // Payments:

        childNodes = SXmlUtils.extractChildElements(nodeList.item(0), SXmlImportFilePayment.NAME);
        for (Node child : childNodes) {
            SXmlImportFilePayment row = new SXmlImportFilePayment();
            namedNodeMap = child.getAttributes();
            
            row.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_ID).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlImportFilePayment.ATT_PAYMENT_ID, true)));
            row.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_TIME_STAMP).setValue(erp.mod.fin.util.SFinUtils.stringToDateTime(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlImportFilePayment.ATT_PAYMENT_TIME_STAMP, false)));
            row.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_CUSTOMER_ID).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlImportFilePayment.ATT_PAYMENT_CUSTOMER_ID, false)));
            row.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_ANALYST_ID).setValue(SLibUtils.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlImportFilePayment.ATT_PAYMENT_ANALYST_ID, true)));
            row.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_REFERENCE).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlImportFilePayment.ATT_PAYMENT_REFERENCE, true));
            row.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_CONCEPT).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlImportFilePayment.ATT_PAYMENT_CONCEPT, true));
            row.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_NUMBER_TX).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlImportFilePayment.ATT_PAYMENT_NUMBER_TX, true));
            row.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_TYPE).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlImportFilePayment.ATT_PAYMENT_TYPE, true));
            row.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_AMOUNT).setValue(SLibUtils.parseDouble(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlImportFilePayment.ATT_PAYMENT_AMOUNT, true)));
            
            mvXmlElements.add(row);
        }
    }
}
