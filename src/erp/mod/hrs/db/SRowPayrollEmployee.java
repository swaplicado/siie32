/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SRowPayrollEmployee implements SGridRow {
    
    public static final int CASE_EMPLOYEE = 1;
    public static final int CASE_RECEIPT = 2;

    protected int mnRowCase;
    protected int mnPkEmployeeId;
    protected int mnFkPaymentTypeId;
    protected String msNumber;
    protected String msName;
    protected boolean mbActive;
    protected int mnRecruitmentSchemaTypeId;
    protected String msRecruitmentSchemaType;
    protected double mdTotalEarnings;
    protected double mdTotalDeductions;
    protected int mnBankId;
    protected String msBank; // name of bank from banks catalog of SAT
    protected String msBankAccount; // number of bank account
    protected SHrsReceipt moHrsReceipt;
    protected boolean mbShowRecruitmentSchemaIcon;

    /**
     * Creates new row payroll employee.
     * @param rowCase Row case: CASE_EMPLOYEE, CASE_RECEIPT.
     * @param showRecruitmentSchemaIcon Whether showing recruitment-schema icon is required or not.
     */
    public SRowPayrollEmployee(final int rowCase, final boolean showRecruitmentSchemaIcon) {
        mnRowCase = rowCase;
        mnPkEmployeeId = 0;
        mnFkPaymentTypeId = 0;
        msNumber = "";
        msName = "";
        mbActive = false;
        mnRecruitmentSchemaTypeId = 0;
        msRecruitmentSchemaType = "";
        mdTotalEarnings = 0;
        mdTotalDeductions = 0;
        mnBankId = 0;
        msBank = "";
        msBankAccount = "";
        moHrsReceipt = null;
        mbShowRecruitmentSchemaIcon = showRecruitmentSchemaIcon;
    }
    
    /**
     * Creates new row payroll employee.
     * @param row Row to copy.
     */
    public SRowPayrollEmployee(SRowPayrollEmployee row) {
        mnRowCase = row.getRowCase();
        mnPkEmployeeId = row.getPkEmployeeId();
        mnFkPaymentTypeId = row.getFkPaymentTypeId();
        msNumber = row.getNumber();
        msName = row.getName();
        mbActive = row.isActive();
        mnRecruitmentSchemaTypeId = row.getRecruitmentSchemaTypeId();
        msRecruitmentSchemaType = row.getRecruitmentSchemaType();
        mdTotalEarnings = row.getTotalEarnings();
        mdTotalDeductions = row.getTotalDeductions();
        mnBankId = row.getBankId();
        msBank = row.getBank();
        msBankAccount = row.getBankAccount();
        moHrsReceipt = row.getHrsReceipt();
        mbShowRecruitmentSchemaIcon = row.isShowRecruitmentSchemaIcon();
    }

    public void setRowCase(int n) { mnRowCase = n; }
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setFkPaymentTypeId(int n) { mnFkPaymentTypeId = n; }
    public void setNumber(String s) { msNumber = s; }
    public void setName(String s) { msName = s; }
    public void setActive(boolean b) { mbActive = b; }
    public void setRecruitmentSchemaTypeId(int n) { mnRecruitmentSchemaTypeId = n; }
    public void setRecruitmentSchemaType(String s) { msRecruitmentSchemaType = s; }
    public void setTotalEarnings(double d) { mdTotalEarnings = d; }
    public void setTotalDeductions(double d) { mdTotalDeductions = d; }
    public void setBankId(int n) { mnBankId = n; }
    public void setBank(String s) { msBank = s; }
    public void setBankAccount(String s) { msBankAccount = s; }
    public void setHrsReceipt(SHrsReceipt o) { moHrsReceipt = o; }
    public void setShowRecruitmentSchemaIcon(boolean b) { mbShowRecruitmentSchemaIcon = b; }

    public int getRowCase() { return mnRowCase; }
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getFkPaymentTypeId() { return mnFkPaymentTypeId; }
    public String getNumber() { return msNumber; }
    public String getName() { return msName; }
    public boolean isActive() { return mbActive; }
    public int getRecruitmentSchemaTypeId() { return mnRecruitmentSchemaTypeId; }
    public String getRecruitmentSchemaType() { return msRecruitmentSchemaType; }
    public double getTotalEarnings() { return mdTotalEarnings; }
    public double getTotalDeductions() { return mdTotalDeductions; }
    public int getBankId() { return mnBankId; }
    public String getBank() { return msBank; }
    public String getBankAccount() { return msBankAccount; }
    public SHrsReceipt getHrsReceipt() { return moHrsReceipt; }
    public boolean isShowRecruitmentSchemaIcon() { return mbShowRecruitmentSchemaIcon; }

    public double getTotalNet() {
        return SLibUtils.roundAmount(mdTotalEarnings - mdTotalDeductions);
    }
    
    public void clearReceipt() {
        mdTotalEarnings = 0;
        mdTotalDeductions = 0;
        moHrsReceipt = null;
    }
    
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkEmployeeId };
    }

    @Override
    public String getRowCode() {
        return getNumber();
    }

    @Override
    public String getRowName() {
        return getName();
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowEdited(final boolean edited) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;
        
        switch (mnRowCase) {
            case CASE_EMPLOYEE:
                switch(row) {
                    case 0:
                        value = msName;
                        break;
                    case 1:
                        value = msNumber;
                        break;
                    case 2:
                        value = mbActive;
                        break;
                    case 3:
                        value = msRecruitmentSchemaType;
                        break;
                    case 4:
                        value = SHrsUtils.getRecruitmentSchemaIcon(mnRecruitmentSchemaTypeId);
                        break;
                    default:
                }
                break;
                
            case CASE_RECEIPT:
                if (mbShowRecruitmentSchemaIcon) {
                    
                    switch(row) {
                        case 0:
                            value = msName;
                            break;
                        case 1:
                            value = msNumber;
                            break;
                        case 2:
                            value = SHrsUtils.getRecruitmentSchemaIcon(mnRecruitmentSchemaTypeId);
                            break;
                        case 3:
                            value = mdTotalEarnings;
                            break;
                        case 4:
                            value = mdTotalDeductions;
                            break;
                        case 5:
                            value = getTotalNet();
                            break;
                        case 6:
                            value = msBank;
                            break;
                        case 7:
                            value = msBankAccount;
                            break;
                        default:
                    }
                }
                else {
                    switch(row) {
                        case 0:
                            value = msName;
                            break;
                        case 1:
                            value = msNumber;
                            break;
                        case 2:
                            value = mdTotalEarnings;
                            break;
                        case 3:
                            value = mdTotalDeductions;
                            break;
                        case 4:
                            value = getTotalNet();
                            break;
                        case 5:
                            value = msBank;
                            break;
                        case 6:
                            value = msBankAccount;
                            break;
                        default:
                    }
                }
                break;
                
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
