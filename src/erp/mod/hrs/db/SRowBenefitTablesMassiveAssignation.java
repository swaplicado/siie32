/*
 * © Software Aplicado SA de CV. Todos los derechos reservados. 
 */
package erp.mod.hrs.db;

import erp.mod.SModSysConsts;
import erp.mod.hrs.utils.SAnniversary;
import java.util.Date;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SRowBenefitTablesMassiveAssignation implements SGridRow {
    
    public static final int UPDATE_MODE_BENEFITS = 1; // since date of benefits
    public static final int UPDATE_MODE_ASSIGNATION = 2; // since date of assignation
    
    protected int mnBenefitType;
    protected int mnUpdateMode;
    protected SDbEmployee moEmployee;
    protected SDbEmployeeBenefitTables moCurrentBenefitTables;
    protected SAnniversary moAnniversary;
    
    /**
     * Creates new row for massive update of benefit tables.
     * @param session GUI user session.
     * @param benefitType Benefit type. Options defined in SModSysConsts.HRSS_TP_BEN_...
     * @param updateMode Update mode. Options defined in this class: UPDATE_MODE_BENEFITS or UPDATE_MODE_ASSIGNATION.
     * @param employee Employee.
     * @param today Cutoff date.
     * @throws java.lang.Exception
     */
    public SRowBenefitTablesMassiveAssignation(final SGuiSession session, final int benefitType, final int updateMode, final SDbEmployee employee, final Date today) throws Exception {
        mnBenefitType = benefitType;
        mnUpdateMode = updateMode;
        moEmployee = employee;
        moCurrentBenefitTables = SHrsBenefitTablesUtils.getCurrentBenefitTables(session, employee.getPkEmployeeId());
        moAnniversary = moEmployee.createAnniversary(today);
    }
    
    public SDbEmployee getEmployee() {
        return moEmployee;
    }
    
    public SDbEmployeeBenefitTables getCurrentBenefitTables() {
        return moCurrentBenefitTables;
    }
    
    public SAnniversary getAnniversary() {
        return moAnniversary;
    }
    
    public int getAssignationAnniversary() {
        return mnUpdateMode == UPDATE_MODE_BENEFITS ? 1 : moAnniversary.getEligibleAnniversary();
    }

    @Override
    public int[] getRowPrimaryKey() {
        return moEmployee.getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return moEmployee.getNumber();
    }

    @Override
    public String getRowName() {
        return moEmployee.getXtaEmployeeName();
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
                value = moEmployee.getXtaEmployeeName();
                break;
                
            case 1:
                value = moEmployee.getNumber();
                break;
                
            case 2:
                value = moEmployee.getDateBenefits();
                break;
                
            case 3:
                value = moAnniversary.getAnniversaryCurr().toDate();
                break;
                
            case 4:
                value = moAnniversary.isAnniversaryCurrTurned();
                break;
                
            case 5:
                value = moAnniversary.getElapsedYears();
                break;
                
            case 6:
                value = moAnniversary.getElapsedDaysForBenefits();
                break;
                
            case 7:
                value = moAnniversary.getOngoingAnniversary();
                break;
                
            case 8:
                value = moAnniversary.getOngoingAnniversaryPropForBenefits();
                break;
                
            case 9:
                value = getAssignationAnniversary();
                break;
                
            case 10:
                if (moCurrentBenefitTables != null) {
                    switch (mnBenefitType) {
                        case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                            value = moCurrentBenefitTables.getXtaBenefitNameAnnualBonus();
                            break;
                        case SModSysConsts.HRSS_TP_BEN_VAC:
                            value = moCurrentBenefitTables.getXtaBenefitNameVacation();
                            break;
                        case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                            value = moCurrentBenefitTables.getXtaBenefitNameVacationBonus();
                            break;
                        default:
                            // nothing
                    }
                }
                break;
                
            case 11:
                if (moCurrentBenefitTables != null) {
                    switch (mnBenefitType) {
                        case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                            value = moCurrentBenefitTables.getStartingAnniversaryAnnualBonus();
                            break;
                        case SModSysConsts.HRSS_TP_BEN_VAC:
                            value = moCurrentBenefitTables.getStartingAnniversaryVacation();
                            break;
                        case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                            value = moCurrentBenefitTables.getStartingAnniversaryVacationBonus();
                            break;
                        default:
                            // nothing
                    }
                }
                break;
                
            case 12:
                if (moCurrentBenefitTables != null) {
                    switch (mnBenefitType) {
                        case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                            value = moCurrentBenefitTables.getTsUserUpdateAnnualBonus();
                            break;
                        case SModSysConsts.HRSS_TP_BEN_VAC:
                            value = moCurrentBenefitTables.getTsUserUpdateVacation();
                            break;
                        case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                            value = moCurrentBenefitTables.getTsUserUpdateVacationBonus();
                            break;
                        default:
                            // nothing
                    }
                }
                break;
                
            case 13:
                if (moCurrentBenefitTables != null) {
                    switch (mnBenefitType) {
                        case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                            value = moCurrentBenefitTables.getXtaUserUpdateNameAnnualBonus();
                            break;
                        case SModSysConsts.HRSS_TP_BEN_VAC:
                            value = moCurrentBenefitTables.getXtaUserUpdateNameVacation();
                            break;
                        case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                            value = moCurrentBenefitTables.getXtaUserUpdateNameVacationBonus();
                            break;
                        default:
                            // nothing
                    }
                }
                break;
                
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
