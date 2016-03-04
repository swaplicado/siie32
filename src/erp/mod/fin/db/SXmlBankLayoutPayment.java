/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import sa.lib.xml.SXmlAttribute;
import sa.lib.xml.SXmlElement;

/**
 *
 * @author Juan Barajas
 */
public class SXmlBankLayoutPayment extends SXmlElement {

    public static final String NAME = "Pay";
    public static final String ATT_LAY_PAY_AMT = "PayAmount";
    public static final String ATT_LAY_PAY_REF = "PayReference";
    public static final String ATT_LAY_PAY_CPT = "PayConcept";
    public static final String ATT_LAY_PAY_HSBC_FIS_VOU = "HsbcFiscalVoucher";
    public static final String ATT_LAY_PAY_HSBC_ACC_TP = "HsbcAccountType";
    public static final String ATT_LAY_PAY_HSBC_BANK_CODE = "HsbcBankCode";
    public static final String ATT_LAY_PAY_HSBC_FIS_ID_DBT = "HsbcFiscalIdDebit";
    public static final String ATT_LAY_PAY_HSBC_FIS_ID_CRD = "HsbcFiscalIdCredit";
    public static final String ATT_LAY_PAY_HSBC_DCRP = "HsbcDescription";
    public static final String ATT_LAY_PAY_SAN_BANK_CODE = "SantBankCode";
    public static final String ATT_LAY_PAY_BAJIO_BANK_CODE = "BajioBankCode";
    public static final String ATT_LAY_PAY_BAJIO_NICK = "BajioNick";
    public static final String ATT_LAY_PAY_BANK_KEY = "BankKey";
    public static final String ATT_LAY_PAY_APPLIED = "PayApplied";
    public static final String ATT_LAY_PAY_BANK_BP = "PayBankBizPartnertId";
    public static final String ATT_LAY_PAY_BANK_BANK = "PayBankBankId";
    public static final String ATT_LAY_PAY_REC_YEAR = "PayRecordYearId";
    public static final String ATT_LAY_PAY_REC_PER = "PayRecordPeriodId";
    public static final String ATT_LAY_PAY_REC_BKC = "PayRecordBkcId";
    public static final String ATT_LAY_PAY_REC_REC_TP = "PayRecordRecordTypeId";
    public static final String ATT_LAY_PAY_REC_NUM = "PayRecordNumberId";
    public static final String ATT_LAY_PAY_BKK_YEAR = "PayBkkYearId";
    public static final String ATT_LAY_PAY_BKK_NUM = "PayBkkNumberId";

    protected SXmlAttribute moPayReference;
    protected SXmlAttribute moPayConcept;
    protected SXmlAttribute moPayAmount;
    protected SXmlAttribute moPayHsbcFiscalVoucher;
    protected SXmlAttribute moPayHsbcAccountType;
    protected SXmlAttribute moPayHsbcBankCode;
    protected SXmlAttribute moPayHsbcFiscalIdDebit;
    protected SXmlAttribute moPayHsbcFiscalIdCredit;
    protected SXmlAttribute moPayHsbcDescription;
    protected SXmlAttribute moPaySantBankCode;
    protected SXmlAttribute moPayBajioBankCode;
    protected SXmlAttribute moPayBajioNick;
    protected SXmlAttribute moPayBankKey;
    protected SXmlAttribute moPayApplied;
    protected SXmlAttribute moPayBankBizPartnertId;
    protected SXmlAttribute moPayBankBankId;
    protected SXmlAttribute moPayRecordYearId;
    protected SXmlAttribute moPayRecordPeriodId;
    protected SXmlAttribute moPayRecordBkcId;
    protected SXmlAttribute moPayRecordRecordTypeId;
    protected SXmlAttribute moPayRecordNumberId;
    protected SXmlAttribute moPayBkkYearId;
    protected SXmlAttribute moPayBkkNumberId;

