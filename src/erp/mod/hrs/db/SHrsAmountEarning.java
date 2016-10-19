/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

/**
 *
 * @author Juan Barajas
 */
public class SHrsAmountEarning {

    protected int mnEarningId;
    protected double mdAmount;
    protected double mdAmountExempt;
    protected double mdAmountTaxable;
    
    public SHrsAmountEarning(int earning) {
        mnEarningId = earning;
        mdAmount = 0;
        mdAmountExempt = 0;
        mdAmountTaxable = 0;
    }
    
    public void setEarning(int n) { mnEarningId = n; }
    public void setAmount(double n) { mdAmount = n; }
    public void setAmountExempt(double n) { mdAmountExempt = n; }
    public void setAmountTaxable(double n) { mdAmountTaxable = n; }
    
    public int getEarning() { return mnEarningId; }
    public double getAmount() { return mdAmount; }
    public double getAmountExempt() { return mdAmountExempt; }
    public double getAmountTaxable() { return mdAmountTaxable; }
    
}
