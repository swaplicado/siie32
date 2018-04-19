/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data.cfd;

import erp.mfin.data.SDataRecord;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SCfdPayment extends erp.lib.table.STableRow {
    
    public int Number;
    public Date Date;
    public String PaymentWay;
    public int CurrencyId;
    public String CurrencyKey;
    public double AmountCurrency;
    public double ExchangeRate;
    public double AmountLocal;
    public String Operation;
    public String AccountSrcFiscalId;
    public String AccountSrcNumber;
    public String AccountSrcEntity;
    public String AccountDesFiscalId;
    public String AccountDesNumber;
    public SDataRecord DataRecord;
    ArrayList<SCfdPaymentDocument> Documents;
    
    public SCfdPayment(int number, Date date, String paymentWay, int currencyId, String currencyKey, double amountCurrency, double exchangeRate, SDataRecord dataRecord) {
        Number = number;
        Date = date;
        PaymentWay = paymentWay;
        CurrencyId = currencyId;
        CurrencyKey = currencyKey;
        AmountCurrency = amountCurrency;
        ExchangeRate = exchangeRate;
        AmountLocal = SLibUtils.round(amountCurrency * exchangeRate, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
        Operation = "";
        AccountSrcFiscalId = "";
        AccountSrcNumber = "";
        AccountSrcEntity = "";
        AccountDesFiscalId = "";
        AccountDesNumber = "";
        DataRecord = dataRecord;
        Documents = new ArrayList<>();
    }
    
    /**
     * Computes payments of documents in CurrencyId.
     * @return 
     */
    public double computePaymentCurrency() {
        double payment = 0;
        
        for (SCfdPaymentDocument document : Documents) {
            payment = SLibUtils.round(
                    payment + SLibUtils.round(
                            document.Payment * document.ExchangeRate, 
                            SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits()), 
                    SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
        }
        
        return payment;
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(Number);
        mvValues.add(Date);
        mvValues.add(PaymentWay);
        mvValues.add(AmountCurrency);
        mvValues.add(CurrencyKey);
        mvValues.add(ExchangeRate);
        mvValues.add(DataRecord.getRecordPrimaryKey());
        mvValues.add(Operation);
        mvValues.add(AccountSrcFiscalId);
        mvValues.add(AccountSrcNumber);
        mvValues.add(AccountSrcEntity);
        mvValues.add(AccountDesFiscalId);
        mvValues.add(AccountDesNumber);
    }
}
