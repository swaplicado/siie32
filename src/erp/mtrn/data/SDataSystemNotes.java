/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDataSystemNotes extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkNotesId;
    protected java.lang.String msNotes;
    protected java.lang.String msCfdComplementDisposition;
    protected java.lang.String msCfdComplementRule;
    protected boolean mbIsAutomatic;
    protected boolean mbIsPrintable;
    protected boolean mbIsCfdComplement;
    protected boolean mbIsDeleted;
    protected int mnFkDpsCategoryId;
    protected int mnFkDpsClassId;
    protected int mnFkDpsTypeId;
    protected int mnFkCurrencyId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;

    public SDataSystemNotes() {
        super(SDataConstants.TRN_DPS_NTS);
        reset();
    }

    public void setPkNotesId(int n) { mnPkNotesId = n; }
    public void setNotes(java.lang.String s) { msNotes = s; }
    public void setCfdComplementDisposition(java.lang.String s) { msCfdComplementDisposition = s; }
    public void setCfdComplementRule(java.lang.String s) { msCfdComplementRule = s; }
    public void setIsAutomatic(boolean b) { mbIsAutomatic = b; }
    public void setIsPrintable(boolean b) { mbIsPrintable = b; }
    public void setIsCfdComplement(boolean b) { mbIsCfdComplement = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkDpsCategoryId(int n) { mnFkDpsCategoryId = n; }
    public void setFkDpsClassId(int n) { mnFkDpsClassId = n; }
    public void setFkDpsTypeId(int n) { mnFkDpsTypeId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkNotesId() { return mnPkNotesId; }
    public java.lang.String getNotes() { return msNotes; }
    public java.lang.String getCfdComplementDisposition() { return msCfdComplementDisposition; }
    public java.lang.String getCfdComplementRule() { return msCfdComplementRule; }
    public boolean getIsAutomatic() { return mbIsAutomatic; }
    public boolean getIsPrintable() { return mbIsPrintable; }
    public boolean getIsCfdComplement() { return mbIsCfdComplement; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkDpsCategoryId() { return mnFkDpsCategoryId; }
    public int getFkDpsClassId() { return mnFkDpsClassId; }
    public int getFkDpsTypeId() { return mnFkDpsTypeId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
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
    
    public int[] getDpsTypeKey() {
        return new int[] { mnFkDpsCategoryId, mnFkDpsClassId, mnFkDpsTypeId };
    }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkNotesId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkNotesId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkNotesId = 0;
        msNotes = "";
        msCfdComplementDisposition = "";
        msCfdComplementRule = "";
        mbIsAutomatic = false;
        mbIsPrintable = false;
        mbIsCfdComplement = false;
        mbIsDeleted = false;
        mnFkDpsCategoryId = 0;
        mnFkDpsClassId = 0;
        mnFkDpsTypeId = 0;
        mnFkCurrencyId = 0;
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
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT n.*, un.usr, ue.usr, ud.usr " +
                    "FROM trn_sys_nts AS n " +
                    "INNER JOIN erp.usru_usr AS un ON " +
                    "n.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON " +
                    "n.fid_usr_new = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON " +
                    "n.fid_usr_new = ud.id_usr " +
                    "WHERE n.id_nts = " + key[0] +  " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkNotesId = resultSet.getInt("n.id_nts");
                msNotes = resultSet.getString("n.nts");
                msCfdComplementDisposition = resultSet.getString("n.cfd_comp_disp");
                msCfdComplementRule = resultSet.getString("n.cfd_comp_rule");
                mbIsAutomatic = resultSet.getBoolean("n.b_aut");
                mbIsPrintable = resultSet.getBoolean("n.b_prt");
                mbIsCfdComplement = resultSet.getBoolean("b_cfd_comp");
                mbIsDeleted = resultSet.getBoolean("n.b_del");
                mnFkDpsCategoryId = resultSet.getInt("n.fid_ct_dps");
                mnFkDpsClassId = resultSet.getInt("n.fid_cl_dps");
                mnFkDpsTypeId = resultSet.getInt("n.fid_tp_dps");
                mnFkCurrencyId = resultSet.getInt("n.fid_cur");
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
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL trn_sys_nts_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkNotesId);
            callableStatement.setString(nParam++, msNotes);
            callableStatement.setString(nParam++, msCfdComplementDisposition);
            callableStatement.setString(nParam++, msCfdComplementRule);
            callableStatement.setBoolean(nParam++, mbIsAutomatic);
            callableStatement.setBoolean(nParam++, mbIsPrintable);
            callableStatement.setBoolean(nParam++, mbIsCfdComplement);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkDpsCategoryId);
            callableStatement.setInt(nParam++, mnFkDpsClassId);
            callableStatement.setInt(nParam++, mnFkDpsTypeId);
            callableStatement.setInt(nParam++, mnFkCurrencyId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, Types.VARCHAR);
            callableStatement.execute();

            mnPkNotesId = callableStatement.getInt(nParam - 3);
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
