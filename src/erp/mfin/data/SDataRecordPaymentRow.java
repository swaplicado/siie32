/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataRecordPaymentRow extends erp.lib.table.STableRow {

    private int mnPkYearId;
    private int mnPkDocId;
    private java.util.Date mtDate;
    private java.lang.String msDocType;
    private java.lang.String msDocNumber;
    private double mdTotNetCy;
    private double mdBalCy;
    private double mdPayCy;
    private double mdBalNewCy;
    private java.lang.String msDocCurKey;
    private double mdPay;
    private java.lang.String msCurKey;

    public SDataRecordPaymentRow() {
        reset();
    }

    public void reset() {
        mnPkYearId = 0;
        mnPkDocId = 0;
        mtDate = null;
        msDocType = "";
        msDocNumber = "";
        mdTotNetCy = 0;
        mdBalCy = 0;
        mdPayCy = 0;
        mdBalNewCy = 0;
        msDocCurKey = "";
        mdPay = 0;
        msCurKey = "";
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setDocType(java.lang.String s) { msDocType = s; }
    public void setDocNumber(java.lang.String s) { msDocNumber = s; }
    public void setTotNetCy(double d) { mdTotNetCy = d; }
    public void setBalCy(double d) { mdBalCy = d; }
    public void setPayCy(double d) { mdPayCy = d; }
    public void setBalNewCy(double d) { mdBalNewCy = d; }
    public void setDocCurKey(java.lang.String s) { msDocCurKey = s; }
    public void setPay(double d) { mdPay = d; }
    public void setCurKey(java.lang.String s) { msCurKey = s; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public java.util.Date getDate() { return mtDate; }
    public java.lang.String getDocType() { return msDocType; }
    public java.lang.String getDocNumber() { return msDocNumber; }
    public double getTotNetCy() { return mdTotNetCy; }
    public double getBalCy() { return mdBalCy; }
    public double getPayCy() { return mdPayCy; }
    public double getBalNewCy() { return mdBalNewCy; }
    public java.lang.String getDocCurKey() { return msDocCurKey; }
    public double getPay() { return mdPay; }
    public java.lang.String getCurKey() { return msCurKey; }

    @Override
    public void prepareTableRow() {
        mvValues.clear();

        mvValues.add(mtDate);
        mvValues.add(msDocType);
        mvValues.add(msDocNumber);
        mvValues.add(mdTotNetCy);
        mvValues.add(mdBalCy);
        mvValues.add(mdPayCy);
        mvValues.add(mdBalNewCy);
        mvValues.add(msDocCurKey);
        mvValues.add(mdPayCy);
        mvValues.add(msCurKey);
    }
}
