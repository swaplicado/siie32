/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import erp.data.SDataConstantsSys;
import sa.lib.SLibUtils;
/**
 *
 * @author Uriel Castañeda
 */
public class SMoney {
    private int mnRoundingDecimals;
    private int mnCurrencyOriginalId;
    private int mnCurrencyLocalId;
    private double mdAmountOriginal;
    private double mdExchangeRate;

    private static final int ROUND_DEC = 2;

    public SMoney(double amountOriginal, int currencyOriginalId) {
        mnRoundingDecimals = ROUND_DEC;
        mnCurrencyOriginalId = currencyOriginalId;
        mnCurrencyLocalId = currencyOriginalId;
        mdAmountOriginal = amountOriginal;
        mdExchangeRate = (mnCurrencyOriginalId == mnCurrencyLocalId ? SDataConstantsSys.FINX_EXC_RATE_CUR_SYS : 0);
    }
    
    public SMoney(double amountOriginal, int currencyOriginalId, double exchangeRate, int currencyLocalId) {
        mnRoundingDecimals = ROUND_DEC;
        mnCurrencyOriginalId = currencyOriginalId;
        mnCurrencyLocalId = currencyLocalId;
        mdAmountOriginal = amountOriginal;
        mdExchangeRate = (mnCurrencyOriginalId == mnCurrencyLocalId ? SDataConstantsSys.FINX_EXC_RATE_CUR_SYS : exchangeRate);
    }
    
    public int getRoundingDecimals() { return mnRoundingDecimals; }
    public int getCurrencyOriginalId() { return mnCurrencyOriginalId; }
    public int getCurrencyLocalId() { return mnCurrencyLocalId; }
    public double getAmountOriginal() { return mdAmountOriginal; }
    public double getExchangeRate() { return mdExchangeRate; }

    public double getAmountLocal () {
        return SLibUtils.round(mdAmountOriginal * mdExchangeRate, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
    }
    
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setRoundingDecimals(int i) { mnRoundingDecimals = i; }
    public void setAmountOriginal(double d) { mdAmountOriginal = d; }
    
    public void addAmountOriginal(double d) {
        mdAmountOriginal = SLibUtils.round(mdAmountOriginal + d, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
    }
    
    public void subtractAmountOginal(double d) {
        mdAmountOriginal = SLibUtils.round(mdAmountOriginal - d, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
    }
}
