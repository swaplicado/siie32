/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.data;

/**
 * Estructura para la obtención de saldos de documentos.
 * 
 * @author Edwin Carmona, Sergio Flores
 */
public class SFinBalanceTax {
    
    private int mnTaxBasicId;
    private int mnTaxId;
    private double mdBalanceLocal;
    private double mdBalanceCurrency;

    public int getTaxBasicId() {
        return mnTaxBasicId;
    }

    public void setTaxBasicId(int taxBasicId) {
        mnTaxBasicId = taxBasicId;
    }

    public int getTaxId() {
        return mnTaxId;
    }

    public void setTaxId(int taxId) {
        mnTaxId = taxId;
    }

    public double getBalance() {
        return mdBalanceLocal;
    }

    public void setBalanceLocal(double balanceLocal) {
        mdBalanceLocal = balanceLocal;
    }

    public double getBalanceCurrency() {
        return mdBalanceCurrency;
    }

    public void setBalanceCurrency(double balanceCurrency) {
        mdBalanceCurrency = balanceCurrency;
    }
    
    public int[] getTaxKey() {
        return new int[] { mnTaxBasicId, mnTaxId };
    }
}
