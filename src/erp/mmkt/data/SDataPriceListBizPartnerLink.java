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
 * @author Néstor Ávalos, Claudio Peña
 */
public class SDataPriceListBizPartnerLink extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkLinkId;
    protected int mnPkReference1Id;
    protected int mnPkReference2Id;
    protected java.util.Date mtPkDateStartId;
    protected double mdDiscountPercentage;
    protected boolean mbIsDeleted;
    protected int mnFkPriceListId;
    protected int mnFkDiscountApplicationTypeId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataPriceListBizPartnerLink() {
        super(SDataConstants.MKT_PLIST_BP_LINK);
        reset();
    }

    public void setPkLinkId(int n) { mnPkLinkId = n; }
    public void setPkReference1Id(int n) { mnPkReference1Id = n; }
    public void setPkReference2Id(int n) { mnPkReference2Id = n; }
    public void setPkDateStartId(java.util.Date t) { mtPkDateStartId = t; }
    public void setDiscountPercentage(double d) { mdDiscountPercentage = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkPriceListId(int n) { mnFkPriceListId = n; }
    public void setFkDiscountApplicationTypeId(int n) { mnFkDiscountApplicationTypeId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkLinkId() { return mnPkLinkId; }
    public int getPkReference1Id() { return mnPkReference1Id; }
    public int getPkReference2Id() { return mnPkReference2Id; }
    public java.util.Date getPkDateStartId() { return mtPkDateStartId; }
    public double getDiscountPercentage() { return mdDiscountPercentage; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkPriceListId() { return mnFkPriceListId; }
    public int getFkDiscountApplicationTypeId() { return mnFkDiscountApplicationTypeId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkLinkId = (int)((Object[]) pk)[0];
        mnPkReference1Id = (int)((Object[]) pk)[1];
        mnPkReference2Id = (int)((Object[]) pk)[2];
        mtPkDateStartId = (Date)((Object[]) pk)[3];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new java.lang.Object[] { mnPkLinkId, mnPkReference1Id, mnPkReference2Id, mtPkDateStartId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkLinkId = 0;
        mnPkReference1Id = 0;
        mnPkReference2Id = 0;
        mtPkDateStartId = null;
        mdDiscountPercentage = 0;
        mbIsDeleted = false;
        mnFkPriceListId = 0;
        mnFkDiscountApplicationTypeId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        Object[] key = (Object[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM mkt_plist_bp_link " +
                    "WHERE id_link = " + ((Integer) key[0]) + " AND id_ref_1 = " + ((Integer) key[1]) +  " AND " +
                    "id_ref_2 = " + ((Integer) key[2]) + " AND id_dt_start = '" + key[3] + "' ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkLinkId = resultSet.getInt("id_link");
                mnPkReference1Id = resultSet.getInt("id_ref_1");
                mnPkReference2Id = resultSet.getInt("id_ref_2");
                mtPkDateStartId = resultSet.getDate("id_dt_start");
                mdDiscountPercentage = resultSet.getDouble("disc_per");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkPriceListId = resultSet.getInt("fid_plist");
                mnFkDiscountApplicationTypeId = resultSet.getInt("fid_tp_disc_app");
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
                    "{ CALL mkt_plist_bp_link_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?) }");
            callableStatement.setInt(nParam++, mnPkLinkId);
            callableStatement.setInt(nParam++, mnPkReference1Id);
            callableStatement.setInt(nParam++, mnPkReference2Id);
            callableStatement.setDate(nParam++, new java.sql.Date(mtPkDateStartId.getTime()));
            callableStatement.setDouble(nParam++, mdDiscountPercentage);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkPriceListId);
            callableStatement.setInt(nParam++, mnFkDiscountApplicationTypeId);
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
