/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.data.SDataReadDescriptions;
import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Sergio Flores
 */
public class SDataAccountItemItem extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkLinkTypeId;
    protected int mnPkReferenceId;
    protected int mnPkBookkepingCenterId;
    protected int mnPkAccountItemId;
    protected java.util.Date mtPkDateStartId;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsLinkType;
    protected java.lang.String msDbmsReference;
    protected java.lang.String msDbmsBookkeepingCenter;

    public SDataAccountItemItem() {
        super(SDataConstants.FIN_ACC_ITEM_ITEM);
        reset();
    }

    public void setPkLinkTypeId(int n) { mnPkLinkTypeId = n; }
    public void setPkReferenceId(int n) { mnPkReferenceId = n; }
    public void setPkBookkepingCenterId(int n) { mnPkBookkepingCenterId = n; }
    public void setPkAccountItemId(int n) { mnPkAccountItemId = n; }
    public void setPkDateStartId(java.util.Date t) { mtPkDateStartId = t; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkLinkTypeId() { return mnPkLinkTypeId; }
    public int getPkReferenceId() { return mnPkReferenceId; }
    public int getPkBookkepingCenterId() { return mnPkBookkepingCenterId; }
    public int getPkAccountItemId() { return mnPkAccountItemId; }
    public java.util.Date getPkDateStartId() { return mtPkDateStartId; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsLinkType(java.lang.String s) { msDbmsLinkType = s; }
    public void setDbmsReference(java.lang.String s) { msDbmsReference = s; }
    public void setDbmsBookkeepingCenter(java.lang.String s) { msDbmsBookkeepingCenter = s; }

    public java.lang.String getDbmsLinkType() { return msDbmsLinkType; }
    public java.lang.String getDbmsReference() { return msDbmsReference; }
    public java.lang.String getDbmsBookkeepingCenter() { return msDbmsBookkeepingCenter; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkLinkTypeId = (Integer) ((Object[]) pk)[0];
        mnPkReferenceId = (Integer) ((Object[]) pk)[1];
        mnPkBookkepingCenterId = (Integer) ((Object[]) pk)[2];
        mnPkAccountItemId = (Integer) ((Object[]) pk)[3];
        mtPkDateStartId = (java.util.Date) ((Object[]) pk)[3];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new Object[] { mnPkLinkTypeId, mnPkReferenceId, mnPkBookkepingCenterId, mnPkAccountItemId, mtPkDateStartId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkLinkTypeId = 0;
        mnPkReferenceId = 0;
        mnPkBookkepingCenterId = 0;
        mnPkAccountItemId = 0;
        mtPkDateStartId = null;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsLinkType = "";
        msDbmsReference = "";
        msDbmsBookkeepingCenter = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        Object[] key = (Object[]) pk;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT ai.*, l.tp_link, bk.bkc " +
                    "FROM fin_acc_item_item AS ai INNER JOIN erp.trns_tp_link AS l ON ai.id_tp_link = l.id_tp_link " +
                    "INNER JOIN fin_bkc AS bk ON ai.id_bkc = bk.id_bkc " +
                    "WHERE ai.id_tp_link = " + key[0] + " AND ai.id_ref = " + key[1] + " AND " +
                    "ai.id_bkc = " + key[2] + " AND ai.id_acc_item = " + key[3] + " AND ai.id_dt_start = '" + key[4] + "' ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkLinkTypeId = resultSet.getInt("ai.id_tp_link");
                mnPkReferenceId = resultSet.getInt("ai.id_ref");
                mnPkBookkepingCenterId = resultSet.getInt("ai.id_bkc");
                mnPkAccountItemId = resultSet.getInt("ai.id_acc_item");
                mtPkDateStartId = resultSet.getDate("ai.id_dt_start");
                mbIsDeleted = resultSet.getBoolean("ai.b_del");
                mnFkUserNewId = resultSet.getInt("ai.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("ai.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("ai.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ai.ts_new");
                mtUserEditTs = resultSet.getTimestamp("ai.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ai.ts_del");

                msDbmsLinkType = resultSet.getString("l.tp_link");
                msDbmsBookkeepingCenter = resultSet.getString("bk.bkc");

                sql = SDataReadDescriptions.createQueryForLinkReference(mnPkLinkTypeId, new int[] { mnPkReferenceId });
                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    msDbmsReference = resultSet.getString("descrip");

                    mbIsRegistryNew = false;
                    mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
                }
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
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL fin_acc_item_item_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkLinkTypeId);
            callableStatement.setInt(nParam++, mnPkReferenceId);
            callableStatement.setInt(nParam++, mnPkBookkepingCenterId);
            callableStatement.setInt(nParam++, mnPkAccountItemId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtPkDateStartId.getTime()));
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
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
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return null;
    }
}
