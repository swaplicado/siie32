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
 * @author Néstor Ávalos, Sergio Flores
 */
public class SHrsPayrollReceiptDeduction implements SGridRow, Comparable {
    
    public static final int BY_EMP = 1;
    public static final int BY_DED = 2;
    
    protected SDbPayrollReceiptDeduction moPayrollReceiptDeduction;
    protected SDbDeduction moDeduction;
    protected SHrsPayrollReceipt moHrsPayrollReceipt;
    
    protected int mnPkMoveId;
    protected int mnRowType;
    protected boolean mbPayment;
    
    protected String msXtaEmployee;
    protected double mdXtaValue;
    protected double mdXtaAmount;
    protected String msXtaUnit;
    protected String msXtaLoan;
    
    public SHrsPayrollReceiptDeduction() {
        moPayrollReceiptDeduction = null;
        moDeduction = null;
        moHrsPayrollReceipt = null;
        
        mnPkMoveId = 0;
        mnRowType = BY_EMP;
        mbPayment = false;
        
        msXtaEmployee = "";
        mdXtaValue = 0;
        mdXtaAmount = 0;
        msXtaUnit = "";
        msXtaLoan = "";
    }

    public void setReceiptDeduction(SDbPayrollReceiptDeduction o) { moPayrollReceiptDeduction = o; }
    public void setDeduction(SDbDeduction o) { moDeduction = o; }
    public void setHrsReceipt(SHrsPayrollReceipt o) { moHrsPayrollReceipt = o; }
    
    public void setPkMoveId(int n) { mnPkMoveId = n; }
    public void setRowType(int n) { mnRowType = n; }
    public void setPayment(boolean b) { mbPayment = b; }

    public void setXtaEmployee(String s) { msXtaEmployee = s; }
    public void setXtaValue(double d) { mdXtaValue = d; }
    public void setXtaAmount(double d) { mdXtaAmount = d; }
    public void setXtaUnit(String s) { msXtaUnit = s; }
    public void setXtaLoan(String s) { msXtaLoan = s; }
    
    public SDbPayrollReceiptDeduction getReceiptDeduction() { return moPayrollReceiptDeduction; }
    public SDbDeduction getDeduction() { return moDeduction; }
    public SHrsPayrollReceipt getHrsReceipt() { return moHrsPayrollReceipt; }

    public int getPkMoveId() { return mnPkMoveId; }
    public int getRowType() { return mnRowType; }
    public boolean isPayment() { return mbPayment; }

    public String getXtaEmployee() { return msXtaEmployee; }
    public double getXtaValue() { return mdXtaValue; }
    public double getXtaAmount() { return mdXtaAmount; }
    public String getXtaUnit() { return msXtaUnit; }
    public String getXtaLoan() { return msXtaLoan; }

    public void computeDeduction() {
        if (!moPayrollReceiptDeduction.isUserEdited()) {
            try {
                if (moDeduction.getFkDeductionComputationTypeId() == SModSysConsts.HRSS_TP_DED_COMP_PER_EAR) {
                    moPayrollReceiptDeduction.setAmountUnitary(moHrsPayrollReceipt.getTotalEarningsDependentsDaysWorked() * moDeduction.getRetPercentage());
                    mdXtaValue = moPayrollReceiptDeduction.getAmountUnitary();
                }
                else if (moPayrollReceiptDeduction.getFkLoanLoanId_n() != SLibConsts.UNDEFINED) {
                    SDbLoan loan = moHrsPayrollReceipt.getHrsEmployee().getLoan(moPayrollReceiptDeduction.getFkLoanLoanId_n());
                    moPayrollReceiptDeduction.setAmountUnitary(SHrsUtils.computeAmoutLoan(moHrsPayrollReceipt, loan));
                    mdXtaValue = moPayrollReceiptDeduction.getAmountUnitary();
                }

                moPayrollReceiptDeduction.setUnitsAlleged(1);
                moPayrollReceiptDeduction.setUnits(1);
                moPayrollReceiptDeduction.setAmountSystem_r(SLibUtils.roundAmount((moPayrollReceiptDeduction.getUnits() * moPayrollReceiptDeduction.getAmountUnitary())));
                moPayrollReceiptDeduction.setAmount_r(SLibUtils.roundAmount((moPayrollReceiptDeduction.getUnits() * moPayrollReceiptDeduction.getAmountUnitary())));
            }
            catch (Exception e) {
                SLibUtils.printException(this, e);
            }
        }
    }
    
