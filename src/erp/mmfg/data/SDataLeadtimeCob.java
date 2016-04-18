/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.util.Date;
import java.util.Vector;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataLeadtimeCob extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkCobId;
    protected int mnPkSupplierId;
    protected int mnPkEntryId;
    protected int mnLtime;
    protected boolean mbIsDeleted;
    protected int mnFkLinkTypeId;
    protected int mnFkReferenceId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;
    protected java.lang.String msDbmsTypeLink;
    protected java.lang.String msDbmsRef;

    protected java.util.Vector<erp.mmfg.data.SDataLeadtimeCob> mvDbmsTypes;

    protected int mnDbmsAuxSortingItem;

    public SDataLeadtimeCob() {
        super(SDataConstants.MFG_LT_COB);

        mvDbmsTypes = new Vector<SDataLeadtimeCob>();
        reset();
    }
    
    public void setPkCobId(int n) { mnPkCobId = n; }
    public void setPkSupplierId(int n) { mnPkSupplierId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setLtime(int n) { mnLtime = n; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkLinkTypeId(int n) { mnFkLinkTypeId = n; }
    public void setFkReferenceId(int n) { mnFkReferenceId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkCobId() { return mnPkCobId; }
    public int getPkSupplierId() { return mnPkSupplierId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getLtime() { return mnLtime; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkLinkTypeId() { return mnFkLinkTypeId; }
    public int getFkReferenceId() { return mnFkReferenceId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsTypeLink(java.lang.String s) { msDbmsTypeLink = s; }
    public void setDbmsReference(java.lang.String s) { msDbmsRef = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }
    public void setDbmsAuxSortingItem(int n) { mnDbmsAuxSortingItem = n; }

    public java.lang.String getDbmsTypeLink() { return msDbmsTypeLink; }
    public java.lang.String getDbmsRef() { return msDbmsRef; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }
    public int getDbmsAuxSortingItem() { return mnDbmsAuxSortingItem; }
    
    public java.util.Vector<SDataLeadtimeCob> getDbmsTypes() { return mvDbmsTypes; }

    private java.lang.String query(java.lang.Object params) {
        int[] key = (int[]) params;
        String sql = "";

        sql = "SELECT t.id_cob, t.id_sup, t.id_ety, t.ltime, t.b_del, b.bp, bpb.bpb, " +
            "CASE t.fid_tp_link " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ALL + " THEN " +
            "('" + SDataConstantsSys.TXT_TRNS_TP_LINK_ALL + "') " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CT_ITEM + " THEN (" +
            "SELECT c.ct_item FROM erp.itms_ct_item c WHERE c.ct_idx = t.fid_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CL_ITEM + " THEN (" +
            "SELECT c.cl_item FROM erp.itms_cl_item c WHERE c.cl_idx = t.fid_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_TP_ITEM + " THEN (" +
            "SELECT tp.tp_item FROM erp.itms_tp_item tp WHERE tp.tp_idx = t.fid_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IFAM + " THEN (" +
            "SELECT ifam.ifam FROM erp.itmu_ifam ifam WHERE ifam.id_ifam = t.fid_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGRP + " THEN (" +
            "SELECT igrp.igrp FROM erp.itmu_igrp igrp WHERE igrp.id_igrp = t.fid_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGEN + " THEN (" +
            "SELECT CONCAT(igen.igen, ' (', igen.code, ')') FROM erp.itmu_igen igen WHERE igen.id_igen = t.fid_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_LINE + " THEN (" +
            "SELECT line.line FROM erp.itmu_line line WHERE line.id_line = t.fid_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_BRD + " THEN (" +
            "SELECT CONCAT(brd.brd, ' (', brd.code, ')') FROM erp.itmu_brd brd WHERE brd.id_brd = t.fid_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_MFR + " THEN (" +
            "SELECT CONCAT(mfr.mfr, ' (', mfr.code, ')') FROM erp.itmu_mfr mfr WHERE mfr.id_mfr = t.fid_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ITEM + " THEN (" +
            "SELECT " + (mnDbmsAuxSortingItem == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "CONCAT(item.item_key, ' - ', item.item)" : "CONCAT(item.item, ' - ', item.item_key)") + " " +
            "FROM erp.itmu_item item WHERE item.id_item = t.fid_ref) " +
            "ELSE 'No existe tipo' " +
            "END AS ref, link.tp_link, t.fid_tp_link, t.fid_ref, t.fid_usr_new, t.fid_usr_edit, t.fid_usr_del, un.usr, t.ts_new, ue.usr, t.ts_edit, ud.usr, t.ts_del " +
            "FROM trn_sup_lt_cob t " +
            "INNER JOIN erp.bpsu_bp b ON t.id_sup = b.id_bp " +
            "INNER JOIN erp.bpsu_bpb bpb ON t.id_cob = bpb.id_bpb " +
            "INNER JOIN erp.trns_tp_link link ON t.fid_tp_link = link.id_tp_link " +
            "INNER JOIN erp.usru_usr AS un ON t.fid_usr_new = un.id_usr " +
            "INNER JOIN erp.usru_usr AS ue ON t.fid_usr_edit = ue.id_usr " +
            "INNER JOIN erp.usru_usr AS ud ON t.fid_usr_del = ud.id_usr " +
            "WHERE t.id_cob = " + key[0] + " AND t.id_sup = " + key[1] +
            (key[2] == 0 ? " " : " AND t.id_ety = " + key[2] + " ");

        return sql;
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkCobId = 0;
        mnPkSupplierId = 0;
        mnPkEntryId = 0;
        mnLtime = 0;
        mbIsDeleted = false;
        mnFkLinkTypeId = 0;
        mnFkReferenceId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsTypeLink = "";
        msDbmsRef = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";

        mvDbmsTypes.clear();
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object key) {
        mnPkCobId = ((int[]) key)[0];
        mnPkSupplierId = ((int[]) key)[1];
        mnPkEntryId = ((int[]) key)[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCobId, mnPkSupplierId, mnPkEntryId };
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        java.sql.ResultSet resultSet = null;
        java.sql.Statement statementAux = null;
        SDataLeadtimeCob type = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = query(pk);
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkCobId = resultSet.getInt("id_cob");
                mnPkSupplierId = resultSet.getInt("id_sup");
                mnPkEntryId = resultSet.getInt("id_ety");
                mnLtime = resultSet.getInt("ltime");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkLinkTypeId = resultSet.getInt("fid_tp_link");
                mnFkReferenceId = resultSet.getInt("fid_ref");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                msDbmsTypeLink = resultSet.getString("tp_link");
                msDbmsRef = resultSet.getString("ref");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");
                
                // Read link types:
                statementAux = statement.getConnection().createStatement();
                mvDbmsTypes.removeAllElements();
                sql = query (new int[] { key[0], key[1], 0 });
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    type = new SDataLeadtimeCob();

                    type.setPkCobId(resultSet.getInt("id_cob"));
                    type.setPkSupplierId(resultSet.getInt("id_sup"));
                    type.setPkEntryId(resultSet.getInt("id_ety"));
                    type.setLtime(resultSet.getInt("ltime"));
                    type.setIsDeleted(resultSet.getBoolean("b_del"));
                    type.setFkLinkTypeId(resultSet.getInt("fid_tp_link"));
                    type.setFkReferenceId(resultSet.getInt("fid_ref"));
                    type.setFkUserNewId(resultSet.getInt("fid_usr_new"));
                    type.setFkUserEditId(resultSet.getInt("fid_usr_edit"));
                    type.setFkUserDeleteId(resultSet.getInt("fid_usr_del"));
                    type.setUserNewTs(resultSet.getTimestamp("ts_new"));
                    type.setUserEditTs(resultSet.getTimestamp("ts_edit"));
                    type.setUserDeleteTs(resultSet.getTimestamp("ts_del"));

                    type.setDbmsTypeLink(resultSet.getString("tp_link"));
                    type.setDbmsReference(resultSet.getString("ref"));
                    type.setDbmsUserNew(resultSet.getString("un.usr"));
                    type.setDbmsUserEdit(resultSet.getString("ue.usr"));
                    type.setDbmsUserDelete(resultSet.getString("ud.usr"));

                    mvDbmsTypes.add(type);
                }

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
        java.sql.CallableStatement callableStatement;
        SDataLeadtimeCob type = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {

            // Save link type:
            for (int i = 0; i < mvDbmsTypes.size(); i++) {
                type = (SDataLeadtimeCob) mvDbmsTypes.get(i);
                nParam = 1;
                if (type != null) {

                    callableStatement = connection.prepareCall("{" +
                    " CALL mfg_ltime_cob_save(" +
                        "?, ?, ?, ?, ?, " +
                        "?, ?, ?, ?, ?, " +
                        "?) }");
                    callableStatement.setInt(nParam++, type.getPkCobId());
                    callableStatement.setInt(nParam++, type.getPkSupplierId());
                    callableStatement.setInt(nParam++, type.getPkEntryId());
                    callableStatement.setInt(nParam++, type.getLtime());
                    callableStatement.setBoolean(nParam++, type.getIsDeleted());
                    callableStatement.setInt(nParam++, type.getFkLinkTypeId());
                    callableStatement.setInt(nParam++, type.getFkReferenceId());
                    callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
                    callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                    callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                    callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
                    callableStatement.execute();
                }
            }

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
        return mtUserEditTs;
    }
}
