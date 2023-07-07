/*
 * (c) Software Aplicado SA de CV.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SHrsBenefitUtils {

    /**
     * Create array of benefit types: annum bonus, vacation and vacation bonus.
     * @return
     */
    public static int[] createBenefitTypes() {
        return new int[] { SModSysConsts.HRSS_TP_BEN_ANN_BON, SModSysConsts.HRSS_TP_BEN_VAC, SModSysConsts.HRSS_TP_BEN_VAC_BON };
    }
    
    /**
     * Get benefit computation for benefit type.
     * @param benefitType Benefit type. Options defined in SModSysConsts.HRSS_TP_BEN_...
     * @return Benefit computation. Constants defined in SHrsConsts.BEN_COMP_...
     */
    public static int getBenefitCompFromBenefitType(final int benefitType) {
        int benefitComputation = 0;
        
        switch (benefitType) {
            case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                benefitComputation = SHrsConsts.BEN_COMP_CALENDAR_YEAR;
                break;
            case SModSysConsts.HRSS_TP_BEN_VAC:
            case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                benefitComputation = SHrsConsts.BEN_COMP_ANNIVERSARY;
                break;
            default:
                // nothing
        }
        
        return benefitComputation;
    }
    
    /**
     * Get description of given benefit computation.
     * @param benefitComp Benefit computation. Constants defined in SHrsConsts.BEN_COMP_...
     * @return Name of given benefit computation.
     */
    public static String getBenefitCompDescription(final int benefitComp) {
        String name = "";
        
        switch (benefitComp) {
            case SHrsConsts.BEN_COMP_CALENDAR_YEAR:
                name = "Por año calendario";
                break;
            case SHrsConsts.BEN_COMP_ANNIVERSARY:
                name = "Por aniversario";
                break;
            default:
                // nothing
        }
        
        return name;
    }
    
    /**
     * Create exception due to incorrect count of benefit annums of given benefit type.
     * @param session GUI user session.
     * @param employeeId ID of employee.
     * @param benefitType Benefit type. Options defined in SModSysConsts.HRSS_TP_BEN_...
     * @param countActual Actual count of benefit annums.
     * @param countRequired Required count of benefit annums.
     * @return 
     */
    public static Exception createBenefitAnnumsCountException(final SGuiSession session, final int employeeId, final int benefitType, final int countActual, final int countRequired) {
        return new Exception("La tabla de prestaciones '" + (benefitType == 0 ? SUtilConsts.TXT_UNKNOWN : (String) session.readField(SModConsts.HRSS_TP_BEN, new int[] { benefitType }, SDbRegistry.FIELD_NAME)) + "' "
                + "de '" + (employeeId == 0 ? SUtilConsts.TXT_UNKNOWN : (String) session.readField(SModConsts.HRSU_EMP, new int[] { employeeId }, SDbRegistry.FIELD_NAME)) + "' "
                + "tiene " + countActual + " aniversarios, pero se requieren " + countRequired + ".");
    }
    
    /**
     * Get the current benefit tables of required employee, that is, the most recent, active and non-obsolete ones.
     * @param session GUI user session.
     * @param employeeId ID of employee.
     * @return The current benefit tables of required employee, otherwise <code>null</code>.
     * @throws Exception 
     */
    public static SDbEmployeeBenefitTables getCurrentBenefitTables(final SGuiSession session, final int employeeId) throws Exception {
        SDbEmployeeBenefitTables benefitTables = null;
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String sql = "SELECT MAX(id_ben) "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_BEN) + " "
                    + "WHERE id_emp = " + employeeId + " AND NOT b_del AND NOT b_obs;";
            
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                benefitTables = new SDbEmployeeBenefitTables();
                benefitTables.read(session, new int[] { employeeId, resultSet.getInt(1) });
            }
        }
        
        return benefitTables;
    }
    
    /**
     * Get required benefit annum from given array of benefit annums.
     * @param session GUI user session (for retrieving name of benefit type for exceptions).
     * @param benefitAnnums Array of benefit annums of certain benefit type.
     * @param annum Required annum.
     * @return Benefit annum.
     * @throws Exception Exception thrown if number of benefit annums in given array differs from SDbEmployeeBenefitTables.MAX_ANNUMS.
     */
    public static SDbEmployeeBenefitTablesAnnum getBenefitAnnum(final SGuiSession session, final ArrayList<SDbEmployeeBenefitTablesAnnum> benefitAnnums, final int annum) throws Exception {
        if (benefitAnnums == null || benefitAnnums.isEmpty()) {
            throw SHrsBenefitUtils.createBenefitAnnumsCountException(session, 0, 0, 0, SDbEmployeeBenefitTables.MAX_ANNUMS);
        }
        
        if (benefitAnnums.size() != SDbEmployeeBenefitTables.MAX_ANNUMS) {
            throw SHrsBenefitUtils.createBenefitAnnumsCountException(session, benefitAnnums.get(0).getPkEmployeeId(), benefitAnnums.get(0).getPkBenefitTypeId(), benefitAnnums.size(), SDbEmployeeBenefitTables.MAX_ANNUMS);
        }
        
        return benefitAnnums.get(annum - 1); // index of required annum
    }
    
    /**
     * Compute massive assignation of benefit tables.
     * Every time any employee has their benefit tables updated, the current ones are marked as obsoleta, and another ones are created.
     * @param session GUI user session.
     * @param benefitType Benefit type. Options defined in SModSysConsts.HRSS_TP_BEN_...
     * @param benefitTable Benefit table.
     * @param rows Rows of employees to be assigned massively.
     * @return <code>true</code> when computation is done.
     * @throws Exception 
     */
    public static boolean computeMassiveAssignation(final SGuiSession session, final int benefitType, final int benefitTable, final ArrayList<SRowBenefitTablesMassiveAssignation> rows) throws Exception {
        boolean done = false;
        
        if (rows.isEmpty()) {
            throw new Exception("No hay empleados para procesar.");
        }
        else {
            int computation = 0;
            boolean transactionActive = false;
            
            try {
                session.getStatement().execute("START TRANSACTION;");
                System.out.println("Transaction started");
                transactionActive = true;
                
                SDbEmployeeBenefitTables.PreserveGlobalBenefitAnnumsMap = true; // activate preservation of retrieved benefit annums
                HashMap<Integer, String> paymentTypesMap = new HashMap<>();

                for (SRowBenefitTablesMassiveAssignation row : rows) {
                    SDbEmployee employee = row.getEmployee();
                    SDbEmployeeBenefitTables curBenefitTables = row.getCurrentBenefitTables();
                    SDbEmployeeBenefitTables newBenefitTables = null;
                    String paymentType = paymentTypesMap.get(employee.getFkPaymentTypeId());
                    
                    if (paymentType == null) {
                        paymentType = (String) session.readField(SModConsts.HRSS_TP_PAY, new int[] { employee.getFkPaymentTypeId() }, SDbRegistry.FIELD_NAME);
                        paymentTypesMap.put(employee.getFkPaymentTypeId(), paymentType);
                    }
                    
                    System.out.println("Processing employee "
                            + "name: '" + employee.getXtaEmployeeName() + "', "
                            + "ID: " + employee.getPkEmployeeId() + ", "
                            + "active: " + employee.isActive() + ", "
                            + "unionized: " + employee.isUnionized() + ", "
                            + "payment type: " + paymentType + ", "
                            + "benefits: " + SLibUtils.DateFormatDate.format(employee.getDateBenefits()) + " "
                            + "(" + ++computation + "/" + rows.size() + ")...");

                    if (curBenefitTables != null) {
                        newBenefitTables = curBenefitTables.clone();
                        newBenefitTables.setRegistryNew(true);

                        curBenefitTables.setObsolete(true);
                        curBenefitTables.save(session);
                    }
                    else {
                        newBenefitTables = new SDbEmployeeBenefitTables();
                        newBenefitTables.setPkEmployeeId(employee.getPkEmployeeId());
                    }

                    newBenefitTables.assignBenefitTable(session, benefitType, benefitTable, row.getAssignationAnniversary());
                    newBenefitTables.save(session);
                }

                session.getStatement().execute("COMMIT;");
                System.out.println("Transaction committed");
                transactionActive = false;
                
                done = true;
            }
            catch (Exception e) {
                if (transactionActive) {
                    session.getStatement().execute("ROLLBACK;");
                }
                
                throw e;
            }
        }
        
        return done;
    }
}
