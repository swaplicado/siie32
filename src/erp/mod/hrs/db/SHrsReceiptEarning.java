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
public class SHrsReceiptEarning implements SGridRow, Comparable {

    public static final int INPUT_BY_EMP = 1;
    public static final int INPUT_BY_EAR = 2;

    protected int mnInputMode;
    protected SHrsReceipt moHrsReceipt;
    protected SDbEarning moEarning;
    protected SDbPayrollReceiptEarning moPayrollReceiptEarning;
    protected boolean mbApplying;
    protected double mdAmountOriginal;
    protected double mdAmountBeingEdited;

    public SHrsReceiptEarning() {
        this(INPUT_BY_EMP);
    }

    public SHrsReceiptEarning(final int inputMode) {
        mnInputMode = inputMode;
        moHrsReceipt = null;
        moEarning = null;
        moPayrollReceiptEarning = null;
        mbApplying = false;
        mdAmountOriginal = Double.NaN;
        mdAmountBeingEdited = Double.NaN;
    }

    public void setHrsReceipt(SHrsReceipt o) { moHrsReceipt = o; }
    public void setEarning(SDbEarning o) { moEarning = o; }
    public void setPayrollReceiptEarning(SDbPayrollReceiptEarning o) { moPayrollReceiptEarning = o; mdAmountOriginal = o.getAmount_r(); evaluateApplying(); }

    public int getInputMode() { return mnInputMode; }
    public SHrsReceipt getHrsReceipt() { return moHrsReceipt; }
    public SDbEarning getEarning() { return moEarning; }
    public SDbPayrollReceiptEarning getPayrollReceiptEarning() { return moPayrollReceiptEarning; }
    public boolean isApplying() { return mbApplying; }
    public double getAmountOriginal() { return mdAmountOriginal; }
    public double getAmountBeingEdited() { return mdAmountBeingEdited; }
    
    private void evaluateApplying() {
        mbApplying = moPayrollReceiptEarning.getUnits() != 0 && moPayrollReceiptEarning.getAmountUnitary() != 0;
    }
    
    private void computeAmount() {
        double amount = SHrsEmployeeDays.computeEarningAmount(moPayrollReceiptEarning.getUnits(), moPayrollReceiptEarning.getAmountUnitary(), moEarning);
        moPayrollReceiptEarning.setAmountSystem_r(amount);
        moPayrollReceiptEarning.setAmount_r(amount);
        
        evaluateApplying();
    }
    
    public void computeEarning() {
        if (!moPayrollReceiptEarning.isUserEdited()) {
            if (moEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_PCT_INCOME) {
                // update amount unit with current income in receipt; units alleged contains percentage to apply:
                moPayrollReceiptEarning.setAmountUnitary(SLibUtils.round(moHrsReceipt.getTotalEarningsDependentsDaysWorked(), SLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits()));
            }
            
            computeAmount();
        }
    }
    
    public boolean isEditableValueAlleged() {
        return moEarning.areUnitsModifiable();
    }
    
    public boolean isEditableAmountUnitary(final double amountUnitary) {
        return !moEarning.isBasedOnUnits() && (!moEarning.isBenefit() || amountUnitary < mdAmountOriginal || SLibUtils.compareAmount(amountUnitary, mdAmountOriginal));
    }
    
    private void updateValueAlleged(final double valueAlleged) {
        if (isEditableValueAlleged()) {
            if (!moPayrollReceiptEarning.isUserEdited() && valueAlleged != moPayrollReceiptEarning.getUnitsAlleged()) {
                moPayrollReceiptEarning.setUserEdited(true);
            }

            SHrsEmployeeDays hrsEmployeeDays = moHrsReceipt.getHrsEmployee().createEmployeeDays();
            moPayrollReceiptEarning.setUnitsAlleged(valueAlleged);
            moPayrollReceiptEarning.setUnits(hrsEmployeeDays.computeEarningUnits(valueAlleged, moEarning));
            
            computeAmount();
        }
    }

    private void updateAmountUnitary(final double amountUnitary) {
        mdAmountBeingEdited = amountUnitary;

        if (isEditableAmountUnitary(amountUnitary)) {
            if (!moPayrollReceiptEarning.isUserEdited() && amountUnitary != moPayrollReceiptEarning.getAmountUnitary()) {
                moPayrollReceiptEarning.setUserEdited(true);
            }

            moPayrollReceiptEarning.setAmountUnitary(amountUnitary);

            computeAmount();
        }
    }

    private void updateApplying(final boolean applying) {
        mbApplying = applying;

        if (!moPayrollReceiptEarning.isUserEdited()) {
            moPayrollReceiptEarning.setUserEdited(true);
        }

        if (!mbApplying) {
            if (moEarning.isBasedOnUnits()) {
                updateValueAlleged(0);
            }
            else {
                updateAmountUnitary(0);
            }
        }
    }
    
