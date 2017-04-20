/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import sa.lib.xml.SXmlAttribute;
import sa.lib.xml.SXmlElement;

/**
 *
 * @author Edwin Carmona
 */
public class SXmlImportFilePayment extends SXmlElement {

    public static final String NAME = "Payment";
    public static final String ATT_PAYMENT_ID = "PaymentId";
    public static final String ATT_PAYMENT_TIME_STAMP = "TimeStamp";
    public static final String ATT_PAYMENT_CUSTOMER_ID = "CustomerId";
    public static final String ATT_PAYMENT_ANALYST_ID = "AnalistId";
    public static final String ATT_PAYMENT_REFERENCE = "Reference";
    public static final String ATT_PAYMENT_CONCEPT = "Concept";
    public static final String ATT_PAYMENT_NUMBER_TX = "NumberTx";
    public static final String ATT_PAYMENT_TYPE = "PaymentType";
    public static final String ATT_PAYMENT_AMOUNT = "Amount";

    protected SXmlAttribute moPaymentId;
    protected SXmlAttribute moPaymentTimeStamp;
    protected SXmlAttribute moPaymentCustomerId;
    protected SXmlAttribute moPaymentAnalystId;
    protected SXmlAttribute moPaymentReference;
    protected SXmlAttribute moPaymentConcept;
    protected SXmlAttribute moPaymentNumberTx;
    protected SXmlAttribute moPaymentType;
    protected SXmlAttribute moPaymentAmount;

    public SXmlImportFilePayment() {
        super(NAME);

        moPaymentId = new SXmlAttribute(ATT_PAYMENT_ID);
        moPaymentTimeStamp = new SXmlAttribute(ATT_PAYMENT_TIME_STAMP);
        moPaymentCustomerId = new SXmlAttribute(ATT_PAYMENT_CUSTOMER_ID);
        moPaymentAnalystId = new SXmlAttribute(ATT_PAYMENT_ANALYST_ID);
        moPaymentReference = new SXmlAttribute(ATT_PAYMENT_REFERENCE);
        moPaymentConcept = new SXmlAttribute(ATT_PAYMENT_CONCEPT);
        moPaymentNumberTx = new SXmlAttribute(ATT_PAYMENT_NUMBER_TX);
        moPaymentType = new SXmlAttribute(ATT_PAYMENT_TYPE);
        moPaymentAmount = new SXmlAttribute(ATT_PAYMENT_AMOUNT);

        mvXmlAttributes.add(moPaymentId);
        mvXmlAttributes.add(moPaymentTimeStamp);
        mvXmlAttributes.add(moPaymentCustomerId);
        mvXmlAttributes.add(moPaymentAnalystId);
        mvXmlAttributes.add(moPaymentReference);
        mvXmlAttributes.add(moPaymentConcept);
        mvXmlAttributes.add(moPaymentNumberTx);
        mvXmlAttributes.add(moPaymentType);
        mvXmlAttributes.add(moPaymentAmount);
    }
}
