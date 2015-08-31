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
 * @author Néstor Ávalos
 */
public class SHrsPayrollReceiptEarning implements SGridRow {

    public static final int BY_EMP = 1;
    public static final int BY_EAR = 2;

    protected SDbPayrollReceiptEarning moPayrollReceiptEarning;
    protected SDbEarning moEarning;
    protected SHrsPayrollReceipt moHrsPayrollReceipt;

    protected int mnPkMoveId;
    protected int mnRowType;
    protected boolean mbPayment;

    protected String msXtaEmployee;
    protected double mdXtaValueAlleged;
    protected double mdXtaValue;
    protected String msXtaUnit;
    protected String msXtaLoan;
    protected double mdXtaAmount;

    public SHrsPayrollReceiptEarning() {
        moPayrollReceiptEarning = null;
        moEarning = null;
        moHrsPayrollReceipt = null;

        mnPkMoveId = 0;
        mnRowType = BY_EMP;
        mbPayment = false;

        msXtaEmployee = "";
        mdXtaValueAlleged = 0;
        mdXtaValue = 0;
        msXtaUnit = "";
        msXtaLoan = "";
        mdXtaAmount = 0;
    }

    public void setReceiptEarning(SDbPayrollReceiptEarning o) { moPayrollReceiptEarning = o; }
    public void setEarning(SDbEarning o) { moEarning = o; }
    public void setHrsReceipt(SHrsPayrollReceipt o) { moHrsPayrollReceipt = o; }

    public void setPkMoveId(int n) { mnPkMoveId = n; }
    public void setRowType(int n) { mnRowType = n; }
    public void setPayment(boolean b) { mbPayment = b; }

    public void setXtaEmployee(String s) { msXtaEmployee = s; }
    public void setXtaValueAlleged(double d) { mdXtaValueAlleged = d; }
    public void setXtaValue(double d) { mdXtaValue = d; }
    public void setXtaUnit(String s) { msXtaUnit = s; }
    public void setXtaLoan(String s) { msXtaLoan = s; }
    public void setXtaAmount(double d) { mdXtaAmount = d; }

    public SDbPayrollReceiptEarning getReceiptEarning() { return moPayrollReceiptEarning; }
    public SDbEarning getEarning() { return moEarning; }
    public SHrsPayrollReceipt getHrsReceipt() { return moHrsPayrollReceipt; }

    public int getPkMoveId() { return mnPkMoveId; }
    public int getRowType() { return mnRowType; }
    public boolean isPayment() { return mbPayment; }

    public String getXtaEmployee() { return msXtaEmployee; }
    public double getXtaValueAlleged() { return mdXtaValueAlleged; }
    public double getXtaValue() { return mdXtaValue; }
    public String getXtaUnit() { return msXtaUnit; }
    public String getXtaLoan() { return msXtaLoan; }
    public double getXtaAmount() { return mdXtaAmount; }

