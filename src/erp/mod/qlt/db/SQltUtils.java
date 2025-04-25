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
import sa.lib.gui.SGuiClient;

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
    
    public static int obtainDatasheetTemplateByItemLink(Statement statement, int nItemId) throws SQLException {
        String sql = "";
        ResultSet resultSet = null;
        int idTemplate = 0;

        sql = "SELECT dtl.fk_tp_link, dtl.fk_datasheet_template " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.QLT_DATASHEET_TEMPLATE_LINK) + " AS dtl WHERE dtl.b_del = 0 AND dtl.b_valid = 1 AND (" +
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
                "ORDER BY dtl.fk_tp_link DESC ";

        resultSet = statement.getConnection().createStatement().executeQuery(sql);
        if (resultSet.next()) {
            idTemplate = resultSet.getInt("dtl.fk_datasheet_template");
        }

        return idTemplate;
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
        oNewAnalysisEntry.setFkUserNewId(oAnalysisEntry.getFkUserNewId());
        oNewAnalysisEntry.setFkUserEditId(oAnalysisEntry.getFkUserEditId());
        oNewAnalysisEntry.setFkUserDeleteId(oAnalysisEntry.getFkUserDeleteId());
        oNewAnalysisEntry.setUserNewTs(oAnalysisEntry.getUserNewTs());
        oNewAnalysisEntry.setUserEditTs(oAnalysisEntry.getUserEditTs());
        oNewAnalysisEntry.setUserDeleteTs(oAnalysisEntry.getUserDeleteTs());

        oNewAnalysisEntry.readAnalysisAuxData(statement);
        
        return oNewAnalysisEntry;
    }
}
