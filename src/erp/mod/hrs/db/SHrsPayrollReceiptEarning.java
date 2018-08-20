/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModSysConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Néstor Ávalos, Juan Barajas, Sergio Flores
 */
public class SHrsPayrollReceiptEarning implements SGridRow, Comparable {

    public static final int INPUT_BY_EMP = 1;
    public static final int INPUT_BY_EAR = 2;

    protected SDbEarning moEarning;
    protected SDbPayrollReceiptEarning moPayrollReceiptEarning;
    protected SHrsPayrollReceipt moHrsPayrollReceipt;

    protected int mnPkMoveId;
    protected int mnInputMode;
    protected boolean mbPayment;

    protected String msXtaEmployee;
    protected double mdXtaValueAlleged;
    protected double mdXtaValue;
    protected double mdXtaAmount;
    protected String msXtaUnit;
    protected String msXtaLoan;

    public SHrsPayrollReceiptEarning() {
        moEarning = null;
        moPayrollReceiptEarning = null;
        moHrsPayrollReceipt = null;

        mnPkMoveId = 0;
        mnInputMode = INPUT_BY_EMP;
        mbPayment = false;

        msXtaEmployee = "";
        mdXtaValueAlleged = 0;
        mdXtaValue = 0;
        mdXtaAmount = 0;
        msXtaUnit = "";
        msXtaLoan = "";
    }

    public void setEarning(SDbEarning o) { moEarning = o; }
    public void setReceiptEarning(SDbPayrollReceiptEarning o) { moPayrollReceiptEarning = o; }
    public void setHrsReceipt(SHrsPayrollReceipt o) { moHrsPayrollReceipt = o; }

    public void setPkMoveId(int n) { mnPkMoveId = n; }
    public void setInputMode(int n) { mnInputMode = n; }
    public void setPayment(boolean b) { mbPayment = b; }

    public void setXtaEmployee(String s) { msXtaEmployee = s; }
    public void setXtaValueAlleged(double d) { mdXtaValueAlleged = d; }
    public void setXtaValue(double d) { mdXtaValue = d; }
    public void setXtaAmount(double d) { mdXtaAmount = d; }
    public void setXtaUnit(String s) { msXtaUnit = s; }
    public void setXtaLoan(String s) { msXtaLoan = s; }

    public SDbEarning getEarning() { return moEarning; }
    public SDbPayrollReceiptEarning getReceiptEarning() { return moPayrollReceiptEarning; }
    public SHrsPayrollReceipt getHrsReceipt() { return moHrsPayrollReceipt; }

    public int getPkMoveId() { return mnPkMoveId; }
    public int getInputMode() { return mnInputMode; }
    public boolean isPayment() { return mbPayment; }

    public String getXtaEmployee() { return msXtaEmployee; }
    public double getXtaValueAlleged() { return mdXtaValueAlleged; }
    public double getXtaValue() { return mdXtaValue; }
    public double getXtaAmount() { return mdXtaAmount; }
    public String getXtaUnit() { return msXtaUnit; }
    public String getXtaLoan() { return msXtaLoan; }
    
    private void computeAmount() {
        moPayrollReceiptEarning.setAmount_r(SLibUtils.roundAmount((moPayrollReceiptEarning.getUnits() * moPayrollReceiptEarning.getAmountUnitary() * moPayrollReceiptEarning.getFactorAmount())));
    }
    
    public void computeEarning() {
        if (!moPayrollReceiptEarning.isUserEdited()) {
            if (moEarning.isDaysWorkedBasedOn()) {
                moPayrollReceiptEarning.setAmountUnitary(moHrsPayrollReceipt.getTotalEarningsDependentsDaysWorked() * moEarning.getPayPercentage());
                computeAmount();
            }
        }
    }
    
    public SHrsPayrollReceiptEarning clone() throws CloneNotSupportedException {
        SHrsPayrollReceiptEarning hrsPayrollReceiptEarning = new SHrsPayrollReceiptEarning();
        
        hrsPayrollReceiptEarning.setEarning(this.getEarning()); // immutable objec
        hrsPayrollReceiptEarning.setReceiptEarning(this.getReceiptEarning().clone());
        hrsPayrollReceiptEarning.setHrsReceipt(this.getHrsReceipt());

        hrsPayrollReceiptEarning.setPkMoveId(this.getPkMoveId());
        hrsPayrollReceiptEarning.setInputMode(this.getInputMode());
        hrsPayrollReceiptEarning.setPayment(this.isPayment());

        hrsPayrollReceiptEarning.setXtaEmployee(this.getXtaEmployee());
        hrsPayrollReceiptEarning.setXtaValueAlleged(this.getXtaValueAlleged());
        hrsPayrollReceiptEarning.setXtaValue(this.getXtaValue());
        hrsPayrollReceiptEarning.setXtaAmount(this.getXtaAmount());
        hrsPayrollReceiptEarning.setXtaUnit(this.getXtaUnit());
        hrsPayrollReceiptEarning.setXtaLoan(this.getXtaLoan());
        
        return hrsPayrollReceiptEarning;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { moEarning.getPkEarningId(), moHrsPayrollReceipt.getHrsEmployee().getEmployee().getPkEmployeeId(), mnPkMoveId };
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet.");
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
                    case 0:
                        value = mnPkMoveId;
                        break;
                    case 1:
                        value = moEarning.getName();
                        break;
                    case 2:
                        value = mdXtaValueAlleged;
                        break;
                    case 3:
                        value = msXtaUnit;
                        break;
                    case 4:
                        value = moPayrollReceiptEarning.getAmountUnitary();
                        break;
                    case 5:
                       value = moPayrollReceiptEarning.getAmount_r();
                        break;
                    case 6:
                        value = mdXtaValue;
                        break;
                    case 7:
                        value = msXtaUnit;
                        break;
                    case 8:
                        value = msXtaLoan;
                        break;
                    default:
                }
                break;
                
