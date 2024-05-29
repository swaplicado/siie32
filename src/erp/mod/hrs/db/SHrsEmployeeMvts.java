/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import java.util.ArrayList;

/**
 * Las instancias de esta clase son una estructura de datos
 * que contiene los valores de días de contratación y de percepciones gravables e impuestos compensados
 * para el procesamiento de impuestos.
 *
 * @author Sergio Flores
 */
public class SHrsEmployeeMvts {
    
    protected int mnDaysHired;
    
    /** Ordinary taxable earnings. */
    protected double mdTaxableEarnings;
    /** Taxable earnings by articule 174 of the RLISR. */
    protected double mdTaxableEarningsArt174;
    /** Tax compensated actually equals tax subsidy compensated! */
    protected double mdTaxCompensated;
    /** Tax subsidy compensated actually equals tax compensated! */
    protected double mdTaxSubsidyCompensated;
    
    /** Old-style assessed tax subsidy preserved for informative purposes! It will exist only in 2024, the transition year.*/
    protected double mdTaxSubsidyAssessedOldInformative;
    
    /** Accumulated earnings in year, current payroll receipt not included (applies only for earnings exempt computation.) */
    protected ArrayList<SHrsAccumulatedEarning> maHrsAccumulatedEarnings;
    /** Accumulated earnings in year, current payroll receipt not included (applies only for annual tax computation.) */
    protected ArrayList<SHrsAccumulatedEarning> maHrsAccumulatedEarningsByType;
    /** Accumulated deductions in year, current payroll receipt not included (applies only for deductions.) */
    protected ArrayList<SHrsAccumulatedDeduction> maHrsAccumulatedDeductions;
    /** Accumulated deductions in year, current payroll receipt not included (applies only for annual tax computation.) */
    protected ArrayList<SHrsAccumulatedDeduction> maHrsAccumulatedDeductionsByType;
    
    public SHrsEmployeeMvts() {
        mnDaysHired = 0;
        
        mdTaxableEarnings = 0;
        mdTaxableEarningsArt174 = 0;
        mdTaxCompensated = 0;
        mdTaxSubsidyCompensated = 0;
        
        mdTaxSubsidyAssessedOldInformative = 0;
        
        maHrsAccumulatedEarnings = new ArrayList<>();
        maHrsAccumulatedEarningsByType = new ArrayList<>();
        maHrsAccumulatedDeductions = new ArrayList<>();
        maHrsAccumulatedDeductionsByType = new ArrayList<>();
    }
    
    public void setDaysHired(int n) { mnDaysHired = n; }
    
    /** Ordinary taxable earnings. */
    public void setTaxableEarnings(double d) { mdTaxableEarnings = d; }
    /** Taxable earnings by articule 174 of the RLISR. */
    public void setTaxableEarningsArt174(double d) { mdTaxableEarningsArt174 = d; }
    /** Tax compensated actually equals tax subsidy compensated! */
    public void setTaxCompensated(double d) { mdTaxCompensated = d; }
    /** Tax subsidy compensated actually equals tax compensated! */
    public void setTaxSubsidyCompensated(double d) { mdTaxSubsidyCompensated = d; }
    
    /** Old-style assessed tax subsidy preserved for informative purposes! It will exist only in 2024, the transition year.*/
    public void setTaxSubsidyAssessedOldInformative(double d) { mdTaxSubsidyAssessedOldInformative = d; }
    
    public int getDaysHired() { return mnDaysHired; }
    
    /** Ordinary taxable earnings. */
    public double getTaxableEarnings() { return mdTaxableEarnings; }
    /** Taxable earnings by articule 174 of the RLISR. */
    public double getTaxableEarningsArt174() { return mdTaxableEarningsArt174; }
    /** Tax compensated actually equals tax subsidy compensated! */
    public double getTaxCompensated() { return mdTaxCompensated; }
    /** Tax subsidy compensated actually equals tax compensated! */
    public double getTaxSubsidyCompensated() { return mdTaxSubsidyCompensated; }
    
    /** Old-style assessed tax subsidy preserved for informative purposes! It will exist only in 2024, the transition year.*/
    public double getTaxSubsidyAssessedOldInformative() { return mdTaxSubsidyAssessedOldInformative; }
    
    /** Accumulated earnings in year, current payroll receipt not included (applies only for earnings exempt computation.) */
    public ArrayList<SHrsAccumulatedEarning> getHrsAccumulatedEarnings() { return maHrsAccumulatedEarnings; }
    /** Accumulated earnings in year, current payroll receipt not included (applies only for annual tax computation.) */
    public ArrayList<SHrsAccumulatedEarning> getHrsAccumulatedEarningsByType() { return maHrsAccumulatedEarningsByType; }
    /** Accumulated deductions in year, current payroll receipt not included (applies only for deductions.) */
    public ArrayList<SHrsAccumulatedDeduction> getHrsAccumulatedDeductions() { return maHrsAccumulatedDeductions; }
    /** Accumulated deductions in year, current payroll receipt not included (applies only for annual tax computation.) */
    public ArrayList<SHrsAccumulatedDeduction> getHrsAccumulatedDeductionsByType() { return maHrsAccumulatedDeductionsByType; }
    
    /**
     * Get accumulated earning by its ID.
     * @param earningId ID of desired earning.
     * @return 
     */
    public SHrsAccumulatedEarning getHrsAccumulatedEarning(final int earningId) {
        SHrsAccumulatedEarning hrsAccumulatedEarning = null;

        for (SHrsAccumulatedEarning earning : maHrsAccumulatedEarnings) {
            if (earning.getEarningId() == earningId) {
                hrsAccumulatedEarning = earning;
                break;
            }
        }

        return hrsAccumulatedEarning;
    }

    /**
     * Get accumulated earning by its earning type.
     * @param earningType ID of desired earning type.
     * @return 
     */
    public SHrsAccumulatedEarning getHrsAccumulatedEarningByType(final int earningType) {
        SHrsAccumulatedEarning hrsAccumulatedEarning = null;

        for (SHrsAccumulatedEarning earning : maHrsAccumulatedEarningsByType) {
            if (earning.getEarningId() == earningType) {
                hrsAccumulatedEarning = earning;
                break;
            }
        }

        return hrsAccumulatedEarning;
    }

    /**
     * Get accumulated deduction by its ID.
     * @param deductionId ID of desired deduction.
     * @return 
     */
    public SHrsAccumulatedDeduction getHrsAccumulatedDeduction(final int deductionId) {
        SHrsAccumulatedDeduction hrsAccumulatedDeduction = null;

        for (SHrsAccumulatedDeduction deduction : maHrsAccumulatedDeductions) {
            if (deduction.getDeductionId() == deductionId) {
                hrsAccumulatedDeduction = deduction;
                break;
            }
        }

        return hrsAccumulatedDeduction;
    }

    /**
     * Get accumulated deduction by its deduction type.
     * @param deductionType ID of desired deduction type.
     * @return 
     */
    public SHrsAccumulatedDeduction getHrsAccumulatedDeductionByType(final int deductionType) {
        SHrsAccumulatedDeduction hrsAccumulatedDeduction = null;

        for (SHrsAccumulatedDeduction deduction : maHrsAccumulatedDeductionsByType) {
            if (deduction.getDeductionId() == deductionType) {
                hrsAccumulatedDeduction = deduction;
                break;
            }
        }

        return hrsAccumulatedDeduction;
    }
}
