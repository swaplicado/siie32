/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SPanelAccountingRow extends erp.lib.table.STableRow {

    protected java.lang.String msPkAccountId;
    protected java.lang.String msAccount;
    protected java.util.Date mtDateStart;
    protected java.util.Date mtDateEnd_n;
    protected boolean mbIsActive;
    protected boolean mbIsDeleted;
    protected double mdOpeningBalance;
    protected double mdDebit;
    protected double mdCredit;
    protected int mnFkCurrencyId;
    protected java.lang.String msCurrencyKey;

    public SPanelAccountingRow() {
        reset();
    }

    public void reset() {
        msPkAccountId = "";
        msAccount = "";
        mtDateStart = null;
        mtDateEnd_n = null;
        mbIsActive = false;
        mbIsDeleted = false;
        mdOpeningBalance = 0;
        mdDebit = 0;
        mdCredit = 0;
        mnFkCurrencyId = 0;
        msCurrencyKey = "";
    }

    public void setPkAccountId(java.lang.String s) { msPkAccountId = s; }
    public void setAccount(java.lang.String s) { msAccount = s; }
    public void setDateStart(java.util.Date t) { mtDateStart = t; }
    public void setDateEnd_n(java.util.Date t) { mtDateEnd_n = t; }
    public void setIsActive(boolean b) { mbIsActive = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setOpeningBalance(double d) { mdOpeningBalance = d; }
    public void setDebit(double d) { mdDebit = d; }
    public void setCredit(double d) { mdCredit = d; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setCurrencyKey(java.lang.String s) { msCurrencyKey = s; }

    public java.lang.String getPkAccountId() { return msPkAccountId; }
    public java.lang.String getAccount() { return msAccount; }
    public java.util.Date getDateStart() { return mtDateStart; }
    public java.util.Date getDateEnd_n() { return mtDateEnd_n; }
    public boolean getIsActive() { return mbIsActive; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public double getOpeningBalance() { return mdOpeningBalance; }
    public double getDebit() { return mdDebit; }
    public double getCredit() { return mdCredit; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public java.lang.String getCurrencyKey() { return msCurrencyKey; }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(msPkAccountId);
        mvValues.add(msAccount);
        mvValues.add(mdOpeningBalance);
        mvValues.add(mdDebit);
        mvValues.add(mdCredit);
        mvValues.add(mdOpeningBalance + mdDebit - mdCredit);
        mvValues.add(msCurrencyKey);
        mvValues.add(mtDateStart);
        mvValues.add(mtDateEnd_n);
        mvValues.add(mbIsActive);
        mvValues.add(mbIsDeleted);
    }
}
