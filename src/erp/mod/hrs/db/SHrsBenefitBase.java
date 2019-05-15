/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SHrsBenefitBase {
    
    protected int mnBenefitTypeId;
    protected int mnBenefitAnn;
    protected int mnBenefitYear;
    protected double mdFactorAmount;

    public SHrsBenefitBase(int benefitType, int benefitAnn, int benefitYear) {
        this(benefitType, benefitAnn, benefitYear, 1);
    }

    public SHrsBenefitBase(int benefitType, int benefitAnn, int benefitYear, double factorAmount) {
        mnBenefitTypeId = benefitType;
        mnBenefitAnn = benefitAnn;
        mnBenefitYear = benefitYear;
        mdFactorAmount = factorAmount;
    }

    public int getBenefitTypeId() { return mnBenefitTypeId; }
    public int getBenefitAnn() { return mnBenefitAnn; }
    public int getBenefitYear() { return mnBenefitYear; }
    public double getFactorAmount() { return mdFactorAmount; }
    
    /**
     * Get benefit key.
     * @return Array of int containing: ID of benefit type, benefit anniversary and benefit year.
     */
    public int[] getBenefitKey() { return new int[] { mnBenefitTypeId, mnBenefitAnn, mnBenefitYear }; } 
    
    @Override
    public SHrsBenefitBase clone() throws CloneNotSupportedException {
        SHrsBenefitBase clone = new SHrsBenefitBase(this.getBenefitTypeId(), this.getBenefitAnn(), this.getBenefitYear(), this.getFactorAmount());
        
        return clone;
    }
}
