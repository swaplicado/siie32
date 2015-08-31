/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDialogPriceHistoryRow extends erp.lib.table.STableRow {

    protected erp.client.SClientInterface miClient;

    protected java.lang.String msBizPartner;
    protected java.util.Date mtDate;
    protected java.lang.String msDocType;
    protected java.lang.String msDocNumber;
    protected double mdPriceUnitary;
    protected double mdDiscountUnitary;

    public SDialogPriceHistoryRow(erp.client.SClientInterface client) {
        miClient = client;
        reset();
    }

    public void reset() {
        msBizPartner = "";
        mtDate = null;
        msDocType = "";
        msDocNumber = "";
        mdPriceUnitary = 0;
        mdDiscountUnitary = 0;
    }

    public void setBizPartner(java.lang.String s) { msBizPartner = s; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setDocType(java.lang.String s) { msDocType = s; }
    public void setDocNumber(java.lang.String s) { msDocNumber = s; }
    public void setPriceUnitary(double d) { mdPriceUnitary = d; }
    public void setDiscountUnitary(double d) { mdDiscountUnitary = d; }

    public java.lang.String getBizPartner() { return msBizPartner; }
    public java.util.Date getDate() { return mtDate; }
    public java.lang.String getDocType() { return msDocType; }
    public java.lang.String getDocNumber() { return msDocNumber; }
    public double getPriceUnitary() { return mdPriceUnitary; }
    public double getDiscountUnitary() { return mdDiscountUnitary; }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(msBizPartner);
        mvValues.add(mtDate);
        mvValues.add(msDocType);
        mvValues.add(msDocNumber);
        mvValues.add(mdPriceUnitary);
        mvValues.add(mdDiscountUnitary);
    }
}
