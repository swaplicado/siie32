/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Juan Barajas, Sergio Flores, Sergio Flores
 */
public class SRowCalculateIncomeTax implements SGridRow {

    protected int mnEmployeeId;
    protected String msEmployeeName;
    protected String msEmployeeCode;
    protected String msPaymentType;
    protected String msRecruitmentSchema;
    protected boolean mbActive;
    protected Date mtDateHire;
    protected Date mtDateDismissal_n;
    
    protected SRowCalculateIncomeTaxValues moValues;
    
    protected ArrayList<SRowCalculateIncomeTaxValues> maSubsidyValueses;

    public SRowCalculateIncomeTax() {
        mnEmployeeId = 0;
        msEmployeeName = "";
        msEmployeeCode = "";
        msPaymentType = "";
        msRecruitmentSchema = "";
        mbActive = false;
        mtDateHire = null;
        mtDateDismissal_n = null;
        
        moValues = new SRowCalculateIncomeTaxValues();
        maSubsidyValueses = new ArrayList<>();
    }

    public void setEmployeeId(int n) { mnEmployeeId = n; }
    public void seEmployeetName(String s) { msEmployeeName = s; }
    public void setEmployeeCode(String s) { msEmployeeCode = s; }
    public void setPaymentType(String s) { msPaymentType = s; }
    public void setRecruitmentSchema(String s) { msRecruitmentSchema = s; }
    public void setActive(boolean b) { mbActive = b; }
    public void setDateHire(Date t) { mtDateHire = t; }
    public void setDateDismissal_n(Date t) { mtDateDismissal_n = t; }
    
    public void setValues(SRowCalculateIncomeTaxValues o) { moValues = o; }
    
    public int getEmployeeId() { return mnEmployeeId; }
    public String getEmployeeName() { return msEmployeeName; }
    public String getEmployeeCode() { return msEmployeeCode; }
    public String getPaymentType() { return msPaymentType; }
    public String getRecruitmentSchema() { return msRecruitmentSchema; }
    public boolean isActive() { return mbActive; }
    public Date getDateHire() { return mtDateHire; }
    public Date getDateDismissal_n() { return mtDateDismissal_n; }
    
    public SRowCalculateIncomeTaxValues getValues() { return moValues; }
    
    public ArrayList<SRowCalculateIncomeTaxValues> getSubsidyValueses() { return maSubsidyValueses; }
    
    public void totalizeSubsidyFromSubsidyValueses() {
        double subsidyAssessed = 0;
        double subsidyPayed = 0;
        
        for (SRowCalculateIncomeTaxValues values : maSubsidyValueses) {
            subsidyAssessed = SLibUtils.roundAmount(subsidyAssessed + values.getSubsidyAssessed());
            subsidyPayed = SLibUtils.roundAmount(subsidyPayed + values.getSubsidyPayed());
        }
        
        moValues.setSubsidyAssessed(subsidyAssessed);
        moValues.setSubsidyPayed(subsidyPayed);
    }
    
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnEmployeeId };
    }

    @Override
    public String getRowCode() {
        return getEmployeeCode();
    }

    @Override
    public String getRowName() {
        return getEmployeeName();
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
                value = msEmployeeName;
                break;
            case 1:
                value = msEmployeeCode;
                break;
            case 2:
                value = msPaymentType;
                break;
            case 3:
                value = moValues.getIncomeGross();
                break;
            case 4:
                value = moValues.getIncomeTaxable();
                break;
            case 5:
                value = moValues.getDaysHired();
                break;
            case 6:
                value = moValues.getDaysIncapacity();
                break;
            case 7:
                value = moValues.getDaysTaxable();
                break;
            case 8:
                value = moValues.getTableFactor();
                break;
            case 9:
                value = moValues.getTaxAssessed();
                break;
            case 10:
                value = moValues.getTaxWithheld();
                break;
            case 11:
                value = moValues.getDiferenceTax();
                break;
            case 12:
                value = moValues.getSubsidyAssessed();
                break;
            case 13:
                value = moValues.getSubsidyPayed();
                break;
            case 14:
                value = moValues.getDiferenceSubsidy();
                break;
            case 15:
                value = moValues.getDiferenceNet();
                break;
            case 16:
               value = mbActive;
                break;
            case 17:
               value = mtDateHire;
                break;
            case 18:
               value = mtDateDismissal_n;
                break;
            case 19:
               value = msRecruitmentSchema;
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
