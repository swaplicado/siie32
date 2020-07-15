/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SHrsAccounting {

    protected Connection moConnection;
    protected SGuiSession moSession;
    protected int mnPkAccountingType;
    protected int mnPkReferenceId;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    
    private SHrsAccounting(final Connection connection, final SGuiSession session) {
        moConnection = connection;
        moSession = session;
        mnPkAccountingType = 0;
        mnPkReferenceId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
    }

    /**
     * Create an instance for procession accounting configurations of earnings and deductions.
     * When connection provided, supposedly creation of configurations will be processed.
     * On the other hand, when GUI session provided, supposedly updating of configurations will be processed.
     * @param connection Database conection (for former framework of SIIE).
     */
    public SHrsAccounting(final Connection connection) {
        this(connection, null);
    }

    /**
     * Create an instance for procession accounting configurations of earnings and deductions.
     * When GUI session provided, supposedly updating of configurations will be processed.
     * On the other hand, when connection provided, supposedly creation of configurations will be processed.
     * @param session GUI session (for new framework of Software Aplicado, SA-Lib 1.0).
     */
    public SHrsAccounting(final SGuiSession session) {
        this(session.getDatabase().getConnection(), session);
    }

    public void setAccountingType(int d) { mnPkAccountingType = d; }
    public void setPkReferenceId(int n) { mnPkReferenceId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    
    public int getAccountingeType() { return mnPkAccountingType; }
    public int getPkReferenceId() { return mnPkReferenceId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    
    public void save() throws Exception {
        String currentSchema = "";
        ArrayList<String> schemas = null;
        
        // define list of schemas:

        if (moConnection != null) {
            try (Statement statement = moConnection.createStatement()) {
                currentSchema = SHrsEmployeeUtils.getCurrentCompanySchema(statement);
                schemas = SHrsEmployeeUtils.getSiblingCompanySchemas(statement);
            }
        }
        else {
            currentSchema = SHrsEmployeeUtils.getCurrentCompanySchema(moSession.getStatement());
            schemas = SHrsEmployeeUtils.getSiblingCompanySchemas(moSession.getStatement());
        }

        if (!currentSchema.isEmpty()) {
            schemas.add(0, currentSchema);
        }
        
        ResultSet resultSet;
        Statement statement = null;
        Statement statementAux = null;
        
        if (moConnection != null) {
            statement = moConnection.createStatement();
            statementAux = moConnection.createStatement();
        }
        else {
            statement = moSession.getStatement().getConnection().createStatement();
            statementAux = moSession.getStatement().getConnection().createStatement();
        }
        
        for (String schema : schemas) {
            String sql;
            String table;
            
            // Save accounting configuration for earnings:

            table = schema + "." + SModConsts.TablesMap.get(SModConsts.HRS_ACC_EAR);
            
            sql = "SELECT DISTINCT id_ear "
                    + "FROM " + table + " "
                    + "WHERE id_tp_acc = " + mnPkAccountingType + " "
                    + "ORDER BY id_ear;";
            
            resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                boolean isDeleted = SHrsUtils.isEarningDeleted(statementAux, resultSet.getInt("id_ear"));
                
                if (moSession != null) {
                    SDbAccountingEarning accountingEarning = new SDbAccountingEarning();
                    
                    accountingEarning.setPkEarningId(resultSet.getInt("id_ear"));
                    accountingEarning.setPkAccountingTypeId(mnPkAccountingType);
                    accountingEarning.setPkReferenceId(mnPkReferenceId);
                    accountingEarning.setDeleted(isDeleted);
                    accountingEarning.setFkAccountId(SModSysConsts.FIN_ACC_NA);
                    accountingEarning.setFkCostCenterId_n(SLibConsts.UNDEFINED);
                    accountingEarning.setFkItemId_n(SLibConsts.UNDEFINED);
                    accountingEarning.setFkBizPartnerId_n(SLibConsts.UNDEFINED);
                    accountingEarning.setFkTaxBasicId_n(SLibConsts.UNDEFINED);
                    accountingEarning.setFkTaxTaxId_n(SLibConsts.UNDEFINED);
                    /*
                    accountingEarning.setFkUserInsertId();
                    accountingEarning.setFkUserUpdateId();
                    accountingEarning.setTsUserInsert();
                    accountingEarning.setTsUserUpdate();
                    */
                    accountingEarning.setAuxTable(schema);
                    
                    accountingEarning.save(moSession);
                }
                else {
                    int count = 0;
                    
                    sql = "SELECT COUNT(*) "
                            + "FROM " + table + " "
                            + "WHERE id_ear = " + resultSet.getInt("id_ear") + " AND "
                            + "id_tp_acc = " + mnPkAccountingType + " AND "
                            + "id_ref = " + mnPkReferenceId + ";";

                    try (ResultSet rs = statementAux.executeQuery(sql)) {
                        if (rs.next()) {
                            count = rs.getInt(1);
                        }
                    }
                    
                    if (count == 0) {
                        sql = "INSERT INTO " + table + " VALUES (" +
                                resultSet.getInt("id_ear") + ", " + 
                                mnPkAccountingType + ", " + 
                                mnPkReferenceId + ", " + 
                                isDeleted + ", " + 
                                SModSysConsts.FIN_ACC_NA + ", " + 
                                "NULL, " +
                                "NULL, " +
                                "NULL, " +
                                "NULL, " +
                                "NULL, " +
                                mnFkUserInsertId + ", " + 
                                mnFkUserUpdateId + ", " + 
                                "NOW()" + ", " +
                                "NOW()" + " " +
                                ");";
                    }
                    else {
                        sql = "UPDATE " + table + " SET " +
                                /*
                                "id_ear = " + mnPkEarningId + ", " +
                                "id_tp_acc = " + mnPkAccountingTypeId + ", " +
                                "id_ref = " + mnPkReferenceId + ", " +
                                */
                                "b_del = " + (isDeleted ? 1 : 0) + ", " +
                                /*
                                "fk_acc = " + mnFkAccountId + ", " +
                                "fk_cc_n = " + (mnFkCostCenterId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCostCenterId_n) + ", " +
                                "fk_item_n = " + (mnFkItemId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemId_n) + ", " +
                                "fk_bp_n = " + (mnFkBizPartnerId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkBizPartnerId_n) + ", " +
                                "fk_tax_bas_n = " + (mnFkTaxBasicId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkTaxBasicId_n) + ", " +
                                "fk_tax_tax_n = " + (mnFkTaxTaxId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkTaxTaxId_n) + ", " +
                                */
                                //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                                //"ts_usr_ins = " + "NOW()" + ", " +
                                "ts_usr_upd = " + "NOW()" + " " +
                                "WHERE id_ear = " + resultSet.getInt("id_ear") + " AND " +
                                "id_tp_acc = " + mnPkAccountingType + " AND " +
                                "id_ref = " + mnPkReferenceId + ";";
                    }

                    statementAux.execute(sql);
                }
            }

            // Save accounting configuration for deductions:
            
            table = schema + "." + SModConsts.TablesMap.get(SModConsts.HRS_ACC_DED);

            sql = "SELECT DISTINCT id_ded "
                    + "FROM " + table + " "
                    + "WHERE id_tp_acc = " + mnPkAccountingType + " "
                    + "ORDER BY id_ded;";

            resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                boolean isDeleted = SHrsUtils.isDeductionDeleted(statementAux, resultSet.getInt("id_ded"));
                
                if (moSession != null) {
                    SDbAccountingDeduction accountingDeduction = new SDbAccountingDeduction();
                    
                    accountingDeduction.setPkDeductionId(resultSet.getInt("id_ded"));
                    accountingDeduction.setPkAccountingTypeId(mnPkAccountingType);
                    accountingDeduction.setPkReferenceId(mnPkReferenceId);
                    accountingDeduction.setDeleted(isDeleted);
                    accountingDeduction.setFkAccountId(SModSysConsts.FIN_ACC_NA);
                    accountingDeduction.setFkCostCenterId_n(SLibConsts.UNDEFINED);
                    accountingDeduction.setFkItemId_n(SLibConsts.UNDEFINED);
                    accountingDeduction.setFkBizPartnerId_n(SLibConsts.UNDEFINED);
                    accountingDeduction.setFkTaxBasicId_n(SLibConsts.UNDEFINED);
                    accountingDeduction.setFkTaxTaxId_n(SLibConsts.UNDEFINED);
                    /*
                    accountingDeduction.setFkUserInsertId();
                    accountingDeduction.setFkUserUpdateId();
                    accountingDeduction.setTsUserInsert();
                    accountingDeduction.setTsUserUpdate();
                    */
                    accountingDeduction.setAuxTable(schema);
                    
                    accountingDeduction.save(moSession);
                }
                else {
                    int count = 0;
                    
                    sql = "SELECT COUNT(*) "
                            + "FROM " + table + " "
                            + "WHERE id_ded = " + resultSet.getInt("id_ded") + " AND "
                            + "id_tp_acc = " + mnPkAccountingType + " AND "
                            + "id_ref = " + mnPkReferenceId + ";";

                    try (ResultSet rs = statementAux.executeQuery(sql)) {
                        if (rs.next()) {
                            count = rs.getInt(1);
                        }
                    }
                    
                    if (count == 0) {
                        sql = "INSERT INTO " + table + " VALUES (" +
                                resultSet.getInt("id_ded") + ", " + 
                                mnPkAccountingType + ", " + 
                                mnPkReferenceId + ", " + 
                                isDeleted + ", " + 
                                SModSysConsts.FIN_ACC_NA + ", " + 
                                "NULL, " +
                                "NULL, " +
                                "NULL, " +
                                "NULL, " +
                                "NULL, " +
                                mnFkUserInsertId + ", " + 
                                mnFkUserUpdateId + ", " + 
                                "NOW()" + ", " +
                                "NOW()" + " " +
                                ");";
                    }
                    else {
                        sql = "UPDATE " + table + " SET " +
                                /*
                                "id_ded = " + mnPkDeductionId + ", " +
                                "id_tp_acc = " + mnPkAccountingTypeId + ", " +
                                "id_ref = " + mnPkReferenceId + ", " +
                                */
                                "b_del = " + (isDeleted ? 1 : 0) + ", " +
                                /*
                                "fk_acc = " + mnFkAccountId + ", " +
                                "fk_cc_n = " + (mnFkCostCenterId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCostCenterId_n) + ", " +
                                "fk_item_n = " + (mnFkItemId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemId_n) + ", " +
                                "fk_bp_n = " + (mnFkBizPartnerId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkBizPartnerId_n) + ", " +
                                "fk_tax_bas_n = " + (mnFkTaxBasicId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkTaxBasicId_n) + ", " +
                                "fk_tax_tax_n = " + (mnFkTaxTaxId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkTaxTaxId_n) + ", " +
                                */
                                //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                                //"ts_usr_ins = " + "NOW()" + ", " +
                                "ts_usr_upd = " + "NOW()" + " " +
                                "WHERE id_ded = " + resultSet.getInt("id_ded") + " AND " +
                                "id_tp_acc = " + mnPkAccountingType + " AND " +
                                "id_ref = " + mnPkReferenceId + ";";
                    }

                    statementAux.execute(sql);
                }
            }
        }
        
        statement.close();
        statementAux.close();
    }
}
