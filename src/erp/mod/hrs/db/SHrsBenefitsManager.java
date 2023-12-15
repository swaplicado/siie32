/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.HashMap;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SHrsBenefitsManager {

    private SGuiSession moSession;
    private SDbEmployee moEmployee;
    private SDbEmployeeBenefitTables moEmployeeBenefitTables;
    private int mnPayrollToExcludeId;
    /** key = benefit type; value = array of employee's benefit annums */
    private HashMap<Integer, ArrayList<SDbEmployeeBenefitTablesAnnum>> moBenefitAnnumsMap;
    /** key = benefit type; value = map of of benefits paid (key = year + "-" + annum) */
    private HashMap<Integer, HashMap<String, SHrsBenefit>> moBenefitsPaidMap;
    
    /**
     * Create new benefit manager.
     * @param session
     * @param employeeId
     * @param payrollToExcludeId
     * @throws Exception 
     */
    public SHrsBenefitsManager(final SGuiSession session, final int employeeId, final int payrollToExcludeId) throws Exception {
        this(session, (SDbEmployee) session.readRegistry(SModConsts.HRSU_EMP, new int[] { employeeId }), payrollToExcludeId);
    }
    
    /**
     * Create new benefit manager.
     * @param session
     * @param employee
     * @param payrollToExcludeId
     * @throws Exception 
     */
    public SHrsBenefitsManager(final SGuiSession session, final SDbEmployee employee, final int payrollToExcludeId) throws Exception {
        moSession = session;
        moEmployee = employee;
        moEmployeeBenefitTables = SHrsBenefitUtils.getCurrentBenefitTables(moSession, moEmployee.getPkEmployeeId());
        mnPayrollToExcludeId = payrollToExcludeId;
        moBenefitAnnumsMap = new HashMap<>();
        moBenefitsPaidMap = new HashMap<>();
    }
    
    /**
     * Get array of benefit annums.
     * @param benefitType Benefit type. Constants defined in SModSysConsts.HRSS_TP_BEN_...
     * @return
     * @throws Exception 
     */
    public ArrayList<SDbEmployeeBenefitTablesAnnum> getBenefitAnnums(final int benefitType) throws Exception {
        ArrayList<SDbEmployeeBenefitTablesAnnum> benefitAnnums = moBenefitAnnumsMap.get(benefitType);
        
        if (benefitAnnums == null) {
            benefitAnnums = moEmployeeBenefitTables.getBenefitAnnums(moSession, benefitType);
            moBenefitAnnumsMap.put(benefitType, benefitAnnums);
        }
        
        return benefitAnnums;
    }
    
    /**
     * Get benefit annum.
     * @param benefitType Benefit type. Constants defined in SModSysConsts.HRSS_TP_BEN_...
     * @param annum Benefit annum.
     * @return
     * @throws Exception 
     */
    public SDbEmployeeBenefitTablesAnnum getBenefitAnnum(final int benefitType, final int annum) throws Exception {
        SDbEmployeeBenefitTablesAnnum benefitAnnum = null;
        ArrayList<SDbEmployeeBenefitTablesAnnum> benefitAnnums = getBenefitAnnums(benefitType);
        
        if (benefitAnnums != null) {
            benefitAnnum = benefitAnnums.get(annum - 1);
        }
        
        return benefitAnnum;
    }

    /**
     * Get map of benefits paid.
     * @param benefitType Benefit type. Constants defined in SModSysConsts.HRSS_TP_BEN_...
     * @return
     * @throws Exception 
     */
    public HashMap<String, SHrsBenefit> getBenefitsPaidMap(final int benefitType) throws Exception {
        HashMap<String, SHrsBenefit> benefitsPaidMap = moBenefitsPaidMap.get(benefitType);
        
        if (benefitsPaidMap == null) {
            benefitsPaidMap = new HashMap<>();
            ArrayList<SHrsBenefit> benefitsPayed = SHrsUtils.getHrsBenefitsPayed(moSession, moEmployee, benefitType, mnPayrollToExcludeId);
            
            for (SHrsBenefit benefit : benefitsPayed) {
                String key = benefit.getBenefitYear() + "-" + benefit.getBenefitAnniversary();
                benefitsPaidMap.put(key, benefit);
            }
            
            moBenefitsPaidMap.put(benefitType, benefitsPaidMap);
        }
        
        return benefitsPaidMap;
    }
    
    /**
     * Get benefit paid for given benefit type, yean and annum.
     * @param benefitType Benefit type. Constants defined in SModSysConsts.HRSS_TP_BEN_...
     * @param year Benefit year.
     * @param annum Benefit annum.
     * @return Benefit paid if found, otherwise <code>null</code>.
     * @throws Exception 
     */
    public SHrsBenefit getBenefitPaid(final int benefitType, final int year, final int annum) throws Exception {
        SHrsBenefit benefitPaid = null;
        ArrayList<SDbEmployeeBenefitTablesAnnum> benefitAnnums = getBenefitAnnums(benefitType);
        
        if (benefitAnnums != null) {
            HashMap<String, SHrsBenefit> benefitsPaidMap = getBenefitsPaidMap(benefitType);

            benefitPaid = benefitsPaidMap.get(year + "-" + annum);

            if (benefitPaid != null) {
                // lookup for required annum:

                SDbEmployeeBenefitTablesAnnum benefitAnnum = SHrsBenefitUtils.getBenefitAnnum(moSession, benefitAnnums, annum);

                // prepare benefit payed:

                switch (benefitType) {
                    case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                    case SModSysConsts.HRSS_TP_BEN_VAC:
                        benefitPaid.setBenefitDays(benefitAnnum.getBenefitDays());
                        break;
                    case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                        benefitPaid.setBenefitBonusPct(benefitAnnum.getBenefitBonusPct());
                        break;
                    default:
                        // nothing
                }
            }
        }
        
        return benefitPaid;
    }
    
    /**
     * Get older annum to be consumed.
     * @param benefitType Benefit type. Constants defined in SModSysConsts.HRSS_TP_BEN_...
     * @return Older annum to be consumed.
     * @throws Exception 
     */
    public int getOlderAnnumToBeConsumed(final int benefitType) throws Exception {
        int olderAnnumToBeConsumed = 1;
        ArrayList<SDbEmployeeBenefitTablesAnnum> benefitAnnums = getBenefitAnnums(benefitType);
        
        if (benefitAnnums != null) {
            HashMap<String, SHrsBenefit> benefitsPaidMap = getBenefitsPaidMap(benefitType);
            int benefitsYear = moEmployee.getBenefitsYear(); // convenience variable

            for (SDbEmployeeBenefitTablesAnnum annum : benefitAnnums) {
                String key = (benefitsYear + (annum.getPkAnnumId() - 1)) + "-" + annum.getPkAnnumId();
                SHrsBenefit benefit = benefitsPaidMap.get(key);
                if (benefit == null || benefit.getPaidDays()< annum.getBenefitDays()) {
                    olderAnnumToBeConsumed = annum.getPkAnnumId();
                    break;
                }
            }
        }
        
        return olderAnnumToBeConsumed;
    }
}
