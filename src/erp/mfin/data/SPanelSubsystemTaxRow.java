/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SPanelSubsystemTaxRow extends erp.lib.table.STableRow {

    protected java.lang.String msTaxBasic;
    protected java.lang.String msTax;
    protected boolean mbIsDeleted;
    protected double mdOpeningBalance;
    protected double mdDebit;
    protected double mdCredit;
    protected int mnFkTaxBasicId;
    protected int mnFkTaxId;
    protected int mnFkCurrencyId;
    protected java.lang.String msCurrencyKey;

    public SPanelSubsystemTaxRow() {
        reset();
    }

    public void reset() {
        msTaxBasic = "";
        msTax = "";
        mbIsDeleted = false;
        mdOpeningBalance = 0;
        mdDebit = 0;
        mdCredit = 0;
        mnFkCurrencyId = 0;
        msCurrencyKey = "";
    }

    public void setTaxBasic(java.lang.String s) { msTaxBasic = s; }
    public void setTax(java.lang.String s) { msTax = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setOpeningBalance(double d) { mdOpeningBalance = d; }
    public void setDebit(double d) { mdDebit = d; }
    public void setCredit(double d) { mdCredit = d; }
    public void setFkTaxBasicId(int n) { mnFkTaxBasicId = n; }
    public void setFkTaxId(int n) { mnFkTaxId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setCurrencyKey(java.lang.String s) { msCurrencyKey = s; }

    public java.lang.String getTaxBasic() { return msTaxBasic; }
    public java.lang.String getTax() { return msTax; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public double getOpeningBalance() { return mdOpeningBalance; }
    public double getDebit() { return mdDebit; }
    public double getCredit() { return mdCredit; }
    public int getFkTaxBasicId() { return mnFkTaxBasicId; }
    public int getFkTaxId() { return mnFkTaxId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public java.lang.String getCurrencyKey() { return msCurrencyKey; }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(msTaxBasic);
        mvValues.add(msTax);
        mvValues.add(mdOpeningBalance);
        mvValues.add(mdDebit);
        mvValues.add(mdCredit);
        mvValues.add(mdOpeningBalance + mdDebit - mdCredit);
        mvValues.add(msCurrencyKey);
        mvValues.add(mbIsDeleted);
    }
}
