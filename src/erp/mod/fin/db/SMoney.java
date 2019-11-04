/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
/**
 *
 * @author Uriel Castañeda, Alfredo Pérez, Sergio Flores
 */
public final class SMoney {
    
    private SGuiSession moSession;
    private double mdOriginalAmount;
    private int mnOriginalCurrencyId;
    private double mdExchangeRate;

    public SMoney(SGuiSession session, double originalAmount, int originalCurrencyId, double exchangeRate) {
        moSession = session;
        mdOriginalAmount = originalAmount;
        mnOriginalCurrencyId = originalCurrencyId;
        setExchangeRate(exchangeRate);
    }
    
    public double getOriginalAmount() { return mdOriginalAmount; }
    public int getOriginalCurrencyId() { return mnOriginalCurrencyId; }
    public double getExchangeRate() { return mdExchangeRate; }

    public void setOriginalAmount(double d) { mdOriginalAmount = d; }
    public void setOriginalCurrencyId(int n) { mnOriginalCurrencyId = n; }
    public void setExchangeRate(double exchangeRate) { mdExchangeRate = isLocalCurrency() ? 1d : exchangeRate; }
    
    public double getLocalAmount() {
        return SLibUtils.roundAmount(mdOriginalAmount * mdExchangeRate);
    }
    
    public boolean isLocalCurrency() {
        return moSession.getSessionCustom().isLocalCurrency(new int[] { mnOriginalCurrencyId });
    }
    
    @Override
    public SMoney clone() {
        SMoney clone = new SMoney(moSession, mdOriginalAmount, mnOriginalCurrencyId, mdExchangeRate);
        
        return clone;
    }
}