    public void clearAmount() {
        updateApplying(false);
    }
    
    @Override
    public SHrsReceiptEarning clone() throws CloneNotSupportedException {
        SHrsReceiptEarning clone = new SHrsReceiptEarning(this.getInputMode());
        
        clone.setHrsReceipt(this.getHrsReceipt()); // just pass the same object!
        clone.setEarning(this.getEarning()); // immutable object, there is no need to clone it
        clone.setPayrollReceiptEarning(this.getPayrollReceiptEarning().clone());
        
        return clone;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { moEarning.getPkEarningId(), moPayrollReceiptEarning.getPkEmployeeId(), moPayrollReceiptEarning.getPkMoveId() };
    }

    @Override
    public String getRowCode() {
        return moEarning.getCode();
    }

    @Override
    public String getRowName() {
        return moEarning.getName();
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
                    case 0: // earning number
                        value = moPayrollReceiptEarning.getPkMoveId();
                        break;
                    case 1: // earning name
                        value = moEarning.getName();
                        break;
                    case 2: // value alleged (units alleged), EDITABLE!
                        value = moPayrollReceiptEarning.getUnitsAlleged();
                        break;
                    case 3: // unit of measure
                        value = moHrsReceipt.getHrsPayroll().getEarningComputationTypesMap().get(moEarning.getFkEarningComputationTypeId());
                        break;
                    case 4: // amount unit, EDITABLE!
                        value = moPayrollReceiptEarning.getAmountUnitary();
                        break;
                    case 5: // amount
                        value = moPayrollReceiptEarning.getAmount_r();
                        break;
                    case 6: // value (units)
                        value = moPayrollReceiptEarning.getUnits();
                        break;
                    case 7: // unit of measure
                        value = moHrsReceipt.getHrsPayroll().getEarningComputationTypesMap().get(moEarning.getFkEarningComputationTypeId());
                        break;
                    case 8: // amount exempt
                        value = moPayrollReceiptEarning.getAmountExempt();
                        break;
                    case 9: // amount taxable
                        value = moPayrollReceiptEarning.getAmountTaxable();
                        break;
                    case 10: // loan description
                        value = moHrsReceipt.getHrsEmployee().getLoanDescription(moPayrollReceiptEarning.getFkLoanLoanId_n());
                        break;
                    default:
                }
                break;
                
            case INPUT_BY_EAR:
                switch (row) {
                    case 0: // employee name
                        value = moHrsReceipt.getHrsEmployee().getEmployee().getXtaEmployeeName();
                        break;
                    case 1: // value alleged (units alleged), EDITABLE!
                        value = moPayrollReceiptEarning.getUnitsAlleged();
                        break;
                    case 2: // unit of measure
                        value = moHrsReceipt.getHrsPayroll().getEarningComputationTypesMap().get(moEarning.getFkEarningComputationTypeId());
                        break;
                    case 3: // amount unit, EDITABLE!
                        value = moPayrollReceiptEarning.getAmountUnitary();
                        break;
                    case 4: // amount
                        value = moPayrollReceiptEarning.getAmount_r();
                        break;
                    case 5: // value (units)
                        value = moPayrollReceiptEarning.getUnits();
                        break;
                    case 6: // unit of measure
                        value = moHrsReceipt.getHrsPayroll().getEarningComputationTypesMap().get(moEarning.getFkEarningComputationTypeId());
                        break;
                    case 7: // is-paid editable flag, EDITABLE!
                        value = mbApplying;
                        break;
                    case 8: // amount exempt
                        value = moPayrollReceiptEarning.getAmountExempt();
                        break;
                    case 9: // amount taxable
                        value = moPayrollReceiptEarning.getAmountTaxable();
                        break;
                    case 10: // loan description
                        value = moHrsReceipt.getHrsEmployee().getLoanDescription(moPayrollReceiptEarning.getFkLoanLoanId_n());
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
                    case 0: // earning number
                    case 1: // earning name
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
                    case 6: // value (units)
                    case 7: // unit of measure
                    case 8: // loan description
                        break;
                        
                    default:
                }
                break;
                
            case INPUT_BY_EAR:
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
                    case 5: // value (units)
                    case 6: // unit of measure
                        break;
                        
                    case 7: // is-paid editable flag
                        updateApplying((boolean) value);
                        break;
                        
                    case 8: // loan description
                        break;
                        
                    default:
                }
                break;
                
            default:
        }
    }

    @Override
    public int compareTo(Object o) {
        String compareCode = ((SHrsReceiptEarning) o).getEarning().getCode();
        
        return this.moEarning.getCode().compareTo(compareCode);
    }
}
