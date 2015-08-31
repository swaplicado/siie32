/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;

import erp.data.SDataConstantsSys;
import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataRequisition extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkReqId;
    protected java.util.Date mtDate;
    protected boolean mbIsDeleted;
    protected int mnFkBizPartnerId;
    protected int mnFkStatusId;
    protected int mnFkTypeId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsStatus;
    protected java.lang.String msDbmsType;
    protected int mnDbmsAuxSortingItem;

    protected java.util.Vector<erp.mmfg.data.SDataRequisitionEntry> mvDbmsRequisitionEntry;

    protected erp.mmfg.data.SDataExplotionMaterials moDbmsExplotionMaterials;

    public SDataRequisition() {
        super(SDataConstants.MFG_REQ);
        mvDbmsRequisitionEntry = new Vector<SDataRequisitionEntry>();

        reset();      
    }
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkReqId(int n) { mnPkReqId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    public void setFkStatusId(int n) { mnFkStatusId = n; }
    public void setFkTypeId(int n) { mnFkTypeId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }
        
    public int getPkYearId() { return mnPkYearId; }
    public int getPkReqId() { return mnPkReqId; }
    public java.util.Date getDate() { return mtDate; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    public int getFkStatusId() { return mnFkStatusId; }
    public int getFkTypeId() { return mnFkTypeId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId(int n) { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsStatus(java.lang.String s) { msDbmsStatus = s; }
    public void setDbmsType(java.lang.String s) { msDbmsType = s; }
    public void setIsNewRegistry(boolean b) { mbIsRegistryNew = b; }
    public void setDbmsRequisitionEntry(java.util.Vector<SDataRequisitionEntry> v) { mvDbmsRequisitionEntry = v; }
    public void setDbmsExplotionMaterials(erp.mmfg.data.SDataExplotionMaterials o) { moDbmsExplotionMaterials = o; }
    public void setDbmsAuxSortingItem(int n) { mnDbmsAuxSortingItem = n; }

    public java.lang.String getDbmsStatus() { return msDbmsStatus; }
    public java.lang.String getDbbmsType() { return msDbmsType; }
    public java.util.Vector<SDataRequisitionEntry> getDbmsRequisitionEntry() { return mvDbmsRequisitionEntry; }
    public erp.mmfg.data.SDataExplotionMaterials getDbmsExplotionMaterials() { return moDbmsExplotionMaterials; }
    public int getDbmsAuxSortingItem() { return mnDbmsAuxSortingItem; }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkReqId = 0;
        mtDate = null;
        mbIsDeleted = false;
        mnFkBizPartnerId = 0;
        mnFkStatusId = 0;
        mnFkTypeId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsStatus = "";
        msDbmsType = "";

        mvDbmsRequisitionEntry.clear();
        moDbmsExplotionMaterials = null;
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object key) {
        mnPkYearId = ((int[]) key)[0];
        mnPkReqId = ((int[]) key)[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkReqId };
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";

        SDataRequisitionEntry oRequisitionEntry = null;
        ResultSet resultSet = null;
        Statement statementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT r.*, s.st, t.tp, e.id_exp_year, e.id_exp " +
                "FROM mfg_req AS r " +
                "INNER JOIN mfg_exp_req AS e ON r.id_year = e.id_req_year AND r.id_req = e.id_req " +
                "INNER JOIN erp.mfgs_st_ord AS s ON r.fid_st_ord = s.id_st " +
                "INNER JOIN erp.mfgu_tp_ord AS t ON r.fid_tp_req = t.id_tp " +
                "INNER JOIN erp.usru_usr AS un ON r.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON r.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON r.fid_usr_del = ud.id_usr " +
                "WHERE r.id_year = " + key[0] + " AND r.id_req = " + key[1];

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("r.id_year");
                mnPkReqId = resultSet.getInt("r.id_req");
                mtDate = resultSet.getDate("r.ts");
                mbIsDeleted = resultSet.getBoolean("r.b_del");
                mnFkBizPartnerId = resultSet.getInt("fid_bp");
                mnFkStatusId = resultSet.getInt("r.fid_st_ord");
                mnFkTypeId = resultSet.getInt("r.fid_tp_req");
                mnFkUserNewId = resultSet.getInt("r.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("r.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("r.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("r.ts_new");
                mtUserEditTs = resultSet.getTimestamp("r.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("r.ts_del");

                msDbmsStatus = resultSet.getString("s.st");
                msDbmsType = resultSet.getString("t.tp");

                // Read explotion of materials object:

                statementAux = statement.getConnection().createStatement();
                moDbmsExplotionMaterials = new SDataExplotionMaterials();
                moDbmsExplotionMaterials.read(new int[] { resultSet.getInt("e.id_exp_year"), resultSet.getInt("e.id_exp") }, statementAux);

                // Read entries:

                mvDbmsRequisitionEntry.removeAllElements();
                sql = "SELECT r.*, i.item_key, i.item " +
                    "FROM mfg_req_ety AS r " +
                    "INNER JOIN erp.itmu_item AS i ON r.fid_item = i.id_item " +
                    "WHERE r.id_req_year = " + key[0] + " AND r.id_req = " + key[1] + " " +
                    (mnDbmsAuxSortingItem == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "ORDER BY i.item_key, i.item " : "ORDER BY i.item, i.item_key ");
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    oRequisitionEntry = new SDataRequisitionEntry();
                    if (oRequisitionEntry.read(new int[] { resultSet.getInt("id_req_year"), resultSet.getInt("id_req"), resultSet.getInt("id_ety") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsRequisitionEntry.add(oRequisitionEntry);
                    }
                }

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
        String sSql = "";

        SDataRequisitionEntry oRequisitionEntry = null;
        
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        Statement statement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {            
            callableStatement = connection.prepareCall(
                    "{ CALL mfg_req_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkReqId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDate.getTime()));
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkBizPartnerId);
            callableStatement.setInt(nParam++, mnFkStatusId);
            callableStatement.setInt(nParam++, mnFkTypeId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();
            mnPkYearId = callableStatement.getInt(nParam - 4);
            mnPkReqId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);
            
            if (mnDbmsErrorId != 0) {
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            }
            else {               
                // Save entries (only update quantity):

                for (i = 0; i < mvDbmsRequisitionEntry.size(); i++) {
                    oRequisitionEntry = (SDataRequisitionEntry) mvDbmsRequisitionEntry.get(i);
                    if (oRequisitionEntry != null) {
                        oRequisitionEntry.setPkReqYearId(mnPkYearId);
                        oRequisitionEntry.setPkReqId(mnPkReqId);
                        oRequisitionEntry.setPkEntryId(oRequisitionEntry.getPkEntryId() > 0 ? oRequisitionEntry.getPkEntryId() : 0);
                        if (oRequisitionEntry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                        else {
                            mvDbmsRequisitionEntry.set(i, oRequisitionEntry);
                        }
                    }
                }

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
    public int delete(java.sql.Connection connection) {
        String sSql = "";

        Statement statement = null;

        try {
            sSql = "DELETE FROM mfg_req WHERE id_year = " + mnPkYearId + " AND id_req = " + mnPkReqId + " ";
            statement = connection.createStatement();
            statement.executeUpdate(sSql);

            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}
