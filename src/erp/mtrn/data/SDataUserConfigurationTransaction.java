/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.musr.data.SDataUserFunctionalArea;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following classes:
 * -  erp.musr.data.SDataUser
 * All of them also make raw SQL insertions.
 */

/**
 *
 * @author Alfonso Flores, Isabel Servín, Adrián Avilés
 */
public class SDataUserConfigurationTransaction extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkUserId;
    protected boolean mbIsPurchasesItemAllApplying;
    protected double mdPurchasesContractLimit_n;
    protected double mdPurchasesOrderLimit_n;
    protected double mdPurchasesOrderLimitMonthly_n;
    protected double mdPurchasesDocLimit_n;
    protected boolean mbIsSalesItemAllApplying;
    protected double mdSalesContractLimit_n;
    protected double mdSalesOrderLimit_n;
    protected double mdSalesOrderLimitMonthly_n;
    protected double mdSalesDocLimit_n;
    protected double mdCapacityVolumeMinPercentage;
    protected double mdCapacityMassMinPercentage;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsUser;
    
    private ArrayList<SDataUserFunctionalArea> maUserFunctionalArea;
    private ArrayList<SDataUserDnsDps> maUserDnsDps;

    public SDataUserConfigurationTransaction() {
        super(SDataConstants.TRN_USR_CFG);
        reset();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setIsPurchasesItemAllApplying(boolean b) { mbIsPurchasesItemAllApplying = b; }
    public void setPurchasesContractLimit_n(double d) { mdPurchasesContractLimit_n = d; }
    public void setPurchasesOrderLimit_n(double d) { mdPurchasesOrderLimit_n = d; }
    public void setPurchasesOrderLimitMonthly_n(double d) { mdPurchasesOrderLimitMonthly_n = d; }
    public void setPurchasesDocLimit_n(double d) { mdPurchasesDocLimit_n = d; }
    public void setIsSalesItemAllApplying(boolean b) { mbIsSalesItemAllApplying = b; }
    public void setSalesContractLimit_n(double d) { mdSalesContractLimit_n = d; }
    public void setSalesOrderLimit_n(double d) { mdSalesOrderLimit_n = d; }
    public void setSalesOrderLimitMonthly_n(double d) { mdSalesOrderLimitMonthly_n = d; }
    public void setSalesDocLimit_n(double d) { mdSalesDocLimit_n = d; }
    public void setCapacityVolumeMinPercentage(double d) { mdCapacityVolumeMinPercentage = d; }
    public void setCapacityMassMinPercentage(double d) { mdCapacityMassMinPercentage = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkUserId() { return mnPkUserId; }
    public boolean getIsPurchasesItemAllApplying() { return mbIsPurchasesItemAllApplying; }
    public double getPurchasesContractLimit_n() { return mdPurchasesContractLimit_n; }
    public double getPurchasesOrderLimit_n() { return mdPurchasesOrderLimit_n; }
    public double getPurchasesOrderLimitMonthly_n() { return mdPurchasesOrderLimitMonthly_n; }
    public double getPurchasesDocLimit_n() { return mdPurchasesDocLimit_n; }
    public boolean getIsSalesItemAllApplying() { return mbIsSalesItemAllApplying; }
    public double getSalesContractLimit_n() { return mdSalesContractLimit_n; }
    public double getSalesOrderLimit_n() { return mdSalesOrderLimit_n; }
    public double getSalesOrderLimitMonthly_n() { return mdSalesOrderLimitMonthly_n; }
    public double getSalesDocLimit_n() { return mdSalesDocLimit_n; }
    public double getCapacityVolumeMinPercentage() { return mdCapacityVolumeMinPercentage; }
    public double getCapacityMassMinPercentage() { return mdCapacityMassMinPercentage; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    public ArrayList<SDataUserFunctionalArea> getUserFunctionalArea() { return maUserFunctionalArea; }
    public ArrayList<SDataUserDnsDps> getUserDnsDps() { return maUserDnsDps; }

    public String getDbmsUser() { return msDbmsUser; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkUserId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkUserId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkUserId = 0;
        mbIsPurchasesItemAllApplying = false;
        mdPurchasesContractLimit_n = 0;
        mdPurchasesOrderLimit_n = 0;
        mdPurchasesOrderLimitMonthly_n = 0;
        mdPurchasesDocLimit_n = 0;
        mbIsSalesItemAllApplying = false;
        mdSalesContractLimit_n = 0;
        mdSalesOrderLimit_n = 0;
        mdSalesOrderLimitMonthly_n = 0;
        mdSalesDocLimit_n = 0;
        mdCapacityVolumeMinPercentage = 0;
        mdCapacityMassMinPercentage = 0;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
        
        maUserFunctionalArea = new ArrayList<>();
        maUserDnsDps = new ArrayList<>();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;
        Statement oStatementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT t.*, u.usr FROM trn_usr_cfg AS t INNER JOIN erp.usru_usr AS u ON t.id_usr = u.id_usr " +
                    "WHERE t.id_usr = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUserId = resultSet.getInt("t.id_usr");
                mbIsPurchasesItemAllApplying = resultSet.getBoolean("t.b_pur_item_all");
                
                mdPurchasesContractLimit_n = resultSet.getDouble("t.pur_con_lim_n");
                if (resultSet.wasNull()) {
                    mdPurchasesContractLimit_n = 0;
                }
                
                mdPurchasesOrderLimit_n = resultSet.getDouble("t.pur_ord_lim_n");
                if (resultSet.wasNull()) {
                    mdPurchasesOrderLimit_n = 0;
                }
                mdPurchasesOrderLimitMonthly_n = resultSet.getDouble("t.pur_ord_lim_mon_n");
                if (resultSet.wasNull()) {
                    mdPurchasesOrderLimitMonthly_n = 0;
                }
                mdPurchasesDocLimit_n = resultSet.getDouble("t.pur_doc_lim_n");
                if (resultSet.wasNull()) {
                    mdPurchasesDocLimit_n = 0;
                }
                
                
                mbIsSalesItemAllApplying = resultSet.getBoolean("t.b_sal_item_all");
                
                mdSalesContractLimit_n = resultSet.getDouble("t.sal_con_lim_n");
                if (resultSet.wasNull()) {
                    mdSalesContractLimit_n = 0;
                }
                
                mdSalesOrderLimit_n = resultSet.getDouble("t.sal_ord_lim_n");
                if (resultSet.wasNull()) {
                    mdSalesOrderLimit_n = 0;
                }
                mdSalesOrderLimitMonthly_n = resultSet.getDouble("t.sal_ord_lim_mon_n");
                if (resultSet.wasNull()) {
                    mdSalesOrderLimitMonthly_n = 0;
                }
                mdSalesDocLimit_n = resultSet.getDouble("t.sal_doc_lim_n");
                if (resultSet.wasNull()) {
                    mdSalesDocLimit_n = 0;
                }
                
                
                mdCapacityVolumeMinPercentage = resultSet.getDouble("t.cap_vol_min_per");
                mdCapacityMassMinPercentage = resultSet.getDouble("t.cap_mass_min_per");
                mbIsDeleted = resultSet.getBoolean("t.b_del");
                mnFkUserNewId = resultSet.getInt("t.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("t.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("t.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("t.ts_new");
                mtUserEditTs = resultSet.getTimestamp("t.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("t.ts_del");

                msDbmsUser = resultSet.getString("u.usr");

                oStatementAux = statement.getConnection().createStatement();
                
                // Read aswell user functional areas:

                sql = "SELECT id_func " +
                        "FROM " + SModConsts.TablesMap.get(SModConsts.USR_USR_FUNC) + " " +
                        "WHERE id_usr = " + mnPkUserId + " " +
                        "ORDER BY id_func ";
                
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataUserFunctionalArea userFunctionalArea = new SDataUserFunctionalArea();
                    if (userFunctionalArea.read(new int[] { mnPkUserId, resultSet.getInt("id_func") }, oStatementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        maUserFunctionalArea.add(userFunctionalArea);
                    }
                }
                
                // Read aswell user document number series
                
                sql = "SELECT id_dns " + 
                        "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_USR_DPS_DNS) + " " + 
                        "WHERE id_usr = " + mnPkUserId + " ";
                
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataUserDnsDps userDnsDps = new SDataUserDnsDps();
                    if (userDnsDps.read(new int[] { mnPkUserId, resultSet.getInt(1) }, oStatementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        maUserDnsDps.add(userDnsDps);
                    }
                }
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int nParam = 1;
        String sql = "";
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL trn_usr_cfg_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkUserId);
            callableStatement.setBoolean(nParam++, mbIsPurchasesItemAllApplying);
            if (mdPurchasesContractLimit_n >= 0) callableStatement.setDouble(nParam++, mdPurchasesContractLimit_n); else callableStatement.setNull(nParam++, java.sql.Types.DECIMAL);
            if (mdPurchasesOrderLimit_n >= 0) callableStatement.setDouble(nParam++, mdPurchasesOrderLimit_n); else callableStatement.setNull(nParam++, java.sql.Types.DECIMAL);
            if (mdPurchasesOrderLimitMonthly_n >= 0) callableStatement.setDouble(nParam++, mdPurchasesOrderLimitMonthly_n); else callableStatement.setNull(nParam++, java.sql.Types.DECIMAL);
            if (mdPurchasesDocLimit_n >= 0) callableStatement.setDouble(nParam++, mdPurchasesDocLimit_n); else callableStatement.setNull(nParam++, java.sql.Types.DECIMAL);
            callableStatement.setBoolean(nParam++, mbIsSalesItemAllApplying);
            if (mdSalesContractLimit_n >= 0) callableStatement.setDouble(nParam++, mdSalesContractLimit_n); else callableStatement.setNull(nParam++, java.sql.Types.DECIMAL);
            if (mdSalesOrderLimit_n >= 0) callableStatement.setDouble(nParam++, mdSalesOrderLimit_n); else callableStatement.setNull(nParam++, java.sql.Types.DECIMAL);
            if (mdSalesOrderLimitMonthly_n >= 0) callableStatement.setDouble(nParam++, mdSalesOrderLimitMonthly_n); else callableStatement.setNull(nParam++, java.sql.Types.DECIMAL);
            if (mdSalesDocLimit_n >= 0) callableStatement.setDouble(nParam++, mdSalesDocLimit_n); else callableStatement.setNull(nParam++, java.sql.Types.DECIMAL);
            callableStatement.setDouble(nParam++, mdCapacityVolumeMinPercentage);
            callableStatement.setDouble(nParam++, mdCapacityMassMinPercentage);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            // 3. Delete aswell user functional areas:
            Statement statement = null;

            statement = connection.createStatement();
            
            sql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.USR_USR_FUNC) + " WHERE id_usr = " + mnPkUserId + " ";
            statement.executeUpdate(sql);
            
            // 3. Save aswell user functional areas:

            for (SDataUserFunctionalArea userFunctionalArea : maUserFunctionalArea) {
                userFunctionalArea.setPkUserId(mnPkUserId);
                userFunctionalArea.setIsRegistryNew(true);

                if (userFunctionalArea.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
            }
            
            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}
