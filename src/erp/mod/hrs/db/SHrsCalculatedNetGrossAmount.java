/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import sa.lib.SLibConsts;

/**
 *
 * @author Juan Barajas
 */
public class SHrsCalculatedNetGrossAmount {
    
    protected double mdSalary;
    protected double mdSalarySs;
    protected double mdNetAmountWithSubsidy;
    protected double mdNetAmount;
    protected double mdGrossAmount;
    protected double mdTaxAmount;
    protected double mdTaxSubsidyAmount;
    protected double mdSsContributionAmount;
    protected int mnCalculatedAmountType;

    public SHrsCalculatedNetGrossAmount(double netAmount, double grossAmount, double taxAmount, double taxSubsidyAmount, double ssContributionAmount) {
        mdNetAmount = netAmount;
        mdGrossAmount = grossAmount;
        mdTaxAmount = taxAmount;
        mdTaxSubsidyAmount = taxSubsidyAmount;
        mdSsContributionAmount = ssContributionAmount;
        mdNetAmountWithSubsidy = netAmount + taxSubsidyAmount;
        mnCalculatedAmountType = SLibConsts.UNDEFINED;
    }
    
    public void setSalary(double d) { mdSalary = d; }
    public void setSalarySs(double d) { mdSalarySs = d; }
    public void setNetAmountWithSubsidy(double d) { mdNetAmountWithSubsidy = d; }
    public void setNetAmount(double d) { mdNetAmount = d; }
    public void setGrossAmount(double d) { mdGrossAmount = d; }
    public void setTaxAmount(double d) { mdTaxAmount = d; }
    public void setTaxSubsidyAmount(double d) { mdTaxSubsidyAmount = d; }
    public void setSsContributionAmount(double d) { mdSsContributionAmount = d; }
    public void setCalculatedAmountType(int n) { mnCalculatedAmountType = n; }
    
    public double getSalary() { return mdSalary; }
    public double getSalarySs() { return mdSalarySs; }
    public double getNetAmountWithSubsidy() { return mdNetAmountWithSubsidy; }
    public double getNetAmount() { return mdNetAmount; }
    public double getGrossAmount() { return mdGrossAmount; }
    public double getTaxAmount() { return mdTaxAmount; }
    public double getTaxSubsidyAmount() { return mdTaxSubsidyAmount; }
    public double getSsContributionAmount() { return mdSsContributionAmount; }
    public int getCalculatedAmountType() { return mnCalculatedAmountType; }
    
    public SHrsCalculatedNetGrossAmount clone() throws CloneNotSupportedException {
        SHrsCalculatedNetGrossAmount registry = new SHrsCalculatedNetGrossAmount(SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, SLibConsts.UNDEFINED);
        
        registry.setSalary(this.getSalary());
        registry.setSalarySs(this.getSalarySs());
        registry.setNetAmountWithSubsidy(this.getNetAmountWithSubsidy());
        registry.setNetAmount(this.getNetAmount());
        registry.setGrossAmount(this.getGrossAmount());
        registry.setTaxAmount(this.getTaxAmount());
        registry.setTaxSubsidyAmount(this.getTaxSubsidyAmount());
        registry.setSsContributionAmount(this.getSsContributionAmount());
        registry.setCalculatedAmountType(this.getCalculatedAmountType());
        
        return registry;
    }
}
