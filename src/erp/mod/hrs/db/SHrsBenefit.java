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
public class SHrsBenefit {
    
    public static final int VALIDATION_BENEFIT_TYPE = 1;
    public static final int VALIDATION_ABSENCE_TYPE = 2;
    
    public static final int VALID_DAYS_TO_PAY = 10;
    public static final int VALID_DAYS_TO_PAY_TOTAL = 20;
    public static final int VALID_DAYS_TABLE = 30;
    public static final int VALID_AMOUNT_TO_PAY = 40;
    public static final int VALID_AMOUNT_TO_PAY_TOTAL = 50;
    public static final int VALID_AMOUNT_TO_PAID_AMOUNT_SYS = 60;

    protected int mnBenefitType;
    protected int mnBenefitAnniversary;
    protected int mnBenefitYear;
    
    protected double mdBenefitDays;
    protected double mdBenefitBonusPct;
    
    protected double mdPaidDays;
    protected double mdPaidAmount;
    
    protected double mdReceiptDays;
    protected double mdReceiptAmount;

    public SHrsBenefit(int benefitType, int benefitAnniversary, int benefitYear) {
        mnBenefitType = benefitType;
        mnBenefitAnniversary = benefitAnniversary;
        mnBenefitYear = benefitYear;
        mdBenefitBonusPct = 1;
        
        mdBenefitDays = 0;
        mdBenefitBonusPct = 1;
        
        mdPaidDays = 0;
        mdPaidAmount = 0;
        
        mdReceiptDays = 0;
        mdReceiptAmount = 0;
    }
    
    public void setBenefitDays(double d) { mdBenefitDays = d; }
    public void setBenefitBonusPct(double d) { mdBenefitBonusPct = d; }
    
    public void setPaidDays(double d) { mdPaidDays = d; }
    public void setPaidAmount(double d) { mdPaidAmount = d; }
    
    public void setReceiptDays(double d) { mdReceiptDays = d; }
    public void setReceiptAmount(double d) { mdReceiptAmount = d; }

    public int getBenefitType() { return mnBenefitType; }
    public int getBenefitAnniversary() { return mnBenefitAnniversary; }
    public int getBenefitYear() { return mnBenefitYear; }
    
    public double getBenefitDays() { return mdBenefitDays; }
    public double getBenefitBonusPct() { return mdBenefitBonusPct; }
    
    public double getPaidDays() { return mdPaidDays; }
    public double getPaidAmount() { return mdPaidAmount; }
    
    public double getReceiptDays() { return mdReceiptDays; }
    public double getReceiptAmount() { return mdReceiptAmount; }
    
    public double getPendingDays() { return SLibUtils.round(mdBenefitDays - mdPaidDays - mdReceiptDays, SLibUtils.DecimalFormatValue8D.getMaximumFractionDigits()); }

    /**
     * Get benefit key.
     * @return Array of int containing: ID of benefit type, benefit anniversary and benefit year.
     */
    public int[] getBenefitKey() { return new int[] { mnBenefitType, mnBenefitAnniversary, mnBenefitYear }; } 
    
    @Override
    public SHrsBenefit clone() throws CloneNotSupportedException {
        SHrsBenefit clone = new SHrsBenefit(this.getBenefitType(), this.getBenefitAnniversary(), this.getBenefitYear());
        
        clone.setBenefitDays(this.getBenefitDays());
        clone.setBenefitBonusPct(this.getBenefitBonusPct());

        clone.setPaidDays(this.getPaidDays());
        clone.setPaidAmount(this.getPaidAmount());

        clone.setReceiptDays(this.getReceiptDays());
        clone.setReceiptAmount(this.getReceiptAmount());
        
        return clone;
    }
}
