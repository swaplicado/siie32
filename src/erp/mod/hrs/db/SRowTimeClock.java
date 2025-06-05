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
    protected String msEmployeeNumber;
    protected String msEmployeeName;
    protected int mnAbsences;
    protected double mdOvertime;
    protected int mnSundays;
    protected int mnHolidays;
    protected int mnDaysOff;

    public SRowTimeClock() {
        mnEmployeeId = 0;
        msEmployeeNumber = "";
        msEmployeeName = "";
        mnAbsences = 0;
        mdOvertime = 0;
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

    public String getEmployeeNumber() {
        return msEmployeeNumber;
    }

    public void setEmployeeNumber(String mnEmployeeNumber) {
        this.msEmployeeNumber = mnEmployeeNumber;
    }

    public String getEmployeeName() {
        return msEmployeeName;
    }

    public void setEmployeeName(String msEmployeeName) {
        this.msEmployeeName = msEmployeeName;
    }

    public int getAbsences() {
        return mnAbsences;
    }

    public void setAbsences(int mnAbsences) {
        this.mnAbsences = mnAbsences;
    }

    public double getOvertime() {
        return mdOvertime;
    }

    public void setOvertime(double extraTime) {
        this.mdOvertime = extraTime;
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
                value = msEmployeeNumber;
                break;
            case 1:
                value = msEmployeeName;
                break;
            case 2:
                value = mnAbsences;
                break;
            case 3:
                value = mdOvertime;
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
