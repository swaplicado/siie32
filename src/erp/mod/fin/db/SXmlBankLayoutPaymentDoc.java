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

    protected SXmlAttribute moRowDpsYear;
    protected SXmlAttribute moRowDpsDoc;
    protected SXmlAttribute moRowAmount;

    public SXmlBankLayoutPaymentDoc() {
        super(NAME);

        moRowDpsYear = new SXmlAttribute(ATT_LAY_ROW_DPS_YEAR);
        moRowDpsDoc = new SXmlAttribute(ATT_LAY_ROW_DPS_DOC);
        moRowAmount = new SXmlAttribute(ATT_LAY_ROW_AMT);

        mvXmlAttributes.add(moRowDpsYear);
        mvXmlAttributes.add(moRowDpsDoc);
        mvXmlAttributes.add(moRowAmount);
    }
}
