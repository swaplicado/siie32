/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public abstract class SHrsEmployeeUtils {
    
    /**
     * Check if contract type is expirable.
     * @param contractType ID of contract type.
     * @return <code>true</code> if contract type is expirable, otherwise <code>false</code>.
     */
    public static boolean isContractTypeExpirable(final int contractType) {
        return SLibUtils.belongsTo(contractType, new int[] { 
            SModSysConsts.HRSS_TP_CON_WORKING_TIME, 
            SModSysConsts.HRSS_TP_CON_COMMISSION,
            SModSysConsts.HRSS_TP_CON_INSUBORDIN,
            SModSysConsts.HRSS_TP_CON_OTH,
        });
    }
    
    /**
     * Check if contract type is indefinite.
     * @param contractType ID of contract type.
     * @return <code>true</code> if contract type is indefinite, otherwise <code>false</code>.
     */
    public static boolean isContractTypeIndefinite(final int contractType) {
        return SLibUtils.belongsTo(contractType, new int[] { 
            SModSysConsts.HRSS_TP_CON_INDEFINITE, 
            SModSysConsts.HRSS_TP_CON_RETIREMENT
        });
    }
    
    /**
     * Check if contract expiration is required for contract type.
     * @param contractType ID of contract type.
     * @return <code>true</code> if contract expiration is required, otherwise <code>false</code>.
     */
    public static boolean isContractExpirationRequired(final int contractType) {
        return !isContractTypeIndefinite(contractType) && !isContractTypeExpirable(contractType);
    }
    
    /**
     * Check if contract expiration allowed for contract type.
     * @param contractType ID of contract type.
     * @return <code>true</code> if contract expiration is required, otherwise <code>false</code>.
     */
    public static boolean isContractExpirationAllowed(final int contractType) {
        return !isContractTypeIndefinite(contractType) || isContractTypeExpirable(contractType);
    }
    
    /**
     * Get current company schema (database name.)
     * @param statement Database statement.
     * @return Current company schema (database name.)
     * @throws java.lang.Exception
     */
    public static String getCurrentCompanySchema(final Statement statement) throws Exception {
        String schema = "";
        
        String sql = "SELECT bd "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_CO) + " "
                + "WHERE id_co = (SELECT id_co FROM " + SModConsts.TablesMap.get(SModConsts.CFG_PARAM_CO) + ");";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                schema = resultSet.getString(1);
            }
        }
        
        return schema;
    }
    
    /**
     * Get current company ID.
     * @param statement Database statement.
     * @return Current company ID.
     * @throws java.lang.Exception
     */
    public static int getCurrentCompanyId(final Statement statement) throws Exception {
        int id = 0;
        
        String sql = "SELECT id_co "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFG_PARAM_CO) + ";";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        }
        
        return id;
    }
    
    /**
     * Get map of company schemas (database names) with SIIE module of Human Resources enabled.
     * @param statement Database statement.
     * @return <code>HashMap</code> company schemas (database names) with SIIE module of Human Resources enabled: key = ID of company; value = schema (database name) of company.
     * @throws java.lang.Exception
     */
    public static HashMap<Integer, String> getHrsCompanySchemas(final Statement statement) throws Exception {
        HashMap<Integer, String> hrsCompanySchemasMap = new HashMap<>();
        
        String sql = "SELECT id_co, bd "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_CO) + " "
                + "WHERE NOT b_del AND b_mod_hrs; ";

        try (ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                hrsCompanySchemasMap.put(resultSet.getInt("id_co"), resultSet.getString("bd"));
            }
        }
        
        return hrsCompanySchemasMap;
    }
    
    /**
     * Get list of sibling-company schemas (database names) with SIIE module of Human Resources enabled.
     * @param statement Database statement.
     * @return <code>ArrayList</code> of sibling-company schemas (database names) with SIIE module of Human Resources enabled.
     * @throws java.lang.Exception
     */
    public static ArrayList<String> getSiblingCompanySchemas(final Statement statement) throws Exception {
        // map of company schemas (database names) with SIIE module of Human Resources enabled:
        
        HashMap<Integer, String> hrsCompanySchemasMap = getHrsCompanySchemas(statement);
        
        // get list of sibling-company ID's:
        
        String[] siblingCompanyIds = SLibUtils.textExplode(SCfgUtils.getParamValue(statement, SDataConstantsSys.CFG_PARAM_HRS_SIBLING_COMPANIES), ";");
        
        // prepare set of schemas (database names) of sibling companies with SIIE module of Human Resources enabled:
        
        HashSet<String> schemasSet = new HashSet<>();
        
        for (String siblingCompanyId : siblingCompanyIds) {
            String database = hrsCompanySchemasMap.get(SLibUtils.parseInt(siblingCompanyId));
            if (database != null) {
                schemasSet.add(database);
            }
        }
                
        return new ArrayList<>(schemasSet);
    }
    
    /**
     * Get map of non sibling-company schemas (database names) with SIIE module of Human Resources enabled.
     * @param statement Database statement.
     * @return <code>HashMap</code> of non sibling-company schemas (database names) with SIIE module of Human Resources enabled: key = ID of company; value = schema (database name) of company.
     * @throws java.lang.Exception
     */
    public static HashMap<Integer, String> getNonSiblingCompanySchemas(final Statement statement) throws Exception {
        // map of company schemas (database names) with SIIE module of Human Resources enabled:
        
        HashMap<Integer, String> hrsCompanySchemasMap = getHrsCompanySchemas(statement);
        
        // get list of sibling-company ID's:
        
        String[] siblingCompanyIds = SLibUtils.textExplode(SCfgUtils.getParamValue(statement, SDataConstantsSys.CFG_PARAM_HRS_SIBLING_COMPANIES), ";");
        
        // get current company ID:
        
        int currentCompanyId = getCurrentCompanyId(statement);
        
        // prepare map of ID's of company (key) and schemas (database names) (value) of non-sibling companies with SIIE module of Human Resources enabled:
        
        HashMap<Integer, String> schemasMap = new HashMap<>();
        
        for (Integer hrsCompanyId : hrsCompanySchemasMap.keySet()) {
            if (hrsCompanyId == currentCompanyId) {
                continue; // skip current company (obviously)
            }
            
            boolean isSibling = false;
            
            for (String siblingCompanyId : siblingCompanyIds) {
                if (hrsCompanyId == SLibUtils.parseInt(siblingCompanyId)) {
                    isSibling = true;
                    break;
                }
            }
            
            if (!isSibling) {
                schemasMap.put(hrsCompanyId, hrsCompanySchemasMap.get(hrsCompanyId));
            }
        }
        
        return schemasMap;
    }
    
    /**
     * Insert employee membership into provided schema (database name), but when empty into current company in statement.
     * @param statement Database statement.
     * @param schema Schema (database name). When empty, current database chosen.
     * @param employeeId ID of employee to insert.
     * @throws java.lang.Exception
     */
    public static void insertMembership(final Statement statement, final String schema, final int employeeId) throws Exception {
        String table = (schema.isEmpty() ? "" : (schema + ".")) + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER); // convenience variable
        String sql = "SELECT COUNT(*) "
                + "FROM " + table + " "
                + "WHERE id_emp = " + employeeId + ";";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            boolean insert = false;
            
            if (resultSet.next()) {
                insert = resultSet.getInt(1) == 0;
            }
            
            if (insert) {
                sql = "INSERT INTO " + table + " VALUES ("
                        + employeeId + ", "
                        + "0, "
                        + SUtilConsts.USR_NA_ID + ", "
                        + SUtilConsts.USR_NA_ID + ", "
                        + "NOW(), "
                        + "NOW() "
                        + ");";
                statement.execute(sql);
            }
        }
    }
    
    /**
     * Delete employee membership from provided schema (database name), but when empty from current company in statement.
     * @param statement Database statement.
     * @param schema Database schema. When empty, current database chosen.
     * @param employeeId ID of employee to delete.
     * @throws java.lang.Exception
     */
    public static void deleteMembership(final Statement statement, final String schema, final int employeeId) throws Exception {
        String table = (schema.isEmpty() ? "" : (schema + ".")) + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER); // convenience variable
        String sql = "DELETE FROM " + table + " WHERE id_emp = " + employeeId + ";";
        statement.execute(sql);
    }
    
    /**
     * Get list of ID's of companies to wich the employee is already a member.
     * @param statement Database statement.
     * @param employeeId ID of employee.
     * @return <code>ArrayList</code> of ID's of companies to wich the employee is already a member.
     * @throws java.lang.Exception
     */
    public static ArrayList<Integer> getMembershipCompanyIds(final Statement statement, final int employeeId) throws Exception {
        ArrayList<Integer> companyIds = new ArrayList<>();
        HashMap<Integer, String> hrsCompanyDbNames = getHrsCompanySchemas(statement);
        
        for (Integer hrsCompanyId : hrsCompanyDbNames.keySet()) {
            String schema = hrsCompanyDbNames.get(hrsCompanyId); // convenience variable
            String sql = "SELECT COUNT(*) "
                    + "FROM " + schema + "." + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " "
                    + "WHERE id_emp = " + employeeId + ";";
            
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) > 0) {
                        companyIds.add(hrsCompanyId);
                    }
                }
            }
        }
        
        return companyIds;
    }
}
