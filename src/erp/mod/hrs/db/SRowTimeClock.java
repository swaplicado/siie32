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
public class SRowTimeClock implements SGridRow {

    protected int mnEmployeeId;
    protected String msNumEmployee;
    protected String msEmployee;
    protected int mnAbsences;
    protected double mdExtraTime;
    protected int mnSundays;
    protected int mnHolidays;
    protected int mnDaysOff;

    public SRowTimeClock() {
        mnEmployeeId = 0;
        msNumEmployee = "";
        msEmployee = "";
        mnAbsences = 0;
        mdExtraTime = 0;
        mnSundays = 0;
        mnHolidays = 0;
        mnDaysOff = 0;
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

    public int getAbsences() {
        return mnAbsences;
    }

    public void setAbsences(int mnAbsences) {
        this.mnAbsences = mnAbsences;
    }

    public double getExtraTime() {
        return mdExtraTime;
    }

    public void setExtraTime(double extraTime) {
        this.mdExtraTime = extraTime;
    }

    public int getSundays() {
        return mnSundays;
    }

    public void setSundays(int mnSundays) {
        this.mnSundays = mnSundays;
    }

    public int getHolidays() {
        return mnHolidays;
    }

    public void setHolidays(int mnHolidays) {
        this.mnHolidays = mnHolidays;
    }

    public int getDaysOff() {
        return mnDaysOff;
    }

    public void setDaysOff(int mnDaysOff) {
        this.mnDaysOff = mnDaysOff;
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
                value = mnAbsences;
                break;
            case 3:
                value = mdExtraTime;
                break;
            case 4:
                value = mnSundays;
                break;
            case 5:
                value = mnHolidays;
                break;
            case 6:
                value = mnDaysOff;
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
