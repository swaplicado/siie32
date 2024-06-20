/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Sergio Flores
 */
public class SRowCalculateIncomeTaxValues implements SGridRow {
    
    public static final int STYLE_NA = 0;
    public static final int STYLE_OLD = 1;
    public static final int STYLE_NEW = 2;

    protected int mnStyle;
    protected double mdDaysHired;
    protected double mdDaysIncapacity;
    protected double mdDaysTaxable;
    protected double mdTableFactor;
    
    protected double mdIncomeGross;
    protected double mdIncomeTaxable;
    protected double mdTaxAssessed;
    protected double mdTaxWithheld;
    protected double mdSubsidyAssessed;
    protected double mdSubsidyPayed;

    public SRowCalculateIncomeTaxValues(final int style) {
        mnStyle = style;
        mdDaysHired = 0;
        mdDaysIncapacity = 0;
        mdDaysTaxable = 0;
        mdTableFactor = 0;
        
        mdIncomeGross = 0;
        mdIncomeTaxable = 0;
        mdTaxAssessed = 0;
        mdTaxWithheld = 0;
        mdSubsidyAssessed = 0;
        mdSubsidyPayed = 0;
    }

    public SRowCalculateIncomeTaxValues() {
        this(STYLE_NA);
    }

    //public void setStyle(int n) { mnStyle = n; } // read only member
    public void setDaysHired(double n) { mdDaysHired = n; }
    public void setDaysIncapacity(double n) { mdDaysIncapacity = n; }
    public void setDaysTaxable(double n) { mdDaysTaxable = n; }
    public void setTableFactor(double d) { mdTableFactor = d; }
    
    public void setIncomeGross(double d) { mdIncomeGross = d; }
    public void setIncomeTaxable(double d) { mdIncomeTaxable = d; }
    public void setTaxAssessed(double d) { mdTaxAssessed = d; }
    public void setTaxWithheld(double d) { mdTaxWithheld = d; }
    public void setSubsidyAssessed(double d) { mdSubsidyAssessed = d; }
    public void setSubsidyPayed(double d) { mdSubsidyPayed = d; }
    
    public int getStyle() { return mnStyle; }
    public double getDaysHired() { return mdDaysHired; }
    public double getDaysIncapacity() { return mdDaysIncapacity; }
    public double getDaysTaxable() { return mdDaysTaxable; }
    public double getTableFactor() { return mdTableFactor; }
    
    public double getIncomeGross() { return mdIncomeGross; }
    public double getIncomeTaxable() { return mdIncomeTaxable; }
    public double getTaxAssessed() { return mdTaxAssessed; }
    public double getTaxWithheld() { return mdTaxWithheld; }
    public double getSubsidyAssessed() { return mdSubsidyAssessed; }
    public double getSubsidyPayed() { return mdSubsidyPayed; }
    
    protected double getDiferenceTax() { return SLibUtils.roundAmount(mdTaxAssessed - mdTaxWithheld); }
    protected double getDiferenceSubsidy() { return SLibUtils.roundAmount(mdSubsidyAssessed - mdSubsidyPayed); }
    protected double getDiferenceNet() { return SLibUtils.roundAmount(getDiferenceTax() - getDiferenceSubsidy()); }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnStyle };
    }

    @Override
    public String getRowCode() {
        return mnStyle == STYLE_OLD ? "OBS" : (mnStyle == STYLE_NEW ? "NVO" : "");
    }

    @Override
    public String getRowName() {
        return mnStyle == STYLE_OLD ? "Obsoleto" : (mnStyle == STYLE_NEW ? "Nuevo" : "");
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
                value = getRowName();
                break;
            case 1:
                value = mdIncomeGross;
                break;
            case 2:
                value = mdIncomeTaxable;
                break;
            case 3:
                value = mdDaysHired;
                break;
            case 4:
                value = mdDaysIncapacity;
                break;
            case 5:
                value = mdDaysTaxable;
                break;
            case 6:
                value = mdTableFactor;
                break;
            case 7:
                value = mdTaxAssessed;
                break;
            case 8:
                value = mdTaxWithheld;
                break;
            case 9:
                value = getDiferenceTax();
                break;
            case 10:
                value = mdSubsidyAssessed;
                break;
            case 11:
                value = mdSubsidyPayed;
                break;
            case 12:
                value = getDiferenceSubsidy();
                break;
            case 13:
                value = getDiferenceNet();
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
