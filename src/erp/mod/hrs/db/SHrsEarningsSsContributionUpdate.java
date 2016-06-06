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
public class SHrsEarningsSsContributionUpdate implements SGridRow {

    protected int mnEmployeeId;
    protected int mnEarningId;
    protected String msCodeEarning;
    protected String msNameEarning;
    protected double mdAmountSys;
    protected double mdAmount;

    public SHrsEarningsSsContributionUpdate() throws Exception {
        mnEmployeeId = 0;
        mnEarningId = 0;
        msCodeEarning = "";
        msNameEarning = "";
        mdAmountSys = 0;
        mdAmount = 0;
    }
    
    public void setEmployeeId(int n) { mnEmployeeId = n; }
    public void setEarningId(int n) { mnEarningId = n; }
    public void setCodeEarning(String s) { msCodeEarning = s; }
    public void setNameEarning(String s) { msNameEarning = s; }
    public void setAmountSys(double d) { mdAmountSys = d; }
    public void setAmount(double d) { mdAmount = d; }
    
    public int getEmployeeId() { return mnEmployeeId; }
    public int getEarningId() { return mnEarningId; }
    public String getCodeEarning() { return msCodeEarning; }
    public String getNameEarning() { return msNameEarning; }
    public double getAmountSys() { return mdAmountSys; }
    public double getAmount() { return mdAmount; }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnEmployeeId, mnEarningId };
    }

    @Override
    public String getRowCode() {
        return getCodeEarning();
    }

    @Override
    public String getRowName() {
        return getNameEarning();
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
                value = msCodeEarning;
                break;
            case 1:
                value = msNameEarning;
                break;
            case 2:
                value = mdAmount;
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
                mdAmount = (double) value;
                break;
            default:
                break;
        }
    }
}
