/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Date;

/**
 * WARNING: Every change that affects the structure of this registry must be reflected in SIIE/ETL Avista classes and methods!
 * @author Néstor Ávalos, Isabel Servín, Sergio Flores
 */
public class SDataCustomerConfig extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkCustomerId;
    protected boolean mbIsSignRestricted;
    protected boolean mbIsSignImmex;
    protected boolean mbIsFreeDiscountDoc;
    protected boolean mbIsFreeCommissions;
    protected boolean mbIsDeleted;
    protected int mnFkCustomerTypeId;
    protected int mnFkMarketSegmentId;
    protected int mnFkMarketSubsegmentId;
    protected int mnFkDistributionChannelId;
    protected int mnFkSalesAgentId_n;
    protected int mnFkSalesSupervisorId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataCustomerConfig() {
        super(SDataConstants.MKT_CFG_CUS);

        reset();
    }

    public void setPkCustomerId(int n) { mnPkCustomerId = n; }
    public void setIsSignRestricted(boolean b) { mbIsSignRestricted = b; }
    public void setIsSignImmex(boolean b) { mbIsSignImmex = b; }
    public void setIsFreeDiscountDoc(boolean b) { mbIsFreeDiscountDoc = b; }
    public void setIsFreeCommissions(boolean b) { mbIsFreeCommissions = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkCustomerTypeId(int n) { mnFkCustomerTypeId = n; }
    public void setFkMarketSegmentId(int n) { mnFkMarketSegmentId = n; }
    public void setFkMarketSubsegmentId(int n) { mnFkMarketSubsegmentId = n; }
    public void setFkDistributionChannelId(int n) { mnFkDistributionChannelId = n; }
    public void setFkSalesAgentId_n(int n) { mnFkSalesAgentId_n = n; }
    public void setFkSalesSupervisorId_n(int n) { mnFkSalesSupervisorId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkCustomerId() { return mnPkCustomerId; }
    public boolean getIsSignRestricted() { return mbIsSignRestricted; }
    public boolean getIsSignImmex() { return mbIsSignImmex; }
    public boolean getIsFreeDiscountDoc() { return mbIsFreeDiscountDoc; }
    public boolean getIsFreeCommissions() { return mbIsFreeCommissions; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkCustomerTypeId() { return mnFkCustomerTypeId; }
    public int getFkMarketSegmentId() { return mnFkMarketSegmentId; }
    public int getFkMarketSubsegmentId() { return mnFkMarketSubsegmentId; }
    public int getFkDistributionChannelId() { return mnFkDistributionChannelId; }
    public int getFkSalesAgentId_n() { return mnFkSalesAgentId_n; }
    public int getFkSalesSupervisorId_n() { return mnFkSalesSupervisorId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkCustomerId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkCustomerId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkCustomerId = 0;
        mbIsSignRestricted = false;
        mbIsSignImmex = false;
        mbIsFreeDiscountDoc = false;
        mbIsFreeCommissions = false;
        mbIsDeleted = false;
        mnFkCustomerTypeId = 0;
        mnFkMarketSegmentId = 0;
        mnFkMarketSubsegmentId = 0;
        mnFkDistributionChannelId = 0;
        mnFkSalesAgentId_n = 0;
        mnFkSalesSupervisorId_n = 0;
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
        String sql;
        ResultSet resultSet;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM mkt_cfg_cus WHERE id_cus = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkCustomerId = resultSet.getInt("id_cus");
                mbIsSignRestricted = resultSet.getBoolean("b_sign_restrict");
                mbIsSignImmex = resultSet.getBoolean("b_sign_immex");
                mbIsFreeDiscountDoc = resultSet.getBoolean("b_free_disc_doc");
                mbIsFreeCommissions = resultSet.getBoolean("b_free_comms");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkCustomerTypeId = resultSet.getInt("fid_tp_cus");
                mnFkMarketSegmentId = resultSet.getInt("fid_mkt_segm");
                mnFkMarketSubsegmentId = resultSet.getInt("fid_mkt_sub");
                mnFkDistributionChannelId = resultSet.getInt("fid_dist_chan");
                mnFkSalesAgentId_n = resultSet.getInt("fid_sal_agt_n");
                mnFkSalesSupervisorId_n = resultSet.getInt("fid_sal_sup_n");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

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
        CallableStatement callableStatement;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mkt_cfg_cus_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkCustomerId);
            callableStatement.setBoolean(nParam++, mbIsSignRestricted);
            callableStatement.setBoolean(nParam++, mbIsSignImmex);
            callableStatement.setBoolean(nParam++, mbIsFreeDiscountDoc);
            callableStatement.setBoolean(nParam++, mbIsFreeCommissions);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkCustomerTypeId);
            callableStatement.setInt(nParam++, mnFkMarketSegmentId);
            callableStatement.setInt(nParam++, mnFkMarketSubsegmentId);
            callableStatement.setInt(nParam++, mnFkDistributionChannelId);
            if (mnFkSalesAgentId_n > 0) callableStatement.setInt(nParam++, mnFkSalesAgentId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkSalesSupervisorId_n > 0) callableStatement.setInt(nParam++, mnFkSalesSupervisorId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
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
    public Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}