    /* XXX jbarajas 15/04/2015
    public void computeEarning() {
        switch (moEarning.getFkEarningComputationTypeId()) {
            case SModSysConsts.HRSS_TP_EAR_COMP_AMT:
                moPayrollReceiptEarning.setUnitsAlleged(1);
                moPayrollReceiptEarning.setUnits(1);
                moPayrollReceiptEarning.setAmountSystem_r(SLibUtils.round((moPayrollReceiptEarning.getUnits() * moPayrollReceiptEarning.getAmountUnitary()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
                moPayrollReceiptEarning.setAmount_r(SLibUtils.round((moPayrollReceiptEarning.getUnits() * moPayrollReceiptEarning.getAmountUnitary()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
                break;
            case SModSysConsts.HRSS_TP_EAR_COMP_DAY:
                moPayrollReceiptEarning.setAmountUnitary(moHrsPayrollReceipt.getReceipt().getPaymentDaily());
                if (!moHrsPayrollReceipt.getHrsPayroll().getPayroll().isNormal()) {
                    moPayrollReceiptEarning.setAmountSystem_r(SLibUtils.round((moPayrollReceiptEarning.getUnits() * moPayrollReceiptEarning.getAmountUnitary() * moEarning.getUnitsFactor()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
                    moPayrollReceiptEarning.setAmount_r(SLibUtils.round((moPayrollReceiptEarning.getUnits() * moPayrollReceiptEarning.getAmountUnitary() * moEarning.getUnitsFactor()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
                }
                else {
                    moPayrollReceiptEarning.setAmountSystem_r(SLibUtils.round((moPayrollReceiptEarning.getUnits() * moPayrollReceiptEarning.getAmountUnitary() * moEarning.getUnitsFactor()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
                    moPayrollReceiptEarning.setAmount_r(SLibUtils.round(( moPayrollReceiptEarning.getUnits() * moPayrollReceiptEarning.getAmountUnitary() * moEarning.getUnitsFactor()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
                }
                break;
            case SModSysConsts.HRSS_TP_EAR_COMP_HRS:
                moPayrollReceiptEarning.setAmountUnitary(moHrsPayrollReceipt.getReceipt().getPaymentHourly());
                moPayrollReceiptEarning.setAmountSystem_r(SLibUtils.round((moPayrollReceiptEarning.getUnits() * moPayrollReceiptEarning.getAmountUnitary() * moEarning.getUnitsFactor()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
                moPayrollReceiptEarning.setAmount_r(SLibUtils.round((moPayrollReceiptEarning.getUnits() * moPayrollReceiptEarning.getAmountUnitary() * moEarning.getUnitsFactor()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
                break;
            default:
                break;
        }
    }
    */
    public void computeAmount() {
        if (moPayrollReceiptEarning.isUserEdited()) {
            moPayrollReceiptEarning.setAmount_r(SLibUtils.round((moPayrollReceiptEarning.getUnits() * moPayrollReceiptEarning.getAmountUnitary() * moEarning.getUnitsFactor()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
        }
    }
    
    public SHrsPayrollReceiptEarning clone() throws CloneNotSupportedException {
        SHrsPayrollReceiptEarning hrsPayrollReceiptEarning = new SHrsPayrollReceiptEarning();
        
        hrsPayrollReceiptEarning.setReceiptEarning(this.getReceiptEarning().clone());
        hrsPayrollReceiptEarning.setEarning(this.getEarning().clone());
        //hrsPayrollReceiptEarning.setHrsReceipt(this.getHrsReceipt());

        hrsPayrollReceiptEarning.setPkMoveId(this.getPkMoveId());
        hrsPayrollReceiptEarning.setRowType(this.getRowType());
        hrsPayrollReceiptEarning.setPayment(this.isPayment());

        hrsPayrollReceiptEarning.setXtaEmployee(this.getXtaEmployee());
        hrsPayrollReceiptEarning.setXtaValueAlleged(this.getXtaValueAlleged());
        hrsPayrollReceiptEarning.setXtaValue(this.getXtaValue());
        hrsPayrollReceiptEarning.setXtaUnit(this.getXtaUnit());
        hrsPayrollReceiptEarning.setXtaLoan(this.getXtaLoan());
        hrsPayrollReceiptEarning.setXtaAmount(this.getXtaAmount());
        
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

        if (mnRowType == BY_EAR) {
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
                    break;
            }
        }
        else {
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
                    break;
            }
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        if (mnRowType == BY_EAR) {
            switch (row) {
                case 0:
                    // Employee:
                    break;
                case 1:
                    if (moEarning.getFkEarningComputationTypeId() != SModSysConsts.HRSS_TP_EAR_COMP_AMT &&
                            (moEarning.getFkAbsenceClassId_n() == SLibConsts.UNDEFINED && moEarning.getFkAbsenceTypeId_n() == SLibConsts.UNDEFINED)) {
                        mdXtaValueAlleged = (double) value;

                        if (moEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT) {
                            moPayrollReceiptEarning.setUserEdited(mdXtaValueAlleged != moPayrollReceiptEarning.getAmountUnitary());
                            moPayrollReceiptEarning.setAmountUnitary(mdXtaValueAlleged);
                        }
                        else {
                            moPayrollReceiptEarning.setUserEdited(mdXtaValueAlleged != moPayrollReceiptEarning.getUnitsAlleged());
                            moPayrollReceiptEarning.setUnitsAlleged(mdXtaValueAlleged);
                            moPayrollReceiptEarning.setUnits(moEarning.getFkEarningComputationTypeId() != SModSysConsts.HRSS_TP_EAR_COMP_DAY ? mdXtaValueAlleged : (!moEarning.isDaysAdjustment() ? mdXtaValueAlleged * moHrsPayrollReceipt.getHrsEmployee().getEmployeeDays().getFactorCalendar() : mdXtaValueAlleged * moHrsPayrollReceipt.getHrsEmployee().getEmployeeDays().getFactorCalendar() * moHrsPayrollReceipt.getHrsEmployee().getEmployeeDays().getFactorDaysPaid()));
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
                        if (moEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT && 
                                moEarning.getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_NON) {
                            moPayrollReceiptEarning.setUserEdited((double) value != moPayrollReceiptEarning.getAmountUnitary());
                            moPayrollReceiptEarning.setAmountUnitary((double) value);
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

                    if (moEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT) {
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
                    break;
            }
        }
        else {
            switch (row) {
                case 0:
                    // Id:
                    break;
                case 1:
                    // Earning name:
                    break;
                case 2:
                    if (moEarning.getFkEarningComputationTypeId() != SModSysConsts.HRSS_TP_EAR_COMP_AMT &&
                            (moEarning.getFkAbsenceClassId_n() == SLibConsts.UNDEFINED && moEarning.getFkAbsenceTypeId_n() == SLibConsts.UNDEFINED)) {
                        mdXtaValueAlleged = (double) value;

                        if (moEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT) {
                            moPayrollReceiptEarning.setUserEdited(mdXtaValueAlleged != moPayrollReceiptEarning.getAmountUnitary());
                            moPayrollReceiptEarning.setAmountUnitary(mdXtaValueAlleged);
                        }
                        else {
                            moPayrollReceiptEarning.setUserEdited(mdXtaValueAlleged != moPayrollReceiptEarning.getUnitsAlleged());
                            moPayrollReceiptEarning.setUnitsAlleged(mdXtaValueAlleged);
                            moPayrollReceiptEarning.setUnits(moEarning.getFkEarningComputationTypeId() != SModSysConsts.HRSS_TP_EAR_COMP_DAY ? mdXtaValueAlleged : (!moEarning.isDaysAdjustment() ? mdXtaValueAlleged * moHrsPayrollReceipt.getHrsEmployee().getEmployeeDays().getFactorCalendar() : mdXtaValueAlleged * moHrsPayrollReceipt.getHrsEmployee().getEmployeeDays().getFactorCalendar() * moHrsPayrollReceipt.getHrsEmployee().getEmployeeDays().getFactorDaysPaid()));
                        }

                        try {
                            // XXX computeEarning();
                            computeAmount();
                            moHrsPayrollReceipt.computeReceipt();
                        }
                        catch (Exception e) {
                            SLibUtils.printException(this, e);
                        }
                    }
                    break;
                case 3:
                    // Unit:
                    break;
                case 4:
                    // Amount unitary:
                    break;
                case 5:
                    // Amount:
                    if ((moEarning.getFkBenefitTypeId() != SModSysConsts.HRSS_TP_BEN_NON && 
                            (double) value >= moPayrollReceiptEarning.getAmount_r()) ||
                            moEarning.getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_NON) {
                        if (moEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT && 
                                moEarning.getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_NON) {
                            moPayrollReceiptEarning.setUserEdited((double) value != moPayrollReceiptEarning.getAmountUnitary());
                            moPayrollReceiptEarning.setAmountUnitary((double) value);
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
                    break;
            }
        }
    }
}
