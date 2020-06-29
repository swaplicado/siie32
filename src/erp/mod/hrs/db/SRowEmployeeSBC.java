/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.hrs.db;

import erp.mod.SModSysConsts;
import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Claudio Peña
 */
public class SRowEmployeeSBC implements SGridRow {
    
    protected int mnKeyEmpl;
    protected String msEmployee;
    protected Date mtInitBen;
    protected double mdAnti;
    protected String msPeriod;
    protected int mnDaysVac;
    protected double mnPrimVac;
    protected double mdDaysBen;
    protected double mdFactor;    
    protected double mdIngrDi;
    protected double mdSbcA;
    protected Date mtLastDate;
    protected double mdSbcFactor;
    protected double mdExtraIncome;
    protected double mdSbcSug;
    protected String msCheck;    
    

    public SRowEmployeeSBC(int rowType) {

        mnKeyEmpl = 0;
        msEmployee = "";
        mtInitBen = null;
        mdAnti = 0.0;
        msPeriod = "";
        mnDaysVac = 0;
        mnPrimVac = 0.0;
        mdDaysBen = 0.0;
        mdFactor = 0.0;
        mdIngrDi = 0.0;
        mdSbcA = 0.0;
        mtLastDate = null;
        mdSbcFactor = 0.0;
        mdExtraIncome = 0.0;
        mdSbcSug = 0.0;
        msCheck = "";
    }

    public void setKeyEmpl(int n) { mnKeyEmpl = n; }
    public void setEmployee(String s) { msEmployee = s; }
    public void setInitBen(Date t) { mtInitBen = t; }
    public void setAnti(double d) { mdAnti = d; }
    public void setPeriod(String s) { msPeriod = s; }
    public void setDaysVac(int n) { mnDaysVac = n; }
    public void setPrimVac(double d) { mnPrimVac = d; }
    public void setDaysBen(double d) { mdDaysBen = d; }
    public void setFactor(double d) { mdFactor = d; }
    public void setIngrDi(double d) { mdIngrDi = d; }
    public void setSbcA(double d) { mdSbcA = d; }
    public void setLastDate(Date t) { mtLastDate = t; }
    public void setSbcFactor(double d) { mdSbcFactor = d; }
    public void setExtraIncome(double d) { mdExtraIncome = d; }
    public void setSbcSug(double d) { mdSbcSug = d; }
    public void setCheck(String s) { msCheck = s; }

    public int getKeyEmpl() { return mnKeyEmpl; }
    public String getEmployee() { return msEmployee; }
    public Date getInitBen() { return mtInitBen; }
    public double getAnti() { return mdAnti; }
    public String getPeriod() { return msPeriod; }
    public int getDaysVac() { return mnDaysVac; }
    public double getPrimVac() { return mnPrimVac; }
    public double getDaysBen() { return mdDaysBen; }
    public double getFactor() { return mdFactor; }
    public double getIngrDi() { return mdIngrDi; }
    public double getSbcA() { return mdSbcA; }
    public Date getLastDate() { return mtLastDate; }
    public double getSbcFactor() { return mdSbcFactor; }
    public double getExtraIncome() { return mdExtraIncome; }
    public double getSbcSug() { return mdSbcSug; }
    public String getCheck() { return msCheck; }

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

        if (mnKeyEmpl == SModSysConsts.HRSS_TP_BEN_VAC_BON) {
            switch(row) {
                case 0:
                    value = msEmployee;
                    break;
                case 1:
                    value = mtInitBen;
                    break;
                case 2:
                    value = mdAnti;
                    break;
                case 3:
                    value = msPeriod;
                    break;
                case 4:
                    value = mnDaysVac;
                    break;
                case 5:
                    value = mnPrimVac;
                    break;
                case 6:
                    value = mdDaysBen;
                    break;
                case 7:
                    value = mdFactor;
                    break;
                case 8:
                    value = mdIngrDi;
                    break;
                case 9:
                    value = mdSbcA;
                    break;
                case 10:
                    value = mtLastDate;
                    break;
                case 11:
                    value = mdSbcFactor;
                    break;
                case 12:
                    value = mdExtraIncome;
                    break;
                case 13:
                    value = mdSbcSug;
                    break;
                case 14:
                    value = msCheck;
                    break;
                default:
            }
        }
        else { 
            switch(row) {
                case 0:
                    value = mnKeyEmpl;
                    break;
                case 1:
                    value = msEmployee;
                    break;
                case 2:
                    value = mtInitBen;
                    break;
                case 3:
                    value = mdAnti;
                    break;
                case 4:
                    value = msPeriod;
                    break;
                case 5:
                    value = mnDaysVac;
                    break;
                case 6:
                    value = mnPrimVac;
                    break;
                case 7:
                    value = mdDaysBen;
                    break;
                case 8:
                    value = mdFactor;
                    break;
                case 9:
                    value = mdIngrDi;
                    break;
                case 10:
                    value = mdSbcA;
                    break;
                case 11:
                    value = mtLastDate;
                    break;
                case 12:
                    value = mdSbcFactor;
                    break;
                case 13:
                    value = mdExtraIncome;
                    break;
                case 14:
                    value = mdSbcSug;
                    break;
                case 15:
                    value = msCheck;
                    break;
                default:
            }
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object o, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
