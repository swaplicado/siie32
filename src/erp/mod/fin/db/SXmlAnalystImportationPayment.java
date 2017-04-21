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
public class SXmlAnalystImportationPayment extends SXmlElement {

    public static final String NAME = "Payment";
    public static final String ATT_PAYMENT_ID = "PaymentId";
    public static final String ATT_PAYMENT_IMPORTED = "PaymentImported";
    public static final String ATT_PAY_ANA_ACC_REF = "AccReeference";
    public static final String ATT_PAY_ANA_AMOUNT_CY = "AmountCy";
    public static final String ATT_PAY_ANA_EXCH_RATE = "ExchangeRate";
    public static final String ATT_PAY_ANA_AMOUNT = "Amount";
    public static final String ATT_PAY_ANA_REC_YEAR_ID = "RecordYearId";
    public static final String ATT_PAY_ANA_REC_PER_ID = "RecordPerId";
    public static final String ATT_PAY_ANA_REC_BKC_ID = "RecordBkcId";
    public static final String ATT_PAY_ANA_REC_TP_ID = "RecordTpRecId";
    public static final String ATT_PAY_ANA_REC_NUM_ID = "RecordNumId";
    public static final String ATT_PAY_ANA_BKC_YEAR = "BkcYearId";
    public static final String ATT_PAY_ANA_BKC_NUM = "BkcNum";

    protected SXmlAttribute moPaymentId;
    protected SXmlAttribute moPaymentImported;
    protected SXmlAttribute moPaymentReference;
    protected SXmlAttribute moPaymentAmountCurrency;
    protected SXmlAttribute moPaymentExchangeRate;
    protected SXmlAttribute moPaymentAmount;
    protected SXmlAttribute moPaymentRecYearId;
    protected SXmlAttribute moPaymentRecPeriodId;
    protected SXmlAttribute moPaymentRecBkcId;
    protected SXmlAttribute moPaymentRecTypeId;
    protected SXmlAttribute moPaymentRecNumberId;
    protected SXmlAttribute moPaymentBkcYear;
    protected SXmlAttribute moPaymentBkcNum;

    public SXmlAnalystImportationPayment() {
        super(NAME);

        moPaymentId = new SXmlAttribute(ATT_PAYMENT_ID);
        moPaymentImported = new SXmlAttribute(ATT_PAYMENT_IMPORTED);
        moPaymentReference = new SXmlAttribute(ATT_PAY_ANA_ACC_REF);
        moPaymentAmountCurrency = new SXmlAttribute(ATT_PAY_ANA_AMOUNT_CY);
        moPaymentExchangeRate = new SXmlAttribute(ATT_PAY_ANA_EXCH_RATE);
        moPaymentAmount = new SXmlAttribute(ATT_PAY_ANA_AMOUNT);
        moPaymentRecYearId = new SXmlAttribute(ATT_PAY_ANA_REC_YEAR_ID);
        moPaymentRecPeriodId = new SXmlAttribute(ATT_PAY_ANA_REC_PER_ID);
        moPaymentRecBkcId = new SXmlAttribute(ATT_PAY_ANA_REC_BKC_ID);
        moPaymentRecTypeId = new SXmlAttribute(ATT_PAY_ANA_REC_TP_ID);
        moPaymentRecNumberId = new SXmlAttribute(ATT_PAY_ANA_REC_NUM_ID);
        moPaymentBkcYear = new SXmlAttribute(ATT_PAY_ANA_BKC_YEAR);
        moPaymentBkcNum = new SXmlAttribute(ATT_PAY_ANA_BKC_NUM);

        mvXmlAttributes.add(moPaymentId);
        mvXmlAttributes.add(moPaymentImported);
        mvXmlAttributes.add(moPaymentReference);
        mvXmlAttributes.add(moPaymentAmountCurrency);
        mvXmlAttributes.add(moPaymentExchangeRate);
        mvXmlAttributes.add(moPaymentAmount);
        mvXmlAttributes.add(moPaymentRecYearId);
        mvXmlAttributes.add(moPaymentRecPeriodId);
        mvXmlAttributes.add(moPaymentRecBkcId);
        mvXmlAttributes.add(moPaymentRecTypeId);
        mvXmlAttributes.add(moPaymentRecNumberId);
        mvXmlAttributes.add(moPaymentBkcYear);
        mvXmlAttributes.add(moPaymentBkcNum);
    }
}
