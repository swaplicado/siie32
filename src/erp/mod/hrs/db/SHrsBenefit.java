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
 * @author Juan Barajas, Sergio Flores
 */
public class SHrsBenefit extends SHrsBenefitBase {
    
    public static final int VALIDATION_BENEFIT_TYPE = 1;
    public static final int VALIDATION_ABSENCE_TYPE = 2;
    
    public static final int VALID_DAYS_TO_PAY = 10;
    public static final int VALID_DAYS_TO_PAY_TOTAL = 20;
    public static final int VALID_DAYS_TABLE = 30;
    public static final int VALID_AMOUNT_TO_PAY = 40;
    public static final int VALID_AMOUNT_TO_PAY_TOTAL = 50;
    public static final int VALID_AMOUNT_TO_PAID_AMOUNT_SYS = 60;

    protected double mdValue;
    protected double mdValuePayed;
    protected double mdValuePayedReceipt;
    protected double mdAmount;
    protected double mdAmountPayed;
    protected double mdAmountPayedReceipt;
    protected double mdAmountPayedReceiptSys;

    public SHrsBenefit(int benefitType, int benefitAnn, int benefitYear) {
        this(benefitType, benefitAnn, benefitYear, 1);
    }

    public SHrsBenefit(int benefitType, int benefitAnn, int benefitYear, double factorAmount) {
        super(benefitType, benefitAnn, benefitYear, factorAmount);
        
        mdValue = 0;
        mdValuePayed = 0;
        mdValuePayedReceipt = 0;
        mdAmount = 0;
        mdAmountPayed = 0;
        mdAmountPayedReceipt = 0;
        mdAmountPayedReceiptSys = 0;
    }

    public void setValue(double d) { mdValue = d; }
    public void setValuePayed(double d) { mdValuePayed = d; }
    public void setValuePayedReceipt(double d) { mdValuePayedReceipt = d; }
    public void setAmount(double d) { mdAmount = d; }
    public void setAmountPayed(double d) { mdAmountPayed = d; }
    public void setAmountPayedReceipt(double d) { mdAmountPayedReceipt = d; }
    public void setAmountPayedReceiptSys(double d) { mdAmountPayedReceiptSys = d; }

    public double getValue() { return mdValue; }
    public double getValuePayed() { return mdValuePayed; }
    public double getValuePayedReceipt() { return mdValuePayedReceipt; }
    public double getAmount() { return mdAmount; }
    public double getAmountPayed() { return mdAmountPayed; }
    public double getAmountPayedReceipt() { return mdAmountPayedReceipt; }
    public double getAmountPayedReceiptSys() { return mdAmountPayedReceiptSys; }
    
    public double getValuePending() { return SLibUtils.round(mdValue - mdValuePayed - mdValuePayedReceipt, SLibUtils.DecimalFormatValue8D.getMaximumFractionDigits()) ; }
    public double getAmountPending() { return SLibUtils.roundAmount(mdAmount - mdAmountPayed - mdAmountPayedReceipt); }
    
    public String validate(int valid, int validationType) throws Exception {
        String msg = "";
        
        switch (valid) {
            case VALID_DAYS_TO_PAY:
                if (mdValuePayedReceipt == 0) {
                    msg = SGuiConsts.ERR_MSG_FIELD_REQ + "'" + (validationType == VALIDATION_BENEFIT_TYPE ? "días por pagar" : "días efectivos") + "'.";
                }
                break;
            case VALID_DAYS_TO_PAY_TOTAL:
                if (getValuePending() < 0) {
                    msg = "La suma de 'días pagados' + '" + (validationType == VALIDATION_BENEFIT_TYPE ? "días por pagar" : "días efectivos") + "' es mayor al valor del campo 'días'.";
                }
                break;
            case VALID_DAYS_TABLE:
                if (mdValuePayedReceipt > mdValue) {
                    msg = SGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + (validationType == VALIDATION_BENEFIT_TYPE ? "días por pagar" : "días efectivos") + "' es mayor al valor del campo 'días'.";
                }
                break;
            case VALID_AMOUNT_TO_PAY:
                if (mdAmountPayedReceipt == 0) {
                    msg = SGuiConsts.ERR_MSG_FIELD_REQ + "'monto por pagar'.";
                }
                break;
            case VALID_AMOUNT_TO_PAY_TOTAL:
                if (getAmountPending() < 0) {
                    msg = "La suma de 'monto pagado' + 'monto por pagar' es mayor al valor del campo 'monto'.";
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
    
    @Override
    public SHrsBenefit clone() throws CloneNotSupportedException {
        SHrsBenefit clone = new SHrsBenefit(this.getBenefitTypeId(), this.getBenefitAnn(), this.getBenefitYear(), this.getFactorAmount());
        
        clone.setValue(this.getValue());
        clone.setValuePayed(this.getValuePayed());
        clone.setValuePayedReceipt(this.getValuePayedReceipt());
        clone.setAmount(this.getAmount());
        clone.setAmountPayed(this.getAmountPayed());
        clone.setAmountPayedReceipt(this.getAmountPayedReceipt());
        clone.setAmountPayedReceiptSys(this.getAmountPayedReceiptSys());
        
        return clone;
    }
}
