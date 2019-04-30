/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModSysConsts;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Néstor Ávalos, Juan Barajas, Sergio Flores
 */
public class SHrsReceiptDeduction implements SGridRow, Comparable {
    
    public static final int INPUT_BY_EMP = 1;
    public static final int INPUT_BY_DED = 2;
    
    protected int mnInputMode;
    protected SHrsReceipt moHrsReceipt;
    protected SDbDeduction moDeduction;
    protected SDbPayrollReceiptDeduction moPayrollReceiptDeduction;
    protected boolean mbApplying;
    protected double mdAmountOriginal;
    protected double mdAmountBeingEdited;
    
    public SHrsReceiptDeduction() {
        this(INPUT_BY_EMP);
    }
    
    public SHrsReceiptDeduction(int inputMode) {
        mnInputMode = inputMode;
        moHrsReceipt = null;
        moDeduction = null;
        moPayrollReceiptDeduction = null;
        mbApplying = false;
        mdAmountOriginal = Double.NaN;
        mdAmountBeingEdited = Double.NaN;
    }

    public void setHrsReceipt(SHrsReceipt o) { moHrsReceipt = o; }
    public void setDeduction(SDbDeduction o) { moDeduction = o; }
    public void setPayrollReceiptDeduction(SDbPayrollReceiptDeduction o) { moPayrollReceiptDeduction = o; mdAmountOriginal = o.getAmount_r(); }
    public void setApplying(boolean b) { mbApplying = b; }

    public int getInputMode() { return mnInputMode; }
    public SHrsReceipt getHrsReceipt() { return moHrsReceipt; }
    public SDbDeduction getDeduction() { return moDeduction; }
    public SDbPayrollReceiptDeduction getPayrollReceiptDeduction() { return moPayrollReceiptDeduction; }
    public boolean isApplying() { return mbApplying; }
    public double getAmountOriginal() { return mdAmountOriginal; }
    public double getAmountBeingEdited() { return mdAmountBeingEdited; }

    private void evaluateApplying() {
        mbApplying = moPayrollReceiptDeduction.getUnits() != 0 && moPayrollReceiptDeduction.getAmountUnitary() != 0;
    }

    private void computeAmount() {
        double amount = SLibUtils.roundAmount(moPayrollReceiptDeduction.getUnits() * moPayrollReceiptDeduction.getAmountUnitary());
        moPayrollReceiptDeduction.setAmountSystem_r(amount);
        moPayrollReceiptDeduction.setAmount_r(amount);
        
        evaluateApplying();
    }
    
    public void computeDeduction() {
        if (!moPayrollReceiptDeduction.isUserEdited()) {
            try {
                if (moDeduction.getFkDeductionComputationTypeId() == SModSysConsts.HRSS_TP_DED_COMP_PCT_INCOME) {
                    // update amount unit with current income in receipt; units alleged contains % to apply:
                    moPayrollReceiptDeduction.setAmountUnitary(SLibUtils.round(moHrsReceipt.getTotalEarningsDependentsDaysWorked(), SLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits()));
                }
                else if (moDeduction.isLoan()) {
                    SDbLoan loan = moHrsReceipt.getHrsEmployee().getLoan(moPayrollReceiptDeduction.getFkLoanLoanId_n());
                    moPayrollReceiptDeduction.setAmountUnitary(SHrsUtils.computeAmountLoan(moHrsReceipt, loan));
                }
                    
                computeAmount();
            }
            catch (Exception e) {
                SLibUtils.printException(this, e);
            }
        }
    }
    
    public boolean isEditableValueAlleged() {
        return moDeduction.areUnitsModifiable();
    }
    
    public boolean isEditableAmountUnitary(final double amountUnitary) {
        return !moDeduction.isBasedOnUnits() && (!moDeduction.isBenefit() || amountUnitary > mdAmountOriginal);
    }
    
    private void updateValueAlleged(final double valueAlleged) {
        if (isEditableValueAlleged()) {
            if (!moPayrollReceiptDeduction.isUserEdited() && valueAlleged != moPayrollReceiptDeduction.getUnitsAlleged()) {
                moPayrollReceiptDeduction.setUserEdited(true);
            }
            
            moPayrollReceiptDeduction.setUnitsAlleged(valueAlleged);
            moPayrollReceiptDeduction.setUnits(valueAlleged);
            
            computeAmount();
        }
    }
    
    private void updateAmountUnitary(final double amountUnitary) {
        mdAmountBeingEdited = amountUnitary;
        
        if (isEditableAmountUnitary(amountUnitary)) {
            if (!moPayrollReceiptDeduction.isUserEdited() && amountUnitary != moPayrollReceiptDeduction.getAmountUnitary()) {
                moPayrollReceiptDeduction.setUserEdited(true);
            }
            
            moPayrollReceiptDeduction.setAmountUnitary(amountUnitary);
            
            computeAmount();
        }
    }
    
