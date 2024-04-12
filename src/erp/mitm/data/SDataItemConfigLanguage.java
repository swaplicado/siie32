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
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataItemConfigLanguage extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkItemId;
    protected int mnPkLanguageId;
    protected java.lang.String msItem;
    protected java.lang.String msItemShort;
    protected boolean mbIsDeleted;

    protected java.lang.String msDbmsLanguage;

    public SDataItemConfigLanguage() {
        super(SDataConstants.ITMU_CFG_ITEM_LAN);
        reset();
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkLanguageId(int n) { mnPkLanguageId = n; }
    public void setItem(java.lang.String s) { msItem = s; }
    public void setItemShort(java.lang.String s) { msItemShort = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkLanguageId() { return mnPkLanguageId; }
    public java.lang.String getItem() { return msItem; }
    public java.lang.String getItemShort() { return msItemShort; }
    public boolean getIsDeleted() { return mbIsDeleted; }

    public void setDbmsLanguage(java.lang.String s) { msDbmsLanguage = s; }
    public java.lang.String getDbmsLanguage() { return msDbmsLanguage; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemId = ((int[]) pk)[0];
        mnPkLanguageId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkItemId, mnPkLanguageId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemId = 0;
        mnPkLanguageId = 0;
        msItem = "";
        msItemShort = "";
        mbIsDeleted = false;

        msDbmsLanguage = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT i.*, l.lan " +
                    "FROM erp.itmu_cfg_item_lan AS i " +
                    "INNER JOIN erp.cfgu_lan AS l ON " +
                    "i.id_lan = l.id_lan " +
                    "WHERE i.id_item = " + key[0] + " AND i.id_lan = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkItemId = resultSet.getInt("i.id_item");
                mnPkLanguageId = resultSet.getInt("i.id_lan");
                msItem = resultSet.getString("i.item");
                msItemShort = resultSet.getString("i.item_short");
                mbIsDeleted = resultSet.getBoolean("i.b_del");

                msDbmsLanguage = resultSet.getString("l.lan");

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
                    "{ CALL erp.itmu_cfg_item_lan_save(" +
                    "?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkItemId);
            callableStatement.setInt(nParam++, mnPkLanguageId);
            callableStatement.setString(nParam++, msItem);
            callableStatement.setString(nParam++, msItemShort);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
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
        return null;
    }
}
