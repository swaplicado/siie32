/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mcfg.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;

/**
 *
 * @author Sergio Flores
 */
public class SDataSysEntityCategory extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkCategoryId;
    protected java.lang.String msCategory;
    protected boolean mbIsDeleted;
    protected int mnFkUserId;
    protected java.util.Date mtUserTs;

    public SDataSysEntityCategory() {
        super(SDataConstants.CFGS_CT_ENT);
        reset();
    }

    public void setPkCategoryId(int n) { mnPkCategoryId = n; }
    public void setCategory(java.lang.String s) { msCategory = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setUserTs(java.util.Date t) { mtUserTs = t; }

    public int getPkCategoryId() { return mnPkCategoryId; }
    public java.lang.String getCategory() { return msCategory; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserId() { return mnFkUserId; }
    public java.util.Date getUserTs() { return mtUserTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkCategoryId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkCategoryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkCategoryId = 0;
        msCategory = "";
        mbIsDeleted = false;
        mnFkUserId = 0;
        mtUserTs = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.cfgs_ct_ent WHERE id_ct_ent = " + key[0]  + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkCategoryId = resultSet.getInt("id_ct_ent");
                msCategory = resultSet.getString("ct_ent");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserId = resultSet.getInt("fid_usr");
                mtUserTs = resultSet.getTimestamp("ts");

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserTs;
    }
}
