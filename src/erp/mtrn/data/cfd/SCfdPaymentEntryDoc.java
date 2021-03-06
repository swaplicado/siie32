/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data.cfd;

import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDps;
import java.util.HashMap;
import sa.lib.SLibUtils;

/**
 * GUI data structure for input of an individual document payment of a payment for CFDI of Payments.
 * Represents the XML element pago10:DoctoRelacionado, child of the element pago10:Pago.
 * @author Sergio Flores
 */
public final class SCfdPaymentEntryDoc extends erp.lib.table.STableRow {
    
    public static final int TYPE_PAY = 1;
    public static final int TYPE_INT = 2;
    public static final int TYPE_FEE = 3;
    public static final int TYPE_FEE_VAT = 4;
    
    public static final HashMap<Integer, String> Types = new HashMap<>();
    
    static {
        Types.put(TYPE_PAY, "Pago");
        Types.put(TYPE_INT, "Intereses");
        Types.put(TYPE_FEE, "Comisiones");
        Types.put(TYPE_FEE_VAT, "IVA comisiones");
    }
    
    public SCfdPaymentEntry ParentPaymentEntry;
    public SDataDps DataDps;
    
    public int Number;
    public int Type;
    public int Installment;
    public double DocBalancePrev;   // in original currency of document
    public double DocPayment;       // in original currency of document
    public double DocBalancePend;   // in original currency of document
    public double ExchangeRate;     // to exchange original currency of document into currency of payment
    public double PayBalancePrev;   // in currency of payment
    public double PayPayment;       // in currency of payment
    public double PayBalancePend;   // in currency of payment
    
    public double PayPaymentLimMin; // minimum limit according to Official Input Guide for CFDI with payments complement.
    public double PayPaymentLimMax; // maximum limit according to Official Input Guide for CFDI with payments complement.
    public double PayPaymentLocal;  // payment in local currency
    
    public int AuxGridIndex;
    
    public SCfdPaymentEntryDoc(SCfdPaymentEntry parentPaymentEntry, SDataDps dataDps, int number, int type, int installment, double balancePrev, double payment, double exchangeRate) {
        ParentPaymentEntry = parentPaymentEntry;
        DataDps = dataDps;
        
        Number = number;
        Type = type;
        Installment = installment;
        DocBalancePrev = balancePrev;
        DocPayment = payment;
        //DocBalancePend...     set in method computePaymentAmounts()
        ExchangeRate = exchangeRate;
        //PayBalancePrev...     set in method computePaymentAmounts()
        //PayPayment...         set in method computePaymentAmounts()
        //PayBalancePend...     set in method computePaymentAmounts()
        
        //PayPaymentLimMin...   set in method computePaymentAmounts()
        //PayPaymentLimMax...   set in method computePaymentAmounts()
        //PayPaymentLocal...    set in method computePaymentAmounts()
        
        AuxGridIndex = -1;
        
        computePaymentAmounts();
    }
    
    /**
     * Gets description of current type.
     */
    public String getTypeDescription() {
        return Types.get(Type);
    }

    /**
     * Computes pending balance in document currency.
     */
    public void computeBalancePend() {
        DocBalancePend = SLibUtils.roundAmount(DocBalancePrev - DocPayment);
    }
    
    /**
     * Computes pending balance in document currency as well as amounts in payment currency.
     */
    public void computePaymentAmounts() {
        computeBalancePend();
        if (ExchangeRate == 0) {
            // compute payment in terms of currency of payment:
            PayBalancePrev = 0;
            PayPayment = 0;
            PayBalancePend = 0;
            
            // compute minimum and maximum limits when totaling payment in terms of currency of payment due to rounding issues:
            PayPaymentLimMin = 0;
            PayPaymentLimMax = 0;
            
            // compute payment in local currency:
            PayPaymentLocal = 0;
        }
        else {
            // compute payment in terms of currency of payment:
            PayBalancePrev = SLibUtils.roundAmount(DocBalancePrev / ExchangeRate);
            PayPayment = SLibUtils.roundAmount(DocPayment / ExchangeRate);
            PayBalancePend = SLibUtils.roundAmount(DocBalancePend / ExchangeRate);
            
            // compute minimum and maximum limits when totaling payment in terms of currency of payment due to rounding issues:
            PayPaymentLimMin = (DocPayment - (Math.pow(10d, -2d) / 2d)) / (ExchangeRate + (Math.pow(10d, -2d) / 2d) - 0.0000000001);
            PayPaymentLimMax = (DocPayment + (Math.pow(10d, -2d) / 2d) - 0.0000000001) / (ExchangeRate - (Math.pow(10d, -2d) / 2d));
            
            // compute payment in local currency:
            if (DataDps.getFkCurrencyId() == SModSysConsts.CFGU_CUR_MXN) {
                // related document is in local currency
                PayPaymentLocal = DocPayment;
            }
            else if (ParentPaymentEntry.CurrencyId == SModSysConsts.CFGU_CUR_MXN) {
                // payment is in local currency
                PayPaymentLocal = PayPayment;
            }
            else {
                // compute amount with exchange rate of payment:
                PayPaymentLocal = SLibUtils.roundAmount(PayPayment * ParentPaymentEntry.ExchangeRate);
            }
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
        mvValues.add(DocBalancePrev);
        mvValues.add(DocPayment);
        mvValues.add(DocBalancePend);
        mvValues.add(DataDps.getDbmsCurrencyKey());
        mvValues.add(ExchangeRate);
        mvValues.add(PayPayment);
        mvValues.add(ParentPaymentEntry.CurrencyKey);
        mvValues.add(Types.get(Type));
    }
}
