/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.util.Date;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataBomNotes extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    protected int mnPkBomId;
    protected int mnPkNotesId;
    protected java.lang.String msNotes;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;

    public SDataBomNotes() {
        super(SDataConstants.MFG_BOM_NTS);
        reset();
    }
    
    public void setPkBomId(int n) { mnPkBomId = n; }
    public void setPkNotesId(int n) { mnPkNotesId = n; }
    public void setNotes(java.lang.String s) { msNotes = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkBomId() { return mnPkBomId; }
    public int getPkNotesId() { return mnPkNotesId; }
    public java.lang.String getNotes() { return msNotes; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkBomId = 0;
        mnPkNotesId = 0;
        msNotes = "";
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBomId = ((int[]) pk)[0];
        mnPkNotesId = ((int[]) pk)[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBomId, mnPkNotesId };
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT b.*, un.usr, ue.usr, ud.usr " +
                    "FROM mfg_bom_nts AS b " +
                        "INNER JOIN erp.usru_usr AS un ON " +
                            "b.fid_usr_new = un.id_usr " +
                        "INNER JOIN erp.usru_usr AS ue ON " +
                            "b.fid_usr_edit = ue.id_usr " +
                        "INNER JOIN erp.usru_usr AS ud ON " +
                            "b.fid_usr_del = ud.id_usr " +
                    "WHERE id_bom = " + key[0] + " " +
                        "AND id_nts = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBomId = resultSet.getInt("b.id_bom");
                mnPkNotesId = resultSet.getInt("b.id_nts");
                msNotes = resultSet.getString("b.nts");
                mbIsDeleted = resultSet.getBoolean("b.b_del");
                mnFkUserNewId = resultSet.getInt("b.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("b.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("b.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("b.ts_new");
                mtUserEditTs = resultSet.getTimestamp("b.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("b.ts_del");

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
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL mfg_bom_nts_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?) }");
            callableStatement.setInt(nParam++, mnPkBomId);
            callableStatement.setInt(nParam++, mnPkNotesId);
            callableStatement.setString(nParam++, msNotes);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

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
        return null;
    }
}
