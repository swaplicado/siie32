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
public class SXmlBankLayoutPaymentDoc extends SXmlElement {

    public static final String NAME = "Doc";
    public static final String ATT_LAY_ROW_DPS_YEAR = "DpsYearId";
    public static final String ATT_LAY_ROW_DPS_DOC = "DpsDocId";
    public static final String ATT_LAY_ROW_AMT = "Amount";
    public static final String ATT_LAY_ROW_AMT_CY = "AmountCy";
    public static final String ATT_LAY_ROW_CUR = "Currency";
    public static final String ATT_LAY_ROW_EXT_RATE = "ExchangeRate";
    public static final String ATT_LAY_ROW_REF_REC = "ReferenceRecord";
    public static final String ATT_LAY_ROW_OBS = "Observation";

    protected SXmlAttribute moRowDpsYear;
    protected SXmlAttribute moRowDpsDoc;
    protected SXmlAttribute moRowAmount;
    protected SXmlAttribute moRowAmountCy;
    protected SXmlAttribute moRowCurrency;
    protected SXmlAttribute moRowExchangeRate;
    protected SXmlAttribute moRowReferenceRecord;
    protected SXmlAttribute moRowObservation;

    public SXmlBankLayoutPaymentDoc() {
        super(NAME);

        moRowDpsYear = new SXmlAttribute(ATT_LAY_ROW_DPS_YEAR);
        moRowDpsDoc = new SXmlAttribute(ATT_LAY_ROW_DPS_DOC);
        moRowAmount = new SXmlAttribute(ATT_LAY_ROW_AMT);
        moRowAmountCy = new SXmlAttribute(ATT_LAY_ROW_AMT_CY);
        moRowCurrency = new SXmlAttribute(ATT_LAY_ROW_CUR);
        moRowExchangeRate = new SXmlAttribute(ATT_LAY_ROW_EXT_RATE);
        moRowReferenceRecord = new SXmlAttribute(ATT_LAY_ROW_REF_REC);
        moRowObservation = new SXmlAttribute(ATT_LAY_ROW_OBS);
        
        mvXmlAttributes.add(moRowDpsYear);
        mvXmlAttributes.add(moRowDpsDoc);
        mvXmlAttributes.add(moRowAmount);
        mvXmlAttributes.add(moRowAmountCy);
        mvXmlAttributes.add(moRowCurrency);
        mvXmlAttributes.add(moRowExchangeRate);
        mvXmlAttributes.add(moRowReferenceRecord);
        mvXmlAttributes.add(moRowObservation);
    }
}
