/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SHrsBenefitTableAnniversary {

    protected int mnBenefitId;
    protected int mnBenefitAnn;
    protected double mdValue; // can be days or bonus percentage

    public SHrsBenefitTableAnniversary(int benefitId, int benefitAnn, double value) {
        mnBenefitId = benefitId;
        mnBenefitAnn = benefitAnn;
        mdValue = value;
    }

    public void setBenefitId(int n) { mnBenefitId = n; }
    public void setBenefitAnn(int n) { mnBenefitAnn = n; }
    public void setValue(double d) { mdValue = d; }

    public int getBenefitId() { return mnBenefitId; }
    public int getBenefitAnn() { return mnBenefitAnn; }
    public double getValue() { return mdValue; }
}
