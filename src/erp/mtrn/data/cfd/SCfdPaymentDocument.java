/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data.cfd;

import erp.mtrn.data.SDataDps;

/**
 *
 * @author Sergio Flores
 */
public class SCfdPaymentDocument extends erp.lib.table.STableRow {
    
    public int Number;
    public SDataDps DataDps;
    public int Installment;
    public double ExchangeRate;
    public double BalancePrevious;
    public double Payment;
    public double Balance;
    
    public SCfdPaymentDocument(int number, SDataDps dataDps, int installment, double exchangeRate, double balancePrevious, double payment, double balance) {
        Number = number;
        DataDps = dataDps;
        Installment = installment;
        ExchangeRate = exchangeRate;
        BalancePrevious = balancePrevious;
        Payment = payment;
        Balance = balance;
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(Number);
        mvValues.add(DataDps.getDpsNumber());
        mvValues.add(DataDps.getDbmsDataCfd().getUuid());
        mvValues.add(Installment);
        mvValues.add(BalancePrevious);
        mvValues.add(Payment);
        mvValues.add(Balance);
        mvValues.add(DataDps.getDbmsCurrencyKey());
        mvValues.add(ExchangeRate);
    }
}
