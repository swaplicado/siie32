/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author Alfonso Flores, Claudio Peña, Sergio Flores
 */
public class SDataItemConfigBizPartner extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkItemId;
    protected int mnPkBizPartnerId;
    protected int mnPkConfigId;
    protected java.lang.String msKey;
    protected java.lang.String msItem;
    protected java.lang.String msItemShort;
    protected java.lang.String mbCfdiUsage;
    protected boolean mbIsItemDescription;
    protected boolean mbIsDeleted;
    protected int mnFkUnitId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsItem;
    protected java.lang.String msDbmsBizPartner;
    protected java.lang.String msDbmsUnit;

    protected erp.mitm.data.SDataUnit moDbmsDataUnit;

    public SDataItemConfigBizPartner() {
        super(SDataConstants.ITMU_CFG_ITEM_BP);
        reset();
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkConfigId(int n) { mnPkConfigId = n; }
    public void setKey(java.lang.String s) { msKey = s; }
    public void setItem(java.lang.String s) { msItem = s; }
    public void setItemShort(java.lang.String s) { msItemShort = s; }
    public void setCfdiUsage(java.lang.String s) { mbCfdiUsage = s; }
    public void setIsItemDescription(boolean b) { mbIsItemDescription = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkConfigId() { return mnPkConfigId; }
    public java.lang.String getKey() { return msKey; }
    public java.lang.String getItem() { return msItem; }
    public java.lang.String getItemShort() { return msItemShort; }
    public java.lang.String getCfdiUsage() { return mbCfdiUsage; }
    public boolean getIsItemDescription() { return mbIsItemDescription; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsItem(java.lang.String s) { msDbmsItem = s; }
    public void setDbmsBizPartner(java.lang.String s) { msDbmsBizPartner = s; }
    public void setDbmsUnit(java.lang.String s) { msDbmsUnit = s; }

    public java.lang.String getDbmsItem() { return msDbmsItem; }
    public java.lang.String getDbmsBizPartner() { return msDbmsBizPartner; }
    public java.lang.String getDbmsUnit() { return msDbmsUnit; }
    public erp.mitm.data.SDataUnit getDbmsDataUnit() { return moDbmsDataUnit; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemId = ((int[]) pk)[0];
        mnPkBizPartnerId = ((int[]) pk)[1];
        mnPkConfigId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkItemId, mnPkBizPartnerId, mnPkConfigId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemId = 0;
        mnPkBizPartnerId = 0;
        mnPkConfigId = 0;
        msKey = "";
        msItem = "";
        msItemShort = "";
        mbCfdiUsage = "";
        mbIsItemDescription = false;
        mbIsDeleted = false;
        mnFkUnitId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsItem = "";
        msDbmsBizPartner = "";
        msDbmsUnit = "";
        moDbmsDataUnit = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT cf.*, i.item, b.bp, u.symbol " +
                    "FROM erp.itmu_cfg_item_bp AS cf " +
                    "INNER JOIN erp.itmu_item AS i ON " +
                    "cf.id_item = i.id_item " +
                    "INNER JOIN erp.bpsu_bp AS b ON " +
                    "cf.id_bp = b.id_bp " +
                    "INNER JOIN erp.itmu_unit AS u ON " +
                    "cf.fid_unit = u.id_unit " +
                    "WHERE cf.id_item = " + key[0] + " AND cf.id_bp = " + key[1] + " AND cf.id_cfg = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkItemId = resultSet.getInt("cf.id_item");
                mnPkBizPartnerId = resultSet.getInt("cf.id_bp");
                mnPkConfigId = resultSet.getInt("cf.id_cfg");
                msKey = resultSet.getString("cf.item_key");
                msItem = resultSet.getString("cf.item");
                msItemShort = resultSet.getString("cf.item_short");
                mbCfdiUsage = resultSet.getString("cf.cfd_use");
                mbIsItemDescription = resultSet.getBoolean("cf.b_item_desc");
                mbIsDeleted = resultSet.getBoolean("cf.b_del");
                mnFkUnitId = resultSet.getInt("cf.fid_unit");
                mnFkUserNewId = resultSet.getInt("cf.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("cf.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("cf.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("cf.ts_new");
                mtUserEditTs = resultSet.getTimestamp("cf.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("cf.ts_del");

                msDbmsItem = resultSet.getString("i.item");
                msDbmsBizPartner = resultSet.getString("b.bp");
                msDbmsUnit = resultSet.getString("u.symbol");

                // Read aswell unit object:

                moDbmsDataUnit = new SDataUnit();
                if (moDbmsDataUnit.read(new int[] { mnFkUnitId }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
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
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL erp.itmu_cfg_item_bp_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkItemId);
            callableStatement.setInt(nParam++, mnPkBizPartnerId);
            callableStatement.setInt(nParam++, mnPkConfigId);
            callableStatement.setString(nParam++, msKey);
            callableStatement.setString(nParam++, msItem);
            callableStatement.setString(nParam++, msItemShort);
            callableStatement.setString(nParam++, mbCfdiUsage);
            callableStatement.setBoolean(nParam++, mbIsItemDescription);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkUnitId);
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
