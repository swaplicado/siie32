/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataCommissionsSalesAgentTypes extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkSalesAgentTypeId;
    protected int mnPkLinkTypeId;
    protected int mnPkReferenceId;
    protected java.util.Date mtPkDateStartId;
    protected double mdAgentPercentage;
    protected double mdAgentValueUnitary;
    protected double mdAgentValue;
    protected double mdSupervisorPercentage;
    protected double mdSupervisorValueUnitary;
    protected double mdSupervisorValue;
    protected boolean mbIsDeleted;
    protected int mnFkCommissionsTypeId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsSalesAgentType;
    protected java.lang.String msDbmsLinkType;
    protected java.lang.String msDbmsReference;

    protected int mnDbmsAuxSortingItem;
    
    protected java.util.Vector<erp.mmkt.data.SDataCommissionsSalesAgentType> mvDbmsCommissionsSalesAgentType;

    public SDataCommissionsSalesAgentTypes() {
        super(SDataConstants.MKTX_COMMS_SAL_AGT_TPS);

        mvDbmsCommissionsSalesAgentType = new Vector<SDataCommissionsSalesAgentType>();
        
        reset();        
    }

    public void setPkSalesAgentTypeId(int n) { mnPkSalesAgentTypeId = n; }
    public void setPkLinkTypeId(int n) { mnPkLinkTypeId = n; }
    public void setPkReferenceId(int n) { mnPkReferenceId = n; }
    public void setPkDateStartId(java.util.Date t) { mtPkDateStartId = t; }
    public void setAgentPercentage(double d) { mdAgentPercentage = d; }
    public void setAgentValueUnitary(double d) { mdAgentValueUnitary = d; }
    public void setAgentValue(double d) { mdAgentValue = d; }
    public void setSupervisorPercentage(double d) { mdSupervisorPercentage = d; }
    public void setSupervisorValueUnitary(double d) { mdSupervisorValueUnitary = d; }
    public void setSupervisorValue(double d) { mdSupervisorValue = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkCommissionsTypeId(int n) { mnFkCommissionsTypeId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }
    
    public int getPkSalesAgentTypeId() { return mnPkSalesAgentTypeId; }
    public int getPkLinkTypeId() { return mnPkLinkTypeId; }
    public int getPkReferenceId() { return mnPkReferenceId; }
    public java.util.Date getPkDateStartId() { return mtPkDateStartId; }
    public double getAgentPercentage() { return mdAgentPercentage; }
    public double getAgentValueUnitary() { return mdAgentValueUnitary; }
    public double getAgentValue() { return mdAgentValue; }
    public double getSupervisorPercentage() { return mdSupervisorPercentage; }
    public double getSupervisorValueUnitary() { return mdSupervisorValueUnitary; }
    public double getSupervisorValue() { return mdSupervisorValue; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkCommissionsTypeId() { return mnFkCommissionsTypeId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsLinkType(java.lang.String s) { msDbmsLinkType = s; }
    public void setDbmsReference(java.lang.String s) { msDbmsReference = s; }
    public void setDbmsAuxSortingItem(int n) { mnDbmsAuxSortingItem = n; }

    public java.lang.String getDbmsSalesAgentType() { return msDbmsSalesAgentType; }
    public java.lang.String getDbmsLinkType() { return msDbmsLinkType; }
    public java.lang.String getDbmsReference() { return msDbmsReference; }
    public int getDbmsAuxSortingItem() { return mnDbmsAuxSortingItem; }

    public java.util.Vector<erp.mmkt.data.SDataCommissionsSalesAgentType> getDbmsCommisionsSalesAgentType() { return mvDbmsCommissionsSalesAgentType; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkSalesAgentTypeId = ((int[]) pk)[0];
        mnPkLinkTypeId = ((int[]) pk)[1];
        mnPkReferenceId = ((int[]) pk)[2];
        mtPkDateStartId = ((java.util.Date[]) pk)[3];        
    }

    @Override
    public java.lang.Object getPrimaryKey() {        
        return new Object[] { mnPkSalesAgentTypeId, mnPkLinkTypeId, mnPkReferenceId, mtPkDateStartId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkSalesAgentTypeId = 0;
        mnPkLinkTypeId = 0;
        mnPkReferenceId = 0;
        mtPkDateStartId = null;
        mdAgentPercentage = 0;
        mdAgentValueUnitary = 0;
        mdAgentValue = 0;
        mdSupervisorPercentage = 0;
        mdSupervisorValueUnitary = 0;
        mdSupervisorValue = 0;
        mbIsDeleted = false;
        mnFkCommissionsTypeId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
        //mnDbmsAuxSortingItem = SDataConstants.UNDEFINED;

        msDbmsSalesAgentType = "";
        msDbmsLinkType = "";
        msDbmsReference = "";

        mvDbmsCommissionsSalesAgentType.removeAllElements();
    }

    private java.lang.String query(int nType, java.lang.Object params) {
        Object[] key = (Object[]) params;
        String sql = "";

        sql = "SELECT tp.tp_sal_agt, cm.*, " +
            "CASE cm.id_tp_link " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ALL + " THEN " +
            "('" + SDataConstantsSys.TXT_TRNS_TP_LINK_ALL + "') " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CT_ITEM + " THEN (" +
            "SELECT c.ct_item FROM erp.itms_ct_item c WHERE c.ct_idx = cm.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CL_ITEM + " THEN (" +
            "SELECT c.cl_item FROM erp.itms_cl_item c WHERE c.cl_idx = cm.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_TP_ITEM + " THEN (" +
            "SELECT tp.tp_item FROM erp.itms_tp_item tp WHERE tp.tp_idx = cm.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IFAM + " THEN (" +
            "SELECT ifam.ifam FROM erp.itmu_ifam ifam WHERE ifam.id_ifam = cm.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGRP + " THEN (" +
            "SELECT igrp.igrp FROM erp.itmu_igrp igrp WHERE igrp.id_igrp = cm.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGEN + " THEN (" +
            "SELECT CONCAT(igen.igen, ' (', igen.code, ')') AS igen FROM erp.itmu_igen igen WHERE igen.id_igen = cm.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_LINE + " THEN (" +
            "SELECT line.line FROM erp.itmu_line line WHERE line.id_line = cm.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_BRD + " THEN (" +
            "SELECT CONCAT(brd.brd, ' (', brd.code, ')') AS brd FROM erp.itmu_brd brd WHERE brd.id_brd = cm.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_MFR + " THEN (" +
            "SELECT CONCAT(mfr.mfr, ' (', mfr.code, ')') AS mfr FROM erp.itmu_mfr mfr WHERE mfr.id_mfr = cm.id_ref) " +
            "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ITEM + " THEN (" +
            "SELECT " + (mnDbmsAuxSortingItem == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "CONCAT(item.item_key, ' - ', item.item)" : "CONCAT(item.item, ' - ', item.item_key)") + " " +
            "FROM erp.itmu_item item WHERE item.id_item = cm.id_ref) " +
            "ELSE 'No existe tipo' " +
            "END AS ref, link.tp_link " +
            "FROM mkt_comms_sal_agt_tp AS cm " +
            "INNER JOIN erp.trns_tp_link AS link ON cm.id_tp_link = link.id_tp_link " +
            "INNER JOIN mktu_tp_sal_agt AS tp ON cm.id_tp_sal_agt = tp.id_tp_sal_agt " +
            "WHERE cm.id_tp_link = " + ((Integer) key[1]) + " AND cm.id_ref = " + ((Integer) key[2]) + " AND cm.id_dt_start = '" + ((String) key[3])  + "' " +
            (nType == 1 ? "GROUP BY cm.id_tp_link, cm.id_ref, cm.id_dt_start " : "") +
            "ORDER BY link.id_tp_link ASC ";

        return sql;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        Object[] key = (Object[]) pk;
        String sql;
        ResultSet resultSet = null;
        Statement statementAux = null;

        SDataCommissionsSalesAgentType oCommisionsSalesAgentType = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {            
            sql = query(1, pk);
            resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkSalesAgentTypeId = resultSet.getInt("cm.id_tp_sal_agt");
                mnPkLinkTypeId = resultSet.getInt("cm.id_tp_link");
                mnPkReferenceId = resultSet.getInt("cm.id_ref");
                mtPkDateStartId = resultSet.getDate("cm.id_dt_start");
                mdAgentPercentage = resultSet.getDouble("cm.agt_per");
                mdAgentValueUnitary = resultSet.getDouble("cm.agt_val_u");
                mdAgentValue = resultSet.getDouble("cm.agt_val");
                mdSupervisorPercentage = resultSet.getDouble("cm.sup_per");
                mdSupervisorValueUnitary = resultSet.getDouble("cm.sup_val_u");
                mdSupervisorValue = resultSet.getDouble("cm.sup_val");
                mbIsDeleted = resultSet.getBoolean("cm.b_del");
                mnFkCommissionsTypeId = resultSet.getInt("cm.fid_tp_comms");
                mnFkUserNewId = resultSet.getInt("cm.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("cm.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("cm.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("cm.ts_new");
                mtUserEditTs = resultSet.getTimestamp("cm.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("cm.ts_del");

                msDbmsSalesAgentType = resultSet.getString("tp_sal_agt");
                msDbmsLinkType = resultSet.getString("tp_link");
                msDbmsReference = resultSet.getString("ref");

                // Read sales agent types:

                statementAux = statement.getConnection().createStatement();
                mvDbmsCommissionsSalesAgentType.removeAllElements();
                sql = query(2, pk);
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    oCommisionsSalesAgentType = new SDataCommissionsSalesAgentType();
                    if (oCommisionsSalesAgentType.read(new Object[] { resultSet.getInt("id_tp_sal_agt"), resultSet.getInt("id_tp_link"), resultSet.getInt("id_ref"), resultSet.getDate("id_dt_start").toString() }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsCommissionsSalesAgentType.add(oCommisionsSalesAgentType);
                    }
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
        CallableStatement callableStatement = null;

        SDataCommissionsSalesAgentType oCommissionsSalesAgentType = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            for (int i=0; i<mvDbmsCommissionsSalesAgentType.size(); i++) {
                nParam = 1;
                oCommissionsSalesAgentType = mvDbmsCommissionsSalesAgentType.get(i);
                callableStatement = connection.prepareCall(
                        "{ CALL mkt_comms_sal_agt_tp_save(" +
                        "?, ?, ?, ?, ?, " +
                        "?, ?, ?, ?, ?, " +
                        "?, ?, ?, ?, ?) }");
                callableStatement.setInt(nParam++, oCommissionsSalesAgentType.getPkSalesAgentTypeId());
                callableStatement.setInt(nParam++, oCommissionsSalesAgentType.getPkLinkTypeId());
                callableStatement.setInt(nParam++, oCommissionsSalesAgentType.getPkReferenceId());
                callableStatement.setDate(nParam++, new java.sql.Date(oCommissionsSalesAgentType.getPkDateStartId().getTime()));
                callableStatement.setDouble(nParam++, oCommissionsSalesAgentType.getAgentPercentage());
                callableStatement.setDouble(nParam++, oCommissionsSalesAgentType.getAgentValueUnitary());
                callableStatement.setDouble(nParam++, oCommissionsSalesAgentType.getAgentValue());
                callableStatement.setDouble(nParam++, oCommissionsSalesAgentType.getSupervisorPercentage());
                callableStatement.setDouble(nParam++, oCommissionsSalesAgentType.getSupervisorValueUnitary());
                callableStatement.setDouble(nParam++, oCommissionsSalesAgentType.getSupervisorValue());
                callableStatement.setBoolean(nParam++, oCommissionsSalesAgentType.getIsDeleted());
                callableStatement.setInt(nParam++, oCommissionsSalesAgentType.getFkCommissionsTypeId());
                callableStatement.setInt(nParam++, mbIsRegistryNew ? oCommissionsSalesAgentType.getFkUserNewId() : oCommissionsSalesAgentType.getFkUserEditId());
                callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                mnDbmsErrorId = callableStatement.getInt(nParam - 2);
                msDbmsError = callableStatement.getString(nParam - 1);

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

    public int del(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        /*
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
        */

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}
