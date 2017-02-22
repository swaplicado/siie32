/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.util;

import java.util.Date;

/**
 *
 * @author Edwin Carmona
 */
public class SLayoutParameters {
    
    private String msTitle;
    private String msCompanyName;
    private Date moDateTimeRequest;
    private Date moApplicationDate;
    private String msLayoutType;
    private String msBank;
    private String msBankAccount;
    private String msTypePayment;
    private String msCurrency;
    private String msCurrencyDps;
    private double mdOriginalTotal;
    private double mdTotal;
    private String msFolio;
    private String msAuthRequests;
    private boolean mbIsDifferentCurrency;

    public String getTitle() { return msTitle; }
    public void setTitle(String title) { msTitle = title; }

    public String getCompanyName() { return msCompanyName; }
    public void setCompanyName(String companyName) { msCompanyName = companyName; }

    public Date getDateTimeRequest() { return moDateTimeRequest; }
    public void setDateTimeRequest(Date dateTimeRequest) { moDateTimeRequest = dateTimeRequest; }

    public Date getApplicationDate() { return moApplicationDate; }
    public void setApplicationDate(Date applicationDate) { moApplicationDate = applicationDate; }

    public String getBank() { return msBank; }
    public void setBank(String bank) { msBank = bank; }
    
    public String getLayoutType() { return msLayoutType; }
    public void setLayoutType(String layoutType) { msLayoutType = layoutType; }

    public String getBankAccount() { return msBankAccount; }
    public void setBankAccount(String bankAccount) { msBankAccount = bankAccount; }

    public String getTypePayment() { return msTypePayment; }
    public void setTypePayment(String typePayment) { msTypePayment = typePayment; }

    public String getCurrency() { return msCurrency; }
    public void setCurrency(String currency) { msCurrency = currency; }
    
    public String getCurrencyDps() { return msCurrencyDps; }
    public void setCurrencyDps(String currency) { msCurrencyDps = currency; }

    public double getTotal() { return mdTotal; }
    public void setTotal(double total) { mdTotal = total; }
    
    public double getOriginalTotal() { return mdOriginalTotal; }
    public void setOriginalTotal(double originalTotal) { mdOriginalTotal = originalTotal; }

    public String getFolio() { return msFolio; }
    public void setFolio(String folio) { msFolio = folio; }

    public String getAuthRequests() { return msAuthRequests; }
    public void setAuthRequests(String authRequests) { msAuthRequests = authRequests; }
    
    public boolean getIsDifferentCurrency() { return mbIsDifferentCurrency; }
    public void setIsDifferentCurrency(boolean b) { mbIsDifferentCurrency = b; }
}
