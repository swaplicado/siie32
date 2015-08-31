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
 *
 * @author Néstor Ávalos
 */
public class SDataCustomerBranchConfig extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkCustomerBranchId;
    protected boolean mbIsDeleted;
    protected int mnFkSalesRouteId;
    protected int mnFkSalesAgentId_n;
    protected int mnFkSalesSupervisorId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsCob;
    protected java.lang.String msDbmsSalesRoute;
    protected java.lang.String msDbmsSalesAgent;
    protected java.lang.String msDbmsSalesSupervisor;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;

    public SDataCustomerBranchConfig() {
        super(SDataConstants.MKT_CFG_CUSB);
        reset();
    }

    public void setPkCustomerBranchId(int n) { mnPkCustomerBranchId = n; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkSalesRouteId(int n) { mnFkSalesRouteId = n; }
    public void setFkSalesAgentId_n(int n) { mnFkSalesAgentId_n = n; }
    public void setFkSalesSupervisorId_n(int n) { mnFkSalesSupervisorId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public void setDbmsCob(java.lang.String s) { msDbmsCob = s; }
    public void setDbmsSalesRoute(java.lang.String s) { msDbmsSalesRoute = s; }
    public void setDbmsSalesAgent(java.lang.String s) { msDbmsSalesAgent = s; }
    public void setDbmsSalesSupervisor(java.lang.String s) { msDbmsSalesSupervisor = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public int getPkCustomerBranchId() { return mnPkCustomerBranchId; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkSalesRouteId() { return mnFkSalesRouteId; }
    public int getFkSalesAgentId_n() { return mnFkSalesAgentId_n; }
    public int getFkSalesSupervisorId_n() { return mnFkSalesSupervisorId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public java.lang.String getDbmsCob() { return msDbmsCob; }
    public java.lang.String getDbmsSalesRoute() { return msDbmsSalesRoute; }
    public java.lang.String getDbmsSalesAgent() { return msDbmsSalesAgent; }
    public java.lang.String getDbmsSalesSupervisor() { return msDbmsSalesSupervisor; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkCustomerBranchId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkCustomerBranchId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkCustomerBranchId = 0;
        mbIsDeleted = false;
        mnFkSalesRouteId = 0;
        mnFkSalesAgentId_n = 0;
        mnFkSalesSupervisorId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsCob = "";
        msDbmsSalesRoute = "";
        msDbmsSalesAgent = "";
        msDbmsSalesSupervisor = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT bpb.bpb, bp.bp, sr.sal_route, bp1.bp AS agt, bp2.bp AS sup, un.usr, ue.usr, ud.usr, cusb.* " +
                    "FROM mkt_cfg_cusb AS cusb " +
                        "INNER JOIN erp.bpsu_bpb AS bpb ON " +
                            "cusb.id_cusb = bpb.id_bpb " +
                        "INNER JOIN mkt_cfg_cus AS cus ON " +
                            "bpb.fid_bp = cus.id_cus " +
                        "INNER JOIN erp.bpsu_bp AS bp ON " +
                            "bpb.fid_bp = bp.id_bp " +
                        "INNER JOIN mktu_sal_route AS sr ON " +
                            "cusb.fid_sal_route = sr.id_sal_route " +
                        "LEFT JOIN erp.bpsu_bp AS bp1 ON " +
                            "cusb.fid_sal_agt_n = bp1.id_bp " +
                        "LEFT JOIN erp.bpsu_bp AS bp2 ON " +
                            "cusb.fid_sal_sup_n = bp2.id_bp " +
                        "INNER JOIN erp.usru_usr AS un ON " +
                            "cusb.fid_usr_new = un.id_usr " +
                        "INNER JOIN erp.usru_usr AS ue ON " +
                            "cusb.fid_usr_edit = ue.id_usr " +
                        "INNER JOIN erp.usru_usr AS ud ON " +
                            "cusb.fid_usr_del = ud.id_usr " +
                    "WHERE cusb.id_cusb = " + key[0] + " " +
                    "ORDER BY bpb.bpb, bp.bp ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkCustomerBranchId = resultSet.getInt("cusb.id_cusb");
                mbIsDeleted = resultSet.getBoolean("cusb.b_del");
                mnFkSalesRouteId = resultSet.getInt("cusb.fid_sal_route");
                mnFkSalesAgentId_n = resultSet.getInt("cusb.fid_sal_agt_n");
                mnFkSalesSupervisorId_n = resultSet.getInt("cusb.fid_sal_sup_n");
                mnFkUserNewId = resultSet.getInt("cusb.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("cusb.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("cusb.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("cusb.ts_new");
                mtUserEditTs = resultSet.getTimestamp("cusb.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("cusb.ts_del");

                msDbmsCob = resultSet.getString("bpb.bpb");
                msDbmsSalesRoute = resultSet.getString("sr.sal_route");
                msDbmsSalesAgent = resultSet.getString("agt");
                msDbmsSalesSupervisor = resultSet.getString("sup");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

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
                    "{ CALL mkt_cfg_cusb_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkCustomerBranchId);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkSalesRouteId);
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
