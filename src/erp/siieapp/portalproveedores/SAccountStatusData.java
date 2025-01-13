/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.portalproveedores;

/**
 *
 * @author swaplicado
 */
public class SAccountStatusData {
    int idYear;
    String date;
    String concept;
    double importForeignCurrency;
    float excRate;
    String currencyCode;
    double debit;
    double credit;

    public int getIdYear() {
        return idYear;
    }

    public String getDate() {
        return date;
    }

    public String getConcept() {
        return concept;
    }

    public double getImportForeignCurrency() {
        return importForeignCurrency;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public double getDebit() {
        return debit;
    }

    public double getCredit() {
        return credit;
    }

    public float getExcRate() {
        return excRate;
    }
    

    public void setIdYear(int idYear) {
        this.idYear = idYear;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public void setImportForeignCurrency(double importForeignCurrency) {
        this.importForeignCurrency = importForeignCurrency;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public void setExcRate(float excRate) {
        this.excRate = excRate;
    }
    
    
    
    
}
