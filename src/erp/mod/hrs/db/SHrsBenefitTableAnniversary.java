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
    protected int mnAnniversary;
    protected double mdValue; // can be days or bonus percentage

    public SHrsBenefitTableAnniversary(int benefitId, int anniversary, double value) {
        mnBenefitId = benefitId;
        mnAnniversary = anniversary;
        mdValue = value;
    }

    public void setBenefitId(int n) { mnBenefitId = n; }
    public void setAnniversary(int n) { mnAnniversary = n; }
    public void setValue(double d) { mdValue = d; }

    public int getBenefitId() { return mnBenefitId; }
    public int getAnniversary() { return mnAnniversary; }
    public double getValue() { return mdValue; }
}
