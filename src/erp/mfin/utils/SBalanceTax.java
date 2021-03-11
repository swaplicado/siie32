/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.utils;

/**
 * Estructura para la obtención de saldos de documentos
 * 
 * @author Edwin Carmona
 */
public class SBalanceTax {
    private int taxBasId;
    private int taxId;
    private double balance;
    private double balanceCurrency;

    public int getTaxBasId() {
        return taxBasId;
    }

    public void setTaxBasId(int taxBasId) {
        this.taxBasId = taxBasId;
    }

    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalanceCurrency() {
        return balanceCurrency;
    }

    public void setBalanceCurrency(double balanceCurrency) {
        this.balanceCurrency = balanceCurrency;
    }
    
    public int[] getTaxPk() {
        return new int[] { taxBasId, taxId };
    }
    
}
