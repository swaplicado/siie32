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
    private double mdAmount;
    private String msConcept;
    private String msObservation;

    public String getBank() { return msBank; }
    public void setBank(String bank) { msBank = bank; }

    public String getBankAccount() { return msBankAccount; }
    public void setBankAccount(String account) { msBankAccount = account; }

    public String getBeneficiary() { return msBeneficiary; }
    public void setBeneficiary(String beneficiary) { msBeneficiary = beneficiary; }

    public double getAmount() { return mdAmount; }
    public void setAmount(double amount) { mdAmount = amount; }

    public String getConcept() { return msConcept; }
    public void setConcept(String concept) { msConcept = concept; }

    public String getObservation() { return msObservation; }
    public void setObservation(String oservation) { msObservation = oservation; }
}