            case INPUT_BY_EAR:
                switch (row) {
                    case 0:
                        value = msXtaEmployee;
                        break;
                    case 1:
                        value = mdXtaValueAlleged;
                        mbPayment = mdXtaValueAlleged != 0;
                        break;
                    case 2:
                        value = msXtaUnit;
                        break;
                    case 3:
                        value = moPayrollReceiptEarning.getAmountUnitary();
                        break;
                    case 4:
                        value = moPayrollReceiptEarning.getAmount_r();
                        break;
                    case 5:
                        value = mdXtaValue;
                        mbPayment = mdXtaValue != 0;
                        break;
                    case 6:
                        value = msXtaUnit;
                        break;
                    case 7:
                        value = mbPayment;
                        break;
                    case 8:
                        value = msXtaLoan;
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
                    case 0: // row number
                    case 1: // earning name
                        break;
                        
                    case 2: // alleged units (raw units), without adjustments
                        if (moEarning.areUnitsModifiable()) {
                            mdXtaValueAlleged = (double) value;

                            moPayrollReceiptEarning.setUserEdited(mdXtaValueAlleged != moPayrollReceiptEarning.getUnitsAlleged());
                            
                            moPayrollReceiptEarning.setUnitsAlleged(mdXtaValueAlleged);
                            
                            // if computation type is based on days, and adjustment may be needed:
                            moPayrollReceiptEarning.setUnits(SLibUtils.roundAmount(
                                    moEarning.getFkEarningComputationTypeId() != SModSysConsts.HRSS_TP_EAR_COMP_DAYS ? 
                                            mdXtaValueAlleged : 
                                            (!moEarning.isDaysAdjustment() ? 
                                                    mdXtaValueAlleged * moHrsPayrollReceipt.getHrsEmployee().getEmployeeDays().getFactorCalendar() : 
                                                    mdXtaValueAlleged * moHrsPayrollReceipt.getHrsEmployee().getEmployeeDays().getFactorCalendar() * moHrsPayrollReceipt.getHrsEmployee().getEmployeeDays().getFactorDaysPaid())));

                            try {
                                computeAmount();
                                moHrsPayrollReceipt.computeReceipt();
                            }
                            catch (Exception e) {
                                SLibUtils.printException(this, e);
                            }
                        }
                        break;
                        
                    case 3: // unit
                    case 4: // amount unitary
                        break;
                        
                    case 5: // amount
                        if ((moEarning.getFkBenefitTypeId() != SModSysConsts.HRSS_TP_BEN_NON && 
                                (double) value >= moPayrollReceiptEarning.getAmount_r()) ||
                                moEarning.getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_NON) {
                            if ((moEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT || 
                                moEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_PCT_INCOME) && 
                                    moEarning.getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_NON) {
                                moPayrollReceiptEarning.setUserEdited((double) value != moPayrollReceiptEarning.getAmountUnitary());
                                moPayrollReceiptEarning.setAmountUnitary((double) value);

                                if ((double) value > 0) {
                                    moPayrollReceiptEarning.setUnitsAlleged(1d);
                                    moPayrollReceiptEarning.setUnits(1d);
                                }
                                else {
                                    moPayrollReceiptEarning.setUnitsAlleged(0d);
                                    moPayrollReceiptEarning.setUnits(0d);
                                }

                                computeAmount();
                            }
                            else {
                                moPayrollReceiptEarning.setUserEdited((double) value != moPayrollReceiptEarning.getAmount_r());
                                moPayrollReceiptEarning.setAmount_r((double) value);
                            }
                            try {
                                moHrsPayrollReceipt.computeReceipt();
                            }
                            catch (Exception e) {
                                SLibUtils.printException(this, e);
                            }
                        }
                        else {
                            mdXtaAmount = (double) value;
                        }
                        break;
                    case 6:
                        // Value alleged:
                        break;
                    case 7:
                        // Unit:
                        break;
                    case 8:
                        // Loan:
                        break;
                    default:
                }
                break;
                
