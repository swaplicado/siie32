/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Date;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataPriceListLink extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkPriceListId;
    protected int mnPkLinkTypeId;
    protected int mnPkReferenceId;

    protected java.lang.String msDbmsLinkType;
    protected java.lang.String msDbmsReference;

    protected int mnDbmsAuxSortingItem;

    public SDataPriceListLink() {
        super(SDataConstants.MKT_PLIST_ITEM);
        
        reset();
    }

    public void setPkPriceListId(int n) { mnPkPriceListId = n; }
    public void setPkLinkTypeId(int n) { mnPkLinkTypeId = n; }
    public void setPkReferenceId(int n) { mnPkReferenceId = n; }
    
    public void setDbmsLinkType(java.lang.String s) { msDbmsLinkType = s; }
    public void setDbmsReference(java.lang.String s) { msDbmsReference = s; }

    public int getPkPriceListId() { return mnPkPriceListId; }
    public int getPkLinkTypeId() { return mnPkLinkTypeId; }
    public int getPkReferenceId() { return mnPkReferenceId; }

    public void setDbmsAuxSortingItem(int n) { mnDbmsAuxSortingItem = n; }
    
    public java.lang.String getDbmsLinkType() { return msDbmsLinkType; }
    public java.lang.String getDbmsReference() { return msDbmsReference; }

    public int getDbmsAuxSortingItem() { return mnDbmsAuxSortingItem; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkPriceListId = ((int[]) pk)[0];
        mnPkLinkTypeId = ((int[]) pk)[1];
        mnPkReferenceId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkPriceListId, mnPkLinkTypeId, mnPkReferenceId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkPriceListId = 0;
        mnPkLinkTypeId = 0;
        mnPkReferenceId = 0;
        //mnDbmsAuxSortingItem = SDataConstants.UNDEFINED;
        
        msDbmsLinkType = "";
        msDbmsReference = "";
    }

    private java.lang.String query(java.lang.Object params) {
        int[] key = (int[]) params;
        String sql = "";

        sql = "SELECT pl.id_plist, pl.id_tp_link, pl.id_ref, " +
            "CASE pl.id_tp_link " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ALL + " THEN " +
                "('" + SDataConstantsSys.TXT_TRNS_TP_LINK_ALL + "') " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CT_ITEM + " THEN (" +
                "SELECT c.ct_item FROM erp.itms_ct_item c WHERE c.ct_idx = pl.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CL_ITEM + " THEN (" +
                "SELECT c.cl_item FROM erp.itms_cl_item c WHERE c.cl_idx = pl.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_TP_ITEM + " THEN (" +
                "SELECT tp.tp_item FROM erp.itms_tp_item tp WHERE tp.tp_idx = pl.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IFAM + " THEN (" +
                "SELECT ifam.ifam FROM erp.itmu_ifam ifam WHERE ifam.id_ifam = pl.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGRP + " THEN (" +
                "SELECT igrp.igrp FROM erp.itmu_igrp igrp WHERE igrp.id_igrp = pl.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGEN + " THEN (" +
                "SELECT CONCAT(igen.igen, ' (', igen.code, ')') AS igen FROM erp.itmu_igen igen WHERE igen.id_igen = pl.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_LINE + " THEN (" +
                "SELECT line.line FROM erp.itmu_line line WHERE line.id_line = pl.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_BRD + " THEN (" +
                "SELECT CONCAT(brd.brd, ' (', brd.code, ')') AS brd FROM erp.itmu_brd brd WHERE brd.id_brd = pl.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_MFR + " THEN (" +
                "SELECT CONCAT(mfr.mfr, ' (', mfr.code, ')') AS mfr FROM erp.itmu_mfr mfr WHERE mfr.id_mfr = pl.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ITEM + " THEN (" +
                "SELECT " + (mnDbmsAuxSortingItem == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "CONCAT(item.item_key, ' - ', item.item)" : "CONCAT(item.item, ' - ', item.item_key)") + " " +
                "FROM erp.itmu_item item WHERE item.id_item = pl.id_ref) " +
            "ELSE 'No existe tipo' " +
            "END AS ref, link.tp_link " +
            "FROM mkt_plist_item AS pl " +
            "INNER JOIN erp.trns_tp_link AS link ON pl.id_tp_link = link.id_tp_link " +
            "WHERE pl.id_plist = " + key[0] + " AND pl.id_tp_link = " + key[1] + " AND pl.id_ref = " + key[2] + " " +
            "ORDER BY link.id_tp_link ASC ";

        return sql;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
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
                mnPkPriceListId = resultSet.getInt("id_plist");
                mnPkLinkTypeId = resultSet.getInt("id_tp_link");
                mnPkReferenceId = resultSet.getInt("id_ref");

                msDbmsLinkType = resultSet.getString("tp_link");
                msDbmsReference = resultSet.getString("ref");

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
                    "{ CALL mkt_plist_link_save(" +
                    "?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkPriceListId);
            callableStatement.setInt(nParam++, mnPkLinkTypeId);
            callableStatement.setInt(nParam++, mnPkReferenceId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

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

    public int del(java.sql.Connection connection) {
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mkt_plist_link_del(" +
                    "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkPriceListId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            mbIsRegistryNew = false;
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
