/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author Alfonso Flores
 */
public class SDataUserConfigurationTransaction extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkUserId;
    protected boolean mbIsPurchasesItemAllApplying;
    protected double mdPurchasesOrderLimit_n;
    protected double mdPurchasesDocLimit_n;
    protected boolean mbIsSalesItemAllApplying;
    protected double mdSalesOrderLimit_n;
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

    public SDataUserConfigurationTransaction() {
        super(SDataConstants.TRN_USR_CFG);
        reset();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setIsPurchasesItemAllApplying(boolean b) { mbIsPurchasesItemAllApplying = b; }
    public void setPurchasesOrderLimit_n(double d) { mdPurchasesOrderLimit_n = d; }
    public void setPurchasesDocLimit_n(double d) { mdPurchasesDocLimit_n = d; }
    public void setIsSalesItemAllApplying(boolean b) { mbIsSalesItemAllApplying = b; }
    public void setSalesOrderLimit_n(double d) { mdSalesOrderLimit_n = d; }
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
    public double getPurchasesOrderLimit_n() { return mdPurchasesOrderLimit_n; }
    public double getPurchasesDocLimit_n() { return mdPurchasesDocLimit_n; }
    public boolean getIsSalesItemAllApplying() { return mbIsSalesItemAllApplying; }
    public double getSalesOrderLimit_n() { return mdSalesOrderLimit_n; }
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
        mdPurchasesOrderLimit_n = 0;
        mdPurchasesDocLimit_n = 0;
        mbIsSalesItemAllApplying = false;
        mdSalesOrderLimit_n = 0;
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
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

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
                mdPurchasesOrderLimit_n = resultSet.getDouble("t.pur_ord_lim_n");
                if (resultSet.wasNull()) {
                    mdPurchasesOrderLimit_n = 0;
                }
                mdPurchasesDocLimit_n = resultSet.getDouble("t.pur_doc_lim_n");
                if (resultSet.wasNull()) {
                    mdPurchasesDocLimit_n = 0;
                }
                mbIsSalesItemAllApplying = resultSet.getBoolean("t.b_sal_item_all");
                mdSalesOrderLimit_n = resultSet.getDouble("t.sal_ord_lim_n");
                if (resultSet.wasNull()) {
                    mdSalesOrderLimit_n = 0;
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
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL trn_usr_cfg_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkUserId);
            callableStatement.setBoolean(nParam++, mbIsPurchasesItemAllApplying);
            if (mdPurchasesOrderLimit_n >= 0) callableStatement.setDouble(nParam++, mdPurchasesOrderLimit_n); else callableStatement.setNull(nParam++, java.sql.Types.DECIMAL);
            if (mdPurchasesDocLimit_n >= 0) callableStatement.setDouble(nParam++, mdPurchasesDocLimit_n); else callableStatement.setNull(nParam++, java.sql.Types.DECIMAL);
            callableStatement.setBoolean(nParam++, mbIsSalesItemAllApplying);
            if (mdSalesOrderLimit_n >= 0) callableStatement.setDouble(nParam++, mdSalesOrderLimit_n); else callableStatement.setNull(nParam++, java.sql.Types.DECIMAL);
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