    private void updateApplying(final boolean applying) {
        mbApplying = applying;

        if (!moPayrollReceiptDeduction.isUserEdited()) {
            moPayrollReceiptDeduction.setUserEdited(true);
        }

        if (!mbApplying) {
            if (moDeduction.isBasedOnUnits()) {
                updateValueAlleged(0);
            }
            else {
                updateAmountUnitary(0);
            }
        }

        computeAmount();
    }
    
    public SHrsReceiptDeduction clone() throws CloneNotSupportedException {
        SHrsReceiptDeduction clone = new SHrsReceiptDeduction(this.getInputMode());
        
        clone.setHrsReceipt(this.getHrsReceipt()); // just pass the same object!
        clone.setDeduction(this.getDeduction()); // immutable object, there is no need to clone it
        clone.setPayrollReceiptDeduction(this.getPayrollReceiptDeduction().clone());
        clone.setApplying(this.isApplying());
        
        return clone;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { moDeduction.getPkDeductionId(), moPayrollReceiptDeduction.getPkEmployeeId(), moPayrollReceiptDeduction.getPkMoveId() };
    }

    @Override
    public String getRowCode() {
        return moDeduction.getCode();
    }

    @Override
    public String getRowName() {
        return moDeduction.getName();
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch (mnInputMode) {
            case INPUT_BY_EMP:
                switch (row) {
                    case 0: // deduction number
                        value = moPayrollReceiptDeduction.getPkMoveId();
                        break;
                    case 1: // deduction name
                        value = moDeduction.getName();
                        break;
                    case 2: // value alleged (units alleged), EDITABLE!
                        value = moPayrollReceiptDeduction.getUnitsAlleged();
                        break;
                    case 3: // unit of measure
                        value = moHrsReceipt.getHrsPayroll().getDeductionComputationTypesMap().get(moDeduction.getFkDeductionComputationTypeId());
                        break;
                    case 4: // amount unit, EDITABLE!
                        value = moPayrollReceiptDeduction.getAmountUnitary();
                        break;
                    case 5: // amount
                        value = moPayrollReceiptDeduction.getAmount_r();
                        break;
                    case 6: // loan description
                        value = moHrsReceipt.getHrsEmployee().getLoanDescription(moPayrollReceiptDeduction.getFkLoanLoanId_n());
                        break;
                    default:
                }
                break;
                
            case INPUT_BY_DED:
                switch (row) {
                    case 0: // employee name
                        value = moHrsReceipt.getHrsEmployee().getEmployee().getAuxEmployee();
                        break;
                    case 1: // value alleged (units alleged), EDITABLE!
                        value = moPayrollReceiptDeduction.getUnitsAlleged();
                        break;
                    case 2: // unit of measure
                        value = moHrsReceipt.getHrsPayroll().getDeductionComputationTypesMap().get(moDeduction.getFkDeductionComputationTypeId());
                        break;
                    case 3: // amount unit, EDITABLE!
                        value = moPayrollReceiptDeduction.getAmountUnitary();
                        break;
                    case 4: // amount
                        value = moPayrollReceiptDeduction.getAmount_r();
                        break;
                    case 5: // is-paid editable flag, EDITABLE!
                        value = mbApplying;
                        break;
                    case 6: // loan description
                        value = moHrsReceipt.getHrsEmployee().getLoanDescription(moPayrollReceiptDeduction.getFkLoanLoanId_n());
                        break;
                    default:
                }
                break;
                
            default:
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch (mnInputMode) {
            case INPUT_BY_EMP:
                switch (row) {
                    case 0: // deduction number
                    case 1: // deduction name
                        break;
                        
                    case 2: // value alleged (units alleged)
                        updateValueAlleged((double) value);
                        break;
                        
                    case 3: // unit of measure
                        break;
                        
                    case 4: // amount unit
                        updateAmountUnitary((double) value);
                        break;
                        
                    case 5: // amount
                    case 6: // loan description
                        break;
                        
                    default:
                }
                break;
                
            case INPUT_BY_DED:
                switch (row) {
                    case 0: // employee name
                        break;
                        
                    case 1: // value alleged (units alleged)
                        updateValueAlleged((double) value);
                        break;
                        
                    case 2: // unit of measure
                        break;
                        
                    case 3: // amount unit
                        updateAmountUnitary((double) value);
                        break;
                        
                    case 4: // amount
                        updateAmountUnitary((double) value);
                        break;
                        
                    case 5: // is-paid editable flag
                        updateApplying((boolean) value);
                        break;
                        
                    case 6: // loan description
                        break;
                        
                    default:
                }
                break;
                
            default:
        }
    }

    @Override
    public int compareTo(Object o) {
        String compareCode = ((SHrsReceiptDeduction) o).getDeduction().getCode();
        
        return this.moDeduction.getCode().compareTo(compareCode);
    }
}
