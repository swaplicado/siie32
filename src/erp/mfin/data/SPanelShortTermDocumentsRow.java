/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Isabel Servín
 */
public class SPanelShortTermDocumentsRow extends erp.lib.table.STableRow {

    protected String msPkAccountId;
    protected String msAccount;
    protected String msReference;
    protected double mdOpeningBalanceCy;
    protected double mdDebitCy;
    protected double mdCreditCy;
    protected double mdClosingBalanceCy;
    protected String msCurrencyKey;
    protected double mdExchangeRate;
    protected double mdOpeningBalance;
    protected double mdDebit;
    protected double mdCredit;
    protected double mdClosingBalance;
    protected String msCurrencyLocalKey;
    protected int mnCurrencyId;

    public SPanelShortTermDocumentsRow() {
        reset();
    }

    public void reset() {
        msPkAccountId = "";
        msAccount = "";
        msReference = "";
        mdOpeningBalanceCy = 0;
        mdDebitCy = 0;
        mdCreditCy = 0;
        mdClosingBalanceCy = 0;
        msCurrencyKey = "";
        mdExchangeRate = 0;
        mdOpeningBalance = 0;
        mdDebit = 0;
        mdCredit = 0;
        mdClosingBalance = 0;
        msCurrencyLocalKey = "";
        mnCurrencyId = 0;
    }

    public void setPkAccountId(java.lang.String s) { msPkAccountId = s; }
    public void setAccount(java.lang.String s) { msAccount = s; }
    public void setReference(java.lang.String s) { msReference = s; }
    public void setOpeningBalanceCy(double d) { mdOpeningBalanceCy = d; }
    public void setDebitCy(double d) { mdDebitCy = d; }
    public void setCreditCy(double d) { mdCreditCy = d; }
    public void setClosingBalanceCy(double d) { mdClosingBalanceCy = d; }
    public void setCurrencyKey(java.lang.String s) { msCurrencyKey = s; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setOpeningBalance(double d) { mdOpeningBalance = d; }
    public void setDebit(double d) { mdDebit = d; }
    public void setCredit(double d) { mdCredit = d; }
    public void setClosingBalance(double d) { mdClosingBalance = d; }
    public void setCurrencyLocalKey(java.lang.String s) { msCurrencyLocalKey = s; }
    public void setCurrencyId(int n) { mnCurrencyId = n; }
    
    public java.lang.String getPkAccountId() { return msPkAccountId; }
    public java.lang.String getAccount() { return msAccount; }
    public java.lang.String getReference() { return msReference; }
    public double getOpeningBalanceCy() { return mdOpeningBalanceCy; }
    public double getDebitCy() { return mdDebitCy; }
    public double getCreditCy() { return mdCreditCy; }
    public double getClosingBalanceCy() { return mdClosingBalanceCy; }
    public java.lang.String getCurrencyKey() { return msCurrencyKey; }
    public double getExchangeRate() { return mdExchangeRate; }
    public double getOpeningBalance() { return mdOpeningBalance; }
    public double getDebit() { return mdDebit; }
    public double getCredit() { return mdCredit; }
    public double getClosingBalance() { return mdClosingBalance; }
    public java.lang.String getCurrencyLocalKey() { return msCurrencyLocalKey; }
    public int getCurrencyId() { return mnCurrencyId; }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(msPkAccountId);
        mvValues.add(msAccount);
        mvValues.add(msReference);
        mvValues.add(mdOpeningBalanceCy);
        mvValues.add(mdDebitCy);
        mvValues.add(mdCreditCy);
        mvValues.add(mdClosingBalanceCy);
        mvValues.add(msCurrencyKey);
        mvValues.add(mdExchangeRate);
        mvValues.add(mdOpeningBalance);
        mvValues.add(mdDebit);
        mvValues.add(mdCredit);
        mvValues.add(mdClosingBalance);
        mvValues.add(msCurrencyLocalKey);
    }
}
