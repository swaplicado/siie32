/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.util.Date;
import java.util.Vector;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataProductionOrderNotes extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkOrdYearId;
    protected int mnPkOrdId;
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

    public SDataProductionOrderNotes (){
        super(SDataConstants.MFG_ORD_NTS);
        
        reset();
    }

    public void setPkOrdYearId(int n) { mnPkOrdYearId = n; }
    public void setPkOrdId(int n) { mnPkOrdId = n; }
    public void setPkNotesId(int n) { mnPkNotesId = n; }
    public void setNotes(java.lang.String s) { msNotes = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkOrdYearId() { return mnPkOrdYearId; }
    public int getPkOrdId() { return mnPkOrdId; }
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
        
        mnPkOrdYearId = 0;
        mnPkOrdId = 0;
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
    public void setPrimaryKey(Object key) {
        mnPkOrdYearId = ((int[]) key)[0];
        mnPkOrdId = ((int[]) key)[1];
        mnPkNotesId = ((int[]) key)[2];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkOrdYearId, mnPkOrdId, mnPkNotesId };
    }

    @Override
    public int read(Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT n.*, un.usr, ue.usr, ud.usr " +
                "FROM mfg_ord_nts AS n " +
                "INNER JOIN erp.usru_usr AS un ON n.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON n.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON n.fid_usr_del = ud.id_usr " +
                "WHERE n.id_ord_year = " + key[0] + " AND n.id_ord = " + key[1] + " AND n.id_nts = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkOrdYearId = resultSet.getInt("n.id_ord_year");
                mnPkOrdId = resultSet.getInt("n.id_ord");
                mnPkNotesId = resultSet.getInt("n.id_nts");
                msNotes = resultSet.getString("n.nts");
                mbIsDeleted = resultSet.getBoolean("n.b_del");
                mnFkUserNewId = resultSet.getInt("n.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("n.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("n.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("n.ts_new");
                mtUserEditTs = resultSet.getTimestamp("n.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("n.ts_del");

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
            callableStatement = connection.prepareCall("{ CALL mfg_ord_nts_save(" +
                    "?, ?, ?, ?, ?," +
                    "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkOrdYearId);
            callableStatement.setInt(nParam++, mnPkOrdId);
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
