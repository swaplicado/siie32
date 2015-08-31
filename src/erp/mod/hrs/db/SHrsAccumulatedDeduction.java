/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

/**
 *
 * @author Sergio Flores
 */
public class SHrsAccumulatedDeduction {

    protected int mnDeductionId;
    protected double mdAcummulated;

    public SHrsAccumulatedDeduction(int deduction, double acummulated) {
        mnDeductionId = deduction;
        mdAcummulated = acummulated;
    }

    public void setDeductionId(int n) { mnDeductionId = n; }
    public void setAcummulated(double d) { mdAcummulated = d; }

    public int getDeductionId() { return mnDeductionId; }
    public double getAcummulated() { return mdAcummulated; }
}
