/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data.cfd;

import erp.mtrn.data.SDataDps;
import sa.lib.SLibUtils;

/**
 * GUI data structure for input of individual document's payment in payments for CFDI of Payments.
 * @author Sergio Flores
 */
public final class SCfdPaymentEntryDoc extends erp.lib.table.STableRow {
    
    public int Number;
    public SDataDps DataDps;
    public int Installment;
    public double BalancePrev;      // in original currency of document
    public double Payment;          // in original currency of document
    public double BalancePend;      // in original currency of document
    public double ExchangeRate;     // to exchange original currency of document into currency of payment
    public double BalancePrevPay;   // in currency of payment
    public double PaymentPay;       // in currency of payment
    public double BalancePendPay;   // in currency of payment
    public int AuxGridIndex;
    public SCfdPaymentEntry ParentPaymentEntry;
    
    public SCfdPaymentEntryDoc(int number, SDataDps dataDps, int installment, double balancePrev, double payment, double exchangeRate, SCfdPaymentEntry parentPaymentEntry) {
        Number = number;
        DataDps = dataDps;
        Installment = installment;
        BalancePrev = balancePrev;
        Payment = payment;
        //BalancePend...    set in method computePaymentAmounts()
        ExchangeRate = exchangeRate;
        //BalancePrevPay... set in method computePaymentAmounts()
        //PaymentPay...     set in method computePaymentAmounts()
        //BalancePendPay... set in method computePaymentAmounts()
        AuxGridIndex = -1;
        ParentPaymentEntry = parentPaymentEntry;
        
        computePaymentAmounts();
    }
    
    /**
     * Computes pending balance in document currency.
     */
    public void computeBalancePend() {
        BalancePend = SLibUtils.roundAmount(BalancePrev - Payment);
    }
    
    /**
     * Computes pending balance in document currency as well as amounts in payment currency.
     */
    public void computePaymentAmounts() {
        computeBalancePend();
        if (ExchangeRate == 0) {
            BalancePrevPay = 0;
            PaymentPay = 0;
            BalancePendPay = 0;
        }
        else {
            BalancePrevPay = SLibUtils.roundAmount(BalancePrev / ExchangeRate);
            PaymentPay = SLibUtils.roundAmount(Payment / ExchangeRate);
            BalancePendPay = SLibUtils.roundAmount(BalancePend / ExchangeRate);
        }
    }

    /**
     * Prepares row to be displayed in GUI grid.
     */
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
        mvValues.add(PaymentPay);
        mvValues.add(ParentPaymentEntry.CurrencyKey);
    }
}
