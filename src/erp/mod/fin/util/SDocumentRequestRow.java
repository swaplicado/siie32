/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.util;

/**
 *
 * @author Edwin Carmona
 */
public class SDocumentRequestRow {
    
    private String msBank;
    private String msBankAccount;
    private String msBeneficiary;
    private double mdCurrencyAmount;
    private double mdOriginalCurrencyAmount;
    private String msConcept;
    private String msObservation;

    
    public void setBank(String s) { msBank = s; }
    public void setBankAccount(String s) { msBankAccount = s; }
    public void setBeneficiary(String s) { msBeneficiary = s; }
    public void setCurrencyAmount(double d) { mdCurrencyAmount = d; }
    public void setOriginalCurrencyAmount(double d) { mdOriginalCurrencyAmount = d; }
    public void setConcept(String s) { msConcept = s; }
    public void setObservation(String s) { msObservation = s; }
    
    public String getBank() { return msBank; }
    public String getBankAccount() { return msBankAccount; }
    public String getBeneficiary() { return msBeneficiary; }
    public double getCurrencyAmount() { return mdCurrencyAmount; }
    public double getOriginalCurrencyAmount() { return mdOriginalCurrencyAmount; }
    public String getConcept() { return msConcept; }
    public String getObservation() { return msObservation; }
}
