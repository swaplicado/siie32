/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.hrs.db;

import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Juan Barajas
 */
public class SRowAbsenceMovesCardex implements SGridRow {

    protected int mnYear;
    protected int mnPeriod;
    protected String msPayrollType;
    protected String msNumber;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    protected int mdDays;
    protected double mdPayment;
    protected String msUserInsert;
    protected Date mtDateUserInsert;
    protected String msUserUpdate;
    protected Date mtDateUserUpdate;

    public SRowAbsenceMovesCardex() {
        mnYear = 0;
        mnPeriod = 0;
        msPayrollType = "";
        msNumber = "";
        mtDateStart = null;
        mtDateEnd = null;
        mdDays = 0;
        mdPayment = 0;
        msUserInsert = "";
        mtDateUserInsert = null;
        msUserUpdate = "";
        mtDateUserUpdate = null;
    }

    public void setYear(int n) { mnYear = n; }
    public void setPeriod(int n) { mnPeriod = n; }
    public void setPayrollType(String s) { msPayrollType = s; }
    public void setNumber(String s) { msNumber = s; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setDays(int d) { mdDays = d; }
    public void setPayment(double d) { mdPayment = d; }
    public void setUserInsert(String s) { msUserInsert = s; }
    public void setDateUserInsert(Date t) { mtDateUserInsert = t; }
    public void setUserUpdate(String s) { msUserUpdate = s; }
    public void setDateUserUpdate(Date t) { mtDateUserUpdate = t; }

    public int getYear() { return mnYear; }
    public int getPeriod() { return mnPeriod; }
    public String getPayrollType() { return msPayrollType; }
    public String getNumber() { return msNumber; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public int getDays() { return mdDays; }
    public double getPayment() { return mdPayment; }
    public String getUserInsert() { return msUserInsert; }
    public Date getDateUserInsert() { return mtDateUserInsert; }
    public String getUserUpdate() { return msUserUpdate; }
    public Date getDateUserUpdate() { return mtDateUserUpdate; }

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
                value = mnYear;
                break;
            case 1:
                value = mnPeriod;
                break;
            case 2:
                value = msPayrollType;
                break;
            case 3:
                value = msNumber;
                break;
            case 4:
                value = mtDateStart;
                break;
            case 5:
                value = mtDateEnd;
                break;
            case 6:
                value = mdDays;
                break;
            case 7:
                value = msUserInsert;
                break;
            case 8:
                value = mtDateUserInsert;
                break;
            case 9:
                value = msUserUpdate;
                break;
            case 10:
                value = mtDateUserUpdate;
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
