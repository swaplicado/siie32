/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

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
        mdTaxable = taxable;
        mdAcummulated = accummulated;
    }

    public void setEarningId(int n) { mnEarningId = n; }
    public void setAcummulated(double d) { mdAcummulated = d; }
    public void setTaxable(double d) { mdTaxable = d; }

    public int getEarningId() { return mnEarningId; }
    public double getAcummulated() { return mdAcummulated; }
    public double getTaxable() { return mdTaxable; }
    public double getExemption() { return mdAcummulated - mdTaxable; }
}
