/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.util;

import java.util.Date;

/**
 *
 * @author Edwin Carmona, Sergio Flores
 */
public class SBankLayoutParams {
    
    private String msTitle;
    private Date moDatetimeRequest;
    private Date moApplicationDate;
    private String msBankLayoutType;
    private String msBank;
    private String msBankAccount;
    private String msPaymentType;
    private String msCurrency;
    private String msCurrencyDps;
    private double mdOriginalTotal;
    private String msBankLayoutNumber;
    private boolean mbIsDifferentCurrency;

    public String getTitle() { return msTitle; }
    public void setTitle(String title) { msTitle = title; }

    public Date getDatetimeRequest() { return moDatetimeRequest; }
    public void setDatetimeRequest(Date dateTimeRequest) { moDatetimeRequest = dateTimeRequest; }

    public Date getApplicationDate() { return moApplicationDate; }
    public void setApplicationDate(Date applicationDate) { moApplicationDate = applicationDate; }

    public String getBank() { return msBank; }
    public void setBank(String bank) { msBank = bank; }
    
    public String getBankLayoutType() { return msBankLayoutType; }
    public void setBankLayoutType(String layoutType) { msBankLayoutType = layoutType; }

    public String getBankAccount() { return msBankAccount; }
    public void setBankAccount(String bankAccount) { msBankAccount = bankAccount; }

    public String getPaymentType() { return msPaymentType; }
    public void setPaymentType(String typePayment) { msPaymentType = typePayment; }

    public String getCurrency() { return msCurrency; }
    public void setCurrency(String currency) { msCurrency = currency; }
    
    public String getCurrencyDps() { return msCurrencyDps; }
    public void setCurrencyDps(String currency) { msCurrencyDps = currency; }

    public double getOriginalTotal() { return mdOriginalTotal; }
    public void setOriginalTotal(double originalTotal) { mdOriginalTotal = originalTotal; }

    public String getBankLayoutNumber() { return msBankLayoutNumber; }
    public void setBankLayoutNumber(String number) { msBankLayoutNumber = number; }

    public boolean getIsDifferentCurrency() { return mbIsDifferentCurrency; }
    public void setIsDifferentCurrency(boolean b) { mbIsDifferentCurrency = b; }
}