    public SHrsPayrollReceiptDeduction clone() throws CloneNotSupportedException {
        SHrsPayrollReceiptDeduction hrsPayrollReceiptDeduction = new SHrsPayrollReceiptDeduction();
        
        hrsPayrollReceiptDeduction.setReceiptDeduction(this.getReceiptDeduction().clone());
        hrsPayrollReceiptDeduction.setDeduction(this.getDeduction().clone());
        //hrsPayrollReceiptDeduction.setHrsReceipt(this.getHrsReceipt());

        hrsPayrollReceiptDeduction.setPkMoveId(this.getPkMoveId());
        hrsPayrollReceiptDeduction.setRowType(this.getRowType());
        hrsPayrollReceiptDeduction.setPayment(this.isPayment());

        hrsPayrollReceiptDeduction.setXtaEmployee(this.getXtaEmployee());
        hrsPayrollReceiptDeduction.setXtaValue(this.getXtaValue());
        hrsPayrollReceiptDeduction.setXtaUnit(this.getXtaUnit());
        hrsPayrollReceiptDeduction.setXtaLoan(this.getXtaLoan());
        hrsPayrollReceiptDeduction.setXtaAmount(this.getXtaAmount());
        
        return hrsPayrollReceiptDeduction;
    }

    @Override
    public int[] getRowPrimaryKey() {
       return new int[] { moDeduction.getPkDeductionId(), moHrsPayrollReceipt.getHrsEmployee().getEmployee().getPkEmployeeId(), mnPkMoveId };
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

        switch (mnRowType) {
            case BY_DED:
                switch (row) {
                    case 0:
                        value = msXtaEmployee;
                        break;
                    case 1:
                        value = mdXtaValue;
                        mbPayment = mdXtaValue != 0;
                        break;
                    case 2:
                        value = msXtaUnit;
                        break;
                    case 3:
                        value = moPayrollReceiptDeduction.getAmount_r();
                        break;
                    case 4:
                        value = mbPayment;
                        break;
                    case 5:
                        value = msXtaLoan;
                        break;
                    default:
                }
                break;
                
            case BY_EMP:
                switch (row) {
                    case 0:
                        value = mnPkMoveId;
                        break;
                    case 1:
                        value = moDeduction.getName();
                        break;
                    case 2:
                        value = mdXtaValue;
                        break;
                    case 3:
                        value = msXtaUnit;
                        break;
                    case 4:
                        value = moPayrollReceiptDeduction.getAmount_r();
                        break;
                    case 5:
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
        switch (mnRowType) {
            case BY_DED:
                switch (row) {
                    case 0:
                        break;
                        
                    case 1:
                        mdXtaValue = (double) value;

                        if (mdXtaValue != moPayrollReceiptDeduction.getAmountUnitary()) {
                            moPayrollReceiptDeduction.setUserEdited(true);
                        }
                        moPayrollReceiptDeduction.setAmountUnitary(mdXtaValue);

                        /*
                        // XXX (jbarajas, 2016-04-20) new field for computation type
                        if (moDeduction.getFkDeductionComputationTypeId() == SModSysConsts.HRSS_TP_DED_COMP_AMT) {
                            moPayrollReceiptDeduction.setAmountUnitary(mdXtaValue);
                        }
                        else {
                            moPayrollReceiptDeduction.setUnitsAlleged(mdXtaValue);
                            moPayrollReceiptDeduction.setUnits(mdXtaValue);
                        }
                        */

                        computeDeduction();
                        mbPayment = mdXtaValue != 0;
                        break;
                        
                    case 2:
                    case 3:
                        break;
                        
                    case 4:
                        mbPayment = (boolean) value;
                        mdXtaValue = !mbPayment ? 0 : mdXtaValue;
                        moPayrollReceiptDeduction.setAmountUnitary(mdXtaValue);

                        /*
                        // XXX (jbarajas, 2016-04-20) new field for computation type
                        if (moDeduction.getFkDeductionComputationTypeId() == SModSysConsts.HRSS_TP_DED_COMP_AMT) {
                            moPayrollReceiptDeduction.setAmountUnitary(mdXtaValue);
                        }
                        else {
                            moPayrollReceiptDeduction.setUnitsAlleged(mdXtaValue);
                            moPayrollReceiptDeduction.setUnits(mdXtaValue);
                        }
                        */

                        if (!mbPayment) {
                            moPayrollReceiptDeduction.setFkLoanEmployeeId_n(SLibConsts.UNDEFINED);
                            moPayrollReceiptDeduction.setFkLoanLoanId_n(SLibConsts.UNDEFINED);
                            moPayrollReceiptDeduction.setFkLoanTypeId_n(SLibConsts.UNDEFINED);
                        }
                        
                        computeDeduction();
                        break;
                        
                    case 5:
                        break;
                        
                    default:
                }
                break;
                
            case BY_EMP:
                switch (row) {
                    case 0:
                    case 1:
                        break;
                        
                    case 2:
                        mdXtaValue = (double) value;

                        if (mdXtaValue != moPayrollReceiptDeduction.getAmountUnitary()) {
                            moPayrollReceiptDeduction.setUserEdited(true);
                        }
                        moPayrollReceiptDeduction.setAmountUnitary(mdXtaValue);

                        computeDeduction();
                        break;
                        
                    case 3:
                    case 4:
                    case 5:
                        break;
                        
                    default:
                }
                break;
                
            default:
        }
    }

    @Override
    public int compareTo(Object o) {
        String compareCode = ((SHrsPayrollReceiptDeduction) o).getDeduction().getCode();
        
        return this.moDeduction.getCode().compareTo(compareCode);
    }
}
