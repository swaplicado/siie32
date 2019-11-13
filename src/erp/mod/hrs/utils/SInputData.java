/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

/**
 *
 * @author Edwin Carmona
 */
public class SInputData {
    
    private String msUuidi;
    private double mdSubsidy;
    private double mdTax;
    
    public SInputData() {}
    
    public SInputData(final String sUuid, final double dSubsidy, final double dTax) {
        this.msUuidi = sUuid;
        this.mdSubsidy = dSubsidy;
        this.mdTax = dTax;
    }
    
    public void setUuid(String sUuid) {
        this.msUuidi = sUuid;
    }
    
    public void setSubsidy(double dSubsidy) {
        this.mdSubsidy = dSubsidy;
    }
    
    public void setTax(double dTax) {
        this.mdTax = dTax;
    }

    public String getUuid() {
        return msUuidi;
    }

    public double getSubsidy() {
        return mdSubsidy;
    }
    
    public double getTax() {
        return mdTax;
    }

}
