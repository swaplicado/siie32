/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import java.util.ArrayList;

/**
 *
 * @author Néstor Ávalos
 */
public class SHrsPayrollReceiptAccumulatedEarnings {

    protected ArrayList<SHrsPayrollReceiptEarning> maHrsPayrollReceiptEarnings;
    protected double mdAmountEarnings_r;

    public SHrsPayrollReceiptAccumulatedEarnings() {
        maHrsPayrollReceiptEarnings = new ArrayList<SHrsPayrollReceiptEarning>();
        mdAmountEarnings_r = 0; // XXX validate
    }

    public void setAmountEarnings_r(double d) { mdAmountEarnings_r = d; }

    public ArrayList<SHrsPayrollReceiptEarning> getHrsPayrollReceiptEarnings() { return maHrsPayrollReceiptEarnings; }
    public double getAmountEarnigs_r() { return mdAmountEarnings_r; }
}
