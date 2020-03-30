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
    
    protected Connection moAuxFormerEmployerConnection;
    
    private SHrsAccounting(final Connection connection, final SGuiSession session) {
        moConnection = connection;
        moSession = session;
        mnPkAccountingType = 0;
        mnPkReferenceId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        
        moAuxFormerEmployerConnection = null;
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
        this(null, session);
    }

    public void setAccountingType(int d) { mnPkAccountingType = d; }
    public void setPkReferenceId(int n) { mnPkReferenceId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    
    /**
     * Set former employer connection to execute employee new earning and deduction accounting configurations within a set of sibling companies when an employeer substitution is being processed.
     * Requires as well that a connection has been provided in constructor of this instance.
     * @param formerEmployerConnection Former employer connection.
     */
    public void setAuxFormerEmployerConnection(Connection formerEmployerConnection) { moAuxFormerEmployerConnection = formerEmployerConnection; }
    
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
            // when moAuxFormerEmployerConnection has been set, use it for database updates to keep them into the global transaction:
            Connection connection = moAuxFormerEmployerConnection != null ? moAuxFormerEmployerConnection : moConnection;
            
            statement = connection.createStatement();
            statementAux = connection.createStatement();
        }
        else {
            statement = moSession.getStatement().getConnection().createStatement();
            statementAux = moSession.getStatement().getConnection().createStatement();
        }
        
        for (String schema : schemas) {
            // Save accounting configuration for earnings:

            String sql = "SELECT DISTINCT id_ear "
                    + "FROM " + schema + "." + SModConsts.TablesMap.get(SModConsts.HRS_ACC_EAR) + " "
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
                    sql = "INSERT INTO " + schema + "." + SModConsts.TablesMap.get(SModConsts.HRS_ACC_EAR) + " VALUES (" +
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

                    statementAux.execute(sql);
                }
            }

            // Save accounting configuration for deductions:

            sql = "SELECT DISTINCT id_ded "
                    + "FROM " + schema + "." + SModConsts.TablesMap.get(SModConsts.HRS_ACC_DED) + " "
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
                    sql = "INSERT INTO " + schema + "." + SModConsts.TablesMap.get(SModConsts.HRS_ACC_DED) + " VALUES (" +
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

                    statementAux.execute(sql);
                }
            }
        }
        
        statement.close();
        statementAux.close();
    }
}
