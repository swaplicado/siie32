/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Juan Barajas
 */
public class SHrsCalculateEstimateISR implements SGridRow {

    protected int mnEmployeeId;
    protected String msNameEmployee;
    protected String msCodeEmployee;
    protected boolean mbStatus;
    protected double mdAmountIncome;
    protected double mdAmountTaxable;
    protected double mdDaysHire;
    protected double mdDaysIncapacity;
    protected double mdDaysTaxable;
    protected double mdFactor;
    protected double mdCalculatedTax;
    protected double mdRetainedTax;
    protected double mdCalculatedSubsidy;
    protected double mdGivenSubsidy;

    public SHrsCalculateEstimateISR() throws Exception {
        mnEmployeeId = 0;
        msCodeEmployee = "";
        msNameEmployee = "";
        mdDaysHire = 0;
        mdDaysIncapacity = 0;
        mdDaysTaxable = 0;
        mdAmountTaxable = 0;
        mdAmountIncome = 0;
        mdFactor = 0;
        mbStatus = false;
        mdCalculatedTax = 0;
        mdRetainedTax = 0;
        mdCalculatedSubsidy = 0;
        mdGivenSubsidy = 0;
    }

    public void setEmployeeId(int n) { mnEmployeeId = n; }
    public void setCodeEmployee(String s) { msCodeEmployee = s; }
    public void setNameEmployee(String s) { msNameEmployee = s; }
    public void setDaysHire(double n) { mdDaysHire = n; }
    public void setDaysIncapacity(double n) { mdDaysIncapacity = n; }
    public void setDaysTaxable(double n) { mdDaysTaxable = n; }
    public void setAmountTaxable(double d) { mdAmountTaxable = d; }
    public void setAmountIncome(double d) { mdAmountIncome = d; }
    public void setFactor(double d) { mdFactor = d; }
    public void setIsStatus(boolean b) { mbStatus = b; }
    public void setCalculatedTax(double d) { mdCalculatedTax = d; }
    public void setRetainedTax(double d) { mdRetainedTax = d; }
    public void setCalculatedSubsidy(double d) { mdCalculatedSubsidy = d; }
    public void setGivenSubsidy(double d) { mdGivenSubsidy = d; }
    
    public int getEmployeeId() { return mnEmployeeId; }
    public String getCodeEmployee() { return msCodeEmployee; }
    public String getNameEmployee() { return msNameEmployee; }
    public double getDaysHire() { return mdDaysHire; }
    public double getDaysIncapacity() { return mdDaysIncapacity; }
    public double getDaysTaxable() { return mdDaysTaxable; }
    public double getAmountTaxable() { return mdAmountTaxable; }
    public double getAmountIncome() { return mdAmountIncome; }
    public double getFactor() { return mdFactor; }
    public boolean isStatus() { return mbStatus; }
    public double getCalculatedTax() { return mdCalculatedTax; }
    public double getRetainedTax() { return mdRetainedTax; }
    public double getCalculatedSubsidy() { return mdCalculatedSubsidy; }
    public double getGivenSubsidy() { return mdGivenSubsidy; }
    
    public double getDiferenceTax() { return mdCalculatedTax - mdRetainedTax; }
    
    public double getDiferenceSubsidy() { return mdCalculatedSubsidy - mdGivenSubsidy; }
    
    public double getDiferenceNet() { return (mdCalculatedTax - mdRetainedTax) - (mdCalculatedSubsidy - mdGivenSubsidy); }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnEmployeeId };
    }

    @Override
    public String getRowCode() {
        return getCodeEmployee();
    }

    @Override
    public String getRowName() {
        return getNameEmployee();
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = msNameEmployee;
                break;
            case 1:
                value = msCodeEmployee;
                break;
            case 2:
               value = mbStatus;
                break;
            case 3:
                value = mdAmountIncome;
                break;
            case 4:
                value = mdAmountTaxable;
                break;
            case 5:
                value = mdDaysHire;
                break;
            case 6:
                value = mdDaysIncapacity;
                break;
            case 7:
                value = mdDaysTaxable;
                break;
            case 8:
                value = mdFactor;
                break;
            case 9:
                value = mdCalculatedTax;
                break;
            case 10:
                value = mdRetainedTax;
                break;
            case 11:
                value = getDiferenceTax();
                break;
            case 12:
                value = mdCalculatedSubsidy;
                break;
            case 13:
                value = mdGivenSubsidy;
                break;
            case 14:
                value = getDiferenceSubsidy();
                break;
            case 15:
                value = getDiferenceNet();
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch (row) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
            case 14:
                break;
            case 15:
                break;
            default:
                break;
        }
    }
}
