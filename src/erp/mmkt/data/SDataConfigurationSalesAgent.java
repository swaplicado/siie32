/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.util.Date;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataConfigurationSalesAgent extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkSalesAgentId;
    protected boolean mbIsDeleted;
    protected int mnFkSalesAgentTypeId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataConfigurationSalesAgent() {
        super(SDataConstants.MKT_CFG_SAL_AGT);
        reset();
    }

    public void setPkSalesAgentId(int n) { mnPkSalesAgentId = n; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkSalesAgentTypeId(int n) { mnFkSalesAgentTypeId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkSalesAgentId() { return mnPkSalesAgentId; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkSalesAgentTypeId() { return mnFkSalesAgentTypeId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkSalesAgentId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkSalesAgentId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkSalesAgentId = 0;
        mbIsDeleted = false;
        mnFkSalesAgentTypeId = 0;
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
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM mkt_cfg_sal_agt WHERE id_sal_agt = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkSalesAgentId = resultSet.getInt("id_sal_agt");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkSalesAgentTypeId = resultSet.getInt("fid_tp_sal_agt");
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
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mkt_cfg_sal_agt_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?) }");
            callableStatement.setInt(nParam++, mnPkSalesAgentId);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkSalesAgentTypeId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();
            
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);
            
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
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
