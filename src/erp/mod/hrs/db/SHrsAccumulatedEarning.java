/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SHrsAccumulatedEarning {

    protected int mnEarningId;
    protected double mdAcummulated;
    protected double mdTaxable;

    public SHrsAccumulatedEarning(int earning, double accummulated, double taxable) {
        mnEarningId = earning;
        mdAcummulated = accummulated;
        mdTaxable = taxable;
    }

    public void setEarningId(int n) { mnEarningId = n; }
    public void setAcummulated(double d) { mdAcummulated = d; }
    public void setTaxable(double d) { mdTaxable = d; }

    public int getEarningId() { return mnEarningId; }
    public double getAcummulated() { return mdAcummulated; }
    public double getTaxable() { return mdTaxable; }
    public double getExemption() { return SLibUtils.roundAmount(mdAcummulated - mdTaxable); }
}
