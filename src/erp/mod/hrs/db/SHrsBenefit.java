/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Juan Barajas
 */
public class SHrsBenefit {
    
    public static final int VALIDATION_BENEFIT_TYPE = 1;
    public static final int VALIDATION_ABSENCE_TYPE = 2;
    
    public static final int VALID_DAYS_TO_PAID = 10;
    public static final int VALID_DAYS_TO_PAID_TOTAL = 20;
    public static final int VALID_DAYS_TABLE = 30;
    public static final int VALID_AMOUNT_TO_PAID = 40;
    public static final int VALID_AMOUNT_TO_PAID_AMOUNT_SYS = 50;

    protected int mnBenefitTypeId;
    protected int mnBenefitAnn;
    protected int mnBenefitYear;
    protected double mdValue;
    protected double mdValuePayed;
    protected double mdValuePayedReceipt;
    protected double mdAmount;
    protected double mdAmountPayed;
    protected double mdAmountPayedReceipt;
    
    protected double mdAmountPayedReceiptSys;
    protected double mdFactorAmount;
    protected boolean mbIsEditAmount;

    public SHrsBenefit(int benefitType, int benefitAnn, int benefitYear) {
        mnBenefitTypeId = benefitType;
        mnBenefitAnn = benefitAnn;
        mnBenefitYear = benefitYear;
        
        mdValue = 0;
        mdValuePayed = 0;
        mdValuePayedReceipt = 0;
        mdAmount = 0;
        mdAmountPayed = 0;
        mdAmountPayedReceipt = 0;
        
        mdAmountPayedReceiptSys = 0;
        mdFactorAmount = 0;
        mbIsEditAmount = false;
    }

    public void setBenefitTypeId(int n) { mnBenefitTypeId = n; }
    public void setBenefitAnn(int n) { mnBenefitAnn = n; }
    public void setBenefitYear(int n) { mnBenefitYear = n; }
    public void setValue(double d) { mdValue = d; }
    public void setValuePayed(double d) { mdValuePayed = d; }
    public void setValuePayedReceipt(double d) { mdValuePayedReceipt = d; }
    public void setAmount(double d) { mdAmount = d; }
    public void setAmountPayed(double d) { mdAmountPayed = d; }
    public void setAmountPayedReceipt(double d) { mdAmountPayedReceipt = d; }
    
    public void setAmountPayedReceiptSys(double d) { mdAmountPayedReceiptSys = d; }
    public void setFactorAmount(double d) { mdFactorAmount = d; }
    public void setEditAmount(boolean b) { mbIsEditAmount = b; }

    public int[] getPrimaryBenefitType() { return new int[] { mnBenefitTypeId, mnBenefitAnn, mnBenefitYear }; } 
    
    public int getBenefitTypeId() { return mnBenefitTypeId; }
    public int getBenefitAnn() { return mnBenefitAnn; }
    public int getBenefitYear() { return mnBenefitYear; }
    public double getValue() { return mdValue; }
    public double getValuePayed() { return mdValuePayed; }
    public double getValuePayedReceipt() { return mdValuePayedReceipt; }
    public double getAmount() { return mdAmount; }
    public double getAmountPayed() { return mdAmountPayed; }
    public double getAmountPayedReceipt() { return mdAmountPayedReceipt; }
    
    public double getAmountPayedReceiptSys() { return mdAmountPayedReceiptSys; }
    public double getFactorAmount() { return mdFactorAmount; }
    public boolean isEditAmount() { return mbIsEditAmount; }
    
    public double getValuePending() { return SLibUtils.round((mdValue - mdValuePayed - mdValuePayedReceipt), SLibUtils.DecimalFormatValue4D.getMaximumFractionDigits()) ; }
    public double getAmountPending() { return SLibUtils.round((mdAmount - mdAmountPayed - mdAmountPayedReceipt), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()); }
    
    public String validate(int valid, int validationType) throws Exception {
        String msg = "";
        
        switch (valid) {
            case VALID_DAYS_TO_PAID:
                if (mdValuePayedReceipt == 0) {
                    msg = SGuiConsts.ERR_MSG_FIELD_REQ + "'" + (validationType == VALIDATION_BENEFIT_TYPE ? "días a pagar" : "días efectivos") + "'.";
                }
                break;
            case VALID_DAYS_TO_PAID_TOTAL:
                if (getValuePending() < 0) {
                    msg = "La suma de 'días pagados' + '" + (validationType == VALIDATION_BENEFIT_TYPE ? "días a pagar" : "días efectivos") + "' es mayor al valor del campo 'días'.";
                }
                break;
            case VALID_DAYS_TABLE:
                if (mdValuePayedReceipt > mdValue) {
                    msg = SGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + (validationType == VALIDATION_BENEFIT_TYPE ? "días a pagar" : "días efectivos") + "' es mayor al valor del campo 'días'.";
                }
                break;
            case VALID_AMOUNT_TO_PAID:
                if (mdAmountPayedReceipt == 0) {
                    msg = SGuiConsts.ERR_MSG_FIELD_REQ + "'monto a pagar'.";
                }
                break;
            case VALID_AMOUNT_TO_PAID_AMOUNT_SYS:
                if (mdAmountPayedReceipt != mdAmountPayedReceiptSys) {
                    msg = SGuiConsts.ERR_MSG_FIELD_VAL_ + "'monto a pagar' es diferente al monto calculado por sistema.";
                }
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return msg;
    }
    
    public SHrsBenefit clone() throws CloneNotSupportedException {
        SHrsBenefit hrsBenefit = new SHrsBenefit(SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, SLibConsts.UNDEFINED);
        
        hrsBenefit.setBenefitTypeId(this.getBenefitTypeId());
        hrsBenefit.setBenefitAnn(this.getBenefitAnn());
        hrsBenefit.setBenefitYear(this.getBenefitYear());
        hrsBenefit.setValue(this.getValue());
        hrsBenefit.setValuePayed(this.getValuePayed());
        hrsBenefit.setValuePayedReceipt(this.getValuePayedReceipt());
        hrsBenefit.setAmount(this.getAmount());
        hrsBenefit.setAmountPayed(this.getAmountPayed());
        hrsBenefit.setAmountPayedReceipt(this.getAmountPayedReceipt());

        hrsBenefit.setAmountPayedReceiptSys(this.getAmountPayedReceiptSys());
        hrsBenefit.setFactorAmount(this.getFactorAmount());
        hrsBenefit.setEditAmount(this.isEditAmount());
        
        return hrsBenefit;
    }
}