    public SXmlBankLayoutPayment() {
        super(NAME);

        moPayAmount = new SXmlAttribute(ATT_LAY_PAY_AMT);
        moPayReference = new SXmlAttribute(ATT_LAY_PAY_REF);
        moPayConcept = new SXmlAttribute(ATT_LAY_PAY_CPT);
        moPayHsbcFiscalVoucher = new SXmlAttribute(ATT_LAY_PAY_HSBC_FIS_VOU);
        moPayHsbcAccountType = new SXmlAttribute(ATT_LAY_PAY_HSBC_ACC_TP);
        moPayHsbcBankCode = new SXmlAttribute(ATT_LAY_PAY_HSBC_BANK_CODE);
        moPayHsbcFiscalIdDebit = new SXmlAttribute(ATT_LAY_PAY_HSBC_FIS_ID_DBT);
        moPayHsbcFiscalIdCredit = new SXmlAttribute(ATT_LAY_PAY_HSBC_FIS_ID_CRD);
        moPayHsbcDescription = new SXmlAttribute(ATT_LAY_PAY_HSBC_DCRP);
        moPaySantBankCode = new SXmlAttribute(ATT_LAY_PAY_SAN_BANK_CODE);
        moPayBajioBankCode = new SXmlAttribute(ATT_LAY_PAY_BAJIO_BANK_CODE);
        moPayBajioNick = new SXmlAttribute(ATT_LAY_PAY_BAJIO_NICK);
        moPayBankKey = new SXmlAttribute(ATT_LAY_PAY_BANK_KEY);
        moPayApplied = new SXmlAttribute(ATT_LAY_PAY_APPLIED);
        moPayBankBizPartnertId = new SXmlAttribute(ATT_LAY_PAY_BANK_BP);
        moPayBankBankId = new SXmlAttribute(ATT_LAY_PAY_BANK_BANK);
        moPayRecordYearId = new SXmlAttribute(ATT_LAY_PAY_REC_YEAR);
        moPayRecordPeriodId = new SXmlAttribute(ATT_LAY_PAY_REC_PER);
        moPayRecordBkcId = new SXmlAttribute(ATT_LAY_PAY_REC_BKC);
        moPayRecordRecordTypeId = new SXmlAttribute(ATT_LAY_PAY_REC_REC_TP);
        moPayRecordNumberId = new SXmlAttribute(ATT_LAY_PAY_REC_NUM);
        moPayBkkYearId = new SXmlAttribute(ATT_LAY_PAY_BKK_YEAR);
        moPayBkkNumberId = new SXmlAttribute(ATT_LAY_PAY_BKK_NUM);

        mvXmlAttributes.add(moPayReference);
        mvXmlAttributes.add(moPayConcept);
        mvXmlAttributes.add(moPayAmount);
        mvXmlAttributes.add(moPayHsbcFiscalVoucher);
        mvXmlAttributes.add(moPayHsbcAccountType);
        mvXmlAttributes.add(moPayHsbcBankCode);
        mvXmlAttributes.add(moPayHsbcFiscalIdDebit);
        mvXmlAttributes.add(moPayHsbcFiscalIdCredit);
        mvXmlAttributes.add(moPayHsbcDescription);
        mvXmlAttributes.add(moPaySantBankCode);
        mvXmlAttributes.add(moPayBajioBankCode);
        mvXmlAttributes.add(moPayBajioNick);
        mvXmlAttributes.add(moPayBankKey);
        mvXmlAttributes.add(moPayApplied);
        mvXmlAttributes.add(moPayBankBizPartnertId);
        mvXmlAttributes.add(moPayBankBankId);
        mvXmlAttributes.add(moPayRecordYearId);
        mvXmlAttributes.add(moPayRecordPeriodId);
        mvXmlAttributes.add(moPayRecordBkcId);
        mvXmlAttributes.add(moPayRecordRecordTypeId);
        mvXmlAttributes.add(moPayRecordNumberId);
        mvXmlAttributes.add(moPayBkkYearId);
        mvXmlAttributes.add(moPayBkkNumberId);
    }
}
