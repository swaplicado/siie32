/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.hrs.db;

import erp.mod.SModSysConsts;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SRowBenefitCardex implements SGridRow {

    private int mnBenefitType;
    private int mnBenefitYear;
    private int mnBenefitAnniversary;
    private double mdProportional;
    private double mdBenefitBonusPct;
    private int mnBenefitDays;
    private double mdBenefitDaysScheduled;
    private double mdBenefitDaysPayed;
    private double mdBenefitAmount;
    private double mdBenefitAmountPayed;

    /**
     * Creates new row of benefit cardex.
     * @param benefitType Type of benefit. SModSysConsts.HRSS_TP_BEN_...
     */
    public SRowBenefitCardex(int benefitType) {
        mnBenefitType = benefitType;
        mnBenefitYear = 0;
        mnBenefitAnniversary = 0;
        mdProportional = 0;
        mdBenefitBonusPct = 0;
        mnBenefitDays = 0;
        mdBenefitDaysScheduled = 0;
        mdBenefitDaysPayed = 0;
        mdBenefitAmount = 0;
        mdBenefitAmountPayed = 0;
    }

    public void setBenefitYear(int n) { mnBenefitYear = n; }
    public void setBenefitAnniversary(int n) { mnBenefitAnniversary = n; }
    public void setProportional(double d) { mdProportional = d; }
    public void setBenefitBonusPct(double d) { mdBenefitBonusPct = d; }
    public void setBenefitDays(int n) { mnBenefitDays = n; }
    public void setBenefitDaysScheduled(double d) { mdBenefitDaysScheduled = d; }
    public void setBenefitDaysPayed(double d) { mdBenefitDaysPayed = d; }
    public void setBenefitAmount(double d) { mdBenefitAmount = d; }
    public void setBenefitAmountPayed(double d) { mdBenefitAmountPayed = d; }

    public int getBenefitType() { return mnBenefitType; }
    public int getBenefitYear() { return mnBenefitYear; }
    public int getBenefitAnniversary() { return mnBenefitAnniversary; }
    public double getProportional() { return mdProportional; }
    public double getBenefitBonusPct() { return mdBenefitBonusPct; }
    public int getBenefitDays() { return mnBenefitDays; }
    public double getBenefitDaysProportional() { return mnBenefitDays * mdProportional; }
    public double getBenefitDaysScheduled() { return mdBenefitDaysScheduled; }
    public double getBenefitDaysPayed() { return mdBenefitDaysPayed; }
    public double getBenefitDaysToPay() { return getBenefitDaysProportional() - mdBenefitDaysPayed; }
    public double getBenefitAmount() { return mdBenefitAmount; }
    public double getBenefitAmountProportional() { return SLibUtils.roundAmount(mdBenefitAmount * mdProportional); }
    public double getBenefitAmountPayed() { return mdBenefitAmountPayed; }
    public double getBenefitAmountToPay() { return SLibUtils.roundAmount(getBenefitAmountProportional() - mdBenefitAmountPayed); }

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
                value = mnBenefitAnniversary;
                break;
            case 1:
                value = mnBenefitYear;
                break;
            case 2:
                value = mnBenefitDays;
                break;
            case 3:
                value = getBenefitDaysProportional();
                break;
            default:
        }
        
        if (mnBenefitType == SModSysConsts.HRSS_TP_BEN_VAC) {
            switch(row) {
                case 4:
                    value = mdBenefitDaysScheduled;
                    break;
                case 5:
                    value = mdBenefitDaysPayed;
                    break;
                case 6:
                    value = getBenefitDaysToPay();
                    break;
                default:
            }
        }
        else {
            if (mnBenefitType == SModSysConsts.HRSS_TP_BEN_ANN_BON) {
                switch(row) {
                    case 4:
                        value = mdBenefitAmount;
                        break;
                    case 5:
                        value = getBenefitAmountProportional();
                        break;
                    case 6:
                        value = mdBenefitAmountPayed;
                        break;
                    case 7:
                        value = getBenefitAmountToPay();
                        break;
                    default:
                }
            }
            else if (mnBenefitType == SModSysConsts.HRSS_TP_BEN_VAC_BON) {
                switch(row) {
                    case 4:
                        value = mdBenefitBonusPct;
                        break;
                    case 5:
                        value = mdBenefitAmount;
                        break;
                    case 6:
                        value = getBenefitAmountProportional();
                        break;
                    case 7:
                        value = mdBenefitAmountPayed;
                        break;
                    case 8:
                        value = getBenefitAmountToPay();
                        break;
                    default:
                }
            }
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object o, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
