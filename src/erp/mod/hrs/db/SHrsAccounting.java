/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public class SHrsAccounting {

    protected ArrayList<String> msCompanies;
    protected Connection moConnection;
    protected SGuiSession moGuiSession;
    protected int mnPkAccountingType = 0;
    protected int mnPkReferenceId;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    
    private void computeCompanies() {
        ResultSet resultSet = null;
        String sql = "";
        
        try {
            sql = "SELECT bd FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_CO) + " " +
                    "WHERE b_mod_hrs = 1; ";

            if (moGuiSession != null) {
                resultSet = moGuiSession.getStatement().getConnection().createStatement().executeQuery(sql);
            }
            else {
                resultSet = moConnection.createStatement().executeQuery(sql);
            }
            
            while (resultSet.next()) {
                msCompanies.add(resultSet.getString(1));
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.printOutException(this, e);
        }
    }
    
    public SHrsAccounting(final Connection connection, final SGuiSession guiSession) {
        moConnection = connection;
        moGuiSession = guiSession;
        msCompanies = new ArrayList<String>();
        
        computeCompanies();
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
        ResultSet resultSet = null;
        String sql = "";
        SDbAccountingEarning accountingEarning = null;
        SDbAccountingDeduction accountingDeduction = null;
        
        for (String tableName : msCompanies) {
            // Save Accounting Earnings:

            sql = "SELECT DISTINCT id_ear, b_del FROM " + tableName + "." + SModConsts.TablesMap.get(SModConsts.HRS_ACC_EAR) + " " +
                    "WHERE id_tp_acc = " + mnPkAccountingType + " ";

            if (moGuiSession != null) {
                resultSet = moGuiSession.getStatement().getConnection().createStatement().executeQuery(sql);
            }
            else {
                resultSet = moConnection.createStatement().executeQuery(sql);
            }
            
            while (resultSet.next()) {
                if (moGuiSession != null) {
                    accountingEarning = new SDbAccountingEarning();
                    
                    accountingEarning.setPkEarningId(resultSet.getInt("id_ear"));
                    accountingEarning.setPkAccountingTypeId(mnPkAccountingType);
                    accountingEarning.setPkReferenceId(mnPkReferenceId);
                    accountingEarning.setDeleted(resultSet.getBoolean("b_del"));
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
                    accountingEarning.setAuxTable(tableName);
                    
                    accountingEarning.save(moGuiSession);
                }
                else {
                    sql = "INSERT INTO " + tableName + "." + "hrs_acc_ear VALUES (" +
                            resultSet.getInt("id_ear") + ", " + 
                            mnPkAccountingType + ", " + 
                            mnPkReferenceId + ", " + 
                            resultSet.getInt("b_del") + ", " + 
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
                            ")";

                    moConnection.createStatement().execute(sql);
                }
            }

            // Save Accounting Deductions:

            sql = "SELECT DISTINCT id_ded, b_del FROM " + tableName + "." + SModConsts.TablesMap.get(SModConsts.HRS_ACC_DED) + " " +
                    "WHERE id_tp_acc = " + mnPkAccountingType + " ";

            if (moGuiSession != null) {
                resultSet = moGuiSession.getStatement().getConnection().createStatement().executeQuery(sql);
            }
            else {
                resultSet = moConnection.createStatement().executeQuery(sql);
            }
            
            while (resultSet.next()) {
                if (moGuiSession != null) {
                    accountingDeduction = new SDbAccountingDeduction();
                    
                    accountingDeduction.setPkDeductionId(resultSet.getInt("id_ded"));
                    accountingDeduction.setPkAccountingTypeId(mnPkAccountingType);
                    accountingDeduction.setPkReferenceId(mnPkReferenceId);
                    accountingDeduction.setDeleted(resultSet.getBoolean("b_del"));
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
                    accountingDeduction.setAuxTable(tableName);
                    
                    accountingDeduction.save(moGuiSession);
                }
                else {
                    sql = "INSERT INTO " + tableName + "." + "hrs_acc_ded VALUES (" +
                            resultSet.getInt("id_ded") + ", " + 
                            mnPkAccountingType + ", " + 
                            mnPkReferenceId + ", " + 
                            resultSet.getInt("b_del") + ", " + 
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
                            ")";

                    moConnection.createStatement().execute(sql);
                }
            }
        }
    }
}
