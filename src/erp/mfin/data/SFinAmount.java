/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SFinAmount {

    public double Amount;
    public double AmountCy;

    public SFinAmount(double amount, double amountCy) {
        Amount = amount;
        AmountCy = amountCy;
    }
}