            case INPUT_BY_EAR:
                switch (row) {
                    case 0:
                        // Employee:
                        break;
                    case 1:
                        if ((moEarning.getFkEarningComputationTypeId() != SModSysConsts.HRSS_TP_EAR_COMP_AMT &&
                                    moEarning.getFkEarningComputationTypeId() != SModSysConsts.HRSS_TP_EAR_COMP_PCT_INCOME) &&
                                (moEarning.getFkAbsenceClassId_n() == SLibConsts.UNDEFINED && moEarning.getFkAbsenceTypeId_n() == SLibConsts.UNDEFINED)) {
                            mdXtaValueAlleged = (double) value;

                            if (moEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT || moEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_PCT_INCOME) {
                                moPayrollReceiptEarning.setUserEdited(mdXtaValueAlleged != moPayrollReceiptEarning.getAmountUnitary());
                                moPayrollReceiptEarning.setAmountUnitary(mdXtaValueAlleged);
                            }
                            else {
                                moPayrollReceiptEarning.setUserEdited(mdXtaValueAlleged != moPayrollReceiptEarning.getUnitsAlleged());
                                moPayrollReceiptEarning.setUnitsAlleged(mdXtaValueAlleged);
                                moPayrollReceiptEarning.setUnits(SLibUtils.roundAmount(moEarning.getFkEarningComputationTypeId() != SModSysConsts.HRSS_TP_EAR_COMP_DAYS ? mdXtaValueAlleged : (!moEarning.isDaysAdjustment() ? mdXtaValueAlleged * moHrsPayrollReceipt.getHrsEmployee().getEmployeeDays().getFactorCalendar() : mdXtaValueAlleged * moHrsPayrollReceipt.getHrsEmployee().getEmployeeDays().getFactorCalendar() * moHrsPayrollReceipt.getHrsEmployee().getEmployeeDays().getFactorDaysPaid())));
                            }
                            // computeEarning(); XXX jbarajas 15/04/2015
                            computeAmount();
                            mbPayment = mdXtaValueAlleged != 0;
                        }
                        break;
                    case 2:
                        // Unit:
                        break;
                    case 3:
                        // Amount unitary:
                        break;
                    case 4:
                        // Amount:
                        if ((moEarning.getFkBenefitTypeId() != SModSysConsts.HRSS_TP_BEN_NON && 
                                (double) value >= moPayrollReceiptEarning.getAmount_r()) ||
                                moEarning.getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_NON) {
                            if ((moEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT ||
                                    moEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_PCT_INCOME) && 
                                    moEarning.getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_NON) {
                                moPayrollReceiptEarning.setUserEdited((double) value != moPayrollReceiptEarning.getAmountUnitary());
                                moPayrollReceiptEarning.setAmountUnitary((double) value);

                                if ((double) value > 0) {
                                    moPayrollReceiptEarning.setUnitsAlleged(1d);
                                    moPayrollReceiptEarning.setUnits(1d);
                                }
                                else {
                                    moPayrollReceiptEarning.setUnitsAlleged(0d);
                                    moPayrollReceiptEarning.setUnits(0d);
                                }

                                computeAmount();
                            }
                            else {
                                moPayrollReceiptEarning.setUserEdited((double) value != moPayrollReceiptEarning.getAmount_r());
                                moPayrollReceiptEarning.setAmount_r((double) value);
                            }
                            try {
                                moHrsPayrollReceipt.computeReceipt();
                            }
                            catch (Exception e) {
                                SLibUtils.printException(this, e);
                            }
                        }
                        else {
                            mdXtaAmount = (double) value;
                        }
                        break;
                    case 5:
                        // Value alleged:
                        break;
                    case 6:
                        // Unit:
                        break;
                    case 7:
                        mbPayment = (boolean) value;
                        mdXtaValueAlleged = !mbPayment ? 0 : mdXtaValueAlleged;
                        mdXtaValue = !mbPayment ? 0 : mdXtaValue;
                        moPayrollReceiptEarning.setUserEdited(true);

                        if (moEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT || moEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_PCT_INCOME) {
                            moPayrollReceiptEarning.setAmountUnitary(mdXtaValue);
                        }
                        else {
                            moPayrollReceiptEarning.setUnitsAlleged(mdXtaValueAlleged);
                            moPayrollReceiptEarning.setUnits(mdXtaValue);
                        }
                        if (!mbPayment) {
                            moPayrollReceiptEarning.setFkLoanEmployeeId_n(SLibConsts.UNDEFINED);
                            moPayrollReceiptEarning.setFkLoanLoanId_n(SLibConsts.UNDEFINED);
                            moPayrollReceiptEarning.setFkLoanTypeId_n(SLibConsts.UNDEFINED);
                        }
                        // computeEarning(); XXX jbarajas 15/04/2015
                        computeAmount();
                        break;
                    case 8:
                        // Loan:
                        break;
                    default:
                }
                break;
                
            default:
        }
    }

    @Override
    public int compareTo(Object o) {
        String compareCode = ((SHrsPayrollReceiptEarning) o).getEarning().getCode();
        
        return this.moEarning.getCode().compareTo(compareCode);
    }
}
