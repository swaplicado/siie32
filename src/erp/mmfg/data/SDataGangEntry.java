/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.util.Date;
import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataGangEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkGangId;
    protected int mnPkEntryId;
    protected boolean mbIsDeleted;
    protected int mnFkBizPartnerId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsBizPartner;
    protected java.lang.String msDbmsBizPartnerKey;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;

    public SDataGangEntry() {
        super(SDataConstants.MFGU_GANG_ETY);
        reset();
    }

    public void setPkGangId(int n) { mnPkGangId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }
    
    public int getPkGangId() { return mnPkGangId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsBizPartner(java.lang.String s) { msDbmsBizPartner = s; }
    public void setDbmsBizPartnerKey(java.lang.String s) { msDbmsBizPartnerKey = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public java.lang.String getDbmsBizPartner() { return msDbmsBizPartner; }
    public java.lang.String getDbmsBizPartnerKey() { return msDbmsBizPartnerKey; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }
    
    @Override
    public void reset() {
        super.resetRegistry();

        mnPkGangId = 0;
        mnPkEntryId = 0;
        mbIsDeleted = false;
        mnFkBizPartnerId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsBizPartner = "";
        msDbmsBizPartnerKey = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object key) {
        mnPkGangId = ((int[]) key)[0];
        mnPkEntryId = ((int[]) key)[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkGangId, mnPkEntryId };
    }
    
    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        
        ResultSet resultSet = null;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT g.*, b.bp, c.bp_key, un.usr, ue.usr, ud.usr " +
                "FROM mfgu_gang_ety AS g " +
                "INNER JOIN erp.bpsu_bp AS b ON g.fid_bp = b.id_bp AND b.b_att_emp = 1 " +
                "INNER JOIN erp.bpsu_bp_ct AS c ON g.fid_bp = c.id_bp " +
                "INNER JOIN erp.usru_usr AS un ON g.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON g.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON g.fid_usr_del = ud.id_usr " +
                "WHERE g.id_gang = " + key[0] + " AND g.id_ety = " + key[1];

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkGangId = resultSet.getInt("g.id_gang");
                mnPkEntryId = resultSet.getInt("g.id_ety");
                mbIsDeleted = resultSet.getBoolean("g.b_del");
                mnFkBizPartnerId = resultSet.getInt("g.fid_bp");
                mnFkUserNewId = resultSet.getInt("g.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("g.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("g.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("g.ts_new");
                mtUserEditTs = resultSet.getTimestamp("g.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("g.ts_del");

                msDbmsBizPartner = resultSet.getString("b.bp");
                msDbmsBizPartnerKey = resultSet.getString("c.bp_key");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int i = 0;
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mfgu_gang_ety_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkGangId);
            callableStatement.setInt(nParam++, mnPkEntryId);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkBizPartnerId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkEntryId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

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
    public int delete(java.sql.Connection connection) {        
        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}
