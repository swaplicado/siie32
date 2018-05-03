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
public class SCfdPaymentDoc extends erp.lib.table.STableRow {
    
    public int Number;
    public SDataDps DataDps;
    public int Installment;
    public double BalancePrev;
    public double Payment;
    public double BalancePend;
    public double ExchangeRate;
    public double BalancePrevPay;
    public double PaymentPay;
    public double BalancePendPay;
    
    public SCfdPaymentDoc(int number, SDataDps dataDps, int installment, double balancePrev, double payment, double balancePend, double exchangeRate, double balancePrevPay, double paymentPay, double balancePendPay) {
        Number = number;
        DataDps = dataDps;
        Installment = installment;
        BalancePrev = balancePrev;
        Payment = payment;
        BalancePend = balancePend;
        ExchangeRate = exchangeRate;
        BalancePrevPay = balancePrevPay;
        PaymentPay = paymentPay;
        BalancePendPay = balancePendPay;
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(Number);
        mvValues.add(DataDps.getDpsNumber());
        mvValues.add(DataDps.getDbmsDataCfd().getUuid());
        mvValues.add(Installment);
        mvValues.add(BalancePrev);
        mvValues.add(Payment);
        mvValues.add(BalancePend);
        mvValues.add(DataDps.getDbmsCurrencyKey());
        mvValues.add(ExchangeRate);
    }
}
