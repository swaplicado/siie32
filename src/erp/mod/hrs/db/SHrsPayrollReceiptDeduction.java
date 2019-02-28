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
public class SHrsPayrollReceiptDeduction implements SGridRow, Comparable {
    
    public static final int INPUT_BY_EMP = 1;
    public static final int INPUT_BY_DED = 2;
    
    protected SDbDeduction moDeduction;
    protected SDbPayrollReceiptDeduction moPayrollReceiptDeduction;
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
    
    public SHrsPayrollReceiptDeduction() {
        moDeduction = null;
        moPayrollReceiptDeduction = null;
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

    public void setDeduction(SDbDeduction o) { moDeduction = o; }
    public void setReceiptDeduction(SDbPayrollReceiptDeduction o) { moPayrollReceiptDeduction = o; }
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
    
    public SDbDeduction getDeduction() { return moDeduction; }
    public SDbPayrollReceiptDeduction getReceiptDeduction() { return moPayrollReceiptDeduction; }
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
        moPayrollReceiptDeduction.setAmountSystem_r(SLibUtils.roundAmount((moPayrollReceiptDeduction.getUnits() * moPayrollReceiptDeduction.getAmountUnitary())));
        moPayrollReceiptDeduction.setAmount_r(SLibUtils.roundAmount((moPayrollReceiptDeduction.getUnits() * moPayrollReceiptDeduction.getAmountUnitary())));
    }
    
    public void computeDeduction() {
        if (!moPayrollReceiptDeduction.isUserEdited()) {
            try {
                if (moDeduction.getFkDeductionComputationTypeId() == SModSysConsts.HRSS_TP_DED_COMP_PCT_INCOME) {
                    moPayrollReceiptDeduction.setAmountUnitary(moHrsPayrollReceipt.getTotalEarningsDependentsDaysWorked() * moDeduction.getRetPercentage());
                }
                else if (moPayrollReceiptDeduction.getFkLoanLoanId_n() != SLibConsts.UNDEFINED) {
                    SDbLoan loan = moHrsPayrollReceipt.getHrsEmployee().getLoan(moPayrollReceiptDeduction.getFkLoanLoanId_n());
                    moPayrollReceiptDeduction.setAmountUnitary(SHrsUtils.computeAmountLoan(moHrsPayrollReceipt, loan));
                }
                
                mdXtaValue = moPayrollReceiptDeduction.getAmountUnitary();

                moPayrollReceiptDeduction.setUnitsAlleged(1);
                moPayrollReceiptDeduction.setUnits(1);
                computeAmount();
            }
            catch (Exception e) {
                SLibUtils.printException(this, e);
            }
        }
    }
    
    public SHrsPayrollReceiptDeduction clone() throws CloneNotSupportedException {
        SHrsPayrollReceiptDeduction hrsPayrollReceiptDeduction = new SHrsPayrollReceiptDeduction();
        
        hrsPayrollReceiptDeduction.setDeduction(this.getDeduction());   // immutable object
        hrsPayrollReceiptDeduction.setReceiptDeduction(this.getReceiptDeduction().clone());
        hrsPayrollReceiptDeduction.setHrsReceipt(this.getHrsReceipt()); // 2018-07-27, Sergio Flores: just uncommented, it is unknown why was commented!

        hrsPayrollReceiptDeduction.setPkMoveId(this.getPkMoveId());
        hrsPayrollReceiptDeduction.setInputMode(this.getInputMode());
        hrsPayrollReceiptDeduction.setPayment(this.isPayment());

        hrsPayrollReceiptDeduction.setXtaEmployee(this.getXtaEmployee());
        hrsPayrollReceiptDeduction.setXtaValueAlleged(this.getXtaValueAlleged());
        hrsPayrollReceiptDeduction.setXtaValue(this.getXtaValue());
        hrsPayrollReceiptDeduction.setXtaAmount(this.getXtaAmount());
        hrsPayrollReceiptDeduction.setXtaUnit(this.getXtaUnit());
        hrsPayrollReceiptDeduction.setXtaLoan(this.getXtaLoan());
        
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

        switch (mnInputMode) {
            case INPUT_BY_EMP:
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
                
            case INPUT_BY_DED:
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
                    case 1: // deduction name
                        break;
                        
                    case 2: // value
                        if (moDeduction.areUnitsModifiable()) {
                            mdXtaValue = (double) value;

                            moPayrollReceiptDeduction.setUserEdited(mdXtaValue != moPayrollReceiptDeduction.getAmountUnitary());
                            
                            // units not yet supported, so allways are set to 1:
                            moPayrollReceiptDeduction.setUnitsAlleged(1);
                            moPayrollReceiptDeduction.setUnits(1);
                            
                            moPayrollReceiptDeduction.setAmountUnitary(mdXtaValue); // weird but true: "value" in GUI, that should be "units" in fact, is amount unitary!

                            try {
                                computeAmount();
                                moHrsPayrollReceipt.computeReceipt();
                            }
                            catch (Exception e) {
                                SLibUtils.printException(this, e);
                            }
                        }
                        break;
                        
                    case 3:
                    case 4:
                    case 5:
                        break;
                        
                    default:
                }
                break;
                
            case INPUT_BY_DED:
                switch (row) {
                    case 0:
                        break;
                        
                    case 1:
                        mdXtaValue = (double) value;

                        if (mdXtaValue != moPayrollReceiptDeduction.getAmountUnitary()) {
                            moPayrollReceiptDeduction.setUserEdited(true);
                        }
                        moPayrollReceiptDeduction.setAmountUnitary(mdXtaValue);

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
                
            default:
        }
    }

    @Override
    public int compareTo(Object o) {
        String compareCode = ((SHrsPayrollReceiptDeduction) o).getDeduction().getCode();
        
        return this.moDeduction.getCode().compareTo(compareCode);
    }
}
