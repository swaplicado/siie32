/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataManufacturingLineConfigItem extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkLinkTypeId;
    protected int mnPkReferenceId;
    protected int mnPkMfgLineId;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected String msDbmsLine;

    public SDataManufacturingLineConfigItem() {
        super(SDataConstants.MFGU_LINE_CFG_ITEM);
        reset();
    }

    public void setPkLinkTypeId(int n) { mnPkLinkTypeId = n; }
    public void setPkReferenceId(int n) { mnPkReferenceId = n; }
    public void setPkMfgLineId(int n) { mnPkMfgLineId = n; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkLinkTypeId() { return mnPkLinkTypeId; }
    public int getPkReferenceId() { return mnPkReferenceId; }
    public int getPkMfgLineId() { return mnPkMfgLineId; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsLine(String s) { msDbmsLine = s; }

    public String getDbmsLine() { return msDbmsLine; }

    private java.lang.String query(java.lang.Object params){
        int[] key = (int[]) params;
        String sql = "";

        sql = "SELECT t.*, l.line, " +
            "CASE t.id_tp_link " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ALL + " THEN " +
                "('" + SDataConstantsSys.TXT_TRNS_TP_LINK_ALL + "') " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CT_ITEM + " THEN (" +
                "SELECT c.ct_item FROM erp.itms_ct_item c WHERE c.ct_idx = t.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CL_ITEM + " THEN (" +
                "SELECT c.cl_item FROM erp.itms_cl_item c WHERE c.cl_idx = t.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_TP_ITEM + " THEN (" +
                "SELECT tp.tp_item FROM erp.itms_tp_item tp WHERE tp.tp_idx = t.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IFAM + " THEN (" +
                "SELECT ifam.ifam FROM erp.itmu_ifam ifam WHERE ifam.id_ifam = t.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGRP + " THEN (" +
                "SELECT igrp.igrp FROM erp.itmu_igrp igrp WHERE igrp.id_igrp = t.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGEN + " THEN (" +
                "SELECT CONCAT(igen.igen, ' (', igen.code, ')') FROM erp.itmu_igen igen WHERE igen.id_igen = t.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_LINE + " THEN (" +
                "SELECT line.line FROM erp.itmu_line line WHERE line.id_line = t.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_BRD + " THEN (" +
                "SELECT CONCAT(brd.brd, ' (', brd.code, ')') FROM erp.itmu_brd brd WHERE brd.id_brd = t.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_MFR + " THEN (" +
                "SELECT CONCAT(mfr.mfr, ' (', mfr.code, ')') FROM erp.itmu_mfr mfr WHERE mfr.id_mfr = t.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ITEM + " THEN (" +
                "SELECT CONCAT(item.item_key, ' - ', item.item) " +
                "FROM erp.itmu_item item WHERE item.id_item = t.id_ref) " +
            "ELSE 'No existe tipo' " +
            "END AS ref, link.tp_link " +
            "FROM mfgu_line_cfg_item t " +
            "INNER JOIN erp.trns_tp_link link ON t.id_tp_link = link.id_tp_link " +
            "INNER JOIN mfgu_line AS l ON t.id_line = l.id_line " +
            "INNER JOIN erp.usru_usr AS un ON t.fid_usr_new = un.id_usr " +
            "INNER JOIN erp.usru_usr AS ue ON t.fid_usr_edit = ue.id_usr " +
            "INNER JOIN erp.usru_usr AS ud ON t.fid_usr_del = ud.id_usr " +
            "WHERE t.id_tp_link = " + key[0] + " AND t.id_ref = " + key[1] + " AND t.id_line = " + key[2] + " ";

        return sql;
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkLinkTypeId = 0;
        mnPkReferenceId = 0;
        mnPkMfgLineId = 0;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsLine = "";
    }

    @Override
    public void setPrimaryKey(java.lang.Object key) {
        mnPkLinkTypeId = ((int[]) key)[0];
        mnPkReferenceId = ((int[]) key)[1];
        mnPkMfgLineId = ((int[]) key)[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkLinkTypeId, mnPkReferenceId, mnPkMfgLineId };
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";

        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = query(pk);
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkLinkTypeId = resultSet.getInt("t.id_tp_link");
                mnPkReferenceId = resultSet.getInt("t.id_ref");
                mnPkMfgLineId = resultSet.getInt("t.id_line");
                mbIsDeleted = resultSet.getBoolean("t.b_del");
                mnFkUserNewId = resultSet.getInt("t.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("t.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("t.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("t.ts_new");
                mtUserEditTs = resultSet.getTimestamp("t.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("t.ts_del");

                msDbmsLine = resultSet.getString("l.line");

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
        CallableStatement callableStatement;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{" +
            " CALL mfgu_line_cfg_item_save(" +
                "?, ?, ?, ?, ?, " +
                "?, ?) }");
            callableStatement.setInt(nParam++, mnPkLinkTypeId);
            callableStatement.setInt(nParam++, mnPkReferenceId);
            callableStatement.setInt(nParam++, mnPkMfgLineId);
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
        return mtUserEditTs;
    }
}
