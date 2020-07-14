/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.hrs.db;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Edwin Carmona
 */
public class SRowBonus implements SGridRow {

    protected int mnEmployeeId;
    protected String msNumEmployee;
    protected String msEmployee;
    protected String msEarning;
    protected String msBonus;
    protected double mdAmount;
    protected String msComments;
    protected boolean mbHasBonus;

    public SRowBonus() {
        mnEmployeeId = 0;
        msNumEmployee = "";
        msEmployee = "";
        msEarning = "";
        msBonus = "";
        mdAmount = 0d;
        msComments = "";
    }

    public int getEmployeeId() {
        return mnEmployeeId;
    }

    public void setEmployeeId(int mnEmployeeId) {
        this.mnEmployeeId = mnEmployeeId;
    }

    public String getNumEmployee() {
        return msNumEmployee;
    }

    public void setNumEmployee(String mnNumEmployee) {
        this.msNumEmployee = mnNumEmployee;
    }

    public String getEmployee() {
        return msEmployee;
    }

    public void setEmployee(String msEmployee) {
        this.msEmployee = msEmployee;
    }

    public double getAmount() {
        return mdAmount;
    }

    public void setAmount(double extraTime) {
        this.mdAmount = extraTime;
    }

    public String getEarning() {
        return msEarning;
    }

    public void setEarning(String msEarning) {
        this.msEarning = msEarning;
    }

    public String getBonus() {
        return msBonus;
    }

    public void setBonus(String msBonus) {
        this.msBonus = msBonus;
    }

    public String getComments() {
        return msComments == null ? "" : msComments;
    }

    public void setComments(String msComments) {
        this.msComments = msComments;
    }

    public boolean isHasBonus() {
        return mbHasBonus;
    }

    public void setHasBonus(boolean mbHasBonus) {
        this.mbHasBonus = mbHasBonus;
    }

    @Override
    public int[] getRowPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet.");
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
        
        switch(row) {
            case 0:
                value = msNumEmployee;
                break;
            case 1:
                value = msEmployee;
                break;
            case 2:
                value = msEarning;
                break;
            case 3:
                value = msBonus;
                break;
            case 4:
                value = mdAmount;
                break;
            case 5:
                value = mbHasBonus;
                break;
            case 6:
                value = msComments;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object o, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
