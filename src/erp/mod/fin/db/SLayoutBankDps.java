/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import erp.mtrn.data.SDataDps;

/**
 *
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores, Isabel Servín
 */
public class SLayoutBankDps {

    protected SDataDps moDps;
    protected double mdPayment;
    protected double mdPaymentCy;
    protected String msEmail; // semicolon separated mail recipients

    public SLayoutBankDps(final SDataDps dps, final double payment, final double paymentCy, final String email) {
        moDps = dps;
        mdPayment = payment;
        mdPaymentCy = paymentCy;
        msEmail = email;
    }

    public SDataDps getDps() { return moDps; }
    public double getPayment() { return mdPayment; }
    public double getPaymentCy() { return mdPaymentCy; }
    public String getEmail() { return msEmail; }
    
    @Override
    public SLayoutBankDps clone() {
        SLayoutBankDps clone = new SLayoutBankDps(this.getDps(), this.getPayment(), this.getPaymentCy(), this.getEmail());
        
        return clone;
    }
}
