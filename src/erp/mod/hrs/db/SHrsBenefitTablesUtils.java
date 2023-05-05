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
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SHrsBenefitTablesUtils {

    /**
     * Create array of benefit types: annum bonus, vacation and vacation bonus.
     * @return
     */
    public static int[] createBenefitTypes() {
        return new int[] { SModSysConsts.HRSS_TP_BEN_ANN_BON, SModSysConsts.HRSS_TP_BEN_VAC, SModSysConsts.HRSS_TP_BEN_VAC_BON };
    }
    
    /**
     * Read all benefit tables of required employee.
     * @param session GUI user session.
     * @param employee ID of employee.
     * @return Array of all benefit tables of required employee.
     * @throws Exception 
     */
    public static ArrayList<SDbEmployeeBenefitTables> readBenefitTables(final SGuiSession session, final int employee) throws Exception {
        ArrayList<SDbEmployeeBenefitTables> maBenefitTableses = new ArrayList<>();

        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String sql = "SELECT id_ben "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_BEN) + " "
                    + "WHERE id_emp = " + employee + " "
                    + "ORDER BY id_ben;";
            
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                SDbEmployeeBenefitTables benefitTables = new SDbEmployeeBenefitTables();
                benefitTables.read(session, new int[] { employee, resultSet.getInt("id_ben") });
                maBenefitTableses.add(benefitTables);
            }
        }
        
        return maBenefitTableses;
    }
    
    /**
     * Get the current benefit tables of required employee, that is, the most recent, active and non-obsolete ones.
     * @param session GUI user session.
     * @param employee ID of employee.
     * @return The current benefit tables of required employee, otherwise <code>null</code>.
     * @throws Exception 
     */
    public static SDbEmployeeBenefitTables getCurrentBenefitTables(final SGuiSession session, final int employee) throws Exception {
        SDbEmployeeBenefitTables benefitTables = null;
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String sql = "SELECT MAX(id_ben) "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_BEN) + " "
                    + "WHERE id_emp = " + employee + " AND NOT b_del AND NOT b_obs;";
            
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                benefitTables = new SDbEmployeeBenefitTables();
                benefitTables.read(session, new int[] { employee, resultSet.getInt(1) });
            }
        }
        
        return benefitTables;
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
