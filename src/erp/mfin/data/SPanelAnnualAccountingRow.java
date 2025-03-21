/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.lib.table.STableRow;
import java.util.Date;

/**
 *
 * @author Isabel Servín
 */
public class SPanelAnnualAccountingRow extends STableRow {

    protected String msPkAccountId;
    protected String msAccount;
    protected double mdOpeningBalance;
    protected double mdDebitJan;
    protected double mdCreditJan;
    protected double mdClosingJan;
    protected double mdDebitFeb;
    protected double mdCreditFeb;
    protected double mdClosingFeb;
    protected double mdDebitMar;
    protected double mdCreditMar;
    protected double mdClosingMar;
    protected double mdDebitApr;
    protected double mdCreditApr;
    protected double mdClosingApr;
    protected double mdDebitMay;
    protected double mdCreditMay;
    protected double mdClosingMay;
    protected double mdDebitJun;
    protected double mdCreditJun;
    protected double mdClosingJun;
    protected double mdDebitJul;
    protected double mdCreditJul;
    protected double mdClosingJul;
    protected double mdDebitAgu;
    protected double mdCreditAgu;
    protected double mdClosingAgu;
    protected double mdDebitSep;
    protected double mdCreditSep;
    protected double mdClosingSep;
    protected double mdDebitOct;
    protected double mdCreditOct;
    protected double mdClosingOct;
    protected double mdDebitNov;
    protected double mdCreditNov;
    protected double mdClosingNov;
    protected double mdDebitDec;
    protected double mdCreditDec;
    protected double mdClosingDec;
    protected String msCurrencyKey;
    protected Date mtDateStart;
    protected Date mtDateEnd_n;
    protected boolean mbIsActive;
    protected boolean mbIsDeleted;
    protected int mnFkCurrencyId;

    public SPanelAnnualAccountingRow() {
        reset();
    }

    public void reset() {
        msPkAccountId = "";
        msAccount = "";
        mdOpeningBalance = 0;
        mdDebitJan = 0;
        mdCreditJan = 0;
        mdClosingJan = 0;
        mdDebitFeb = 0;
        mdCreditFeb = 0;
        mdClosingFeb = 0;
        mdDebitMar = 0;
        mdCreditMar = 0;
        mdClosingMar = 0;
        mdDebitApr = 0;
        mdCreditApr = 0;
        mdClosingApr = 0;
        mdDebitMay = 0;
        mdCreditMay = 0;
        mdClosingMay = 0;
        mdDebitJun = 0;
        mdCreditJun = 0;
        mdClosingJun = 0;
        mdDebitJul = 0;
        mdCreditJul = 0;
        mdClosingJul = 0;
        mdDebitAgu = 0;
        mdCreditAgu = 0;
        mdClosingAgu = 0;
        mdDebitSep = 0;
        mdCreditSep = 0;
        mdClosingSep = 0;
        mdDebitOct = 0;
        mdCreditOct = 0;
        mdClosingOct = 0;
        mdDebitNov = 0;
        mdCreditNov = 0;
        mdClosingNov = 0;
        mdDebitDec = 0;
        mdCreditDec = 0;
        mdClosingDec = 0;
        msCurrencyKey = "";
        mtDateStart = null;
        mtDateEnd_n = null;
        mbIsActive = false;
        mbIsDeleted = false;
        mnFkCurrencyId = 0;
    }

    public void setPkAccountId(java.lang.String s) { msPkAccountId = s; }
    public void setAccount(java.lang.String s) { msAccount = s; }
    public void setOpeningBalance(double d) { mdOpeningBalance = d; }
    public void setDebitJan(double d) { mdDebitJan = d; }
    public void setCreditJan(double d) { mdCreditJan = d; }
    public void setClosingJan(double d) { mdClosingJan = d; }
    public void setDebitFeb(double d) { mdDebitFeb = d; }
    public void setCreditFeb(double d) { mdCreditFeb = d; }
    public void setClosingFeb(double d) { mdClosingFeb = d; }
    public void setDebitMar(double d) { mdDebitMar = d; }
    public void setCreditMar(double d) { mdCreditMar = d; }
    public void setClosingMar(double d) { mdClosingMar = d; }
    public void setDebitApr(double d) { mdDebitApr = d; }
    public void setCreditApr(double d) { mdCreditApr = d; }
    public void setClosingApr(double d) { mdClosingApr = d; }
    public void setDebitMay(double d) { mdDebitMay = d; }
    public void setCreditMay(double d) { mdCreditMay = d; }
    public void setClosingMay(double d) { mdClosingMay = d; }
    public void setDebitJun(double d) { mdDebitJun = d; }
    public void setCreditJun(double d) { mdCreditJun = d; }
    public void setClosingJun(double d) { mdClosingJun = d; }
    public void setDebitJul(double d) { mdDebitJul = d; }
    public void setCreditJul(double d) { mdCreditJul = d; }
    public void setClosingJul(double d) { mdClosingJul = d; }
    public void setDebitAgu(double d) { mdDebitAgu = d; }
    public void setCreditAgu(double d) { mdCreditAgu = d; }
    public void setClosingAgu(double d) { mdClosingAgu = d; }
    public void setDebitSep(double d) { mdDebitSep = d; }
    public void setCreditSep(double d) { mdCreditSep = d; }
    public void setClosingSep(double d) { mdClosingSep = d; }
    public void setDebitOct(double d) { mdDebitOct = d; }
    public void setCreditOct(double d) { mdCreditOct = d; }
    public void setClosingOct(double d) { mdClosingOct = d; }
    public void setDebitNov(double d) { mdDebitNov = d; }
    public void setCreditNov(double d) { mdCreditNov = d; }
    public void setClosingNov(double d) { mdClosingNov = d; }
    public void setDebitDec(double d) { mdDebitDec = d; }
    public void setCreditDec(double d) { mdCreditDec = d; }
    public void setClosingDec(double d) { mdClosingDec = d; }
    public void setCurrencyKey(java.lang.String s) { msCurrencyKey = s; }
    public void setDateStart(java.util.Date t) { mtDateStart = t; }
    public void setDateEnd_n(java.util.Date t) { mtDateEnd_n = t; }
    public void setIsActive(boolean b) { mbIsActive = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }

    public java.lang.String getPkAccountId() { return msPkAccountId; }
    public java.lang.String getAccount() { return msAccount; }
    public double getOpeningBalance() { return mdOpeningBalance; }
    public double getDebitJan() { return mdDebitJan; }
    public double getCreditJan() { return mdCreditJan; }
    public double getClosingJan() { return mdClosingJan; }
    public double getDebitFeb() { return mdDebitFeb; }
    public double getCreditFeb() { return mdCreditFeb; }
    public double getClosingFeb() { return mdClosingFeb; }
    public double getDebitMar() { return mdDebitMar; }
    public double getCreditMar() { return mdCreditMar; }
    public double getClosingMar() { return mdClosingMar; }
    public double getDebitApr() { return mdDebitApr; }
    public double getCreditApr() { return mdCreditApr; }
    public double getClosingApr() { return mdClosingApr; }
    public double getDebitMay() { return mdDebitMay; }
    public double getCreditMay() { return mdCreditMay; }
    public double getClosingMay() { return mdClosingMay; }
    public double getDebitJun() { return mdDebitJun; }
    public double getCreditJun() { return mdCreditJun; }
    public double getClosingJun() { return mdClosingJun; }
    public double getDebitJul() { return mdDebitJul; }
    public double getCreditJul() { return mdCreditJul; }
    public double getClosingJul() { return mdClosingJul; }
    public double getDebitAgu() { return mdDebitAgu; }
    public double getCreditAgu() { return mdCreditAgu; }
    public double getClosingAgu() { return mdClosingAgu; }
    public double getDebitSep() { return mdDebitSep; }
    public double getCreditSep() { return mdCreditSep; }
    public double getClosingSep() { return mdClosingSep; }
    public double getDebitOct() { return mdDebitOct; }
    public double getCreditOct() { return mdCreditOct; }
    public double getClosingOct() { return mdClosingOct; }
    public double getDebitNov() { return mdDebitNov; }
    public double getCreditNov() { return mdCreditNov; }
    public double getClosingNov() { return mdClosingNov; }
    public double getDebitDec() { return mdDebitDec; }
    public double getCreditDec() { return mdCreditDec; }
    public double getClosingDec() { return mdClosingDec; }
    public java.lang.String getCurrencyKey() { return msCurrencyKey; }
    public java.util.Date getDateStart() { return mtDateStart; }
    public java.util.Date getDateEnd_n() { return mtDateEnd_n; }
    public boolean getIsActive() { return mbIsActive; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(msPkAccountId);
        mvValues.add(msAccount);
        mvValues.add(mdOpeningBalance);
        mvValues.add(mdDebitJan);
        mvValues.add(mdCreditJan);
        mvValues.add(mdClosingJan);
        mvValues.add(mdDebitFeb);
        mvValues.add(mdCreditFeb);
        mvValues.add(mdClosingFeb);
        mvValues.add(mdDebitMar);
        mvValues.add(mdCreditMar);
        mvValues.add(mdClosingMar);
        mvValues.add(mdDebitApr);
        mvValues.add(mdCreditApr);
        mvValues.add(mdClosingApr);
        mvValues.add(mdDebitMay);
        mvValues.add(mdCreditMay);
        mvValues.add(mdClosingMay);
        mvValues.add(mdDebitJun);
        mvValues.add(mdCreditJun);
        mvValues.add(mdClosingJun);
        mvValues.add(mdDebitJul);
        mvValues.add(mdCreditJul);
        mvValues.add(mdClosingJul);
        mvValues.add(mdDebitAgu);
        mvValues.add(mdCreditAgu);
        mvValues.add(mdClosingAgu);
        mvValues.add(mdDebitSep);
        mvValues.add(mdCreditSep);
        mvValues.add(mdClosingSep);
        mvValues.add(mdDebitOct);
        mvValues.add(mdCreditOct);
        mvValues.add(mdClosingOct);
        mvValues.add(mdDebitNov);
        mvValues.add(mdCreditNov);
        mvValues.add(mdClosingNov);
        mvValues.add(mdDebitDec);
        mvValues.add(mdCreditDec);
        mvValues.add(mdClosingDec);
        mvValues.add(msCurrencyKey);
        mvValues.add(mtDateStart);
        mvValues.add(mtDateEnd_n);
        mvValues.add(mbIsActive);
        mvValues.add(mbIsDeleted);
    }
}
