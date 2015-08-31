/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Sergio Flores
 */
public class SDataTaxGroupItem extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkItemId;
    protected int mnPkTaxRegionId;
    protected java.util.Date mtPkDateStartId;
    protected boolean mbIsDeleted;
    protected int mnFkTaxGroupId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsItem;
    protected java.lang.String msDbmsTaxRegion;
    protected java.lang.String msDbmsTaxGroup;

    public SDataTaxGroupItem() {
        super(SDataConstants.FIN_TAX_GRP_ITEM);
        reset();
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkTaxRegionId(int n) { mnPkTaxRegionId = n; }
    public void setPkDateStartId(java.util.Date t) { mtPkDateStartId = t; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkTaxGroupId(int n) { mnFkTaxGroupId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkTaxRegionId() { return mnPkTaxRegionId; }
    public java.util.Date getPkDateStartId() { return mtPkDateStartId; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkTaxGroupId() { return mnFkTaxGroupId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsItem(java.lang.String s) { msDbmsItem = s; }
    public void setDbmsTaxRegion(java.lang.String s) { msDbmsTaxRegion = s; }
    public void setDbmsTaxGroup(java.lang.String s) { msDbmsTaxGroup = s; }

    public java.lang.String getDbmsItem() { return msDbmsItem; }
    public java.lang.String getDbmsTaxRegion() { return msDbmsTaxRegion; }
    public java.lang.String getDbmsTaxGroup() { return msDbmsTaxGroup; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemId = (Integer) ((Object[]) pk)[0];
        mnPkTaxRegionId = (Integer) ((Object[]) pk)[1];
        mtPkDateStartId = (java.util.Date) ((Object[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new Object[] { mnPkItemId, mnPkTaxRegionId, mtPkDateStartId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemId = 0;
        mnPkTaxRegionId = 0;
        mtPkDateStartId = null;
        mbIsDeleted = false;
        mnFkTaxGroupId = 0;
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
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT ti.*, i.item, tr.tax_reg, tg.tax_grp " +
                    "FROM fin_tax_grp_item AS ti " +
                    "INNER JOIN erp.itmu_item AS i ON " +
                    "ti.id_item = i.id_item " +
                    "INNER JOIN erp.finu_tax_reg AS tr ON " +
                    "ti.id_tax_reg = tr.id_tax_reg " +
                    "INNER JOIN fin_tax_grp AS tg ON " +
                    "ti.fid_tax_grp = tg.id_tax_grp " +
                    "WHERE ti.id_item = " + key[0] + " AND ti.id_tax_reg = " + key[1] + " AND ti.id_dt_start = '" + key[2] + "' ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkItemId = resultSet.getInt("ti.id_item");
                mnPkTaxRegionId = resultSet.getInt("ti.id_tax_reg");
                mtPkDateStartId = resultSet.getDate("ti.id_dt_start");
                mbIsDeleted = resultSet.getBoolean("ti.b_del");
                mnFkTaxGroupId = resultSet.getInt("ti.fid_tax_grp");
                mnFkUserNewId = resultSet.getInt("ti.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("ti.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("ti.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ti.ts_new");
                mtUserEditTs = resultSet.getTimestamp("ti.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ti.ts_del");

                msDbmsItem  = resultSet.getString("i.item");
                msDbmsTaxRegion  = resultSet.getString("tr.tax_reg");
                msDbmsTaxGroup  = resultSet.getString("tg.tax_grp");

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
                    "{ CALL fin_tax_grp_item_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkItemId);
            callableStatement.setInt(nParam++, mnPkTaxRegionId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtPkDateStartId.getTime()));
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkTaxGroupId);
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
