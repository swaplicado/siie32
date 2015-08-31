/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

/**
 *
 * @author Sergio Flores
 */
public class SFinBalance {

    protected double mdBalanceCy;
    protected double mdBalance;
    protected int mnCurrencyId;

    public SFinBalance(double balanceCy, double balance, int currencyId) {
        mdBalance = balance;
        mdBalanceCy = balanceCy;
        mnCurrencyId = currencyId;
    }

    public double getBalanceCy() { return mdBalanceCy; }
    public double getBalance() { return mdBalance; }
    public int getCurrencyId() { return mnCurrencyId; }

    public void add(final double valueCy, final double value) {
        mdBalanceCy += valueCy;
        mdBalance += value;
    }
}
