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
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataTaxGroupItemGeneric extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkItemGenericId;
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

    protected java.lang.String msDbmsItemGeneric;
    protected java.lang.String msDbmsTaxRegion;
    protected java.lang.String msDbmsTaxGroup;

    public SDataTaxGroupItemGeneric() {
        super(SDataConstants.FIN_TAX_GRP_IGEN);
        reset();
    }

    public void setPkItemGenericId(int n) { mnPkItemGenericId = n; }
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

    public int getPkItemGenericId() { return mnPkItemGenericId; }
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

    public void setDbmsItemGeneric(java.lang.String s) { msDbmsItemGeneric = s; }
    public void setDbmsTaxRegion(java.lang.String s) { msDbmsTaxRegion = s; }
    public void setDbmsTaxGroup(java.lang.String s) { msDbmsTaxGroup = s; }

    public java.lang.String getDbmsItemGeneric() { return msDbmsItemGeneric; }
    public java.lang.String getDbmsTaxRegion() { return msDbmsTaxRegion; }
    public java.lang.String getDbmsTaxGroup() { return msDbmsTaxGroup; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemGenericId = (Integer) ((Object[]) pk)[0];
        mnPkTaxRegionId = (Integer) ((Object[]) pk)[1];
        mtPkDateStartId = (java.util.Date) ((Object[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new Object[] { mnPkItemGenericId, mnPkTaxRegionId, mtPkDateStartId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemGenericId = 0;
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
            sql = "SELECT tig.*, ig.igen, tr.tax_reg, tg.tax_grp " +
                    "FROM fin_tax_grp_igen AS tig " +
                    "INNER JOIN erp.itmu_igen AS ig ON " +
                    "tig.id_igen = ig.id_igen " +
                    "INNER JOIN erp.finu_tax_reg AS tr ON " +
                    "tig.id_tax_reg = tr.id_tax_reg " +
                    "INNER JOIN fin_tax_grp AS tg ON " +
                    "tig.fid_tax_grp = tg.id_tax_grp " +
                    "WHERE tig.id_igen = " + key[0] + " AND tig.id_tax_reg = " + key[1] + " AND tig.id_dt_start = '" + key[2] + "' ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkItemGenericId = resultSet.getInt("tig.id_igen");
                mnPkTaxRegionId = resultSet.getInt("tig.id_tax_reg");
                mtPkDateStartId = resultSet.getDate("tig.id_dt_start");
                mbIsDeleted = resultSet.getBoolean("tig.b_del");
                mnFkTaxGroupId = resultSet.getInt("tig.fid_tax_grp");
                mnFkUserNewId = resultSet.getInt("tig.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("tig.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("tig.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("tig.ts_new");
                mtUserEditTs = resultSet.getTimestamp("tig.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("tig.ts_del");

                msDbmsItemGeneric  = resultSet.getString("ig.igen");
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
                    "{ CALL fin_tax_grp_igen_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkItemGenericId);
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
