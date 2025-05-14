/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.qlt.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.trn.db.SDbDpsEntryAnalysis;
import erp.mtrn.data.SDataDpsEntryAnalysis;
import erp.mtrn.data.STrnStockMove;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Uriel Castañeda, Edwin Carmona
 */
public abstract class SQltUtils {
    
    /**
     * Verifies if lot exists in quality for the given parameters
     * @param client
     * @param date of quality approval
     * @param idBizPartner Supplier of the item
     * @param stockMove move with lot information
     * @return true if the lot exists
     */
    public static boolean checkLotApproved(final SGuiClient client, final Date date, final int idBizPartner, final STrnStockMove stockMove) {
        boolean exists = false;        
        String sql = "";
        ResultSet resultSet = null;
        
        try {

            Statement statement = client.getSession().getStatement().getConnection().createStatement();
            
            sql = "SELECT id_lot_apr "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.QLT_LOT_APR) + " "
                + "WHERE " + "dt = '" + SLibUtils.DbmsDateFormatDate.format(date) + "' AND "
                + "fk_bp = " + idBizPartner + " AND "
                + "fk_item = " + stockMove.getPkItemId() + " AND "
                + "fk_unit = " + stockMove.getPkUnitId() + " AND "
                +  "lot = '" + stockMove.getAuxLot() + "' ";

            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
               exists = true;
            }
        }
        catch (Exception e) {
            SLibUtils.showException(SQltUtils.class.getName(), e);
        }
        return exists;
    }
    
    public static int obtainDatasheetTemplateByItemLink(Statement statement, final int nItemId, final int logTypeDeliveryId) {
        try {
            String sql = "";
            ResultSet resultSet = null;
            int idTemplate = 0;
            
            sql = "SELECT dtl.fk_tp_link, dtl.fk_datasheet_template " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.QLT_DATASHEET_TEMPLATE_LINK) + " AS dtl "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.QLT_DATASHEET_TEMPLATE) + " AS dtt ON dtl.fk_datasheet_template = dtt.id_datasheet_template "
                    + "WHERE dtl.b_del = 0 AND dtl.b_valid = 1 ";
            
            if (logTypeDeliveryId > 0) {
                sql += "AND dtt.fk_log_tp_dly_n = " + logTypeDeliveryId + " ";
            }
            
            sql += "AND (" +
                    "(dtl.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_ITEM + " AND dtl.fk_ref = " + nItemId + ") OR " +
                    "(dtl.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_MFR + " AND dtl.fk_ref IN (SELECT i.fid_mfr FROM erp.itmu_item AS i WHERE i.id_item = " + nItemId + ")) OR " +
                    "(dtl.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_BRD + " AND dtl.fk_ref IN (SELECT i.fid_brd FROM erp.itmu_item AS i WHERE i.id_item = " + nItemId + ")) OR " +
                    "(dtl.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_LINE + " AND dtl.fk_ref IN (SELECT i.fid_line_n FROM erp.itmu_item AS i WHERE i.id_item = " + nItemId + ")) OR " +
                    "(dtl.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_IGEN + " AND dtl.fk_ref IN (SELECT i.fid_igen FROM erp.itmu_item AS i WHERE i.id_item = " + nItemId + ")) OR " +
                    "(dtl.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_IGRP + " AND dtl.fk_ref IN (SELECT igen.fid_igrp FROM erp.itmu_igen AS igen INNER JOIN erp.itmu_item AS i ON igen.id_igen = i.fid_igen AND i.id_item = " + nItemId + ")) OR " +
                    "(dtl.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_IFAM + " AND dtl.fk_ref IN (SELECT igrp.fid_ifam FROM erp.itmu_igrp AS igrp INNER JOIN erp.itmu_igen AS igen ON igrp.id_igrp = igen.fid_igrp INNER JOIN erp.itmu_item AS i ON igen.id_igen = i.fid_igen AND i.id_item = " + nItemId + ")) OR " +
                    "(dtl.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_TP_ITEM + " AND dtl.fk_ref IN (SELECT itp.tp_idx FROM erp.itmu_igen AS igen INNER JOIN erp.itmu_item AS i ON igen.id_igen = i.fid_igen AND i.id_item = " + nItemId + " INNER JOIN erp.itms_tp_item AS itp ON igen.fid_ct_item = itp.id_ct_item AND igen.fid_cl_item = itp.id_cl_item AND igen.fid_tp_item = itp.id_tp_item)) OR " +
                    "(dtl.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_CL_ITEM + " AND dtl.fk_ref IN (SELECT icl.cl_idx FROM erp.itmu_igen AS igen INNER JOIN erp.itmu_item AS i ON igen.id_igen = i.fid_igen AND i.id_item = " + nItemId + " INNER JOIN erp.itms_cl_item AS icl ON igen.fid_ct_item = icl.id_ct_item AND igen.fid_cl_item = icl.id_cl_item)) OR " +
                    "(dtl.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_CT_ITEM + " AND dtl.fk_ref IN (SELECT ict.ct_idx FROM erp.itmu_igen AS igen INNER JOIN erp.itmu_item AS i ON igen.id_igen = i.fid_igen AND i.id_item = " + nItemId + " INNER JOIN erp.itms_ct_item AS ict ON igen.fid_ct_item = ict.id_ct_item)) OR " +
                    "(dtl.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_ALL + " AND dtl.fk_ref = " + SLibConstants.UNDEFINED + ")) " +
                    "ORDER BY dtl.fk_tp_link DESC, dtt.template_standard DESC, dtl.ts_usr_upd DESC  ";
            
            resultSet = statement.getConnection().createStatement().executeQuery(sql);
            if (resultSet.next()) {
                idTemplate = resultSet.getInt("dtl.fk_datasheet_template");
            }
            
            return idTemplate;
        }
        catch (SQLException ex) {
            Logger.getLogger(SQltUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }

    public static boolean mustBeConfigured(Statement statement, int nItemId) throws SQLException {
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT qcr.id_qlty_config_required " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.QLT_QLTY_CONFIG_REQUIRED) + " AS qcr WHERE qcr.b_del = 0 AND (" +
                "(qcr.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_ITEM + " AND qcr.fk_ref = " + nItemId + ") OR " +
                "(qcr.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_MFR + " AND qcr.fk_ref IN (SELECT i.fid_mfr FROM erp.itmu_item AS i WHERE i.id_item = " + nItemId + ")) OR " +
                "(qcr.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_BRD + " AND qcr.fk_ref IN (SELECT i.fid_brd FROM erp.itmu_item AS i WHERE i.id_item = " + nItemId + ")) OR " +
                "(qcr.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_LINE + " AND qcr.fk_ref IN (SELECT i.fid_line_n FROM erp.itmu_item AS i WHERE i.id_item = " + nItemId + ")) OR " +
                "(qcr.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_IGEN + " AND qcr.fk_ref IN (SELECT i.fid_igen FROM erp.itmu_item AS i WHERE i.id_item = " + nItemId + ")) OR " +
                "(qcr.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_IGRP + " AND qcr.fk_ref IN (SELECT igen.fid_igrp FROM erp.itmu_igen AS igen INNER JOIN erp.itmu_item AS i ON igen.id_igen = i.fid_igen AND i.id_item = " + nItemId + ")) OR " +
                "(qcr.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_IFAM + " AND qcr.fk_ref IN (SELECT igrp.fid_ifam FROM erp.itmu_igrp AS igrp INNER JOIN erp.itmu_igen AS igen ON igrp.id_igrp = igen.fid_igrp INNER JOIN erp.itmu_item AS i ON igen.id_igen = i.fid_igen AND i.id_item = " + nItemId + ")) OR " +
                "(qcr.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_TP_ITEM + " AND qcr.fk_ref IN (SELECT itp.tp_idx FROM erp.itmu_igen AS igen INNER JOIN erp.itmu_item AS i ON igen.id_igen = i.fid_igen AND i.id_item = " + nItemId + " INNER JOIN erp.itms_tp_item AS itp ON igen.fid_ct_item = itp.id_ct_item AND igen.fid_cl_item = itp.id_cl_item AND igen.fid_tp_item = itp.id_tp_item)) OR " +
                "(qcr.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_CL_ITEM + " AND qcr.fk_ref IN (SELECT icl.cl_idx FROM erp.itmu_igen AS igen INNER JOIN erp.itmu_item AS i ON igen.id_igen = i.fid_igen AND i.id_item = " + nItemId + " INNER JOIN erp.itms_cl_item AS icl ON igen.fid_ct_item = icl.id_ct_item AND igen.fid_cl_item = icl.id_cl_item)) OR " +
                "(qcr.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_CT_ITEM + " AND qcr.fk_ref IN (SELECT ict.ct_idx FROM erp.itmu_igen AS igen INNER JOIN erp.itmu_item AS i ON igen.id_igen = i.fid_igen AND i.id_item = " + nItemId + " INNER JOIN erp.itms_ct_item AS ict ON igen.fid_ct_item = ict.id_ct_item)) OR " +
                "(qcr.fk_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_ALL + " AND qcr.fk_ref = " + SLibConstants.UNDEFINED + ")) " +
                "ORDER BY qcr.fk_tp_link DESC ";

        resultSet = statement.getConnection().createStatement().executeQuery(sql);
        if (resultSet.next()) {
            Logger.getLogger(SQltUtils.class.getName()).log(Level.INFO, "\u00cdtem configurado: {0} {1}", new Object[]{nItemId, resultSet.getInt("qcr.id_qlty_config_required")});
            return true;
        }

        return false;
    }

    public static SDataDpsEntryAnalysis newAnalysisEntryToOldAnalysisEntry(java.sql.Statement statement, SDbDpsEntryAnalysis oAnalysisEntry) {
        SDataDpsEntryAnalysis oNewAnalysisEntry = new SDataDpsEntryAnalysis();

        oNewAnalysisEntry.setPkEntryAnalysisId(oAnalysisEntry.getPkEntryAnalysisId());
        oNewAnalysisEntry.setSortPosition(oAnalysisEntry.getSortPos());
        oNewAnalysisEntry.setOriginalSpecification(oAnalysisEntry.getOriginalSpecification());
        oNewAnalysisEntry.setSpecification(oAnalysisEntry.getSpecification());
        oNewAnalysisEntry.setOriginalMinValue(oAnalysisEntry.getOriginalMinValue());
        oNewAnalysisEntry.setOriginalMaxValue(oAnalysisEntry.getOriginalMaxValue());
        oNewAnalysisEntry.setMinValue(oAnalysisEntry.getMinValue());
        oNewAnalysisEntry.setMaxValue(oAnalysisEntry.getMaxValue());
        oNewAnalysisEntry.setIsMin(oAnalysisEntry.isMin());
        oNewAnalysisEntry.setIsMax(oAnalysisEntry.isMax());
        oNewAnalysisEntry.setIsRequired(oAnalysisEntry.isRequired());
        oNewAnalysisEntry.setIsForDps(oAnalysisEntry.isForDps());
        oNewAnalysisEntry.setIsForCoA(oAnalysisEntry.isForCoA());
        oNewAnalysisEntry.setWasAdded(oAnalysisEntry.wasAdded());
        oNewAnalysisEntry.setIsDeleted(oAnalysisEntry.isDeleted());
        oNewAnalysisEntry.setFkDpsYearId(oAnalysisEntry.getFkDpsYearId());
        oNewAnalysisEntry.setFkDpsDocId(oAnalysisEntry.getFkDpsDocId());
        oNewAnalysisEntry.setFkDpsEtyId(oAnalysisEntry.getFkDpsEntryId());
        oNewAnalysisEntry.setFkAnalysisId(oAnalysisEntry.getFkAnalysisId());
        oNewAnalysisEntry.setFkItemId(oAnalysisEntry.getFkItemId());
        oNewAnalysisEntry.setFkDatasheetTemplateId_n(oAnalysisEntry.getFkDatasheetTemplateId_n());
        oNewAnalysisEntry.setFkUserNewId(oAnalysisEntry.getFkUserNewId());
        oNewAnalysisEntry.setFkUserEditId(oAnalysisEntry.getFkUserEditId());
        oNewAnalysisEntry.setFkUserDeleteId(oAnalysisEntry.getFkUserDeleteId());
        oNewAnalysisEntry.setUserNewTs(oAnalysisEntry.getUserNewTs());
        oNewAnalysisEntry.setUserEditTs(oAnalysisEntry.getUserEditTs());
        oNewAnalysisEntry.setUserDeleteTs(oAnalysisEntry.getUserDeleteTs());

        oNewAnalysisEntry.readAnalysisAuxData(statement);
        
        return oNewAnalysisEntry;
    }
    
    public static SDbCoAResult getCoAResults(SGuiSession session, final int idYear, final int idDoc, final int idEntry) {
        String sql = "SELECT id_coa_result FROM " + 
                    SModConsts.TablesMap.get(SModConsts.QLT_COA_RESULT) + 
                    " WHERE fk_dps_year = " + idYear + 
                    " AND fk_dps_doc = " + idDoc +
                    " AND fk_dps_ety = " + idEntry +
                    " AND b_del = 0 ORDER BY ts_usr_upd DESC" +
                    " LIMIT 1;";
                    
        ResultSet coaResultSet = null;
        SDbCoAResult oCoAResult = null;
        try {
            coaResultSet = session.getStatement().getConnection().createStatement().executeQuery(sql);
            if (coaResultSet.next()) {
                oCoAResult = new SDbCoAResult();
                oCoAResult.read(session, new int[] { coaResultSet.getInt("id_coa_result") });
                return oCoAResult;
            }
            
            String searchContractMysql = "SELECT  " +
                "    s.id_src_year, s.id_src_doc, s.id_src_ety " +
                "FROM " +
                "    erp_aeth.trn_dps_dps_supply s " +
                "        JOIN " +
                "    (SELECT  " +
                "        id_src_year, id_src_doc, id_src_ety " +
                "    FROM " +
                "        erp_aeth.trn_dps_dps_supply " +
                "    WHERE " +
                "        id_des_year = " + idYear + " AND id_des_doc = " + idDoc + " " +
                "            AND id_des_ety = " + idEntry + " " +
                "    LIMIT 1) ref ON s.id_des_year = ref.id_src_year " +
                "        AND s.id_des_doc = ref.id_src_doc " +
                "        AND s.id_des_ety = ref.id_src_ety LIMIT 1;";
            
            int idContractYear = 0;
            int idContractDoc = 0;
            int idContractEntry = 0;
            ResultSet resultSetContract = session.getStatement().getConnection().createStatement().executeQuery(searchContractMysql);
            if (resultSetContract.next()) {
                idContractYear = resultSetContract.getInt("s.id_src_year");
                idContractDoc = resultSetContract.getInt("s.id_src_doc");
                idContractEntry = resultSetContract.getInt("s.id_src_ety");
            }
            else {
                return null;
            }

            String sqlEtyAnalysis = "SELECT * FROM " +
                    SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY_ANALYSIS) +
                    " WHERE fid_dps_year = " + idContractYear +
                    " AND fid_dps_doc = " + idContractDoc +
                    " AND fid_dps_ety = " + idContractEntry +
                    " AND b_del = 0 ORDER BY sort_pos ASC;";

            oCoAResult = new SDbCoAResult();
            
            oCoAResult.setDate(new Date());
            oCoAResult.setCaptureStatus("CAPTURA");
            oCoAResult.setFkDatasheetTemplateId_n(0);
            oCoAResult.setFkSourceDpsYearId_n(idContractYear);
            oCoAResult.setFkSourceDpsDocId_n(idContractDoc);
            oCoAResult.setFkSourceDpsEtyId_n(idContractEntry);
            oCoAResult.setFkDpsYearId(idYear);
            oCoAResult.setFkDpsDocId(idDoc);
            oCoAResult.setFkDpsEtyId(idEntry);

            ResultSet resultSetEtyAnalysis = session.getStatement().getConnection().createStatement().executeQuery(sqlEtyAnalysis);
            while (resultSetEtyAnalysis.next()) {
                SDbCoAResultRow coaResultRow = new SDbCoAResultRow();

                coaResultRow.setPkCoAResultId(oCoAResult.getPkCoAResultId());
                coaResultRow.setSortPosition(resultSetEtyAnalysis.getInt("sort_pos"));
                coaResultRow.setSpecification(resultSetEtyAnalysis.getString("specification"));
                coaResultRow.setAnalysisResult("");
                coaResultRow.setMinValue(resultSetEtyAnalysis.getString("min_value"));
                coaResultRow.setMaxValue(resultSetEtyAnalysis.getString("max_value"));
                coaResultRow.setMin(resultSetEtyAnalysis.getBoolean("b_min"));
                coaResultRow.setMax(resultSetEtyAnalysis.getBoolean("b_max"));
                coaResultRow.setForDps(resultSetEtyAnalysis.getBoolean("b_dps"));
                coaResultRow.setCoA(resultSetEtyAnalysis.getBoolean("b_coa"));
                coaResultRow.setFkAnalysisId(resultSetEtyAnalysis.getInt("fid_analysis"));
                if (resultSetEtyAnalysis.getInt("fid_dtsht_tmplte_n") > 0) {
                    oCoAResult.setFkDatasheetTemplateId_n(resultSetEtyAnalysis.getInt("fid_dtsht_tmplte_n"));
                    coaResultRow.setAuxFkTemplateId_n(oCoAResult.getFkDatasheetTemplateId_n());
                }
                coaResultRow.readAuxData(session);

                oCoAResult.getCoAResultRows().add(coaResultRow);
            }

            if (oCoAResult.getFkDatasheetTemplateId_n() > 0) {
                SDbDatasheetTemplate oDatasheetTemplate = new SDbDatasheetTemplate();
                oDatasheetTemplate.setPkDatasheetTemplateId(oCoAResult.getFkDatasheetTemplateId_n());
                oDatasheetTemplate.read(session, new int[] { oCoAResult.getFkDatasheetTemplateId_n() });
                if (oDatasheetTemplate.getQueryResultId() == SDbConsts.READ_OK) {
                    oCoAResult.setAuxDatasheetTemplate(oDatasheetTemplate);
                }
                else {
                    oCoAResult.setAuxDatasheetTemplate(null);
                }
            }

            return oCoAResult;
        }
        catch (SQLException e) {
            SLibUtils.showException(SQltUtils.class.getName(), e);
        }
        catch (Exception ex) {
            Logger.getLogger(SQltUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
