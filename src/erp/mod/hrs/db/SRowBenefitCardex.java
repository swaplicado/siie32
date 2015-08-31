/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.hrs.db;

import erp.mod.SModSysConsts;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Juan Barajas
 */
public class SRowBenefitCardex implements SGridRow {

    protected int mnRowType;
    protected int mnAnn;
    protected int mnAnnYear;
    protected int mnDays;
    protected double mdBenefitBonPer;
    protected double mdDaysPayed;
    protected double mdDaysToPaid;
    protected double mdAmount;
    protected double mdAmountPayed;
    protected double mdAmountToPaid;

    public SRowBenefitCardex(int rowType) {
        mnRowType = rowType;
        mnAnn = 0;
        mnAnnYear = 0;
        mnDays = 0;
        mdBenefitBonPer = 0;
        mdDaysPayed = 0;
        mdDaysToPaid = 0;
        mdAmount = 0;
        mdAmountPayed = 0;
        mdAmountToPaid = 0;
    }

    public void setRowType(int n) { mnRowType = n; }
    public void setAnn(int n) { mnAnn = n; }
    public void setAnnYear(int n) { mnAnnYear = n; }
    public void setDays(int n) { mnDays = n; }
    public void setBenefitBonPer(double d) { mdBenefitBonPer = d; }
    public void setDaysPayed(double d) { mdDaysPayed = d; }
    public void setDaysToPaid(double d) { mdDaysToPaid = d; }
    public void setAmount(double d) { mdAmount = d; }
    public void setAmountPayed(double d) { mdAmountPayed = d; }
    public void setAmountToPaid(double d) { mdAmountToPaid = d; }

    public int getRowType() { return mnRowType; }
    public int getAnn() { return mnAnn; }
    public int getAnnYear() { return mnAnnYear; }
    public int getDays() { return mnDays; }
    public double getBenefitBonPer() { return mdBenefitBonPer; }
    public double getDaysPayed() { return mdDaysPayed; }
    public double getDaysToPaid() { return mdDaysToPaid; }
    public double getAmount() { return mdAmount; }
    public double getAmountPayed() { return mdAmountPayed; }
    public double getAmountToPaid() { return mdAmountToPaid; }

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

        if (mnRowType == SModSysConsts.HRSS_TP_BEN_VAC_BON) { 
            switch(row) {
                case 0:
                    value = mnAnn;
                    break;
                case 1:
                    value = mnAnnYear;
                    break;
                case 2:
                    value = mnDays;
                    break;
                case 3:
                    value = mdBenefitBonPer;
                    break;
                case 4:
                    value = mdAmount;
                    break;
                case 5:
                    value = mdAmountPayed;
                    break;
                case 6:
                    value = mdAmountToPaid;
                    break;
                default:
            }
        }
        else if (mnRowType == SModSysConsts.HRSS_TP_BEN_ANN_BON) { 
            switch(row) {
                case 0:
                    value = mnAnn;
                    break;
                case 1:
                    value = mnAnnYear;
                    break;
                case 2:
                    value = mnDays;
                    break;
                case 3:
                    value = mdAmount;
                    break;
                case 4:
                    value = mdAmountPayed;
                    break;
                case 5:
                    value = mdAmountToPaid;
                    break;
                default:
            }
        }
        else {
            switch(row) {
                case 0:
                    value = mnAnn;
                    break;
                case 1:
                    value = mnAnnYear;
                    break;
                case 2:
                    value = mnDays;
                    break;
                case 3:
                    value = mdDaysPayed;
                    break;
                case 4:
                    value = mdDaysToPaid;
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
