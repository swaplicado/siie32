/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SPanelSubsystemEntityRow extends erp.lib.table.STableRow {

    protected java.lang.String msCompanyBranch;
    protected java.lang.String msEntity;
    protected java.lang.String msEntityKey;
    protected boolean mbIsDeleted;
    protected double mdOpeningBalance;
    protected double mdDebit;
    protected double mdCredit;
    protected int mnFkCurrencyId;
    protected java.lang.String msCurrencyKey;

    public SPanelSubsystemEntityRow() {
        reset();
    }

    public void reset() {
        msCompanyBranch = "";
        msEntity = "";
        msEntityKey = "";
        mbIsDeleted = false;
        mdOpeningBalance = 0;
        mdDebit = 0;
        mdCredit = 0;
        mnFkCurrencyId = 0;
        msCurrencyKey = "";
    }

    public void setCompanyBranch(java.lang.String s) { msCompanyBranch = s; }
    public void setEntity(java.lang.String s) { msEntity = s; }
    public void setEntityKey(java.lang.String s) { msEntityKey = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setOpeningBalance(double d) { mdOpeningBalance = d; }
    public void setDebit(double d) { mdDebit = d; }
    public void setCredit(double d) { mdCredit = d; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setCurrencyKey(java.lang.String s) { msCurrencyKey = s; }

    public java.lang.String getCompanyBranch() { return msCompanyBranch; }
    public java.lang.String getEntity() { return msEntity; }
    public java.lang.String getEntityKey() { return msEntityKey; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public double getOpeningBalance() { return mdOpeningBalance; }
    public double getDebit() { return mdDebit; }
    public double getCredit() { return mdCredit; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public java.lang.String getCurrencyKey() { return msCurrencyKey; }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(msCompanyBranch);
        mvValues.add(msEntity);
        mvValues.add(msEntityKey);
        mvValues.add(mdOpeningBalance);
        mvValues.add(mdDebit);
        mvValues.add(mdCredit);
        mvValues.add(mdOpeningBalance + mdDebit - mdCredit);
        mvValues.add(msCurrencyKey);
        mvValues.add(mbIsDeleted);
    }
}
